import org.junit.Test;
import static org.junit.Assert.*;

public class DataUtilTest {
    
    @Test
    public void testLoadFromFile() throws IOException {
        File input = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com/";
        
        Document doc = DataUtil.load(input, charsetName, baseUri);
        
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
    }
    
    @Test
    public void testLoadFromInputStream() throws IOException {
        InputStream input = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com/";
        
        Document doc = DataUtil.load(input, charsetName, baseUri);
        
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
    }
    
    @Test
    public void testLoadFromInputStreamWithParser() throws IOException {
        InputStream input = getClass().getResourceAsStream("test.xml");
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com/";
        Parser parser = Parser.xmlParser();
        
        Document doc = DataUtil.load(input, charsetName, baseUri, parser);
        
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
    }
    
    @Test
    public void testCrossStreams() throws IOException {
        byte[] inputBytes = "This is a test.".getBytes();
        ByteArrayInputStream in = new ByteArrayInputStream(inputBytes);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        DataUtil.crossStreams(in, out);
        
        byte[] outputBytes = out.toByteArray();
        
        assertArrayEquals(inputBytes, outputBytes);
    }
    
    @Test
    public void testParseInputStream() throws IOException {
        InputStream input = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com/";
        Parser parser = Parser.htmlParser();
        
        Document doc = DataUtil.parseInputStream(input, charsetName, baseUri, parser);
        
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
    }
    
    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream input = getClass().getResourceAsStream("test.html");
        int maxSize = 1024 * 5;
        
        ByteBuffer buffer = DataUtil.readToByteBuffer(input, maxSize);
        
        assertNotNull(buffer);
        assertEquals(maxSize, buffer.limit());
    }
    
    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        
        assertNotNull(boundary);
        assertEquals(32, boundary.length());
    }
    
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        assertNotNull(charset);
        assertEquals("UTF-8", charset);
    }
    
    @Test
    public void testValidateCharset() {
        String validCharset = "UTF-8";
        String invalidCharset = "InvalidCharset";
        
        String validatedCharset1 = DataUtil.validateCharset(validCharset);
        String validatedCharset2 = DataUtil.validateCharset(invalidCharset);
        
        assertNotNull(validatedCharset1);
        assertEquals(validCharset, validatedCharset1);
        assertNull(validatedCharset2);
    }
    
    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer byteDataLE = ByteBuffer.wrap(new byte[] { -17, -69, -65 }); // UTF-8 BOM
        ByteBuffer byteDataBE = ByteBuffer.wrap(new byte[] { -2, -1 }); // UTF-16 BE BOM
        
        BomCharset bomCharsetLE = DataUtil.detectCharsetFromBom(byteDataLE);
        BomCharset bomCharsetBE = DataUtil.detectCharsetFromBom(byteDataBE);
        
        assertNotNull(bomCharsetLE);
        assertEquals("UTF-8", bomCharsetLE.charset);
        assertFalse(bomCharsetLE.offset);
        
        assertNotNull(bomCharsetBE);
        assertEquals("UTF-16", bomCharsetBE.charset);
        assertFalse(bomCharsetBE.offset);
    }
}