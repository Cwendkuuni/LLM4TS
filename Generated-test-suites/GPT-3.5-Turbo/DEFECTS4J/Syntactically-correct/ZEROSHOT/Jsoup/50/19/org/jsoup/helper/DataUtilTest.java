import static org.junit.Assert.*;
import org.junit.Test;

public class DataUtilTest {

    @Test
    public void testLoadFile() {
        // Arrange
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        // Act
        Document document = DataUtil.load(file, charsetName, baseUri);

        // Assert
        assertNotNull(document);
        assertEquals("http://example.com", document.baseUri());
        // Additional assertions for document content
    }

    @Test
    public void testLoadInputStream() {
        // Arrange
        InputStream inputStream = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        // Act
        Document document = DataUtil.load(inputStream, charsetName, baseUri);

        // Assert
        assertNotNull(document);
        assertEquals("http://example.com", document.baseUri());
        // Additional assertions for document content
    }

    @Test
    public void testLoadInputStreamWithParser() {
        // Arrange
        InputStream inputStream = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        // Act
        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);

        // Assert
        assertNotNull(document);
        assertEquals("http://example.com", document.baseUri());
        // Additional assertions for document content
    }

    @Test
    public void testCrossStreams() {
        // Arrange
        InputStream in = new FileInputStream("input.txt");
        OutputStream out = new FileOutputStream("output.txt");

        // Act and Assert
        assertDoesNotThrow(() -> DataUtil.crossStreams(in, out));
        // Additional assertions for output stream
    }

    @Test
    public void testParseByteData() {
        // Arrange
        ByteBuffer byteData = ByteBuffer.wrap("Test data".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        // Act
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        // Assert
        assertNotNull(document);
        assertEquals("http://example.com", document.baseUri());
        // Additional assertions for document content
    }

    @Test
    public void testReadToByteBufferWithMaxSize() {
        // Arrange
        InputStream inStream = new FileInputStream("input.txt");
        int maxSize = 1024;

        // Act
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inStream, maxSize);

        // Assert
        assertNotNull(byteBuffer);
        assertTrue(byteBuffer.limit() <= maxSize);
        // Additional assertions for byte buffer content
    }

    @Test
    public void testReadToByteBufferWithoutMaxSize() {
        // Arrange
        InputStream inStream = new FileInputStream("input.txt");

        // Act
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inStream);

        // Assert
        assertNotNull(byteBuffer);
        // Additional assertions for byte buffer content
    }

    @Test
    public void testReadFileToByteBuffer() {
        // Arrange
        File file = new File("test.html");

        // Act
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);

        // Assert
        assertNotNull(byteBuffer);
        // Additional assertions for byte buffer content
    }

    @Test
    public void testEmptyByteBuffer() {
        // Act
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();

        // Assert
        assertNotNull(byteBuffer);
        assertEquals(0, byteBuffer.limit());
    }

    @Test
    public void testGetCharsetFromContentType() {
        // Arrange
        String contentType = "text/html; charset=UTF-8";

        // Act
        String charset = DataUtil.getCharsetFromContentType(contentType);

        // Assert
        assertEquals("UTF-8", charset);

        // Arrange
        contentType = "text/html; charset=ISO-8859-1";

        // Act
        charset = DataUtil.getCharsetFromContentType(contentType);

        // Assert
        assertEquals("ISO-8859-1", charset);

        // Arrange
        contentType = "text/html";

        // Act
        charset = DataUtil.getCharsetFromContentType(contentType);

        // Assert
        assertNull(charset);
    }

    @Test
    public void testMimeBoundary() {
        // Act
        String boundary = DataUtil.mimeBoundary();

        // Assert
        assertNotNull(boundary);
        assertEquals(DataUtil.boundaryLength, boundary.length());
        // Additional assertions for boundary format
    }
}