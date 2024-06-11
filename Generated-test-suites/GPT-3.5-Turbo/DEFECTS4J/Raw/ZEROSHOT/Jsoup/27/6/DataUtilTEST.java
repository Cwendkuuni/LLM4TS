import org.jsoup.helper.DataUtil;
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

public class DataUtilTest {
    
    private static final String CHARSET_NAME = "UTF-8";
    private static final String BASE_URI = "http://example.com";
    
    @Test
    public void testLoadFile() throws IOException {
        File testFile = new File("test.html");
        Document document = DataUtil.load(testFile, CHARSET_NAME, BASE_URI);
        
        Assert.assertNotNull(document);
    }
    
    @Test
    public void testLoadInputStream() throws IOException {
        InputStream testInputStream = new FileInputStream("test.html");
        Document document = DataUtil.load(testInputStream, CHARSET_NAME, BASE_URI);
        
        Assert.assertNotNull(document);
        
        testInputStream.close();
    }
    
    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        InputStream testInputStream = new FileInputStream("test.html");
        Parser parser = Parser.htmlParser();
        Document document = DataUtil.load(testInputStream, CHARSET_NAME, BASE_URI, parser);
        
        Assert.assertNotNull(document);
        
        testInputStream.close();
    }
    
    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.allocate(1024);
        String charsetName = CHARSET_NAME;
        String baseUri = BASE_URI;
        Parser parser = Parser.htmlParser();
        
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        
        Assert.assertNotNull(document);
    }
    
    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream testInputStream = new FileInputStream("test.html");
        ByteBuffer byteData = DataUtil.readToByteBuffer(testInputStream);
        
        Assert.assertNotNull(byteData);
        
        testInputStream.close();
    }
    
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        Assert.assertEquals("UTF-8", charset);
    }
}