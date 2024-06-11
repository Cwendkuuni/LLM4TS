package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        Path filePath = Paths.get("test.html");
        Document doc = DataUtil.load(filePath.toFile(), null, null);
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body>Test</body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, "UTF-8", null);
        assertNotNull(doc);
        assertEquals("<html><head><meta charset=\"UTF-8\"></head><body>Test</body></html>", doc.html());
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String html = "<html><head><meta charset=\"GBK\"></head><body>Test</body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        assertThrows(IllegalArgumentException.class, () -> DataUtil.load(inputStream, "GBK", null));
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String html = "<html><head><meta charset=\"\"></head><body>Test</body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, null, null);
        assertNotNull(doc);
        assertEquals("<html><head><meta charset=\"UTF-8\"></head><body>Test</body></html>", doc.html());
    }

    @Test
    public void testParseInputStream() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body>Test</body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseInputStream(inputStream, "UTF-8", null, Parser.htmlParser());
        assertNotNull(doc);
        assertEquals("<html><head><meta charset=\"UTF-8\"></head><body>Test</body></html>", doc.html());
    }

    @Test
    public void testCrossStreams() throws IOException {
        String source = "Source";
        String destination = "Destination";
        String data = "Data";

        InputStream inputStream = new ByteArrayInputStream(source.getBytes(StandardCharsets.UTF_8));
        OutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);
        byte[] result = outputStream.toByteArray();

        assertEquals(data, new String(result, StandardCharsets.UTF_8));
    }

    @Test
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String invalidHtml = "<html><head><meta charset=\"GBK\"></head><body>Test</body></html>";
        InputStream inputStream = new ByteArrayInputStream(invalidHtml.getBytes(StandardCharsets.UTF_8));
        assertThrows(IllegalArgumentException.class, () -> DataUtil.parseInputStream(inputStream, "GBK", null, Parser.htmlParser()));
    }

    @Test
    public void testParseInputStreamWithDefaultCharset() throws IOException {
        String html = "<html><head><meta charset=\"\"></head><body>Test</body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
        assertNotNull(doc);
        assertEquals("<html><head></head><body>Test</body></html>", doc.html());
    }

    @Test
    public void testParseInputStreamWithCustomParser() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body>Test</body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Parser parser = new Parser(); // Custom parser
        Document doc = DataUtil.parseInputStream(inputStream, "UTF-8", null, parser);
        assertNotNull(doc);
        assertEquals("<html><head><meta charset=\"UTF-8\"></head><body>Test</body></html>", doc.html());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body>Test</body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        ByteBuffer buffer = DataUtil.readToByteBuffer(inputStream);

        byte[] expectedBytes = html.getBytes(StandardCharsets.UTF_8);
        byte[] actualBytes = new byte[buffer.limit()];
        buffer.get(actualBytes);

        assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test
    public void testReadToByteBufferWithInvalidStream() throws IOException {
        InputStream inputStream = null;
        assertThrows(NullPointerException.class, () -> DataUtil.readToByteBuffer(inputStream));
    }

    @Test
    public void testReadToByteBufferWithEmptyStream() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ByteBuffer buffer = DataUtil.readToByteBuffer(inputStream);
        assertEquals(0, buffer.limit());
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        Path filePath = Paths.get("test.html");
        byte[] expectedBytes = Files.readAllBytes(filePath);
        ByteBuffer buffer = DataUtil.readFileToByteBuffer(filePath.toFile());

        assertArrayEquals(expectedBytes, buffer.array());
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer buffer = DataUtil.emptyByteBuffer();
        assertEquals(0, buffer.limit());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidCharset() {
        String contentType = "text/html; charset=GBK";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithNoCharset() {
        String contentType = "text/html";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        String validatedCharset = DataUtil.validateCharset(charset);
        assertEquals(charset, validatedCharset);
    }

    @Test
    public void testValidateCharsetWithInvalidCharset() {
        String charset = "GBK";
        String validatedCharset = DataUtil.validateCharset(charset);
        assertNull(validatedCharset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertNotNull(boundary);
    }

    @Test
    public void testDetectCharsetFromBom() throws IOException {
        String bomUTF8 = "\uFEFF<html><head><meta charset=\"UTF-8\"></head><body>Test</body></html>";
        String bomUTF16 = "\uFEFF\uFFFE<html><head><meta charset=\"UTF-16\"></head><body>Test</body></html>";
        String bomUTF32 = "\uFFFE\uFFFE<html><head><meta charset=\"UTF-32\"></head><body>Test</body></html>";

        ByteBuffer byteDataUTF8 = DataUtil.readToByteBuffer(new ByteArrayInputStream(bomUTF8.getBytes(StandardCharsets.UTF_8)));
        BomCharset bomCharsetUTF8 = DataUtil.detectCharsetFromBom(byteDataUTF8);
        assertEquals("UTF-8", bomCharsetUTF8.charset);
        assertEquals(0, bomCharsetUTF8.offset);

        ByteBuffer byteDataUTF16 = DataUtil.readToByteBuffer(new ByteArrayInputStream(bomUTF16.getBytes(StandardCharsets.UTF_8)));
        BomCharset bomCharsetUTF16 = DataUtil.detectCharsetFromBom(byteDataUTF16);
        assertEquals("UTF-16", bomCharsetUTF16.charset);
        assertEquals(0, bomCharsetUTF16.offset);

        ByteBuffer byteDataUTF32 = DataUtil.readToByteBuffer(new ByteArrayInputStream(bomUTF32.getBytes(StandardCharsets.UTF_8)));
        BomCharset bomCharsetUTF32 = DataUtil.detectCharsetFromBom(byteDataUTF32);
        assertEquals("UTF-32", bomCharsetUTF32.charset);
        assertEquals(0, bomCharsetUTF32.offset);

        ByteBuffer byteDataEmpty = DataUtil.readToByteBuffer(new ByteArrayInputStream(new byte[0]));
        BomCharset bomCharsetEmpty = DataUtil.detectCharsetFromBom(byteDataEmpty);
        assertNull(bomCharsetEmpty);
    }
}