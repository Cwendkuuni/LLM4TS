import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;

public class DataUtilTest {

    @Test
    public void testLoadFile() {
        try {
            File file = new File("test.html");
            String charsetName = "UTF-8";
            String baseUri = "http://example.com";
            Document doc = DataUtil.load(file, charsetName, baseUri);
            
            assertNotNull(doc);
            assertEquals(baseUri, doc.baseUri());
        } catch (Exception e) {
            fail("An exception occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testLoadInputStream() {
        try {
            InputStream inputStream = new FileInputStream("test.html");
            String charsetName = "UTF-8";
            String baseUri = "http://example.com";
            Document doc = DataUtil.load(inputStream, charsetName, baseUri);
            
            assertNotNull(doc);
            assertEquals(baseUri, doc.baseUri());
            
            inputStream.close();
        } catch (Exception e) {
            fail("An exception occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testLoadInputStreamWithParser() {
        try {
            InputStream inputStream = new FileInputStream("test.html");
            String charsetName = "UTF-8";
            String baseUri = "http://example.com";
            Parser parser = Parser.htmlParser();
            Document doc = DataUtil.load(inputStream, charsetName, baseUri, parser);
            
            assertNotNull(doc);
            assertEquals(baseUri, doc.baseUri());
            
            inputStream.close();
        } catch (Exception e) {
            fail("An exception occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testParseByteData() {
        try {
            ByteBuffer byteData = ByteBuffer.wrap("<html><head></head><body></body></html>".getBytes(Charset.forName("UTF-8")));
            String charsetName = "UTF-8";
            String baseUri = "http://example.com";
            Parser parser = Parser.htmlParser();
            Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
            
            assertNotNull(doc);
            assertEquals(baseUri, doc.baseUri());
            assertEquals("UTF-8", doc.outputSettings().charset().name());
        } catch (Exception e) {
            fail("An exception occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testReadToByteBuffer() {
        try {
            InputStream inputStream = new FileInputStream("test.html");
            ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
            
            assertNotNull(byteData);
            assertTrue(byteData.capacity() > 0);
            
            inputStream.close();
        } catch (Exception e) {
            fail("An exception occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        assertNotNull(charset);
        assertEquals("UTF-8", charset);
    }
}