import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.Locale;

public class DataUtilTest {
    private static File testFile;

    @BeforeClass
    public static void setUp() {
        testFile = new File("test.html");
        // create a test file for the load method
        try {
            FileWriter writer = new FileWriter(testFile);
            writer.write("<html><body><h1>Hello World!</h1></body></html>");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoadWithFile() {
        try {
            Document document = DataUtil.load(testFile, "UTF-8", "http://example.com");
            assertNotNull(document);
            assertEquals("Hello World!", document.select("h1").text());
        } catch (IOException e) {
            fail("Exception was thrown");
        }
    }

    @Test
    public void testLoadWithInputStream() {
        try {
            FileInputStream fileInputStream = new FileInputStream(testFile);
            Document document = DataUtil.load(fileInputStream, "UTF-8", "http://example.com");
            assertNotNull(document);
            assertEquals("Hello World!", document.select("h1").text());
        } catch (IOException e) {
            fail("Exception was thrown");
        }
    }

    @Test
    public void testLoadWithCustomParser() {
        try {
            FileInputStream fileInputStream = new FileInputStream(testFile);
            Document document = DataUtil.load(fileInputStream, "UTF-8", "http://example.com", Parser.xmlParser());
            assertNotNull(document);
            assertEquals("Hello World!", document.select("h1").text());
        } catch (IOException e) {
            fail("Exception was thrown");
        }
    }

    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.wrap("Test Data".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(document);
    }

    @Test
    public void testReadToByteBufferUnlimited() {
        try {
            FileInputStream fileInputStream = new FileInputStream(testFile);
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(fileInputStream, 0);
            assertNotNull(byteBuffer);
            assertTrue(byteBuffer.hasRemaining());
        } catch (IOException e) {
            fail("Exception was thrown");
        }
    }

    @Test
    public void testReadToByteBufferCapped() {
        try {
            FileInputStream fileInputStream = new FileInputStream(testFile);
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(fileInputStream, 5);
            assertNotNull(byteBuffer);
            assertEquals(5, byteBuffer.remaining());
        } catch (IOException e) {
            fail("Exception was thrown");
        }
    }

    @Test
    public void testGetCharsetFromContentType() {
        String charset = DataUtil.getCharsetFromContentType("text/html; charset=UTF-8");
        assertNotNull(charset);
        assertEquals("UTF-8", charset);

        charset = DataUtil.getCharsetFromContentType(null);
        assertNull(charset);
    }
}