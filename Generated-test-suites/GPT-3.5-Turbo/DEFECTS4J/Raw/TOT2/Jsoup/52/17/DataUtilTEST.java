import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

public class DataUtilTest {

    @Test
    public void testLoadFile() {
        // Typical use case
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        try {
            Document doc = DataUtil.load(file, charsetName, baseUri);
            assertNotNull(doc);
        } catch (IOException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testLoadInputStream() {
        // Typical use case
        InputStream in = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        try {
            Document doc = DataUtil.load(in, charsetName, baseUri);
            assertNotNull(doc);
        } catch (IOException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testLoadWithParser() {
        // Typical use case
        InputStream in = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Parser parser = Parser.xmlParser();
        try {
            Document doc = DataUtil.load(in, charsetName, baseUri, parser);
            assertNotNull(doc);
        } catch (IOException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testCrossStreams() {
        // Typical use case
        InputStream in = new FileInputStream("input.txt");
        OutputStream out = new ByteArrayOutputStream();
        try {
            DataUtil.crossStreams(in, out);
            // Assert no exception thrown
        } catch (IOException e) {
            fail("Exception thrown");
        }
    }

    // Add more test cases for other methods here

}