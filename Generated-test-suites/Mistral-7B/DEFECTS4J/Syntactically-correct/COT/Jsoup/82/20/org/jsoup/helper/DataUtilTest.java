import org.jsoup.helper.DataUtil;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File inputFile = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "/test";
        Document doc = DataUtil.load(inputFile, charsetName, baseUri);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String html = "<html><head><meta charset=\"UTF-16\"></head><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-16";
        String baseUri = "/test";
        Document doc = DataUtil.load(inputStream, charsetName, baseUri);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String html = "<html><head></head><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        String baseUri = "/test";
        Document doc = DataUtil.load(inputStream, null, baseUri);
        Assert.assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String html = "<html><head></head><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        String charsetName = "INVALID_CHARSET";
        String baseUri = "/test";
        DataUtil.load(inputStream, charsetName, baseUri);
    }

    @Test
    public void testParseInputStream() throws IOException {
        String html = "<html><head><meta charset=\"UTF-16\"></head><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-16";
        String baseUri = "/test";
        Document doc = DataUtil.parseInputStream(inputStream, charsetName, baseUri, Parser.htmlParser());
        Assert.assertNotNull(doc);
    }

    @Test
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String html = "<html><head></head><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        String charsetName = "INVALID_CHARSET";
        String baseUri = "/test";
        try {
            DataUtil.parseInputStream(inputStream, charsetName, baseUri, Parser.htmlParser());
            Assert.fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputData = "Hello, world!";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));
        OutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);

        String outputData = outputStream.toString(StandardCharsets.UTF_8.name());
        Assert.assertEquals(inputData, outputData);
    }

    @Test
    public void testParseInputStreamWithBom() throws IOException {
        String bomCharset = "UTF-32";
        String html = "\u0000\u0000\u00FE\u00FF<html><head><meta charset=\"UTF-16\"></head><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.parseInputStream(inputStream, bomCharset, "/test", Parser.htmlParser());
        Assert.assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testParseInputStreamWithInvalidBomCharset() throws IOException {
        String bomCharset = "UTF-42";
        String html = "\u0000\u0000\u00FE\u00FF<html><head><meta charset=\"UTF-16\"></head><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));

        try {
            DataUtil.parseInputStream(inputStream, bomCharset, "/test", Parser.htmlParser());
            Assert.fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer byteData = ByteBuffer.allocate(4);
        byteData.put((byte) 0x00);
        byteData.put((byte) 0x00);
        byteData.put((byte) 0xFE);
        byteData.put((byte) 0xFF);
        byteData.flip();

        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteData);
        Assert.assertNotNull(bomCharset);
        Assert.assertEquals("UTF-32", bomCharset.charset);
        Assert.assertFalse(bomCharset.offset);

        byteData = ByteBuffer.allocate(3);
        byteData.put((byte) 0xEF);
        byteData.put((byte) 0xBB);
        byteData.put((byte) 0xBF);
        byteData.flip();

        bomCharset = DataUtil.detectCharsetFromBom(byteData);
        Assert.assertNotNull(bomCharset);
        Assert.assertEquals("UTF-8", bomCharset.charset);
        Assert.assertTrue(bomCharset.offset);

        byteData = ByteBuffer.allocate(3);
        byteData.put((byte) 0xFE);
        byteData.put((byte) 0xFF);
        byteData.put((byte) 0x00);
        byteData.flip();

        bomCharset = DataUtil.detectCharsetFromBom(byteData);
        Assert.assertNull(bomCharset);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String inputData = "Hello, world!";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 0);

        byte[] expectedData = inputData.getBytes(StandardCharsets.UTF_8);
        byte[] actualData = new byte[byteBuffer.remaining()];
        byteBuffer.get(actualData);

        Assert.assertArrayEquals(expectedData, actualData);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-16";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals("UTF-16", charset);

        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertNull(charset);
    }

    @Test
    public void testValidateCharset() {
        String cs = "UTF-8";
        String validCharset = DataUtil.validateCharset(cs);
        Assert.assertEquals("UTF-8", validCharset);

        cs = "UTF-8";
        cs = cs.toUpperCase(Locale.ENGLISH);
        validCharset = DataUtil.validateCharset(cs);
        Assert.assertEquals("UTF-8", validCharset);

        cs = "INVALID_CHARSET";
        validCharset = DataUtil.validateCharset(cs);
        Assert.assertNull(validCharset);
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        Assert.assertNotNull(mimeBoundary);
        Assert.assertEquals(32, mimeBoundary.length());
    }
}