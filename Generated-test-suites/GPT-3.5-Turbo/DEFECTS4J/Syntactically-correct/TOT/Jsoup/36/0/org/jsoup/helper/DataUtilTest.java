import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Locale;

public class DataUtilTest {
    
    // Test load(File in, String charsetName, String baseUri)
    @Test
    public void testLoadFromFile() throws Exception {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        Document document = DataUtil.load(file, charsetName, baseUri);
        
        // Assert that the document is not null
        Assert.assertNotNull(document);
        
        // Assert that the document has correct base URI
        Assert.assertEquals(baseUri, document.baseUri());
    }
    
    // Test load(InputStream in, String charsetName, String baseUri)
    @Test
    public void testLoadFromInputStream() throws Exception {
        FileInputStream inputStream = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        
        // Assert that the document is not null
        Assert.assertNotNull(document);
        
        // Assert that the document has correct base URI
        Assert.assertEquals(baseUri, document.baseUri());
        
        // Close the input stream
        inputStream.close();
    }
    
    // Test load(InputStream in, String charsetName, String baseUri, Parser parser)
    @Test
    public void testLoadFromInputStreamWithParser() throws Exception {
        FileInputStream inputStream = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        
        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
        
        // Assert that the document is not null
        Assert.assertNotNull(document);
        
        // Assert that the document has correct base URI
        Assert.assertEquals(baseUri, document.baseUri());
        
        // Close the input stream
        inputStream.close();
    }
    
    // Test parseByteData(ByteBuffer byteData, String charsetName, String baseUri, Parser parser)
    @Test
    public void testParseByteData() throws Exception {
        // Create a valid byte buffer with HTML data
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        ByteBuffer byteData = Charset.forName("UTF-8").encode(html);
        
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        
        // Assert that the document is not null
        Assert.assertNotNull(document);
        
        // Assert that the document has correct base URI
        Assert.assertEquals(baseUri, document.baseUri());
    }
    
    // Test readToByteBuffer(InputStream inStream, int maxSize)
    @Test
    public void testReadToByteBufferWithMaxSize() throws Exception {
        InputStream inputStream = new FileInputStream("test.html");
        
        // Set max size to 10 bytes
        int maxSize = 10;
        
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, maxSize);
        
        // Assert that the byte buffer is not null
        Assert.assertNotNull(byteData);
        
        // Assert that the byte buffer size is equal to the specified max size
        Assert.assertEquals(maxSize, byteData.remaining());
        
        // Close the input stream
        inputStream.close();
    }
    
    // Test readToByteBuffer(InputStream inStream)
    @Test
    public void testReadToByteBufferWithoutMaxSize() throws Exception {
        InputStream inputStream = new FileInputStream("test.html");
        
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        
        // Assert that the byte buffer is not null
        Assert.assertNotNull(byteData);
        
        // Close the input stream
        inputStream.close();
    }
    
    // Test getCharsetFromContentType(String contentType)
    @Test
    public void testGetCharsetFromContentType() throws Exception {
        String contentType = "text/html; charset=UTF-8";
        
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        // Assert that the charset is not null
        Assert.assertNotNull(charset);
        
        // Assert that the charset is equal to the specified content type
        Assert.assertEquals("UTF-8", charset);
    }
}