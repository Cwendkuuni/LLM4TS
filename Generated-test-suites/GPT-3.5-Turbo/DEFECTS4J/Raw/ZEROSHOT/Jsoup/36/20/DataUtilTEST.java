import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;

public class DataUtilTest {
    private static final String CHARSET_NAME = "UTF-8";
    private static final String BASE_URI = "http://example.com";

    private File testFile;
    private InputStream testInputStream;

    @Before
    public void setUp() throws Exception {
        testFile = new File("test.html");
        testInputStream = new FileInputStream(testFile);
    }

    @Test
    public void testLoadFromFile() throws IOException {
        Document doc = DataUtil.load(testFile, CHARSET_NAME, BASE_URI);
        assertNotNull(doc);
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        Document doc = DataUtil.load(testInputStream, CHARSET_NAME, BASE_URI);
        assertNotNull(doc);
    }

    @Test
    public void testLoadFromInputStreamWithParser() throws IOException {
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.load(testInputStream, CHARSET_NAME, BASE_URI, parser);
        assertNotNull(doc);
    }

    @Test
    public void testParseByteDataWithDefaultCharset() {
        ByteBuffer byteData = ByteBuffer.allocate(10);
        String charsetName = null;
        String baseUri = BASE_URI;
        Parser parser = Parser.htmlParser();

        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(doc);
        assertEquals(CHARSET_NAME, doc.outputSettings().charset().displayName());
    }

    @Test
    public void testParseByteDataWithCharsetFromMeta() {
        ByteBuffer byteData = ByteBuffer.allocate(10);
        String charsetName = null;
        String baseUri = BASE_URI;
        Parser parser = Parser.htmlParser();

        byteData.put("<!-- <meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\"> -->".getBytes());
        byteData.flip();

        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(doc);
        assertEquals("UTF-8", doc.outputSettings().charset().displayName());
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() {
        ByteBuffer byteData = ByteBuffer.allocate(10);
        String charsetName = CHARSET_NAME;
        String baseUri = BASE_URI;
        Parser parser = Parser.htmlParser();

        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(doc);
        assertEquals(CHARSET_NAME, doc.outputSettings().charset().displayName());
    }

    @Test
    public void testReadToByteBufferWithUnlimitedMaxSize() throws IOException {
        ByteBuffer byteData = DataUtil.readToByteBuffer(testInputStream, 0);
        assertNotNull(byteData);
    }

    @Test
    public void testReadToByteBufferWithCappedMaxSize() throws IOException {
        int maxSize = 100;
        ByteBuffer byteData = DataUtil.readToByteBuffer(testInputStream, maxSize);
        assertNotNull(byteData);
        assertTrue(byteData.limit() <= maxSize);
    }

    @Test
    public void testGetCharsetFromContentTypeWithSupportedCharset() {
        String contentType = "text/html; charset=UTF-8";

        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNotNull(charset);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithUnsupportedCharset() {
        String contentType = "text/html; charset=unknowncharset";

        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithMixedCaseCharset() {
        String contentType = "text/html; charset=ISO-8859-1";

        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNotNull(charset);
        assertEquals("ISO-8859-1", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithMissingCharset() {
        String contentType = "text/html";

        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }
}