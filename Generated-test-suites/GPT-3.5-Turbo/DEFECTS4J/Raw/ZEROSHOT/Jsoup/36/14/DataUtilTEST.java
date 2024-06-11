import org.junit.Test;

import static org.junit.Assert.*;
import java.io.*;
import java.nio.ByteBuffer;

public class DataUtilTest {

    @Test
    public void testLoad_FileWithCharsetAndBaseUri() throws IOException {
        File inputFile = new File("input.txt");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(inputFile, charsetName, baseUri);
        
        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testLoad_InputSteamWithCharsetAndBaseUri() throws IOException {
        FileInputStream inputStream = new FileInputStream("input.txt");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        
        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
    }
    
    @Test
    public void testLoad_InputSteamWithCharsetAndBaseUriAndParser() throws IOException {
        FileInputStream inputStream = new FileInputStream("input.txt");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = new Parser();
        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
        
        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testParseByteData_DefaultCharset() {
        String charsetName = null;
        String baseUri = "http://example.com";
        Parser parser = new Parser();
        
        ByteBuffer byteData = ByteBuffer.allocate(1024);
        byteData.put("Hello, World!".getBytes());
        byteData.flip();
        
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        
        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testParseByteData_SpecifiedCharset() {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = new Parser();
        
        ByteBuffer byteData = ByteBuffer.allocate(1024);
        byteData.put("Hello, World!".getBytes());
        byteData.flip();
        
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        
        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testReadToByteBuffer_UnlimitedMaxSize() throws IOException {
        FileInputStream inputStream = new FileInputStream("input.txt");
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, 0);
        
        assertNotNull(byteData);
        assertTrue(byteData.limit() > 0);
    }

    @Test
    public void testReadToByteBuffer_LimitedMaxSize() throws IOException {
        FileInputStream inputStream = new FileInputStream("input.txt");
        int maxSize = 1024;
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, maxSize);
        
        assertNotNull(byteData);
        assertTrue(byteData.limit() <= maxSize);
    }

    @Test
    public void testReadToByteBuffer_NoMaxSize() throws IOException {
        FileInputStream inputStream = new FileInputStream("input.txt");
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        
        assertNotNull(byteData);
        assertTrue(byteData.limit() > 0);
    }

    @Test
    public void testGetCharsetFromContentType_ValidContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        assertNotNull(charset);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentType_InvalidContentType() {
        String contentType = "text/html; charset=InvalidCharset";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        assertNull(charset);
    }
}