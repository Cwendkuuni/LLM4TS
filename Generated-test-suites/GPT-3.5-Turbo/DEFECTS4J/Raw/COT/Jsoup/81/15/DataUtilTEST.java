import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {
    private File file;
    private InputStream inputStream;

    @Before
    public void setUp() {
        file = new File("/path/to/file.html"); // replace with actual file path
        inputStream = getClass().getResourceAsStream("/test.html"); // replace with actual resource file path
    }

    @Test
    public void testLoadFile() throws IOException {
        Document document = DataUtil.load(file, "UTF-8", "http://example.com");
        assertNotNull(document);
        // add more assertions here
    }

    @Test
    public void testLoadInputStream() throws IOException {
        Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com");
        assertNotNull(document);
        // add more assertions here
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com", Parser.htmlParser());
        assertNotNull(document);
        // add more assertions here
    }

    @Test
    public void testCrossStreams() throws IOException {
        // create input and output streams
        // call DataUtil.crossStreams and assert the result
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 1024);
        assertNotNull(byteBuffer);
        // add more assertions here
    }

    @Test
    public void testReadToByteBufferNoLimit() throws IOException {
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        assertNotNull(byteBuffer);
        // add more assertions here
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
        assertNotNull(byteBuffer);
        // add more assertions here
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        assertNotNull(byteBuffer);
        assertEquals(byteBuffer.capacity(), 0);
    }

    @Test
    public void testGetCharsetFromContentTypeWithCharset() {
        String charset = DataUtil.getCharsetFromContentType("text/html; charset=UTF-8");
        assertEquals(charset, "UTF-8");
    }

    @Test
    public void testGetCharsetFromContentTypeWithoutCharset() {
        String charset = DataUtil.getCharsetFromContentType("text/html");
        assertEquals(charset, null);
    }
}