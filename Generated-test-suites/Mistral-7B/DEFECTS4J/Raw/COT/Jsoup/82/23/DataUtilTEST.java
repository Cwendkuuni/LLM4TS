package org.jsoup.helper;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;

/**
 * Test class for DataUtil.
 */
public final class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String filePath = "path/to/your/testfile.html";
        File file = new File(filePath);

        Document doc = DataUtil.load(file, null, null);
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String testData = "test data";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());

        Document doc = DataUtil.load(inputStream, null, null);
        assertEquals(testData, doc.body().html());
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        String testData = "test data";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes("UTF-16BE"));

        Document doc = DataUtil.load(inputStream, "UTF-16BE", null);
        assertEquals(testData, doc.body().html());
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String testData = "test data";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());

        Document doc = DataUtil.load(inputStream, null, null, Parser.xmlParser());
        assertEquals(testData, doc.html());
    }

    @Test
    public void testCrossStreams() throws IOException {
        String testData = "test data";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);
        assertEquals(testData, outputStream.toString());
    }

    @Test
    public void testParseInputStreamWithDefaultCharset() throws IOException {
        String testData = "test data";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());

        Document doc = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
        assertEquals(testData, doc.body().html());
    }

    @Test
    public void testParseInputStreamWithSpecifiedCharset() throws IOException {
        String testData = "test data";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes("UTF-16BE"));

        Document doc = DataUtil.parseInputStream(inputStream, "UTF-16BE", null, Parser.htmlParser());
        assertEquals(testData, doc.body().html());
    }

    @Test
    public void testParseInputStreamWithParserAndSpecifiedCharset() throws IOException {
        String testData = "test data";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes("UTF-16BE"));

        Document doc = DataUtil.parseInputStream(inputStream, "UTF-16BE", null, Parser.xmlParser());
        assertEquals(testData, doc.html());
    }

    @Test
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String testData = "test data";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());

        assertThrows(IllegalArgumentException.class, () -> {
            DataUtil.parseInputStream(inputStream, "InvalidCharset", null, Parser.htmlParser());
        });
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String testData = "test data";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 1024);

        byte[] expected = testData.getBytes();
        byte[] actual = new byte[byteBuffer.limit()];
        byteBuffer.get(actual);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);

        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        String validatedCharset = DataUtil.validateCharset(charset);
        assertEquals(charset, validatedCharset);

        charset = "InvalidCharset";
        validatedCharset = DataUtil.validateCharset(charset);
        assertNull(validatedCharset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertNotNull(boundary);
    }

    @Test
    public void testDetectCharsetFromBomUTF8() throws IOException {
        String testData = "\ufefftest data";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(DataUtil.readToByteBuffer(inputStream, 1024));
        assertEquals("UTF-8", bomCharset.charset);
        assertTrue(bomCharset.offset);
    }

    @Test
    public void testDetectCharsetFromBomUTF16LE() throws IOException {
        String testData = "\uFFFE\u0001test data";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(DataUtil.readToByteBuffer(inputStream, 1024));
        assertEquals("UTF-16LE", bomCharset.charset);
        assertTrue(bomCharset.offset);
    }

    @Test
    public void testDetectCharsetFromBomUTF16BE() throws IOException {
        String testData = "\uFFFE\u0001test data";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes((byte) 0xfe, (byte) 0xff));
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(DataUtil.readToByteBuffer(inputStream, 1024));
        assertEquals("UTF-16BE", bomCharset.charset);
        assertTrue(bomCharset.offset);
    }

    @Test
    public void testDetectCharsetFromBomInvalid() throws IOException {
        String testData = "\uFFFEtest data";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(DataUtil.readToByteBuffer(inputStream, 1024));
        assertNull(bomCharset);
    }

    private static void assertArrayEquals(byte[] expected, byte[] actual) {
        if (!java.util.Arrays.equals(expected, actual)) {
            fail("Expected: [" + new String(expected) + "], but was: [" + new String(actual) + "]");
        }
    }

    private static void assertNotNull(Object obj) {
        if (obj == null) {
            fail("Expected an object, but got null.");
        }
    }
}