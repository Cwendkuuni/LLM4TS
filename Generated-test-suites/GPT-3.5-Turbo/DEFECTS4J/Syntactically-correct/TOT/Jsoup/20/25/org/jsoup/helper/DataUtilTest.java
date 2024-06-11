import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {

    @Test
    public void testLoad_ValidFile_ReturnsDocumentWithSameCharset() throws IOException {
        // Arrange
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        // Act
        Document document = DataUtil.load(file, charsetName, baseUri);
        
        // Assert
        assertNotNull(document);
        assertEquals(charsetName, document.charset().name());
    }

    @Test
    public void testLoad_InvalidFile_ThrowsIOException() {
        // Arrange
        File file = new File("nonexistent.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        // Act and Assert
        try {
            DataUtil.load(file, charsetName, baseUri);
            // Expecting an exception to be thrown, so if we reach this point, the test fails
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            // Exception caught, test passed
        }
    }

    @Test
    public void testLoad_InputSteam_ReturnsDocument() throws IOException {
        // Arrange
        InputStream inputStream = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        // Act
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        
        // Assert
        assertNotNull(document);
    }

    @Test
    public void testLoad_InputSteam_NullInputStream_ThrowsIOException() {
        // Arrange
        InputStream inputStream = null;
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        // Act and Assert
        try {
            DataUtil.load(inputStream, charsetName, baseUri);
            // Expecting an exception to be thrown, so if we reach this point, the test fails
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            // Exception caught, test passed
        }
    }

    @Test
    public void testLoad_InputSteamWithParser_ReturnsDocument() throws IOException {
        // Arrange
        InputStream inputStream = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        
        // Act
        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
        
        // Assert
        assertNotNull(document);
    }

    @Test
    public void testLoad_InputSteamWithParser_NullInputStream_ThrowsIOException() {
        // Arrange
        InputStream inputStream = null;
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        
        // Act and Assert
        try {
            DataUtil.load(inputStream, charsetName, baseUri, parser);
            // Expecting an exception to be thrown, so if we reach this point, the test fails
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            // Exception caught, test passed
        }
    }

    @Test
    public void testParseByteData_ValidData_ReturnsDocumentWithSameCharset() {
        // Arrange
        ByteBuffer byteData = ByteBuffer.wrap("Hello, World!".getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        
        // Act
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        
        // Assert
        assertNotNull(document);
        assertEquals(charsetName, document.charset().name());
    }

    @Test
    public void testParseByteData_MissingCharsetInMeta_ReturnsDocumentWithDefaultCharset() {
        // Arrange
        ByteBuffer byteData = ByteBuffer.wrap("<meta>".getBytes(StandardCharsets.UTF_8));
        String charsetName = null;
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        
        // Act
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        
        // Assert
        assertNotNull(document);
        assertEquals(DataUtil.defaultCharset, document.charset().name());
    }

    @Test
    public void testParseByteData_MultipleCharsetsInMeta_DetectsAndUsesFirstCharset() {
        // Arrange
        ByteBuffer byteData = ByteBuffer.wrap("<meta charset=\"UTF-8\"><meta charset=\"ISO-8859-1\">".getBytes(StandardCharsets.UTF_8));
        String charsetName = null;
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        
        // Act
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        
        // Assert
        assertNotNull(document);
        assertEquals("UTF-8", document.charset().name());
    }

    @Test
    public void testParseByteData_ValidCharsetInMeta_ReturnsDocumentWithFoundCharset() {
        // Arrange
        ByteBuffer byteData = ByteBuffer.wrap("<meta charset=\"ISO-8859-1\">".getBytes(StandardCharsets.UTF_8));
        String charsetName = null;
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        
        // Act
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        
        // Assert
        assertNotNull(document);
        assertEquals("ISO-8859-1", document.charset().name());
    }

    @Test
    public void testParseByteData_ValidCharsetInContentType_ReturnsDocumentWithFoundCharset() {
        // Arrange
        ByteBuffer byteData = ByteBuffer.wrap("Content-Type: text/html; charset=ISO-8859-1".getBytes(StandardCharsets.UTF_8));
        String charsetName = null;
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        
        // Act
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        
        // Assert
        assertNotNull(document);
        assertEquals("ISO-8859-1", document.charset().name());
    }

    @Test
    public void testReadToByteBuffer_InputData_ReadsAndReturnsByteBuffer() throws IOException {
        // Arrange
        InputStream inputStream = new FileInputStream("test.html");
        
        // Act
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        
        // Assert
        assertNotNull(byteData);
        assertTrue(byteData.limit() > 0);
    }

    @Test
    public void testGetCharsetFromContentType_ValidContentType_ReturnsCorrectCharset() {
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
        String contentType = "text/html";
        
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
}