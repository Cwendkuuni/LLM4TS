
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class DataUtilTest {
    // Test load(File in, String charsetName, String baseUri)
    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        Document doc = DataUtil.load(file, charsetName, baseUri);
        
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
    }
    
    // Test load(InputStream in, String charsetName, String baseUri)
    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inStream = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        Document doc = DataUtil.load(inStream, charsetName, baseUri);
        
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
        
        inStream.close();
    }
    
    // Test load(InputStream in, String charsetName, String baseUri, Parser parser)
    @Test
    public void testLoadInputStreamWithCustomParser() throws IOException {
        InputStream inStream = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.xmlParser();
        
        Document doc = DataUtil.load(inStream, charsetName, baseUri, parser);
        
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
        
        inStream.close();
    }
    
    // Test parseByteData(ByteBuffer byteData, String charsetName, String baseUri, Parser parser)
    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.wrap("test".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
    }
    
    // Test readToByteBuffer(InputStream inStream, int maxSize)
    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inStream = new FileInputStream("test.html");
        int maxSize = 1000;
        
        ByteBuffer byteData = DataUtil.readToByteBuffer(inStream, maxSize);
        
        assertNotNull(byteData);
        assertTrue(byteData.capacity() <= maxSize);
        
        inStream.close();
    }
    
    // Test readToByteBuffer(InputStream inStream)
    @Test
    public void testReadToByteBufferUnlimitedSize() throws IOException {
        InputStream inStream = new FileInputStream("test.html");
        
        ByteBuffer byteData = DataUtil.readToByteBuffer(inStream);
        
        assertNotNull(byteData);
        
        inStream.close();
    }
    
    // Test getCharsetFromContentType(String contentType)
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        assertNotNull(charset);
        assertEquals("UTF-8", charset);
    }
}

