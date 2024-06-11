
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.File;
import java.io.InputStream;
import java.io.IOException;

import static org.junit.Assert.*;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        
        Document document = DataUtil.load(file, charsetName, baseUri);
        
        assertNotNull(document);
    }
    
    @Test
    public void testLoadFileNotFound() throws IOException {
        File file = new File("nonexistent.html");
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        
        try {
            Document document = DataUtil.load(file, charsetName, baseUri);
            
            fail("Should have thrown an IOException");
        } catch (IOException e) {
            // Pass
        }
    }
    
    @Test
    public void testLoadFileInvalidCharset() throws IOException {
        File file = new File("test.html");
        String charsetName = "INVALID-CHARSET";
        String baseUri = "https://www.example.com";
        
        try {
            Document document = DataUtil.load(file, charsetName, baseUri);
            
            fail("Should have thrown an IOException");
        } catch (IOException e) {
            // Pass
        }
    }
    
    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        
        assertNotNull(document);
    }
    
    @Test
    public void testLoadInputStreamNullCharset() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        String charsetName = null;
        String baseUri = "https://www.example.com";
        
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        
        assertNotNull(document);
    }
    
    @Test
    public void testLoadInputStreamInvalidBaseUri() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = null;
        
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        
        assertNotNull(document);
    }
    
    @Test
    public void testLoadInputStreamNullInputStream() throws IOException {
        InputStream inputStream = null;
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        
        try {
            Document document = DataUtil.load(inputStream, charsetName, baseUri);
            
            fail("Should have thrown an IOException");
        } catch (IOException e) {
            // Pass
        }
    }
    
    @Test
    public void testLoadInputStreamInvalidCharset() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        String charsetName = "INVALID-CHARSET";
        String baseUri = "https://www.example.com";
        
        try {
            Document document = DataUtil.load(inputStream, charsetName, baseUri);
            
            fail("Should have thrown an IOException");
        } catch (IOException e) {
            // Pass
        }
    }
    
    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        Parser parser = Parser.htmlParser();
        
        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
        
        assertNotNull(document);
    }
    
    @Test
    public void testLoadInputStreamWithParserNullCharset() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        String charsetName = null;
        String baseUri = "https://www.example.com";
        Parser parser = Parser.htmlParser();
        
        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
        
        assertNotNull(document);
    }
    
    @Test
    public void testLoadInputStreamWithParserInvalidBaseUri() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = null;
        Parser parser = Parser.htmlParser();
        
        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
        
        assertNotNull(document);
    }
    
    @Test
    public void testLoadInputStreamWithParserNullInputStream() throws IOException {
        InputStream inputStream = null;
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        Parser parser = Parser.htmlParser();
        
        try {
            Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
            
            fail("Should have thrown an IOException");
        } catch (IOException e) {
            // Pass
        }
    }
    
    @Test
    public void testLoadInputStreamWithParserInvalidCharset() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        String charsetName = "INVALID-CHARSET";
        String baseUri = "https://www.example.com";
        Parser parser = Parser.htmlParser();
        
        try {
            Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
            
            fail("Should have thrown an IOException");
        } catch (IOException e) {
            // Pass
        }
    }
    
    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.allocate(100);
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        Parser parser = Parser.htmlParser();
        
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        
        assertNotNull(document);
    }
    
    @Test
    public void testParseByteDataNullCharset() {
        ByteBuffer byteData = ByteBuffer.allocate(100);
        String charsetName = null;
        String baseUri = "https://www.example.com";
        Parser parser = Parser.htmlParser();
        
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        
        assertNotNull(document);
    }
    
    @Test
    public void testParseByteDataNullByteData() {
        ByteBuffer byteData = null;
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        Parser parser = Parser.htmlParser();
        
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        
        assertNotNull(document);
    }
    
    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        
        assertNotNull(byteData);
    }
    
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        assertEquals("UTF-8", charset);
    }
    
    @Test
    public void testGetCharsetFromContentTypeNullContentType() {
        String contentType = null;
        
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        assertNull(charset);
    }
    
    @Test
    public void testGetCharsetFromContentTypeInvalidContentType() {
        String contentType = "text/html";
        
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        assertNull(charset);
    }
}
