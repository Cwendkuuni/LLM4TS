import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import java.io.File;
import java.io.IOException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, "UTF-8", "http://www.example.com");
        assertNotNull(doc);
        assertEquals(doc.baseUri(), "http://www.example.com");
    }
    
    @Test
    public void testLoadInputStream() throws IOException {
        // Create an InputStream with test data
        InputStream input = new ByteArrayInputStream("<html><body><p>Hello, World!</p></body></html>".getBytes());
        
        Document doc = DataUtil.load(input, "UTF-8", "http://www.example.com");
        assertNotNull(doc);
        assertEquals(doc.baseUri(), "http://www.example.com");
        assertEquals(doc.title(), "");
    }
    
    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        // Create an InputStream with test data
        InputStream input = new ByteArrayInputStream("<html><body><p>Hello, World!</p></body></html>".getBytes());
        
        Document doc = DataUtil.load(input, "UTF-8", "http://www.example.com", Parser.xmlParser());
        assertNotNull(doc);
        assertEquals(doc.baseUri(), "http://www.example.com");
        assertEquals(doc.title(), "");
    }
    
    @Test
    public void testCrossStreams() throws IOException {
        // Create an InputStream with test data
        InputStream input = new ByteArrayInputStream("test data".getBytes());
        
        // Create an OutputStream to write to
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        
        DataUtil.crossStreams(input, output);
        assertEquals("test data", output.toString());
    }
    
    @Test
    public void testParseInputStream() throws IOException {
        // Create an InputStream with test data
        InputStream input = new ByteArrayInputStream("<html><body><p>Hello, World!</p></body></html>".getBytes());
        
        Document doc = DataUtil.parseInputStream(input, "UTF-8", "http://www.example.com", Parser.htmlParser());
        assertNotNull(doc);
        assertEquals(doc.baseUri(), "http://www.example.com");
        assertEquals(doc.title(), "");
    }
    
    @Test
    public void testReadToByteBuffer() throws IOException {
        // Create an InputStream with test data
        InputStream input = new ByteArrayInputStream("test data".getBytes());
        
        ByteBuffer buffer = DataUtil.readToByteBuffer(input, 0);
        assertEquals("test data", new String(buffer.array(), buffer.position(), buffer.remaining()));
    }
    
    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File file = new File("test.html");
        ByteBuffer buffer = DataUtil.readFileToByteBuffer(file);
        assertNotNull(buffer);
        assertTrue(buffer.remaining() > 0);
    }
    
    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer buffer = DataUtil.emptyByteBuffer();
        assertNotNull(buffer);
        assertEquals(0, buffer.remaining());
    }
    
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNotNull(charset);
        assertEquals("UTF-8", charset);
    }
    
    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertNotNull(boundary);
        assertEquals(32, boundary.length());
    }
    
    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer byteData = ByteBuffer.wrap(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF, 0x41, 0x42, 0x43});
        DataUtil.BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteData);
        assertNotNull(bomCharset);
        assertEquals("UTF-8", bomCharset.charset);
        assertEquals(3, bomCharset.offset);
    }
}