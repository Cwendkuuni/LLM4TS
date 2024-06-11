import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

public class DataUtilTest {

    @Test
    public void testLoadWithValidFile_shouldLoadDocument() throws IOException {
        File file = new File("test.html");
        Document document = DataUtil.load(file, StandardCharsets.UTF_8.name(), "http://example.com");
        assertNotNull(document);
    }

    @Test
    public void testLoadWithInvalidFile_shouldThrowIOException() {
        assertThrows(IOException.class, () -> {
            File file = new File("invalid.html");
            DataUtil.load(file, StandardCharsets.UTF_8.name(), "http://example.com");
        });
    }

    @Test
    public void testLoadWithNullInputStream_shouldThrowIOException() {
        assertThrows(IOException.class, () -> {
            InputStream inputStream = null;
            DataUtil.load(inputStream, StandardCharsets.UTF_8.name(), "http://example.com");
        });
    }

    @Test
    public void testLoadWithValidInputStream_shouldLoadDocument() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/test.html");
        Document document = DataUtil.load(inputStream, StandardCharsets.UTF_8.name(), "http://example.com");
        assertNotNull(document);
    }

    @Test
    public void testLoadWithAlternateParser_shouldLoadDocument() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/test.html");
        Document document = DataUtil.load(inputStream, StandardCharsets.UTF_8.name(), "http://example.com", Parser.xmlParser());
        assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithValidCharset_shouldParseDocument() {
        String docData = "<html><body><h1>Hello World</h1></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(docData.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseByteData(byteData, StandardCharsets.UTF_8.name(), "http://example.com", Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithNullCharset_shouldParseDocument() {
        String docData = "<html><body><h1>Hello World</h1></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(docData.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseByteData(byteData, null, "http://example.com", Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testGetCharsetFromContentTypeWithValidContentType_shouldReturnCharset() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidContentType_shouldReturnNull() {
        String contentType = "text/html";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testReadToByteBufferWithValidInputStreamAndMaxSize_shouldReadByteBuffer() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/test.html");
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 1024);
        assertNotNull(byteBuffer);
        assertTrue(byteBuffer.limit() <= 1024);
    }

    @Test
    public void testReadToByteBufferWithValidInputStreamAndZeroMaxSize_shouldReadByteBuffer() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/test.html");
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 0);
        assertNotNull(byteBuffer);
    }

    @Test
    public void testReadToByteBufferWithValidInputStreamAndNegativeMaxSize_shouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            InputStream inputStream = getClass().getResourceAsStream("/test.html");
            DataUtil.readToByteBuffer(inputStream, -1);
        });
    }

    @Test
    public void testReadToByteBufferWithNullInputStream_shouldThrowIOException() {
        assertThrows(IOException.class, () -> {
            InputStream inputStream = null;
            DataUtil.readToByteBuffer(inputStream);
        });
    }
    
    @Test
    public void testLoadWithNullCharset_shouldAttemptToDetectCharset() throws IOException {
        File file = new File("test.html");
        Document document = DataUtil.load(file, null, "http://example.com");
        assertNotNull(document);
    }

}