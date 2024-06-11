import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;

import static org.junit.Assert.*;

public class DataUtilTest {
    
    private File validFile;
    private File invalidFile;
    
    @Before
    public void setup() {
        validFile = new File("validFile.html");
        invalidFile = new File("invalidFile.html");
    }

    @Test
    public void testLoad_validFile_validDocument() {
        try {
            Document document = DataUtil.load(validFile, "UTF-8", "https://www.example.com");
            
            assertNotNull(document);
            // Add assertions for valid document content
        } catch (IOException e) {
            fail("Should not throw an exception");
        }
    }
    
    @Test(expected = FileNotFoundException.class)
    public void testLoad_invalidFile_fileNotFoundException() throws IOException {
        DataUtil.load(invalidFile, "UTF-8", "https://www.example.com");
    }
    
    @Test
    public void testLoad_inputStream_validDocument() {
        try {
            FileInputStream inputStream = new FileInputStream(validFile);
            Document document = DataUtil.load(inputStream, "UTF-8", "https://www.example.com");
            
            assertNotNull(document);
            // Add assertions for valid document content
        } catch (IOException e) {
            fail("Should not throw an exception");
        }
    }
    
    @Test
    public void testLoad_inputStream_customParser_validDocument() {
        try {
            FileInputStream inputStream = new FileInputStream(validFile);
            Document document = DataUtil.load(inputStream, "UTF-8", "https://www.example.com", Parser.xmlParser());
            
            assertNotNull(document);
            // Add assertions for valid document content
        } catch (IOException e) {
            fail("Should not throw an exception");
        }
    }
    
    @Test(expected = IOException.class)
    public void testLoad_inputStream_ioException() throws IOException {
        FileInputStream inputStream = new FileInputStream(invalidFile);
        DataUtil.load(inputStream, "UTF-8", "https://www.example.com");
    }
    
    @Test
    public void testParseByteData_validByteData_validDocument() {
        // Create a valid byte data (simulating charset detection)
        String byteDataString = "<html><body><p>Test</p></body></html>";
        byte[] byteDataBytes = byteDataString.getBytes(Charset.forName("UTF-8"));
        ByteBuffer byteData = ByteBuffer.wrap(byteDataBytes);
        
        Document document = DataUtil.parseByteData(byteData, null, "https://www.example.com", Parser.htmlParser());
        
        assertNotNull(document);
        // Add assertions for valid document content
    }
    
    @Test
    public void testParseByteData_invalidByteData_validDocument() {
        // Create an invalid byte data (missing closing tags)
        String byteDataString = "<html><body><p>Test";
        byte[] byteDataBytes = byteDataString.getBytes(Charset.forName("UTF-8"));
        ByteBuffer byteData = ByteBuffer.wrap(byteDataBytes);
        
        Document document = DataUtil.parseByteData(byteData, null, "https://www.example.com", Parser.htmlParser());
        
        assertNotNull(document);
        // Add assertions for valid document content (should handle invalid input gracefully)
    }
    
    @Test
    public void testParseByteData_charsetName_validDocument() {
        // Create a valid byte data with specified charset (UTF-16)
        String byteDataString = "<html><body><p>Test</p></body></html>";
        byte[] byteDataBytes = byteDataString.getBytes(Charset.forName("UTF-16"));
        ByteBuffer byteData = ByteBuffer.wrap(byteDataBytes);
        
        Document document = DataUtil.parseByteData(byteData, "UTF-16", "https://www.example.com", Parser.htmlParser());
        
        assertNotNull(document);
        // Add assertions for valid document content
    }
    
    @Test(expected = IllegalStateException.class)
    public void testParseByteData_invalidCharsetName_illegalStateException() {
        // Create a valid byte data with specified charset (UTF-8)
        String byteDataString = "<html><body><p>Test</p></body></html>";
        byte[] byteDataBytes = byteDataString.getBytes(Charset.forName("UTF-8"));
        ByteBuffer byteData = ByteBuffer.wrap(byteDataBytes);
        
        DataUtil.parseByteData(byteData, "invalidCharset", "https://www.example.com", Parser.htmlParser());
    }
    
    @Test
    public void testReadToByteBuffer_validInputStream_validByteBuffer() {
        try {
            FileInputStream inputStream = new FileInputStream(validFile);
            
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
            
            assertNotNull(byteBuffer);
            // Add assertions for valid byte buffer content
        } catch (IOException e) {
            fail("Should not throw an exception");
        }
    }
    
    @Test
    public void testReadToByteBuffer_validInputStreamWithMaxSize_validByteBuffer() {
        try {
            FileInputStream inputStream = new FileInputStream(validFile);
            
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 1024);
            
            assertNotNull(byteBuffer);
            // Add assertions for valid byte buffer content
        } catch (IOException e) {
            fail("Should not throw an exception");
        }
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testReadToByteBuffer_negativeMaxSize_illegalArgumentException() throws IOException {
        FileInputStream inputStream = new FileInputStream(validFile);
        
        DataUtil.readToByteBuffer(inputStream, -1);
    }
    
    @Test
    public void testGetCharsetFromContentType_validContentType_validCharset() {
        String contentType = "text/html; charset=UTF-8";
        
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        assertEquals("UTF-8", charset);
    }
    
    @Test
    public void testGetCharsetFromContentType_validContentTypeUpperCase_validCharset() {
        String contentType = "text/html; charset=ISO-8859-1";
        
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        assertEquals("ISO-8859-1", charset);
    }
    
    @Test
    public void testGetCharsetFromContentType_invalidContentType_nullCharset() {
        String contentType = "text/html; charset=invalidCharset";
        
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        assertNull(charset);
    }
    
    @Test
    public void testGetCharsetFromContentType_nullContentType_nullCharset() {
        String charset = DataUtil.getCharsetFromContentType(null);
        
        assertNull(charset);
    }
    
    @Test
    public void testGetCharsetFromContentType_emptyContentType_nullCharset() {
        String contentType = "";
        
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        assertNull(charset);
    }

}