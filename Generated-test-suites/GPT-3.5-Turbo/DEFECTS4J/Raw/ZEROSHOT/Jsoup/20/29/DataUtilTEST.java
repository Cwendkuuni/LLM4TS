import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.helper.DataUtil;

import java.io.*;
import java.nio.charset.Charset;

public class DataUtilTest {

    @Test
    public void testLoad_File_CharsetName_BaseUri() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        Document doc = DataUtil.load(file, charsetName, baseUri);
        
        assertNotNull(doc);
    }
    
    @Test
    public void testLoad_InputStream_CharsetName_BaseUri() throws IOException {
        InputStream in = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        Document doc = DataUtil.load(in, charsetName, baseUri);
        
        assertNotNull(doc);
        
        in.close();
    }
    
    @Test
    public void testLoad_InputStream_CharsetName_BaseUri_Parser() throws IOException {
        InputStream in = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        
        Document doc = DataUtil.load(in, charsetName, baseUri, parser);
        
        assertNotNull(doc);
        
        in.close();
    }
    
    @Test
    public void testParseByteData_withCharsetName() throws IOException {
        ByteBuffer byteData = ByteBuffer.wrap("<html></html>".getBytes(Charset.forName("UTF-8")));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        
        assertNotNull(doc);
    }
    
    @Test
    public void testParseByteData_withoutCharsetName() throws IOException {
        ByteBuffer byteData = ByteBuffer.wrap("<html></html>".getBytes(Charset.forName("UTF-8")));
        String charsetName = null;
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        
        assertNotNull(doc);
    }
    
    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream in = new FileInputStream("test.html");
        
        ByteBuffer byteData = DataUtil.readToByteBuffer(in);
        
        assertNotNull(byteData);
        
        in.close();
    }
    
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        assertEquals("UTF-8", charset);
    }
}