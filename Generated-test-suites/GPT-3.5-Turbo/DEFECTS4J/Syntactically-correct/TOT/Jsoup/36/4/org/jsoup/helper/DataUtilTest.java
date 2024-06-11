import org.junit.Test;
import static org.junit.Assert.*;

public class DataUtilTest {

    @Test
    public void testLoadFile_ValidInput_ReturnsDocument() {
        // Arrange
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        
        // Act
        Document result = DataUtil.load(file, charsetName, baseUri);
        
        // Assert
        assertNotNull(result);
        assertEquals(0, result.childNodeSize());
    }
    
    @Test
    public void testLoadFile_InvalidFile_ThrowsIOException() {
        // Arrange
        File file = new File("invalid.html");
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        
        // Act & Assert
        assertThrows(IOException.class, () -> {
            DataUtil.load(file, charsetName, baseUri);
        });
    }
    
    @Test
    public void testLoadInputStream_ValidInput_ReturnsDocument() {
        // Arrange
        InputStream in = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        
        // Act
        Document result = DataUtil.load(in, charsetName, baseUri);
        
        // Assert
        assertNotNull(result);
        assertEquals(0, result.childNodeSize());
    }
    
    @Test
    public void testLoadInputStream_InvalidInput_ThrowsIOException() {
        // Arrange
        InputStream in = new ByteArrayInputStream("invalid".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        
        // Act & Assert
        assertThrows(IOException.class, () -> {
            DataUtil.load(in, charsetName, baseUri);
        });
    }
    
    @Test
    public void testLoadInputStreamWithParser_ValidInput_ReturnsDocument() {
        // Arrange
        InputStream in = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Parser parser = Parser.htmlParser();
        
        // Act
        Document result = DataUtil.load(in, charsetName, baseUri, parser);
        
        // Assert
        assertNotNull(result);
        assertEquals(0, result.childNodeSize());
    }
    
    @Test
    public void testLoadInputStreamWithParser_InvalidInput_ThrowsIOException() {
        // Arrange
        InputStream in = new ByteArrayInputStream("invalid".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Parser parser = Parser.htmlParser();
        
        // Act & Assert
        assertThrows(IOException.class, () -> {
            DataUtil.load(in, charsetName, baseUri, parser);
        });
    }
    
    @Test
    public void testParseByteData_WithoutCharset_ReturnsDocument() {
        // Arrange
        ByteBuffer byteData = ByteBuffer.wrap("<html><head></head><body></body></html>".getBytes());
        String charsetName = null;
        String baseUri = "http://www.example.com";
        Parser parser = Parser.htmlParser();
        
        // Act
        Document result = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        
        // Assert
        assertNotNull(result);
        assertEquals(0, result.childNodeSize());
    }
    
    @Test
    public void testParseByteData_WithCharset_ReturnsDocument() {
        // Arrange
        ByteBuffer byteData = ByteBuffer.wrap("<html><head></head><body></body></html>".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Parser parser = Parser.htmlParser();
        
        // Act
        Document result = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        
        // Assert
        assertNotNull(result);
        assertEquals(0, result.childNodeSize());
    }
    
    @Test
    public void testReadToByteBuffer_WithMaxSize_ReturnsByteBuffer() {
        // Arrange
        InputStream inStream = new ByteArrayInputStream("test".getBytes());
        int maxSize = 5;
        
        // Act
        ByteBuffer result = DataUtil.readToByteBuffer(inStream, maxSize);
        
        // Assert
        assertNotNull(result);
        assertEquals(4, result.limit());
    }
    
    @Test
    public void testReadToByteBuffer_WithoutMaxSize_ReturnsByteBuffer() {
        // Arrange
        InputStream inStream = new ByteArrayInputStream("test".getBytes());
        
        // Act
        ByteBuffer result = DataUtil.readToByteBuffer(inStream);
        
        // Assert
        assertNotNull(result);
        assertEquals(4, result.limit());
    }
    
    @Test
    public void testGetCharsetFromContentType_WithValidContentType_ReturnsCharset() {
        // Arrange
        String contentType = "text/html; charset=UTF-8";
        
        // Act
        String result = DataUtil.getCharsetFromContentType(contentType);
        
        // Assert
        assertNotNull(result);
        assertEquals("UTF-8", result);
    }
    
    @Test
    public void testGetCharsetFromContentType_WithUnsupportedCharset_ReturnsNull() {
        // Arrange
        String contentType = "text/html; charset=invalid";
        
        // Act
        String result = DataUtil.getCharsetFromContentType(contentType);
        
        // Assert
        assertNull(result);
    }
    
    @Test
    public void testGetCharsetFromContentType_WithValidContentTypeAndUnsupportedCharset_ReturnsDefaultCharset() {
        // Arrange
        String contentType = "text/html; charset=invalid";
        
        // Act
        String result = DataUtil.getCharsetFromContentType(contentType);
        
        // Assert
        assertEquals(DataUtil.defaultCharset, result);
    }
}