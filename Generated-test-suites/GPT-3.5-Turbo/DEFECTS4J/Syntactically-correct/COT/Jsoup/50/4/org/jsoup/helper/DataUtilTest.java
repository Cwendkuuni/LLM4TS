
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.helper.DataUtil;

public class DataUtilTest {
    
    @Test
    public void testLoadFile() {
        File file = new File("test.html");
        String charsetName = "utf-8";
        String baseUri = "https://example.com";
        try {
            Document doc = DataUtil.load(file, charsetName, baseUri);
            assertNotNull(doc);
            // Add more assertions if needed
        } catch (IOException e) {
            fail("IOException occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testLoadInputStream() {
        InputStream is = new FileInputStream("test.html");
        String charsetName = "utf-8";
        String baseUri = "https://example.com";
        try {
            Document doc = DataUtil.load(is, charsetName, baseUri);
            assertNotNull(doc);
            // Add more assertions if needed
        } catch (IOException e) {
            fail("IOException occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testLoadInputStreamWithParser() {
        InputStream is = new FileInputStream("test.html");
        String charsetName = "utf-8";
        String baseUri = "https://example.com";
        Parser parser = Parser.htmlParser();
        try {
            Document doc = DataUtil.load(is, charsetName, baseUri, parser);
            assertNotNull(doc);
            // Add more assertions if needed
        } catch (IOException e) {
            fail("IOException occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testCrossStreams() {
        InputStream in = new ByteArrayInputStream("Hello, World!".getBytes());
        OutputStream out = new ByteArrayOutputStream();
        try {
            DataUtil.crossStreams(in, out);
            assertEquals("Hello, World!", out.toString());
        } catch (IOException e) {
            fail("IOException occurred: " + e.getMessage());
        }
    }

    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.wrap("Hello, World!".getBytes());
        String charsetName = "utf-8";
        String baseUri = "https://example.com";
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(doc);
        // Add assertions if needed
    }
    
    @Test
    public void testReadToByteBuffer() {
        InputStream is = new FileInputStream("test.html");
        try {
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(is);
            assertNotNull(byteBuffer);
            // Add assertions if needed
        } catch (IOException e) {
            fail("IOException occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testReadToByteBufferWithMaxSize() {
        InputStream is = new FileInputStream("test.html");
        int maxSize = 1024;
        try {
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(is, maxSize);
            assertNotNull(byteBuffer);
            // Add assertions if needed
        } catch (IOException e) {
            fail("IOException occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testReadFileToByteBuffer() {
        File file = new File("test.html");
        try {
            ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
            assertNotNull(byteBuffer);
            // Add assertions if needed
        } catch (IOException e) {
            fail("IOException occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        assertNotNull(byteBuffer);
        assertEquals(0, byteBuffer.remaining());
    }
    
    @Test
    public void testGetCharsetFromContentType() {
        String contentType1 = "text/html; charset=utf-8";
        String charset1 = DataUtil.getCharsetFromContentType(contentType1);
        assertEquals("utf-8", charset1);
        
        String contentType2 = "text/html; charset=EUC-JP";
        String charset2 = DataUtil.getCharsetFromContentType(contentType2);
        assertEquals("EUC-JP", charset2);
        
        String contentType3 = "text/html";
        String charset3 = DataUtil.getCharsetFromContentType(contentType3);
        assertNull(charset3);
    }
    
    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertNotNull(boundary);
        assertEquals(DataUtil.boundaryLength, boundary.length());
        // Additional assertions if needed
    }
}
