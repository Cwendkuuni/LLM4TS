package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;
import org.jsoup.nodes.Document;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String filePath = "testFile.html";
        File file = new File(filePath);

        Document doc = DataUtil.load(file, StandardCharsets.UTF_8.name(), null);
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String testData = "test data";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStream writer = outputStream;
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));

        DataUtil.crossStreams(inputStream, writer);
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        Document doc = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        String testData = "test data";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStream writer = outputStream;
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.parseByteData(DataUtil.readToByteBuffer(inputStream), "ISO-8859-1", null, Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testParseByteDataWithNullByteData() {
        assertThrows(NullPointerException.class, () -> {
            DataUtil.parseByteData(null, null, null, Parser.htmlParser());
        });
    }

    @Test
    public void testParseByteDataWithEmptyByteData() {
        ByteBuffer emptyByteBuffer = DataUtil.emptyByteBuffer();
        Document doc = DataUtil.parseByteData(emptyByteBuffer, null, null, Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNotNull(charset);
        assertNotNull(Charset.forName(charset));

        String contentTypeWithoutCharset = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentTypeWithoutCharset);
        assertNull(charset);
    }

    @Test(expected = IllegalCharsetNameException.class)
    public void testValidateCharsetWithUnsupportedCharset() {
        String unsupportedCharset = "XYZ-123";
        DataUtil.validateCharset(unsupportedCharset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertNotNull(boundary);
        assertFalse(boundary.isEmpty());
    }

    @Test
    public void testDetectCharsetFromBomWithUTF8() throws IOException {
        String testDataUTF8 = "\u00ef\u00bb\u00bftest data UTF-8";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStream writer = outputStream;
        InputStream inputStream = new ByteArrayInputStream(testDataUTF8.getBytes(StandardCharsets.UTF_8));

        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        String charsetDetected = DataUtil.detectCharsetFromBom(byteData, null);
        assertEquals("UTF-8", charsetDetected);
    }

    @Test
    public void testDetectCharsetFromBomWithUTF16LE() throws IOException {
        String testDataUTF16LE = "\u00fe\u00fftest data UTF-16 LE";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStream writer = outputStream;
        InputStream inputStream = new ByteArrayInputStream(testDataUTF16LE.getBytes(StandardCharsets.UTF_8));

        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        byteData.position(2);
        String charsetDetected = DataUtil.detectCharsetFromBom(byteData, null);
        assertEquals("UTF-16", charsetDetected);
    }

    @Test
    public void testDetectCharsetFromBomWithUTF16BE() throws IOException {
        String testDataUTF16BE = "\u00fe\u00fftest data UTF-16 BE";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStream writer = outputStream;
        InputStream inputStream = new ByteArrayInputStream(testDataUTF16BE.getBytes(StandardCharsets.UTF_8));

        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        byteData.position(1); // BOM for UTF-16 BE is FEFF
        String charsetDetected = DataUtil.detectCharsetFromBom(byteData, null);
        assertEquals("UTF-16", charsetDetected);
    }

    @Test
    public void testDetectCharsetFromBomWithUTF32LE() throws IOException {
        String testDataUTF32LE = "\u00fe\u00ff\u0000\u0000test data UTF-32 LE";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStream writer = outputStream;
        InputStream inputStream = new ByteArrayInputStream(testDataUTF32LE.getBytes(StandardCharsets.UTF_8));

        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        String charsetDetected = DataUtil.detectCharsetFromBom(byteData, null);
        assertEquals("UTF-32", charsetDetected);
    }

    @Test
    public void testDetectCharsetFromBomWithUTF32BE() throws IOException {
        String testDataUTF32BE = "\u00ff\u00fe\u0000\u0000test data UTF-32 BE";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStream writer = outputStream;
        InputStream inputStream = new ByteArrayInputStream(testDataUTF32BE.getBytes(StandardCharsets.UTF_8));

        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        String charsetDetected = DataUtil.detectCharsetFromBom(byteData, null);
        assertEquals("UTF-32", charsetDetected);
    }

    @Test
    public void testDetectCharsetFromBomWithNonUTFByteData() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStream writer = outputStream;
        InputStream inputStream = new ByteArrayInputStream("test data".getBytes(StandardCharsets.UTF_8));

        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        String charsetDetected = DataUtil.detectCharsetFromBom(byteData, null);
        assertNull(charsetDetected);
    }
}