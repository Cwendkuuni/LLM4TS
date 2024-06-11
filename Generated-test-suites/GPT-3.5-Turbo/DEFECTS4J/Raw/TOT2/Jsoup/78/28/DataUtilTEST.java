import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import static org.junit.Assert.*;

public class DataUtilTest {

    @Test
    public void testLoadFile() {
        try {
            Document doc = DataUtil.load(new File("test.html"), "UTF-8", "https://example.com");
            assertNotNull(doc);
            // Add more assertions as needed
        } catch (IOException e) {
            fail("Exception thrown while loading file");
        }
    }

    @Test
    public void testLoadInputStream() {
        try {
            Document doc = DataUtil.load(new FileInputStream("test.html"), "UTF-8", "https://example.com");
            assertNotNull(doc);
            // Add more assertions as needed
        } catch (IOException e) {
            fail("Exception thrown while loading input stream");
        }
    }

    @Test
    public void testLoadInputStreamWithCustomParser() {
        try {
            Document doc = DataUtil.load(new FileInputStream("test.html"), "UTF-8", "https://example.com", Parser.xmlParser());
            assertNotNull(doc);
            // Add more assertions as needed
        } catch (IOException e) {
            fail("Exception thrown while loading input stream with custom parser");
        }
    }

    @Test
    public void testCrossStreams() {
        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            DataUtil.crossStreams(new ByteArrayInputStream("test".getBytes()), output);
            assertEquals("test", output.toString());
            // Add more assertions as needed
        } catch (IOException e) {
            fail("Exception thrown while crossing streams");
        }
    }

    // Add more test methods for other public methods in DataUtil class
    
}