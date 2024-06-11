import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;

public class DataUtilTest {
    
    @Test
    public void testLoad_file() throws IOException {
        File file = new File("test.html");
        String charset = "UTF-8";
        String baseUri = "http://example.com";
        
        Document document = DataUtil.load(file, charset, baseUri);
        
        // Assertions
        Assert.assertNotNull(document);
    }
    
    @Test
    public void testLoad_inputStream() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        String charset = "UTF-8";
        String baseUri = "http://example.com";
        
        Document document = DataUtil.load(inputStream, charset, baseUri);
        
        // Assertions
        Assert.assertNotNull(document);
    }
    
    @Test
    public void testLoad_inputStream_withParser() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        String charset = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        
        Document document = DataUtil.load(inputStream, charset, baseUri, parser);
        
        // Assertions
        Assert.assertNotNull(document);
    }
    
    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.allocate(100);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        
        // Assertions
        Assert.assertNotNull(document);
    }
    
    @Test
    public void testReadToByteBuffer_withMaxSize() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        int maxSize = 100;
        
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, maxSize);
        
        // Assertions
        Assert.assertNotNull(byteData);
    }
    
    @Test
    public void testReadToByteBuffer_withoutMaxSize() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        
        // Assertions
        Assert.assertNotNull(byteData);
    }
    
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        // Assertions
        Assert.assertEquals("UTF-8", charset);
    }
}