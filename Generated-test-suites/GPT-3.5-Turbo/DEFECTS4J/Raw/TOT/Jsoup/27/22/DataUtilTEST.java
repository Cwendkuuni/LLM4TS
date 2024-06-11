import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DataUtilTest {

    private static final String CHARSET_UTF8 = "UTF-8";
    private static final String CHARSET_ISO = "ISO-8859-1";

    private static final String BASE_URI = "https://example.com";

    @Test
    public void testLoadFromFile() {
        File file = new File("test.html");
        String charsetName = CHARSET_UTF8;

        try {
            Document doc = DataUtil.load(file, charsetName, BASE_URI);

            assertNotNull(doc);
            assertEquals(BASE_URI, doc.baseUri());
            assertEquals("Test HTML", doc.title());
            // test other assertions or validations for the loaded document
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoadFromInputStream() {
        InputStream inputStream = getClass().getResourceAsStream("/test.html");
        String charsetName = CHARSET_ISO;

        try {
            Document doc = DataUtil.load(inputStream, charsetName, BASE_URI);

            assertNotNull(doc);
            assertEquals(BASE_URI, doc.baseUri());
            assertEquals("Test HTML", doc.title());
            // test other assertions or validations for the loaded document
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoadFromInputStreamWithCustomParser() {
        InputStream inputStream = getClass().getResourceAsStream("/test.html");
        String charsetName = CHARSET_ISO;
        Parser parser = Parser.xmlParser(); // using XML parser

        try {
            Document doc = DataUtil.load(inputStream, charsetName, BASE_URI, parser);

            assertNotNull(doc);
            assertEquals(BASE_URI, doc.baseUri());
            assertEquals("Test XML", doc.title());
            // test other assertions or validations for the loaded document
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testParseByteDataWithCharsetFound() {
        String html = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html;charset=gb2312\"></head><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(html.getBytes());
        String charsetName = null;
        String baseUri = BASE_URI;

        Parser parser = Parser.htmlParser();

        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        assertNotNull(doc);
        assertEquals(BASE_URI, doc.baseUri());
        // test other assertions or validations for the parsed document
    }

    @Test
    public void testParseByteDataWithCharsetNotFound() {
        String html = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html\"></head><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(html.getBytes());
        String charsetName = null;
        String baseUri = BASE_URI;

        Parser parser = Parser.htmlParser();

        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        assertNotNull(doc);
        assertEquals(BASE_URI, doc.baseUri());
        // test other assertions or validations for the parsed document
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() {
        String html = "<html><head></head><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(html.getBytes());
        String charsetName = CHARSET_UTF8;
        String baseUri = BASE_URI;

        Parser parser = Parser.htmlParser();

        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        assertNotNull(doc);
        assertEquals(BASE_URI, doc.baseUri());
        // test other assertions or validations for the parsed document
    }

    @Test
    public void testReadToByteBuffer() {
        // Test case for reading inputStream and converting it to byte buffer

        InputStream inputStream = getClass().getResourceAsStream("/test.html");

        try {
            ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);

            assertNotNull(byteData);
            // test other assertions or validations for the byte data
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetCharsetFromContentTypeWithCharsetPresent() {
        String contentType = "text/html; charset=UTF-8";

        String foundCharset = DataUtil.getCharsetFromContentType(contentType);

        assertNotNull(foundCharset);
        assertEquals(CHARSET_UTF8, foundCharset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithoutCharsetPresent() {
        String contentType = "text/html";

        String foundCharset = DataUtil.getCharsetFromContentType(contentType);

        assertNull(foundCharset);
    }
}