package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    private static final String CHARSET_UTF8 = "UTF-8";
    private static final String CHARSET_ISO88591 = "ISO-8859-1";
    private static final String BASE_URI = "http://www.example.com/";

    @Test
    public void testLoadFromFile() {
        File file = new File("test.html");
        try {
            Document document = DataUtil.load(file, CHARSET_UTF8, BASE_URI);
            Assert.assertNotNull(document);
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail("IOException thrown");
        }
    }

    @Test
    public void testLoadFromInputStream() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        try {
            Document document = DataUtil.load(inputStream, CHARSET_UTF8, BASE_URI);
            Assert.assertNotNull(document);
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail("IOException thrown");
        }
    }

    @Test
    public void testLoadFromInputStreamWithParser() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        try {
            Document document = DataUtil.load(inputStream, CHARSET_UTF8, BASE_URI, Parser.htmlParser());
            Assert.assertNotNull(document);
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail("IOException thrown");
        }
    }

    @Test
    public void testCrossStreams() {
        InputStream in = new ByteArrayInputStream("test".getBytes(StandardCharsets.UTF_8));
        OutputStream out = new ByteArrayOutputStream();
        try {
            DataUtil.crossStreams(in, out);
            String result = out.toString();
            Assert.assertEquals("test", result);
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail("IOException thrown");
        }
    }

    @Test
    public void testParseInputStream() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        try {
            Document document = DataUtil.parseInputStream(inputStream, CHARSET_UTF8, BASE_URI, Parser.htmlParser());
            Assert.assertNotNull(document);
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail("IOException thrown");
        }
    }

    @Test
    public void testReadToByteBuffer() {
        String text = "This is a test";
        InputStream inputStream = new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));
        try {
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 0);
            String result = new String(byteBuffer.array(), StandardCharsets.UTF_8);
            Assert.assertEquals(text, result);
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail("IOException thrown");
        }
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        Assert.assertEquals(0, byteBuffer.capacity());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals(CHARSET_UTF8, charset);
    }

    @Test
    public void testValidateCharset() {
        String charset = "ISO-8859-1";
        String validatedCharset = DataUtil.validateCharset(charset);
        Assert.assertEquals(CHARSET_ISO88591, validatedCharset);
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        Assert.assertNotNull(mimeBoundary);
        Assert.assertEquals(DataUtil.boundaryLength, mimeBoundary.length());
    }

    @Test
    public void testDetectCharsetFromBom() {
        String text = "This is a test";
        byte[] bomUtf8 = {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};
        byte[] inputUtf8 = new byte[bomUtf8.length + text.getBytes(StandardCharsets.UTF_8).length];
        System.arraycopy(bomUtf8, 0, inputUtf8, 0, bomUtf8.length);
        System.arraycopy(text.getBytes(StandardCharsets.UTF_8), 0, inputUtf8, bomUtf8.length, text.getBytes(StandardCharsets.UTF_8).length);
        byte[] bomUtf16LE = {(byte) 0xFF, (byte) 0xFE};
        byte[] inputUtf16LE = new byte[bomUtf16LE.length + text.getBytes(StandardCharsets.UTF_16LE).length];
        System.arraycopy(bomUtf16LE, 0, inputUtf16LE, 0, bomUtf16LE.length);
        System.arraycopy(text.getBytes(StandardCharsets.UTF_16LE), 0, inputUtf16LE, bomUtf16LE.length, text.getBytes(StandardCharsets.UTF_16LE).length);
        byte[] bomUtf16BE = {(byte) 0xFE, (byte) 0xFF};
        byte[] inputUtf16BE = new byte[bomUtf16BE.length + text.getBytes(StandardCharsets.UTF_16BE).length];
        System.arraycopy(bomUtf16BE, 0, inputUtf16BE, 0, bomUtf16BE.length);
        System.arraycopy(text.getBytes(StandardCharsets.UTF_16BE), 0, inputUtf16BE, bomUtf16BE.length, text.getBytes(StandardCharsets.UTF_16BE).length);

        ByteArrayInputStream inputStreamUtf8 = new ByteArrayInputStream(inputUtf8);
        ByteArrayInputStream inputStreamUtf16LE = new ByteArrayInputStream(inputUtf16LE);
        ByteArrayInputStream inputStreamUtf16BE = new ByteArrayInputStream(inputUtf16BE);

        try {
            DataUtil.BomCharset bomCharset = DataUtil.detectCharsetFromBom(inputStreamUtf8);
            Assert.assertNotNull(bomCharset);
            Assert.assertEquals(CHARSET_UTF8, bomCharset.charset);
            Assert.assertTrue(bomCharset.offset);

            bomCharset = DataUtil.detectCharsetFromBom(inputStreamUtf16LE);
            Assert.assertNotNull(bomCharset);
            Assert.assertEquals("UTF-16", bomCharset.charset);
            Assert.assertFalse(bomCharset.offset);

            bomCharset = DataUtil.detectCharsetFromBom(inputStreamUtf16BE);
            Assert.assertNotNull(bomCharset);
            Assert.assertEquals("UTF-16", bomCharset.charset);
            Assert.assertFalse(bomCharset.offset);
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail("IOException thrown");
        }
    }
}