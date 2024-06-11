import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

public class DataUtilTest {

    @Test
    public void testLoadFromFile() {
        File file = new File("test.html");
        try {
            Document doc = DataUtil.load(file, "UTF-8", "https://www.example.com");
            assertNotNull(doc);
            // Add more assertions here if needed
        } catch (IOException e) {
            fail("IOException should not be thrown here");
        }
    }

    @Test
    public void testLoadFromInputStream() {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        try {
            Document doc = DataUtil.load(inputStream, "UTF-8", "https://www.example.com");
            assertNotNull(doc);
            // Add more assertions here if needed
        } catch (IOException e) {
            fail("IOException should not be thrown here");
        }
    }

    @Test
    public void testLoadFromInputStreamWithParser() {
        InputStream inputStream = getClass().getResourceAsStream("test.xml");
        try {
            Document doc = DataUtil.load(inputStream, "UTF-8", "https://www.example.com", Parser.xmlParser());
            assertNotNull(doc);
            // Add more assertions here if needed
        } catch (IOException e) {
            fail("IOException should not be thrown here");
        }
    }

    // Add more test cases for other methods as needed

}