import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {
    
    @Test
    public void testLoadWithValidFile() {
        String filePath = "src/test/resources/testfile.html";
        File file = new File(filePath);
        
        try {
            Document document = DataUtil.load(file, "UTF-8", "");
            assertNotNull(document);
            assertEquals(1, document.select("div").size());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }
    
    @Test(expected = FileNotFoundException.class)
    public void testLoadWithInvalidFile() throws IOException {
        String filePath = "src/test/resources/nonexistent.html";
        File file = new File(filePath);
        
        DataUtil.load(file, "UTF-8", "");
    }
    
    @Test
    public void testLoadWithNullCharset() {
        String filePath = "src/test/resources/testfile.html";
        File file = new File(filePath);
        
        try {
            Document document = DataUtil.load(file, null, "");
            assertNotNull(document);
            assertEquals(1, document.select("div").size());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }
    
    @Test
    public void testLoadWithInputStream() {
        String filePath = "src/test/resources/testfile.html";
        try {
            InputStream inputStream = new FileInputStream(filePath);
            
            Document document = DataUtil.load(inputStream, "UTF-8", "");
            assertNotNull(document);
            assertEquals(1, document.select("div").size());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }
    
    @Test
    public void testLoadWithInputStreamAndParser() {
        String filePath = "src/test/resources/testfile.html";
        try {
            InputStream inputStream = new FileInputStream(filePath);
            
            Document document = DataUtil.load(inputStream, "UTF-8", "", Parser.htmlParser());
            assertNotNull(document);
            assertEquals(1, document.select("div").size());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }
    
    @Test
    public void testParseByteDataWithValidData() {
        String data = "<div>Hello World</div>";
        byte[] byteData = data.getBytes(StandardCharsets.UTF_8);
        
        Document document = DataUtil.parseByteData(ByteBuffer.wrap(byteData), "UTF-8", "", Parser.htmlParser());
        assertNotNull(document);
        assertEquals(1, document.select("div").size());
    }
    
    @Test
    public void testParseByteDataWithInvalidCharset() {
        String data = "<div>Hello World</div>";
        byte[] byteData = data.getBytes(StandardCharsets.UTF_8);
        
        Document document = DataUtil.parseByteData(ByteBuffer.wrap(byteData), "ISO-8859-1", "", Parser.htmlParser());
        assertNotNull(document);
        assertEquals(1, document.select("div").size());
    }
    
    @Test
    public void testReadToByteBufferWithValidInputStream() {
        String filePath = "src/test/resources/testfile.html";
        try {
            InputStream inputStream = new FileInputStream(filePath);
            
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 0);
            assertTrue(byteBuffer.limit() > 0);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }
    
    @Test
    public void testReadToByteBufferWithLimitedInputStream() {
        String filePath = "src/test/resources/testfile.html";
        try {
            InputStream inputStream = new FileInputStream(filePath);
            
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 10);
            assertTrue(byteBuffer.limit() <= 10);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }
    
    @Test
    public void testGetCharsetFromContentTypeWithValidContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNotNull(charset);
        assertEquals("UTF-8", charset);
    }
    
    @Test
    public void testGetCharsetFromContentTypeWithInvalidContentType() {
        String contentType = "text/html; charset=INVALID";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }
}