package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * Test class for DataUtil.
 */
public class DataUtilTest {
    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        File file = new File("test.html"); // Replace with a valid file path
        Document doc = DataUtil.load(file, null);
        assertNotNull(doc);
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        File file = new File("test.html"); // Replace with a valid file path
        Document doc = DataUtil.load(file, "UTF-8");
        assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithNullInput() {
        Document doc = DataUtil.load(null, "UTF-8", "baseUri");
        fail("Expected IOException, but got: " + doc);
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        InputStream inputStream = this.getClass().getResourceAsStream("/test.html"); // Replace with a valid resource path
        Document doc = DataUtil.load(inputStream, null);
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        InputStream inputStream = this.getClass().getResourceAsStream("/test.html"); // Replace with a valid resource path
        Document doc = DataUtil.load(inputStream, "UTF-8");
        assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithNullInput() {
        Document doc = DataUtil.load(null, "UTF-8", "baseUri");
        fail("Expected IOException, but got: " + doc);
    }

    @Test
    public void testParseInputStreamWithDefaultCharset() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String testData = "<html><head></head><body></body></html>";
        OutputStream writer = outputStream;
        writer.write(testData.getBytes());
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Document doc = DataUtil.parseInputStream(inputStream, null, null, DataUtil.Parser.htmlParser());
        assertNotNull(doc);
        assertEquals(testData, doc.body().html());
    }

    @Test
    public void testParseInputStreamWithSpecifiedCharset() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String testData = "<html><head></head><body></body></html>";
        OutputStream writer = outputStream;
        writer.write(testData.getBytes("UTF-16LE"));
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Document doc = DataUtil.parseInputStream(inputStream, "UTF-16LE", null, DataUtil.Parser.htmlParser());
        assertNotNull(doc);
        assertEquals(testData, doc.body().html());
    }

    @Test(expected = IOException.class)
    public void testParseInputStreamWithNullInput() {
        Document doc = DataUtil.parseInputStream(null, null, null, DataUtil.Parser.htmlParser());
        fail("Expected IOException, but got: " + doc);
    }

    @Test(expected = IOException.class)
    public void testCrossStreamsWithNullInputStream() throws IOException {
        InputStream in = null;
        OutputStream out = new ByteArrayOutputStream();
        DataUtil.crossStreams(in, out);
        fail("Expected IOException, but got: " + out.toString());
    }
}