import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.TakesScreenshot;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.support.ui.*;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static java.lang.Thread.sleep;

public class NCTest {

    private String location;
    private String keyword;


    @Test
    public void chromeTest() throws IOException, ParserConfigurationException, SAXException, InterruptedException {
        readParameters();
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        Wait<WebDriver> wait = new WebDriverWait(driver, 5 ,1000);
        driver.navigate().to("https://www.google.com/");
        driver.findElement(By.id("lst-ib")).sendKeys("Netcracker - Open Positions");
        driver.findElement(By.name("btnK")).submit();

        driver.findElement(By.linkText("Netcracker - Open Positions")).click();

        driver.findElement(By.id("keyword")).sendKeys(keyword);

        sleep(2000);
        List<WebElement> elements = driver.findElements(By.cssSelector(".active >.info"));
        for (WebElement item: elements){
            if (!item.findElements(By.xpath(".//p[contains(text(), '"+location+"')]")).isEmpty()){

                item.findElement(By.cssSelector("a")).click();
                break;
            }
        }
        File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(scrFile, new File(new SimpleDateFormat("dd-MM-yyyy HH-mm-ss").format(new Date()) +".png"));
    }

    private void readParameters () throws ParserConfigurationException, IOException, SAXException {
        File fXmlFile = new File("SearchParams.xml");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(fXmlFile);

        location = doc.getElementsByTagName("location").item(0).getTextContent();
        keyword = doc.getElementsByTagName("keyword").item(0).getTextContent();
        System.out.println(location + " " + keyword);
    }
}
