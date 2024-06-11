import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.helper.DataUtil;

public class DataUtilTest {

    @Test
    public void testLoadFromFile() {
        // Test loading file
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        try {
            Document document = DataUtil.load(new File("test.html"), charsetName, baseUri);
            assertNotNull(document);
            assertEquals(baseUri, document.baseUri());
        } catch (IOException e) {
            fail("Failed to load file");
        }
    }

    @Test
    public void testLoadFromInputStream() {
        // Test loading from input stream
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        try {
            InputStream inputStream = new FileInputStream(new File("test.html"));
            Document document = DataUtil.load(inputStream, charsetName, baseUri);
            assertNotNull(document);
            assertEquals(baseUri, document.baseUri());
        } catch (IOException e) {
            fail("Failed to load input stream");
        }
    }

    @Test
    public void testLoadFromInputStreamWithParser() {
        // Test loading from input stream with parser
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        try {
            InputStream inputStream = new FileInputStream(new File("test.html"));
            Parser parser = Parser.htmlParser();
            Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
            assertNotNull(document);
            assertEquals(baseUri, document.baseUri());
        } catch (IOException e) {
            fail("Failed to load input stream with parser");
        }
    }

    @Test
    public void testParseByteData() {
        // Test parsing byte data
        ByteBuffer byteData = ByteBuffer.wrap("test".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testReadToByteBuffer() {
        // Test reading to byte buffer
        try {
            InputStream inputStream = new FileInputStream(new File("test.html"));
            ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
            assertNotNull(byteData);
            assertTrue(byteData.hasArray());
        } catch (IOException e) {
            fail("Failed to read to byte buffer");
        }
    }

    @Test
    public void testGetCharsetFromContentType() {
        // Test getting charset from content type
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

}