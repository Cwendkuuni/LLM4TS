import org.jsoup.helper.DataUtil;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertNotNull;

public class DataUtilTest {

    

    @Test
    public void testLoadFileDocument() throws IOException {
        File inputFile = new File("example.html");
        String charsetName = StandardCharsets.UTF_8.name();
        String baseUri = "http://example.com";
        Document document = DataUtil.load(inputFile, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamDocument() throws IOException {
        File inputFile = new File("example.html");
        InputStream inputStream = new FileInputStream(inputFile);
        String charsetName = StandardCharsets.UTF_8.name();
        String baseUri = "http://example.com";
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamDocumentWithCustomParser() throws IOException {
        File inputFile = new File("example.xml");
        InputStream inputStream = new FileInputStream(inputFile);
        String charsetName = StandardCharsets.UTF_8.name();
        String baseUri = "http://example.com";
        Document document = DataUtil.load(inputStream, charsetName, baseUri, DataUtil.Parser.xmlParser());
        assertNotNull(document);
    }

    @Test
    public void testCrossStreams() throws IOException {
        File inputFile = new File("example.html");
        File outputFile = new File("output.html");
        InputStream inputStream = new FileInputStream(inputFile);
        OutputStream outputStream = new FileOutputStream(outputFile);

        DataUtil.crossStreams(inputStream, outputStream);

        Assert.assertTrue(outputFile.length() > 0);

        inputStream.close();
        outputStream.close();
    }

    @Test
    public void testParseInputStreamDocumentWithEmptyInput() throws IOException {
        InputStream inputStream = new FileInputStream(new File("empty.html"));
        String charsetName = StandardCharsets.UTF_8.name();
        String baseUri = "http://example.com";
        Document document = DataUtil.parseInputStream(inputStream, charsetName, baseUri, DataUtil.Parser.htmlParser());
        assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testParseInputStreamDocumentWithInvalidCharset() throws IOException {
        InputStream inputStream = new FileInputStream(new File("example.html"));
        String charsetName = "InvalidCharset";
        String baseUri = "http://example.com";
        DataUtil.parseInputStream(inputStream, charsetName, baseUri, DataUtil.Parser.htmlParser());
    }

    @Test(expected = IOException.class)
    public void testParseInputStreamDocumentWithEmptyBuffer() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        String charsetName = StandardCharsets.UTF_8.name();
        String baseUri = "http://example.com";
        DataUtil.parseInputStream(inputStream, charsetName, baseUri, DataUtil.Parser.htmlParser());
    }

    @Test
    public void testReadToByteBufferWithValidSize() throws IOException {
        File inputFile = new File("example.html");
        InputStream inputStream = new FileInputStream(inputFile);
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 500);
        Assert.assertNotNull(byteBuffer);
    }

    @Test(expected = IOException.class)
    public void testReadToByteBufferWithNegativeSize() throws IOException {
        File inputFile = new File("example.html");
        InputStream inputStream = new FileInputStream(inputFile);
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, -1);
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File file = new File("example.html");
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
        Assert.assertNotNull(byteBuffer);
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        Assert.assertNotNull(byteBuffer);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String expectedCharset = "UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals(expectedCharset, charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidCharset() {
        String contentType = "text/html; charset=InvalidCharset";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertNull(charset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        Assert.assertNotNull(boundary);
    }

    @Test
    public void testDetectCharsetFromBom() {
        byte[] bomUtf32 = {0, 0, (byte) 0xFE, (byte) 0xFF};
        ByteBuffer byteDataUtf32 = ByteBuffer.wrap(bomUtf32);
        BomCharset expectedBomCharsetUtf32 = new BomCharset("UTF-32", false);
        BomCharset bomCharsetUtf32 = DataUtil.detectCharsetFromBom(byteDataUtf32);
        Assert.assertEquals(expectedBomCharsetUtf32.charset, bomCharsetUtf32.charset);
        Assert.assertEquals(expectedBomCharsetUtf32.offset, bomCharsetUtf32.offset);

        byte[] bomUtf16BE = {(byte) 0xFE, (byte) 0xFF, 0, 0};
        ByteBuffer byteDataUtf16BE = ByteBuffer.wrap(bomUtf16BE);
        BomCharset expectedBomCharsetUtf16BE = new BomCharset("UTF-16", false);
        BomCharset bomCharsetUtf16BE = DataUtil.detectCharsetFromBom(byteDataUtf16BE);
        Assert.assertEquals(expectedBomCharsetUtf16BE.charset, bomCharsetUtf16BE.charset);
        Assert.assertEquals(expectedBomCharsetUtf16BE.offset, bomCharsetUtf16BE.offset);

        byte[] bomUtf8 = {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};
        ByteBuffer byteDataUtf8 = ByteBuffer.wrap(bomUtf8);
        BomCharset expectedBomCharsetUtf8 = new BomCharset("UTF-8", true);
        BomCharset bomCharsetUtf8 = DataUtil.detectCharsetFromBom(byteDataUtf8);
        Assert.assertEquals(expectedBomCharsetUtf8.charset, bomCharsetUtf8.charset);
        Assert.assertEquals(expectedBomCharsetUtf8.offset, bomCharsetUtf8.offset);

        byte[] bomInvalid = {(byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04};
        ByteBuffer byteDataInvalid = ByteBuffer.wrap(bomInvalid);
        Assert.assertNull(DataUtil.detectCharsetFromBom(byteDataInvalid));
    }

    
}