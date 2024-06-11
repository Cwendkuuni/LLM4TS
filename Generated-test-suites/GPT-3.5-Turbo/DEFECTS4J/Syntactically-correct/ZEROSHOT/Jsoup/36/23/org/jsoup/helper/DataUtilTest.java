import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.*;

public class DataUtilTest {

    private String testFile = "test.html";
    private String baseUri = "http://example.com";
    private String charsetName = "UTF-8";
    private String contentType = "text/html; charset=UTF-8";

    private File file;
    private InputStream inputStream;

    @Before
    public void setUp() throws IOException {
        file = new File(testFile);
        String htmlContent = "<html><head></head><body></body></html>";
        OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(file), charsetName);
        out.write(htmlContent);
        out.close();
        inputStream = new FileInputStream(file);
    }

    @Test
    public void testLoadFromFile() throws IOException {
        Document doc = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(doc);
        assertEquals("UTF-8", doc.outputSettings().charset().name());
        assertTrue(doc.select("html").size() != 0);
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        Document doc = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(doc);
        assertEquals("UTF-8", doc.outputSettings().charset().name());
        assertTrue(doc.select("html").size() != 0);
    }

    @Test
    public void testLoadFromInputStreamWithParser() throws IOException {
        Document doc = DataUtil.load(inputStream, charsetName, baseUri, Parser.htmlParser());
        assertNotNull(doc);
        assertEquals("UTF-8", doc.outputSettings().charset().name());
        assertTrue(doc.select("html").size() != 0);
    }

    @Test
    public void testParseByteData_withCharset() {
        String charset = "ISO-8859-1";
        ByteBuffer byteData = ByteBuffer.wrap("<html><head></head><body></body></html>".getBytes(Charset.forName(charset)));
        Document doc = DataUtil.parseByteData(byteData, charset, baseUri, Parser.htmlParser());
        assertNotNull(doc);
        assertEquals(charset, doc.outputSettings().charset().name());
        assertTrue(doc.select("html").size() != 0);
    }

    @Test
    public void testParseByteData_withoutCharset() {
        ByteBuffer byteData = ByteBuffer.wrap("<html><head></head><body></body></html>".getBytes(Charset.forName("UTF-8")));
        Document doc = DataUtil.parseByteData(byteData, null, baseUri, Parser.htmlParser());
        assertNotNull(doc);
        assertEquals("UTF-8", doc.outputSettings().charset().name());
        assertTrue(doc.select("html").size() != 0);
    }

    @Test
    public void testReadToByteBuffer_unlimited() throws IOException {
        int maxSize = 0;
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);
        assertNotNull(byteBuffer);
        assertEquals(57, byteBuffer.limit());
    }

    @Test
    public void testReadToByteBuffer_capped() throws IOException {
        int maxSize = 10;
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);
        assertNotNull(byteBuffer);
        assertEquals(maxSize, byteBuffer.limit());
    }

    @Test
    public void testGetCharsetFromContentType_supportedCharset() {
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNotNull(charset);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentType_unsupportedCharset() {
        String contentType = "text/html; charset=GBK";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testGetCharsetFromContentType_uppercaseCharset() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNotNull(charset);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentType_noCharset() {
        String contentType = "text/html";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testGetCharsetFromContentType_emptyCharset() {
        String contentType = "text/html; charset=";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }
}