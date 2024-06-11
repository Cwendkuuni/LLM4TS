import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    // Test load(File in, String charsetName, String baseUri) method
    @Test
    public void testLoadFromFile() throws IOException {
        File file = new File("test.html");

        Document document = DataUtil.load(file, "UTF-8", "http://example.com");
        
        Assert.assertNotNull(document);
        Assert.assertEquals("http://example.com", document.baseUri());
    }

    // Test load(InputStream in, String charsetName, String baseUri) method
    @Test
    public void testLoadFromInputStream() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");

        Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com");

        Assert.assertNotNull(document);
        Assert.assertEquals("http://example.com", document.baseUri());
    }

    // Test load(InputStream in, String charsetName, String baseUri, Parser parser) method
    @Test
    public void testLoadFromInputStreamWithCustomParser() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.xml");

        Parser parser = Parser.xmlParser();

        Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com", parser);

        Assert.assertNotNull(document);
        Assert.assertEquals("http://example.com", document.baseUri());
    }

    // Test crossStreams(InputStream in, OutputStream out) method
    @Test
    public void testCrossStreams() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.txt");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);

        String expectedOutput = "This is a test file.";

        Assert.assertEquals(expectedOutput, outputStream.toString());
    }

    // Test parseByteData(ByteBuffer byteData, String charsetName, String baseUri, Parser parser) method
    @Test
    public void testParseByteDataWithCharsets() {
        String inputString = "<html><head><meta charset=\"UTF-16\"></head><body>Hello World!</body></html>";
        ByteBuffer byteBuffer = ByteBuffer.wrap(inputString.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.parseByteData(byteBuffer, null, "http://example.com", Parser.htmlParser());

        Assert.assertNotNull(document);
        Assert.assertEquals("UTF-16", document.charset().name());
        Assert.assertEquals("Hello World!", document.body().text());
    }

    // Test readToByteBuffer(InputStream inStream, int maxSize) method
    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.txt");

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 10);

        String expectedOutput = "This is a ";

        Assert.assertEquals(expectedOutput, new String(byteBuffer.array(), StandardCharsets.UTF_8));
    }

    // Test readToByteBuffer(InputStream inStream) method
    @Test
    public void testReadToByteBufferWithoutMaxSize() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.txt");

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);

        String expectedOutput = "This is a test file.";

        Assert.assertEquals(expectedOutput, new String(byteBuffer.array(), StandardCharsets.UTF_8));
    }

    // Test readFileToByteBuffer(File file) method
    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File file = new File("test.txt");

        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);

        String expectedOutput = "This is a test file.";

        Assert.assertEquals(expectedOutput, new String(byteBuffer.array(), StandardCharsets.UTF_8));
    }

    // Test getCharsetFromContentType(String contentType) method
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";

        String charset = DataUtil.getCharsetFromContentType(contentType);

        Assert.assertEquals("UTF-8", charset);
    }

    // Test mimeBoundary() method
    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();

        Assert.assertNotNull(mimeBoundary);
        Assert.assertEquals(DataUtil.boundaryLength, mimeBoundary.length());
    }
}