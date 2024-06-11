import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

    @Mock
    private File file;

    @Test
    public void testLoadFromFile() throws IOException {
        Document document = DataUtil.load(file, "UTF-8", "https://example.com");
        assertNotNull(document);
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        String html = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        Document document = DataUtil.load(inputStream, "UTF-8", "https://example.com");
        assertNotNull(document);
    }

    @Test
    public void testLoadFromInputStreamWithParser() throws IOException {
        String html = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        Document document = DataUtil.load(inputStream, "UTF-8", "https://example.com", Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Test data".getBytes());
        DataUtil.crossStreams(inputStream, System.out);
        // Verify if the method executes without throwing an exception
    }

    @Test
    public void testParseByteData() {
        ByteBuffer byteBuffer = ByteBuffer.wrap("Test data".getBytes());
        Document document = DataUtil.parseByteData(byteBuffer, "UTF-8", "https://example.com", Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        byte[] data = "Test data".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        assertEquals(data.length, byteBuffer.array().length);
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        byte[] data = "Test data".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 5);
        assertEquals(5, byteBuffer.array().length);
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File file = new File("test.txt");
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
        assertNotNull(byteBuffer);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        assertEquals(32, mimeBoundary.length());
    }
}