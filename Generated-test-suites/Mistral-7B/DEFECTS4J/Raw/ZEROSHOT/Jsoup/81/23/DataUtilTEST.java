import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html"); // Replace with the path to your test file
        Document document = DataUtil.load(file, "UTF-8", null);
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String htmlContent = "<html><body><h1>Test</h1></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(inputStream, "UTF-8", null);
        Assert.assertNotNull(document);
    }

    @Test
    public void testParseInputStream() throws IOException {
        String htmlContent = "<html><body><h1>Test</h1></body></html>";
        byte[] htmlBytes = htmlContent.getBytes(StandardCharsets.UTF_8);
        InputStream inputStream = new ByteArrayInputStream(htmlBytes);
        Document document = DataUtil.parseInputStream(inputStream, "UTF-8", null, DataUtil.Parser.htmlParser());
        Assert.assertNotNull(document);
    }

    @Test
    public void testCrossStreams() throws IOException {
        File inputFile = new File("input.txt");
        File outputFile = new File("output.txt");
        InputStream inputStream = new FileInputStream(inputFile);
        OutputStream outputStream = new FileOutputStream(outputFile);

        DataUtil.crossStreams(inputStream, outputStream);

        Assert.assertTrue(outputFile.length() > 0);
    }

    @Test
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String invalidHtmlContent = "<html><head><meta charset='X-TEST'></html><body></body>";
        byte[] invalidHtmlBytes = invalidHtmlContent.getBytes(StandardCharsets.UTF_8);
        InputStream inputStream = new ByteArrayInputStream(invalidHtmlBytes);

        try {
            DataUtil.parseInputStream(inputStream, "X-TEST", null, DataUtil.Parser.htmlParser());
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Invalid charset: X-TEST", e.getMessage());
        }
    }

    @Test
    public void testParseInputStreamWithDefaultCharset() throws IOException {
        String htmlContent = "<html><body><h1>Test</h1></body></html>";
        byte[] htmlBytes = htmlContent.getBytes(StandardCharsets.UTF_8);
        InputStream inputStream = new ByteArrayInputStream(htmlBytes);

        Document document = DataUtil.parseInputStream(inputStream, null, null, DataUtil.Parser.htmlParser());
        Assert.assertNotNull(document);
    }

    @Test
    public void testParseInputStreamWithCustomParser() throws IOException {
        String xmlContent = "<xml><data></data></xml>";
        byte[] xmlBytes = xmlContent.getBytes(StandardCharsets.UTF_8);
        InputStream inputStream = new ByteArrayInputStream(xmlBytes);

        Document document = DataUtil.parseInputStream(inputStream, "UTF-8", null, DataUtil.Parser.xmlParser());
        Assert.assertNotNull(document);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String testContent = "Test Content";
        byte[] buffer = testContent.getBytes(StandardCharsets.UTF_8);
        InputStream inputStream = new ByteArrayInputStream(buffer);

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        Assert.assertNotNull(byteBuffer);
        Assert.assertEquals(testContent, byteBuffer.asCharBuffer().toString());
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        String testContent = "Test Content";
        byte[] buffer = testContent.getBytes(StandardCharsets.UTF_8);
        InputStream inputStream = new ByteArrayInputStream(buffer);

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 10);
        Assert.assertNotNull(byteBuffer);
        Assert.assertEquals(10, byteBuffer.limit());
        Assert.assertEquals(testContent.substring(0, 10), byteBuffer.asCharBuffer().toString());
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        String testContent = "Test Content";
        File file = new File("test.txt");
        FileUtils.writeStringToFile(file, testContent, StandardCharsets.UTF_8);

        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
        Assert.assertNotNull(byteBuffer);
        Assert.assertEquals(testContent, byteBuffer.asCharBuffer().toString());

        FileUtils.deleteQuietly(file);
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer emptyByteBuffer = DataUtil.emptyByteBuffer();
        Assert.assertNotNull(emptyByteBuffer);
        Assert.assertEquals(0, emptyByteBuffer.limit());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType1 = "text/html; charset=UTF-8";
        String contentType2 = "text/html; charset=gb2312";
        String contentType3 = "text/html";

        String charset1 = DataUtil.getCharsetFromContentType(contentType1);
        String charset2 = DataUtil.getCharsetFromContentType(contentType2);
        String charset3 = DataUtil.getCharsetFromContentType(contentType3);

        Assert.assertEquals("UTF-8", charset1);
        Assert.assertEquals("gb2312", charset2);
        Assert.assertNull(charset3);
    }

    @Test
    public void testValidateCharset() {
        String charset1 = "UTF-8";
        String charset2 = "gb2312";
        String charset3 = "EUC-JP";
        String charset4 = "InvalidCharset";

        String validCharset1 = DataUtil.validateCharset(charset1);
        String validCharset2 = DataUtil.validateCharset(charset2);
        String validCharset3 = DataUtil.validateCharset(charset3);
        String validCharset4 = DataUtil.validateCharset(charset4);

        Assert.assertEquals("UTF-8", validCharset1);
        Assert.assertEquals("gb2312", validCharset2);
        Assert.assertEquals("EUC-JP", validCharset3);
        Assert.assertNull(validCharset4);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        Assert.assertNotNull(boundary);
        Assert.assertEquals(DataUtil.boundaryLength, boundary.length());
    }

    @Test
    public void testDetectCharsetFromBom() {
        String bomUtf16 = "EF BB FE";
        String bomUtf32 = "00 00 FE FF";
        String bomUtf8 = "EF BBBF";

        ByteBuffer byteBufferUtf16 = ByteBuffer.wrap(bomUtf16.getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteBufferUtf32 = ByteBuffer.wrap(bomUtf32.getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteBufferUtf8 = ByteBuffer.wrap(bomUtf8.getBytes(StandardCharsets.UTF_8));

        DataUtil.BomCharset bomCharsetUtf16 = DataUtil.detectCharsetFromBom(byteBufferUtf16);
        DataUtil.BomCharset bomCharsetUtf32 = DataUtil.detectCharsetFromBom(byteBufferUtf32);
        DataUtil.BomCharset bomCharsetUtf8 = DataUtil.detectCharsetFromBom(byteBufferUtf8);

        Assert.assertEquals("UTF-16", bomCharsetUtf16.charset);
        Assert.assertEquals(false, bomCharsetUtf16.offset);
        Assert.assertEquals("UTF-32", bomCharsetUtf32.charset);
        Assert.assertEquals(false, bomCharsetUtf32.offset);
        Assert.assertEquals("UTF-8", bomCharsetUtf8.charset);
        Assert.assertEquals(true, bomCharsetUtf8.offset);
    }
}