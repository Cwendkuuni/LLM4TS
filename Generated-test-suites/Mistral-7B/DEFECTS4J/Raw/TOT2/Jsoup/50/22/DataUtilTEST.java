import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
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
        File file = new File("test.html");
        file.createNewFile(); // create a new file for testing
        try (Writer writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(fileContent);
        }

        Document document = DataUtil.load(file, "UTF-8", null);
        Assert.assertEquals("This is a test file.", document.body().text());
        file.delete(); // clean up after test
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String inputStreamContent = "This is a test input stream.";
        byte[] contentAsBytes = inputStreamContent.getBytes(StandardCharsets.UTF_8);
        InputStream inputStream = new ByteArrayInputStream(contentAsBytes);

        Document document = DataUtil.load(inputStream, "UTF-8", null);
        Assert.assertEquals("This is a test input stream.", document.body().text());
    }

    @Test
    public void testLoadInputStreamWithCharsetNameNull() throws IOException {
        String inputStreamContent = "This is a test input stream.";
        byte[] contentAsBytes = inputStreamContent.getBytes(StandardCharsets.UTF_8);
        InputStream inputStream = new ByteArrayInputStream(contentAsBytes);

        Document document = DataUtil.load(inputStream, null, null);
        Assert.assertEquals("This is a test input stream.", document.body().text());
    }

    @Test
    public void testLoadInputStreamWithInvalidCharsetName() throws IOException {
        String inputStreamContent = "This is a test input stream.";
        byte[] contentAsBytes = inputStreamContent.getBytes(StandardCharsets.UTF_8);
        InputStream inputStream = new ByteArrayInputStream(contentAsBytes);

        try {
            Document document = DataUtil.load(inputStream, "invalidCharset", null);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected exception
        }
    }

    @Test
    public void testLoadInputStreamWithBaseUri() throws IOException {
        String baseUri = "http://example.com/test/";
        String inputStreamContent = "<html><head><title>Test</title></head><body><h1>Test</h1></body></html>";
        byte[] contentAsBytes = inputStreamContent.getBytes(StandardCharsets.UTF_8);
        InputStream inputStream = new ByteArrayInputStream(contentAsBytes);

        Document document = DataUtil.load(inputStream, null, baseUri);
        Assert.assertEquals("<html><head><title>Test</title></head><body><h1>Test</h1></body></html>", document.toString());
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String inputStreamContent = "<xml><tag>Test</tag></xml>";
        byte[] contentAsBytes = inputStreamContent.getBytes(StandardCharsets.UTF_8);
        InputStream inputStream = new ByteArrayInputStream(contentAsBytes);

        Document document = DataUtil.load(inputStream, null, null, DataUtil.Parser.xmlParser());
        Assert.assertEquals("<xml><tag>Test</tag></xml>", document.toString());
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputContent = "This is a test.";
        String outputContent = "Another test.";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream(inputContent.getBytes(StandardCharsets.UTF_8));
        OutputStream output = outputStream;

        DataUtil.crossStreams(inputStream, output);
        Assert.assertEquals(inputContent, new String(outputStream.toByteArray(), StandardCharsets.UTF_8));
    }

    @Test
    public void testParseByteDataWithDefaultCharset() throws IOException {
        String byteDataContent = "<html><head><title>Test</title></head><body><h1>Test</h1></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(byteDataContent.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        Assert.assertEquals("<html><head><title>Test</title></head><body><h1>Test</h1></body></html>", document.toString());
    }

    @Test
    public void testParseByteDataWithCustomCharset() throws IOException {
        String byteDataContent = "<html><head><title>Test</title></head><body><h1>Test</h1></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(byteDataContent.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.parseByteData(byteData, "UTF-16LE", null, Parser.htmlParser());
        Assert.assertEquals("<html><head><title>Test</title></head><body><h1>Test</h1></body></html>", document.toString());
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        String inputContent = "This is a test.";
        InputStream inputStream = new ByteArrayInputStream(inputContent.getBytes(StandardCharsets.UTF_8));
        int maxSize = 10;

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);
        Assert.assertEquals(10, byteBuffer.limit());
    }

    @Test
    public void testReadToByteBufferWithoutMaxSize() throws IOException {
        String inputContent = "This is a test.";
        InputStream inputStream = new ByteArrayInputStream(inputContent.getBytes(StandardCharsets.UTF_8));

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        Assert.assertEquals(inputContent.length(), byteBuffer.limit());
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        String fileContent = "This is a test file.";
        File file = new File("test.html");
        file.createNewFile(); // create a new file for testing

        try (Writer writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(fileContent);
        }

        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
        Assert.assertEquals(fileContent.length(), byteBuffer.limit());
        file.delete(); // clean up after test
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals("UTF-8", charset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        Assert.assertEquals(32, boundary.length());
    }
}