import org.junit.Test;
import org.jsoup.helper.DataUtil;

import java.io.File;
import java.io.FileInputStream;

import static org.junit.Assert.*;

public class DataUtilTest {

    @Test
    public void testLoad_File_Valid() {
        try {
            // Create a temporary test file
            File inputFile = File.createTempFile("test", ".html");
            
            // Write some content to the file
            
            // Test loading the file with valid charset and base URI
            DataUtil.load(inputFile, "UTF-8", "http://example.com");
            // Assert expected result
            
            // Test loading the file with invalid charset
            DataUtil.load(inputFile, "INVALID_CHARSET", "http://example.com");
            // Assert expected result
            
            // Test loading the file with invalid base URI
            DataUtil.load(inputFile, "UTF-8", "");
            // Assert expected result
            
            // Clean up the temporary test file
            inputFile.delete();
        } catch (Exception e) {
            fail("An unexpected exception occurred");
        }
    }
    
    @Test
    public void testLoad_File_Invalid() {
        try {
            // Test loading a non-existent file
            DataUtil.load(new File("non_existent_file.html"), "UTF-8", "http://example.com");
            // Assert expected result
            
            // Test loading a directory instead of a file
            DataUtil.load(new File("directory"), "UTF-8", "http://example.com");
            // Assert expected result
        } catch (Exception e) {
            fail("An unexpected exception occurred");
        }
    }
    
    @Test
    public void testLoad_InputStream_Valid() {
        try {
            // Create a temporary test file
            File inputFile = File.createTempFile("test", ".html");
            
            // Write some content to the file
            
            FileInputStream inputStream = new FileInputStream(inputFile);
            
            // Test loading the input stream with valid charset and base URI
            DataUtil.load(inputStream, "UTF-8", "http://example.com");
            // Assert expected result
            
            // Test loading the input stream with invalid charset
            DataUtil.load(inputStream, "INVALID_CHARSET", "http://example.com");
            // Assert expected result
            
            // Test loading the input stream with invalid base URI
            DataUtil.load(inputStream, "UTF-8", "");
            // Assert expected result
            
            // Clean up the temporary test file
            inputFile.delete();
        } catch (Exception e) {
            fail("An unexpected exception occurred");
        }
    }
    
    @Test
    public void testLoad_InputStream_Invalid() {
        try {
            // Create a temporary test file
            File inputFile = File.createTempFile("test", ".html");
            
            // Write some content to the file
            
            FileInputStream inputStream = new FileInputStream(inputFile);
            
            // Test loading the input stream with non-existent file
            DataUtil.load(new FileInputStream("non_existent_file.html"), "UTF-8", "http://example.com");
            // Assert expected result
            
            // Test loading the input stream with invalid base URI
            DataUtil.load(inputStream, "UTF-8", "");
            // Assert expected result
            
            // Clean up the temporary test file
            inputFile.delete();
        } catch (Exception e) {
            fail("An unexpected exception occurred");
        }
    }
    
    @Test
    public void testLoad_InputStream_Parser_Valid() {
        try {
            // Create a temporary test file
            File inputFile = File.createTempFile("test", ".html");
            
            // Write some content to the file
            
            FileInputStream inputStream = new FileInputStream(inputFile);
            
            // Test loading the input stream with valid charset, base URI and parser
            DataUtil.load(inputStream, "UTF-8", "http://example.com", Parser.htmlParser());
            // Assert expected result
            
            // Test loading the input stream with invalid charset
            DataUtil.load(inputStream, "INVALID_CHARSET", "http://example.com", Parser.htmlParser());
            // Assert expected result
            
            // Test loading the input stream with invalid base URI
            DataUtil.load(inputStream, "UTF-8", "", Parser.htmlParser());
            // Assert expected result
            
            // Clean up the temporary test file
            inputFile.delete();
        } catch (Exception e) {
            fail("An unexpected exception occurred");
        }
    }
    
    @Test
    public void testLoad_InputStream_Parser_Invalid() {
        try {
            // Create a temporary test file
            File inputFile = File.createTempFile("test", ".html");
            
            // Write some content to the file
            
            FileInputStream inputStream = new FileInputStream(inputFile);
            
            // Test loading the input stream with non-existent file
            DataUtil.load(new FileInputStream("non_existent_file.html"), "UTF-8", "http://example.com", Parser.htmlParser());
            // Assert expected result
            
            // Test loading the input stream with invalid base URI
            DataUtil.load(inputStream, "UTF-8", "", Parser.htmlParser());
            // Assert expected result
            
            // Clean up the temporary test file
            inputFile.delete();
        } catch (Exception e) {
            fail("An unexpected exception occurred");
        }
    }

    @Test
    public void testParseByteData_Valid() {
        try {
            // Create a temporary test file
            File inputFile = File.createTempFile("test", ".html");
            
            // Write some content to the file
            
            FileInputStream inputStream = new FileInputStream(inputFile);
            
            // Test parsing byte data with valid charset, base URI, and parser
            DataUtil.parseByteData(ByteBuffer.wrap(inputStream.readAllBytes()), "UTF-8", "http://example.com", Parser.htmlParser());
            // Assert expected result
            
            // Test parsing byte data with valid charset and base URI, but invalid parser
            DataUtil.parseByteData(ByteBuffer.wrap(inputStream.readAllBytes()), "UTF-8", "http://example.com", Parser.xmlParser());
            // Assert expected result
            
            // Clean up the temporary test file
            inputFile.delete();
        } catch (Exception e) {
            fail("An unexpected exception occurred");
        }
    }
    
    @Test
    public void testParseByteData_Invalid() {
        try {
            // Create a temporary test file
            File inputFile = File.createTempFile("test", ".html");
            
            // Write some content to the file
            
            FileInputStream inputStream = new FileInputStream(inputFile);
            
            // Test parsing byte data with invalid charset
            DataUtil.parseByteData(ByteBuffer.wrap(inputStream.readAllBytes()), "INVALID_CHARSET", "http://example.com", Parser.htmlParser());
            // Assert expected result
            
            // Test parsing byte data with invalid base URI
            DataUtil.parseByteData(ByteBuffer.wrap(inputStream.readAllBytes()), "UTF-8", "", Parser.htmlParser());
            // Assert expected result
            
            // Clean up the temporary test file
            inputFile.delete();
        } catch (Exception e) {
            fail("An unexpected exception occurred");
        }
    }
    
    @Test
    public void testReadToByteBuffer_Valid() {
        try {
            // Create a temporary test file
            File inputFile = File.createTempFile("test", ".html");
            
            // Write some content to the file
            
            FileInputStream inputStream = new FileInputStream(inputFile);
            
            // Test reading input stream to byte buffer with unlimited max size
            DataUtil.readToByteBuffer(inputStream, 0);
            // Assert expected result
            
            // Test reading input stream to byte buffer with limited max size
            DataUtil.readToByteBuffer(inputStream, 1024);
            // Assert expected result
            
            // Clean up the temporary test file
            inputFile.delete();
        } catch (Exception e) {
            fail("An unexpected exception occurred");
        }
    }
    
    @Test
    public void testReadToByteBuffer_Invalid() {
        try {
            // Test reading a non-existent file to byte buffer
            DataUtil.readToByteBuffer(new FileInputStream("non_existent_file.html"), 0);
            // Assert expected result
        } catch (Exception e) {
            fail("An unexpected exception occurred");
        }
    }
    
    @Test
    public void testReadToByteBuffer_NegativeMaxSize() {
        try {
            // Create a temporary test file
            File inputFile = File.createTempFile("test", ".html");
            
            // Write some content to the file
            
            FileInputStream inputStream = new FileInputStream(inputFile);
            
            // Test reading input stream to byte buffer with negative max size
            DataUtil.readToByteBuffer(inputStream, -1);
            // Assert expected result
            
            // Clean up the temporary test file
            inputFile.delete();
        } catch (Exception e) {
            fail("An unexpected exception occurred");
        }
    }
    
    @Test
    public void testGetCharsetFromContentType_Valid() {
        // Test getting charset from valid content type header
        String charset = DataUtil.getCharsetFromContentType("text/html; charset=utf-8");
        assertEquals("UTF-8", charset);
        
        // Test getting charset from valid content type header with uppercase charset value
        charset = DataUtil.getCharsetFromContentType("text/html; charset=UTF-8");
        assertEquals("UTF-8", charset);
    }
    
    @Test
    public void testGetCharsetFromContentType_Invalid() {
        // Test getting charset from invalid content type header
        String charset = DataUtil.getCharsetFromContentType("text/html; charset=INVALID_CHARSET");
        assertNull(charset);
        
        // Test getting charset from null content type header
        charset = DataUtil.getCharsetFromContentType(null);
        assertNull(charset);
    }
}