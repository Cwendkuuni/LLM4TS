import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class DataUtilTest {
    
    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, "UTF-8", "http://example.com");
        
        assertNotNull(doc);
    }
    
    @Test
    public void testLoadInputStream() throws IOException {
        InputStream in = getClass().getResourceAsStream("test.html");
        Document doc = DataUtil.load(in, "UTF-8", "http://example.com");
        
        assertNotNull(doc);
    }
    
    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        InputStream in = getClass().getResourceAsStream("test.html");
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.load(in, "UTF-8", "http://example.com", parser);
        
        assertNotNull(doc);
    }
    
    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.allocate(1024);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        
        assertNotNull(doc);
    }
    
    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        InputStream inStream = getClass().getResourceAsStream("test.html");
        int maxSize = 1024;
        ByteBuffer byteData = DataUtil.readToByteBuffer(inStream, maxSize);
        
        assertNotNull(byteData);
    }
    
    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inStream = getClass().getResourceAsStream("test.html");
        ByteBuffer byteData = DataUtil.readToByteBuffer(inStream);
        
        assertNotNull(byteData);
    }
    
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        assertEquals("UTF-8", charset);
    }
}