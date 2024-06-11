import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.helper.DataUtil;
import org.junit.Test;
import static org.junit.Assert.*;

public class DataUtilTest {

    @Test
    public void testLoadFile() {
        // Typical use case
        try {
            Document doc = DataUtil.load(new File("test.html"), "UTF-8", "http://example.com");
            assertNotNull(doc);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testLoadInputStream() {
        // Edge case: Null input stream
        try {
            Document doc = DataUtil.load(null, "UTF-8", "http://example.com");
            assertNull(doc);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testCrossStreams() {
        // Typical use case
        try {
            DataUtil.crossStreams(new InputStream(), new OutputStream());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testReadToByteBuffer() {
        // Typical use case
        try {
            ByteBuffer buffer = DataUtil.readToByteBuffer(new InputStream(), 1024);
            assertNotNull(buffer);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testMimeBoundary() {
        // Typical use case
        String boundary = DataUtil.mimeBoundary();
        assertNotNull(boundary);
    }

    @Test
    public void testGetCharsetFromContentType() {
        // Typical use case
        String charset = DataUtil.getCharsetFromContentType("text/html; charset=UTF-8");
        assertEquals("UTF-8", charset);

        // Edge case: Null content type
        charset = DataUtil.getCharsetFromContentType(null);
        assertNull(charset);
    }

    @Test
    public void testValidateCharset() {
        // Typical use case
        String charset = DataUtil.validateCharset("UTF-8");
        assertEquals("UTF-8", charset);

        // Edge case: Invalid charset
        charset = DataUtil.validateCharset("UnknownCharset");
        assertNull(charset);
    }

}