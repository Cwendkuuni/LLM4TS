package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

public class DataUtilTest {
    @Test
    public void testLoad() throws IOException {
        File testFile = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        
        Document expected = new Document(baseUri);
        Document actual = DataUtil.load(testFile, charsetName, baseUri);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testLoadWithInputStream() throws IOException {
        InputStream in = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        
        Document expected = new Document(baseUri);
        Document actual = DataUtil.load(in, charsetName, baseUri);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testLoadWithInputStreamAndParser() throws IOException {
        InputStream in = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Parser parser = Parser.htmlParser();
        
        Document expected = new Document(baseUri);
        Document actual = DataUtil.load(in, charsetName, baseUri, parser);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testParseByteDataWithCharset() {
        ByteBuffer byteData = ByteBuffer.wrap("test".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Parser parser = Parser.htmlParser();
        
        Document expected = new Document(baseUri);
        Document actual = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testParseByteDataWithoutCharset() {
        ByteBuffer byteData = ByteBuffer.wrap("test".getBytes(charsetName));
        String baseUri = "http://www.example.com";
        Parser parser = Parser.htmlParser();
        
        Document expected = new Document(baseUri);
        Document actual = DataUtil.parseByteData(byteData, null, baseUri, parser);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        
        String expected = "UTF-8";
        String actual = DataUtil.getCharsetFromContentType(contentType);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetCharsetFromContentTypeNotFound() {
        String contentType = "text/html";
        
        String expected = null;
        String actual = DataUtil.getCharsetFromContentType(contentType);
        
        assertEquals(expected, actual);
    }
}