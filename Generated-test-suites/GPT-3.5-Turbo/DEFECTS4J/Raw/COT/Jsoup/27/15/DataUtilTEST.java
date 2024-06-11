import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;

import static org.junit.Assert.assertEquals;

public class DataUtilTest {

    @Test
    public void testLoad_withFile_shouldReturnDocument() throws IOException {
        // Arrange
        File file = new File("example.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        // Act
        Document document = DataUtil.load(file, charsetName, baseUri);

        // Assert
        assertEquals("http://example.com", document.baseUri());
    }

    @Test
    public void testLoad_withInputStream_shouldReturnDocument() throws IOException {
        // Arrange
        InputStream inputStream = new FileInputStream("example.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        // Act
        Document document = DataUtil.load(inputStream, charsetName, baseUri);

        // Assert
        assertEquals("http://example.com", document.baseUri());
    }

    @Test
    public void testLoad_withInputStreamAndParser_shouldReturnDocument() throws IOException {
        // Arrange
        InputStream inputStream = new FileInputStream("example.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        // Act
        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);

        // Assert
        assertEquals("http://example.com", document.baseUri());
    }
    
    @Test
    public void testParseByteData_withCharsetNull_shouldReturnDocumentWithDefaultCharset() {
        // Arrange
        ByteBuffer byteData = ByteBuffer.allocate(0);
        String charsetName = null;
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        
        // Act
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        
        // Assert
        assertEquals("UTF-8", document.outputSettings().charset().displayName());
    }
    
    @Test
    public void testParseByteData_withCharsetSpecified_shouldReturnDocumentWithSpecifiedCharset() {
        // Arrange
        ByteBuffer byteData = ByteBuffer.allocate(0);
        String charsetName = "ISO-8859-1";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        
        // Act
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        
        // Assert
        assertEquals(charsetName, document.outputSettings().charset().displayName());
    }
    
    @Test
    public void testReadToByteBuffer_withInputStream_shouldReturnByteBuffer() throws IOException {
        // Arrange
        InputStream inputStream = new FileInputStream("example.html");

        // Act
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);

        // Assert
        assertEquals(1024, byteData.capacity());
    }
    
    @Test
    public void testGetCharsetFromContentType_withContentType_shouldReturnCharset() {
        // Arrange
        String contentType = "text/html; charset=UTF-8";
        
        // Act
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        // Assert
        assertEquals("UTF-8", charset);
    }
    
    @Test
    public void testGetCharsetFromContentType_withInvalidContentType_shouldReturnNull() {
        // Arrange
        String contentType = "text/html";
        
        // Act
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        // Assert
        assertEquals(null, charset);
    }

}