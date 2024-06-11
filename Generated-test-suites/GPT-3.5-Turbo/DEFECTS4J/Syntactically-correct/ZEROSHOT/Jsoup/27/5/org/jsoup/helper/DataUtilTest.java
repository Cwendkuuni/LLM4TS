import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;


public class DataUtilTest {
    
    @Mock
    private File file;
    
    @Mock
    private InputStream inputStream;
    
    @Mock
    private Parser parser;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void testLoad_withFileAndCharsetNameAndBaseUri_shouldLoadDocument() throws IOException {
        // Arrange
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        // Act
        Document document = DataUtil.load(file, charsetName, baseUri);
        
        // Assert
        assertNotNull(document);
    }
    
    @Test
    public void testLoad_withInputStreamAndCharsetNameAndBaseUri_shouldLoadDocument() throws IOException {
        // Arrange
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        // Act
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        
        // Assert
        assertNotNull(document);
    }
    
    @Test
    public void testLoad_withInputStreamAndCharsetNameAndBaseUriAndParser_shouldLoadDocument() throws IOException {
        // Arrange
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        // Act
        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
        
        // Assert
        assertNotNull(document);
    }
    
    @Test
    public void testParseByteData_withByteBufferAndCharsetNameAndBaseUriAndParser_shouldParseDocument() {
        // Arrange
        ByteBuffer byteData = ByteBuffer.allocate(10);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        // Act
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        
        // Assert
        assertNotNull(document);
    }
    
    @Test
    public void testReadToByteBuffer_withInputStream_shouldReadByteBuffer() throws IOException {
        // Arrange
        byte[] buffer = new byte[10];
        ByteArrayInputStream inStream = new ByteArrayInputStream(buffer);
        
        // Act
        ByteBuffer byteData = DataUtil.readToByteBuffer(inStream);
        
        // Assert
        assertNotNull(byteData);
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
}