import org.jsoup.helper.*;
import org.jsoup.nodes.*;

import org.junit.Test;
import java.io.*;
import java.nio.ByteBuffer;

import static org.junit.Assert.*;

public class DataUtilTest {
    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        Document doc = DataUtil.load(file, charsetName, baseUri);
        
        assertNotNull(doc);
    }
    
    @Test
    public void testLoadInputStream() throws IOException {
        FileInputStream inStream = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        Document doc = DataUtil.load(inStream, charsetName, baseUri);
        
        assertNotNull(doc);
        
        inStream.close();
    }
    
    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        FileInputStream inStream = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.load(inStream, charsetName, baseUri, parser);
        
        assertNotNull(doc);
        
        inStream.close();
    }
    
    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.allocate(10);
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        
        assertNotNull(doc);
    }
    
    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        FileInputStream inStream = new FileInputStream("test.html");
        int maxSize = 1000;
        ByteBuffer byteData = DataUtil.readToByteBuffer(inStream, maxSize);
        
        assertNotNull(byteData);
        
        inStream.close();
    }
    
    @Test
    public void testReadToByteBuffer() throws IOException {
        FileInputStream inStream = new FileInputStream("test.html");
        ByteBuffer byteData = DataUtil.readToByteBuffer(inStream);
        
        assertNotNull(byteData);
        
        inStream.close();
    }
    
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        assertEquals("UTF-8", charset);
    }
}