import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class DataUtilTest {
    
    @Test
    public void testLoadFile() {
        try {
            File file = new File("test.html");
            String charsetName = "UTF-8";
            String baseUri = "http://example.com";
            
            Document document = DataUtil.load(file, charsetName, baseUri);
            
            Assert.assertNotNull(document);
            Assert.assertEquals(charsetName, document.charset().name());
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail("Failed to load file");
        }
    }
    
    @Test
    public void testLoadInputStream() {
        try {
            InputStream inputStream = getClass().getResourceAsStream("/test.html");
            String charsetName = "UTF-8";
            String baseUri = "http://example.com";
            
            Document document = DataUtil.load(inputStream, charsetName, baseUri);
            
            Assert.assertNotNull(document);
            Assert.assertEquals(charsetName, document.charset().name());
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail("Failed to load input stream");
        }
    }
    
    @Test
    public void testLoadInputStreamWithParser() {
        try {
            InputStream inputStream = getClass().getResourceAsStream("/test.html");
            String charsetName = "UTF-8";
            String baseUri = "http://example.com";
            Parser parser = Parser.htmlParser();
            
            Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
            
            Assert.assertNotNull(document);
            Assert.assertEquals(charsetName, document.charset().name());
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail("Failed to load input stream with parser");
        }
    }
    
    @Test
    public void testParseByteData() {
        try {
            String html = "<html><body><h1>Hello World!</h1></body></html>";
            Charset charset = Charset.forName("UTF-8");
            byte[] bytes = html.getBytes(charset);
            ByteBuffer byteData = ByteBuffer.wrap(bytes);
            String charsetName = "UTF-8";
            String baseUri = "http://example.com";
            Parser parser = Parser.htmlParser();
            
            Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
            
            Assert.assertNotNull(document);
            Assert.assertEquals(charsetName, document.charset().name());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Failed to parse byte data");
        }
    }
    
    @Test
    public void testReadToByteBuffer() {
        try {
            InputStream inputStream = getClass().getResourceAsStream("/test.html");
            int maxSize = 0;
            
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);
            
            Assert.assertNotNull(byteBuffer);
            Assert.assertTrue(byteBuffer.capacity() > 0);
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail("Failed to read to byte buffer");
        }
    }
    
    @Test
    public void testReadToByteBufferWithLimitedSize() {
        try {
            InputStream inputStream = getClass().getResourceAsStream("/test.html");
            int maxSize = 1024;
            
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);
            
            Assert.assertNotNull(byteBuffer);
            Assert.assertTrue(byteBuffer.capacity() > 0);
            Assert.assertTrue(byteBuffer.capacity() <= maxSize);
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail("Failed to read to byte buffer with limited size");
        }
    }
    
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        Assert.assertNotNull(charset);
        Assert.assertEquals("UTF-8", charset);
    }
    
    
}