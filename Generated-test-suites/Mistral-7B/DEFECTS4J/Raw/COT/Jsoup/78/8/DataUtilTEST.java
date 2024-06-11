package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.junit.Test;

/**
 * Test class for DataUtil.
 */
public class DataUtilTest {

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        File file = new File("testData/sample.html");
        Document document = DataUtil.load(file, null);
        assertNotNull(document);
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        File file = new File("testData/sample.html");
        Document document = DataUtil.load(file, "UTF-8");
        assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidCharset() throws IOException {
        File file = new File("testData/sample.html");
        Document document = DataUtil.load(file, "InvalidCharset");
        assertNull(document);
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String testData = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(inputStream, null);
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        String testData = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(inputStream, "UTF-8");
        assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String testData = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(inputStream, "InvalidCharset");
        assertNull(document);
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String testData = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(inputStream, null, null, Parser.xmlParser());
        assertNotNull(document);
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Test data".getBytes());
        OutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(inputStream, outputStream);
        assertNotNull(outputStream.toString());
    }

    @Test
    public void testParseInputStream() throws IOException {
        String testData = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String testData = "Test data";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        byte[] expectedBytes = testData.getBytes(StandardCharsets.UTF_8);
        ByteBuffer expectedByteBuffer = ByteBuffer.wrap(expectedBytes);
        assertEquals(expectedByteBuffer, byteBuffer);
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        String testData = "Test data";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 10);
        assertEquals(ByteBuffer.wrap(testData.getBytes(0, 10)), byteBuffer);
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        String testData = "Test data";
        File file = new File("testData/sample.html");
        if (!file.exists()) {
            file.createNewFile();
        }
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw")) {
            randomAccessFile.writeBytes(testData);
        }
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
        byte[] expectedBytes = testData.getBytes(StandardCharsets.UTF_8);
        ByteBuffer expectedByteBuffer = ByteBuffer.wrap(expectedBytes);
        assertEquals(expectedByteBuffer, byteBuffer);
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        assertEquals(ByteBuffer.allocate(0), byteBuffer);
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
    public void testParseOutBomFromInputStream() throws IOException {
        String testData = "\ufeffTest data";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
        assertTrue(document.outputSettings().charset().equals("UTF-8"));
    }

    @Test
    public void testParseOutBomFromInputStreamWithInvalidCharset() throws IOException {
        String testData = "\ufeffTest data";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseInputStream(inputStream, "InvalidCharset", null, Parser.htmlParser());
        assertNull(document);
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        assertEquals("UTF-8", DataUtil.validateCharset(charset));

        String invalidCharset = "InvalidCharset";
        assertNull(DataUtil.validateCharset(invalidCharset));
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        byte[] mimeBoundaryBytes = mimeBoundary.getBytes(StandardCharsets.UTF_8);
        ByteBuffer mimeBoundaryByteBuffer = ByteBuffer.wrap(mimeBoundaryBytes);
        int boundaryLength = mimeBoundary.length();
        assertEquals(boundaryLength, mimeBoundaryByteBuffer.limit());
    }
}