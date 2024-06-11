import org.jsoup.helper.DataUtil;

import org.junit.Test;
import static org.junit.Assert.*;

public class DataUtilTest {
    
    @Test
    public void testLoadFile() {
        // Typical use case: Load file with valid charset and baseUri
        String charsetName = "UTF-8";
        String baseUri = "https://example.com";
        File file = new File("path/to/file.html");
        try {
            Document document = DataUtil.load(file, charsetName, baseUri);
            assertNotNull(document);
        } catch (IOException e) {
            fail("IOException occurred.");
        }
        
        // Edge cases: Load file with null charset and baseUri
        charsetName = null;
        baseUri = null;
        try {
            Document document = DataUtil.load(file, charsetName, baseUri);
            assertNotNull(document);
        } catch (IOException e) {
            fail("IOException occurred.");
        }
        
        // Error scenario: Load non-existent file
        file = new File("path/to/nonexistent.html");
        try {
            Document document = DataUtil.load(file, charsetName, baseUri);
            fail("IOException should have been thrown.");
        } catch (IOException e) {
            // Success
        }
    }
    
    @Test
    public void testLoadInputStream() {
        // Typical use case: Load input stream with valid charset and baseUri
        String charsetName = "UTF-8";
        String baseUri = "https://example.com";
        InputStream inputStream = new ByteArrayInputStream("Test".getBytes());
        try {
            Document document = DataUtil.load(inputStream, charsetName, baseUri);
            assertNotNull(document);
        } catch (IOException e) {
            fail("IOException occurred.");
        }
        
        // Edge cases: Load input stream with null charset and baseUri
        charsetName = null;
        baseUri = null;
        try {
            Document document = DataUtil.load(inputStream, charsetName, baseUri);
            assertNotNull(document);
        } catch (IOException e) {
            fail("IOException occurred.");
        }
        
        // Error scenario: Load null input stream
        inputStream = null;
        try {
            Document document = DataUtil.load(inputStream, charsetName, baseUri);
            fail("IOException should have been thrown.");
        } catch (IOException e) {
            // Success
        }
    }
    
    @Test
    public void testLoadInputStreamWithParser() {
        // Typical use case: Load input stream with valid charset, baseUri, and parser
        String charsetName = "UTF-8";
        String baseUri = "https://example.com";
        Parser parser = Parser.htmlParser();
        InputStream inputStream = new ByteArrayInputStream("Test".getBytes());
        try {
            Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
            assertNotNull(document);
        } catch (IOException e) {
            fail("IOException occurred.");
        }
        
        // Edge cases: Load input stream with null charset, baseUri, and parser
        charsetName = null;
        baseUri = null;
        parser = null;
        try {
            Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
            assertNotNull(document);
        } catch (IOException e) {
            fail("IOException occurred.");
        }
        
        // Error scenario: Load null input stream with valid charset, baseUri, and parser
        inputStream = null;
        charsetName = "UTF-8";
        baseUri = "https://example.com";
        parser = Parser.htmlParser();
        try {
            Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
            fail("IOException should have been thrown.");
        } catch (IOException e) {
            // Success
        }
    }
    
    @Test
    public void testParseByteData() {
        // Typical use case: Parse valid byte data with valid charset, baseUri, and parser
        String charsetName = "UTF-8";
        String baseUri = "https://example.com";
        Parser parser = Parser.htmlParser();
        ByteBuffer byteData = ByteBuffer.wrap("Test".getBytes());
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(document);
        
        // Edge cases: Parse byte data with null charset, baseUri, and parser
        charsetName = null;
        baseUri = null;
        parser = null;
        document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(document);
        
        // Error scenario: Parse null byte data with valid charset, baseUri, and parser
        byteData = null;
        charsetName = "UTF-8";
        baseUri = "https://example.com";
        parser = Parser.htmlParser();
        document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNull(document);
    }
    
    @Test
    public void testReadToByteBuffer() {
        // Typical use case: Read data from valid input stream to byte buffer
        InputStream inputStream = new ByteArrayInputStream("Test".getBytes());
        try {
            ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
            assertNotNull(byteData);
        } catch (IOException e) {
            fail("IOException occurred.");
        }
        
        // Edge case: Read data from empty input stream
        inputStream = new ByteArrayInputStream(new byte[0]);
        try {
            ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
            assertNotNull(byteData);
        } catch (IOException e) {
            fail("IOException occurred.");
        }
        
        // Edge case: Read data from null input stream
        inputStream = null;
        try {
            ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
            assertNull(byteData);
        } catch (IOException e) {
            fail("IOException occurred.");
        }
    }
    
    @Test
    public void testGetCharsetFromContentType() {
        // Typical use case: Extract valid charset from content type
        String contentType = "text/html; charset=UTF-8";
        String expectedCharset = "UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(expectedCharset, charset);
        
        // Edge cases: Extract charset from null content type and empty content type
        contentType = null;
        expectedCharset = null;
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(expectedCharset, charset);
        
        contentType = "";
        expectedCharset = null;
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(expectedCharset, charset);
        
        // Error scenario: Extract charset from invalid content type
        contentType = "text/html; encoding=UTF-8";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }
}