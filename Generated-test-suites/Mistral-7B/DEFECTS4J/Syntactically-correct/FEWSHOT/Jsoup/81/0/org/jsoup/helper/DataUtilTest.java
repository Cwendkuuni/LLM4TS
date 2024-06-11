package org.jsoup.helper;

import static org.junit.Assert.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "/path/to/test";
        File file = new File("test.html");
        Document doc = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "/path/to/test";
        InputStream inputStream = new ByteArrayInputStream("test content".getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(doc);
    }

    @Test
    public void testParseInputStream() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "/path/to/test";
        String inputData = "test content";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseInputStream(inputStream, charsetName, baseUri, Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testParseInputStreamWithAlternateParser() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "/path/to/test";
        String inputData = "test content";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseInputStream(inputStream, charsetName, baseUri, Parser.xmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testCrossStreams() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("test content".getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(inputStream, outputStream);
        String expectedOutput = "test content";
        assertEquals(expectedOutput, outputStream.toString());
    }

    @Test
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String charsetName = " invalid_charset";
        String baseUri = "/path/to/test";
        String inputData = "test content";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));
        Document doc = null;
        try {
            doc = DataUtil.parseInputStream(inputStream, charsetName, baseUri, Parser.htmlParser());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
        assertNull(doc);
    }

    @Test
    public void testParseInputStreamWithUtf16Bom() throws IOException {
        String charsetName = "UTF-16";
        String baseUri = "/path/to/test";
        String inputData = "\u00FE\u00FFtest content";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseInputStream(inputStream, charsetName, baseUri, Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testParseInputStreamWithUtf32Bom() throws IOException {
        String charsetName = "UTF-32";
        String baseUri = "/path/to/test";
        String inputData = "\u0000\u0000\u00FF\u00FEtest content";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseInputStream(inputStream, charsetName, baseUri, Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testParseInputStreamWithUtf8Bom() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "/path/to/test";
        String inputData = "\uEFFF\uFFFEtest content";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseInputStream(inputStream, charsetName, baseUri, Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testParseInputStreamWithUtf8BomAndOffset() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "/path/to/test";
        String inputData = "\uEFFF\uFFFEtest content";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseInputStream(inputStream, charsetName, baseUri, Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String filePath = "test.html";
        File file = new File(filePath);
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(file);
        assertNotNull(byteBuffer);
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        String filePath = "test.html";
        File file = new File(filePath);
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(file, 5);
        assertNotNull(byteBuffer);
    }

    @Test(expected = IOException.class)
    public void testReadToByteBufferWithInterruptedIOException() throws IOException, InterruptedException {
        String filePath = "test.html";
        File file = new File(filePath);
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(file, 5, Thread.currentThread());
        fail("Expected IOException");
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        String filePath = "test.html";
        File file = new File(filePath);
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
        assertNotNull(byteBuffer);
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        assertTrue(byteBuffer.capacity() == 0);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=ISO-8859-1";
        String expectedCharset = "ISO-8859-1";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(expectedCharset, charset);

        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        assertEquals(charset, DataUtil.validateCharset(charset));

        charset = "ISO-8859-1";
        assertEquals(charset, DataUtil.validateCharset(charset));

        charset = "GBK";
        assertEquals(charset, DataUtil.validateCharset(charset));

        charset = "invalid_charset";
        assertNull(DataUtil.validateCharset(charset));
    }

    @Test
    public void testMimeBoundary() {
        String expectedBoundary = "----=_Part_0_0.12345678";
        String boundary = DataUtil.mimeBoundary();
        assertEquals(expectedBoundary, boundary);
    }
}