import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

public class DataUtilTest {

    private static final String UTF8_CHARSET = "UTF-8";
    private static final String INVALID_CHARSET = "invalid_charset";

    private DataUtil dataUtil;

    @Before
    public void setup() {
        dataUtil = new DataUtil();
    }

    @Test
    public void testLoadValidFile() throws IOException {
        File file = new File("src/test/resources/test.html");
        Document doc = dataUtil.load(file, UTF8_CHARSET, "");
        Assert.assertNotNull(doc);
        Assert.assertEquals("Test Document", doc.title());
    }

    @Test(expected = IOException.class)
    public void testLoadNonExistingFile() throws IOException {
        File file = new File("src/test/resources/non_existing_file.html");
        dataUtil.load(file, UTF8_CHARSET, "");
    }

    @Test
    public void testLoadEmptyFile() throws IOException {
        File file = new File("src/test/resources/empty.html");
        Document doc = dataUtil.load(file, UTF8_CHARSET, "");
        Assert.assertNotNull(doc);
        Assert.assertEquals("", doc.title());
    }

    @Test
    public void testLoadValidInputStream() throws IOException {
        InputStream inputStream = new FileInputStream("src/test/resources/test.html");
        Document doc = dataUtil.load(inputStream, UTF8_CHARSET, "");
        Assert.assertNotNull(doc);
        Assert.assertEquals("Test Document", doc.title());
    }

    @Test(expected = IOException.class)
    public void testLoadInvalidInputStream() throws IOException {
        InputStream inputStream = new FileInputStream("src/test/resources/non_existing_file.html");
        dataUtil.load(inputStream, UTF8_CHARSET, "");
    }

    @Test
    public void testLoadNullInputStream() throws IOException {
        Document doc = dataUtil.load(null, UTF8_CHARSET, "");
        Assert.assertNotNull(doc);
        Assert.assertEquals("", doc.title());
    }

    @Test
    public void testLoadValidInputStreamWithParser() throws IOException {
        InputStream inputStream = new FileInputStream("src/test/resources/test.html");
        Document doc = dataUtil.load(inputStream, UTF8_CHARSET, "", Parser.htmlParser());
        Assert.assertNotNull(doc);
        Assert.assertEquals("Test Document", doc.title());
    }

    @Test
    public void testCrossStreams() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream("Test input".getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        dataUtil.crossStreams(in, out);

        ByteArrayInputStream resultStream = new ByteArrayInputStream(out.toByteArray());
        BufferedReader reader = new BufferedReader(new InputStreamReader(resultStream));
        String line = reader.readLine();
        Assert.assertEquals("Test input", line);
    }

    @Test
    public void testParseInputStreamWithValidCharset() throws IOException {
        InputStream inputStream = new FileInputStream("src/test/resources/test.html");
        Document doc = dataUtil.parseInputStream(inputStream, UTF8_CHARSET, "", Parser.htmlParser());
        Assert.assertNotNull(doc);
        Assert.assertEquals("Test Document", doc.title());
    }

    @Test(expected = IOException.class)
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        InputStream inputStream = new FileInputStream("src/test/resources/test.html");
        dataUtil.parseInputStream(inputStream, INVALID_CHARSET, "", Parser.htmlParser());
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        InputStream inStream = new FileInputStream("src/test/resources/test.html");
        int maxSize = 100;
        ByteBuffer byteBuffer = dataUtil.readToByteBuffer(inStream, maxSize);
        Assert.assertNotNull(byteBuffer);
        Assert.assertTrue(byteBuffer.capacity() <= maxSize);
    }

    @Test
    public void testReadToByteBufferWithoutMaxSize() throws IOException {
        InputStream inStream = new FileInputStream("src/test/resources/test.html");
        ByteBuffer byteBuffer = dataUtil.readToByteBuffer(inStream);
        Assert.assertNotNull(byteBuffer);
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File file = new File("src/test/resources/test.html");
        ByteBuffer byteBuffer = dataUtil.readFileToByteBuffer(file);
        Assert.assertNotNull(byteBuffer);
        Assert.assertTrue(byteBuffer.capacity() > 0);
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = dataUtil.emptyByteBuffer();
        Assert.assertNotNull(byteBuffer);
        Assert.assertEquals(0, byteBuffer.capacity());
    }

    @Test
    public void testGetCharsetFromContentTypeWithValidContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = dataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidContentType() {
        String contentType = "text/html";
        String charset = dataUtil.getCharsetFromContentType(contentType);
        Assert.assertNull(charset);
    }

    @Test
    public void testValidateCharsetWithValidCharset() {
        String charset = "UTF-8";
        String validatedCharset = dataUtil.validateCharset(charset);
        Assert.assertNotNull(validatedCharset);
        Assert.assertEquals("UTF-8", validatedCharset);
    }

    @Test
    public void testValidateCharsetWithInvalidCharset() {
        String charset = "invalid_charset";
        String validatedCharset = dataUtil.validateCharset(charset);
        Assert.assertNull(validatedCharset);
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = dataUtil.mimeBoundary();
        Assert.assertNotNull(mimeBoundary);
        Assert.assertEquals(32, mimeBoundary.length());
    }

    @Test
    public void testDetectCharsetFromBomWithValidBom() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(4);
        byte[] bom = { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF };
        byteBuffer.put(bom);
        byteBuffer.rewind();

        DataUtil.BomCharset bomCharset = dataUtil.detectCharsetFromBom(byteBuffer);
        Assert.assertNotNull(bomCharset);
        Assert.assertEquals("UTF-8", bomCharset.charset);
    }

    @Test
    public void testDetectCharsetFromBomWithInvalidBom() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(4);
        byte[] bom = { (byte) 0xAA, (byte) 0xBB, (byte) 0xCC };
        byteBuffer.put(bom);
        byteBuffer.rewind();

        DataUtil.BomCharset bomCharset = dataUtil.detectCharsetFromBom(byteBuffer);
        Assert.assertNull(bomCharset);
    }
}