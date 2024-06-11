import org.junit.Test;
import static org.junit.Assert.*;

public class DataUtilTest {
    private static final String FILE_PATH = "path/to/file";
    
    @Test
    public void testLoadFile() {
        try {
            Document document = DataUtil.load(new File(FILE_PATH), "UTF-8", "https://www.example.com");
            assertNotNull(document);
        } catch (IOException e) {
            fail("Failed to load file");
        }
    }
    
    @Test
    public void testLoadInputStream() {
        try {
            FileInputStream fileInputStream = new FileInputStream(FILE_PATH);
            Document document = DataUtil.load(fileInputStream, "UTF-8", "https://www.example.com");
            assertNotNull(document);
        } catch (IOException e) {
            fail("Failed to load input stream");
        }
    }
    
    @Test
    public void testLoadInputStreamWithParser() {
        try {
            FileInputStream fileInputStream = new FileInputStream(FILE_PATH);
            Document document = DataUtil.load(fileInputStream, "UTF-8", "https://www.example.com", Parser.htmlParser());
            assertNotNull(document);
        } catch (IOException e) {
            fail("Failed to load input stream with parser");
        }
    }
    
    @Test
    public void testCrossStreams() {
        try {
            InputStream input = new FileInputStream(FILE_PATH);
            OutputStream output = new ByteArrayOutputStream();
            DataUtil.crossStreams(input, output);
            
            assertTrue(output.size() > 0);
        } catch (IOException e) {
            fail("Failed to cross streams");
        }
    }
    
    @Test
    public void testParseInputStream() {
        try {
            FileInputStream fileInputStream = new FileInputStream(FILE_PATH);
            Document document = DataUtil.parseInputStream(fileInputStream, "UTF-8", "https://www.example.com", Parser.htmlParser());
            assertNotNull(document);
        } catch (IOException e) {
            fail("Failed to parse input stream");
        }
    }
    
    @Test
    public void testReadToByteBuffer() {
        try {
            FileInputStream fileInputStream = new FileInputStream(FILE_PATH);
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(fileInputStream, 1024);
            
            assertNotNull(byteBuffer);
            assertTrue(byteBuffer.limit() > 0);
        } catch (IOException e) {
            fail("Failed to read to byte buffer");
        }
    }
    
    @Test
    public void testReadToByteBufferWithoutMaxSize() {
        try {
            FileInputStream fileInputStream = new FileInputStream(FILE_PATH);
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(fileInputStream);
            
            assertNotNull(byteBuffer);
            assertTrue(byteBuffer.limit() > 0);
        } catch (IOException e) {
            fail("Failed to read to byte buffer without max size");
        }
    }
    
    @Test
    public void testReadFileToByteBuffer() {
        try {
            File file = new File(FILE_PATH);
            ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
            
            assertNotNull(byteBuffer);
            assertTrue(byteBuffer.limit() > 0);
        } catch (IOException e) {
            fail("Failed to read file to byte buffer");
        }
    }
    
    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        
        assertNotNull(byteBuffer);
        assertEquals(0, byteBuffer.capacity());
        assertEquals(0, byteBuffer.limit());
    }
    
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        assertEquals("UTF-8", charset);
    }
    
    @Test
    public void testMimeBoundary() {
        String mimeType = DataUtil.mimeBoundary();
        
        assertNotNull(mimeType);
        assertEquals(DataUtil.boundaryLength, mimeType.length());
    }
    
    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        String validatedCharset = DataUtil.validateCharset(charset);
        
        assertEquals(charset, validatedCharset);
    }
}