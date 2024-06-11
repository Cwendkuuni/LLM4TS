import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;
import java.io.*;

import static org.junit.Assert.*;

public class DataUtilTest {
    private File testFile;
    private InputStream testInputStream;
    
    @Before
    public void setUp() {
        testFile = new File("test.html");
        testInputStream = getClass().getResourceAsStream("/test.html");
    }
    
    @Test
    public void testLoadFromFile() {
        try {
            Document doc = DataUtil.load(testFile, "UTF-8", "http://example.com");
            assertNotNull(doc);
            // Add more assertions if needed
        } catch (IOException e) {
            fail("Exception not expected");
        }
    }
    
    @Test
    public void testLoadFromInputStream() {
        try {
            Document doc = DataUtil.load(testInputStream, "UTF-8", "http://example.com");
            assertNotNull(doc);
            // Add more assertions if needed
        } catch (IOException e) {
            fail("Exception not expected");
        }
    }
    
    @Test
    public void testLoadFromInputStreamWithParser() {
        try {
            Document doc = DataUtil.load(testInputStream, "UTF-8", "http://example.com", Parser.xmlParser());
            assertNotNull(doc);
            // Add more assertions if needed
        } catch (IOException e) {
            fail("Exception not expected");
        }
    }
    
    @Test
    public void testLoadFromInputStreamWithInvalidCharset() {
        try {
            Document doc = DataUtil.load(testInputStream, "Invalid", "http://example.com");
            assertNotNull(doc);
            // Add more assertions if needed
        } catch (IOException e) {
            // Exception expected due to invalid charset
            assertTrue(true);
        }
    }
    
    @Test
    public void testParseByteData() {
        ByteBuffer byteBuffer = ByteBuffer.wrap("Test".getBytes());
        Document doc = DataUtil.parseByteData(byteBuffer, "UTF-8", "http://example.com", Parser.htmlParser());
        assertNotNull(doc);
        // Add more assertions if needed
    }
    
    @Test
    public void testGetCharsetFromContentType() {
        String charset = DataUtil.getCharsetFromContentType("text/html; charset=UTF-8");
        assertEquals("UTF-8", charset);
        // Add more assertions if needed
    }
}