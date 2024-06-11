import org.jsoup.helper.DataUtil;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    private static File file;
    private static InputStream inputStream;
    private static final String defaultCharset = "UTF-8";

    @BeforeClass
    public static void setUp() throws IOException {
        file = new File("test.html");
        FileWriter writer = new FileWriter(file);
        writer.write("<html><head><title>Test HTML</title></head><body><h1>Test</h1></body></html>");
        writer.close();
        inputStream = new FileInputStream(file);
    }

    @Test
    public void testLoadWithFile() throws IOException {
        Document doc = DataUtil.load(file, defaultCharset, "");
        Assert.assertNotNull(doc);
        Assert.assertEquals("Test HTML", doc.title());
        Assert.assertEquals("h1", doc.select("h1").first().tagName());
    }

    @Test
    public void testLoadWithInputStream() throws IOException {
        Document doc = DataUtil.load(inputStream, defaultCharset, "");
        Assert.assertNotNull(doc);
        Assert.assertEquals("Test HTML", doc.title());
        Assert.assertEquals("h1", doc.select("h1").first().tagName());
    }

    @Test
    public void testLoadWithInputStreamAndParser() throws IOException {
        Document doc = DataUtil.load(inputStream, defaultCharset, "", Parser.htmlParser());
        Assert.assertNotNull(doc);
        Assert.assertEquals("Test HTML", doc.title());
        Assert.assertEquals("h1", doc.select("h1").first().tagName());
    }

    @Test
    public void testParseByteDataWithCharset() {
        byte[] data = "<html><head><title>Test HTML</title></head><body><h1>Test</h1></body></html>".getBytes(StandardCharsets.UTF_8);
        ByteBuffer byteData = ByteBuffer.wrap(data);
        Document doc = DataUtil.parseByteData(byteData, defaultCharset, "", Parser.htmlParser());
        Assert.assertNotNull(doc);
        Assert.assertEquals("Test HTML", doc.title());
        Assert.assertEquals("h1", doc.select("h1").first().tagName());
    }

    @Test
    public void testParseByteDataWithoutCharset() {
        byte[] data = "<html><head><title>Test HTML</title></head><body><h1>Test</h1></body></html>".getBytes(StandardCharsets.UTF_8);
        ByteBuffer byteData = ByteBuffer.wrap(data);
        Document doc = DataUtil.parseByteData(byteData, null, "", Parser.htmlParser());
        Assert.assertNotNull(doc);
        Assert.assertEquals("Test HTML", doc.title());
        Assert.assertEquals("h1", doc.select("h1").first().tagName());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        byte[] data = "<html><head><title>Test HTML</title></head><body><h1>Test</h1></body></html>".getBytes(StandardCharsets.UTF_8);
        InputStream inputStream = new ByteArrayInputStream(data);
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        Assert.assertNotNull(byteData);
        Assert.assertEquals(data.length, byteData.array().length);
    }

    @Test
    public void testGetCharsetFromContentTypeWithCharsetDefined() {
        String contentType = "text/html; charset=EUC-JP";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals("EUC-JP", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithoutCharsetDefined() {
        String contentType = "text/html";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertNull(charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidCharset() {
        String contentType = "text/html; charset=InvalidCharset";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertNull(charset);
    }

    @Test(expected = IOException.class)
    public void testLoadWithInvalidFile() throws IOException {
        File invalidFile = new File("invalid.html");
        DataUtil.load(invalidFile, defaultCharset, "");
    }

    @Test(expected = IOException.class)
    public void testLoadWithInvalidInputStream() throws IOException {
        InputStream invalidInputStream = new FileInputStream("invalid.html");
        DataUtil.load(invalidInputStream, defaultCharset, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadWithoutCharset() throws IOException {
        DataUtil.load(file, "", "");
    }
}