package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class DataUtilTest {
    @Test
    public void testLoad() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, "UTF-8", "");
        assertTrue(doc instanceof Document);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream in = new FileInputStream("test.html");
        Document doc = DataUtil.load(in, "UTF-8", "");
        assertTrue(doc instanceof Document);
    }

    @Test
    public void testLoadParser() throws IOException {
        InputStream in = new FileInputStream("test.html");
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.load(in, "UTF-8", "", parser);
        assertTrue(doc instanceof Document);
    }

    @Test
    public void testCrossStreams() {
        // Test not implemented as it is a private method
    }

    @Test
    public void testParseInputStream() throws IOException {
        InputStream in = new FileInputStream("test.html");
        Document doc = DataUtil.parseInputStream(in, "UTF-8", "", Parser.htmlParser());
        assertTrue(doc instanceof Document);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream in = new FileInputStream("test.html");
        assertEquals(1024 * 5, DataUtil.readToByteBuffer(in, 1024 * 5).limit());   
    }

    @Test
    public void testReadToByteBufferDefault() throws IOException {
        InputStream in = new FileInputStream("test.html");
        assertEquals(0, DataUtil.readToByteBuffer(in).limit()); 
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File file = new File("test.html");
        assertEquals(1024 * 5, DataUtil.readFileToByteBuffer(file).limit());
    }

    @Test
    public void testEmptyByteBuffer() {
        assertEquals(0, DataUtil.emptyByteBuffer().limit());
    }

    @Test
    public void testGetCharsetFromContentType() {
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/html; charset=UTF-8"));
        assertNull(DataUtil.getCharsetFromContentType("text/html"));
    }

    @Test
    public void testValidateCharset() {
        assertEquals("UTF-8", DataUtil.validateCharset("UTF-8"));
        assertNull(DataUtil.validateCharset("InvalidCharset"));
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        assertEquals(32, mimeBoundary.length());
    }

    @Test
    public void testDetectCharsetFromBom() {
        // Test not implemented as it is a private method
    }
}