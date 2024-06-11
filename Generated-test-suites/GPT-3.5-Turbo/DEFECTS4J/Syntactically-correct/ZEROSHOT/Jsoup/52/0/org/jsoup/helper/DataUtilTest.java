import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class DataUtilTest {

    @Test
    public void testLoadFromFile() throws IOException {
        File file = new File("test.html");
        Document document = DataUtil.load(file, "UTF-8", "http://example.com");
        Assert.assertEquals("http://example.com", document.baseUri());
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        String html = "<html><head><title>Test</title></head><body>Hello world!</body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com");
        Assert.assertEquals("http://example.com", document.baseUri());
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoadFromInputStreamWithParser() throws IOException {
        String html = "<html><head><title>Test</title></head><body>Hello world!</body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com", Parser.htmlParser());
        Assert.assertEquals("http://example.com", document.baseUri());
        Assert.assertNotNull(document);
    }

    @Test
    public void testCrossStreams() throws IOException {
        String input = "Hello world!";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        ByteBuffer byteBuffer = ByteBuffer.allocate(input.length());
        DataUtil.crossStreams(inputStream, byteBuffer);
        byteBuffer.flip();
        String output = new String(byteBuffer.array());
        Assert.assertEquals(input, output);
    }

    @Test
    public void testParseByteData() throws IOException {
        String html = "<html><head><title>Test</title></head><body>Hello world!</body></html>";
        ByteBuffer byteBuffer = ByteBuffer.wrap(html.getBytes());
        Document document = DataUtil.parseByteData(byteBuffer, "UTF-8", "http://example.com", Parser.htmlParser());
        Assert.assertEquals("http://example.com", document.baseUri());
        Assert.assertNotNull(document);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String input = "Hello world!";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        byteBuffer.flip();
        String output = new String(byteBuffer.array());
        Assert.assertEquals(input, output);
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File file = new File("test.txt");
        String input = "Hello world!";
        byte[] bytes = input.getBytes();
        ByteBuffer expectedByteBuffer = ByteBuffer.wrap(bytes);
        DataUtil.writeFile(file, bytes);
        ByteBuffer actualByteBuffer = DataUtil.readFileToByteBuffer(file);
        Assert.assertEquals(expectedByteBuffer, actualByteBuffer);
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        Assert.assertEquals(0, byteBuffer.capacity());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String expectedCharset = "UTF-8";
        String actualCharset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals(expectedCharset, actualCharset);
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        Assert.assertNotNull(mimeBoundary);
        Assert.assertEquals(DataUtil.boundaryLength, mimeBoundary.length());
    }

    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[] {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF, 0x41, 0x42, 0x43});
        String charsetName = DataUtil.detectCharsetFromBom(byteBuffer, null);
        Assert.assertEquals("UTF-8", charsetName);
    }
}