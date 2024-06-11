import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class DataUtilTest {

    private Document document;
    private Parser parser;

    @Before
    public void setup() {
        parser = Parser.htmlParser();
    }

    @Test
    public void testLoadFileWithValidCharset() throws Exception {
        File file = new File("path/to/your/file.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document result = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(result);
        assertEquals(charsetName, result.outputSettings().charset());
    }

    @Test
    public void testLoadFileWithDefaultCharset() throws Exception {
        File file = new File("path/to/your/file.html");
        String baseUri = "http://example.com";
        Document result = DataUtil.load(file, null, baseUri);
        assertNotNull(result);
        assertEquals(DataUtil.defaultCharset, result.outputSettings().charset());
    }

    @Test
    public void testLoadInputStreamWithValidCharset() throws Exception {
        byte[] inputData = "...".getBytes("UTF-8");
        InputStream inputStream = new ByteArrayInputStream(inputData);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document result = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(result);
        assertEquals(charsetName, result.outputSettings().charset());
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws Exception {
        byte[] inputData = "...".getBytes("UTF-8");
        InputStream inputStream = new ByteArrayInputStream(inputData);
        String baseUri = "http://example.com";
        Document result = DataUtil.load(inputStream, null, baseUri);
        assertNotNull(result);
        assertEquals(DataUtil.defaultCharset, result.outputSettings().charset());
    }

    @Test
    public void testLoadInputStreamWithParser() throws Exception {
        byte[] inputData = "...".getBytes("UTF-8");
        InputStream inputStream = new ByteArrayInputStream(inputData);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = DataUtil.xmlParser();
        Document result = DataUtil.load(inputStream, charsetName, baseUri, parser);
        assertNotNull(result);
        assertEquals(charsetName, result.outputSettings().charset());
    }

    @Test(expected = IOException.class)
    public void testCrossStreamsWithNullInputStream() throws Exception {
        InputStream in = null;
        OutputStream out = new ByteArrayOutputStream();
        DataUtil.crossStreams(in, out);
        fail("Expected IOException");
    }

    @Test(expected = IOException.class)
    public void testCrossStreamsWithNullOutputStream() throws Exception {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        OutputStream out = null;
        DataUtil.crossStreams(in, out);
        fail("Expected IOException");
    }

    @Test
    public void testParseByteDataWithValidCharset() throws Exception {
        ByteBuffer byteData = DataUtil.readFileToByteBuffer(new File("path/to/your/file.html"));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document result = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(result);
        assertEquals(charsetName, result.outputSettings().charset());
    }

    @Test
    public void testParseByteDataWithDefaultCharset() throws Exception {
        ByteBuffer byteData = DataUtil.readFileToByteBuffer(new File("path/to/your/file.html"));
        String baseUri = "http://example.com";
        Document result = DataUtil.parseByteData(byteData, null, baseUri, parser);
        assertNotNull(result);
        assertEquals(DataUtil.defaultCharset, result.outputSettings().charset());
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws Exception {
        InputStream in = new ByteArrayInputStream(new byte[10]);
        ByteBuffer result = DataUtil.readToByteBuffer(in, 5);
        assertNotNull(result);
        assertEquals(5, result.limit());
    }

    @Test
    public void testReadToByteBufferUnlimited() throws Exception {
        InputStream in = new ByteArrayInputStream(new byte[10]);
        ByteBuffer result = DataUtil.readToByteBuffer(in);
        assertNotNull(result);
        assertEquals(10, result.limit());
    }

    @Test
    public void testGetCharsetFromContentType() throws Exception {
        String contentType = "text/html; charset=UTF-8";
        String result = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", result);
    }

    @Test
    public void testValidateCharset() throws Exception {
        String result = DataUtil.validateCharset("UTF-8");
        assertEquals("UTF-8", result);
        result = DataUtil.validateCharset("UTF-8\t");
        assertEquals("UTF-8", result);
        result = DataUtil.validateCharset("UTF-8\"");
        assertEquals("UTF-8", result);
        result = DataUtil.validateCharset("UTF-8\'");
        assertEquals("UTF-8", result);
        result = DataUtil.validateCharset("gb2312");
        assertNull(result);
        result = DataUtil.validateCharset("Utf-8");
        assertNull(result);
        result = DataUtil.validateCharset("UTF-8\n");
        assertNull(result);
    }

    @Test
    public void testMimeBoundary() {
        String result = DataUtil.mimeBoundary();
        assertNotNull(result);
        assertEquals(DataUtil.boundaryLength, result.length());
    }

}