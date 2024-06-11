import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class DataUtilTest {

    @Test
    public void testLoadFromFile() {
        File file = new File("test.html");
        try {
            Document doc = DataUtil.load(file, "UTF-8", "https://www.example.com");
            assertNotNull(doc);
            // Add more assertions as needed for this method
        } catch (IOException e) {
            fail("IOException should not be thrown here");
        }
    }

    @Test
    public void testLoadFromInputStream() {
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        try {
            Document doc = DataUtil.load(inputStream, "UTF-8", "https://www.example.com");
            assertNotNull(doc);
            // Add more assertions as needed for this method
        } catch (IOException e) {
            fail("IOException should not be thrown here");
        }
    }

    @Test
    public void testLoadFromInputStreamWithParser() {
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        try {
            Document doc = DataUtil.load(inputStream, "UTF-8", "https://www.example.com", Parser.xmlParser());
            assertNotNull(doc);
            // Add more assertions as needed for this method
        } catch (IOException e) {
            fail("IOException should not be thrown here");
        }
    }

    // Add more test cases for other public methods as needed

}