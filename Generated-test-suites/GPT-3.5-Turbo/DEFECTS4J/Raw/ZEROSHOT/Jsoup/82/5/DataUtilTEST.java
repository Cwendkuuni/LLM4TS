package org.jsoup.helper;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.io.*;

public class DataUtilTest {

    private static final String TEST_FILE_PATH = "test/test.html";
    private static final String CHARSET_NAME = "UTF-8";
    private static final String BASE_URI = "https://example.com";

    private Document document;

    @Before
    public void setUp() throws Exception {
        File file = new File(TEST_FILE_PATH);
        document = DataUtil.load(file, CHARSET_NAME, BASE_URI);
    }

    @After
    public void tearDown() throws Exception {
        document = null;
    }

    @Test
    public void testLoadFile() throws IOException {
        File file = new File(TEST_FILE_PATH);
        Document doc = DataUtil.load(file, CHARSET_NAME, BASE_URI);

        assertNotNull(doc);
        assertEquals(BASE_URI, doc.baseUri());
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inputStream = new FileInputStream(TEST_FILE_PATH);
        Document doc = DataUtil.load(inputStream, CHARSET_NAME, BASE_URI);

        assertNotNull(doc);
        assertEquals(BASE_URI, doc.baseUri());
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        InputStream inputStream = new FileInputStream(TEST_FILE_PATH);
        Document doc = DataUtil.load(inputStream, CHARSET_NAME, BASE_URI, Parser.htmlParser());

        assertNotNull(doc);
        assertEquals(BASE_URI, doc.baseUri());
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream in = new ByteArrayInputStream("test".getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        DataUtil.crossStreams(in, out);

        assertEquals("test", out.toString());
    }

    @Test
    public void testParseInputStream() throws IOException {
        InputStream inputStream = new FileInputStream(TEST_FILE_PATH);
        Document doc = DataUtil.parseInputStream(inputStream, CHARSET_NAME, BASE_URI, Parser.htmlParser());

        assertNotNull(doc);
        assertEquals(BASE_URI, doc.baseUri());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = new FileInputStream(TEST_FILE_PATH);
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 1024);

        assertNotNull(byteBuffer);
        assertTrue(byteBuffer.limit() > 0);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();

        assertNotNull(boundary);
        assertTrue(boundary.length() > 0);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertNotNull(charset);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentType_NoCharset() {
        String contentType = "text/html";

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertNull(charset);
    }

    @Test
    public void testValidateCharset() {
        String charset = "utf-8";

        String validatedCharset = DataUtil.validateCharset(charset);

        assertNotNull(validatedCharset);
        assertEquals("UTF-8", validatedCharset);
    }

    @Test
    public void testValidateCharset_InvalidCharset() {
        String charset = "invalid-charset";

        String validatedCharset = DataUtil.validateCharset(charset);

        assertNull(validatedCharset);
    }

    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(4);
        byte[] bom = new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};
        byteBuffer.put(bom);
        byteBuffer.rewind();

        DataUtil.BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);

        assertNotNull(bomCharset);
        assertEquals("UTF-8", bomCharset.charset);
        assertTrue(bomCharset.offset);
    }

    @Test
    public void testDetectCharsetFromBom_NoBom() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(4);
        byte[] noBom = new byte[]{(byte) 0x41, (byte) 0x42, (byte) 0x43, (byte) 0x44};
        byteBuffer.put(noBom);
        byteBuffer.rewind();

        DataUtil.BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);

        assertNull(bomCharset);
    }
}