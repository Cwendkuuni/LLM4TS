import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {
    private File file;
    private String charsetName;
    private String baseUri;
    private Parser parser;

    @Before
    public void setUp() {
        file = new File("file.html");
        charsetName = "UTF-8";
        baseUri = "http://example.com";
        parser = Parser.htmlParser();
    }

    @Test
    public void testLoadFile() throws IOException {
        Document document = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(document);
        // Add additional assertions here
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("file.html");
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(document);
        // Add additional assertions here
    }

    @Test
    public void testLoadInputStreamWithCustomParser() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("file.html");
        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
        assertNotNull(document);
        // Add additional assertions here
    }

    @Test
    public void testCrossStreams() throws IOException {
        // Add test case for crossStreams() method
    }

    @Test
    public void testParseInputStream() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("file.html");
        Document document = DataUtil.parseInputStream(inputStream, charsetName, baseUri, parser);
        assertNotNull(document);
        // Add additional assertions here
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("file.html");
        byte[] expectedBytes = new byte[] { 1, 2, 3, 4, 5 };
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, expectedBytes.length);
        assertNotNull(byteBuffer);
        assertEquals(expectedBytes.length, byteBuffer.remaining());
        byte[] actualBytes = new byte[byteBuffer.remaining()];
        byteBuffer.get(actualBytes);
        assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File file = new File("file.html");
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
        assertNotNull(byteBuffer);
        // Add additional assertions here
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
}