package org.jsoup.helper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DataUtilTest {

    private static final String TEST_FILE = "test.html";
    private static final String TEST_CHARSET = "UTF-8";
    private static final String TEST_BASE_URI = "http://example.com";

    @Before
    public void setup() {
        // Set up your test here, if needed.
    }

    @After
    public void teardown() {
        // Tear down your test here, if needed.
    }

    @Test
    public void testLoadFile() throws IOException {
        File testFile = new File(TEST_FILE);
        Document doc = DataUtil.load(testFile, TEST_CHARSET, TEST_BASE_URI);
        assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithNullCharset() throws IOException {
        File testFile = new File(TEST_FILE);
        DataUtil.load(testFile, null, TEST_BASE_URI);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String htmlContent = "<html><head></head><body></body></html>";
        InputStream testInput = new ByteArrayInputStream(htmlContent.getBytes(TEST_CHARSET));
        Document doc = DataUtil.load(testInput, TEST_CHARSET, TEST_BASE_URI);
        assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithNullCharset() throws IOException {
        String htmlContent = "<html><head></head><body></body></html>";
        InputStream testInput = new ByteArrayInputStream(htmlContent.getBytes(TEST_CHARSET));
        DataUtil.load(testInput, null, TEST_BASE_URI);
    }

    @Test
    public void testParseInputStream() throws IOException {
        String htmlContent = "<html><head></head><body></body></html>";
        InputStream testInput = new ByteArrayInputStream(htmlContent.getBytes(TEST_CHARSET));
        Document doc = DataUtil.parseInputStream(testInput, TEST_CHARSET, TEST_BASE_URI, Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testParseInputStreamWithNullCharset() throws IOException {
        String htmlContent = "<html><head></head><body></body></html>";
        InputStream testInput = new ByteArrayInputStream(htmlContent.getBytes(TEST_CHARSET));
        DataUtil.parseInputStream(testInput, null, TEST_BASE_URI, Parser.htmlParser());
    }

    @Test
    public void testParseInputStreamWithXmlParser() throws IOException {
        String xmlContent = "<xml version=\"1.0\" encoding=\"UTF-8\"><root></root></xml>";
        InputStream testInput = new ByteArrayInputStream(xmlContent.getBytes(TEST_CHARSET));
        Document doc = DataUtil.parseInputStream(testInput, TEST_CHARSET, TEST_BASE_URI, Parser.xmlParser());
        assertNotNull(doc);
        Element rootElement = doc.select("root").first();
        assertNotNull(rootElement);
    }

    @Test(expected = IOException.class)
    public void testParseInputStreamWithXmlParserAndNullCharset() throws IOException {
        String xmlContent = "<xml version=\"1.0\" encoding=\"UTF-8\"><root></root></xml>";
        InputStream testInput = new ByteArrayInputStream(xmlContent.getBytes(TEST_CHARSET));
        DataUtil.parseInputStream(testInput, null, TEST_BASE_URI, Parser.xmlParser());
    }

    @Test
    public void testCrossStreams() throws IOException {
        File inputFile = new File("input.txt");
        InputStream input = new FileInputStream(inputFile);
        OutputStream output = new FileOutputStream("output.txt");

        DataUtil.crossStreams(input, output);

        byte[] outputBytes = new byte[inputFile.length()];
        File outputFile = new File("output.txt");
        FileInputStream outputStream = new FileInputStream(outputFile);
        outputStream.read(outputBytes, 0, outputBytes.length);
        outputStream.close();

        assertEquals(inputFile.length(), outputBytes.length);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);

        contentType = "text/html; charset=GB2312";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("GB2312", charset);

        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertNotNull(boundary);
        assertFalse(boundary.isEmpty());
    }

    @Test
    public void testDetectCharsetFromBomUtf32() throws IOException {
        String bomUtf32 = new String(new byte[]{0, 0, (byte) 0xFE, (byte) 0xFF}, "UTF-32");
        ByteBuffer byteData = ByteBuffer.wrap(bomUtf32.getBytes());
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteData);
        assertEquals("UTF-32", bomCharset.charset);
        assertFalse(bomCharset.offset);
    }

    @Test
    public void testDetectCharsetFromBomUtf16() throws IOException {
        String bomUtf16BE = new String(new byte[]{0xFE, 0xFF, 0, 0}, "UTF-16BE");
        ByteBuffer byteData = ByteBuffer.wrap(bomUtf16BE.getBytes());
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteData);
        assertEquals("UTF-16", bomCharset.charset);
        assertFalse(bomCharset.offset);

        String bomUtf16LE = new String(new byte[]{0xFF, 0xFE, 0, 0}, "UTF-16LE");
        byteData = ByteBuffer.wrap(bomUtf16LE.getBytes());
        bomCharset = DataUtil.detectCharsetFromBom(byteData);
        assertEquals("UTF-16", bomCharset.charset);
        assertTrue(bomCharset.offset);
    }

    @Test
    public void testDetectCharsetFromBomUtf8() throws IOException {
        String bomUtf8 = new String(new byte[]{0xEF, 0xBB, 0xBF}, "UTF-8");
        ByteBuffer byteData = ByteBuffer.wrap(bomUtf8.getBytes());
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteData);
        assertEquals("UTF-8", bomCharset.charset);
        assertTrue(bomCharset.offset);
    }

    @Test
    public void testDetectCharsetFromBomNoMatch() throws IOException {
        String testData = new String(new byte[]{0, 1, 2, 3, 4}, "UTF-8");
        ByteBuffer byteData = ByteBuffer.wrap(testData.getBytes());
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteData);
        assertNull(bomCharset);
    }
}