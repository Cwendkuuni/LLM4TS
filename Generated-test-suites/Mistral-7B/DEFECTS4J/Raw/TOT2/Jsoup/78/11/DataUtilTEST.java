import org.jsoup.helper.DataUtil;
import org.jsoup.helper.Parser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        File inputFile = new File("test.html");
        Document doc = DataUtil.load(inputFile, null);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        File inputFile = new File("test.html");
        Document doc = DataUtil.load(inputFile, "UTF-8");
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String htmlContent = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, null);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        String htmlContent = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, "UTF-8");
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String htmlContent = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, null, Parser.xmlParser());
        Assert.assertNotNull(doc);
    }

    @Test
    public void testCrossStreams() throws IOException {
        File inputFile = new File("test.html");
        File outputFile = new File("test_output.html");
        InputStream inputStream = new FileInputStream(inputFile);
        OutputStream outputStream = new FileOutputStream(outputFile);

        DataUtil.crossStreams(inputStream, outputStream);

        inputStream.close();
        outputStream.close();

        Assert.assertTrue(outputFile.length() > 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadFileWithNullInput() throws IOException {
        Document doc = DataUtil.load(null, null);
        Assert.fail("Expected IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadInputStreamWithNullInput() throws IOException {
        Document doc = DataUtil.load((InputStream) null, null);
        Assert.fail("Expected IllegalArgumentException");
    }

    @Test
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String htmlContent = "<?xml version=\"1.0\" encoding=\"ISO-8859-15\"?>\r\n<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.parseInputStream(inputStream, null, null, Parser.xmlParser(), outputStream);

        Assert.assertEquals(htmlContent, outputStream.toString(StandardCharsets.UTF_8.name()));
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        File inputFile = new File("test.html");
        InputStream inputStream = new FileInputStream(inputFile);
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 1024 * 1024); // 1 MB
        Assert.assertNotNull(byteBuffer);
    }

    @Test
    public void testReadToByteBufferWithUnlimitedSize() throws IOException {
        File inputFile = new File("test.html");
        InputStream inputStream = new FileInputStream(inputFile);
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        Assert.assertNotNull(byteBuffer);
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File file = new File("test.html");
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
        Assert.assertNotNull(byteBuffer);
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        Assert.assertNotNull(byteBuffer);
        Assert.assertEquals(0, byteBuffer.limit());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals("UTF-8", charset);

        contentType = "text/html; charset=gb2312";
        charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals("gb2312", charset);

        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals(null, charset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        Assert.assertNotNull(boundary);
        Assert.assertEquals(32, boundary.length());
    }
}