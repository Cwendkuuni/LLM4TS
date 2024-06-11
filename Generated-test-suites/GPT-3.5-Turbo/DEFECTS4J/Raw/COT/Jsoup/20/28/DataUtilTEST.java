import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import static org.junit.Assert.*;
import org.junit.Test;

public class DataUtilTest {
    
    @Test
    public void testLoad_File() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://example.com";
        
        Document doc = DataUtil.load(file, charsetName, baseUri);
        
        assertNotNull(doc);
    }
    
    @Test
    public void testLoad_InputStream() throws IOException {
        InputStream in = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://example.com";
        
        Document doc = DataUtil.load(in, charsetName, baseUri);
        
        assertNotNull(doc);
    }
    
    @Test
    public void testLoad_InputStream_Parser() throws IOException {
        InputStream in = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://example.com";
        Parser parser = Parser.htmlParser();
        
        Document doc = DataUtil.load(in, charsetName, baseUri, parser);
        
        assertNotNull(doc);
    }
    
    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.wrap("Test Data".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "https://example.com";
        Parser parser = Parser.htmlParser();
        
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        
        assertNotNull(doc);
    }
    
    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream in = new FileInputStream("test.html");
        
        ByteBuffer byteData = DataUtil.readToByteBuffer(in);
        
        assertNotNull(byteData);
    }
    
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        assertEquals("UTF-8", charset);
    }
}