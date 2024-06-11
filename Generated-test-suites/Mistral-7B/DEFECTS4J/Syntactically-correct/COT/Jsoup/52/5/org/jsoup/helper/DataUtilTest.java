import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "This is a test file.";
        File file = new File("testFile.html");
        DataUtil.writeToFile(file, fileContent.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.load(file, StandardCharsets.UTF_8.name(), null);
        String content = document.body().text();
        Assert.assertEquals(fileContent, content);

        DataUtil.deleteFile(file);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String inputStreamContent = "This is a test input stream.";
        byte[] inputStreamData = inputStreamContent.getBytes(StandardCharsets.UTF_8);
        InputStream inputStream = new ByteArrayInputStream(inputStreamData);

        Document document = DataUtil.load(inputStream, StandardCharsets.UTF_8.name(), null);
        String content = document.body().text();
        Assert.assertEquals(inputStreamContent, content);

        inputStream.close();
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String inputStreamContent = "<html><body><h1>This is a test input stream.</h1></body></html>";
        byte[] inputStreamData = inputStreamContent.getBytes(StandardCharsets.UTF_8);
        InputStream inputStream = new ByteArrayInputStream(inputStreamData);

        Document document = DataUtil.load(inputStream, StandardCharsets.UTF_8.name(), null, DataUtil.Parser.xmlParser());
        Assert.assertEquals(inputStreamContent, document.html());

        inputStream.close();
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputContent = "This is a test.";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream(inputContent.getBytes(StandardCharsets.UTF_8));
        OutputStream output = outputStream;

        DataUtil.crossStreams(inputStream, output);
        inputStream.close();
        String outputContent = outputStream.toString(StandardCharsets.UTF_8.name());
        Assert.assertEquals(inputContent, outputContent);
    }

    @Test
    public void testParseByteDataWithDefaultCharset() throws IOException {
        String testData = "<html><body><h1>This is a test parseByteDataWithDefaultCharset.</h1></body></html>";
        byte[] testDataBytes = testData.getBytes(StandardCharsets.UTF_8);

        Document document = DataUtil.parseByteData(DataUtil.emptyByteBuffer(), null, null, DataUtil.Parser.htmlParser());
        DataUtil.putBytes(document.inputStream(), testDataBytes);
        document.inputStream().close();

        String content = document.body().text();
        Assert.assertEquals(testData, content);
    }

    @Test
    public void testParseByteDataWithCustomCharset() throws IOException {
        String testData = "<html><body><h1>This is a test parseByteDataWithCustomCharset.</h1></body></html>";
        byte[] testDataBytes = testData.getBytes("ISO-8859-1");

        Document document = DataUtil.parseByteData(DataUtil.emptyByteBuffer(), "ISO-8859-1", null, DataUtil.Parser.htmlParser());
        DataUtil.putBytes(document.inputStream(), testDataBytes);
        document.inputStream().close();

        String content = document.body().text();
        Assert.assertEquals(testData, content);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseByteDataWithInvalidCharset() {
        String testData = "<html><body><h1>This is a test parseByteDataWithInvalidCharset.</h1></body></html>";
        byte[] testDataBytes = testData.getBytes(StandardCharsets.UTF_8);

        Document document = DataUtil.parseByteData(DataUtil.emptyByteBuffer(), "INVALID_CHARSET", null, DataUtil.Parser.htmlParser());
        DataUtil.putBytes(document.inputStream(), testDataBytes);
        document.inputStream().close();
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals("UTF-8", charset);

        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals(null, charset);
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        Assert.assertEquals(32, mimeBoundary.length());
        for (int i = 0; i < mimeBoundary.length(); i++) {
            char c = mimeBoundary.charAt(i);
            Assert.assertTrue(Character.isAlphabetic(c) || Character.isDigit(c) || c == '-' || c == '_');
        }
    }
}