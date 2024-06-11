import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.helper.DataUtil;
import java.io.File;
import java.io.IOException;
import static org.junit.Assert.*;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        Document document = DataUtil.load(file, "UTF-8", "http://example.com");
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        // sample input stream
        InputStream inputStream = new ByteArrayInputStream("<html><head></head><body></body></html>".getBytes());
        Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com");
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        // sample input stream
        InputStream inputStream = new ByteArrayInputStream("<html><head></head><body></body></html>".getBytes());
        Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com", Parser.xmlParser());
        assertNotNull(document);
    }

    @Test
    public void testCrossStreams() throws IOException {
        // test data
        InputStream inStream = new ByteArrayInputStream("test data".getBytes());
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inStream, outStream);

        assertEquals("test data", outStream.toString());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("test data".getBytes());
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 0);

        assertNotNull(byteBuffer);
        assertEquals(9, byteBuffer.limit());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertEquals("UTF-8", charset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();

        assertNotNull(boundary);
        assertEquals(32, boundary.length());
    }

}