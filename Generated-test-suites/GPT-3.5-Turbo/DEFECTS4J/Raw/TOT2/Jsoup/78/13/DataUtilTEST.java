import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class DataUtilTest {

    // Test for load(File in, String charsetName, String baseUri) method
    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        Document doc = DataUtil.load(file, charsetName, baseUri);
        
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
    }

    // Test for load(InputStream in, String charsetName, String baseUri) method
    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        Document doc = DataUtil.load(inputStream, charsetName, baseUri);
        
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
    }

    // Test for load(InputStream in, String charsetName, String baseUri, Parser parser) method
    @Test
    public void testLoadWithParser() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        
        Document doc = DataUtil.load(inputStream, charsetName, baseUri, parser);
        
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
    }

    // Test for readToByteBuffer(InputStream inStream, int maxSize) method
    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = new FileInputStream("test.txt");
        int maxSize = 1024;
        
        ByteBuffer buffer = DataUtil.readToByteBuffer(inputStream, maxSize);
        
        assertNotNull(buffer);
        assertTrue(buffer.capacity() <= maxSize);
    }

    // Test for getCharsetFromContentType(String contentType) method with valid content type
    @Test
    public void testGetCharsetFromValidContentType() {
        String contentType = "text/html; charset=UTF-8";
        
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        assertNotNull(charset);
        assertEquals("UTF-8", charset);
    }

    // Test for getCharsetFromContentType(String contentType) method with invalid content type
    @Test
    public void testGetCharsetFromInvalidContentType() {
        String contentType = "text/html";
        
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        assertNull(charset);
    }

}