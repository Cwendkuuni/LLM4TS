import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.helper.DataUtil;

public class DataUtilTest {
    
    @Test
    public void testLoadFile() {
        // Test case 1: valid file with UTF-8 charset
        try {
            DataUtil.load(new java.io.File("test.html"), "UTF-8", "https://example.com");
        } catch (java.io.IOException e) {
            fail("Failed to load file");
        }
        
        // Test case 2: invalid file
        try {
            DataUtil.load(new java.io.File("invalid.html"), "UTF-8", "https://example.com");
            fail("Able to load invalid file");
        } catch (java.io.IOException e) {
            // Exception is expected
        }
    }
    
    @Test
    public void testLoadInputStream() {
        // Test case 1: valid input stream with UTF-8 charset
        try {
            java.io.InputStream inputStream = new java.io.FileInputStream("test.html");
            DataUtil.load(inputStream, "UTF-8", "https://example.com");
        } catch (java.io.IOException e) {
            fail("Failed to load input stream");
        }
        
        // Test case 2: invalid input stream
        try {
            java.io.InputStream inputStream = new java.io.FileInputStream("invalid.html");
            DataUtil.load(inputStream, "UTF-8", "https://example.com");
            fail("Able to load invalid input stream");
        } catch (java.io.IOException e) {
            // Exception is expected
        }
    }
    
    @Test
    public void testLoadInputStreamWithParser() {
        // Test case 1: valid input stream with UTF-8 charset and HTML parser
        try {
            java.io.InputStream inputStream = new java.io.FileInputStream("test.html");
            DataUtil.load(inputStream, "UTF-8", "https://example.com", org.jsoup.parser.Parser.htmlParser());
        } catch (java.io.IOException e) {
            fail("Failed to load input stream with parser");
        }
        
        // Test case 2: invalid input stream with XML parser
        try {
            java.io.InputStream inputStream = new java.io.FileInputStream("invalid.html");
            DataUtil.load(inputStream, "UTF-8", "https://example.com", org.jsoup.parser.Parser.xmlParser());
            fail("Able to load invalid input stream with parser");
        } catch (java.io.IOException e) {
            // Exception is expected
        }
    }
    
    @Test
    public void testParseByteData() {
        // Test case 1: valid byte data with UTF-8 charset
        java.nio.ByteBuffer byteData = java.nio.ByteBuffer.wrap("test".getBytes());
        org.jsoup.nodes.Document doc = DataUtil.parseByteData(byteData, "UTF-8", "https://example.com", org.jsoup.parser.Parser.htmlParser());
        assertNotNull(doc);
        
        // Test case 2: invalid byte data
        byteData = java.nio.ByteBuffer.wrap("invalid".getBytes());
        doc = DataUtil.parseByteData(byteData, "UTF-8", "https://example.com", org.jsoup.parser.Parser.htmlParser());
        assertNull(doc);
    }
    
    @Test
    public void testReadToByteBuffer() {
        // Test case 1: valid input stream
        try {
            java.io.InputStream inputStream = new java.io.FileInputStream("test.html");
            java.nio.ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
            assertNotNull(byteData);
        } catch (java.io.IOException e) {
            fail("Failed to read input stream to byte buffer");
        }
        
        // Test case 2: invalid input stream
        try {
            java.io.InputStream inputStream = new java.io.FileInputStream("invalid.html");
            java.nio.ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
            assertNull(byteData);
        } catch (java.io.IOException e) {
            // Exception is expected
        }
    }
    
    @Test
    public void testGetCharsetFromContentType() {
        // Test case 1: valid content type with charset
        String charset = DataUtil.getCharsetFromContentType("text/html; charset=UTF-8");
        assertEquals("UTF-8", charset);
        
        // Test case 2: valid content type without charset
        charset = DataUtil.getCharsetFromContentType("text/html");
        assertNull(charset);
        
        // Test case 3: invalid content type
        charset = DataUtil.getCharsetFromContentType("invalid/content-type");
        assertNull(charset);
    }
}