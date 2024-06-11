import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.nio.charset.IllegalCharsetNameException;

public class DataUtilTest {

    @Test
    public void testLoad_withValidFile_shouldReturnDocument() throws IOException {
        // Arrange
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        // Act
        Document document = DataUtil.load(file, charsetName, baseUri);

        // Assert
        Assert.assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testLoad_withInvalidFile_shouldThrowIOException() throws IOException {
        // Arrange
        File file = new File("invalid.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        // Act
        DataUtil.load(file, charsetName, baseUri);
    }

    @Test
    public void testLoad_withValidInputStream_shouldReturnDocument() throws IOException {
        // Arrange
        InputStream inputStream = new ByteArrayInputStream("Hello".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        // Act
        Document document = DataUtil.load(inputStream, charsetName, baseUri);

        // Assert
        Assert.assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testLoad_withInvalidInputStream_shouldThrowIOException() throws IOException {
        // Arrange
        InputStream inputStream = new FileInputStream("invalid.txt");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        // Act
        DataUtil.load(inputStream, charsetName, baseUri);
    }

    @Test
    public void testLoad_withValidInputStreamAndParser_shouldReturnDocument() throws IOException {
        // Arrange
        InputStream inputStream = new ByteArrayInputStream("Hello".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        // Act
        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);

        // Assert
        Assert.assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testLoad_withInvalidInputStreamAndParser_shouldThrowIOException() throws IOException {
        // Arrange
        InputStream inputStream = new FileInputStream("invalid.txt");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        // Act
        DataUtil.load(inputStream, charsetName, baseUri, parser);
    }

    @Test
    public void testCrossStreams_withValidInputStreamAndOutputStream_shouldWriteData() throws IOException {
        // Arrange
        InputStream inputStream = new ByteArrayInputStream("Hello".getBytes());
        OutputStream outputStream = new ByteArrayOutputStream();

        // Act
        DataUtil.crossStreams(inputStream, outputStream);

        // Assert
        Assert.assertEquals("Hello", outputStream.toString());
    }

    @Test
    public void testParseByteData_withValidData_shouldReturnDocument() {
        // Arrange
        ByteBuffer byteData = ByteBuffer.wrap("Hello".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        // Act
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        // Assert
        Assert.assertNotNull(document);
    }

    @Test(expected = IllegalCharsetNameException.class)
    public void testParseByteData_withInvalidData_shouldThrowIllegalCharsetNameException() {
        // Arrange
        ByteBuffer byteData = ByteBuffer.wrap("Hello".getBytes());
        String charsetName = "InvalidCharset";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        // Act
        DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
    }

    @Test
    public void testReadToByteBuffer_withValidInputStream_shouldReturnByteBuffer() throws IOException {
        // Arrange
        InputStream inputStream = new ByteArrayInputStream("Hello".getBytes());

        // Act
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);

        // Assert
        Assert.assertNotNull(byteBuffer);
    }

    @Test
    public void testReadToByteBuffer_withValidInputStreamAndMaxSize_shouldReturnByteBuffer() throws IOException {
        // Arrange
        InputStream inputStream = new ByteArrayInputStream("Hello".getBytes());
        int maxSize = 5;

        // Act
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);

        // Assert
        Assert.assertNotNull(byteBuffer);
    }

    @Test
    public void testReadFileToByteBuffer_withValidFile_shouldReturnByteBuffer() throws IOException {
        // Arrange
        File file = new File("test.txt");

        // Act
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);

        // Assert
        Assert.assertNotNull(byteBuffer);
    }

    @Test
    public void testEmptyByteBuffer_shouldReturnEmptyByteBuffer() {
        // Act
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();

        // Assert
        Assert.assertEquals(0, byteBuffer.capacity());
    }

    @Test
    public void testGetCharsetFromContentType_withValidContentType_shouldReturnCharset() {
        // Arrange
        String contentType = "text/html; charset=UTF-8";

        // Act
        String charset = DataUtil.getCharsetFromContentType(contentType);

        // Assert
        Assert.assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentType_withInvalidContentType_shouldReturnNull() {
        // Arrange
        String contentType = "text/html; charset=InvalidCharset";

        // Act
        String charset = DataUtil.getCharsetFromContentType(contentType);

        // Assert
        Assert.assertNull(charset);
    }

    @Test
    public void testMimeBoundary_shouldReturnRandomStringWithCorrectLength() {
        // Act
        String mimeBoundary = DataUtil.mimeBoundary();

        // Assert
        Assert.assertEquals(32, mimeBoundary.length());
    }
}