import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, "UTF-8", "http://example.com");
        assertNotNull(doc);
        // Add more assertions here
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream in = getClass().getResourceAsStream("test.html");
        Document doc = DataUtil.load(in, "UTF-8", "http://example.com");
        assertNotNull(doc);
        // Add more assertions here
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        InputStream in = getClass().getResourceAsStream("test.html");
        Document doc = DataUtil.load(in, "UTF-8", "http://example.com", Parser.xmlParser());
        assertNotNull(doc);
        // Add more assertions here
    }

    @Test
    public void testCrossStreams() throws IOException {
        // Add test for crossStreams if needed
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream in = getClass().getResourceAsStream("test.html");
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(in, 1024);
        assertNotNull(byteBuffer);
        // Add more assertions here
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
        // Add more assertions here
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertNotNull(boundary);
        assertEquals(32, boundary.length());
        // Add more assertions here
    }

    // Add more test cases for other methods in DataUtil class

}