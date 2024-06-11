import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        // Arrange
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        // Act
        Document document = DataUtil.load(file, charsetName, baseUri);
        
        // Assert
        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
    }
    
    @Test
    public void testLoadInputStream() throws IOException {
        // Arrange
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        // Act
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        
        // Assert
        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
    }
    
    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        // Arrange
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        
        // Act
        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
        
        // Assert
        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
    }
    
    @Test
    public void testCrossStreams() throws IOException {
        // Arrange
        InputStream inputStream = getClass().getResourceAsStream("input.txt");
        OutputStream outputStream = System.out;
        
        // Act
        DataUtil.crossStreams(inputStream, outputStream);
        
        // Assert
        // No assertion, just check that there were no exceptions
    }
    
    @Test
    public void testParseInputStream() throws IOException {
        // Arrange
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        
        // Act
        Document document = DataUtil.parseInputStream(inputStream, charsetName, baseUri, parser);
        
        // Assert
        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
    }
    
    @Test
    public void testReadToByteBuffer() throws IOException {
        // Arrange
        File file = new File("test.html");
        
        // Act
        ByteBuffer buffer = DataUtil.readToByteBuffer(file);
        
        // Assert
        assertNotNull(buffer);
        assertTrue(buffer.remaining() > 0);
    }
    
    @Test
    public void testEmptyByteBuffer() {
        // Act
        ByteBuffer buffer = DataUtil.emptyByteBuffer();
        
        // Assert
        assertNotNull(buffer);
        assertEquals(0, buffer.remaining());
    }
    
    @Test
    public void testGetCharsetFromContentType() {
        // Arrange
        String contentType = "text/html; charset=ISO-8859-1";
        
        // Act
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        // Assert
        assertNotNull(charset);
        assertEquals("ISO-8859-1", charset);
    }
    
    @Test
    public void testMimeBoundary() {
        // Act
        String boundary = DataUtil.mimeBoundary();
        
        // Assert
        assertNotNull(boundary);
        assertEquals(DataUtil.boundaryLength, boundary.length());
    }
}