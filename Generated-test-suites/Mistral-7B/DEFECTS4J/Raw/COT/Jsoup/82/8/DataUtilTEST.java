package org.jsoup.helper;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertNull;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File inputFile = new File("test.html");
        String charsetName = StandardCharsets.UTF_8.name();
        String baseUri = "http://example.com";

        Document document = DataUtil.load(inputFile, charsetName, baseUri);
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String htmlContent = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        String charsetName = StandardCharsets.UTF_8.name();
        String baseUri = "http://example.com";

        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String htmlContent = "<html><head><meta charset=\"GBK\"></head><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        String charsetName = StandardCharsets.UTF_8.name();
        String baseUri = "http://example.com";

        try {
            Document document = DataUtil.load(inputStream, charsetName, baseUri);
            Assert.fail();
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testLoadInputStreamWithEmptyInput() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        String charsetName = StandardCharsets.UTF_8.name();
        String baseUri = "http://example.com";

        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        Assert.assertNotNull(document);
        Assert.assertTrue(document.childNodeSize() == 0);
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputData = "Input data";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));
        OutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);
        String outputData = outputStream.toString(StandardCharsets.UTF_8.name());
        Assert.assertEquals(inputData, outputData);
    }

    @Test
    public void testParseInputStream() throws IOException {
        String htmlContent = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello World</h1></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        String charsetName = StandardCharsets.UTF_8.name();
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.parseInputStream(inputStream, charsetName, baseUri, parser);
        Assert.assertNotNull(document);
    }

    @Test
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String htmlContent = "<html><head><meta charset=\"GBK\"></head><body><h1>Hello World</h1></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        String charsetName = StandardCharsets.UTF_8.name();
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        try {
            Document document = DataUtil.parseInputStream(inputStream, charsetName, baseUri, parser);
            Assert.fail();
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testParseInputStreamWithNoContentType() throws IOException {
        String htmlContent = "<html><body><h1>Hello World</h1></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        String charsetName = StandardCharsets.UTF_8.name();
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.parseInputStream(inputStream, charsetName, baseUri, parser);
        Assert.assertNotNull(document);
    }

    @Test
    public void testParseInputStreamWithXmlDeclaration() throws IOException {
        String xmlContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello World</h1></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(xmlContent.getBytes(StandardCharsets.UTF_8));
        String charsetName = StandardCharsets.UTF_8.name();
        String baseUri = "http://example.com";
        Parser parser = Parser.xmlParser();

        Document document = DataUtil.parseInputStream(inputStream, charsetName, baseUri, parser);
        Assert.assertNotNull(document);
    }

    @Test
    public void testParseInputStreamWithInvalidXmlDeclaration() throws IOException {
        String xmlContent = "<?xml version=\"1.0\" encoding=\"GBK\"?>\r\n<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello World</h1></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(xmlContent.getBytes(StandardCharsets.UTF_8));
        String charsetName = StandardCharsets.UTF_8.name();
        String baseUri = "http://example.com";
        Parser parser = Parser.xmlParser();

        Document document = DataUtil.parseInputStream(inputStream, charsetName, baseUri, parser);
        Assert.assertNull(document);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String text = "Hello World";
        InputStream inputStream = new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 0);

        byte[] expectedBytes = text.getBytes(StandardCharsets.UTF_8);
        byte[] actualBytes = new byte[byteBuffer.remaining()];
        byteBuffer.get(actualBytes);

        assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test
    public void testReadToByteBufferWithInvalidCharset() throws IOException {
        String text = "Hello World";
        InputStream inputStream = new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, StandardCharsets.GBK.name());

        Assert.assertEquals(0, byteBuffer.limit());
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
        String contentType = "text/html; charset=GBK";
        String expectedCharset = null;

        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals(expectedCharset, charset);
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        String validatedCharset = DataUtil.validateCharset(charset);
        Assert.assertEquals(charset, validatedCharset);

        String invalidCharset = "invalid_charset";
        String validatedInvalidCharset = DataUtil.validateCharset(invalidCharset);
        Assert.assertNull(validatedInvalidCharset);
    }

    @Test
    public void testMimeBoundary() {
        String expectedBoundary = "yM8D-7Kjt6b_Jk9a-F8W";
        String boundary = DataUtil.mimeBoundary();

        Assert.assertEquals(expectedBoundary, boundary);
    }

    @Test
    public void testDetectCharsetFromBom() {
        String bomUtf8 = "\uFEFFHello World";
        ByteBuffer byteBufferUtf8 = ByteBuffer.wrap(bomUtf8.getBytes(StandardCharsets.UTF_8));
        DataUtil.BomCharset expectedBomUtf8 = new DataUtil.BomCharset("UTF-8", true);
        DataUtil.BomCharset bomUtf8Detected = DataUtil.detectCharsetFromBom(byteBufferUtf8);

        Assert.assertEquals(expectedBomUtf8.charset, bomUtf8Detected.charset);
        Assert.assertEquals(expectedBomUtf8.offset, bomUtf8Detected.offset);

        String bomUtf16 = "\uFFFEHello World";
        ByteBuffer byteBufferUtf16 = ByteBuffer.wrap(bomUtf16.getBytes(StandardCharsets.UTF_16BE));
        DataUtil.BomCharset expectedBomUtf16 = new DataUtil.BomCharset("UTF-16", false);
        DataUtil.BomCharset bomUtf16Detected = DataUtil.detectCharsetFromBom(byteBufferUtf16);

        Assert.assertEquals(expectedBomUtf16.charset, bomUtf16Detected.charset);
        Assert.assertEquals(expectedBomUtf16.offset, bomUtf16Detected.offset);

        String bomUtf32 = "\uFEFF\u0000\u0000Hello World";
        ByteBuffer byteBufferUtf32 = ByteBuffer.wrap(bomUtf32.getBytes(StandardCharsets.UTF_32BE));
        DataUtil.BomCharset expectedBomUtf32 = new DataUtil.BomCharset("UTF-32", false);
        DataUtil.BomCharset bomUtf32Detected = DataUtil.detectCharsetFromBom(byteBufferUtf32);

        Assert.assertEquals(expectedBomUtf32.charset, bomUtf32Detected.charset);
        Assert.assertEquals(expectedBomUtf32.offset, bomUtf32Detected.offset);

        String bomUnknown = "UnknownBOMHello World";
        ByteBuffer byteBufferUnknown = ByteBuffer.wrap(bomUnknown.getBytes(StandardCharsets.UTF_8));
        DataUtil.BomCharset bomUnknownDetected = DataUtil.detectCharsetFromBom(byteBufferUnknown);

        Assert.assertNull(bomUnknownDetected);
    }
}