import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

public class DataUtilTest {
    @Test
    public void testLoad() throws IOException {
        String baseUri = "http://example.com/";
        File file = new File("test.html");
        String charsetName = "UTF-8";
        
        Document doc = DataUtil.load(file, charsetName, baseUri);
        
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
    }
    
    @Test 
    public void testLoadWithInputStream() throws IOException {
        String baseUri = "http://example.com/";
        InputStream in = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        
        Document doc = DataUtil.load(in, charsetName, baseUri);
        
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
    }
    
    @Test
    public void testLoadWithParser() throws IOException {
        String baseUri = "http://example.com/";
        InputStream in = new FileInputStream("test.xml");
        String charsetName = "UTF-8";
        Parser parser = Parser.xmlParser();
        
        Document doc = DataUtil.load(in, charsetName, baseUri, parser);
        
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
    }
    
    @Test
    public void testCrossStreams() throws IOException {
        InputStream in = new FileInputStream("input.txt");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        DataUtil.crossStreams(in, out);
        
        assertEquals("expectedOutput", out.toString());
    }
    
    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream in = new FileInputStream("input.txt");
        int maxSize = 1024;
        
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(in, maxSize);
        
        assertNotNull(byteBuffer);
        assertTrue(byteBuffer.remaining() <= maxSize);
    }
    
    @Test 
    public void testGetCharsetFromContentType() {
        String contentType = "text/html;charset=utf-8";
        
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        assertNotNull(charset);
        assertEquals("UTF-8", charset);
    }
    
    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        
        assertNotNull(mimeBoundary);
        assertEquals(32, mimeBoundary.length());
    }
}