import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class DataUtilTest {

    @Test
    public void testLoadFromFile() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, "UTF-8", "http://example.com");
        assertNotNull(doc);
        // Add more assertions as needed
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        String html = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        Document doc = DataUtil.load(inputStream, "UTF-8", "http://example.com");
        assertNotNull(doc);
        // Add more assertions as needed
    }

    @Test
    public void testLoadFromInputStreamAndParser() throws IOException {
        String html = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        Document doc = DataUtil.load(inputStream, "UTF-8", "http://example.com", Parser.xmlParser());
        assertNotNull(doc);
        // Add more assertions as needed
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Hello, world!".getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(inputStream, outputStream);
        assertEquals("Hello, world!", outputStream.toString());
    }

    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.wrap("test".getBytes());
        Document doc = DataUtil.parseByteData(byteData, "UTF-8", "http://example.com", Parser.htmlParser());
        assertNotNull(doc);
        // Add more assertions as needed
    }

    @Test
    public void testReadToByteBufferCapped() throws IOException {
        String text = "test";
        InputStream inputStream = new ByteArrayInputStream(text.getBytes());
        ByteBuffer buffer = DataUtil.readToByteBuffer(inputStream, 2);
        assertEquals(2, buffer.capacity());
    }

    @Test
    public void testReadToByteBufferUncapped() throws IOException {
        String text = "test";
        InputStream inputStream = new ByteArrayInputStream(text.getBytes());
        ByteBuffer buffer = DataUtil.readToByteBuffer(inputStream);
        assertEquals(4, buffer.capacity());
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File file = new File("test.txt");
        ByteBuffer buffer = DataUtil.readFileToByteBuffer(file);
        assertNotNull(buffer);
        // Add more assertions as needed
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer buffer = DataUtil.emptyByteBuffer();
        assertEquals(0, buffer.capacity());
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
        assertTrue(boundary.length() == 32);
    }
}