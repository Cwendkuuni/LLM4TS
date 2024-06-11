import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.helper.DataUtil;

public class DataUtilTest {

    @Test
    public void testLoadFile() {
        File file = new File("test.html");
        try {
            Document doc = DataUtil.load(file, "UTF-8", "http://example.com");
            assertNotNull(doc);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }
    
    @Test
    public void testLoadInputStream() {
        String html = "<html><body><h1>Hello world!</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        try {
            Document doc = DataUtil.load(inputStream, "UTF-8", "http://example.com");
            assertNotNull(doc);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }
    
    @Test
    public void testLoadInputStreamWithParser() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><element>Test</element></root>";
        InputStream inputStream = new ByteArrayInputStream(xml.getBytes());
        try {
            Document doc = DataUtil.load(inputStream, "UTF-8", "http://example.com", Parser.xmlParser());
            assertNotNull(doc);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }
    
    @Test
    public void testCrossStreams() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String input = "Hello world!";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        try {
            DataUtil.crossStreams(inputStream, outputStream);
            assertEquals(input, outputStream.toString());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }
    
    @Test
    public void testParseInputStream() {
        String html = "<html><body><h1>Hello world!</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        try {
            Document doc = DataUtil.parseInputStream(inputStream, "UTF-8", "http://example.com", Parser.htmlParser());
            assertNotNull(doc);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }
    
    @Test
    public void testReadToByteBuffer() {
        String input = "Hello world!";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        try {
            ByteBuffer buffer = DataUtil.readToByteBuffer(inputStream, input.length());
            assertNotNull(buffer);
            assertEquals(input, new String(buffer.array()));
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }
    
    @Test
    public void testReadToByteBufferWithoutMaxSize() {
        String input = "Hello world!";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        try {
            ByteBuffer buffer = DataUtil.readToByteBuffer(inputStream);
            assertNotNull(buffer);
            assertEquals(input, new String(buffer.array()));
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }
    
    @Test
    public void testReadFileToByteBuffer() {
        File file = new File("test.txt");
        try {
            ByteBuffer buffer = DataUtil.readFileToByteBuffer(file);
            assertNotNull(buffer);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }
    
    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer buffer = DataUtil.emptyByteBuffer();
        assertNotNull(buffer);
        assertEquals(0, buffer.capacity());
    }
    
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html;charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNotNull(charset);
        assertEquals("UTF-8", charset);
    }
    
    @Test
    public void testGetCharsetFromInvalidContentType() {
        String contentType = "text/html;charset=invalid-charset";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }
    
    @Test
    public void testGetCharsetFromNullContentType() {
        String contentType = null;
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }
    
    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        String validatedCharset = DataUtil.validateCharset(charset);
        assertNotNull(validatedCharset);
        assertEquals(charset, validatedCharset);
    }
    
    @Test
    public void testValidateInvalidCharset() {
        String charset = "invalid-charset";
        String validatedCharset = DataUtil.validateCharset(charset);
        assertNull(validatedCharset);
    }
    
    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertNotNull(boundary);
        assertEquals(DataUtil.boundaryLength, boundary.length());
    }
    
    @Test
    public void testDetectCharsetFromBom() {
        byte[] bytes = { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF, 'H', 'e', 'l', 'l', 'o' };
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        DataUtil.BomCharset bomCharset = DataUtil.detectCharsetFromBom(buffer);
        assertNotNull(bomCharset);
        assertEquals("UTF-8", bomCharset.charset);
        assertTrue(bomCharset.offset);
    }
    
    @Test
    public void testDetectCharsetFromInvalidBom() {
        byte[] bytes = { 'H', 'e', 'l', 'l', 'o', '!' };
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        DataUtil.BomCharset bomCharset = DataUtil.detectCharsetFromBom(buffer);
        assertNull(bomCharset);
    }
}