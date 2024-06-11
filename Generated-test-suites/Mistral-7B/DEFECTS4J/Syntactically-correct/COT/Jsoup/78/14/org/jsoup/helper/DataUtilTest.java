package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.junit.Test;

/**
 * Test class for DataUtil methods.
 *
 */
public class DataUtilTest {

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        File testFile = new File("testFile.html");
        Document doc = DataUtil.load(testFile, null);
        assertNotNull(doc);
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        File testFile = new File("testFile.html");
        Document doc = DataUtil.load(testFile, "UTF-8");
        assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoadNullFile() throws IOException {
        Document doc = DataUtil.load(null, "UTF-8", "");
        assertNull(doc);
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String testData = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, null);
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        String testData = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, "UTF-8");
        assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoadNullInputStream() throws IOException {
        Document doc = DataUtil.load(null, "UTF-8", "");
        assertNull(doc);
    }

    @Test
    public void testParseInputStream() throws IOException {
        String testData = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseInputStream(inputStream, "UTF-8", "", Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testCrossStreams() throws IOException {
        byte[] testData = new byte[bufferSize];
        RandomAccessFile testFile = new RandomAccessFile("testFile.out", "rw");
        InputStream inputStream = new ByteArrayInputStream(testData);
        OutputStream outputStream = testFile;

        DataUtil.crossStreams(inputStream, outputStream);

        testFile.seek(0);
        byte[] readData = new byte[testData.length];
        testFile.read(readData);

        assertEquals(testData.length, readData.length);
        for (int i = 0; i < testData.length; i++) {
            assertEquals(testData[i], readData[i]);
        }

        testFile.close();
    }

    @Test
    public void testParseInputStreamWithEmptyBOM() throws IOException {
        String testData = "";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseInputStream(inputStream, null, "", Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testParseInputStreamWithValidBOM() throws IOException {
        String testData = "\uFEFF<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseInputStream(inputStream, null, "", Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testParseInputStreamWithInvalidBOM() throws IOException {
        String testData = "\uFFFE<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));
        Document doc = null;
        try {
            doc = DataUtil.parseInputStream(inputStream, null, "", Parser.htmlParser());
        } catch (IOException e) {
            // expected
        }
        assertNull(doc);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithNoCharset() {
        String contentType = "text/html";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        assertEquals(charset, DataUtil.validateCharset(charset));

        String invalidCharset = "UTF-8\"";
        assertNull(DataUtil.validateCharset(invalidCharset));
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertNotNull(boundary);
        assertEquals(boundaryLength, boundary.length());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String testData = "Test Data";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);

        byte[] expectedByteData = testData.getBytes(StandardCharsets.UTF_8);
        byte[] bufferData = new byte[byteBuffer.limit()];
        byteBuffer.get(bufferData);

        assertEquals(expectedByteData.length, byteBuffer.limit());
        for (int i = 0; i < expectedByteData.length; i++) {
            assertEquals(expectedByteData[i], bufferData[i]);
        }
    }

    @Test
    public void testReadToByteBufferWithLargeData() throws IOException {
        String testData = "Test Data";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 1024 * 1024 * 10);

        byte[] expectedByteData = testData.getBytes(StandardCharsets.UTF_8);
        byte[] bufferData = new byte[byteBuffer.limit()];
        byteBuffer.get(bufferData);

        assertEquals(expectedByteData.length, byteBuffer.limit());
        for (int i = 0; i < expectedByteData.length; i++) {
            assertEquals(expectedByteData[i], bufferData[i]);
        }
    }
}