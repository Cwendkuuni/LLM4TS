import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.*;

public class DataUtilTest {

    private static final String CHARSET_UTF8 = "UTF-8";

    private InputStream inputStream;
    private File file;
    private String baseUri;

    @Before
    public void setUp() {
        String html = "<html><body><h1>Test Page</h1></body></html>";
        inputStream = new ByteArrayInputStream(html.getBytes());
        file = new File("test.html");
        baseUri = "http://example.com";
    }

    @Test
    public void testLoad_File_ValidCharset() throws IOException {
        Document document = DataUtil.load(file, CHARSET_UTF8, baseUri);
        Assert.assertEquals(baseUri, document.baseUri());
        Assert.assertTrue(document.select("h1").size() > 0);
    }

    @Test
    public void testLoad_File_InvalidCharset() throws IOException {
        Document document = DataUtil.load(file, "INVALID_CHARSET", baseUri);
        Assert.assertEquals(baseUri, document.baseUri());
        Assert.assertTrue(document.select("h1").size() > 0);
    }

    @Test
    public void testLoad_File_NullCharset() throws IOException {
        Document document = DataUtil.load(file, null, baseUri);
        Assert.assertEquals(baseUri, document.baseUri());
        Assert.assertTrue(document.select("h1").size() > 0);
    }

    @Test
    public void testLoad_InputStream_ValidCharset() throws IOException {
        Document document = DataUtil.load(inputStream, CHARSET_UTF8, baseUri);
        Assert.assertEquals(baseUri, document.baseUri());
        Assert.assertTrue(document.select("h1").size() > 0);
    }

    @Test
    public void testLoad_InputStream_InvalidCharset() throws IOException {
        Document document = DataUtil.load(inputStream, "INVALID_CHARSET", baseUri);
        Assert.assertEquals(baseUri, document.baseUri());
        Assert.assertTrue(document.select("h1").size() > 0);
    }

    @Test
    public void testLoad_InputStream_NullCharset() throws IOException {
        Document document = DataUtil.load(inputStream, null, baseUri);
        Assert.assertEquals(baseUri, document.baseUri());
        Assert.assertTrue(document.select("h1").size() > 0);
    }

    @Test
    public void testLoad_InputStream_CustomParser() throws IOException {
        Document document = DataUtil.load(inputStream, CHARSET_UTF8, baseUri, Parser.xmlParser());
        Assert.assertEquals(baseUri, document.baseUri());
        Assert.assertTrue(document.select("h1").size() > 0);
    }

    @Test
    public void testCrossStreams() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(inputStream, outputStream);
        byte[] bytes = outputStream.toByteArray();
        Assert.assertTrue(bytes.length > 0);
    }

    @Test
    public void testParseInputStream_ValidCharset() throws IOException {
        Document document = DataUtil.parseInputStream(inputStream, CHARSET_UTF8, baseUri, Parser.htmlParser());
        Assert.assertEquals(baseUri, document.baseUri());
        Assert.assertTrue(document.select("h1").size() > 0);
    }

    @Test
    public void testParseInputStream_InvalidCharset() throws IOException {
        Document document = DataUtil.parseInputStream(inputStream, "INVALID_CHARSET", baseUri, Parser.htmlParser());
        Assert.assertEquals(baseUri, document.baseUri());
        Assert.assertTrue(document.select("h1").size() > 0);
    }

    @Test
    public void testParseInputStream_NullCharset() throws IOException {
        Document document = DataUtil.parseInputStream(inputStream, null, baseUri, Parser.htmlParser());
        Assert.assertEquals(baseUri, document.baseUri());
        Assert.assertTrue(document.select("h1").size() > 0);
    }

    @Test
    public void testReadToByteBuffer_LargeSize() throws IOException {
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, bufferSize * 2); // exceeds first read buffer size
        Assert.assertTrue(byteBuffer.limit() > 0);
    }

    @Test
    public void testReadToByteBuffer_MaxSizeZero() throws IOException {
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 0); // unlimited
        Assert.assertTrue(byteBuffer.limit() > 0);
    }

    @Test
    public void testReadToByteBuffer_NormalSize() throws IOException {
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, bufferSize);
        Assert.assertTrue(byteBuffer.limit() > 0);
    }

    @Test
    public void testReadToByteBuffer_DefaultSize() throws IOException {
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        Assert.assertTrue(byteBuffer.limit() > 0);
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
        Assert.assertTrue(byteBuffer.limit() > 0);
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        Assert.assertTrue(byteBuffer.limit() == 0);
    }

    @Test
    public void testGetCharsetFromContentType_Valid() {
        String contentType = "text/html;charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals(CHARSET_UTF8, charset);
    }

    @Test
    public void testGetCharsetFromContentType_Invalid() {
        String contentType = "text/html;encoding=INVALID_CHARSET";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertNull(charset);
    }

    @Test
    public void testValidateCharset_Valid() {
        String charset = "UTF-8";
        String validatedCharset = DataUtil.validateCharset(charset);
        Assert.assertEquals(charset, validatedCharset);
    }

    @Test
    public void testValidateCharset_Invalid() {
        String charset = "INVALID_CHARSET";
        String validatedCharset = DataUtil.validateCharset(charset);
        Assert.assertNull(validatedCharset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        Assert.assertNotNull(boundary);
        Assert.assertEquals(DataUtil.boundaryLength, boundary.length());
    }

    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer byteData = ByteBuffer.wrap(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});
        DataUtil.BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteData);
        Assert.assertNotNull(bomCharset);
        Assert.assertEquals(CHARSET_UTF8, bomCharset.charset);
    }
}