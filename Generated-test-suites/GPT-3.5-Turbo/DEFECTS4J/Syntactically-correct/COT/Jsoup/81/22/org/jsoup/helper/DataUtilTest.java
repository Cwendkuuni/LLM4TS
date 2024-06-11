import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws Exception {
        Document doc = DataUtil.load(new File("test.html"), "UTF-8", "http://example.com");
        assertNotNull(doc);
        // Additional assertions
    }

    @Test
    public void testLoadInputStream() throws Exception {
        InputStream in = new FileInputStream("test.html");
        Document doc = DataUtil.load(in, "UTF-8", "http://example.com");
        assertNotNull(doc);
        // Additional assertions
    }

    @Test
    public void testLoadInputStreamWithParser() throws Exception {
        InputStream in = new FileInputStream("test.html");
        Document doc = DataUtil.load(in, "UTF-8", "http://example.com", Parser.htmlParser());
        assertNotNull(doc);
        // Additional assertions
    }

    @Test
    public void testCrossStreams() throws Exception {
        InputStream in = new FileInputStream("input.txt");
        OutputStream out = new FileOutputStream("output.txt");
        DataUtil.crossStreams(in, out);
        // Additional assertions
    }

    @Test
    public void testReadToByteBuffer() throws Exception {
        InputStream in = new FileInputStream("input.txt");
        ByteBuffer buffer = DataUtil.readToByteBuffer(in, 1024);
        assertNotNull(buffer);
        // Additional assertions
    }

    @Test
    public void testReadToByteBufferWithoutMaxSize() throws Exception {
        InputStream in = new FileInputStream("input.txt");
        ByteBuffer buffer = DataUtil.readToByteBuffer(in);
        assertNotNull(buffer);
        // Additional assertions
    }

    @Test
    public void testReadFileToByteBuffer() throws Exception {
        File file = new File("input.txt");
        ByteBuffer buffer = DataUtil.readFileToByteBuffer(file);
        assertNotNull(buffer);
        // Additional assertions
    }

    @Test
    public void testEmptyByteBuffer() throws Exception {
        ByteBuffer buffer = DataUtil.emptyByteBuffer();
        assertNotNull(buffer);
        assertEquals(0, buffer.capacity());
    }

    @Test
    public void testGetCharsetFromContentType() throws Exception {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithoutCharset() throws Exception {
        String contentType = "text/html";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testMimeBoundary() throws Exception {
        String boundary = DataUtil.mimeBoundary();
        assertNotNull(boundary);
        assertEquals(32, boundary.length());
    }
}