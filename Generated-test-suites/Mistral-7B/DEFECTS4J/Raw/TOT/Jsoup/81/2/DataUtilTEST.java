package org.jsoup.helper.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jsoup.helper.DataUtil;
import org.jsoup.helper.DataUtil.BomCharset;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * Tests for the DataUtil class.
 */
public class DataUtilTest {

    @Test
    public void testLoadFromFileWithDefaultCharset() throws IOException {
        File testFile = new File("testFile.html");
        Document doc = DataUtil.load(testFile, null);
        assertNotNull(doc);
    }

    @Test
    public void testLoadFromFileWithSpecifiedCharset() throws IOException {
        File testFile = new File("testFile.html");
        Document doc = DataUtil.load(testFile, "UTF-8");
        assertNotNull(doc);
    }

    @Test
    public void testLoadFromInputStreamWithDefaultCharset() throws IOException {
        String htmlContent = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, null);
        assertNotNull(doc);
    }

    @Test
    public void testLoadFromInputStreamWithSpecifiedCharset() throws IOException {
        String htmlContent = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, "UTF-8");
        assertNotNull(doc);
    }

    @Test
    public void testLoadFromInputStreamWithInvalidCharset() throws IOException {
        String htmlContent = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document doc = null;
        try {
            doc = DataUtil.load(inputStream, "invalidCharset");
        } catch (IOException e) {
            // expected
        }
        assertNull(doc);
    }

    @Test
    public void testParseInputStreamWithDefaultCharset() throws IOException {
        String htmlContent = "<html><body></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testParseInputStreamWithSpecifiedCharset() throws IOException {
        String htmlContent = "<html><body></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseInputStream(inputStream, "UTF-8", null, Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testParseInputStreamWithError() throws IOException {
        String htmlContent = "<html><body></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document doc = null;
        try {
            doc = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
        } catch (IOException e) {
            // expected
        }
        assertNull(doc);
    }

    @Test
    public void testCrossStreams() throws IOException {
        String source = "Source content";
        String target = "Target content";
        byte[] sourceBytes = source.getBytes(StandardCharsets.UTF_8);
        byte[] targetBytes = new byte[sourceBytes.length];

        try (InputStream sourceInputStream = new ByteArrayInputStream(sourceBytes);
             OutputStream targetOutputStream = new ByteArrayOutputStream()) {
            DataUtil.crossStreams(sourceInputStream, targetOutputStream);
            targetBytes = targetOutputStream.toByteArray();
        }

        assertEquals(source, new String(targetBytes, StandardCharsets.UTF_8));
    }

    @Test
    public void testDetectCharsetFromBOM() throws IOException {
        String utf8Bom = "\uFEFF<html><body></body></html>";
        String utf16Bom = "\uFFFE<html><body></body></html>";
        String utf32Bom = "\u0000\u0000\u00FE\u00FF<html><body></body></html>";

        ByteArrayInputStream utf8BomInputStream = new ByteArrayInputStream(utf8Bom.getBytes(StandardCharsets.UTF_8));
        ByteArrayInputStream utf16BomInputStream = new ByteArrayInputStream(utf16Bom.getBytes(StandardCharsets.UTF_16));
        ByteArrayInputStream utf32BomInputStream = new ByteArrayInputStream(utf32Bom.getBytes(StandardCharsets.UTF_32));

        BomCharset utf8BomCharset = DataUtil.detectCharsetFromBom(DataUtil.readToByteBuffer(utf8BomInputStream));
        BomCharset utf16BomCharset = DataUtil.detectCharsetFromBom(DataUtil.readToByteBuffer(utf16BomInputStream));
        BomCharset utf32BomCharset = DataUtil.detectCharsetFromBom(DataUtil.readToByteBuffer(utf32BomInputStream));

        assertEquals("UTF-8", utf8BomCharset.charset);
        assertEquals(false, utf8BomCharset.offset);

        assertEquals("UTF-16", utf16BomCharset.charset);
        assertEquals(false, utf16BomCharset.offset);

        assertEquals("UTF-32", utf32BomCharset.charset);
        assertEquals(true, utf32BomCharset.offset);
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
        String validCharset = "UTF-8";
        String invalidCharset = "invalidCharset";
        String emptyCharset = "";

        assertEquals(validCharset, DataUtil.validateCharset(validCharset));
        assertNull(DataUtil.validateCharset(null));
        assertNull(DataUtil.validateCharset(emptyCharset));
        assertNull(DataUtil.validateCharset(invalidCharset));
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertNotNull(boundary);
        assertFalse(boundary.isEmpty());
    }
}