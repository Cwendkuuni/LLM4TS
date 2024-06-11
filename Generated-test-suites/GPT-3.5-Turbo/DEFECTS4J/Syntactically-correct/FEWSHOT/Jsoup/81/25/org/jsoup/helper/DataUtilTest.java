package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.junit.Test;

public class DataUtilTest {
    @Test
    public void testLoad_File() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, "UTF-8", "http://example.com");
        assertTrue(doc != null);
        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testLoad_InputStream() throws IOException {
        InputStream in = new FileInputStream("test.html");
        Document doc = DataUtil.load(in, "UTF-8", "http://example.com");
        assertTrue(doc != null);
        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testLoad_InputStream_Parser() throws IOException {
        InputStream in = new FileInputStream("test.html");
        Document doc = DataUtil.load(in, "UTF-8", "http://example.com", Parser.htmlParser());
        assertTrue(doc != null);
        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream in = new FileInputStream("input.txt");
        OutputStream out = new ByteArrayOutputStream();
        DataUtil.crossStreams(in, out);
        assertTrue(out.size() > 0);
    }

    @Test
    public void testParseInputStream() throws IOException {
        InputStream in = new FileInputStream("input.txt");
        Document doc = DataUtil.parseInputStream(in, "UTF-8", "http://example.com", Parser.htmlParser());
        assertTrue(doc != null);
        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testReadToByteBuffer_InputStream() throws IOException {
        InputStream in = new FileInputStream("input.txt");
        ByteBuffer buffer = DataUtil.readToByteBuffer(in);
        assertTrue(buffer.isDirect());
        assertTrue(buffer.remaining() > 0);
    }

    @Test
    public void testReadToByteBuffer_InputStream_maxSize() throws IOException {
        InputStream in = new FileInputStream("input.txt");
        ByteBuffer buffer = DataUtil.readToByteBuffer(in, 1024);
        assertTrue(buffer.isDirect());
        assertTrue(buffer.remaining() > 0);
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File file = new File("input.txt");
        ByteBuffer buffer = DataUtil.readFileToByteBuffer(file);
        assertTrue(buffer.isDirect());
        assertTrue(buffer.remaining() > 0);
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer buffer = DataUtil.emptyByteBuffer();
        assertTrue(buffer.isDirect());
        assertEquals(0, buffer.remaining());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertEquals(32, boundary.length());
    }
}