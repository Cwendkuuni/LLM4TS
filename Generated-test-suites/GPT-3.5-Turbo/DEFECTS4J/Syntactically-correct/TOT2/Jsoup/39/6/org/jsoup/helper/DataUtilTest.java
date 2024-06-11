import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class DataUtilTest {

    @Test
    public void testLoadFile() {
        try {
            Document doc = DataUtil.load(new File("test.html"), "UTF-8", "http://example.com");
            assertNotNull(doc);
            // Add more assertions here
        } catch (IOException e) {
            // Handle exception
        }
    }

    @Test
    public void testLoadInputStream() {
        try {
            InputStream in = new FileInputStream(new File("test.html"));
            Document doc = DataUtil.load(in, "UTF-8", "http://example.com");
            assertNotNull(doc);
            // Add more assertions here
        } catch (IOException e) {
            // Handle exception
        }
    }

    @Test
    public void testLoadInputStreamWithParser() {
        try {
            InputStream in = new FileInputStream(new File("test.html"));
            Document doc = DataUtil.load(in, "UTF-8", "http://example.com", Parser.htmlParser());
            assertNotNull(doc);
            // Add more assertions here
        } catch (IOException e) {
            // Handle exception
        }
    }

    // Add more test cases for other public methods in DataUtil class

}