import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.File;
import java.io.InputStream;
import java.io.IOException;

public class DataUtilTest {
    
    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, "UTF-8", "http://example.com");
        
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
    }
    
    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inputStream = this.getClass().getResourceAsStream("test.html");
        Document doc = DataUtil.load(inputStream, "UTF-8", "http://example.com");
        
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
    }
    
    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        InputStream inputStream = this.getClass().getResourceAsStream("test.html");
        Document doc = DataUtil.load(inputStream, "UTF-8", "http://example.com", Parser.xmlParser());
        
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
    }
    
    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.allocate(1024);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
    }
    
    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = this.getClass().getResourceAsStream("test.html");
        int maxSize = 1024;
        
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, maxSize);
        
        assertNotNull(byteData);
        assertTrue(byteData.capacity() <= maxSize);
    }
    
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        assertNotNull(charset);
        assertEquals("UTF-8", charset);
    }
}