import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;

public class DataUtilTest {

    @Test
    public void testLoadFile() {
        File file = new File("testfile.html");
        try {
            Document document = DataUtil.load(file, "UTF-8", "http://example.com");
            assertNotNull(document);
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testLoadInputStream() {
        String htmlString = "<html><body><p>Hello, World!</p></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlString.getBytes());
        try {
            Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com");
            assertNotNull(document);
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testLoadInputStreamWithParser() {
        String htmlString = "<html><body><p>Hello, World!</p></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlString.getBytes());
        try {
            Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com", Parser.htmlParser());
            assertNotNull(document);
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    // Additional test cases for edge cases and error scenarios can be added here

}