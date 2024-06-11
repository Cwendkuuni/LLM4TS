package org.jsoup.helper;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;

/**
 * Test class for DataUtil methods.
 *
 */
public class DataUtilTest {

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        String expectedContent = "This is a test file.";
        String filePath = "TestFile.html";
        File testFile = new File(filePath);
        FileInputStream inputStream = new FileInputStream(testFile);
        Document doc = DataUtil.load(testFile, null);
        assertSame(expectedContent, doc.body().text());
        inputStream.close();
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        String expectedContent = "Das ist ein Testdatei."; // German text for testing charset detection
        String filePath = "TestFile_de.html";
        File testFile = new File(filePath);
        FileInputStream inputStream = new FileInputStream(testFile);
        Document doc = DataUtil.load(testFile, "ISO-8859-1"); // German charset
        assertSame(expectedContent, doc.body().text());
        inputStream.close();
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String expectedContent = "This is a test stream.";
        InputStream inputStream = new ByteArrayInputStream(expectedContent.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, null);
        assertSame(expectedContent, doc.body().text());
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        String expectedContent = "Das ist ein Teststrom."; // German text for testing charset detection
        InputStream inputStream = new ByteArrayInputStream(expectedContent.getBytes(StandardCharsets.ISO_8859_1));
        Document doc = DataUtil.load(inputStream, "ISO-8859-1"); // German charset
        assertSame(expectedContent, doc.body().text());
    }

    @Test
    public void testLoadInputStreamWithSpecifiedParser() throws IOException {
        String expectedContent = "<html><body><script src=\"test.js\"></script></body></html>";
        InputStream inputStream = new ByteArrayInputStream(expectedContent.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, null, null, DataUtil.Parser.xmlParser());
        assertSame(expectedContent, doc.html().toString());
    }

    @Test
    public void testCrossStreams() throws IOException {
        String testData = "Test data.";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);

        String output = outputStream.toString(StandardCharsets.UTF_8.name());
        assertTrue(testData.equals(output));

        inputStream.close();
        outputStream.close();
    }

    @Test
    public void testParseInputStream() throws IOException {
        String testData = "<html><body><p>Test data.</p></body></html>";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.parseInputStream(inputStream, "UTF-8", null, null);
        assertSame(testData, doc.toString());

        inputStream.close();
    }

    @Test
    public void testParseInputStreamWithDefaultCharset() throws IOException {
        String testData = "<html><body><p>Test data.</p></body></html>";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.parseInputStream(inputStream, null, null, null);
        assertSame(testData, doc.toString());

        inputStream.close();
    }

    @Test
    public void testParseInputStreamWithSpecifiedCharset() throws IOException {
        String testData = "<html><body><p>Test data.</p></body></html>";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.parseInputStream(inputStream, "UTF-8", null, null);
        String expectedContent = testData;
        assertSame(expectedContent, doc.toString());

        Document doc2 = DataUtil.parseInputStream(inputStream, "ISO-8859-1", null, null); // charset does not matter, as test data is UTF-8
        assertSame(expectedContent, doc2.toString());

        inputStream.close();
    }

    @Test
    public void testParseInputStreamWithSpecifiedParser() throws IOException {
        String testData = "<html><body><script src=\"test.js\"></script><p>Test data.</p></body></html>";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.parseInputStream(inputStream, "UTF-8", null, DataUtil.Parser.xmlParser());
        String expectedContent = "<html><body><script src=\"test.js\"></script></body>"; // No p tag in XML parser
        assertSame(expectedContent, doc.toString());

        inputStream.close();
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String testData = "Test data.";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 100);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byteBuffer.flip();
        byte[] buffer = new byte[byteBuffer.remaining()];
        byteBuffer.get(buffer);
        outputStream.write(buffer, 0, buffer.length);
        String result = outputStream.toString(StandardCharsets.UTF_8.name());

        assertTrue(testData.equals(result));

        inputStream.close();
        outputStream.close();
    }

    @Test
    public void testReadToByteBufferWithMaxSizeZero() throws IOException {
        String testData = "Test data.";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 0);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byteBuffer.flip();
        byte[] buffer = new byte[byteBuffer.remaining()];
        byteBuffer.get(buffer);
        outputStream.write(buffer, 0, buffer.length);
        String result = outputStream.toString(StandardCharsets.UTF_8.name());

        assertTrue(testData.equals(result));

        inputStream.close();
        outputStream.close();
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String expectedCharset = "UTF-8";

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertEquals(expectedCharset, charset);
    }

    @Test
    public void testGetCharsetFromContentTypeFailed() {
        String contentType = "text/html";
        String expectedCharset = null;

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertEquals(expectedCharset, charset);
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        assertEquals(charset, DataUtil.validateCharset(charset));

        String invalidCharset = "UTF-32-LE";
        assertNull(DataUtil.validateCharset(invalidCharset));
    }

    @Test
    public void testMimeBoundary() {
        String expectedBoundary = "dGVzdA=="; // "=_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ" in base64

        String boundary = DataUtil.mimeBoundary();

        assertEquals(expectedBoundary, boundary);
    }

    @Test
    public void testDetectCharsetFromBomUTF8() {
        String testData = "\uFEFFThis is a test.";

        ByteBuffer byteBuffer = ByteBuffer.wrap(testData.getBytes(StandardCharsets.UTF_8));

        DataUtil.BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);

        String expectedCharset = "UTF-8";
        assertEquals(expectedCharset, bomCharset.charset);
        assertEquals(false, bomCharset.offset);
    }

    @Test
    public void testDetectCharsetFromBomUTF16() {
        String testData = "\uFEFF\u0061\u0062"; // "AB" in UTF-16 LE

        ByteBuffer byteBuffer = ByteBuffer.wrap(testData.getBytes(StandardCharsets.UTF_16LE));

        DataUtil.BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);

        String expectedCharset = "UTF-16";
        assertEquals(expectedCharset, bomCharset.charset);
        assertEquals(false, bomCharset.offset);
    }

    @Test
    public void testDetectCharsetFromBomUTF32() {
        String testData = "\u0000\u0000\uFEFF\u0061\u0062"; // "AB" in UTF-32 LE

        ByteBuffer byteBuffer = ByteBuffer.wrap(testData.getBytes(StandardCharsets.UTF_32LE));

        DataUtil.BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);

        String expectedCharset = "UTF-32";
        assertEquals(expectedCharset, bomCharset.charset);
        assertEquals(true, bomCharset.offset);
    }

    @Test
    public void testDetectCharsetFromBomNone() {
        String testData = "This is a test.";

        ByteBuffer byteBuffer = ByteBuffer.wrap(testData.getBytes(StandardCharsets.UTF_8));

        DataUtil.BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);

        assertNull(bomCharset);
    }

    private void assertEquals(String expected, String actual) {
        assertEquals(expected, actual);
    }
}