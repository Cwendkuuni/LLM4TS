import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

public class DataUtilTest {

    @Test
    public void testLoadFile() {
        try {
            File file = new File("test.html");
            Document doc = DataUtil.load(file, "UTF-8", "http://example.com");
            assertNotNull(doc);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testLoadInputStream() {
        try {
            InputStream inputStream = new FileInputStream("test.html");
            Document doc = DataUtil.load(inputStream, "UTF-8", "http://example.com");
            assertNotNull(doc);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testLoadInputStreamWithParser() {
        try {
            InputStream inputStream = new FileInputStream("test.html");
            Document doc = DataUtil.load(inputStream, "UTF-8", "http://example.com", Parser.xmlParser());
            assertNotNull(doc);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testCrossStreams() {
        try {
            InputStream inputStream = new FileInputStream("input.txt");
            OutputStream outputStream = new ByteArrayOutputStream();
            DataUtil.crossStreams(inputStream, outputStream);
            assertNotNull(outputStream);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testReadToByteBuffer() {
        try {
            InputStream inputStream = new FileInputStream("test.txt");
            ByteBuffer buffer = DataUtil.readToByteBuffer(inputStream, 1024);
            assertNotNull(buffer);
        } catch (IOException e) {
            fail("IOException should not be thrown");
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