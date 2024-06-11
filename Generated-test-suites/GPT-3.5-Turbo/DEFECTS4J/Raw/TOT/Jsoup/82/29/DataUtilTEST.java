
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.*;
import static org.junit.Assert.*;

public class DataUtilTest {

    private static String validHtml = "<html>\n" +
            "<head>\n" +
            "    <title>Test</title>\n" +
            "</head>\n" +
            "<body>\n" +
            "    <h1>Hello</h1>\n" +
            "</body>\n" +
            "</html>";

    private static String invalidHtml = "<!DOCTYPE html>\n" +
            "<html>\n" +
            "<head>\n" +
            "<title>Test</title>\n" +
            "</head\n" +
            "<body>\n" +
            "<h1>Hello</h1>\n" +
            "</body>\n" +
            "</html>";

    @Test
    public void testLoadFile_ValidFile_Success() {
        try {
            Document document = DataUtil.load(new File("validFile.html"), "UTF-8", "http://example.com");
            assertEquals(validHtml, document.html());
        } catch (IOException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test(expected = IOException.class)
    public void testLoadFile_InvalidFile_ThrowsIOException() throws IOException {
        Document document = DataUtil.load(new File("invalidFile.html"), "UTF-8", "http://example.com");
    }

    @Test
    public void testLoadInputStream_ValidInputStream_Success() {
        try {
            InputStream inputStream = new ByteArrayInputStream(validHtml.getBytes());
            Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com");
            assertEquals(validHtml, document.html());
        } catch (IOException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test(expected = IOException.class)
    public void testLoadInputStream_InvalidInputStream_ThrowsIOException() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(invalidHtml.getBytes());
        Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com");
    }

    @Test
    public void testCrossStreams_ValidInputStreamAndOutputStream_Success() {
        try {
            InputStream inputStream = new ByteArrayInputStream(validHtml.getBytes());
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            DataUtil.crossStreams(inputStream, outputStream);
            assertEquals(validHtml.getBytes(), outputStream.toByteArray());
        } catch (IOException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void parseInputStream_ValidInputStream_Success() {
        try {
            InputStream inputStream = new ByteArrayInputStream(validHtml.getBytes());
            Document document = DataUtil.parseInputStream(inputStream, "UTF-8", "http://example.com", Jsoup.parser());
            assertEquals(validHtml, document.html());
        } catch (IOException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test(expected = IOException.class)
    public void parseInputStream_InvalidInputStream_ThrowsIOException() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(invalidHtml.getBytes());
        Document document = DataUtil.parseInputStream(inputStream, "UTF-8", "http://example.com", Jsoup.parser());
    }

    @Test
    public void testReadToByteBuffer_ValidInputStream_Success() {
        try {
            InputStream inputStream = new ByteArrayInputStream(validHtml.getBytes());
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, validHtml.length());
            assertEquals(validHtml.getBytes(), byteBuffer.array());
        } catch (IOException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadToByteBuffer_InvalidMaxSize_ThrowsIllegalArgumentException() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(validHtml.getBytes());
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, -1);
    }

    @Test
    public void testGetCharsetFromContentType_ValidContentType_Success() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentType_InvalidContentType_ReturnsNull() {
        String contentType = "text/html";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testValidateCharset_ValidCharset_Success() {
        String cs = "UTF-8";
        String validatedCharset = DataUtil.validateCharset(cs);
        assertEquals(cs, validatedCharset);
    }

    @Test
    public void testValidateCharset_InvalidCharset_ReturnsNull() {
        String cs = "InvalidCharset";
        String validatedCharset = DataUtil.validateCharset(cs);
        assertNull(validatedCharset);
    }

    @Test
    public void testMimeBoundary_ValidBoundary_Success() {
        String mimeBoundary = DataUtil.mimeBoundary();
        assertNotNull(mimeBoundary);
        assertEquals(DataUtil.boundaryLength, mimeBoundary.length());
    }

    @Test
    public void detectCharsetFromBom_ValidBomCharset_Success() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(4);
        byteBuffer.put((byte) 0xEF);
        byteBuffer.put((byte) 0xBB);
        byteBuffer.put((byte) 0xBF);
        byteBuffer.rewind();
        DataUtil.BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);
        assertNotNull(bomCharset);
        assertEquals("UTF-8", bomCharset.charset);
        assertTrue(bomCharset.offset);
    }

    @Test
    public void detectCharsetFromBom_InvalidBomCharset_ReturnsNull() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(4);
        byteBuffer.put((byte) 0x00);
        byteBuffer.put((byte) 0x00);
        byteBuffer.put((byte) 0x00);
        byteBuffer.put((byte) 0x00);
        byteBuffer.rewind();
        DataUtil.BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);
        assertNull(bomCharset);
    }
}

