import org.jsoup.helper.DataUtil;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String filePath = "path/to/your/test/file.html"; // Replace this with the path to your test file
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";

        Document document = DataUtil.load(new File(filePath), charsetName, baseUri);
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String testData = "<html><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";

        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        Assert.assertNotNull(document);
        Assert.assertEquals("Hello World", document.title());
    }

    @Test
    public void testParseInputStream() throws IOException {
        String testData = "<html><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";

        Document document = DataUtil.parseInputStream(inputStream, charsetName, baseUri, Parser.htmlParser());
        Assert.assertNotNull(document);
        Assert.assertEquals("Hello World", document.title());
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputData = "Test data to be read from InputStream and written to OutputStream.";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));
        OutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);
        String expectedOutputData = "Test data to be read from InputStream and written to OutputStream.";
        String outputData = new String(outputStream.toByteArray(), StandardCharsets.UTF_8);

        Assert.assertEquals(expectedOutputData, outputData);
    }

    @Test
    public void testParseInputStreamWithCustomParser() throws IOException {
        String testData = "<html xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xhtml=\"http://www.w3.org/1999/xhtml\"><head><title>Test Document</title></head><body><p>Test data</p></body></html>";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";
        Parser parser = Parser.xmlParser(); // You can use other Parser implementations as well

        Document document = DataUtil.parseInputStream(inputStream, charsetName, baseUri, parser);
        Assert.assertNotNull(document);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String testData = "Test data to be read into ByteBuffer.";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));
        int maxSize = testData.length();

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);
        Assert.assertNotNull(byteBuffer);
        Assert.assertEquals(testData.length(), byteBuffer.limit());
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        String filePath = "path/to/your/test/file.html"; // Replace this with the path to your test file

        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(new File(filePath));
        Assert.assertNotNull(byteBuffer);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=ISO-8859-1";
        String expectedCharset = "ISO-8859-1";

        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals(expectedCharset, charset);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidateCharsetWithInvalidCharset() {
        String invalidCharset = "invalidCharset";

        DataUtil.validateCharset(invalidCharset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        Assert.assertNotNull(boundary);
        Assert.assertEquals(boundary.length(), 32);
    }
}