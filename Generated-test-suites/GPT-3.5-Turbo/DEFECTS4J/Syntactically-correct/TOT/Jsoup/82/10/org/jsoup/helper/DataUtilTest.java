import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.*;

import static org.junit.Assert.*;

public class DataUtilTest {

    private static final String TEST_FILE_PATH = "src/test/resources/test.html";

    private DataUtil dataUtil;

    @Before
    public void setup() {
        dataUtil = new DataUtil();
    }

    // Test for load(File in, String charsetName, String baseUri) method
    @Test
    public void testLoadFromFile() throws IOException {
        File file = new File(TEST_FILE_PATH);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        Document document = dataUtil.load(file, charsetName, baseUri);

        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
    }

    // Test for load(InputStream in, String charsetName, String baseUri) method
    @Test
    public void testLoadFromInputStream() throws IOException {
        InputStream inputStream = new FileInputStream(TEST_FILE_PATH);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        Document document = dataUtil.load(inputStream, charsetName, baseUri);

        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
    }

    // Test for load(InputStream in, String charsetName, String baseUri, Parser parser) method
    @Test
    public void testLoadFromInputStreamWithParser() throws IOException {
        InputStream inputStream = new FileInputStream(TEST_FILE_PATH);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        Document document = dataUtil.load(inputStream, charsetName, baseUri, parser);

        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
    }

    // Test for crossStreams(InputStream in, OutputStream out) method
    @Test
    public void testCrossStreams() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Hello, World!".getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        dataUtil.crossStreams(inputStream, outputStream);

        assertEquals("Hello, World!", outputStream.toString());
    }

    // Test for parseInputStream(InputStream input, String charsetName, String baseUri, Parser parser) method
    @Test
    public void testParseInputStream() throws IOException {
        InputStream inputStream = new FileInputStream(TEST_FILE_PATH);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        Document document = dataUtil.parseInputStream(inputStream, charsetName, baseUri, parser);

        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
    }

    // Test for readToByteBuffer(InputStream inStream, int maxSize) method
    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = new FileInputStream(TEST_FILE_PATH);
        int maxSize = 1024;

        byte[] expectedBytes = new byte[maxSize];
        inputStream.read(expectedBytes);

        ByteBuffer byteBuffer = dataUtil.readToByteBuffer(inputStream, maxSize);

        assertArrayEquals(expectedBytes, byteBuffer.array());
    }

    // Test for getCharsetFromContentType(String contentType) method
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";

        String charset = dataUtil.getCharsetFromContentType(contentType);

        assertEquals("UTF-8", charset);
    }

    // Test for mimeBoundary() method
    @Test
    public void testMimeBoundary() {
        String mimeBoundary = dataUtil.mimeBoundary();

        assertNotNull(mimeBoundary);
        assertEquals(DataUtil.boundaryLength, mimeBoundary.length());
    }

    // Test for validateCharset(String cs) method
    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";

        String validatedCharset = dataUtil.validateCharset(charset);

        assertEquals(charset, validatedCharset);
    }

    // Test for emptyByteBuffer() method
    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = dataUtil.emptyByteBuffer();

        assertEquals(0, byteBuffer.capacity());
    }

    // Test for detectCharsetFromBom(ByteBuffer byteData) method
    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});

        DataUtil.BomCharset bomCharset = dataUtil.detectCharsetFromBom(byteBuffer);

        assertNotNull(bomCharset);
        assertEquals("UTF-8", bomCharset.charset);
        assertTrue(bomCharset.offset);
    }

}