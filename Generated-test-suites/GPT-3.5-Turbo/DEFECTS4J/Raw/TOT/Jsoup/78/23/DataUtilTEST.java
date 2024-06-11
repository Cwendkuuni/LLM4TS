import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        Document document = DataUtil.load(file, StandardCharsets.UTF_8.name(), "http://www.example.com");
        Assert.assertNotNull(document);
        Assert.assertEquals("http://www.example.com", document.baseUri());
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        Document document = DataUtil.load(inputStream, StandardCharsets.UTF_16.name(), "http://www.example.com");
        Assert.assertNotNull(document);
        Assert.assertEquals("http://www.example.com", document.baseUri());
    }

    @Test
    public void testLoadWithParser() throws IOException {
        InputStream inputStream = new FileInputStream("test.xml");
        Document document = DataUtil.load(inputStream, StandardCharsets.UTF_8.name(), "http://www.example.com", Parser.xmlParser());
        Assert.assertNotNull(document);
        Assert.assertEquals("http://www.example.com", document.baseUri());
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Hello World!".getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(inputStream, outputStream);
        String output = outputStream.toString(StandardCharsets.UTF_8.name());
        Assert.assertEquals("Hello World!", output);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        Assert.assertNotNull(byteBuffer);
        Assert.assertEquals(12204, byteBuffer.capacity());
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 1024);
        Assert.assertNotNull(byteBuffer);
        Assert.assertEquals(1024, byteBuffer.capacity());
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File file = new File("test.html");
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
        Assert.assertNotNull(byteBuffer);
        Assert.assertEquals(12204, byteBuffer.capacity());
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        Assert.assertNotNull(byteBuffer);
        Assert.assertEquals(0, byteBuffer.capacity());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromInvalidContentType() {
        String contentType = "text/html; charset=INVALID";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertNull(charset);
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        Assert.assertNotNull(mimeBoundary);
        Assert.assertEquals(32, mimeBoundary.length());
    }

    @Test
    public void testParseInputStream() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        Document document = DataUtil.parseInputStream(inputStream, StandardCharsets.UTF_8.name(), "http://www.example.com", Parser.htmlParser());
        Assert.assertNotNull(document);
        Assert.assertEquals("http://www.example.com", document.baseUri());
    }

    @Test
    public void testParseInputStreamEmpty() throws IOException {
        InputStream inputStream = null;
        Document document = DataUtil.parseInputStream(inputStream, StandardCharsets.UTF_8.name(), "http://www.example.com", Parser.htmlParser());
        Assert.assertNotNull(document);
        Assert.assertEquals("http://www.example.com", document.baseUri());
    }

    @Test
    public void testParseInputStreamWithNoCharset() throws IOException {
        InputStream inputStream = new FileInputStream("test_no_charset.html");
        Document document = DataUtil.parseInputStream(inputStream, null, "http://www.example.com", Parser.htmlParser());
        Assert.assertNotNull(document);
        Assert.assertEquals("http://www.example.com", document.baseUri());
    }

    @Test
    public void testParseInputStreamWithCharsetFromMeta() throws IOException {
        InputStream inputStream = new FileInputStream("test_charset_meta.html");
        Document document = DataUtil.parseInputStream(inputStream, null, "http://www.example.com", Parser.htmlParser());
        Assert.assertNotNull(document);
        Assert.assertEquals("http://www.example.com", document.baseUri());
    }

    @Test
    public void testParseInputStreamWithCharsetFromProlog() throws IOException {
        InputStream inputStream = new FileInputStream("test_charset_prolog.html");
        Document document = DataUtil.parseInputStream(inputStream, null, "http://www.example.com", Parser.htmlParser());
        Assert.assertNotNull(document);
        Assert.assertEquals("http://www.example.com", document.baseUri());
    }

    @Test
    public void testValidateCharset() {
        String validCharset = "UTF-8";
        String validatedCharset = DataUtil.validateCharset(validCharset);
        Assert.assertEquals("UTF-8", validatedCharset);
    }

    @Test
    public void testValidateInvalidCharset() {
        String invalidCharset = "INVALID";
        String validatedCharset = DataUtil.validateCharset(invalidCharset);
        Assert.assertNull(validatedCharset);
    }

    @Test
    public void testDetectCharsetFromBom() throws IOException {
        InputStream inputStream = new FileInputStream("test_bom_charset.html");
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        DataUtil.BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);
        Assert.assertNotNull(bomCharset);
        Assert.assertEquals("UTF-16", bomCharset.charset);
        Assert.assertEquals(0, bomCharset.offset);
    }

    @Test
    public void testDetectCharsetFromInvalidBom() throws IOException {
        InputStream inputStream = new FileInputStream("test_no_bom_charset.html");
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        DataUtil.BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);
        Assert.assertNull(bomCharset);
    }

}