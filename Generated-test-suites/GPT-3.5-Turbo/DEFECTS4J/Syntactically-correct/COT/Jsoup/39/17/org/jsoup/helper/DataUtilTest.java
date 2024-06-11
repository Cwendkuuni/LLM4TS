
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import static org.junit.Assert.*;

public class DataUtilTest {
    private File testFile;
    private InputStream testInputStream;

    @Before
    public void setUp() {
        testFile = new File("test.html"); // Replace "test.html" with the actual test file name
        testInputStream = getClass().getResourceAsStream("test.html"); // Replace "test.html" with the actual test file name
    }

    @Test
    public void testLoadFromFile() throws IOException {
        Document document = DataUtil.load(testFile, Charset.defaultCharset().name(), "");
        assertNotNull(document);
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        Document document = DataUtil.load(testInputStream, Charset.defaultCharset().name(), "");
        assertNotNull(document);
    }

    @Test
    public void testLoadFromInputStreamWithParser() throws IOException {
        Document document = DataUtil.load(testInputStream, Charset.defaultCharset().name(), "", Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithCharset() {
        // Create a ByteBuffer testByteData with specific byte sequence
        Document document = DataUtil.parseByteData(testByteData, "UTF-8", "", Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithoutCharset() {
        // Create a ByteBuffer testByteData with specific byte sequence
        Document document = DataUtil.parseByteData(testByteData, null, "", Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithMetaCharset() {
        // Create a ByteBuffer testByteData with specific byte sequence
        Document document = DataUtil.parseByteData(testByteData, null, "", Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testReadToByteBufferWithoutMaxSize() throws IOException {
        // Create an InputStream testInputStream and fill it with test data
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(testInputStream);
        assertNotNull(byteBuffer);
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        // Create an InputStream testInputStream and fill it with test data
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(testInputStream, 1024);
        assertNotNull(byteBuffer);
    }

    @Test
    public void testGetCharsetFromContentTypeWithCharset() {
        String charset = DataUtil.getCharsetFromContentType("text/html;charset=UTF-8");
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithoutCharset() {
        String charset = DataUtil.getCharsetFromContentType("text/html");
        assertNull(charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidCharset() {
        String charset = DataUtil.getCharsetFromContentType("text/html;charset=invalid");
        assertNull(charset);
    }
}
