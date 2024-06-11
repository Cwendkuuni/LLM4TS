import static org.junit.Assert.*;
import org.junit.Test;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

public class DataUtilTest {
    
    @Test
    public void testLoad() throws IOException {
        File file = new File("/path/to/file");
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        
        Document document = DataUtil.load(file, charsetName, baseUri);
        
        assertNotNull(document);
    }
    
    @Test
    public void testLoad_InputStream() throws IOException {
        InputStream inputStream = new FileInputStream("/path/to/file");
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        
        assertNotNull(document);
    }
    
    @Test
    public void testLoad_InputStream_Parser() throws IOException {
        InputStream inputStream = new FileInputStream("/path/to/file");
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Parser parser = Parser.htmlParser();
        
        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
        
        assertNotNull(document);
    }
    
    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.allocate(1024);
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Parser parser = Parser.htmlParser();
        
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        
        assertNotNull(document);
    }
    
    @Test
    public void testReadToByteBuffer_MaxSize() throws IOException {
        InputStream inputStream = new FileInputStream("/path/to/file");
        int maxSize = 1024;
        
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);
        
        assertNotNull(byteBuffer);
    }
    
    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = new FileInputStream("/path/to/file");
        
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        
        assertNotNull(byteBuffer);
    }
    
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        assertNotNull(charset);
        assertEquals("UTF-8", charset);
    }
}