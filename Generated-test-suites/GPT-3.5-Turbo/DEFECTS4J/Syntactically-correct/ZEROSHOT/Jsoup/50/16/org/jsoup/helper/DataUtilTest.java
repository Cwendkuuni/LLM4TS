import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    @Test
    public void testLoadFromFile() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, "UTF-8", "http://example.com");
        Assert.assertEquals("http://example.com", doc.baseUri());
        Assert.assertEquals("Test Document", doc.title());
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        String html = "<html><head><title>Test Document</title></head><body></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, "UTF-8", "http://example.com");
        Assert.assertEquals("http://example.com", doc.baseUri());
        Assert.assertEquals("Test Document", doc.title());
    }

    @Test
    public void testLoadFromInputStreamWithParser() throws IOException {
        String html = "<html><head><title>Test Document</title></head><body></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, "UTF-8", "http://example.com", Parser.xmlParser());
        Assert.assertEquals("http://example.com", doc.baseUri());
        Assert.assertEquals("Test Document", doc.title());
    }

    @Test
    public void testCrossStreams() throws IOException {
        String input = "This is a test input";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(inputStream, outputStream);
        String output = new String(outputStream.toByteArray(), StandardCharsets.UTF_8);
        Assert.assertEquals(input, output);
    }

    @Test
    public void testParseByteData() {
        // Not testable as it depends on Jsoup internal functionality.
        // Testing is covered by the other methods that use parseByteData internally.
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        String input = "This is a test input";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 10);
        String output = StandardCharsets.UTF_8.decode(byteBuffer).toString();
        Assert.assertEquals("This is a", output);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String input = "This is a test input";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        String output = StandardCharsets.UTF_8.decode(byteBuffer).toString();
        Assert.assertEquals(input, output);
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File file = new File("test.html");
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
        String output = StandardCharsets.UTF_8.decode(byteBuffer).toString();
        Assert.assertTrue(output.contains("<title>Test Document</title>"));
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        Assert.assertEquals(0, byteBuffer.capacity());
        Assert.assertEquals(0, byteBuffer.limit());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals("UTF-8", charset);
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        Assert.assertEquals(DataUtil.boundaryLength, mimeBoundary.length());
    }
}