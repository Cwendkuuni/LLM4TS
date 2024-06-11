package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

public class DataUtilTest {
    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, null, "http://www.example.com");
        assertNotNull(doc);
        assertEquals("http://www.example.com", doc.baseUri());
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        Document doc = DataUtil.load(inputStream, null, "http://www.example.com");
        assertNotNull(doc);
        assertEquals("http://www.example.com", doc.baseUri());
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        Document doc = DataUtil.load(inputStream, null, "http://www.example.com", Parser.htmlParser());
        assertNotNull(doc);
        assertEquals("http://www.example.com", doc.baseUri());
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream inputStream = new FileInputStream("input.txt");
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        DataUtil.crossStreams(inputStream, byteBuffer);
        assertNotNull(byteBuffer);
        assertEquals(1024, byteBuffer.capacity());
    }

    @Test
    public void testParseInputStream() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        Document doc = DataUtil.parseInputStream(inputStream, null, "http://www.example.com", Parser.htmlParser());
        assertNotNull(doc);
        assertEquals("http://www.example.com", doc.baseUri());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = new FileInputStream("input.txt");
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 1024);
        assertNotNull(byteBuffer);
        assertEquals(1024, byteBuffer.capacity());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String charset1 = DataUtil.getCharsetFromContentType("text/html; charset=EUC-JP");
        assertEquals("EUC-JP", charset1);

        String charset2 = DataUtil.getCharsetFromContentType("text/html; charset=UTF-8");
        assertEquals("UTF-8", charset2);

        String charset3 = DataUtil.getCharsetFromContentType("text/html; charset=ISO-8859-1");
        assertEquals("ISO-8859-1", charset3);

        String charset4 = DataUtil.getCharsetFromContentType(null);
        assertNull(charset4);
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        assertNotNull(mimeBoundary);
        assertEquals(32, mimeBoundary.length());
    }

    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(4);
        byteBuffer.put(new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF });
        byteBuffer.rewind();
        DataUtil.BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);
        assertNotNull(bomCharset);
        assertEquals("UTF-8", bomCharset.charset);
        assertEquals(true, bomCharset.offset);
    }
}