package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head><meta charset=\"UTF-16\"></meta></head><body>Hello World</body></html>";
        File inputFile = new File("input.xml");
        FileUtil.writeStringToFile(inputFile, fileContent, "UTF-8");

        Document document = DataUtil.load(inputFile, null, null);
        assertEquals("UTF-8", document.outputSettings().charset());
        assertEquals("Hello World", document.body().text());
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head><meta charset=\"UTF-16\"></meta></head><body>Hello World</body></html>";
        File inputFile = new File("input.xml");
        FileUtil.writeStringToFile(inputFile, fileContent, "UTF-8");

        Document document = DataUtil.load(inputFile, "UTF-16", null);
        assertEquals("UTF-16", document.outputSettings().charset());
        assertEquals("Hello World", document.body().text());
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head><meta charset=\"UTF-16\"></meta></head><body>Hello World</body></html>";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes("UTF-8"));

        Document document = DataUtil.load(inputStream, null, null);
        assertEquals("UTF-8", document.outputSettings().charset());
        assertEquals("Hello World", document.body().text());
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        String content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head><meta charset=\"UTF-16\"></meta></head><body>Hello World</body></html>";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes("UTF-8"));

        Document document = DataUtil.load(inputStream, "UTF-16", null);
        assertEquals("UTF-16", document.outputSettings().charset());
        assertEquals("Hello World", document.body().text());
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String content = "<html><head><title>Test</title></head><body>Hello World</body></html>";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes("UTF-8"));

        Document document = DataUtil.load(inputStream, null, null, Parser.xmlParser());
        assertEquals("UTF-8", document.outputSettings().charset());
        assertEquals("Test", document.title().text());
        assertEquals("Hello World", document.body().text());
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputData = "Hello World";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes("UTF-8"));
        OutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);

        byte[] outputData = outputStream.toByteArray();
        assertEquals(inputData, new String(outputData, "UTF-8"));
    }

    @Test
    public void testParseByteDataWithDefaultCharset() throws IOException {
        String content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head><meta charset=\"UTF-16\"></meta></head><body>Hello World</body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(content.getBytes("UTF-8"));

        Document document = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        assertEquals("UTF-8", document.outputSettings().charset());
        assertEquals("Hello World", document.body().text());
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() throws IOException {
        String content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head><meta charset=\"UTF-16\"></meta></head><body>Hello World</body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(content.getBytes("UTF-8"));

        String charset = "UTF-16";
        Document document = DataUtil.parseByteData(byteData, charset, null, Parser.htmlParser());
        assertEquals(charset, document.outputSettings().charset());
        assertEquals("Hello World", document.body().text());
    }

    @Test
    public void testDetectCharsetFromBomWithUTF8() throws IOException {
        String content = "\uFEFFHello World\uFEFF";
        ByteBuffer byteData = ByteBuffer.wrap(content.getBytes("UTF-8"));

        String detectedCharset = DataUtil.detectCharsetFromBom(byteData, null);
        assertEquals("UTF-8", detectedCharset);
    }

    @Test
    public void testDetectCharsetFromBomWithUTF16BE() throws IOException {
        String content = "\u00FE\u00FFHello World\u00FE\u00FF";
        ByteBuffer byteData = ByteBuffer.wrap(content.getBytes("UTF-8"));

        String detectedCharset = DataUtil.detectCharsetFromBom(byteData, null);
        assertEquals("UTF-16BE", detectedCharset);
    }

    @Test
    public void testDetectCharsetFromBomWithUTF16LE() throws IOException {
        String content = "\u00FF\u00FEHello World\u00FF\u00FE";
        ByteBuffer byteData = ByteBuffer.wrap(content.getBytes("UTF-8"));

        String detectedCharset = DataUtil.detectCharsetFromBom(byteData, null);
        assertEquals("UTF-16LE", detectedCharset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithUTF8() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithUnknownCharset() {
        String contentType = "text/html; charset=X-FOO-BAR";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testValidateCharsetWithUTF8() {
        String charset = "UTF-8";
        String validatedCharset = DataUtil.validateCharset(charset);
        assertEquals("UTF-8", validatedCharset);
    }

    @Test
    public void testValidateCharsetWithUnknownCharset() {
        String charset = "X-FOO-BAR";
        String validatedCharset = DataUtil.validateCharset(charset);
        assertNull(validatedCharset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertTrue(boundary.length() > 0);
    }
}