import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Random;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html"); // Sample HTML file
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";

        Document document = DataUtil.load(file, charsetName, baseUri);
        String title = document.title();

        Assert.assertEquals("Test HTML File", title);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/test.html"); // Sample HTML file
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";

        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        String title = document.title();

        Assert.assertEquals("Test HTML File", title);
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/test.html"); // Sample HTML file
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
        String title = document.title();

        Assert.assertEquals("Test HTML File", title);
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/test.html"); // Sample HTML file
        OutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);

        // Assert the output stream contains the same data as the input stream
        String expectedHtml = getResourceAsString("/test.html");
        String actualHtml = outputStream.toString();
        Assert.assertEquals(expectedHtml, actualHtml);
    }

    @Test
    public void testParseByteData() {
        String html = "<html><head><title>Test HTML</title></head><body><h1>Hello, World!</h1></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(html.getBytes());
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        String title = document.title();

        Assert.assertEquals("Test HTML", title);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/test.html"); // Sample HTML file

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);

        // Assert the byte buffer contains the same data as the input stream
        String expectedHtml = getResourceAsString("/test.html");
        String actualHtml = new String(byteBuffer.array());
        Assert.assertEquals(expectedHtml, actualHtml);
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/test.html"); // Sample HTML file
        int maxSize = 1024; // Maximum size of 1KB

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);

        // Assert the byte buffer contains the same data as the input stream, up to the maximum size
        String expectedHtml = getResourceAsString("/test.html");
        String actualHtml = new String(byteBuffer.array(), 0, maxSize);
        Assert.assertEquals(expectedHtml.substring(0, maxSize), actualHtml);
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File file = new File("test.html"); // Sample HTML file

        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);

        // Assert the byte buffer contains the same data as the file
        String expectedHtml = getResourceAsString("/test.html");
        String actualHtml = new String(byteBuffer.array());
        Assert.assertEquals(expectedHtml, actualHtml);
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

        Assert.assertNotNull(mimeBoundary);
        Assert.assertEquals(DataUtil.boundaryLength, mimeBoundary.length());
    }

    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF, 0x3C, 0x68, 0x74, 0x6D, 0x6C, 0x3E}); // UTF-8 BOM + "<html>"

        String charsetName = DataUtil.detectCharsetFromBom(byteBuffer, null);

        Assert.assertEquals("UTF-8", charsetName);
        Assert.assertEquals(3, byteBuffer.position());
    }
    
    // Additional test cases can be added to cover all methods and edge/error scenarios
    
    private String getResourceAsString(String name) throws IOException {
        InputStream inputStream = getClass().getResourceAsStream(name);
        byte[] buffer = new byte[inputStream.available()];
        inputStream.read(buffer);
        return new String(buffer);
    }
}