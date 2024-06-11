import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import static org.junit.Assert.*;

public class DataUtilTest {

    @Test
    public void testLoadFile() {
        // Typical use case
        Document doc = DataUtil.load(new File("test.html"), "UTF-8", "http://example.com");
        assertNotNull(doc);
        
        // Edge case - empty file
        try {
            Document emptyDoc = DataUtil.load(new File("empty.html"), "UTF-8", "http://example.com");
            assertNull(emptyDoc);
        } catch (IOException e) {
            // Exception handling scenario
            fail("Should not throw IOException for empty file");
        }
    }

    @Test
    public void testLoadInputStream() {
        // Typical use case
        Document doc = DataUtil.load(new FileInputStream(new File("test.html")), "UTF-8", "http://example.com");
        assertNotNull(doc);
        
        // Edge case - null input stream
        try {
            Document nullDoc = DataUtil.load(null, "UTF-8", "http://example.com");
            assertNull(nullDoc);
        } catch (IOException e) {
            // Exception handling scenario
            fail("Should not throw IOException for null input stream");
        }
    }

    @Test
    public void testLoadInputStreamWithParser() {
        // Typical use case
        Document doc = DataUtil.load(new FileInputStream(new File("test.html")), "UTF-8", "http://example.com", Parser.htmlParser());
        assertNotNull(doc);
        
        // Edge case - invalid parser
        try {
            Document invalidDoc = DataUtil.load(new FileInputStream(new File("test.html")), "UTF-8", "http://example.com", Parser.xmlParser());
            assertNull(invalidDoc);
        } catch (IOException e) {
            // Exception handling scenario
            fail("Should not throw IOException for invalid parser");
        }
    }

    @Test
    public void testCrossStreams() {
        // Typical use case
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataUtil.crossStreams(new ByteArrayInputStream(new byte[] {1, 2, 3}), out);
        assertEquals(3, out.toByteArray().length);
        
        // Edge case - empty input stream
        try {
            DataUtil.crossStreams(new ByteArrayInputStream(new byte[0]), out);
            assertEquals(0, out.toByteArray().length);
        } catch (IOException e) {
            // Exception handling scenario
            fail("Should not throw IOException for empty input stream");
        }
    }

    @Test
    public void testReadToByteBuffer() {
        // Typical use case
        ByteBuffer buffer = DataUtil.readToByteBuffer(new ByteArrayInputStream(new byte[] {1, 2, 3}), 3);
        assertEquals(3, buffer.limit());
        
        // Edge case - maxSize exceeds input stream
        ByteBuffer bigBuffer = DataUtil.readToByteBuffer(new ByteArrayInputStream(new byte[] {1, 2, 3}), 5);
        assertEquals(3, bigBuffer.limit());
    }
}