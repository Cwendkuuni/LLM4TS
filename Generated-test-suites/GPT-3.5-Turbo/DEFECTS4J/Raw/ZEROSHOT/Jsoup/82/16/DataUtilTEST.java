import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class DataUtilTest {

    @Test
    public void testLoadFile() {
        try {
            File file = new File("test.html");
            Document document = DataUtil.load(file, "UTF-8", "");
            Assert.assertNotNull(document);
            // Add more assertions as needed
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoadInputStream() {
        try {
            InputStream inputStream = getClass().getResourceAsStream("test.html");
            Document document = DataUtil.load(inputStream, "UTF-8", "");
            Assert.assertNotNull(document);
            // Add more assertions as needed
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testParseInputStream() {
        try {
            InputStream inputStream = getClass().getResourceAsStream("test.html");
            Document document = DataUtil.parseInputStream(inputStream, "UTF-8", "", Parser.htmlParser());
            Assert.assertNotNull(document);
            // Add more assertions as needed
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Add more test methods for other DataUtil class methods

}