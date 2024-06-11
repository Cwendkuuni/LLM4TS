package org.jsoup.helper;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String filePath = "testData/sample.html";
        Document doc = DataUtil.load(new File(filePath), null, null);
        assertEquals("Sample Page", doc.title());
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        Document doc = DataUtil.load(inputStream, null, null);
        assertEquals("Hello World", doc.select("h1").text());
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        Document doc = DataUtil.load(inputStream, null, null, Parser.xmlParser());
        assertEquals("Hello World", doc.select("h1").text());
    }

    @Test
    public void testParseInputStream() throws IOException {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        Document doc = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
        assertEquals("Hello World", doc.select("h1").text());
    }

    @Test
    public void testParseInputStreamWithParserAndCharset() throws IOException {
        String html = "<meta charset=\"UTF-16\"><html><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        Document doc = DataUtil.parseInputStream(inputStream, "UTF-16", null, Parser.htmlParser());
        assertEquals("Hello World", doc.select("h1").text());
    }

    @Test
    public void testParseInputStreamWithCharset() throws IOException {
        String html = "<meta charset=\"UTF-16\"><html><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        Document doc = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
        assertEquals("Hello World", doc.select("h1").text());

        // Verify the charset was detected correctly
        assertEquals("UTF-16", doc.outputSettings().charset().name());
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputData = "Input Data";
        String outputData = "Output Data";

        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputData.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);
        assertEquals(inputData, new String(inputStream.readAllBytes()));
        assertEquals(outputData, new String(outputStream.toByteArray()));
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer buffer = DataUtil.emptyByteBuffer();
        assertTrue(buffer.capacity() == 0);
        assertTrue(buffer.limit() == 0);
        assertTrue(buffer.position() == 0);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String testData = "Test Data";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());

        ByteBuffer buffer = DataUtil.readToByteBuffer(inputStream, 20);
        byte[] expectedData = testData.getBytes();
        assertArrayEquals(expectedData, buffer.array());
        assertEquals(expectedData.length, buffer.limit());
    }

    @Test
    public void testParseInputStreamWithBom() throws IOException {
        String bomUtf8 = "\uFEFF<html><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(bomUtf8.getBytes());

        Document doc = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
        assertEquals("Hello World", doc.select("h1").text());
        assertEquals("UTF-8", doc.outputSettings().charset().name());
    }

    @Test
    public void testParseInputStreamWithBomAndOffset() throws IOException {
        String bomUtf16 = "\uFFFE<html><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(bomUtf16.getBytes());

        Document doc = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
        assertEquals("Hello World", doc.select("h1").text());
        assertEquals("UTF-16", doc.outputSettings().charset().name());
        assertTrue(doc.outputSettings().byteOrderMarkPresent());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String charset = DataUtil.getCharsetFromContentType("text/html; charset=UTF-8");
        assertEquals("UTF-8", charset);

        charset = DataUtil.getCharsetFromContentType("text/html; charset=gb2312");
        assertEquals("gb2312", charset);

        charset = DataUtil.getCharsetFromContentType("text/html");
        assertNull(charset);
    }

    @Test
    public void testValidateCharset() {
        String charset = DataUtil.validateCharset("UTF-8");
        assertEquals("UTF-8", charset);

        charset = DataUtil.validateCharset("utf-8");
        assertEquals("UTF-8", charset);

        charset = DataUtil.validateCharset("GB2312");
        assertEquals("GB2312", charset);

        charset = DataUtil.validateCharset("gb2312");
        assertEquals("GB2312", charset);

        charset = DataUtil.validateCharset("ISO-8859-15");
        assertEquals("ISO-8859-15", charset);

        charset = DataUtil.validateCharset("is0-8859-15");
        assertEquals("ISO-8859-15", charset);

        charset = DataUtil.validateCharset("invalid");
        assertNull(charset);

        charset = DataUtil.validateCharset(null);
        assertNull(charset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertEquals(32, boundary.length());
        for (int i = 0; i < boundary.length(); i++) {
            assertTrue(Character.isAlphabetic(boundary.charAt(i)) || boundary.charAt(i) == '-');
        }
    }

    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer bomUtf32 = ByteBuffer.allocate(4);
        bomUtf32.put(0x00);
        bomUtf32.put(0x00);
        bomUtf32.put((byte) 0xFE);
        bomUtf32.put((byte) 0xFF);
        bomUtf32.flip();

        BomCharset bomUtf32Charset = DataUtil.detectCharsetFromBom(bomUtf32);
        assertEquals("UTF-32", bomUtf32Charset.charset);
        assertFalse(bomUtf32Charset.offset);

        ByteBuffer bomUtf16BE = ByteBuffer.allocate(4);
        bomUtf16BE.put((byte) 0xFE);
        bomUtf16BE.put((byte) 0xFF);
        bomUtf16BE.put(0x00);
        bomUtf16BE.put(0x00);
        bomUtf16BE.flip();

        BomCharset bomUtf16BECharset = DataUtil.detectCharsetFromBom(bomUtf16BE);
        assertEquals("UTF-16", bomUtf16BECharset.charset);
        assertFalse(bomUtf16BECharset.offset);

        ByteBuffer bomUtf8 = ByteBuffer.allocate(3);
        bomUtf8.put((byte) 0xEF);
        bomUtf8.put((byte) 0xBB);
        bomUtf8.put((byte) 0xBF);
        bomUtf8.flip();

        BomCharset bomUtf8Charset = DataUtil.detectCharsetFromBom(bomUtf8);
        assertEquals("UTF-8", bomUtf8Charset.charset);
        assertTrue(bomUtf8Charset.offset);

        ByteBuffer emptyBom = ByteBuffer.allocate(0);
        BomCharset emptyBomCharset = DataUtil.detectCharsetFromBom(emptyBom);
        assertNull(emptyBomCharset);
    }
}