import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import static org.junit.Assert.*;

public class DataUtilTest {

    @Test
    public void testLoadFile() {
        // Typical use case
        Document document = DataUtil.load(new File("test.html"), "UTF-8", "http://example.com");
        assertNotNull(document);

        // Edge case
        try {
            DataUtil.load(new File("nonexistent.html"), "UTF-8", "http://example.com");
            fail("IOException not thrown for non-existent file");
        } catch (IOException e) {
            // Expected IOException
        }
    }

    @Test
    public void testLoadInputStream() {
        // Typical use case
        Document document = DataUtil.load(new FileInputStream("test.html"), "UTF-8", "http://example.com");
        assertNotNull(document);

        // Edge case
        try {
            DataUtil.load(new ByteArrayInputStream(new byte[0]), "UTF-8", "http://example.com");
            fail("IOException not thrown for empty input stream");
        } catch (IOException e) {
            // Expected IOException
        }
    }

    @Test
    public void testCrossStreams() {
        // Typical use case
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(new ByteArrayInputStream("test".getBytes()), outputStream);
        assertEquals("test", outputStream.toString());
    }

    @Test
    public void testReadToByteBuffer() {
        // Typical use case
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(new ByteArrayInputStream("test".getBytes()), 10);
        assertNotNull(byteBuffer);

        // Edge case
        try {
            DataUtil.readToByteBuffer(new ByteArrayInputStream("test".getBytes()), -1);
            fail("IllegalArgumentException not thrown for negative maxSize");
        } catch (IllegalArgumentException e) {
            // Expected IllegalArgumentException
        }
    }

    @Test
    public void testReadFileToByteBuffer() {
        // Typical use case
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(new File("test.txt"));
        assertNotNull(byteBuffer);

        // Edge case
        try {
            DataUtil.readFileToByteBuffer(new File("nonexistent.txt"));
            fail("IOException not thrown for non-existent file");
        } catch (IOException e) {
            // Expected IOException
        }
    }

    @Test
    public void testGetCharsetFromContentType() {
        // Typical use case
        String charset = DataUtil.getCharsetFromContentType("text/html; charset=UTF-8");
        assertEquals("UTF-8", charset);

        // Edge case
        charset = DataUtil.getCharsetFromContentType(null);
        assertNull(charset);
    }

    @Test
    public void testMimeBoundary() {
        // Typical use case
        String boundary = DataUtil.mimeBoundary();
        assertNotNull(boundary);
        assertEquals(32, boundary.length());
    }
}