import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        Document document = DataUtil.load(file, "UTF-8", "https://example.com");
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        Document document = DataUtil.load(inputStream, "UTF-8", "https://example.com");
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.xml");
        Document document = DataUtil.load(inputStream, "UTF-8", "https://example.com", Parser.xmlParser());
        assertNotNull(document);
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("image.png");
        OutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(inputStream, outputStream);
        assertEquals(inputStream.available(), outputStream.toByteArray().length);
    }

    @Test
    public void testParseInputStream() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        Document document = DataUtil.parseInputStream(inputStream, "UTF-8", "https://example.com", Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.txt");
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 1024);
        assertNotNull(byteBuffer);
    }

    @Test
    public void testReadToByteBufferWithoutMaxSize() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.txt");
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        assertNotNull(byteBuffer);
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File file = new File("test.txt");
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
        assertNotNull(byteBuffer);
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        assertNotNull(byteBuffer);
        assertEquals(0, byteBuffer.capacity());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithNoCharset() {
        String contentType = "text/html";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidCharset() {
        String contentType = "text/html; charset=invalid";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        assertNotNull(mimeBoundary);
        assertEquals(32, mimeBoundary.length());
    }

    // Additional test cases for edge cases and exceptions to be added here

}