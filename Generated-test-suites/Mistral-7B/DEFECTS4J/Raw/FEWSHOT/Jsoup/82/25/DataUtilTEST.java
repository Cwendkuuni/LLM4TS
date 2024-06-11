package org.jsoup.helper;

import static org.junit.Assert.*;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html"); // your test file path
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String testString = "Test String";
        byte[] testBytes = testString.getBytes();
        InputStream inputStream = new ByteArrayInputStream(testBytes);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(doc);
        assertEquals(testString, doc.body().text());
    }

    @Test
    public void testParseInputStream() throws IOException {
        String testString = "Test String";
        byte[] testBytes = testString.getBytes();
        InputStream inputStream = new ByteArrayInputStream(testBytes);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = DataUtil.parseInputStream(inputStream, charsetName, baseUri, Parser.htmlParser());
        assertNotNull(doc);
        assertEquals(testString, doc.body().text());
    }

    @Test
    public void testCrossStreams() throws IOException {
        String testString = "Test String";
        byte[] testBytes = testString.getBytes();
        InputStream inputStream = new ByteArrayInputStream(testBytes);
        OutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(inputStream, outputStream);
        byte[] resultBytes = outputStream.toByteArray();
        String resultString = new String(resultBytes, "UTF-8");
        assertEquals(testString, resultString);
    }

    @Test
    public void testParseInputStreamWithCharset() throws IOException {
        String charsetName = "UTF-16";
        String baseUri = "http://example.com";
        String testString = "Test String";
        byte[] testBytes = testString.getBytes("UTF-16");
        InputStream inputStream = new ByteArrayInputStream(testBytes);
        Document doc = DataUtil.parseInputStream(inputStream, charsetName, baseUri, Parser.htmlParser());
        assertNotNull(doc);
        assertEquals(testString, doc.body().text());
    }

    @Test
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String charsetName = "invalidCharset";
        String baseUri = "http://example.com";
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Document doc = assertThrows(IllegalArgumentException.class, () -> DataUtil.parseInputStream(inputStream, charsetName, baseUri, Parser.htmlParser()));
        assertEquals("Unsupported charset: " + charsetName, doc.getMessage());
    }

    @Test
    public void testParseInputStreamWithNullBaseUri() throws IOException {
        String charsetName = "UTF-8";
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Document doc = assertThrows(NullPointerException.class, () -> DataUtil.parseInputStream(inputStream, charsetName, null, Parser.htmlParser()));
    }

    @Test
    public void testParseInputStreamWithNullParser() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Document doc = assertThrows(NullPointerException.class, () -> DataUtil.parseInputStream(inputStream, charsetName, baseUri, null));
    }

    @Test
    public void testDetectCharsetFromBom() throws IOException {
        String testStringUtf8 = "Test String UTF-8";
        String testStringUtf16 = "Test String UTF-16";
        String testStringUtf32 = "Test String UTF-32";
        byte[] testBytesUtf8 = testStringUtf8.getBytes();
        byte[] testBytesUtf16BE = new byte[]{(byte) 0xFF, (byte) 0xFE, (byte) 0x00, (byte) 0x00};
        byte[] testBytesUtf16LE = new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0xFF, (byte) 0xFE};
        byte[] testBytesUtf32BE = new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0xFE, (byte) 0xFF, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};
        byte[] testBytesUtf32LE = new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xFF, (byte) 0xFE, (byte) 0x00, (byte) 0x00};

        ByteBuffer bufferUtf8 = ByteBuffer.wrap(testBytesUtf8);
        ByteBuffer bufferUtf16BE = ByteBuffer.wrap(testBytesUtf16BE);
        ByteBuffer bufferUtf16LE = ByteBuffer.wrap(testBytesUtf16LE);
        ByteBuffer bufferUtf32BE = ByteBuffer.wrap(testBytesUtf32BE);
        ByteBuffer bufferUtf32LE = ByteBuffer.wrap(testBytesUtf32LE);

        BomCharset bomUtf8 = DataUtil.detectCharsetFromBom(bufferUtf8);
        BomCharset bomUtf16BE = DataUtil.detectCharsetFromBom(bufferUtf16BE);
        BomCharset bomUtf16LE = DataUtil.detectCharsetFromBom(bufferUtf16LE);
        BomCharset bomUtf32BE = DataUtil.detectCharsetFromBom(bufferUtf32BE);
        BomCharset bomUtf32LE = DataUtil.detectCharsetFromBom(bufferUtf32LE);

        assertEquals("UTF-8", bomUtf8.charset);
        assertEquals(false, bomUtf8.offset);
        assertEquals("UTF-16", bomUtf16BE.charset);
        assertEquals(false, bomUtf16BE.offset);
        assertEquals("UTF-16", bomUtf16LE.charset);
        assertEquals(true, bomUtf16LE.offset);
        assertEquals("UTF-32", bomUtf32BE.charset);
        assertEquals(false, bomUtf32BE.offset);
        assertEquals("UTF-32", bomUtf32LE.charset);
        assertEquals(true, bomUtf32LE.offset);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType1 = "text/html; charset=UTF-8";
        String contentType2 = "text/html; charset=gb2312";
        String contentType3 = "text/html";

        assertEquals("UTF-8", DataUtil.getCharsetFromContentType(contentType1));
        assertEquals("gb2312", DataUtil.getCharsetFromContentType(contentType2));
        assertEquals(null, DataUtil.getCharsetFromContentType(contentType3));
    }

    @Test
    public void testValidateCharset() {
        String charset1 = "UTF-8";
        String charset2 = "gb2312";
        String charset3 = "ISO-8859-1";
        String charset4 = "UTF-16";
        String charset5 = "invalid";
        String charset6 = "";

        assertEquals(charset1, DataUtil.validateCharset(charset1));
        assertEquals(charset2, DataUtil.validateCharset(charset2));
        assertEquals(charset3, DataUtil.validateCharset(charset3));
        assertEquals(charset4, DataUtil.validateCharset(charset4));
        assertEquals(null, DataUtil.validateCharset(charset5));
        assertEquals(null, DataUtil.validateCharset(charset6));
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertNotNull(boundary);
        assertFalse(boundary.isEmpty());
        int boundaryLength = boundary.length();
        assertTrue(boundaryLength >= 32);
    }
}