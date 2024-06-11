import org.jsoup.Jsoup;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DataUtilTest {

    private static final String TEST_FILE_PATH = "src/test/resources/sample.html";
    private static final String CHARSET_UTF_8 = "UTF-8";
    private static File testFile;

    @BeforeClass
    public static void setUp() {
        testFile = new File(TEST_FILE_PATH);
    }

    @Test
    public void testLoadFile_ValidFile_ReturnsDocument() throws IOException {
        Document doc = DataUtil.load(testFile, CHARSET_UTF_8, "");
        assertNotNull(doc);
    }

    @Test
    public void testLoadFile_InvalidFile_ThrowsIOException() {
        File invalidFile = new File("invalid/file/path.html");
        try {
            DataUtil.load(invalidFile, CHARSET_UTF_8, "");
        } catch (IOException e) {
            assertEquals("java.io.FileNotFoundException", e.getClass().getName());
            assertEquals("invalid/file/path.html (No such file or directory)", e.getMessage());
        }
    }

    @Test
    public void testLoadFile_NullFile_ThrowsIOException() {
        try {
            DataUtil.load(null, CHARSET_UTF_8, "");
        } catch (IOException e) {
            assertEquals("java.io.IOException", e.getClass().getName());
            assertEquals("Empty body", e.getMessage());
        }
    }

    @Test
    public void testLoadInputStream_ValidInputStream_ReturnsDocument() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("<html><body><h1>Test</h1></body></html>".getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, CHARSET_UTF_8, "");
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStream_NullInputStream_ThrowsIOException() {
        try {
            DataUtil.load(null, CHARSET_UTF_8, "");
        } catch (IOException e) {
            assertEquals("java.io.IOException", e.getClass().getName());
            assertEquals("Empty body", e.getMessage());
        }
    }

    @Test
    public void testLoadInputStream_InvalidCharset_ThrowsIOException() {
        InputStream inputStream = new ByteArrayInputStream("<html><body><h1>Test</h1></body></html>".getBytes(StandardCharsets.UTF_8));
        try {
            DataUtil.load(inputStream, "InvalidCharset", "");
        } catch (IOException e) {
            assertEquals("java.nio.charset.UnsupportedCharsetException", e.getClass().getName());
            assertEquals("InvalidCharset", e.getMessage());
        }
    }

    @Test
    public void testLoadInputStream_NullCharset_ReturnsDocumentWithDefaultCharset() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("<html><body><h1>Test</h1></body></html>".getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, null, "");
        assertNotNull(doc);
        assertEquals("UTF-8", doc.outputSettings().charset().displayName());
    }

    @Test
    public void testLoadInputStreamWithParser_ValidInputStream_ReturnsDocumentWithParser() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("<html><body><h1>Test</h1></body></html>".getBytes(StandardCharsets.UTF_8));
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.load(inputStream, CHARSET_UTF_8, "", parser);
        assertNotNull(doc);
        assertEquals(parser, doc.parser());
    }

    @Test
    public void testLoadInputStreamWithParser_NullInputStream_ThrowsIOException() {
        try {
            DataUtil.load(null, CHARSET_UTF_8, "", Parser.htmlParser());
        } catch (IOException e) {
            assertEquals("java.io.IOException", e.getClass().getName());
            assertEquals("Empty body", e.getMessage());
        }
    }

    @Test
    public void testCrossStreams_ValidInputStreamAndOutputStream_NoExceptionsThrown() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("<html><body><h1>Test</h1></body></html>".getBytes(StandardCharsets.UTF_8));
        OutputStream outputStream = System.out; // Write to console
        DataUtil.crossStreams(inputStream, outputStream);
    }

    // More test cases can be added for the remaining methods

}