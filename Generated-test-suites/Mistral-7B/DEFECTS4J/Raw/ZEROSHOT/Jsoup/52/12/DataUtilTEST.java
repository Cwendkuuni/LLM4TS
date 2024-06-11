package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";
        File file = new File("testFile.html");
        Document doc = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithNullFile() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";
        File nullFile = null;
        Document doc = DataUtil.load(nullFile, charsetName, baseUri);
        assertNull(doc);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";
        String html = "<html><head><meta charset=\"UTF-16\"></head><body>Test</body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        Document doc = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(doc);
        assertEquals("UTF-16", doc.outputSettings().charset());
    }

    @Test
    public void testLoadInputStreamWithNullCharsetName() throws IOException {
        String baseUri = "http://example.com/";
        String html = "<html><head><meta charset=\"UTF-16\"></head><body>Test</body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        Document doc = DataUtil.load(inputStream, null, baseUri);
        assertNotNull(doc);
        assertEquals("UTF-8", doc.outputSettings().charset()); // default charset is UTF-8
    }

    @Test
    public void testLoadInputStreamWithInvalidCharsetName() throws IOException {
        String baseUri = "http://example.com/";
        String html = "<html><head><meta charset=\"UTF-16\"></head><body>Test</body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        Document doc = DataUtil.load(inputStream, "invalidCharset", baseUri);
        assertNotNull(doc);
        assertEquals("UTF-8", doc.outputSettings().charset()); // default charset is UTF-8
    }

    @Test
    public void testParseByteData() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";
        String html = "<html><head><meta charset=\"UTF-16\"></head><body>Test</body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(html.getBytes());
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        assertNotNull(doc);
        assertEquals("UTF-16", doc.outputSettings().charset());
    }

    @Test
    public void testParseByteDataWithNullCharsetName() throws IOException {
        String baseUri = "http://example.com/";
        String html = "<html><head><meta charset=\"UTF-16\"></head><body>Test</body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(html.getBytes());
        Document doc = DataUtil.parseByteData(byteData, null, baseUri, Parser.htmlParser());
        assertNotNull(doc);
        assertEquals("UTF-8", doc.outputSettings().charset()); // default charset is UTF-8
    }

    @Test
    public void testParseByteDataWithInvalidCharsetName() throws IOException {
        String baseUri = "http://example.com/";
        String html = "<html><head><meta charset=\"UTF-16\"></head><body>Test</body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(html.getBytes());
        Document doc = DataUtil.parseByteData(byteData, "invalidCharset", baseUri, Parser.htmlParser());
        assertNotNull(doc);
        assertEquals("UTF-8", doc.outputSettings().charset()); // default charset is UTF-8
    }

    @Test
    public void testParseByteDataWithEmptyByteBuffer() {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";
        ByteBuffer byteData = DataUtil.emptyByteBuffer();
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        assertNull(doc);
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputFilePath = "input.txt";
        String outputFilePath = "output.txt";
        File inputFile = new File(inputFilePath);
        File outputFile = new File(outputFilePath);
        InputStream inputStream = new FileInputStream(inputFile);
        OutputStream outputStream = new FileOutputStream(outputFile);
        DataUtil.crossStreams(inputStream, outputStream);
        assertEquals(inputFile.length(), outputFile.length());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=EUC-JP";
        String expectedCharset = "EUC-JP";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(expectedCharset, charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithNullContentType() {
        String contentType = null;
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        String validatedCharset = DataUtil.validateCharset(charset);
        assertEquals(charset, validatedCharset);
    }

    @Test
    public void testValidateCharsetWithNullCharset() {
        String charset = null;
        String validatedCharset = DataUtil.validateCharset(charset);
        assertNull(validatedCharset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertNotNull(boundary);
        int boundaryLength = boundary.length();
        assertTrue(boundaryLength > 0);
        assertTrue(boundaryLength <= 32);
    }

    @Test
    public void testDetectCharsetFromBom() {
        String charsetName = "UTF-8";
        ByteBuffer byteData = ByteBuffer.wrap(new byte[]{0x00, 0x00, (byte) 0xFE, (byte) 0xFF});
        String detectedCharset = DataUtil.detectCharsetFromBom(byteData, charsetName);
        assertEquals("UTF-32", detectedCharset);

        byteData.rewind();
        byteData.put(new byte[]{0xFF, 0xFE, 0x00, 0x00});
        detectedCharset = DataUtil.detectCharsetFromBom(byteData, charsetName);
        assertEquals("UTF-16", detectedCharset);

        byteData.rewind();
        byteData.put(new byte[]{0xEF, 0xBB, 0xBF});
        detectedCharset = DataUtil.detectCharsetFromBom(byteData, charsetName);
        assertEquals("UTF-8", detectedCharset);

        byteData.rewind();
        detectedCharset = DataUtil.detectCharsetFromBom(byteData, charsetName);
        assertNull(detectedCharset);
    }
}