package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
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
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import org.junit.Test;

/**
 * Tests for the DataUtil class.
 */
public class DataUtilTest {

    @Test
    public void testLoadFileWithValidCharset() throws IOException {
        String charset = "UTF-8";
        String fileContent = "<html><head><meta charset='" + charset + "'></head><body>Hello World!</body></html>";
        File testFile = new File("testFile.html");
        try (RandomAccessFile raf = new RandomAccessFile(testFile, "rw");
             OutputStream os = raf) {
            os.write(fileContent.getBytes(StandardCharsets.UTF_8));
        }

        Document document = DataUtil.load(testFile, charset, null);
        assertNotNull(document);
        assertEquals("Hello World!", document.text());
        assertEquals(charset, document.outputSettings().charset());
    }

    @Test
    public void testLoadFileWithNullCharset() throws IOException {
        String fileContent = "<html><head><meta charset='UTF-8'></head><body>Hello World!</body></html>";
        File testFile = new File("testFile.html");
        try (RandomAccessFile raf = new RandomAccessFile(testFile, "rw");
             OutputStream os = raf) {
            os.write(fileContent.getBytes(StandardCharsets.UTF_8));
        }

        Document document = DataUtil.load(testFile, null, null);
        assertNotNull(document);
        assertEquals("Hello World!", document.text());
        assertEquals("UTF-8", document.outputSettings().charset());
    }

    @Test
    public void testLoadFileWithInvalidCharset() throws IOException {
        String fileContent = "<html><head><meta charset='InvalidCharset'></head><body>Hello World!</body></html>";
        File testFile = new File("testFile.html");
        try (RandomAccessFile raf = new RandomAccessFile(testFile, "rw");
             OutputStream os = raf) {
            os.write(fileContent.getBytes(StandardCharsets.UTF_8));
        }

        assertThrows(IllegalArgumentException.class, () -> {
            DataUtil.load(testFile, "InvalidCharset", null);
        });
    }

    @Test
    public void testLoadInputStreamWithValidCharset() throws IOException {
        String charset = "UTF-8";
        String inputStreamContent = "<html><head><meta charset='" + charset + "'></head><body>Hello World!</body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.load(inputStream, charset, null);
        assertNotNull(document);
        assertEquals("Hello World!", document.text());
        assertEquals(charset, document.outputSettings().charset());
    }

    @Test
    public void testLoadInputStreamWithNullCharset() throws IOException {
        String inputStreamContent = "<html><head><meta charset='UTF-8'></head><body>Hello World!</body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.load(inputStream, null, null);
        assertNotNull(document);
        assertEquals("Hello World!", document.text());
        assertEquals("UTF-8", document.outputSettings().charset());
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String inputStreamContent = "<html><head><meta charset='InvalidCharset'></head><body>Hello World!</body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));

        assertThrows(IllegalArgumentException.class, () -> {
            DataUtil.load(inputStream, "InvalidCharset", null);
        });
    }

    @Test
    public void testLoadInputStreamWithEmptyBody() throws IOException {
        Document document = DataUtil.load(new ByteArrayInputStream(new byte[0]), null, null);
        assertNotNull(document);
        assertTrue(document.isEmpty());
        assertEquals("", document.text());
    }

    @Test
    public void testParseInputStreamWithValidCharset() throws IOException {
        String charset = "UTF-8";
        String inputStreamContent = "<html><head><meta charset='" + charset + "'></head><body>Hello World!</body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.load(inputStream, charset, null, Parser.htmlParser());
        assertNotNull(document);
        assertEquals("Hello World!", document.text());
        assertEquals(charset, document.outputSettings().charset());
    }

    @Test
    public void testParseInputStreamWithNullCharset() throws IOException {
        String inputStreamContent = "<html><head><meta charset='UTF-8'></head><body>Hello World!</body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.load(inputStream, null, null, Parser.htmlParser());
        assertNotNull(document);
        assertEquals("Hello World!", document.text());
        assertEquals("UTF-8", document.outputSettings().charset());
    }

    @Test
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String inputStreamContent = "<html><head><meta charset='InvalidCharset'></head><body>Hello World!</body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));

        assertThrows(IllegalArgumentException.class, () -> {
            DataUtil.load(inputStream, "InvalidCharset", null, Parser.htmlParser());
        });
    }

    @Test
    public void testParseInputStreamWithEmptyBody() throws IOException {
        Document document = DataUtil.load(new ByteArrayInputStream(new byte[0]), null, null, Parser.htmlParser());
        assertNotNull(document);
        assertTrue(document.isEmpty());
        assertEquals("", document.text());
    }

    @Test
    public void testCrossStreams() throws IOException {
        String sourceContent = "Source: Hello World!";
        String targetContent = "Target: ";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(sourceContent.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);

        assertEquals("Source: Hello World!", new String(inputStream.readAllBytes()));
        assertEquals("Source: Hello World!Target:", new String(outputStream.toByteArray()));
    }

    @Test
    public void testParseInputStreamWithBOM() throws IOException {
        String charset = "UTF-32";
        String inputStreamContent = "\u0000\u0000\u00FE\u00FF<html><body>Hello World!</body></html>";

        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.load(inputStream, charset, null, Parser.htmlParser());
        assertNotNull(document);
        assertEquals("Hello World!", document.text());
        assertEquals(charset, document.outputSettings().charset());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String expectedCharset = "UTF-8";

        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(expectedCharset, charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithNullContentType() {
        String contentType = null;

        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        String invalidCharset = "InvalidCharset";

        assertEquals(charset, DataUtil.validateCharset(charset));
        assertNull(DataUtil.validateCharset(null));
        assertNull(DataUtil.validateCharset(""));
        assertNull(DataUtil.validateCharset(invalidCharset));
    }

    @Test
    public void testMimeBoundary() {
        String expectedMimeBoundary = "JgJgLmNhOiPjQkRlSzAaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz";
        String mimeBoundary = DataUtil.mimeBoundary();

        assertEquals(expectedMimeBoundary, mimeBoundary);
    }
}