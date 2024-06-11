import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import java.io.*;

public class DataUtilTest {
    
    @Test
    public void testLoad() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, "UTF-8", "");
        String expected = "<html><head></head><body></body></html>";
        assertEquals(expected, doc.html());
    }
    
    @Test
    public void testLoadCharsetNull() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, null, "");
        String expected = "<html><head></head><body></body></html>";
        assertEquals(expected, doc.html());
    }
    
    @Test
    public void testLoadInputStream() throws IOException {
        FileInputStream inStream = new FileInputStream("test.html");
        Document doc = DataUtil.load(inStream, "UTF-8", "");
        String expected = "<html><head></head><body></body></html>";
        assertEquals(expected, doc.html());
        inStream.close();
    }
    
    @Test
    public void testLoadInputStreamCharsetNull() throws IOException {
        FileInputStream inStream = new FileInputStream("test.html");
        Document doc = DataUtil.load(inStream, null, "");
        String expected = "<html><head></head><body></body></html>";
        assertEquals(expected, doc.html());
        inStream.close();
    }
    
    @Test
    public void testLoadInputStreamParser() throws IOException {
        FileInputStream inStream = new FileInputStream("test.html");
        Document doc = DataUtil.load(inStream, "UTF-8", "", Parser.htmlParser());
        String expected = "<html><head></head><body></body></html>";
        assertEquals(expected, doc.html());
        inStream.close();
    }
    
    @Test
    public void testLoadCharsetNameNotNull() throws IOException {
        FileInputStream inStream = new FileInputStream("test.html");
        Document doc = DataUtil.load(inStream, "ISO-8859-1", "");
        String expected = "<html><head></head><body></body></html>";
        assertEquals(expected, doc.html());
        inStream.close();
    }
    
    @Test
    public void testParseByteData() {
        // Write test cases to test parseByteData method
    }
    
    @Test
    public void testReadToByteBuffer() throws IOException {
        FileInputStream inStream = new FileInputStream("test.html");
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inStream, 1024);
        assertEquals(1024, byteBuffer.capacity());
        inStream.close();
    }
    
    @Test
    public void testReadToByteBufferUnlimitedSize() throws IOException {
        FileInputStream inStream = new FileInputStream("test.html");
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inStream, 0);
        assertEquals(130560, byteBuffer.capacity());
        inStream.close();
    }
    
    @Test
    public void testReadToByteBufferInputStream() throws IOException {
        FileInputStream inStream = new FileInputStream("test.html");
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inStream);
        assertEquals(130560, byteBuffer.capacity());
        inStream.close();
    }
    
    @Test
    public void testGetCharsetFromContentType() {
        // Write test cases to test getCharsetFromContentType method
    }
}