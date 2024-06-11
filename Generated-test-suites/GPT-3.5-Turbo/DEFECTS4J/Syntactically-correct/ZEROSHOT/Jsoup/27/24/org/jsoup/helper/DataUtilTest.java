import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

public class DataUtilTest {
    
    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        
        Document document = DataUtil.load(file, charsetName, baseUri);
        
        assertNotNull(document);
    }
    
    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        
        assertNotNull(document);
    }
    
    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        Parser parser = Parser.htmlParser();
        
        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
        
        assertNotNull(document);
    }
    
    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.allocate(1024);
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        Parser parser = Parser.htmlParser();
        
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        
        assertNull(document);
    }
    
    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        
        assertNotNull(byteData);
    }
    
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        assertEquals("UTF-8", charset);
    }
}