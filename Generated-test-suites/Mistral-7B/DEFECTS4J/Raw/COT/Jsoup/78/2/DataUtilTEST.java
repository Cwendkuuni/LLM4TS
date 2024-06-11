package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import org.jsoup.helper.DataUtil;
import org.jsoup.helper.DataUtil.BomCharset;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;

/**
 * Test class for DataUtil class.
 */
public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html"); // Replace with the path to a valid HTML file
        String charsetName = StandardCharsets.UTF_8.name();
        String baseUri = "http://example.com";
        Document document = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String htmlContent = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        String charsetName = StandardCharsets.UTF_8.name();
        String baseUri = "http://example.com";
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String htmlContent = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        String invalidCharsetName = "InvalidCharset";
        String baseUri = "http://example.com";
        assertThrows(IllegalArgumentException.class, () -> DataUtil.load(inputStream, invalidCharsetName, baseUri));
    }

    @Test
    public void testParseInputStream() throws IOException {
        String htmlContent = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        String charsetName = StandardCharsets.UTF_8.name();
        String baseUri = "http://example.com";
        Document document = DataUtil.parseInputStream(inputStream, charsetName, baseUri, DataUtil.Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testCrossStreams() throws IOException {
        String srcContent = "Source content";
        String destContent = "Destination content";
        InputStream srcStream = new ByteArrayInputStream(srcContent.getBytes(StandardCharsets.UTF_8));
        OutputStream destStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(srcStream, destStream);

        String actualContent = new String(destStream.toByteArray(), StandardCharsets.UTF_8);
        assertNotNull(actualContent);
        assertNotNull(actualContent.trim());
        assertEquals(srcContent, actualContent);
    }

    @Test
    public void testParseInputStreamWithBOM() throws IOException {
        String htmlContentWithBOM = "\uFEFF<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContentWithBOM.getBytes(StandardCharsets.UTF_8));
        String charsetName = StandardCharsets.UTF_8.name();
        String baseUri = "http://example.com";
        Document document = DataUtil.parseInputStream(inputStream, charsetName, baseUri, DataUtil.Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testParseInputStreamWithInvalidContentType() throws IOException {
        String contentType = "text/plain; charset=UTF-8";
        String htmlContent = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        assertThrows(IllegalArgumentException.class, () -> DataUtil.parseInputStream(inputStream, contentType, "", DataUtil.Parser.htmlParser()));
    }

    @Test
    public void testParseInputStreamWithValidContentType() throws IOException {
        String contentType = "text/html; charset=UTF-8";
        String htmlContent = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        String charsetName = StandardCharsets.UTF_8.name();
        String baseUri = "http://example.com";
        Document document = DataUtil.parseInputStream(inputStream, contentType, "", DataUtil.Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String content = "Test content";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
        ByteBuffer expectedByteBuffer = ByteBuffer.wrap(content.getBytes(StandardCharsets.UTF_8));
        ByteBuffer actualByteBuffer = DataUtil.readToByteBuffer(inputStream);
        assertNotNull(actualByteBuffer);
        assertEquals(expectedByteBuffer, actualByteBuffer);
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        String longContent = "Long test content";
        InputStream inputStream = new ByteArrayInputStream(longContent.getBytes(StandardCharsets.UTF_8));
        ByteBuffer expectedByteBuffer = ByteBuffer.wrap(longContent.getBytes(StandardCharsets.UTF_8));
        ByteBuffer actualByteBuffer = DataUtil.readToByteBuffer(inputStream, 1024);
        assertNotNull(actualByteBuffer);
        assertEquals(expectedByteBuffer.limit(), actualByteBuffer.limit());
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        String content = "Test content";
        File file = new File("test.txt");
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
        randomAccessFile.writeBytes(content.getBytes());
        randomAccessFile.close();
        ByteBuffer expectedByteBuffer = ByteBuffer.wrap(content.getBytes(StandardCharsets.UTF_8));
        ByteBuffer actualByteBuffer = DataUtil.readFileToByteBuffer(file);
        assertNotNull(actualByteBuffer);
        assertEquals(expectedByteBuffer, actualByteBuffer);
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer expectedByteBuffer = DataUtil.emptyByteBuffer();
        ByteBuffer actualByteBuffer = expectedByteBuffer;
        actualByteBuffer.put("Test content".getBytes(StandardCharsets.UTF_8));
        actualByteBuffer.flip();
        assertEquals(expectedByteBuffer, actualByteBuffer);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType1 = "text/html; charset=UTF-8";
        String expectedCharset1 = "UTF-8";
        String contentType2 = "text/html; charset=ISO-8859-1";
        String expectedCharset2 = "ISO-8859-1";
        String contentType3 = "text/html";
        String expectedCharset3 = null;
        assertEquals(expectedCharset1, DataUtil.getCharsetFromContentType(contentType1));
        assertEquals(expectedCharset2, DataUtil.getCharsetFromContentType(contentType2));
        assertEquals(expectedCharset3, DataUtil.getCharsetFromContentType(contentType3));
    }

    @Test
    public void testValidateCharset() {
        String charset1 = "UTF-8";
        String charset2 = "ISO-8859-1";
        String charset3 = "UTF-16";
        String charset4 = "UTF-32";
        String charset5 = "InvalidCharset";
        assertEquals(charset1, DataUtil.validateCharset(charset1));
        assertEquals(charset2, DataUtil.validateCharset(charset2));
        assertEquals(charset3, DataUtil.validateCharset(charset3));
        assertEquals(charset1, DataUtil.validateCharset(charset4)); // Case insensitive check
        assertNull(DataUtil.validateCharset(charset5));
    }

    @Test
    public void testMimeBoundary() {
        String expectedMimeBoundary = new String(DataUtil.mimeBoundary().getBytes(StandardCharsets.UTF_8));
        String actualMimeBoundary = new String(DataUtil.mimeBoundary().getBytes(StandardCharsets.UTF_8));
        assertEquals(expectedMimeBoundary, actualMimeBoundary);
    }

    @Test
    public void testDetectCharsetFromBom() {
        String expectedContent1 = "Test content 1";
        String expectedContent2 = "Test content 2";
        String expectedContent3 = "Test content 3";
        ByteBuffer byteData1 = ByteBuffer.wrap(expectedContent1.getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteData2 = ByteBuffer.wrap(expectedContent2.getBytes(StandardCharsets.UTF_16LE));
        ByteBuffer byteData3 = ByteBuffer.wrap(expectedContent3.getBytes(StandardCharsets.UTF_32BE));
        BomCharset bomCharset1 = DataUtil.detectCharsetFromBom(byteData1);
        BomCharset bomCharset2 = DataUtil.detectCharsetFromBom(byteData2);
        BomCharset bomCharset3 = DataUtil.detectCharsetFromBom(byteData3);
        assertEquals(StandardCharsets.UTF_8.name(), bomCharset1.charset);
        assertEquals(0, bomCharset1.offset);
        assertEquals(StandardCharsets.UTF_16LE.name(), bomCharset2.charset);
        assertEquals(0, bomCharset2.offset);
        assertEquals(StandardCharsets.UTF_32BE.name(), bomCharset3.charset);
        assertEquals(0, bomCharset3.offset);
    }
}

class BomCharset {
    private final String charset;
    private final int offset;

    public BomCharset(String charset, int offset) {
        this.charset = charset;
        this.offset = offset;
    }
}