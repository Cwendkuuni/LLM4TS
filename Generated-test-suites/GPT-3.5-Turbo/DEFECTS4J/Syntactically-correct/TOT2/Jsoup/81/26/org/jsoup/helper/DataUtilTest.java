import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws Exception {
        File file = new File("test.html");
        Document document = DataUtil.load(file, "UTF-8", "http://example.com");
        assertNotNull(document);
        // Add more assertions to test the loaded document
    }

    @Test
    public void testLoadInputStream() throws Exception {
        String html = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com");
        assertNotNull(document);
        // Add more assertions to test the loaded document
    }

    @Test
    public void testLoadInputStreamWithCustomParser() throws Exception {
        String html = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com", Parser.xmlParser());
        assertNotNull(document);
        // Add more assertions to test the loaded document
    }

    // Add more test methods for other public methods in DataUtil class

    @Test
    public void testReadToByteBufferWithMaxSize() throws Exception {
        String text = "Hello, World!";
        InputStream inputStream = new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 10);
        assertNotNull(byteBuffer);
        assertEquals(10, byteBuffer.limit());
    }


    @Test
    public void testMimeBoundary() throws Exception {
        String mimeBoundary = DataUtil.mimeBoundary();
        assertNotNull(mimeBoundary);
        assertEquals(32, mimeBoundary.length());
        assertTrue(mimeBoundary.matches("[\\-_0-9a-zA-Z]{32}"));
    }

    // Add more test methods for edge cases and error scenarios

}