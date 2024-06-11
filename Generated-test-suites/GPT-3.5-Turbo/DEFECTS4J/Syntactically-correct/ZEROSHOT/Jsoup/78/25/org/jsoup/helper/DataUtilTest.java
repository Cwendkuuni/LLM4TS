package org.jsoup.helper;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import static org.junit.Assert.*;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, "UTF-8", "");
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream is = new FileInputStream("test.html");
        Document doc = DataUtil.load(is, "UTF-8", "");
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        InputStream is = new FileInputStream("test.xml");
        Document doc = DataUtil.load(is, "UTF-8", "", Parser.xmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testCrossStreams() throws IOException {
        //TODO: implement test case
    }

    @Test
    public void testParseInputStream() throws IOException {
        InputStream is = new FileInputStream("test.html");
        Document doc = DataUtil.parseInputStream(is, "UTF-8", "", Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream is = new FileInputStream("test.txt");
        ByteBuffer buffer = DataUtil.readToByteBuffer(is, 1024);
        assertNotNull(buffer);
        assertEquals(1024, buffer.capacity());
    }

    @Test
    public void testReadToByteBufferWithoutMaxSize() throws IOException {
        InputStream is = new FileInputStream("test.txt");
        ByteBuffer buffer = DataUtil.readToByteBuffer(is);
        assertNotNull(buffer);
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File file = new File("test.txt");
        ByteBuffer buffer = DataUtil.readFileToByteBuffer(file);
        assertNotNull(buffer);
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer buffer = DataUtil.emptyByteBuffer();
        assertNotNull(buffer);
        assertEquals(0, buffer.capacity());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String charset = DataUtil.getCharsetFromContentType("text/html; charset=UTF-8");
        assertNotNull(charset);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertNotNull(boundary);
        assertEquals(32, boundary.length());
    }

    @Test
    public void testDetectCharsetFromBom() {
        //TODO: implement test case
    }
}