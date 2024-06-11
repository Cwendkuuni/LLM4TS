package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    private InputStream inputStream;
    private OutputStream outputStream;
    private Parser parser;

    @Before
    public void setup() {
        inputStream = mock(InputStream.class);
        outputStream = mock(OutputStream.class);
        parser = mock(Parser.class);
    }

    @Test
    public void testLoadFile() throws Exception {
        File file = new File("test.html");
        Document document = DataUtil.load(file, null, null);
        assertEquals("test.html", document.baseUri());
    }

    @Test
    public void testLoadInputStream() throws Exception {
        String html = "<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(inputStream, null, null);
        assertEquals("<html><body><h1>Test</h1></body></html>", document.body().html());
    }

    @Test
    public void testLoadInputStreamWithParser() throws Exception {
        String xml = "<xml><data>Test</data></xml>";
        InputStream inputStream = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
        Parser parser = new Parser();
        Document document = DataUtil.load(inputStream, null, null, parser);
        assertEquals("<xml><data>Test</data></xml>", document.html());
    }

    @Test
    public void testCrossStreams() throws Exception {
        String source = "Source data.";
        String target = "";
        InputStream inputStream = new ByteArrayInputStream(source.getBytes(StandardCharsets.UTF_8));
        OutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);
        assertEquals(source, outputStream.toString(StandardCharsets.UTF_8.name()));
    }

    @Test
    public void testParseInputStream() throws IOException {
        String html = "<html><body><h1>Test</h1></body></html>";
        String charsetName = "UTF-8";
        String baseUri = "/test";

        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseInputStream(inputStream, charsetName, baseUri, parser);
        assertEquals("<html><body><h1>Test</h1></body></html>", document.body().html());
    }

    @Test
    public void testParseInputStreamWithCharset() throws IOException {
        String html = "<html><body><h1>Test</h1></body></html>";
        String charsetName = "UTF-16";
        String baseUri = "/test";

        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseInputStream(inputStream, charsetName, baseUri, parser);
        assertEquals("<html><body><h1>Test</h1></body></html>", document.body().html());
    }

    @Test
    public void testParseInputStreamWithParserAndCharset() throws IOException {
        String xml = "<xml><data>Test</data></xml>";
        String charsetName = "UTF-16";
        String baseUri = "/test";
        Parser parser = new Parser();

        InputStream inputStream = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseInputStream(inputStream, charsetName, baseUri, parser);
        assertEquals("<xml><data>Test</data></xml>", document.html());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String html = "<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 1024 * 5);
        assertEquals(html.length(), byteBuffer.capacity());

        byte[] data = new byte[html.length()];
        byteBuffer.get(data);
        assertEquals(html, new String(data, StandardCharsets.UTF_8));
    }

    @Test
    public void testReadToByteBufferWithLimit() throws IOException {
        String html = "<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 10);
        assertEquals(10, byteBuffer.capacity());
        assertEquals(0, byteBuffer.remaining());
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        String html = "<html><body><h1>Test</h1></body></html>";
        File file = new File("test.html");
        file.createNewFile();
        FileWriter writer = new FileWriter(file);
        writer.write(html);
        writer.close();

        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
        assertEquals(html.length(), byteBuffer.capacity());

        byte[] data = new byte[html.length()];
        byteBuffer.get(data);
        assertEquals(html, new String(data, StandardCharsets.UTF_8));
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        assertEquals(0, byteBuffer.capacity());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);

        String contentType2 = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType2);
        assertNull(charset);
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        String validatedCharset = DataUtil.validateCharset(charset);
        assertEquals(charset, validatedCharset);

        charset = "GBK";
        validatedCharset = DataUtil.validateCharset(charset);
        assertEquals("GBK", validatedCharset);

        charset = "";
        validatedCharset = DataUtil.validateCharset(charset);
        assertNull(validatedCharset);

        charset = "Invalid charset";
        validatedCharset = DataUtil.validateCharset(charset);
        assertNull(validatedCharset);
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        assertSame(mimeBoundary.length(), DataUtil.boundaryLength);
    }

    @Test
    public void testDetectCharsetFromBom() throws IOException {
        String bomUtf8 = "\uFEFF" + new String(new byte[] { 0x47, 0x45, 0x54, 0x54 }, StandardCharsets.UTF_8);
        String bomUtf16 = "\uFFFE" + new String(new byte[] { 0x4C, 0x69, 0x62, 0x72 }, StandardCharsets.UTF_16LE);
        String bomUtf32 = "\uFFFE\u0000" + new String(new byte[] { 0x4C, 0x69, 0x62, 0x72, 0x00, 0x00 }, StandardCharsets.UTF_32BE);

        ByteBuffer byteDataUtf8 = StandardCharsets.UTF_8.decode(new ByteArrayInputStream(bomUtf8.getBytes(StandardCharsets.UTF_8)));
        BomCharset bomUtf8Charset = DataUtil.detectCharsetFromBom(byteDataUtf8);
        assertEquals("UTF-8", bomUtf8Charset.charset);
        assertFalse(bomUtf8Charset.offset);

        ByteBuffer byteDataUtf16 = StandardCharsets.UTF_16LE.decode(new ByteArrayInputStream(bomUtf16.getBytes(StandardCharsets.UTF_8)));
        BomCharset bomUtf16Charset = DataUtil.detectCharsetFromBom(byteDataUtf16);
        assertEquals("UTF-16", bomUtf16Charset.charset);
        assertTrue(bomUtf16Charset.offset);

        ByteBuffer byteDataUtf32 = StandardCharsets.UTF_32BE.decode(new ByteArrayInputStream(bomUtf32.getBytes(StandardCharsets.UTF_8)));
        BomCharset bomUtf32Charset = DataUtil.detectCharsetFromBom(byteDataUtf32);
        assertEquals("UTF-32", bomUtf32Charset.charset);
        assertTrue(bomUtf32Charset.offset);

        ByteBuffer byteData = StandardCharsets.UTF_8.decode(new ByteArrayInputStream("Test".getBytes(StandardCharsets.UTF_8)));
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteData);
        assertNull(bomCharset);
    }
}