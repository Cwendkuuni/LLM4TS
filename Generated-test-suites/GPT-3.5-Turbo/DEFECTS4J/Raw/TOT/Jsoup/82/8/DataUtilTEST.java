
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        // Arrange
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";

        // Act
        Document document = DataUtil.load(file, charsetName, baseUri);

        // Assert
        Assert.assertNotNull(document);
        Assert.assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testLoadFile_InvalidFile() throws IOException {
        // Arrange
        File file = new File("invalid.html");
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";

        // Act
        Document document = DataUtil.load(file, charsetName, baseUri);

        // Assert
        Assert.assertNotNull(document);
        Assert.assertEquals(baseUri, document.baseUri());
        Assert.assertEquals(0, document.childNodeSize());
    }

    @Test
    public void testLoadInputStream() throws IOException {
        // Arrange
        String html = "<html><head></head><body><p>Hello World!</p></body></html>";
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";

        // Act
        Document document = DataUtil.load(inputStream, charsetName, baseUri);

        // Assert
        Assert.assertNotNull(document);
        Assert.assertEquals(baseUri, document.baseUri());
        Assert.assertEquals(html, document.html());
    }

    @Test
    public void testLoadInputStream_InvalidInputStream() throws IOException {
        // Arrange
        InputStream inputStream = null;
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";

        // Act
        Document document = DataUtil.load(inputStream, charsetName, baseUri);

        // Assert
        Assert.assertNotNull(document);
        Assert.assertEquals(baseUri, document.baseUri());
        Assert.assertEquals(0, document.childNodeSize());
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        // Arrange
        String html = "<html><head></head><body><p>Hello World!</p></body></html>";
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Parser parser = Parser.htmlParser();

        // Act
        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);

        // Assert
        Assert.assertNotNull(document);
        Assert.assertEquals(baseUri, document.baseUri());
        Assert.assertEquals(html, document.html());
    }

    @Test
    public void testCrossStreams() throws IOException {
        // Arrange
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Document document = DataUtil.load(inputStream, charsetName, baseUri);

        // Act
        OutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(inputStream, outputStream);
        String output = outputStream.toString();

        // Assert
        Assert.assertNotNull(output);
        Assert.assertEquals(document.html(), Jsoup.parse(output).html());
    }

    @Test
    public void testParseInputStream() throws IOException {
        // Arrange
        String html = "<html><head></head><body><p>Hello World!</p></body></html>";
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Parser parser = Parser.htmlParser();

        // Act
        Document document = DataUtil.parseInputStream(inputStream, charsetName, baseUri, parser);

        // Assert
        Assert.assertNotNull(document);
        Assert.assertEquals(baseUri, document.baseUri());
        Assert.assertEquals(html, document.html());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        // Arrange
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        int maxSize = 1024;

        // Act
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);

        // Assert
        Assert.assertNotNull(byteBuffer);
        Assert.assertEquals(maxSize, byteBuffer.limit());
    }
    
    @Test
    public void testReadToByteBuffer_ZeroMaxSize() throws IOException {
        // Arrange
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        int maxSize = 0;

        // Act
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);

        // Assert
        Assert.assertNotNull(byteBuffer);
        Assert.assertTrue(byteBuffer.limit() > 0);
    }

    @Test
    public void testGetCharsetFromContentType() {
        // Arrange
        String contentType = "text/html; charset=UTF-8";

        // Act
        String charset = DataUtil.getCharsetFromContentType(contentType);

        // Assert
        Assert.assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentType_NoCharset() {
        // Arrange
        String contentType = "text/html";

        // Act
        String charset = DataUtil.getCharsetFromContentType(contentType);

        // Assert
        Assert.assertNull(charset);
    }

    @Test
    public void testMimeBoundary() {
        // Act
        String mimeBoundary = DataUtil.mimeBoundary();

        // Assert
        Assert.assertNotNull(mimeBoundary);
        Assert.assertEquals(DataUtil.boundaryLength, mimeBoundary.length());
    }

    @Test
    public void testDetectCharsetFromBom() {
        // Arrange
        ByteBuffer byteBuffer = ByteBuffer.allocate(4);
        byteBuffer.put((byte) 0xEF);
        byteBuffer.put((byte) 0xBB);
        byteBuffer.put((byte) 0xBF);
        byteBuffer.put((byte) 0x41);

        // Act
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);

        // Assert
        Assert.assertNotNull(bomCharset);
        Assert.assertEquals("UTF-8", bomCharset.charset);
        Assert.assertTrue(bomCharset.offset);
    }
}