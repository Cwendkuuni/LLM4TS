package org.jsoup.helper.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import org.jsoup.HelperException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.junit.Test;

public class DataUtilTest {

    @Test
    public void testLoadFileWithValidCharset() throws IOException {
        String charset = "UTF-8";
        File file = new File("path/to/valid/file.html");
        Document doc = DataUtil.load(file, charset, "");
        assertEquals(200, doc.statusCode()); // Assuming the file returns a 200 status code
        assertEquals("html", doc.tagName());
        assertEquals("title", doc.title().tagName());
        assertEquals("Test Page", doc.title().text());
    }

    @Test
    public void testLoadFileWithInvalidCharset() throws IOException {
        String charset = "InvalidCharset";
        File file = new File("path/to/valid/file.html");
        assertThrows(IOException.class, () -> DataUtil.load(file, charset, ""));
    }

    @Test
    public void testLoadFileWithNullCharset() throws IOException {
        File file = new File("path/to/valid/file.html");
        Document doc = DataUtil.load(file, null, "");
        assertTrue(doc != null); // The default charset should be used
        // Perform some checks on the document to ensure the default charset was used correctly
    }

    @Test
    public void testLoadInputStreamWithValidCharset() throws IOException {
        String charset = "UTF-8";
        byte[] htmlContent = "...</html>".getBytes(StandardCharsets.UTF_8);
        InputStream inputStream = new ByteArrayInputStream(htmlContent);
        Document doc = DataUtil.load(inputStream, charset, "");
        assertEquals("html", doc.tagName());
        assertEquals("title", doc.title().tagName());
        assertEquals("Test InputStream", doc.title().text());
    }

    @Test
    public void testLoadInputStreamWithNullCharset() throws IOException {
        byte[] htmlContent = "...</html>".getBytes(StandardCharsets.UTF_8);
        InputStream inputStream = new ByteArrayInputStream(htmlContent);
        Document doc = DataUtil.load(inputStream, null);
        assertTrue(doc != null); // The default charset should be used
        // Perform some checks on the document to ensure the default charset was used correctly
    }

    @Test(expected = HelperException.class)
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String charset = "InvalidCharset";
        byte[] htmlContent = "...</html>".getBytes(StandardCharsets.UTF_8);
        InputStream inputStream = new ByteArrayInputStream(htmlContent);
        DataUtil.load(inputStream, charset, "");
    }

    @Test
    public void testLoadInputStreamWithEmptyContent() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Document doc = DataUtil.load(inputStream, "UTF-8", "");
        assertTrue(doc != null);
        assertEquals(0, doc.childNodeSize());
    }

    @Test
    public void testParseInputStreamWithValidCharset() throws IOException {
        String charset = "UTF-8";
        byte[] htmlContent = "...</html>".getBytes(StandardCharsets.UTF_8);
        InputStream inputStream = new ByteArrayInputStream(htmlContent);
        Document doc = DataUtil.parseInputStream(inputStream, charset, "", Parser.htmlParser());
        assertEquals("html", doc.tagName());
        assertEquals("title", doc.title().tagName());
        assertEquals("Test InputStream", doc.title().text());
    }

    @Test
    public void testParseInputStreamWithNullCharset() throws IOException {
        byte[] htmlContent = "...</html>".getBytes(StandardCharsets.UTF_8);
        InputStream inputStream = new ByteArrayInputStream(htmlContent);
        Document doc = DataUtil.parseInputStream(inputStream, null);
        assertTrue(doc != null); // The default charset should be used
        // Perform some checks on the document to ensure the default charset was used correctly
    }

    @Test(expected = HelperException.class)
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String charset = "InvalidCharset";
        byte[] htmlContent = "...</html>".getBytes(StandardCharsets.UTF_8);
        InputStream inputStream = new ByteArrayInputStream(htmlContent);
        DataUtil.parseInputStream(inputStream, charset, "", Parser.htmlParser());
    }

    @Test
    public void testParseInputStreamWithEmptyContent() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Document doc = DataUtil.parseInputStream(inputStream, "UTF-8", "");
        assertTrue(doc != null);
        assertEquals(0, doc.childNodeSize());
    }

    @Test
    public void testCrossStreams() throws IOException {
        String source = "Source text";
        String destination = "Destination text";

        byte[] sourceBytes = source.getBytes(StandardCharsets.UTF_8);
        byte[] destinationBytes = new byte[destination.length()];

        InputStream inputStream = new ByteArrayInputStream(sourceBytes);
        OutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);
        byte[] outputBytes = outputStream.toByteArray();

        assertEquals(source.length(), outputBytes.length);
        assertArrayEquals(sourceBytes, outputBytes);
    }

    @Test
    public void testParseInputStreamWithBomUtf16() throws IOException {
        String charset = "UTF-16";
        byte[] htmlContent = {0xFE, 0xFF, 0x00, 0x00, "...</html>".getBytes()[0], "...</html>".getBytes()[1]};
        InputStream inputStream = new ByteArrayInputStream(htmlContent);
        Document doc = DataUtil.parseInputStream(inputStream, charset, "", Parser.htmlParser());
        assertTrue(doc != null);
        // Perform some checks on the document to ensure the UTF-16 BOM was handled correctly
    }

    @Test
    public void testParseInputStreamWithBomUtf32() throws IOException {
        String charset = "UTF-32";
        byte[] htmlContent = {0x00, 0x00, 0xFE, 0xFF, "...</html>".getBytes()[0], "...</html>".getBytes()[1]};
        InputStream inputStream = new ByteArrayInputStream(htmlContent);
        Document doc = DataUtil.parseInputStream(inputStream, charset, "", Parser.htmlParser());
        assertTrue(doc != null);
        // Perform some checks on the document to ensure the UTF-32 BOM was handled correctly
    }

    @Test
    public void testParseInputStreamWithoutBomUtf8() throws IOException {
        String charset = "UTF-8";
        byte[] htmlContent = "...</html>".getBytes(); // No BOM
        InputStream inputStream = new ByteArrayInputStream(htmlContent);
        Document doc = DataUtil.parseInputStream(inputStream, charset, "", Parser.htmlParser());
        assertTrue(doc != null);
        // Perform some checks on the document to ensure the UTF-8 BOM was not incorrectly handled
    }

    @Test
    public void testParseInputStreamWithoutBomOther() throws IOException {
        String charset = "InvalidCharset";
        byte[] htmlContent = "...</html>".getBytes(); // No BOM
        InputStream inputStream = new ByteArrayInputStream(htmlContent);
        Document doc = DataUtil.parseInputStream(inputStream, charset, "", Parser.htmlParser());
        assertNull(doc);
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

        charset = "ISO-8859-1";
        validatedCharset = DataUtil.validateCharset(charset);
        assertEquals(charset, validatedCharset);

        charset = "";
        validatedCharset = DataUtil.validateCharset(charset);
        assertNull(validatedCharset);

        charset = "InvalidCharset";
        validatedCharset = DataUtil.validateCharset(charset);
        assertNull(validatedCharset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertTrue(boundary.length() == 32);
        // Perform some checks on the boundary string to ensure it's a valid MIME boundary
    }
}