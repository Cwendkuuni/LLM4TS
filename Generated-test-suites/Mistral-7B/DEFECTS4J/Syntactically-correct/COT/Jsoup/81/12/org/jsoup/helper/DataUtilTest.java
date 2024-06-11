import org.junit.Assert;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File inputFile = new File("input.html");
        Document doc = DataUtil.load(inputFile, "UTF-8", "");
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String htmlContent = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, "UTF-8", "");
        Assert.assertNotNull(doc);
    }

    @Test
    public void testParseInputStream() throws IOException {
        String htmlContent = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseInputStream(inputStream, "UTF-8", "", Parser.htmlParser());
        Assert.assertNotNull(doc);
    }

    @Test
    public void testCrossStreams() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Test Input".getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);

        String result = outputStream.toString(StandardCharsets.UTF_8.toString());
        Assert.assertEquals("Test Input", result);
    }

    @Test(expected = IOException.class)
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String htmlContent = "<html><head><meta charset=\"InvalidCharset\"></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        DataUtil.parseInputStream(inputStream, "InvalidCharset", "", Parser.htmlParser());
    }

    @Test
    public void testParseInputStreamWithInvalidParser() throws IOException {
        String htmlContent = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseInputStream(inputStream, "UTF-8", "", Parser.xmlParser()); // Using an invalid parser for HTML content
        Assert.assertNull(doc);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String htmlContent = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        Assert.assertNotNull(byteBuffer);
    }

    @Test(expected = IOException.class)
    public void testReadToByteBufferWithInvalidInputStream() throws IOException {
        InputStream inputStream = null;
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals("UTF-8", charset);

        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertNull(charset);
    }

    @Test
    public void testValidateCharset() {
        String charset1 = "UTF-8";
        String charset2 = "InvalidCharset";
        String charset3 = "UTF-16";

        Assert.assertEquals(charset1, DataUtil.validateCharset(charset1));
        Assert.assertNull(DataUtil.validateCharset(charset2));
        Assert.assertEquals(charset3, DataUtil.validateCharset(charset3));
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        Assert.assertNotNull(boundary);
        Assert.assertTrue(boundary.length() > 0);
    }

    @Test
    public void testDetectCharsetFromBom() {
        String bomUtf8 = "\uFEFF";
        String bomUtf16LE = "\uFFFE\u0000";
        String bomUtf16BE = "\u0000\uFFFE";
        String bomUTF32LE = "\u0000\u0000\u0000\uFEFF";
        String bomUTF32BE = "\uFEFF\u0000\u0000\u0000";

        ByteBuffer byteBufferUtf8 = StandardCharsets.UTF_8.decode(bomUtf8);
        ByteBuffer byteBufferUtf16LE = StandardCharsets.UTF_16.decode(bomUtf16LE);
        ByteBuffer byteBufferUtf16BE = StandardCharsets.UTF_16.decodeOrdered(bomUtf16BE.getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteBufferUTF32LE = StandardCharsets.UTF_32.decode(bomUTF32LE);
        ByteBuffer byteBufferUTF32BE = StandardCharsets.UTF_32.decodeOrdered(bomUTF32BE.getBytes(StandardCharsets.UTF_8));

        BomCharset bomUtf8Result = DataUtil.detectCharsetFromBom(byteBufferUtf8);
        BomCharset bomUtf16LEResult = DataUtil.detectCharsetFromBom(byteBufferUtf16LE);
        BomCharset bomUtf16BEResult = DataUtil.detectCharsetFromBom(byteBufferUtf16BE);
        BomCharset bomUTF32LEResult = DataUtil.detectCharsetFromBom(byteBufferUTF32LE);
        BomCharset bomUTF32BEResult = DataUtil.detectCharsetFromBom(byteBufferUTF32BE);

        Assert.assertEquals("UTF-8", bomUtf8Result.charset);
        Assert.assertEquals(false, bomUtf8Result.offset);
        Assert.assertEquals("UTF-16", bomUtf16LEResult.charset);
        Assert.assertEquals(false, bomUtf16LEResult.offset);
        Assert.assertEquals("UTF-16", bomUtf16BEResult.charset);
        Assert.assertEquals(true, bomUtf16BEResult.offset);
        Assert.assertEquals("UTF-32", bomUTF32LEResult.charset);
        Assert.assertEquals(false, bomUTF32LEResult.offset);
        Assert.assertEquals("UTF-32", bomUTF32BEResult.charset);
        Assert.assertEquals(true, bomUTF32BEResult.offset);
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        Assert.assertNotNull(byteBuffer);
        Assert.assertEquals(0, byteBuffer.limit());
    }
}