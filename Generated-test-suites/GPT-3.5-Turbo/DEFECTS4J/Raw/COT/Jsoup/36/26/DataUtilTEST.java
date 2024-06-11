import static org.junit.Assert.*;
import org.jsoup.helper.DataUtil;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

public class DataUtilTest {
    
    @Test
    public void testLoad_File_CharsetName_BaseUri() {
        try {
            String baseUri = "http://www.example.com";
            File file = new File("test.html");
            String charsetName = "UTF-8";
            
            Document document = DataUtil.load(file, charsetName, baseUri);
            
            assertNotNull(document);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testLoad_InputStream_CharsetName_BaseUri() {
        try {
            String baseUri = "http://www.example.com";
            FileInputStream inputStream = new FileInputStream("test.html");
            String charsetName = "UTF-8";
            
            Document document = DataUtil.load(inputStream, charsetName, baseUri);
            
            assertNotNull(document);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testLoad_InputStream_CharsetName_BaseUri_Parser() {
        try {
            String baseUri = "http://www.example.com";
            FileInputStream inputStream = new FileInputStream("test.html");
            String charsetName = "UTF-8";
            Parser parser = Parser.htmlParser();
            
            Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
            
            assertNotNull(document);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.wrap("test".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Parser parser = Parser.htmlParser();
        
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        
        assertNotNull(document);
    }
    
    @Test
    public void testReadToByteBuffer_InputStream_MaxSize() {
        try {
            FileInputStream inputStream = new FileInputStream("test.html");
            int maxSize = 0;
            
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);
            
            assertNotNull(byteBuffer);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testReadToByteBuffer_InputStream() {
        try {
            FileInputStream inputStream = new FileInputStream("test.html");
            
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
            
            assertNotNull(byteBuffer);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        assertEquals("UTF-8", charset);
    }
}