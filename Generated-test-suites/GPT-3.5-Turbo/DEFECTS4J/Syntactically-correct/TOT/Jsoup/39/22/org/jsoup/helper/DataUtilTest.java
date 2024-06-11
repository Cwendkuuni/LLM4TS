
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;

import static org.junit.Assert.*;

public class DataUtilTest {
    private final String FILE_PATH = "test.html";
    private final String CHARSET_NAME = "UTF-8";
    private final String BASE_URI = "http://example.com";

    @Test
    public void testLoadFile_validFile_expectDocument() {
        File file = new File(FILE_PATH);
        try {
            Document doc = DataUtil.load(file, CHARSET_NAME, BASE_URI);
            assertNotNull(doc);
            assertEquals(1, doc.getAllElements().size());
        } catch (IOException e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testLoadFile_invalidFile_expectIOException() {
        File file = new File("invalid-file.html");
        try {
            DataUtil.load(file, CHARSET_NAME, BASE_URI);
            fail("Expected IOException was not thrown.");
        } catch (IOException e) {
            assertEquals("File not found: invalid-file.html", e.getMessage());
        }
    }

    @Test
    public void testLoadInputStream_validInputStream_expectDocument() {
        String html = "<html><head></head><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        try {
            Document doc = DataUtil.load(inputStream, CHARSET_NAME, BASE_URI);
            assertNotNull(doc);
            assertEquals(1, doc.getAllElements().size());
        } catch (IOException e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testLoadInputStream_invalidInputStream_expectIOException() {
        InputStream inputStream = null;
        try {
            Document doc = DataUtil.load(inputStream, CHARSET_NAME, BASE_URI);
            fail("Expected IOException was not thrown.");
        } catch (IOException e) {
            assertEquals("Input stream is null.", e.getMessage());
        }
    }

    @Test
    public void testLoadInputStream_withParser_expectDocument() {
        String html = "<html><head></head><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        try {
            Document doc = DataUtil.load(inputStream, CHARSET_NAME, BASE_URI, Parser.xmlParser());
            assertNotNull(doc);
            assertEquals(1, doc.getAllElements().size());
        } catch (IOException e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testLoadInputStream_withInvalidParser_expectIOException() {
        String html = "<html><head></head><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        try {
            Document doc = DataUtil.load(inputStream, CHARSET_NAME, BASE_URI, null);
            fail("Expected IOException was not thrown.");
        } catch (IOException e) {
            assertEquals("Parser is null.", e.getMessage());
        }
    }

    @Test
    public void testParseByteData_validData_expectDocument() {
        String html = "<html><head></head><body><h1>Test</h1></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(html.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseByteData(byteData, CHARSET_NAME, BASE_URI, Parser.htmlParser());
        assertNotNull(doc);
        assertEquals(1, doc.getAllElements().size());
    }

    @Test
    public void testParseByteData_invalidData_expectNullPointerException() {
        ByteBuffer byteData = null;
        try {
            Document doc = DataUtil.parseByteData(byteData, CHARSET_NAME, BASE_URI, Parser.htmlParser());
            fail("Expected NullPointerException was not thrown.");
        } catch (NullPointerException e) {
            assertEquals("Byte buffer is null.", e.getMessage());
        }
    }

    @Test
    public void testReadToByteBuffer_validInputStream_expectByteBuffer() {
        String html = "<html><head></head><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        try {
            ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
            assertNotNull(byteData);
            assertEquals(37, byteData.limit());
        } catch (IOException e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testReadToByteBuffer_invalidInputStream_expectIOException() {
        InputStream inputStream = null;
        try {
            ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
            fail("Expected IOException was not thrown.");
        } catch (IOException e) {
            assertEquals("Input stream is null.", e.getMessage());
        }
    }

    @Test
    public void testReadToByteBuffer_validInputStreamAndMaxSize_expectByteBuffer() {
        String html = "<html><head></head><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        try {
            ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, 50);
            assertNotNull(byteData);
            assertEquals(37, byteData.limit());
        } catch (IOException e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testReadToByteBuffer_invalidInputStreamAndMaxSize_expectIOException() {
        InputStream inputStream = null;
        try {
            ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, 50);
            fail("Expected IOException was not thrown.");
        } catch (IOException e) {
            assertEquals("Input stream is null.", e.getMessage());
        }
    }

    @Test
    public void testGetCharsetFromContentType_validContentType_expectCharset() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentType_invalidContentType_expectNull() {
        String contentType = "text/html";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testGetCharsetFromContentType_invalidCharset_expectNull() {
        String contentType = "text/html; charset=invalidCharset";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }
}

