package org.jsoup.helper.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import org.junit.Test;

public class DataUtilTest {

    @Test
    public void testLoadFileWithValidCharset() throws IOException {
        String charset = "UTF-8";
        File file = new File("testFile.html"); // should exist and be valid UTF-8 encoded file
        Document doc = DataUtil.load(file, charset, "");

        assertTrue(doc != null);
        assertEquals("text/html", doc.outputSettings().contentType());
        assertEquals(charset, doc.outputSettings().charset());
    }

    @Test
    public void testLoadFileWithInvalidCharset() throws IOException {
        String charset = "InvalidCharset";
        File file = new File("testFile.html"); // should exist and be valid UTF-8 encoded file
        Document doc = null;

        try {
            doc = DataUtil.load(file, charset, "");
        } catch (IOException e) {
            assertEquals("Error parsing input as " + charset, e.getMessage());
        }

        assertNull(doc);
    }

    @Test
    public void testLoadInputStreamWithValidCharset() throws IOException {
        String charset = "UTF-8";
        String htmlContent = "<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, charset, "");

        assertTrue(doc != null);
        assertEquals("text/html", doc.outputSettings().contentType());
        assertEquals(charset, doc.outputSettings().charset());
        assertEquals(htmlContent, doc.toString());
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String charset = "InvalidCharset";
        String htmlContent = "<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document doc = null;

        try {
            doc = DataUtil.load(inputStream, charset, "");
        } catch (IOException e) {
            assertEquals("Error parsing input as " + charset, e.getMessage());
        }

        assertNull(doc);
    }

    @Test
    public void testLoadInputStreamWithNullCharset() throws IOException {
        String htmlContent = "<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document doc = null;

        try {
            doc = DataUtil.load(inputStream, null, "");
        } catch (IOException e) {
            assertTrue(e.getMessage().startsWith("Must set charset arg"));
        }

        assertNull(doc);
    }

    @Test
    public void testParseInputStreamWithValidCharset() throws IOException {
        String charset = "UTF-8";
        String htmlContent = "<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseInputStream(inputStream, charset, "", Parser.htmlParser());

        assertTrue(doc != null);
        assertEquals("text/html", doc.outputSettings().contentType());
        assertEquals(charset, doc.outputSettings().charset());
        assertEquals(htmlContent, doc.toString());
    }

    @Test
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String charset = "InvalidCharset";
        String htmlContent = "<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document doc = null;

        try {
            doc = DataUtil.parseInputStream(inputStream, charset, "", Parser.htmlParser());
        } catch (IOException e) {
            assertEquals("Error parsing input as " + charset, e.getMessage());
        }

        assertNull(doc);
    }

    @Test
    public void testParseInputStreamWithNullCharset() throws IOException {
        String htmlContent = "<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document doc = null;

        try {
            doc = DataUtil.parseInputStream(inputStream, null, "", Parser.htmlParser());
        } catch (IOException e) {
            assertTrue(e.getMessage().startsWith("Must set charset arg"));
        }

        assertNull(doc);
    }

    @Test
    public void testCrossStreams() throws IOException {
        String sourceContent = "Source Content";
        String destinationContent = "";
        String charset = "UTF-8";

        // Test with valid input streams
        InputStream source = new ByteArrayInputStream(sourceContent.getBytes(StandardCharsets.UTF_8));
        OutputStream destination = new ByteArrayOutputStream();

        DataUtil.crossStreams(source, destination);

        destinationContent = new String(destination.toByteArray(), StandardCharsets.UTF_8);
        assertEquals(sourceContent, destinationContent);

        source.close();
        destination.close();

        // Test with invalid input streams
        source = new ByteArrayInputStream(new byte[0]);
        destination = new ByteArrayOutputStream();

        try {
            DataUtil.crossStreams(source, destination);
            fail("Expected IOException");
        } catch (IOException e) {
            assertTrue(e.getMessage().startsWith("Stream closed"));
        }

        source.close();
        destination.close();
    }
}