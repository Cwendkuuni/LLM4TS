package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;

public class DataUtilTest {
    private File file;
    private InputStream inputStream;

    @Before
    public void setUp() {
        file = new File("test.html");
        inputStream = new ByteArrayInputStream("<title>Test HTML</title>".getBytes(StandardCharsets.UTF_8));
    }

    @Test
    public void testLoadFromFile() throws IOException {
        Document document = DataUtil.load(file, "UTF-8", "");
        assertEquals("Test HTML", document.title());
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        Document document = DataUtil.load(inputStream, "UTF-8", "");
        assertEquals("Test HTML", document.title());
    }

    @Test
    public void testLoadFromInputStreamWithParser() throws IOException {
        Document document = DataUtil.load(inputStream, "UTF-8", "", Parser.htmlParser());
        assertEquals("Test HTML", document.title());
    }

    @Test
    public void testCrossStreams() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(inputStream, outputStream);
        assertEquals("<title>Test HTML</title>", outputStream.toString());
    }

    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.allocate(300);
        byteData.put("<title>Test HTML</title>".getBytes(StandardCharsets.UTF_8));
        byteData.rewind();
        Document document = DataUtil.parseByteData(byteData, "UTF-8", "", Parser.htmlParser());
        assertEquals("Test HTML", document.title());
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 1024);
        assertEquals("<title>Test HTML</title>", new String(byteBuffer.array(), StandardCharsets.UTF_8));
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        assertEquals("<title>Test HTML</title>", new String(byteBuffer.array(), StandardCharsets.UTF_8));
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
        assertEquals("<title>Test HTML</title>", new String(byteBuffer.array(), StandardCharsets.UTF_8));
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        assertEquals(0, byteBuffer.capacity());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        assertEquals(32, mimeBoundary.length());
    }

    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(300);
        byteBuffer.put(new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF, 60, 116, 105, 116, 108, 101, 62 });
        byteBuffer.rewind();
        String charset = DataUtil.detectCharsetFromBom(byteBuffer, null);
        assertEquals("UTF-8", charset);
    }
}