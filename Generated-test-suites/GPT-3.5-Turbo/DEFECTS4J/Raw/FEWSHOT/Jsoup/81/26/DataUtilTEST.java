package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class DataUtilTest {

    @Test
    public void testLoadFromInputStream() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        assertNotNull(DataUtil.load(inputStream, "UTF-8", ""));
        inputStream.close();
    }

    @Test
    public void testLoadFromFile() throws IOException {
        File file = new File("test.html");
        assertNotNull(DataUtil.load(file, "UTF-8", ""));
    }

    @Test
    public void testParseInputStream() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        assertNotNull(DataUtil.parseInputStream(inputStream, "UTF-8", "", Parser.htmlParser()));
        inputStream.close();
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream inputStream = new FileInputStream("file.txt");
        assertNotNull(DataUtil.crossStreams(inputStream, System.out));
        inputStream.close();
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = new FileInputStream("file.txt");
        assertNotNull(DataUtil.readToByteBuffer(inputStream));
        inputStream.close();
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File file = new File("file.txt");
        assertNotNull(DataUtil.readFileToByteBuffer(file));
    }

    @Test
    public void testEmptyByteBuffer() {
        assertEquals(0, DataUtil.emptyByteBuffer().capacity());
    }

    @Test
    public void testGetCharsetFromContentType() {
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/html; charset=UTF-8"));
        assertNull(DataUtil.getCharsetFromContentType(null));
    }

    @Test
    public void testMimeBoundary() {
        assertNotNull(DataUtil.mimeBoundary());
    }

    @Test
    public void testDetectCharsetFromBom() {
        String utf8Text = "\uFEFFHello World";
        assertEquals("UTF-8", DataUtil.detectCharsetFromBom(ByteBuffer.wrap(utf8Text.getBytes())));
    }
}