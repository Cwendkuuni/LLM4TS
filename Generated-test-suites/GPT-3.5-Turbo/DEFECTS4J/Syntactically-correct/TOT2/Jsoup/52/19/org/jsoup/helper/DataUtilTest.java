import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {

    @Test
    public void testLoadFile() {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        try {
            Document doc = DataUtil.load(file, charsetName, baseUri);
            assertNotNull(doc);
            // Add more assertions based on the expected behavior
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoadInputStream() {
        InputStream inputStream = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        try {
            Document doc = DataUtil.load(inputStream, charsetName, baseUri);
            assertNotNull(doc);
            // Add more assertions based on the expected behavior
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCrossStreams() {
        InputStream in = new ByteArrayInputStream("test".getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            DataUtil.crossStreams(in, out);
            assertEquals("test", out.toString());
            // Add more assertions based on the expected behavior
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Include more test methods for other public methods in DataUtil class
    // Add test cases for edge cases and exception scenarios

}