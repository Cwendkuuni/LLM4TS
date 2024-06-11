import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import static org.mockito.Mockito.*;

public class DataUtilTest {

    // Test data
    private File testFile;
    private static final String BASE_URI = "http://example.com";
    private static final String CHARSET_UTF8 = "UTF-8";
    private static final String CHARSET_GB2312 = "gb2312";

    @Mock
    private InputStream inputStreamMock;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        testFile = new File("test.html");
    }

    @Test
    public void testLoadFromFileWithCharset() throws IOException {
        // Test case: Load a file with specified charset
        Document doc = DataUtil.load(testFile, CHARSET_UTF8, BASE_URI);
        String title = doc.title();
        Assert.assertEquals("Test Title", title);
    }

    @Test
    public void testLoadFromFileWithMetaCharset() throws IOException {
        // Test case: Load a file without specified charset, but with <meta charset> tag
        Document doc = DataUtil.load(testFile, null, BASE_URI);
        String title = doc.title();
        Assert.assertEquals("Test Title", title);
    }

    @Test
    public void testLoadFromFileWithoutCharset() throws IOException {
        // Test case: Load a file without specified charset and without <meta charset> tag
        Document doc = DataUtil.load(testFile, null, BASE_URI);
        String title = doc.title();
        Assert.assertEquals("Test Title", title);
    }

    @Test
    public void testLoadFromInputStreamWithCharset() throws IOException {
        // Test case: Load from an input stream with specified charset
        when(inputStreamMock.read(any(byte[].class))).thenReturn(-1);
        Document doc = DataUtil.load(inputStreamMock, CHARSET_UTF8, BASE_URI);
        verify(inputStreamMock).close();
        String title = doc.title();
        Assert.assertEquals("Test Title", title);
    }

    @Test
    public void testLoadFromInputStreamWithMetaCharset() throws IOException {
        // Test case: Load from an input stream without specified charset, but with <meta charset> tag
        when(inputStreamMock.read(any(byte[].class))).thenReturn(-1);
        Document doc = DataUtil.load(inputStreamMock, null, BASE_URI);
        verify(inputStreamMock).close();
        String title = doc.title();
        Assert.assertEquals("Test Title", title);
    }

    @Test
    public void testLoadFromInputStreamWithoutCharset() throws IOException {
        // Test case: Load from an input stream without specified charset and without <meta charset> tag
        when(inputStreamMock.read(any(byte[].class))).thenReturn(-1);
        Document doc = DataUtil.load(inputStreamMock, null, BASE_URI);
        verify(inputStreamMock).close();
        String title = doc.title();
        Assert.assertEquals("Test Title", title);
    }

    @Test
    public void testLoadFromInputStreamWithCustomParser() throws IOException {
        // Test case: Load from an input stream with a custom parser
        when(inputStreamMock.read(any(byte[].class))).thenReturn(-1);
        Document doc = DataUtil.load(inputStreamMock, CHARSET_UTF8, BASE_URI, Parser.xmlParser());
        verify(inputStreamMock).close();
        String title = doc.title();
        Assert.assertEquals("Test Title", title);
    }

    @Test
    public void testCrossStreams() throws IOException {
        // Test case: Cross streams
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream("Test Data".getBytes(StandardCharsets.UTF_8));
        DataUtil.crossStreams(inputStream, outputStream);
        Assert.assertEquals("Test Data", outputStream.toString(StandardCharsets.UTF_8));
    }

    @Test
    public void testParseByteDataWithCharsetOverride() {
        // Test case: Parse byte data with charset override
        ByteBuffer byteData = ByteBuffer.wrap("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html;charset=gb2312\"></head><body><h1>Test Title</h1></body></html>".getBytes(StandardCharsets.UTF_8));
        String charsetName = "gb2312";
        String baseUri = BASE_URI;
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        String title = doc.title();
        Assert.assertEquals("Test Title", title);
    }

    @Test
    public void testParseByteDataWithMetaCharset() {
        // Test case: Parse byte data with <meta charset> tag
        ByteBuffer byteData = ByteBuffer.wrap("<html><head><meta charset=\"gb2312\"></head><body><h1>Test Title</h1></body></html>".getBytes(StandardCharsets.UTF_8));
        String charsetName = null;
        String baseUri = BASE_URI;
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        String title = doc.title();
        Assert.assertEquals("Test Title", title);
    }

    @Test
    public void testParseByteDataWithoutCharset() {
        // Test case: Parse byte data without specified charset and without <meta charset> tag
        ByteBuffer byteData = ByteBuffer.wrap("<html><head></head><body><h1>Test Title</h1></body></html>".getBytes(StandardCharsets.UTF_8));
        String charsetName = null;
        String baseUri = BASE_URI;
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        String title = doc.title();
        Assert.assertEquals("Test Title", title);
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        // Test case: Read input stream to byte buffer with maximum size
        InputStream inputStream = new ByteArrayInputStream("Test Data".getBytes(StandardCharsets.UTF_8));
        int maxSize = 5;
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);
        Assert.assertEquals("Test ", Charset.forName("UTF-8").decode(byteBuffer).toString());
    }

    @Test
    public void testReadToByteBufferWithoutMaxSize() throws IOException {
        // Test case: Read input stream to byte buffer without maximum size
        InputStream inputStream = new ByteArrayInputStream("Test Data".getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        Assert.assertEquals("Test Data", Charset.forName("UTF-8").decode(byteBuffer).toString());
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        // Test case: Read file to byte buffer
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(testFile);
        Assert.assertEquals("Test Data", Charset.forName("UTF-8").decode(byteBuffer).toString());
    }

    @Test
    public void testEmptyByteBuffer() {
        // Test case: Empty byte buffer
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        Assert.assertEquals(0, byteBuffer.capacity());
    }

    @Test
    public void testGetCharsetFromContentType() {
        // Test case: Get charset from content type header
        String contentType = "text/html; charset=gb2312";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals(CHARSET_GB2312, charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidCharset() {
        // Test case: Get charset from content type header with invalid charset
        String contentType = "text/html; charset=invalid";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertNull(charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithNoCharset() {
        // Test case: Get charset from content type header with no charset
        String contentType = "text/html";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertNull(charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithEmptyCharset() {
        // Test case: Get charset from content type header with empty charset
        String contentType = "text/html; charset=";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertNull(charset);
    }

    @Test
    public void testMimeBoundary() {
        // Test case: Generate mime boundary
        String mimeBoundary = DataUtil.mimeBoundary();
        Assert.assertNotNull(mimeBoundary);
        Assert.assertEquals(DataUtil.boundaryLength, mimeBoundary.length());
    }
}