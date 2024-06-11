import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

public class DataUtilTest {

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testLoadFile_Success() {
        // Arrange
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        // Act
        try {
            Document document = DataUtil.load(file, charsetName, baseUri);

            // Assert
            assertNotNull(document);
            // Add more assertions for specific validation of the document
        } catch (IOException e) {
            // Assert
            fail("An unexpected IOException occurred: " + e.getMessage());
        }
    }

    @Test
    public void testLoadFile_IOException() {
        // Arrange
        File file = new File("nonexistent.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        // Act & Assert
        assertThrows(IOException.class, () -> DataUtil.load(file, charsetName, baseUri));
    }

    @Test
    public void testLoadInputStream_Success() {
        // Arrange
        String html = "<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        // Act
        try {
            Document document = DataUtil.load(inputStream, charsetName, baseUri);

            // Assert
            assertNotNull(document);
            // Add more assertions for specific validation of the document
        } catch (IOException e) {
            // Assert
            fail("An unexpected IOException occurred: " + e.getMessage());
        }
    }

    @Test
    public void testLoadInputStream_IOException() {
        // Arrange
        InputStream inputStream = null; // Invalid null input stream
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        // Act & Assert
        assertThrows(IOException.class, () -> DataUtil.load(inputStream, charsetName, baseUri));
    }

    @Test
    public void testLoadInputStream_CustomParser_Success() {
        // Arrange
        String xml = "<root><element>Test</element></root>";
        InputStream inputStream = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser customParser = Parser.xmlParser();

        // Act
        try {
            Document document = DataUtil.load(inputStream, charsetName, baseUri, customParser);

            // Assert
            assertNotNull(document);
            // Add more assertions for specific validation of the document
        } catch (IOException e) {
            // Assert
            fail("An unexpected IOException occurred: " + e.getMessage());
        }
    }

    @Test
    public void testLoadInputStream_CustomParser_IOException() {
        // Arrange
        InputStream inputStream = null; // Invalid null input stream
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser customParser = Parser.xmlParser();

        // Act & Assert
        assertThrows(IOException.class, () -> DataUtil.load(inputStream, charsetName, baseUri, customParser));
    }
    
    @Test
    public void testParseByteData_WithCharset_Success() {
        // Arrange
        String html = "<html><body><h1>Test</h1></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(html.getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        // Act
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        // Assert
        assertNotNull(document);
        // Add more assertions for specific validation of the document
    }
    
    @Test
    public void testParseByteData_WithoutCharset_Success() {
        // Arrange
        String html = "<html><body><h1>Test</h1></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(html.getBytes(StandardCharsets.UTF_8));
        String charsetName = null;
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        // Act
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        // Assert
        assertNotNull(document);
        // Add more assertions for specific validation of the document
    }
    
    @Test
    public void testParseByteData_WithoutCharset_InvalidMetaCharset() {
        // Arrange
        String html = "<html><body><h1>Test</h1></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(html.getBytes(StandardCharsets.UTF_8));
        String charsetName = null;
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        // Act
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        // Assert
        assertNotNull(document);
        assertEquals("UTF-8", document.outputSettings().charset().name());
        // Add more assertions for specific validation of the document
    }

    @Test
    public void testReadToByteBuffer_Success() {
        // Arrange
        String html = "<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));

        // Act
        try {
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);

            // Assert
            assertNotNull(byteBuffer);
            assertEquals(html, new String(byteBuffer.array(), StandardCharsets.UTF_8));
        } catch (IOException e) {
            // Assert
            fail("An unexpected IOException occurred: " + e.getMessage());
        }
    }

    @Test
    public void testGetCharsetFromContentType_WithCharset() {
        // Arrange
        String contentType = "text/html; charset=UTF-8";

        // Act
        String charset = DataUtil.getCharsetFromContentType(contentType);

        // Assert
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentType_WithoutCharset() {
        // Arrange
        String contentType = "text/html";

        // Act
        String charset = DataUtil.getCharsetFromContentType(contentType);

        // Assert
        assertNull(charset);
    }

    @Test
    public void testGetCharsetFromContentType_InvalidContentType() {
        // Arrange
        String contentType = null; // Invalid null content type

        // Act
        String charset = DataUtil.getCharsetFromContentType(contentType);

        // Assert
        assertNull(charset);
    }
}