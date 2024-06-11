import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class DataUtilTest {

    private static final String CHARSET_NAME = "UTF-8";
    private static final String BASE_URI = "http://example.com";

    private File testFile;

    @Before
    public void setUp() {
        testFile = new File("test.html");
    }

    @Test
    public void testLoadFile() throws IOException {
        Document document = DataUtil.load(testFile, CHARSET_NAME, BASE_URI);

        assertNotNull(document);
        assertEquals(BASE_URI, document.baseUri());
        assertTrue(document.html().contains("Test HTML"));
    }

    @Test
    public void testLoadInputStream() throws IOException {
        FileInputStream fileInputStream = new FileInputStream(testFile);
        Document document = DataUtil.load(fileInputStream, CHARSET_NAME, BASE_URI);

        assertNotNull(document);
        assertEquals(BASE_URI, document.baseUri());
        assertTrue(document.html().contains("Test HTML"));

        fileInputStream.close();
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        FileInputStream fileInputStream = new FileInputStream(testFile);
        Document document = DataUtil.load(fileInputStream, CHARSET_NAME, BASE_URI, Parser.xmlParser());

        assertNotNull(document);
        assertEquals(BASE_URI, document.baseUri());
        assertTrue(document.html().contains("Test XML"));

        fileInputStream.close();
    }

    @Test
    public void testParseByteDataWithCharset() {
        String charset = "ISO-8859-1";
        String data = "<html><head></head><body></body></html>";
        byte[] byteData = data.getBytes();
        ByteBuffer byteBuffer = ByteBuffer.wrap(byteData);

        Document document = DataUtil.parseByteData(byteBuffer, charset, BASE_URI, Parser.htmlParser());

        assertNotNull(document);
        assertEquals(BASE_URI, document.baseUri());
    }

    @Test
    public void testParseByteDataWithoutCharset() {
        String data = "<html><head></head><body></body></html>";
        byte[] byteData = data.getBytes();
        ByteBuffer byteBuffer = ByteBuffer.wrap(byteData);

        Document document = DataUtil.parseByteData(byteBuffer, null, BASE_URI, Parser.htmlParser());

        assertNotNull(document);
        assertEquals(BASE_URI, document.baseUri());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        FileInputStream fileInputStream = new FileInputStream(testFile);
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(fileInputStream);

        assertNotNull(byteBuffer);
        assertEquals(fileInputStream.available(), byteBuffer.limit());

        fileInputStream.close();
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=ISO-8859-1";

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertNotNull(charset);
        assertEquals("ISO-8859-1", charset);
    }
}