package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.Locale;

public class DataUtilTest {
    private File file;
    private String charsetName;
    private String baseUri;
    private Parser parser;

    @Before
    public void setUp() {
        file = new File("testFile.html");
        charsetName = "UTF-8";
        baseUri = "http://example.com";
        parser = Parser.htmlParser();
    }

    @Test
    public void testLoadFile() throws IOException {
        Document doc = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream in = new FileInputStream(file);
        Document doc = DataUtil.load(in, charsetName, baseUri);
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        InputStream in = new FileInputStream(file);
        Document doc = DataUtil.load(in, charsetName, baseUri, parser);
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream in = new ByteArrayInputStream("Test String".getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataUtil.crossStreams(in, out);
        assertEquals("Test String", out.toString());
    }

    @Test
    public void testParseInputStream() throws IOException {
        InputStream in = new FileInputStream(file);
        Document doc = DataUtil.parseInputStream(in, charsetName, baseUri, parser);
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream in = new FileInputStream(file);
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(in, 1024);
        assertNotNull(byteBuffer);
        assertEquals(1024, byteBuffer.limit());
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        assertNotNull(byteBuffer);
        assertEquals(0, byteBuffer.limit());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=EUC-JP";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNotNull(charset);
        assertEquals("EUC-JP", charset);
    }

    @Test
    public void testValidateCharset() {
        String cs = "UTF-8";
        String validatedCharset = DataUtil.validateCharset(cs);
        assertNotNull(validatedCharset);
        assertEquals("UTF-8", validatedCharset);
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        assertNotNull(mimeBoundary);
        assertEquals(DataUtil.boundaryLength, mimeBoundary.length());
    }

    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer byteData = ByteBuffer.wrap(new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF });
        DataUtil.BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteData);
        assertNotNull(bomCharset);
        assertEquals("UTF-8", bomCharset.charset);
        assertTrue(bomCharset.offset);
    }
}