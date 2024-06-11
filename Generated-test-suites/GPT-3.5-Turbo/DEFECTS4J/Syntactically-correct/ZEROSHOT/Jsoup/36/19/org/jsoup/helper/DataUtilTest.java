import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {
    
    @Test
    public void testLoadFile() {
        try {
            File file = new File("test.html");
            Document document = DataUtil.load(file, "UTF-8", "");
            
            assertNotNull(document);
        } catch (IOException e) {
            fail("Should not throw IOException");
        }
    }
    
    @Test
    public void testLoadInputStream() {
        try {
            String html = "<html><body><h1>Hello, World!</h1></body></html>";
            InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
            
            Document document = DataUtil.load(inputStream, "UTF-8", "");
            
            assertNotNull(document);
        } catch (IOException e) {
            fail("Should not throw IOException");
        }
    }
    
    @Test
    public void testLoadInputStreamWithParser() {
        try {
            String xml = "<root><element><subelement>Test</subelement></element></root>";
            InputStream inputStream = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
            
            Parser parser = Parser.xmlParser();
            Document document = DataUtil.load(inputStream, "UTF-8", "", parser);
            
            assertNotNull(document);
        } catch (IOException e) {
            fail("Should not throw IOException");
        }
    }
    
    @Test
    public void testParseByteData() {
        try {
            String html = "<html><body><h1>Hello, World!</h1></body></html>";
            byte[] bytes = html.getBytes(StandardCharsets.UTF_8);
            ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
            
            Document document = DataUtil.parseByteData(byteBuffer, "UTF-8", "", Parser.htmlParser());
            
            assertNotNull(document);
        } catch (Exception e) {
            fail("Should not throw Exception");
        }
    }
    
    @Test
    public void testReadToByteBuffer() {
        try {
            String text = "This is a test.";
            ByteArrayInputStream inputStream = new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
            
            assertNotNull(byteBuffer);
        } catch (IOException e) {
            fail("Should not throw IOException");
        }
    }
    
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        assertEquals("UTF-8", charset);
    }
    
    @Test
    public void testGetCharsetFromContentTypeUnsupported() {
        String contentType = "text/html; charset=ISO-8859-1";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        assertNull(charset);
    }
}