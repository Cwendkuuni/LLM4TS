import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class DataUtilTest {
    private static final File TEST_FILE = new File("test.html");
    private static final String CHARSET_NAME = "UTF-8";
    private static final String BASE_URI = "";

    @Test
    public void testLoadFromInputStream() throws IOException {
        InputStream in = getClass().getResourceAsStream("test.html");
        Document doc = DataUtil.load(in, CHARSET_NAME, BASE_URI);
        assertNotNull(doc);
    }

    @Test
    public void testLoadFromFile() throws IOException {
        Document doc = DataUtil.load(TEST_FILE, CHARSET_NAME, BASE_URI);
        assertNotNull(doc);
    }

    @Test
    public void testLoadFromInputStreamWithParser() throws IOException {
        InputStream in = getClass().getResourceAsStream("test.html");
        Document doc = DataUtil.load(in, CHARSET_NAME, BASE_URI, Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream in = getClass().getResourceAsStream("test.html");
        ByteBuffer byteData = DataUtil.readToByteBuffer(in);
        assertNotNull(byteData);
    }

    @Test
    public void testParseByteData() throws IOException {
        ByteBuffer byteData = DataUtil.readFileToByteBuffer(TEST_FILE);
        Document doc = DataUtil.parseByteData(byteData, CHARSET_NAME, BASE_URI, Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream in = getClass().getResourceAsStream("test.html");
        ByteBuffer byteData = DataUtil.readToByteBuffer(in);
        assertNotNull(byteData);
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        ByteBuffer byteData = DataUtil.readFileToByteBuffer(TEST_FILE);
        assertNotNull(byteData);
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        assertNotNull(byteBuffer);
        assertEquals(0, byteBuffer.remaining());
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
        assertEquals(DataUtil.boundaryLength, boundary.length());
    }
}