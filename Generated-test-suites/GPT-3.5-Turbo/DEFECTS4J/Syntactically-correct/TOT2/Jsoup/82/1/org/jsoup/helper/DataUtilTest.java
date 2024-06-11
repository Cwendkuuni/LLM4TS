import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import static org.junit.Assert.*;

public class DataUtilTest {

    @Test
    public void testLoadFile() {
        File file = new File("test.html");
        try {
            Document doc = DataUtil.load(file, "UTF-8", "https://www.example.com");
            assertNotNull(doc);
        } catch (IOException e) {
            fail("Exception thrown while loading file");
        }
    }

    @Test
    public void testLoadInputStream() {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        try {
            Document doc = DataUtil.load(inputStream, "UTF-8", "https://www.example.com");
            assertNotNull(doc);
        } catch (IOException e) {
            fail("Exception thrown while loading input stream");
        }
    }

    @Test
    public void testParseInputStream() {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        try {
            Document doc = DataUtil.parseInputStream(inputStream, "UTF-8", "https://www.example.com", Parser.htmlParser());
            assertNotNull(doc);
        } catch (IOException e) {
            fail("Exception thrown while parsing input stream");
        }
    }

    @Test
    public void testCrossStreams() {
        InputStream inputStream = getClass().getResourceAsStream("input.txt");
        OutputStream outputStream = new ByteArrayOutputStream();
        try {
            DataUtil.crossStreams(inputStream, outputStream);
            assertTrue(outputStream.size() > 0);
        } catch (IOException e) {
            fail("Exception thrown while crossing streams");
        }
    }

    @Test
    public void testReadToByteBuffer() {
        InputStream inputStream = getClass().getResourceAsStream("input.txt");
        try {
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 1024);
            assertNotNull(byteBuffer);
        } catch (IOException e) {
            fail("Exception thrown while reading to byte buffer");
        }
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertNotNull(boundary);
        assertEquals(32, boundary.length());
    }

}