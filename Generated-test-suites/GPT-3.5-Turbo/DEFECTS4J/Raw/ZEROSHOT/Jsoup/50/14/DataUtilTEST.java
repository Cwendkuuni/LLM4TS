import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Pattern;

public class DataUtilTest {
    
    // Test load(File in, String charsetName, String baseUri) method
    @Test
    public void testLoadFile() {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        try {
            Document document = DataUtil.load(file, charsetName, baseUri);
            assertNotNull(document);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }
    
    // Test load(InputStream in, String charsetName, String baseUri) method
    @Test
    public void testLoadInputStream() {
        InputStream inputStream = new ByteArrayInputStream("test html".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        try {
            Document document = DataUtil.load(inputStream, charsetName, baseUri);
            assertNotNull(document);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }
    
    // Test load(InputStream in, String charsetName, String baseUri, Parser parser) method
    @Test
    public void testLoadInputStreamWithParser() {
        InputStream inputStream = new ByteArrayInputStream("test html".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        try {
            Document document = DataUtil.load(inputStream, charsetName, baseUri, Parser.htmlParser());
            assertNotNull(document);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }
    
    // Test crossStreams(InputStream in, OutputStream out) method
    @Test
    public void testCrossStreams() {
        InputStream inputStream = new ByteArrayInputStream("input stream".getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        try {
            DataUtil.crossStreams(inputStream, outputStream);
            assertEquals("input stream", outputStream.toString());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }
    
    // Test parseByteData(ByteBuffer byteData, String charsetName, String baseUri, Parser parser) method
    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.wrap("byte data".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        assertNotNull(document);
    }
    
    // Test readToByteBuffer(InputStream inStream, int maxSize) method
    @Test
    public void testReadToByteBuffer() {
        InputStream inputStream = new ByteArrayInputStream("input stream".getBytes());
        
        try {
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 0);
            assertNotNull(byteBuffer);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }
    
    // Test readToByteBuffer(InputStream inStream) method
    @Test
    public void testReadToByteBufferWithoutMaxSize() {
        InputStream inputStream = new ByteArrayInputStream("input stream".getBytes());
        
        try {
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
            assertNotNull(byteBuffer);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }
    
    // Test readFileToByteBuffer(File file) method
    @Test
    public void testReadFileToByteBuffer() {
        File file = new File("test.txt");
        
        try {
            ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
            assertNotNull(byteBuffer);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }
    
    // Test emptyByteBuffer() method
    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        assertNotNull(byteBuffer);
        assertEquals(0, byteBuffer.remaining());
    }
    
    // Test getCharsetFromContentType(String contentType) method
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNotNull(charset);
        assertEquals("UTF-8", charset);
    }
    
    // Test mimeBoundary() method
    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        assertNotNull(mimeBoundary);
        assertEquals(DataUtil.boundaryLength, mimeBoundary.length());
    }
}