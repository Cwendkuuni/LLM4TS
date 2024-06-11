import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class DataUtilTest {

    private static Document doc;
    private static Parser parser;

    @BeforeClass
    public static void setup() {
        parser = DataUtil.Parser.htmlParser();
    }

    /**
     * Test method for {@link DataUtil#load(File, String, String)}
     */
    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        String charset = "UTF-8";
        String baseUri = "http://example.com";
        doc = DataUtil.load(file, charset, baseUri);
        assertNotNull(doc);
    }

    /**
     * Test method for {@link DataUtil#load(File, String, String)} with invalid file
     */
    @Test(expected = IOException.class)
    public void testLoadInvalidFile() throws IOException {
        File file = new File("invalidFile.txt");
        String charset = "UTF-8";
        String baseUri = "http://example.com";
        DataUtil.load(file, charset, baseUri);
    }

    /**
     * Test method for {@link DataUtil#load(InputStream, String, String)}
     */
    @Test
    public void testLoadInputStream() throws IOException {
        String htmlData = "<html><head></head><body></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(htmlData.getBytes());
        String charset = "UTF-8";
        String baseUri = "http://example.com";
        doc = DataUtil.load(inputStream, charset, baseUri);
        assertNotNull(doc);
    }

    /**
     * Test method for {@link DataUtil#load(InputStream, String, String, Parser)}
     */
    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String htmlData = "<html><head></head><body></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(htmlData.getBytes());
        String charset = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = DataUtil.Parser.xmlParser();
        doc = DataUtil.load(inputStream, charset, baseUri, parser);
        assertNotNull(doc);
    }

    /**
     * Test method for {@link DataUtil#parseByteData(ByteBuffer, String, String, Parser)}
     */
    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.wrap("test".getBytes());
        String charset = "UTF-8";
        String baseUri = "http://example.com";
        doc = DataUtil.parseByteData(byteData, charset, baseUri, parser);
        assertNotNull(doc);
    }

    /**
     * Test method for {@link DataUtil#parseByteData(ByteBuffer, String, String, Parser)} with null charset
     */
    @Test
    public void testParseByteDataNullCharset() {
        ByteBuffer byteData = ByteBuffer.wrap("test".getBytes());
        String baseUri = "http://example.com";
        doc = DataUtil.parseByteData(byteData, null, baseUri, parser);
        assertNull(doc);
    }

    /**
     * Test method for {@link DataUtil#parseByteData(ByteBuffer, String, String, Parser)} with invalid charset
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseByteDataInvalidCharset() {
        ByteBuffer byteData = ByteBuffer.wrap("test".getBytes());
        String charset = "InvalidCharset";
        String baseUri = "http://example.com";
        doc = DataUtil.parseByteData(byteData, charset, baseUri, parser);
    }

    /**
     * Test method for {@link DataUtil#readToByteBuffer(InputStream, int)}
     */
    @Test
    public void testReadToByteBuffer() throws IOException {
        String htmlData = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlData.getBytes());
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, 0);
        assertEquals(htmlData.length(), byteData.limit());
    }

    /**
     * Test method for {@link DataUtil#getCharsetFromContentType(String)}
     */
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=EUC-JP";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("EUC-JP", charset);
    }

    /**
     * Test method for {@link DataUtil#getCharsetFromContentType(String)} with no charset
     */
    @Test
    public void testGetCharsetFromContentTypeNoCharset() {
        String contentType = "text/html";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }
}