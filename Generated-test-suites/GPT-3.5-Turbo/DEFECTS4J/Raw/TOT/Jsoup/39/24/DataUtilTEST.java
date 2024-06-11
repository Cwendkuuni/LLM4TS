import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.File;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {
    
    private static final String CHARSET_UTF8 = "UTF-8";
    private static final String CHARSET_ISO_8859_1 = "ISO-8859-1";
    
    private File inputHtmlFile;
    private String baseUri;

    @Before
    public void setUp() {
        // Prepare test data
        inputHtmlFile = new File("test.html");
        baseUri = "http://example.com";
    }
    
    @Test
    public void testLoadFromFile_ValidFile_ShouldReturnDocument() throws IOException {
        // Arrange
        String input = "<html><head></head><body>Hello World!</body></html>";
        FileUtils.writeStringToFile(inputHtmlFile, input, StandardCharsets.UTF_8);
        
        // Act
        Document document = DataUtil.load(inputHtmlFile, CHARSET_UTF8, baseUri);
        
        // Assert
        assertNotNull(document);
        assertEquals(input, document.html());
    }
    
    @Test(expected = IOException.class)
    public void testLoadFromFile_NonExistingFile_ShouldThrowIOException() throws IOException {
        // Act
        Document document = DataUtil.load(new File("non_existing_file.html"), CHARSET_UTF8, baseUri);
        
        // Expect IOException
    }
    
    @Test
    public void testLoadFromInputStream_ValidInputStream_ShouldReturnDocument() throws IOException {
        // Arrange
        String input = "<html><head></head><body>Hello World!</body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        
        // Act
        Document document = DataUtil.load(inputStream, CHARSET_UTF8, baseUri);
        
        // Assert
        assertNotNull(document);
        assertEquals(input, document.html());
    }
    
    @Test
    public void testLoadFromInputStream_NullInputStream_ShouldReturnEmptyDocument() throws IOException {
        // Act
        Document document = DataUtil.load(null, CHARSET_UTF8, baseUri);
        
        // Assert
        assertNotNull(document);
        assertTrue(document.html().isEmpty());
    }
    
    @Test
    public void testLoadFromInputStream_InvalidCharset_ShouldReturnDocumentWithDefaultCharset() throws IOException {
        // Arrange
        String input = "<html><head></head><body>Hello World!</body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        
        // Act
        Document document = DataUtil.load(inputStream, "invalid_charset", baseUri);
        
        // Assert
        assertNotNull(document);
        assertEquals(input, document.html());
        assertEquals(CHARSET_UTF8, document.outputSettings().charset().name());
    }
    
    @Test
    public void testLoadFromInputStream_ValidInputStreamAndParser_ShouldReturnDocumentParsedWithParser() throws IOException {
        // Arrange
        String input = "<html><head></head><body>Hello World!</body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        Parser parser = Parser.xmlParser();
        
        // Act
        Document document = DataUtil.load(inputStream, CHARSET_UTF8, baseUri, parser);
        
        // Assert
        assertNotNull(document);
        assertEquals(input, document.html());
        assertEquals(parser, document.parser());
    }
    
    @Test
    public void testParseByteData_NullCharset_ShouldAttemptToDetectCharsetFromMeta() {
        // Arrange
        String input = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\"></head><body>Hello World!</body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(input.getBytes(StandardCharsets.UTF_8));
        
        // Act
        Document document = DataUtil.parseByteData(byteData, null, baseUri, Parser.htmlParser());
        
        // Assert
        assertNotNull(document);
        assertEquals(input, document.html());
        assertEquals(CHARSET_UTF8, document.outputSettings().charset().name());
    }
    
    @Test
    public void testParseByteData_NullCharsetAndInvalidCharsetFromMeta_ShouldUseDefaultCharset() {
        // Arrange
        String input = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html;charset=invalid_charset\"></head><body>Hello World!</body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(input.getBytes(StandardCharsets.UTF_8));
        
        // Act
        Document document = DataUtil.parseByteData(byteData, null, baseUri, Parser.htmlParser());
        
        // Assert
        assertNotNull(document);
        assertEquals(input, document.html());
        assertEquals(CHARSET_UTF8, document.outputSettings().charset().name());
    }
    
    @Test
    public void testParseByteData_ValidCharset_ShouldDecodeDataWithProvidedCharset() {
        // Arrange
        String input = "<html><head></head><body>Hello World!</body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(input.getBytes(StandardCharsets.ISO_8859_1));
        
        // Act
        Document document = DataUtil.parseByteData(byteData, CHARSET_ISO_8859_1, baseUri, Parser.htmlParser());
        
        // Assert
        assertNotNull(document);
        assertEquals(input, document.html());
        assertEquals(CHARSET_ISO_8859_1, document.outputSettings().charset().name());
    }
    
    @Test
    public void testReadToByteBuffer_ValidInputStream_ShouldReadDataIntoByteBuffer() throws IOException {
        // Arrange
        String input = "Hello World!";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        
        // Act
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        String output = new String(byteBuffer.array(), StandardCharsets.UTF_8);
        
        // Assert
        assertEquals(input, output);
    }
    
    @Test
    public void testReadToByteBuffer_ValidInputStreamWithMaxSize_ShouldReadDataUpToMaxSizeIntoByteBuffer() throws IOException {
        // Arrange
        String input = "<html><head></head><body>Hello World!</body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        int maxSize = 10;
        
        // Act
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);
        String output = new String(byteBuffer.array(), StandardCharsets.UTF_8);
        
        // Assert
        assertTrue(output.length() <= maxSize);
        assertTrue(output.contains("Hello"));
    }
    
    @Test
    public void testGetCharsetFromContentType_ValidContentTypeWithCharset_ShouldReturnCharset() {
        // Arrange
        String contentType = "text/html; charset=UTF-8";
        
        // Act
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        // Assert
        assertEquals(CHARSET_UTF8, charset);
    }
    
    @Test
    public void testGetCharsetFromContentType_ValidContentTypeWithoutCharset_ShouldReturnNull() {
        // Arrange
        String contentType = "text/html";
        
        // Act
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        // Assert
        assertNull(charset);
    }
    
    @Test
    public void testGetCharsetFromContentType_NullContentType_ShouldReturnNull() {
        // Act
        String charset = DataUtil.getCharsetFromContentType(null);
        
        // Assert
        assertNull(charset);
    }
    
    @Test
    public void testGetCharsetFromContentType_InvalidContentTypeWithUnsupportedCharset_ShouldReturnNull() {
        // Arrange
        String contentType = "text/html; charset=unsupported_charset";
        
        // Act
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        // Assert
        assertNull(charset);
    }
    
}