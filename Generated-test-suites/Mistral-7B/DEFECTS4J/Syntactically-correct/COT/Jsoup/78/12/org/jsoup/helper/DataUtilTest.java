import org.jsoup.helper.DataUtil;
import org.jsoup.helper.Parser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html"); // Replace with the path to an actual HTML file
        Document doc = DataUtil.load(file, "UTF-8", "");
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String htmlContent = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-8"));
        Document doc = DataUtil.load(inputStream, "UTF-8", "");
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String htmlContent = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-8"));
        Document doc = DataUtil.load(inputStream, "UTF-8", "", Parser.htmlParser());
        Assert.assertNotNull(doc);
    }

    @Test
    public void testCrossStreams() throws IOException {
        File inputFile = new File("test.html");
        File outputFile = new File("test_output.html");

        InputStream inputStream = new FileInputStream(inputFile);
        OutputStream outputStream = new FileOutputStream(outputFile);

        DataUtil.crossStreams(inputStream, outputStream);

        Assert.assertTrue(outputFile.length() > 0);
    }

    @Test
    public void testParseInputStream() throws IOException {
        String htmlContent = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-8"));
        Document doc = DataUtil.parseInputStream(inputStream, "UTF-8", "", Parser.htmlParser());
        Assert.assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testParseInputStreamInvalidCharset() throws IOException {
        String htmlContent = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-8"));
        Document doc = DataUtil.parseInputStream(inputStream, "ISO-8859-1", "", Parser.htmlParser()); // Invalid charset
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        File file = new File("test.html");
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(new FileInputStream(file));
        Assert.assertNotNull(byteBuffer);
    }

    @Test
    public void testReadToByteBufferEmpty() throws IOException {
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer((InputStream) () -> { });
        Assert.assertNotNull(byteBuffer);
        Assert.assertEquals(0, byteBuffer.limit());
    }

    @Test(expected = IOException.class)
    public void testReadToByteBufferWithException() throws IOException {
        InputStream inputStream = () -> { throw new IOException(); };
        DataUtil.readToByteBuffer(inputStream);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeEmpty() {
        String contentType = "text/html";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertNull(charset);
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        String validatedCharset = DataUtil.validateCharset(charset);
        Assert.assertEquals(charset, validatedCharset);
    }

    @Test
    public void testValidateCharsetNull() {
        String charset = null;
        String validatedCharset = DataUtil.validateCharset(charset);
        Assert.assertNull(validatedCharset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        Assert.assertNotNull(boundary);
        Assert.assertEquals(boundary.length(), 32);
    }
}