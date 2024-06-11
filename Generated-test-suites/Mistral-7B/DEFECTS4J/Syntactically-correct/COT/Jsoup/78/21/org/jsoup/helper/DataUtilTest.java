import org.jsoup.helper.DataUtil;
import org.jsoup.helper.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html"); // replace with actual file
        String charsetName = StandardCharsets.UTF_8.name();
        String baseUri = "http://test.com";
        Document document = DataUtil.load(file, charsetName, baseUri);
        // add assertions here
    }

    @Test
    public void testLoadInputStream() throws IOException, ParseException {
        String htmlContent = "<html><head><meta charset=\"UTF-8\"></head><body>Test</body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        String charsetName = StandardCharsets.UTF_8.name();
        String baseUri = "http://test.com";
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        // add assertions here
    }

    @Test
    public void testLoadInputStreamWithCustomParser() throws IOException, ParseException {
        String xmlContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><element>Test</element></root>";
        InputStream inputStream = new ByteArrayInputStream(xmlContent.getBytes(StandardCharsets.UTF_8));
        String charsetName = StandardCharsets.UTF_8.name();
        String baseUri = "http://test.com";
        Parser parser = DataUtil.Parser.xmlParser();
        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
        // add assertions here
    }

    @Test
    public void testCrossStreams() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Test".getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(inputStream, outputStream);
        String expectedOutput = "Test";
        String actualOutput = outputStream.toString();
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test(expected = IOException.class)
    public void testParseInputStreamWithInvalidCharset() throws IOException, ParseException {
        InputStream inputStream = new ByteArrayInputStream("InvalidContentType".getBytes(StandardCharsets.UTF_8));
        String charsetName = "InvalidCharset";
        String baseUri = "http://test.com";
        Document document = null;
        try {
            document = DataUtil.parseInputStream(inputStream, charsetName, baseUri, DataUtil.Parser.htmlParser());
            Assert.fail(); // expected ParseException
        } catch (ParseException e) {
            // expected
        }
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String testString = "TestString";
        InputStream inputStream = new ByteArrayInputStream(testString.getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        byte[] expectedBytes = testString.getBytes(StandardCharsets.UTF_8);
        byte[] actualBytes = byteBuffer.array();
        Assert.assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String expectedCharset = "UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals(expectedCharset, charset);
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        String expectedCharset = "UTF-8";
        String validatedCharset = DataUtil.validateCharset(charset);
        Assert.assertEquals(expectedCharset, validatedCharset);

        String invalidCharset = "invalidCharset";
        String actualCharset = DataUtil.validateCharset(invalidCharset);
        Assert.assertNull(actualCharset);
    }

    @Test
    public void testMimeBoundary() {
        String expectedMimeBoundary = "----WebKitFormBoundary7MA4YWxkTrZUjhsjMWxzFlswRnJlbWlz";
        String mimeBoundary = DataUtil.mimeBoundary();
        Assert.assertEquals(expectedMimeBoundary, mimeBoundary);
    }
}