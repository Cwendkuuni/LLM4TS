import static org.junit.Assert.*;
import org.junit.Test;

public class DataUtilTest {
    
    @Test
    public void testLoadFileValid() {
        try {
            Document doc = DataUtil.load(new File("valid.html"), "UTF-8", "http://example.com");
            assertNotNull(doc);
        } catch(IOException e) {
            fail("IOException should not be thrown");
        }
    }
    
    @Test
    public void testLoadFileInvalid() {
        try {
            Document doc = DataUtil.load(new File("invalid.html"), "UTF-8", "http://example.com");
            fail("IOException should be thrown");
        } catch(IOException e) {
            assertEquals("Expected IOException", "file not found", e.getMessage());
        }
    }
    
    @Test
    public void testLoadInputStreamValid() {
        try {
            InputStream in = new FileInputStream("valid.html");
            Document doc = DataUtil.load(in, "UTF-8", "http://example.com");
            assertNotNull(doc);
            in.close();
        } catch(IOException e) {
            fail("IOException should not be thrown");
        }
    }
    
    @Test
    public void testLoadInputStreamInvalid() {
        try {
            InputStream in = new FileInputStream("invalid.html");
            Document doc = DataUtil.load(in, "UTF-8", "http://example.com");
            fail("IOException should be thrown");
        } catch(IOException e) {
            assertEquals("Expected IOException", "file not found", e.getMessage());
        }
    }
    
    @Test
    public void testLoadInputStreamParserValid() {
        try {
            InputStream in = new FileInputStream("valid.html");
            Document doc = DataUtil.load(in, "UTF-8", "http://example.com", Parser.htmlParser());
            assertNotNull(doc);
            in.close();
        } catch(IOException e) {
            fail("IOException should not be thrown");
        }
    }
    
    @Test
    public void testLoadInputStreamParserInvalid() {
        try {
            InputStream in = new FileInputStream("invalid.html");
            Document doc = DataUtil.load(in, "UTF-8", "http://example.com", Parser.htmlParser());
            fail("IOException should be thrown");
        } catch(IOException e) {
            assertEquals("Expected IOException", "file not found", e.getMessage());
        }
    }
    
    @Test
    public void testCrossStreamsValid() {
        try {
            InputStream in = new ByteArrayInputStream("test".getBytes());
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            DataUtil.crossStreams(in, out);
            assertEquals("test", out.toString());
            in.close();
            out.close();
        } catch(IOException e) {
            fail("IOException should not be thrown");
        }
    }
    
    @Test
    public void testCrossStreamsInvalid() {
        try {
            InputStream in = new FileInputStream("invalid.html");
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            DataUtil.crossStreams(in, out);
            fail("IOException should be thrown");
        } catch(IOException e) {
            assertEquals("Expected IOException", "file not found", e.getMessage());
        }
    }
    
    @Test
    public void testParseByteDataValid() {
        ByteBuffer byteData = ByteBuffer.wrap("test".getBytes());
        Document doc = DataUtil.parseByteData(byteData, "UTF-8", "http://example.com", Parser.htmlParser());
        assertNotNull(doc);
    }
    
    @Test
    public void testParseByteDataInvalid() {
        ByteBuffer byteData = ByteBuffer.wrap("".getBytes());
        Document doc = DataUtil.parseByteData(byteData, "UTF-8", "http://example.com", Parser.htmlParser());
        assertNull(doc);
    }
    
    @Test
    public void testReadToByteBufferValid() {
        try {
            InputStream in = new FileInputStream("valid.html");
            ByteBuffer byteData = DataUtil.readToByteBuffer(in);
            assertNotNull(byteData);
            in.close();
        } catch(IOException e) {
            fail("IOException should not be thrown");
        }
    }
    
    @Test
    public void testReadToByteBufferInvalid() {
        try {
            InputStream in = new FileInputStream("invalid.html");
            ByteBuffer byteData = DataUtil.readToByteBuffer(in);
            fail("IOException should be thrown");
        } catch(IOException e) {
            assertEquals("Expected IOException", "file not found", e.getMessage());
        }
    }
    
    @Test
    public void testReadToByteBufferWithMaxSizeValid() {
        try {
            InputStream in = new FileInputStream("valid.html");
            ByteBuffer byteData = DataUtil.readToByteBuffer(in, 100000);
            assertNotNull(byteData);
            in.close();
        } catch(IOException e) {
            fail("IOException should not be thrown");
        }
    }
    
    @Test
    public void testReadToByteBufferWithMaxSizeInvalid() {
        try {
            InputStream in = new FileInputStream("invalid.html");
            ByteBuffer byteData = DataUtil.readToByteBuffer(in, 100000);
            fail("IOException should be thrown");
        } catch(IOException e) {
            assertEquals("Expected IOException", "file not found", e.getMessage());
        }
    }
    
    @Test
    public void testReadFileToByteBufferValid() {
        try {
            ByteBuffer byteData = DataUtil.readFileToByteBuffer(new File("valid.html"));
            assertNotNull(byteData);
        } catch(IOException e) {
            fail("IOException should not be thrown");
        }
    }
    
    @Test
    public void testReadFileToByteBufferInvalid() {
        try {
            ByteBuffer byteData = DataUtil.readFileToByteBuffer(new File("invalid.html"));
            fail("IOException should be thrown");
        } catch(IOException e) {
            assertEquals("Expected IOException", "file not found", e.getMessage());
        }
    }
    
    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteData = DataUtil.emptyByteBuffer();
        assertNotNull(byteData);
        assertEquals(0, byteData.remaining());
    }
    
    @Test
    public void testGetCharsetFromContentTypeValid() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }
    
    @Test
    public void testGetCharsetFromContentTypeInvalid() {
        String contentType = "text/html; charset=INVALID";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }
    
    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertNotNull(boundary);
        assertEquals(32, boundary.length());
    }
    
}