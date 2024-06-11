import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        
        Document doc = DataUtil.load(file, charsetName, baseUri);
        
        Assert.assertNotNull(doc);
        Assert.assertEquals(baseUri, doc.baseUri());
    }
    
    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        
        Document doc = DataUtil.load(inputStream, charsetName, baseUri);
        
        Assert.assertNotNull(doc);
        Assert.assertEquals(baseUri, doc.baseUri());
    }
    
    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        Parser parser = Parser.htmlParser();
        
        Document doc = DataUtil.load(inputStream, charsetName, baseUri, parser);
        
        Assert.assertNotNull(doc);
        Assert.assertEquals(baseUri, doc.baseUri());
    }
    
    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.wrap("<html><body><h1>Hello, World!</h1></body></html>".getBytes(Charset.forName("UTF-8")));
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        Parser parser = Parser.htmlParser();
        
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        
        Assert.assertNotNull(doc);
        Assert.assertEquals(baseUri, doc.baseUri());
    }
    
    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        int maxSize = 10000;
        
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);
        
        Assert.assertNotNull(byteBuffer);
        Assert.assertTrue(byteBuffer.array().length <= maxSize);
    }
    
    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        
        Assert.assertNotNull(byteBuffer);
        Assert.assertTrue(byteBuffer.array().length > 0);
    }
    
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        Assert.assertNotNull(charset);
        Assert.assertEquals("UTF-8", charset);
    }
}