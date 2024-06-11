import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import static org.junit.Assert.*;

public class DataUtilTest {

    private static final String TEST_FILE = "test.html";
    private static final String CHARSET_UTF8 = "UTF-8";
    private static final String CHARSET_ISO8859 = "ISO-8859-1";

    @Test
    public void testLoadFile() throws IOException {
        File testFile = createTestFile(TEST_FILE, CHARSET_UTF8);

        Document document = DataUtil.load(testFile, CHARSET_UTF8, "");
        assertNotNull(document);
    }

    @Test
    public void testLoadFileWithInvalidCharset() throws IOException {
        File testFile = createTestFile(TEST_FILE, CHARSET_ISO8859);

        try {
            Document document = DataUtil.load(testFile, CHARSET_UTF8, "");
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            // Expected IOException due to invalid charset
        }
    }

    @Test
    public void testLoadFileWithNonExistentFile() throws IOException {
        File testFile = new File("nonexistent.html");

        try {
            Document document = DataUtil.load(testFile, CHARSET_UTF8, "");
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            // Expected IOException due to non-existent file
        }
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithNullFile() throws IOException {
        Document document = DataUtil.load(null, CHARSET_UTF8, "");
    }

    @Test
    public void testLoadInputStream() throws IOException {
        File testFile = createTestFile(TEST_FILE, CHARSET_UTF8);
        InputStream inputStream = new FileInputStream(testFile);

        Document document = DataUtil.load(inputStream, CHARSET_UTF8, "");
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        File testFile = createTestFile(TEST_FILE, CHARSET_ISO8859);
        InputStream inputStream = new FileInputStream(testFile);

        try {
            Document document = DataUtil.load(inputStream, CHARSET_UTF8, "");
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            // Expected IOException due to invalid charset
        }
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithNullInputStream() throws IOException {
        Document document = DataUtil.load(null, CHARSET_UTF8, "");
    }

    @Test
    public void testLoadInputStreamWithClosedInputStream() throws IOException {
        File testFile = createTestFile(TEST_FILE, CHARSET_UTF8);
        InputStream inputStream = new FileInputStream(testFile);
        inputStream.close();

        try {
            Document document = DataUtil.load(inputStream, CHARSET_UTF8, "");
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            // Expected IOException due to closed input stream
        }
    }

    @Test
    public void testLoadInputStreamWithEmptyInputStream() throws IOException {
        File testFile = createTestFile(TEST_FILE, CHARSET_UTF8);
        InputStream inputStream = new FileInputStream(testFile);
        inputStream.readAllBytes(); // Read all bytes and empty the input stream

        try {
            Document document = DataUtil.load(inputStream, CHARSET_UTF8, "");
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            // Expected IOException due to empty input stream
        }
    }

    @Test
    public void testLoadInputStreamWithNullCharset() throws IOException {
        File testFile = createTestFile(TEST_FILE, CHARSET_UTF8);
        InputStream inputStream = new FileInputStream(testFile);

        Document document = DataUtil.load(inputStream, null, "");
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithDifferentParser() throws IOException {
        File testFile = createTestFile(TEST_FILE, CHARSET_UTF8);
        InputStream inputStream = new FileInputStream(testFile);

        Document document = DataUtil.load(inputStream, CHARSET_UTF8, "", Parser.xmlParser());
        assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithNullCharset() {
        ByteBuffer byteData = ByteBuffer.wrap("<html><head></head><body></body></html>".getBytes());

        Document document = DataUtil.parseByteData(byteData, null, "", Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithCharsetInMetaTag() {
        ByteBuffer byteData = ByteBuffer.wrap(("<html><head><meta charset=\"" + CHARSET_UTF8 + "\"></head><body></body></html>").getBytes());

        Document document = DataUtil.parseByteData(byteData, null, "", Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithCharsetInContentType() {
        ByteBuffer byteData = ByteBuffer.wrap(("<html><head></head><body></body></html>").getBytes());

        Document document = DataUtil.parseByteData(byteData, CHARSET_UTF8, "", Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithInvalidCharsetInMetaTag() {
        ByteBuffer byteData = ByteBuffer.wrap(("<html><head><meta charset=\"invalid-charset\"></head><body></body></html>").getBytes());

        Document document = DataUtil.parseByteData(byteData, null, "", Parser.htmlParser());
        assertNotNull(document);
        assertEquals(CHARSET_UTF8, document.charset().name());
    }

    @Test
    public void testParseByteDataWithInvalidCharsetInContentType() {
        ByteBuffer byteData = ByteBuffer.wrap(("<html><head></head><body></body></html>").getBytes());

        Document document = DataUtil.parseByteData(byteData, "invalid-charset", "", Parser.htmlParser());
        assertNotNull(document);
        assertEquals(CHARSET_UTF8, document.charset().name());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        File testFile = createTestFile(TEST_FILE, CHARSET_UTF8);
        InputStream inputStream = new FileInputStream(testFile);

        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        assertNotNull(byteData);
        assertTrue(byteData.capacity() > 0);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=" + CHARSET_UTF8;

        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(CHARSET_UTF8, charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidCharset() {
        String contentType = "text/html; charset=invalid-charset";

        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    private File createTestFile(String fileName, String charset) throws IOException {
        Path filePath = Files.createTempFile(fileName, ".html");
        Files.writeString(filePath, "<html><head></head><body></body></html>", Charset.forName(charset));
        return filePath.toFile();
    }
}