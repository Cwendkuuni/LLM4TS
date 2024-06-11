import org.jsoup.helper.DataUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import static org.junit.Assert.*;
import org.junit.Test;

public class DataUtilTest {
    
    @Test
    public void testLoadFromFile() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        Document doc = DataUtil.load(file, charsetName, baseUri);
        
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
    }
    
    @Test
    public void testLoadFromInputStream() throws IOException {
        InputStream in = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        Document doc = DataUtil.load(in, charsetName, baseUri);
        
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
    }
    
    @Test
    public void testLoadFromInputStreamAndParser() throws IOException {
        InputStream in = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        
        Document doc = DataUtil.load(in, charsetName, baseUri, parser);
        
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
    }
    
    @Test
    public void testParseByteData() throws IOException {
        String data = "<html><head></head><body><h1>Hello World!</h1></body></html>";
        byte[] bytes = data.getBytes();
        ByteBuffer byteData = ByteBuffer.wrap(bytes);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertEquals(1, doc.select("h1").size());
        assertEquals("Hello World!", doc.select("h1").text());
    }
    
    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        File file = new File("test.html");
        InputStream in = new FileInputStream(file);
        int maxSize = 4096;
        
        ByteBuffer byteData = DataUtil.readToByteBuffer(in, maxSize);
        
        assertNotNull(byteData);
        assertEquals(maxSize, byteData.limit());
    }
    
    @Test
    public void testReadToByteBufferWithoutMaxSize() throws IOException {
        File file = new File("test.html");
        InputStream in = new FileInputStream(file);
        
        ByteBuffer byteData = DataUtil.readToByteBuffer(in);
        
        assertNotNull(byteData);
        assertEquals(DataUtil.bufferSize, byteData.limit());
    }
    
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        assertNotNull(charset);
        assertEquals("UTF-8", charset);
    }
}