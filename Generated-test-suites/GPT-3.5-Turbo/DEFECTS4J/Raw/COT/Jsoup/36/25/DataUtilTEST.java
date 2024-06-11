
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class DataUtilTest {

    private static String testFilePath;

    @BeforeClass
    public static void setUpBeforeClass() {
        testFilePath = "path/to/test/file.html";
    }

    @Test
    public void shouldLoadFileToDocument() throws IOException {
        File file = new File(testFilePath);
        Document document = DataUtil.load(file, "UTF-8", "https://example.com");
        assertNotNull(document);
        assertEquals("https://example.com", document.baseUri());
    }

    @Test
    public void shouldLoadInputStreamToDocument() throws IOException {
        FileInputStream inputStream = new FileInputStream(testFilePath);
        Document document = DataUtil.load(inputStream, "UTF-8", "https://example.com");
        assertNotNull(document);
        assertEquals("https://example.com", document.baseUri());
    }

    @Test
    public void shouldLoadInputStreamToDocumentWithCustomParser() throws IOException {
        FileInputStream inputStream = new FileInputStream(testFilePath);
        Parser parser = Parser.xmlParser(); // Change to specific parser you want to test
        Document document = DataUtil.load(inputStream, "UTF-8", "https://example.com", parser);
        assertNotNull(document);
        assertEquals("https://example.com", document.baseUri());
    }

    @Test
    public void shouldParseByteDataToDocument() {
        ByteBuffer byteData = ByteBuffer.wrap("Example byte data".getBytes());
        Document document = DataUtil.parseByteData(byteData, "UTF-8", "https://example.com", Parser.htmlParser());
        assertNotNull(document);
        assertEquals("https://example.com", document.baseUri());
    }

    @Test
    public void shouldReadInputStreamToByteBuffer() throws IOException {
        FileInputStream inputStream = new FileInputStream(testFilePath);
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        assertNotNull(byteData);
        assertTrue(byteData.remaining() > 0);
    }

    @Test
    public void shouldReadInputStreamToByteBufferWithMaxSize() throws IOException {
        FileInputStream inputStream = new FileInputStream(testFilePath);
        int maxSize = 1024; // Set maximum size as desired
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, maxSize);
        assertNotNull(byteData);
        assertTrue(byteData.remaining() > 0);
        assertTrue(byteData.remaining() <= maxSize);
    }

    @Test
    public void shouldGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNotNull(charset);
        assertEquals("UTF-8", charset);
    }

}
