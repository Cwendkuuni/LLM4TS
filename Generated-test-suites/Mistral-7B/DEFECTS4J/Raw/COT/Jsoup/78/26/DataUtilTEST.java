package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "This is a test file.";
        File testFile = new File("testFile.html");
        testFile.createNewFile();
        Files.write(testFile.getAbsoluteFile().toPath(), fileContent.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.load(testFile, "UTF-8", null);
        assertNotNull(document);
        assertEquals(fileContent, document.body().text());
        testFile.delete();
    }

    @Test
    public void testLoadInputStreamNullFile() throws IOException {
        InputStream inputStream = null;
        assertThrows(IOException.class, () -> DataUtil.load(inputStream, null, null));
    }

    @Test
    public void testLoadInputStreamEmptyBuffer() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Document document = DataUtil.load(inputStream, null, null);
        assertNotNull(document);
        assertEquals("", document.body().text());
    }

    @Test
    public void testLoadInputStreamInvalidCharset() throws IOException {
        String invalidCharset = "INVALID_CHARSET";
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Test".getBytes(StandardCharsets.UTF_8));
        assertThrows(IllegalArgumentException.class, () -> DataUtil.load(inputStream, invalidCharset, null));
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String testContent = "This is a test document.";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testContent.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(inputStream, "UTF-8", null);
        assertNotNull(document);
        assertEquals(testContent, document.body().text());
    }

    @Test
    public void testLoadInputStreamWithBaseUri() throws IOException {
        String testContent = "This is a test document.";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testContent.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com");
        assertNotNull(document);
        assertEquals(testContent, document.body().text());
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String testContent = "<html><body><h1>Test</h1></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testContent.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(inputStream, "UTF-8", null, Parser.xmlParser());
        assertNotNull(document);
        assertEquals("<h1>Test</h1>", document.body().html());
    }

    @Test
    public void testCrossStreams() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Test".getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);
        byte[] output = outputStream.toByteArray();
        assertEquals(inputStream.available(), output.length);
        assertEquals(inputStream.readAllBytes(), output);
    }

    @Test
    public void testParseInputStreamNullInputStream() throws IOException {
        InputStream inputStream = null;
        assertThrows(IOException.class, () -> DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser()));
    }

    @Test
    public void testParseInputStreamEmptyBuffer() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Document document = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
        assertNotNull(document);
        assertEquals("", document.body().text());
    }

    @Test
    public void testParseInputStreamNullCharset() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Test".getBytes(StandardCharsets.UTF_8));
        assertThrows(IllegalArgumentException.class, () -> DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser()));
    }

    @Test
    public void testParseInputStream() throws IOException {
        String testContent = "This is a test document.";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testContent.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseInputStream(inputStream, "UTF-8", null, Parser.htmlParser());
        assertNotNull(document);
        assertEquals(testContent, document.body().text());
    }

    @Test
    public void testParseInputStreamWithBaseUri() throws IOException {
        String testContent = "This is a test document";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testContent.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseInputStream(inputStream, "UTF-8", "http://example.com", Parser.htmlParser());
        assertNotNull(document);
        assertEquals(testContent, document.body().text());
    }

    @Test
    public void testParseInputStreamWithParserXml() throws IOException {
        String testContent = "<html><body><h1>Test</h1></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testContent.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseInputStream(inputStream, "UTF-8", null, Parser.xmlParser());
        assertNotNull(document);
        assertEquals("<h1>Test</h1>", document.body().html());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Test".getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        byte[] expected = inputStream.readAllBytes();
        assertEquals(expected.length, byteBuffer.remaining());
        byte[] actual = new byte[byteBuffer.remaining()];
        byteBuffer.get(actual);
        assertEquals(expected, actual);
    }

    @Test
    public void testReadToByteBufferEmptyBuffer() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        assertEquals(0, byteBuffer.remaining());
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File file = new File("testFile.html");
        String testContent = "This is a test file.";
        Files.write(file.getAbsoluteFile().toPath(), testContent.getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
        assertEquals(testContent.length(), byteBuffer.remaining());
        byte[] expected = testContent.getBytes(StandardCharsets.UTF_8);
        byte[] actual = new byte[byteBuffer.remaining()];
        byteBuffer.get(actual);
        assertEquals(expected, actual);
        file.delete();
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        assertEquals(0, byteBuffer.remaining());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/plain; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);

        contentType = "application/xml; charset=GB2312";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("GB2312", charset);

        contentType = "application/octet-stream";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        assertEquals("UTF-8", DataUtil.validateCharset(charset));

        charset = "UTF-8\t";
        assertEquals("UTF-8", DataUtil.validateCharset(charset));

        charset = "UTF-8\"";
        assertEquals("UTF-8", DataUtil.validateCharset(charset));

        charset = "UTF-8'";
        assertEquals("UTF-8", DataUtil.validateCharset(charset));

        charset = "InvalidCharset";
        assertNull(DataUtil.validateCharset(charset));

        charset = "ISO-8859-1";
        assertEquals("ISO-8859-1", DataUtil.validateCharset(charset));
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        assertEquals(32, mimeBoundary.length());
    }
}