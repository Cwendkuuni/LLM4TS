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
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DataUtilTest {
    // Test variables
    private Document expectedDocument;
    private Document actualDocument;
    private static final String TEST_FILE_PATH = "testData.html";
    private static final String TEST_UTF8_FILE_PATH = "testData_utf8.html";
    private static final String TEST_UTF16LE_FILE_PATH = "testData_utf16le.html";
    private static final String TEST_UTF16BE_FILE_PATH = "testData_utf16be.html";
    private static final String TEST_UTF32LE_FILE_PATH = "testData_utf32le.html";
    private static final String TEST_UTF32BE_FILE_PATH = "testData_utf32be.html";
    private static final String TEST_XML_FILE_PATH = "testData.xml";
    private static final String TEST_XML_UTF8_FILE_PATH = "testData_utf8.xml";
    private static final String TEST_XML_UTF16LE_FILE_PATH = "testData_utf16le.xml";
    private static final String TEST_XML_UTF16BE_FILE_PATH = "testData_utf16be.xml";
    private static final String TEST_XML_UTF32LE_FILE_PATH = "testData_utf32le.xml";
    private static final String TEST_XML_UTF32BE_FILE_PATH = "testData_utf32be.xml";
    private static final String TEST_EMPTY_FILE_PATH = "testData_empty.html";
    private static final String TEST_INVALID_FILE_PATH = "testData_invalid.html";

    @Before
    public void setUp() throws Exception {
        // Set up test data
        // ... initialize test documents, etc.
    }

    @After
    public void tearDown() throws Exception {
        // Clean up test data
        // ... close files, etc.
    }

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        actualDocument = DataUtil.load(new File(TEST_FILE_PATH), null, null);
        assertEquals(expectedDocument, actualDocument);
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        actualDocument = DataUtil.load(new File(TEST_FILE_PATH), "UTF-8", null);
        assertEquals(expectedDocument, actualDocument);
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidCharset() throws IOException {
        DataUtil.load(new File(TEST_FILE_PATH), "invalidCharset", null);
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        InputStream inputStream = new FileInputStream(new File(TEST_FILE_PATH));
        actualDocument = DataUtil.load(inputStream, null, null);
        assertEquals(expectedDocument, actualDocument);
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        InputStream inputStream = new FileInputStream(new File(TEST_FILE_PATH));
        actualDocument = DataUtil.load(inputStream, "UTF-8", null);
        assertEquals(expectedDocument, actualDocument);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        InputStream inputStream = new FileInputStream(new File(TEST_FILE_PATH));
        DataUtil.load(inputStream, "invalidCharset", null);
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        InputStream inputStream = new FileInputStream(new File(TEST_XML_FILE_PATH));
        actualDocument = DataUtil.load(inputStream, null, null, Parser.xmlParser());
        assertNotNull(actualDocument.xml());
    }

    @Test(expected = IOException.class)
    public void testCrossStreamsWithInvalidInput() throws IOException {
        InputStream invalidInput = new ByteArrayInputStream(new byte[0]);
        OutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(invalidInput, outputStream);
    }

    @Test
    public void testParseInputStream() throws IOException {
        InputStream inputStream = new FileInputStream(new File(TEST_FILE_PATH));
        actualDocument = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
        assertEquals(expectedDocument, actualDocument);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        File file = new File(TEST_FILE_PATH);
        InputStream inputStream = new FileInputStream(file);
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 0);
        // Add assertions here to test the byte buffer contents
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType(contentType));
    }

    @Test
    public void testValidateCharset() {
        assertEquals("UTF-8", DataUtil.validateCharset("UTF-8"));
        assertEquals("UTF-16", DataUtil.validateCharset("UTF-16"));
        assertEquals("UTF-32", DataUtil.validateCharset("UTF-32"));
        assertNull(DataUtil.validateCharset("invalidCharset"));
        assertNull(DataUtil.validateCharset(""));
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        // Add assertions here to test the generated boundary string
    }

    @Test
    public void testDetectCharsetFromBom() throws IOException {
        ByteBuffer byteData = DataUtil.readToByteBuffer(new FileInputStream(new File(TEST_UTF8_FILE_PATH)), 12);
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteData);
        assertEquals("UTF-8", bomCharset.charset);
        assertEquals(false, bomCharset.offset);

        byteData = DataUtil.readToByteBuffer(new FileInputStream(new File(TEST_UTF16LE_FILE_PATH)), 2);
        bomCharset = DataUtil.detectCharsetFromBom(byteData);
        assertEquals("UTF-16", bomCharset.charset);
        assertEquals(true, bomCharset.offset);

        byteData = DataUtil.readToByteBuffer(new FileInputStream(new File(TEST_UTF16BE_FILE_PATH)), 2);
        bomCharset = DataUtil.detectCharsetFromBom(byteData);
        assertEquals("UTF-16", bomCharset.charset);
        assertEquals(false, bomCharset.offset);

        byteData = DataUtil.readToByteBuffer(new FileInputStream(new File(TEST_UTF32LE_FILE_PATH)), 4);
        bomCharset = DataUtil.detectCharsetFromBom(byteData);
        assertEquals("UTF-32", bomCharset.charset);
        assertEquals(true, bomCharset.offset);

        byteData = DataUtil.readToByteBuffer(new FileInputStream(new File(TEST_UTF32BE_FILE_PATH)), 4);
        bomCharset = DataUtil.detectCharsetFromBom(byteData);
        assertEquals("UTF-32", bomCharset.charset);
        assertEquals(false, bomCharset.offset);

        byteData = DataUtil.readToByteBuffer(new FileInputStream(new File(TEST_XML_UTF8_FILE_PATH)), 2);
        bomCharset = DataUtil.detectCharsetFromBom(byteData);
        assertEquals("UTF-8", bomCharset.charset);
        assertEquals(false, bomCharset.offset);

        byteData = DataUtil.readToByteBuffer(new FileInputStream(new File(TEST_XML_UTF16LE_FILE_PATH)), 2);
        bomCharset = DataUtil.detectCharsetFromBom(byteData);
        assertEquals("UTF-16", bomCharset.charset);
        assertEquals(true, bomCharset.offset);

        byteData = DataUtil.readToByteBuffer(new FileInputStream(new File(TEST_XML_UTF16BE_FILE_PATH)), 2);
        bomCharset = DataUtil.detectCharsetFromBom(byteData);
        assertEquals("UTF-16", bomCharset.charset);
        assertEquals(false, bomCharset.offset);

        byteData = DataUtil.readToByteBuffer(new FileInputStream(new File(TEST_XML_UTF32LE_FILE_PATH)), 8);
        bomCharset = DataUtil.detectCharsetFromBom(byteData);
        assertEquals("UTF-32", bomCharset.charset);
        assertEquals(true, bomCharset.offset);

        byteData = DataUtil.readToByteBuffer(new FileInputStream(new File(TEST_XML_UTF32BE_FILE_PATH)), 8);
        bomCharset = DataUtil.detectCharsetFromBom(byteData);
        assertEquals("UTF-32", bomCharset.charset);
        assertEquals(false, bomCharset.offset);

        byteData = DataUtil.readToByteBuffer(new FileInputStream(new File(TEST_EMPTY_FILE_PATH)), 0);
        bomCharset = DataUtil.detectCharsetFromBom(byteData);
        assertNull(bomCharset);

        byteData = DataUtil.readToByteBuffer(new FileInputStream(new File(TEST_INVALID_FILE_PATH)), 1);
        bomCharset = DataUtil.detectCharsetFromBom(byteData);
        assertNull(bomCharset);
    }
}