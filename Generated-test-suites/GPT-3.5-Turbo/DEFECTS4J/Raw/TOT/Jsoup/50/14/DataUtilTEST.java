import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import static org.junit.Assert.*;

public class DataUtilTest {

    private File file;
    private InputStream inputStream;

    @Before
    public void setUp() throws IOException {
        // Create a temporary file for testing
        file = File.createTempFile("test", ".html");
        FileWriter writer = new FileWriter(file);
        writer.write("<html><head><title>Test Page</title></head><body><h1>Hello World!</h1></body></html>");
        writer.close();

        // Create a ByteArrayInputStream from the file content for testing
        String content = "<html><head><title>Test Page</title></head><body><h1>Hello World!</h1></body></html>";
        inputStream = new ByteArrayInputStream(content.getBytes());
    }

    @Test
    public void testLoadFile_ValidFile_ReturnsDocument() throws IOException {
        // Arrange
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        // Act
        Document document = DataUtil.load(file, charsetName, baseUri);

        // Assert
        assertNotNull(document);
        assertEquals("Test Page", document.title());
    }

    @Test
    public void testLoadFile_InvalidFile_ThrowsIOException() {
        // Arrange
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        File invalidFile = new File("invalid.html");

        // Act & Assert
        assertThrows(IOException.class, () -> {
            DataUtil.load(invalidFile, charsetName, baseUri);
        });
    }

    @Test
    public void testLoadInputStream_ValidInputStream_ReturnsDocument() throws IOException {
        // Arrange
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        // Act
        Document document = DataUtil.load(inputStream, charsetName, baseUri);

        // Assert
        assertNotNull(document);
        assertEquals("Test Page", document.title());
    }

    @Test
    public void testLoadInputStream_InvalidInputStream_ThrowsIOException() {
        // Arrange
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        InputStream invalidInputStream = new ByteArrayInputStream(new byte[0]);

        // Act & Assert
        assertThrows(IOException.class, () -> {
            DataUtil.load(invalidInputStream, charsetName, baseUri);
        });
    }

    @Test
    public void testLoadInputStream_ValidInputStreamWithParser_ReturnsDocument() throws IOException {
        // Arrange
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        // Act
        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);

        // Assert
        assertNotNull(document);
        assertEquals("Test Page", document.title());
    }

    @Test
    public void testLoadInputStream_InvalidInputStreamWithParser_ThrowsIOException() {
        // Arrange
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        InputStream invalidInputStream = new ByteArrayInputStream(new byte[0]);

        // Act & Assert
        assertThrows(IOException.class, () -> {
            DataUtil.load(invalidInputStream, charsetName, baseUri, parser);
        });
    }

    @Test
    public void testCrossStreams_ValidInputStreamAndOutputStream_CopyData() throws IOException {
        // Arrange
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] expectedData = "Hello World!".getBytes(StandardCharsets.UTF_8);

        // Act
        DataUtil.crossStreams(inputStream, outputStream);

        // Assert
        byte[] actualData = outputStream.toByteArray();
        assertArrayEquals(expectedData, actualData);
    }

    @Test
    public void testParseByteData_ValidByteData_ReturnsDocument() {
        // Arrange
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        byte[] byteData = "<html><head><title>Test Page</title></head><body><h1>Hello World!</h1></body></html>".getBytes(StandardCharsets.UTF_8);
        ByteBuffer byteBuffer = ByteBuffer.wrap(byteData);

        // Act
        Document document = DataUtil.parseByteData(byteBuffer, charsetName, baseUri, parser);

        // Assert
        assertNotNull(document);
        assertEquals("Test Page", document.title());
    }

    @Test
    public void testReadToByteBuffer_ValidInputStream_ReturnsByteBuffer() throws IOException {
        // Arrange
        int maxSize = 0;
        byte[] expectedData = "<html><head><title>Test Page</title></head><body><h1>Hello World!</h1></body></html>".getBytes(StandardCharsets.UTF_8);

        // Act
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);

        // Assert
        assertNotNull(byteBuffer);
        assertArrayEquals(expectedData, byteBuffer.array());
    }

    @Test
    public void testReadToByteBuffer_ValidInputStreamWithMaxSize_ReturnsByteBufferWithLimitedData() throws IOException {
        // Arrange
        int maxSize = 10;
        byte[] expectedData = "<html><hea".getBytes(StandardCharsets.UTF_8);

        // Act
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);

        // Assert
        assertNotNull(byteBuffer);
        assertArrayEquals(expectedData, byteBuffer.array());
    }

    @Test
    public void testReadToByteBuffer_InvalidInputStream_ThrowsIOException() {
        // Arrange
        InputStream invalidInputStream = new ByteArrayInputStream(new byte[0]);
        int maxSize = 0;

        // Act & Assert
        assertThrows(IOException.class, () -> {
            DataUtil.readToByteBuffer(invalidInputStream, maxSize);
        });
    }

    @Test
    public void testReadToByteBuffer_ValidInputStreamWithoutMaxSize_ReturnsByteBuffer() throws IOException {
        // Arrange
        byte[] expectedData = "<html><head><title>Test Page</title></head><body><h1>Hello World!</h1></body></html>".getBytes(StandardCharsets.UTF_8);

        // Act
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);

        // Assert
        assertNotNull(byteBuffer);
        assertArrayEquals(expectedData, byteBuffer.array());
    }

    @Test
    public void testReadFileToByteBuffer_ValidFile_ReturnsByteBuffer() throws IOException {
        // Arrange
        byte[] expectedData = "<html><head><title>Test Page</title></head><body><h1>Hello World!</h1></body></html>".getBytes(StandardCharsets.UTF_8);

        // Act
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);

        // Assert
        assertNotNull(byteBuffer);
        assertArrayEquals(expectedData, byteBuffer.array());
    }

    @Test
    public void testEmptyByteBuffer_ReturnsEmptyByteBuffer() {
        // Act
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();

        // Assert
        assertNotNull(byteBuffer);
        assertEquals(0, byteBuffer.capacity());
    }

    @Test
    public void testGetCharsetFromContentType_ValidContentType_ReturnsCharset() {
        // Arrange
        String contentType = "text/html; charset=UTF-8";

        // Act
        String charset = DataUtil.getCharsetFromContentType(contentType);

        // Assert
        assertNotNull(charset);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentType_InvalidContentType_ReturnsNull() {
        // Arrange
        String contentType = "text/html; charset=invalid";

        // Act
        String charset = DataUtil.getCharsetFromContentType(contentType);

        // Assert
        assertNull(charset);
    }

    @Test
    public void testGetCharsetFromContentType_NullContentType_ReturnsNull() {
        // Arrange
        String contentType = null;

        // Act
        String charset = DataUtil.getCharsetFromContentType(contentType);

        // Assert
        assertNull(charset);
    }

    @Test
    public void testMimeBoundary_ReturnsRandomStringWithLength32() {
        // Act
        String boundary = DataUtil.mimeBoundary();

        // Assert
        assertNotNull(boundary);
        assertEquals(32, boundary.length());
    }
}