import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.IllegalCharsetNameException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {
    
    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(file, "UTF-8", baseUri);
        
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
    }
    
    @Test
    public void testLoadInputStream() throws IOException {
        InputStream in = new FileInputStream("test.html");
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(in, "UTF-8", baseUri);
        
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
    }
    
    @Test
    public void testLoadCustomParser() throws IOException {
        InputStream in = new FileInputStream("test.html");
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.load(in, "UTF-8", baseUri, parser);
        
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
    }
    
    @Test
    public void testCrossStreams() throws IOException {
        InputStream in = new FileInputStream("input.txt");
        OutputStream out = new FileOutputStream("output.txt");
        
        DataUtil.crossStreams(in, out);
        
        // add assertion here if needed
    }
    
    @Test
    public void testParseInputStream() throws IOException {
        InputStream in = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        
        Document doc = DataUtil.parseInputStream(in, charsetName, baseUri, parser);
        
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
    }
    
    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream in = new FileInputStream("test.html");
        int maxSize = 1024;
        
        ByteBuffer buffer = DataUtil.readToByteBuffer(in, maxSize);
        
        assertNotNull(buffer);
        // add assertion here if needed
    }
    
    @Test
    public void testReadToByteBufferUnlimited() throws IOException {
        InputStream in = new FileInputStream("test.html");
        
        ByteBuffer buffer = DataUtil.readToByteBuffer(in);
        
        assertNotNull(buffer);
        // add assertion here if needed
    }
    
    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File file = new File("test.html");
        
        ByteBuffer buffer = DataUtil.readFileToByteBuffer(file);
        
        assertNotNull(buffer);
        // add assertion here if needed
    }
    
    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        
        assertNotNull(boundary);
        // add assertion here if needed
    }
    
    @Test
    public void testGetCharsetFromContentType() {
        String contentType1 = "text/html; charset=UTF-8";
        String contentType2 = "text/html";
        
        String charset1 = DataUtil.getCharsetFromContentType(contentType1);
        String charset2 = DataUtil.getCharsetFromContentType(contentType2);
        
        assertEquals("UTF-8", charset1);
        assertEquals(null, charset2);
    }
    
    @Test
    public void testValidateCharset() {
        String charset1 = "UTF-8";
        String charset2 = "INVALID";
        
        String validatedCharset1 = DataUtil.validateCharset(charset1);
        String validatedCharset2 = DataUtil.validateCharset(charset2);
        
        assertEquals("UTF-8", validatedCharset1);
        assertEquals(null, validatedCharset2);
    }
}