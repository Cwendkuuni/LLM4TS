package org.jsoup.helper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import static org.junit.Assert.*;

public class DataUtilTest {
    private Document document;

    @Before
    public void setUp() throws Exception {
        String html = "<html><head><title>Test Page</title></head><body><h1>Hello World</h1></body></html>";
        document = Jsoup.parse(html);
    }

    @After
    public void tearDown() throws Exception {
        // Clean up any resources here
    }

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        Document loadedDoc = DataUtil.load(file, "UTF-8", "http://example.com");
        assertEquals(document, loadedDoc);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(document.outerHtml().getBytes(Charset.forName("UTF-8")));
        Document loadedDoc = DataUtil.load(inputStream, "UTF-8", "http://example.com");
        assertEquals(document, loadedDoc);
    }

    @Test
    public void testLoadWithParser() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(document.outerHtml().getBytes(Charset.forName("UTF-8")));
        Document loadedDoc = DataUtil.load(inputStream, "UTF-8", "http://example.com", Parser.htmlParser());
        assertEquals(document, loadedDoc);
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream inStream = new ByteArrayInputStream("Hello, World!".getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(inStream, outStream);
        assertEquals("Hello, World!", outStream.toString(StandardCharsets.UTF_8.name()));
    }

    @Test
    public void testParseByteData() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(document.outerHtml().getBytes(Charset.forName("UTF-8")));
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        Document parsedDoc = DataUtil.parseByteData(byteBuffer, "UTF-8", "http://example.com", Parser.htmlParser());
        assertEquals(document, parsedDoc);
    }

    @Test
    public void testReadToByteBufferUnlimited() throws IOException {
        byte[] expectedBytes = document.outerHtml().getBytes(Charset.forName("UTF-8"));
        InputStream inputStream = new ByteArrayInputStream(expectedBytes);
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 0);
        byte[] actualBytes = new byte[byteBuffer.remaining()];
        byteBuffer.get(actualBytes);
        assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test
    public void testReadToByteBufferCapped() throws IOException {
        byte[] expectedBytes = "Hello, World!".getBytes(Charset.forName("UTF-8"));
        InputStream inputStream = new ByteArrayInputStream(expectedBytes);
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, expectedBytes.length);
        byte[] actualBytes = new byte[byteBuffer.remaining()];
        byteBuffer.get(actualBytes);
        assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        String expectedHtml = "<html><head><title>Test Page</title></head><body><h1>Hello World</h1></body></html>";
        File file = new File("test.html");
        Files.write(Paths.get(file.getAbsolutePath()), expectedHtml.getBytes());
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
        String actualHtml = new String(byteBuffer.array(), Charset.forName("UTF-8"));
        assertEquals(expectedHtml, actualHtml);
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        assertEquals(0, byteBuffer.remaining());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidCharset() {
        String contentType = "text/html; charset=InvalidCharset";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testValidateCharset() {
        String validCharset = "UTF-8";
        String validatedCharset = DataUtil.validateCharset(validCharset);
        assertEquals(validCharset, validatedCharset);
    }

    @Test
    public void testValidateCharsetWithInvalidCharset() {
        String invalidCharset = "InvalidCharset";
        String validatedCharset = DataUtil.validateCharset(invalidCharset);
        assertNull(validatedCharset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertNotNull(boundary);
        assertEquals(DataUtil.boundaryLength, boundary.length());
    }

    @Test
    public void testDetectCharsetFromBom() {
        String charsetName = "UTF-8";
        ByteBuffer byteBuffer = ByteBuffer.allocate(4);
        byteBuffer.put((byte) 0xEF);
        byteBuffer.put((byte) 0xBB);
        byteBuffer.put((byte) 0xBF);
        byteBuffer.put((byte) 0xC0);
        byteBuffer.rewind();
        String detectedCharset = DataUtil.detectCharsetFromBom(byteBuffer, charsetName);
        assertEquals(charsetName, detectedCharset);
    }
}