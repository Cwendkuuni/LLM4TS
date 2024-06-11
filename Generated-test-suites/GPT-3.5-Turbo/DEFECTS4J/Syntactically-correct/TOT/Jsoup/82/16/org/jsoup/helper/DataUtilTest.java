import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.*;

public class DataUtilTest {

    @Test
    public void testLoadFile() {
        File testFile = new File("test.html");
        try {
            Document doc = DataUtil.load(testFile, "UTF-8", "http://example.com");
            assertNotNull(doc);
            // Add more assertions here based on the expected result
        } catch (IOException e) {
            fail("IO Exception occurred");
        }
    }

    @Test
    public void testLoadInputStream() {
        InputStream inputStream = new ByteArrayInputStream("<html><body></body></html>".getBytes());
        try {
            Document doc = DataUtil.load(inputStream, "UTF-8", "http://example.com");
            assertNotNull(doc);
            // Add more assertions here based on the expected result
        } catch (IOException e) {
            fail("IO Exception occurred");
        }
    }

    @Test
    public void testLoadInputStreamWithParser() {
        InputStream inputStream = new ByteArrayInputStream("<html><body></body></html>".getBytes());
        try {
            Document doc = DataUtil.load(inputStream, "UTF-8", "http://example.com", Parser.xmlParser());
            assertNotNull(doc);
            // Add more assertions here based on the expected result
        } catch (IOException e) {
            fail("IO Exception occurred");
        }
    }

    @Test
    public void testCrossStreams() {
        InputStream inputStream = new ByteArrayInputStream("Hello World".getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            DataUtil.crossStreams(inputStream, outputStream);
            String result = outputStream.toString("UTF-8");
            assertEquals("Hello World", result);
        } catch (IOException e) {
            fail("IO Exception occurred");
        }
    }

    @Test
    public void testReadToByteBuffer() {
        InputStream inputStream = new ByteArrayInputStream("Hello World".getBytes());
        try {
            ByteBuffer buffer = DataUtil.readToByteBuffer(inputStream, 100);
            assertNotNull(buffer);
            String result = new String(buffer.array(), "UTF-8");
            assertEquals("Hello World", result);
        } catch (IOException e) {
            fail("IO Exception occurred");
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
        // Add more assertions here based on the expected result
    }
}