import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

public class DataUtilTest {
    private static final String CHARSET_NAME = "UTF-8";
    private static final String BASE_URI = "http://example.com";
    private static final String FILE_PATH = "path/to/file.html";

    private DataUtil dataUtil;

    @Before
    public void setUp() {
        dataUtil = new DataUtil();
    }

    @Test
    public void testLoadFile() throws IOException {
        File file = new File(FILE_PATH);
        Document doc = dataUtil.load(file, CHARSET_NAME, BASE_URI);
        assertNotNull(doc);
        assertEquals(BASE_URI, doc.baseUri());
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream in = getClass().getResourceAsStream(FILE_PATH);
        Document doc = dataUtil.load(in, CHARSET_NAME, BASE_URI);
        assertNotNull(doc);
        assertEquals(BASE_URI, doc.baseUri());
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        InputStream in = getClass().getResourceAsStream(FILE_PATH);
        Parser parser = Parser.htmlParser();
        Document doc = dataUtil.load(in, CHARSET_NAME, BASE_URI, parser);
        assertNotNull(doc);
        assertEquals(BASE_URI, doc.baseUri());
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream in = getClass().getResourceAsStream(FILE_PATH);
        OutputStream out = System.out;
        dataUtil.crossStreams(in, out);
    }

    @Test
    public void testParseInputStream() throws IOException {
        InputStream in = getClass().getResourceAsStream(FILE_PATH);
        Document doc = dataUtil.parseInputStream(in, CHARSET_NAME, BASE_URI, Parser.htmlParser());
        assertNotNull(doc);
        assertEquals(BASE_URI, doc.baseUri());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream in = getClass().getResourceAsStream(FILE_PATH);
        ByteBuffer buffer = dataUtil.readToByteBuffer(in, 1024);
        assertNotNull(buffer);
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        InputStream in = getClass().getResourceAsStream(FILE_PATH);
        ByteBuffer buffer = dataUtil.readToByteBuffer(in);
        assertNotNull(buffer);
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File file = new File(FILE_PATH);
        ByteBuffer buffer = dataUtil.readFileToByteBuffer(file);
        assertNotNull(buffer);
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer buffer = dataUtil.emptyByteBuffer();
        assertNotNull(buffer);
        assertEquals(0, buffer.capacity());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = dataUtil.getCharsetFromContentType(contentType);
        assertNotNull(charset);
        assertEquals(CHARSET_NAME, charset);
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = dataUtil.mimeBoundary();
        assertNotNull(mimeBoundary);
        assertEquals(DataUtil.boundaryLength, mimeBoundary.length());
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        String validatedCharset = dataUtil.validateCharset(charset);
        assertNotNull(validatedCharset);
        assertEquals(charset, validatedCharset);
    }

    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer byteData = ByteBuffer.allocate(4);
        byte[] bom = {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};
        byteData.put(bom);
        byteData.rewind();
        DataUtil.BomCharset bomCharset = dataUtil.detectCharsetFromBom(byteData);
        assertNotNull(bomCharset);
        assertEquals("UTF-8", bomCharset.charset);
        assertTrue(bomCharset.offset);
    }
}