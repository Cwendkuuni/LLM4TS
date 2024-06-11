package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class DataUtilTest {
    private File testFile;
    private String charsetName;
    private String baseUri;
    private Parser parser;

    @Before
    public void setUp() {
        testFile = new File("test.html");
        charsetName = "UTF-8";
        baseUri = "http://example.com";
        parser = Parser.htmlParser();
    }

    @After
    public void tearDown() {
        testFile.delete();
    }

    @Test
    public void testLoadFile() throws IOException {
        // create test file
        createTestFile();

        Document document = DataUtil.load(testFile, charsetName, baseUri);

        assertNotNull(document);
        assertEquals("Test Document", document.title());
    }

    @Test(expected = IOException.class)
    public void testLoadFile_NotFound() throws IOException {
        DataUtil.load(new File("nonexistent.html"), charsetName, baseUri);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        // create test file
        createTestFile();

        InputStream inputStream = DataUtilTest.class.getResourceAsStream("test.html");
        Document document = DataUtil.load(inputStream, charsetName, baseUri);

        assertNotNull(document);
        assertEquals("Test Document", document.title());
    }

    @Test(expected = IOException.class)
    public void testLoadInputStream_Null() throws IOException {
        DataUtil.load(null, charsetName, baseUri);
    }

    @Test
    public void testLoadInputStream_Parser() throws IOException {
        // create test file
        createTestFile();

        InputStream inputStream = DataUtilTest.class.getResourceAsStream("test.html");
        Document document = DataUtil.load(inputStream, charsetName, baseUri, Parser.xmlParser());

        assertNotNull(document);
        assertEquals("Test Document", document.title());
    }

    @Test(expected = IOException.class)
    public void testLoadInputStream_Parser_Null() throws IOException {
        DataUtil.load(null, charsetName, baseUri, null);
    }

    @Test
    public void testCrossStreams() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        InputStream inputStream = DataUtilTest.class.getResourceAsStream("test.html");
        DataUtil.crossStreams(inputStream, outputStream);

        byte[] data = outputStream.toByteArray();
        assertNotNull(data);
        assertEquals(96, data.length);
    }

    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.wrap("test".getBytes());
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        assertNotNull(document);
        assertEquals("test", document.body().text());
    }

    @Test
    public void testParseByteData_CharsetNull() {
        ByteBuffer byteData = ByteBuffer.wrap("test".getBytes());
        Document document = DataUtil.parseByteData(byteData, null, baseUri, parser);

        assertNotNull(document);
        assertEquals("test", document.body().text());
    }

    @Test
    public void testParseByteData_CharsetBOM() {
        byte[] data = new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF, (byte) 't', (byte) 'e', (byte) 's', (byte) 't'};
        ByteBuffer byteData = ByteBuffer.wrap(data);
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        assertNotNull(document);
        assertEquals("test", document.body().text());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = DataUtilTest.class.getResourceAsStream("test.html");
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);

        assertNotNull(byteBuffer);
        assertEquals(96, byteBuffer.capacity());
    }

    @Test
    public void testReadToByteBuffer_MaxSize() throws IOException {
        InputStream inputStream = DataUtilTest.class.getResourceAsStream("test.html");
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 50);

        assertNotNull(byteBuffer);
        assertEquals(50, byteBuffer.capacity());
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        // create test file
        createTestFile();

        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(testFile);

        assertNotNull(byteBuffer);
        assertEquals(96, byteBuffer.capacity());
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        assertNotNull(byteBuffer);
        assertEquals(0, byteBuffer.capacity());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String charset = DataUtil.getCharsetFromContentType("text/html; charset=UTF-8");
        assertEquals("UTF-8", charset);

        charset = DataUtil.getCharsetFromContentType("text/html");
        assertNull(charset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertNotNull(boundary);
        assertEquals(32, boundary.length());
    }

    private void createTestFile() throws IOException {
        OutputStream outputStream = new java.io.FileOutputStream(testFile);
        outputStream.write("<html><head><title>Test Document</title></head><body></body></html>".getBytes());
        outputStream.close();
    }
}