package org.jsoup.helper.test;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.jsoup.HelperException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.junit.rules.ExpectedException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DataUtilTest {

    private static final String TEST_FILE = "test.html";
    private static final String TEST_FILE_UTF16LE = "test_utf16le.html";
    private static final String TEST_FILE_UTF16BE = "test_utf16be.html";
    private static final String TEST_FILE_UTF32 = "test_utf32.html";
    private static final String TEST_FILE_UTF8 = "test_utf8.html";
    private static final String TEST_FILE_UTF8_BOM = "test_utf8_bom.html";
    private static final String TEST_FILE_NO_CHARSET = "test_no_charset.html";

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() {
        // Initialize test files if not already present
    }

    @Test
    public void testLoadFileUtf8() throws Exception {
        File file = new File(TEST_FILE_UTF8);
        String baseUri = "http://localhost";
        Document doc = DataUtil.load(file, "UTF-8", baseUri);
        assertNotNull(doc);
        assertEquals("UTF-8", doc.outputSettings().charset());
    }

    @Test
    public void testLoadFileUtf8Bom() throws Exception {
        File file = new File(TEST_FILE_UTF8_BOM);
        String baseUri = "http://localhost";
        Document doc = DataUtil.load(file, null, baseUri);
        assertNotNull(doc);
        assertEquals("UTF-8", doc.outputSettings().charset());
    }

    @Test
    public void testLoadFileUtf16LE() throws Exception {
        File file = new File(TEST_FILE_UTF16LE);
        String baseUri = "http://localhost";
        Document doc = DataUtil.load(file, null, baseUri);
        assertNotNull(doc);
        assertEquals("UTF-16LE", doc.outputSettings().charset());
    }

    @Test
    public void testLoadFileUtf16BE() throws Exception {
        File file = new File(TEST_FILE_UTF16BE);
        String baseUri = "http://localhost";
        Document doc = DataUtil.load(file, null, baseUri);
        assertNotNull(doc);
        assertEquals("UTF-16BE", doc.outputSettings().charset());
    }

    @Test
    public void testLoadFileUtf32() throws Exception {
        File file = new File(TEST_FILE_UTF32);
        String baseUri = "http://localhost";
        Document doc = DataUtil.load(file, null, baseUri);
        assertNotNull(doc);
        assertEquals("UTF-32", doc.outputSettings().charset());
    }

    @Test(expected = HelperException.class)
    public void testLoadFileNoCharset() throws Exception {
        File file = new File(TEST_FILE_NO_CHARSET);
        String baseUri = "http://localhost";
        Document doc = DataUtil.load(file, null, baseUri);
        assertNull(doc);
        expectedException.expect(HelperException.class);
    }

    @Test
    public void testLoadInputStreamUtf8() throws Exception {
        String testXml = "<html><head><meta charset=\"UTF-8\"></meta></head><body><p>Test</p></body></html>";
        InputStream input = new ByteArrayInputStream(testXml.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(input, "UTF-8", null);
        assertNotNull(doc);
        assertEquals("UTF-8", doc.outputSettings().charset());
    }

    @Test
    public void testLoadInputStreamUtf16LE() throws Exception {
        String testXml = "\uFEFF<html><head><meta charset=\"UTF-16LE\"></meta></head><body><p>Test</p></body></html>";
        InputStream input = new ByteArrayInputStream(testXml.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(input, null, null);
        assertNotNull(doc);
        assertEquals("UTF-16LE", doc.outputSettings().charset());
    }

    @Test
    public void testLoadInputStreamUtf16BE() throws Exception {
        String testXml = "\uFFFE<html><head><meta charset=\"UTF-16BE\"></meta></head><body><p>Test</p></body></html>";
        InputStream input = new ByteArrayInputStream(testXml.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(input, null, null);
        assertNotNull(doc);
        assertEquals("UTF-16BE", doc.outputSettings().charset());
    }

    @Test
    public void testLoadInputStreamUtf32() throws Exception {
        String testXml = "\uFEFF\uFEFF<html><head><meta charset=\"UTF-32\"></meta></head><body><p>Test</p></body></html>";
        InputStream input = new ByteArrayInputStream(testXml.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(input, null, null);
        assertNotNull(doc);
        assertEquals("UTF-32", doc.outputSettings().charset());
    }

    @Test(expected = HelperException.class)
    public void testLoadInputStreamNoCharset() throws Exception {
        String testXml = "<html><body><p>Test</p></body></html>";
        InputStream input = new ByteArrayInputStream(testXml.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(input, null, null);
        assertNull(doc);
        expectedException.expect(HelperException.class);
    }

    @Test
    public void testCrossStreams() throws Exception {
        String source = "<html><body><p>Source</p></body></html>";
        String target = "<html><body><p>Target</p></body></html>";
        InputStream input = new ByteArrayInputStream(source.getBytes(StandardCharsets.UTF_8));
        OutputStream output = new ByteArrayOutputStream();
        DataUtil.crossStreams(input, output);
        Document doc = DataUtil.parseByteData(ByteBuffer.wrap(output.toByteArray()), null, null, Parser.htmlParser());
        assertNotNull(doc);
        assertEquals(target, doc.toString());
    }
}