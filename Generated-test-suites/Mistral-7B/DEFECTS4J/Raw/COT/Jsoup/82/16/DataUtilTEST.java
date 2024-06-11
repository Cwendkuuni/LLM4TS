import org.jsoup.helper.DataUtil;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html"); // Replace with the actual file path
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        Document document = DataUtil.load(file, charsetName, baseUri);
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamNullCharset() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("test data".getBytes("UTF-8"));

        try {
            Document document = DataUtil.load(inputStream, null, null);
            Assert.assertNotNull(document);
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("charset arg must not be null", e.getMessage());
        }
    }

    @Test
    public void testLoadInputStreamEmptyInput() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);

        try {
            Document document = DataUtil.load(inputStream, "UTF-8", null);
            Assert.assertNotNull(document);
            Assert.assertEquals("<html></html>", document.html());
        } finally {
            inputStream.close();
        }
    }

    @Test
    public void testLoadInputStreamInvalidCharset() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("test data".getBytes("UTF-16BE"));

        try {
            Document document = DataUtil.load(inputStream, "invalidCharset", null);
            Assert.assertNull(document);
        } finally {
            inputStream.close();
        }
    }

    @Test
    public void testLoadInputStreamWithBaseUri() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("test data".getBytes("UTF-8"));
        String baseUri = "http://example.com/test";

        Document document = DataUtil.load(inputStream, "UTF-8", baseUri);
        Assert.assertNotNull(document);
        Assert.assertEquals("test data", document.text());
    }

    @Test
    public void testParseInputStreamNullCharset() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("test data".getBytes("UTF-8"));

        try {
            Document document = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
            Assert.assertNull(document);
        } finally {
            inputStream.close();
        }
    }

    @Test
    public void testParseInputStreamEmptyInput() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);

        Document document = DataUtil.parseInputStream(inputStream, "UTF-8", null, Parser.htmlParser());
        Assert.assertNotNull(document);
        Assert.assertEquals("<html></html>", document.html());

        inputStream.close();
    }

    @Test
    public void testParseInputStreamInvalidCharset() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("test data".getBytes("UTF-16BE"));

        try {
            Document document = DataUtil.parseInputStream(inputStream, "invalidCharset", null, Parser.htmlParser());
            Assert.assertNull(document);
        } finally {
            inputStream.close();
        }
    }

    @Test
    public void testParseInputStreamValidCharset() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("test data".getBytes("UTF-8"));

        Document document = DataUtil.parseInputStream(inputStream, "UTF-8", null, Parser.htmlParser());
        Assert.assertNotNull(document);
        Assert.assertEquals("test data", document.text());

        inputStream.close();
    }

    @Test
    public void testParseInputStreamWithBaseUri() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("test data".getBytes("UTF-8"));
        String baseUri = "http://example.com/test";

        Document document = DataUtil.parseInputStream(inputStream, "UTF-8", baseUri, Parser.htmlParser());
        Assert.assertNotNull(document);
        Assert.assertEquals("test data", document.text());

        inputStream.close();
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputData = "test input data";
        String outputData = "test output data";

        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes("UTF-8"));
        OutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);

        Assert.assertEquals(inputData, new String(outputStream.toByteArray(), "UTF-8"));

        inputStream.close();
        outputStream.close();
    }

    @Test
    public void testParseInputStreamWithUnsupportedCharset() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("test data".getBytes("UTF-16LE"));
        String charsetName = "UTF-8";
        String baseUri = null;

        try {
            Document document = DataUtil.parseInputStream(inputStream, charsetName, baseUri, Parser.htmlParser());
            Assert.assertNull(document);
        } finally {
            inputStream.close();
        }
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("test data".getBytes("UTF-8"));
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 1024);

        Assert.assertEquals(1024, byteBuffer.capacity());
        Assert.assertEquals("test data", Charset.forName("UTF-8").decode(byteBuffer).toString());

        inputStream.close();
    }

    @Test
    public void testReadToByteBufferEmptyInput() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 1024);

        Assert.assertEquals(0, byteBuffer.capacity());
        Assert.assertFalse(byteBuffer.hasRemaining());

        inputStream.close();
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String expectedCharset = "UTF-8";

        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals(expectedCharset, charset);
    }

    @Test
    public void testGetCharsetFromContentTypeNullInput() {
        String contentType = null;
        String expectedCharset = null;

        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals(expectedCharset, charset);
    }

    @Test
    public void testValidateCharsetNullInput() {
        String charset = null;
        String expectedCharset = null;

        String validatedCharset = DataUtil.validateCharset(charset);
        Assert.assertEquals(expectedCharset, validatedCharset);
    }

    @Test
    public void testValidateCharsetEmptyInput() {
        String charset = "";
        String expectedCharset = null;

        String validatedCharset = DataUtil.validateCharset(charset);
        Assert.assertEquals(expectedCharset, validatedCharset);
    }

    @Test
    public void testValidateCharsetSupportedCharset() {
        String charset = "UTF-8";
        String expectedCharset = "UTF-8";

        String validatedCharset = DataUtil.validateCharset(charset);
        Assert.assertEquals(expectedCharset, validatedCharset);
    }

    @Test
    public void testValidateCharsetUnsupportedCharset() {
        String charset = "invalidCharset";
        String expectedCharset = null;

        String validatedCharset = DataUtil.validateCharset(charset);
        Assert.assertEquals(expectedCharset, validatedCharset);
    }

    @Test
    public void testMimeBoundary() {
        String expectedBoundary = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        String boundary = DataUtil.mimeBoundary();

        Assert.assertEquals(expectedBoundary, boundary);
    }

    @Test(expected = AssertionError.class)
    public void testDetectCharsetFromBomInvalidInput() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();

        DataUtil.detectCharsetFromBom(byteBuffer);
    }

    @Test
    public void testDetectCharsetFromBomUTF16BE() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(2);
        byteBuffer.put((byte) 0xFE);
        byteBuffer.put((byte) 0xFF);

        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);

        Assert.assertEquals("UTF-16", bomCharset.charset);
        Assert.assertFalse(bomCharset.offset);
    }

    @Test
    public void testDetectCharsetFromBomUTF16LE() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(2);
        byteBuffer.put((byte) 0xFF);
        byteBuffer.put((byte) 0xFE);

        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);

        Assert.assertEquals("UTF-16", bomCharset.charset);
        Assert.assertTrue(bomCharset.offset);
    }

    @Test
    public void testDetectCharsetFromBomUTF8() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(3);
        byteBuffer.put((byte) 0xEF);
        byteBuffer.put((byte) 0xBB);
        byteBuffer.put((byte) 0xBF);

        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);

        Assert.assertEquals("UTF-8", bomCharset.charset);
        Assert.assertTrue(bomCharset.offset);
    }

    @Test
    public void testDetectCharsetFromBomUTF32() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(4);
        byteBuffer.put((byte) 0x00);
        byteBuffer.put((byte) 0x00);
        byteBuffer.put((byte) 0xFE);
        byteBuffer.put((byte) 0xFF);

        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);

        Assert.assertEquals("UTF-32", bomCharset.charset);
        Assert.assertFalse(bomCharset.offset);
    }

}