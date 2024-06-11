package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.InputStreamDocument;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * Test class for DataUtil methods.
 */
public class DataUtilTest {

    @org.junit.Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        File file = new File("testFile.html");
        Document doc = DataUtil.load(file, null);
        assertNotNull(doc);
    }

    @org.junit.Test(expected = IOException.class)
    public void testLoadFileWithInvalidCharset() throws IOException {
        File file = new File("testFile.html");
        Document doc = DataUtil.load(file, "XYZ-123");
        fail("Expected IOException");
    }

    @org.junit.Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String html = "<html><head><meta charset='UTF-8'></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, null);
        assertNotNull(doc);
    }

    @org.junit.Test(expected = IOException.class)
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String invalidHtml = "<html><head><meta charset='XYZ-123'></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(invalidHtml.getBytes(StandardCharsets.UTF_8));
        Document doc = null;
        try {
            doc = DataUtil.load(inputStream, "XYZ-123");
            fail("Expected IOException");
        } catch (IOException e) {
            // Pass
        }
    }

    @org.junit.Test(expected = IOException.class)
    public void testLoadInputStreamWithNullInputStream() throws IOException {
        InputStream inputStream = null;
        Document doc = DataUtil.load(inputStream, null);
        fail("Expected IOException");
    }

    @org.junit.Test
    public void testParseInputStreamWithDetectedCharset() throws IOException {
        String html = "<html><head><meta charset='UTF-8'></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
        assertNotNull(doc);
    }

    @org.junit.Test(expected = IOException.class)
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String invalidHtml = "<html><head><meta charset='XYZ-123'></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(invalidHtml.getBytes(StandardCharsets.UTF_8));
        Document doc = null;
        try {
            doc = DataUtil.parseInputStream(inputStream, "XYZ-123", null, Parser.htmlParser());
            fail("Expected IOException");
        } catch (IOException e) {
            // Pass
        }
    }

    @org.junit.Test(expected = IOException.class)
    public void testParseInputStreamWithNullInputStream() throws IOException {
        InputStream inputStream = null;
        Document doc = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
        fail("Expected IOException");
    }

    @org.junit.Test
    public void testCrossStreams() throws IOException {
        String source = "Source text.";
        String target = "Target text.";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(source.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);

        assertEquals(source, outputStream.toString());
    }
}