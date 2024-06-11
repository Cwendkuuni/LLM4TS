import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class DataUtilTest {
    
    @Test
    public void testLoadFromFile() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, "UTF-8", "http://example.com");
        
        Assert.assertNotNull(doc);
        Assert.assertEquals("http://example.com", doc.baseUri());
    }
    
    @Test
    public void testLoadFromInputStream() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        Document doc = DataUtil.load(inputStream, "UTF-8", "http://example.com");
        
        Assert.assertNotNull(doc);
        Assert.assertEquals("http://example.com", doc.baseUri());
        
        inputStream.close();
    }
    
    @Test
    public void testLoadFromInputStreamWithParser() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        Document doc = DataUtil.load(inputStream, "UTF-8", "http://example.com", Parser.htmlParser());
        
        Assert.assertNotNull(doc);
        Assert.assertEquals("http://example.com", doc.baseUri());
        
        inputStream.close();
    }
    
    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.wrap("<html><head></head><body><h1>Hello world</h1></body></html>".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        
        Assert.assertNotNull(doc);
        Assert.assertEquals("http://example.com", doc.baseUri());
        Assert.assertEquals("<html>\n <head></head>\n <body>\n  <h1>\n   Hello world\n  </h1>\n </body>\n</html>\n", doc.outerHtml());
    }
    
    @Test
    public void testReadToByteBuffer() throws IOException {
        File file = new File("test.html");
        FileInputStream inStream = new FileInputStream(file);
        
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inStream);
        
        Assert.assertNotNull(byteBuffer);
        Assert.assertEquals(file.length(), byteBuffer.limit());
        
        inStream.close();
    }
    
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        Assert.assertEquals("UTF-8", charset);
    }
}