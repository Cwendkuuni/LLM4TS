import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Locale;

import static org.junit.Assert.*;

public class DataUtilTest {

    @Test
    public void testLoadFile() {
        try {
            File file = new File("test.html");
            String charsetName = "UTF-8";
            String baseUri = "http://www.example.com";
            Document doc = DataUtil.load(file, charsetName, baseUri);
            
            assertEquals(baseUri, doc.baseUri());
            assertNotNull(doc);
            
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testLoadInputStream() {
        try {
            InputStream inputStream = getClass().getResourceAsStream("test.html");
            String charsetName = "UTF-8";
            String baseUri = "http://www.example.com";
            Document doc = DataUtil.load(inputStream, charsetName, baseUri);
            
            assertEquals(baseUri, doc.baseUri());
            assertNotNull(doc);
            
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testLoadInputStreamWithParser() {
        try {
            InputStream inputStream = getClass().getResourceAsStream("test.html");
            String charsetName = "UTF-8";
            String baseUri = "http://www.example.com";
            Parser parser = Parser.htmlParser();
            Document doc = DataUtil.load(inputStream, charsetName, baseUri, parser);
            
            assertEquals(baseUri, doc.baseUri());
            assertNotNull(doc);
            
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.allocate(20);
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        assertEquals(baseUri, doc.baseUri());
        assertNotNull(doc);
    }

    @Test
    public void testReadToByteBufferWithMaxSize() {
        try {
            InputStream inputStream = getClass().getResourceAsStream("test.html");
            ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, 1024);
            
            assertNotNull(byteData);
            assertEquals(1024, byteData.capacity());
            
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testReadToByteBuffer() {
        try {
            InputStream inputStream = getClass().getResourceAsStream("test.html");
            ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
            
            assertNotNull(byteData);
            
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        assertEquals("UTF-8", charset);
    }
    
    @Test
    public void testGetCharsetFromContentTypeWithInvalidCharset() {
        String contentType = "text/html; charset=INVALID";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        assertNull(charset);
    }
    
    @Test
    public void testGetCharsetFromContentTypeWithNoCharset() {
        String contentType = "text/html";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        assertNull(charset);
    }

}