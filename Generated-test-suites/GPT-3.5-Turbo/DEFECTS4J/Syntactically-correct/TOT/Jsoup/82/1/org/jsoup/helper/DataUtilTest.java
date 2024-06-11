import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DataUtilTest {
    private DataUtil dataUtil;

    @Before
    public void setup() {
        dataUtil = new DataUtil();
    }

    // Test for load(File in, String charsetName, String baseUri)
    @Test
    public void testLoadFromFile() {
        try {
            Document document = dataUtil.load(new File("test.html"), "UTF-8", "https://www.example.com");
            assertNotNull("Document should not be null", document);
            assertEquals("https://www.example.com", document.baseUri());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    // Test for load(InputStream in, String charsetName, String baseUri)
    @Test
    public void testLoadFromInputStream() {
        try {
            InputStream inputStream = new FileInputStream("test.html");
            Document document = dataUtil.load(inputStream, "UTF-8", "https://www.example.com");
            assertNotNull("Document should not be null", document);
            assertEquals("https://www.example.com", document.baseUri());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    // Test for load(InputStream in, String charsetName, String baseUri, Parser parser)
    @Test
    public void testLoadFromInputStreamWithParser() {
        try {
            InputStream inputStream = new FileInputStream("test.xml");
            Document document = dataUtil.load(inputStream, "UTF-8", "https://www.example.com", Parser.xmlParser());
            assertNotNull("Document should not be null", document);
            assertEquals("https://www.example.com", document.baseUri());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    // Test for crossStreams(InputStream in, OutputStream out)
    @Test
    public void testCrossStreams() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Hello, World!".getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            dataUtil.crossStreams(inputStream, outputStream);
            assertEquals("Hello, World!", outputStream.toString());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    // Test for parseInputStream(InputStream input, String charsetName, String baseUri, Parser parser)
    @Test
    public void testParseInputStream() {
        try {
            InputStream inputStream = new FileInputStream("test.html");
            Document document = dataUtil.parseInputStream(inputStream, "UTF-8", "https://www.example.com", Parser.htmlParser());
            assertNotNull("Document should not be null", document);
            assertEquals("https://www.example.com", document.baseUri());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    // Test for readToByteBuffer(InputStream inStream, int maxSize)
    @Test
    public void testReadToByteBuffer() {
        try {
            InputStream inputStream = new FileInputStream("test.html");
            ByteBuffer byteBuffer = dataUtil.readToByteBuffer(inputStream, 1024);
            assertNotNull("ByteBuffer should not be null", byteBuffer);
            assertTrue("ByteBuffer should not be empty", byteBuffer.capacity() > 0);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    // Test for mimeBoundary()
    @Test
    public void testMimeBoundary() {
        String mimeBoundary = dataUtil.mimeBoundary();
        assertNotNull("mimeBoundary should not be null", mimeBoundary);
        assertEquals(32, mimeBoundary.length());
    }

    // Test for getCharsetFromContentType(String contentType)
    @Test
    public void testGetCharsetFromContentType() {
        String charset = dataUtil.getCharsetFromContentType("text/html; charset=UTF-8");
        assertNotNull("Charset should not be null", charset);
        assertEquals("UTF-8", charset);
    }

    // Test for validateCharset(String cs)
    @Test
    public void testValidateCharset() {
        String validatedCharset = dataUtil.validateCharset("UTF-8");
        assertNotNull("Validated Charset should not be null", validatedCharset);
        assertEquals("UTF-8", validatedCharset);

        validatedCharset = dataUtil.validateCharset("InvalidCharset");
        assertNull("Invalid Charset should be null", validatedCharset);
    }

    // Test for detectCharsetFromBom(ByteBuffer byteData)
    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer byteBuffer = dataUtil.emptyByteBuffer();
        DataUtil.BomCharset bomCharset = dataUtil.detectCharsetFromBom(byteBuffer);
        assertNull("BomCharset should be null", bomCharset);
    }

}