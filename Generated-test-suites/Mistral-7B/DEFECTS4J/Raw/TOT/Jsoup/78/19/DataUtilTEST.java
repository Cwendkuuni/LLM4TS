package org.jsoup.helper.test;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.HelperException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class DataUtilTest {

    private DataUtil dataUtil;

    @Before
    public void setup() {
        dataUtil = new DataUtil();
    }

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        File file = new File("test.html");
        Document doc = dataUtil.load(file, null);
        assertEquals("Test document", doc.title());
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        File file = new File("test.html");
        Document doc = dataUtil.load(file, "UTF-16");
        assertNull(doc.title()); // UTF-16 isn't a valid charset for HTML documents
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String htmlContent = "<html><body><h1>Test document</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document doc = dataUtil.load(inputStream, null);
        assertEquals("Test document", doc.title());
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        String htmlContent = "<html><body><h1>Test document</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document doc = dataUtil.load(inputStream, "UTF-16");
        assertNull(doc.title()); // UTF-16 isn't a valid charset for HTML documents
    }

    @Test
    public void testLoadFileWithInvalidCharsetPattern() throws IOException {
        File file = new File("test.html");
        assertThrows(HelperException.class, () -> dataUtil.load(file, "invalidCharset"));
    }

    @Test
    public void testLoadInputStreamWithInvalidCharsetPattern() throws IOException {
        String htmlContent = "<html><body><h1>Test document</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        assertThrows(HelperException.class, () -> dataUtil.load(inputStream, "invalidCharset"));
    }

    @Test
    public void testParseInputStreamWithEmptyContent() throws IOException {
        Document doc = dataUtil.parseInputStream(new ByteArrayInputStream(new byte[0]), null, null, Parser.htmlParser());
        assertEquals(0, doc.childNodeSize());
    }

    @Test
    public void testParseInputStreamWithDefaultCharset() throws IOException {
        String htmlContent = "<html><body><h1>Test document</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document doc = dataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
        assertEquals("Test document", doc.title());
    }

    @Test
    public void testParseInputStreamWithSpecifiedCharset() throws IOException {
        String htmlContent = "<html><body><h1>Test document</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document doc = dataUtil.parseInputStream(inputStream, "UTF-16", null, Parser.htmlParser());
        assertNull(doc.title()); // UTF-16 isn't a valid charset for HTML documents
    }

    @Test
    public void testParseInputStreamWithInvalidCharsetPattern() throws IOException {
        String htmlContent = "<html><body><h1>Test document</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        assertThrows(HelperException.class, () -> dataUtil.parseInputStream(inputStream, "invalidCharset", null, Parser.htmlParser()));
    }

    @Test
    public void testCrossStreams() throws IOException {
        String sourceContent = "Source content";
        String destinationContent = "Destination content";
        String testContent = sourceContent + System.lineSeparator() + destinationContent;

        InputStream sourceStream = new ByteArrayInputStream(sourceContent.getBytes(StandardCharsets.UTF_8));
        OutputStream destinationStream = new ByteArrayOutputStream();

        dataUtil.crossStreams(sourceStream, destinationStream);
        String readContent = new String(destinationStream.toByteArray(), StandardCharsets.UTF_8);

        assertEquals(testContent, readContent);
    }
}