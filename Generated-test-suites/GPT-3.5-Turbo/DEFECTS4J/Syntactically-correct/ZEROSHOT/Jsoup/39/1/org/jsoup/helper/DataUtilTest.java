import static org.junit.Assert.*;
import org.junit.Test;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.BeforeClass;

public class DataUtilTest {
    private static final File testFile = new File("test.html");
    
    private static final String defaultCharset = "UTF-8";
    
    @BeforeClass
    public static void setUp() {
        // Create a test file
        try {
            FileWriter writer = new FileWriter(testFile);
            writer.write("<html></html>");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testLoadFile() {
        try {
            Document doc = DataUtil.load(testFile, "UTF-8", "");
            assertEquals("UTF-8", doc.outputSettings().charset().displayName());
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testLoadInputStream() {
        try {
            FileInputStream fileStream = new FileInputStream(testFile);
            Document doc = DataUtil.load(fileStream, "UTF-8", "");
            fileStream.close();
            assertEquals("UTF-8", doc.outputSettings().charset().displayName());
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testLoadInputStreamWithParser() {
        try {
            FileInputStream fileStream = new FileInputStream(testFile);
            Document doc = DataUtil.load(fileStream, "UTF-8", "", Parser.htmlParser());
            fileStream.close();
            assertEquals("UTF-8", doc.outputSettings().charset().displayName());
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.wrap("<html></html>".getBytes());
        Document doc = DataUtil.parseByteData(byteData, "UTF-8", "", Parser.htmlParser());
        assertEquals("UTF-8", doc.outputSettings().charset().displayName());
    }
    
    @Test
    public void testReadToByteBufferWithMaxSize() {
        try {
            FileInputStream fileStream = new FileInputStream(testFile);
            ByteBuffer byteData = DataUtil.readToByteBuffer(fileStream, 100);
            fileStream.close();
            assertEquals(100, byteData.capacity());
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testReadToByteBufferWithoutMaxSize() {
        try {
            FileInputStream fileStream = new FileInputStream(testFile);
            ByteBuffer byteData = DataUtil.readToByteBuffer(fileStream);
            fileStream.close();
            assertTrue(byteData.capacity() > 0);
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }
}