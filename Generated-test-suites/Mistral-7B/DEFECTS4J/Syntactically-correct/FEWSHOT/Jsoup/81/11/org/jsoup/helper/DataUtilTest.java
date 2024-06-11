package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html"); // replace with valid file path
        Document doc = DataUtil.load(file, null, null);
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String html = "<html><body></body></html>";
        InputStream input = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(input, StandardCharsets.UTF_8.name(), null);
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String html = "<html><body></body></html>";
        InputStream input = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(input, StandardCharsets.UTF_8.name(), null, Parser.xmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testParseInputStream() throws IOException {
        String html = "<html><body></body></html>";
        InputStream input = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseInputStream(input, StandardCharsets.UTF_8.name(), null, Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testParseInputStreamWithCharset() throws IOException {
        String html = "<html><body></body></html>";
        InputStream input = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseInputStream(input, "UTF-16LE", null, Parser.htmlParser());
        assertNull(doc);
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputStr = "Test Input Stream";
        InputStream input = new ByteArrayInputStream(inputStr.getBytes(StandardCharsets.UTF_8));
        OutputStream output = new ByteArrayOutputStream();
        DataUtil.crossStreams(input, output);
        assertNotNull(output.toString());
        assertEquals(inputStr, output.toString());
    }
}