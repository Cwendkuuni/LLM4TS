import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

public class DataUtilTest {
    
    private File testFile;
    
    @Before
    public void setUp() {
        testFile = new File("test.html");
    }
    
    @Test
    public void testLoadFile() throws IOException {
        Document doc = DataUtil.load(testFile, "UTF-8", "");
        assertNotNull(doc);
        assertEquals("test.html", doc.baseUri());
    }
    
    @Test
    public void testLoadInputStream() throws IOException {
        InputStream in = getClass().getResourceAsStream("test.html");
        Document doc = DataUtil.load(in, "UTF-8", "");
        assertNotNull(doc);
        assertEquals("", doc.baseUri());
    }
    
    @Test
    public void testLoadInputStreamWithParser() throws IOException { 
        InputStream in = getClass().getResourceAsStream("test.html");
        Document doc = DataUtil.load(in, "UTF-8", "", Parser.htmlParser());
        assertNotNull(doc);
        assertEquals("", doc.baseUri());
    }
    
    @Test
    public void testCrossStreams() throws IOException {
        InputStream in = getClass().getResourceAsStream("test.html");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataUtil.crossStreams(in, out);
        assertNotNull(out.toByteArray());
    }
    
    @Test
    public void testParseByteData() {
        // Test is not possible due to private access modifier
        // Implementation needs to be refactored to allow for testing
        // Will skip this test for now
    }
    
    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        InputStream in = getClass().getResourceAsStream("test.html");
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(in, 1024);
        assertNotNull(byteBuffer);
    }
    
    @Test
    public void testReadToByteBufferWithoutMaxSize() throws IOException {
        InputStream in = getClass().getResourceAsStream("test.html");
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(in);
        assertNotNull(byteBuffer);
    }
    
    @Test
    public void testReadFileToByteBuffer() throws IOException {
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(testFile);
        assertNotNull(byteBuffer);
    }
    
    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer empty = DataUtil.emptyByteBuffer();
        assertEquals(0, empty.capacity());
    }
    
    @Test
    public void testGetCharsetFromContentType() {
        String charset = DataUtil.getCharsetFromContentType("text/html; charset=UTF-8");
        assertEquals("UTF-8", charset);
        
        charset = DataUtil.getCharsetFromContentType("text/html");
        assertNull(charset);
    }
    
    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertNotNull(boundary);
        assertEquals(32, boundary.length());
    }
    
    @Test
    public void testDetectCharsetFromBom() {
        String charset = DataUtil.detectCharsetFromBom(ByteBuffer.wrap(new byte[]{(byte) 0xFE, (byte) 0xFF}), "UTF-8");
        assertEquals("UTF-16", charset);
        
        charset = DataUtil.detectCharsetFromBom(ByteBuffer.wrap(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF}), "UTF-8");
        assertEquals("UTF-8", charset);
        
        charset = DataUtil.detectCharsetFromBom(ByteBuffer.wrap(new byte[]{0x00, 0x00, (byte) 0xFE, (byte) 0xFF}), "UTF-8");
        assertEquals("UTF-32", charset);

        charset = DataUtil.detectCharsetFromBom(ByteBuffer.wrap(new byte[]{(byte) 0xFF, (byte) 0xFE, 0x00, 0x00}), "UTF-8");
        assertEquals("UTF-32", charset);
        
        charset = DataUtil.detectCharsetFromBom(ByteBuffer.wrap(new byte[]{0x00, 'a', 0x00, 0x00}), "UTF-8");
        assertEquals("UTF-8", charset);
    }
}