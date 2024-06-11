package org.jsoup.helper;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.junit.Test;

/**
 * Test class for DataUtil.
 */
public class DataUtilTest {

    /**
     * Test loading a file with different charsets.
     */
    @Test
    public void testLoadFile() throws IOException {
        String utf8FilePath = "testFiles/DataUtilTest_UTF8.html";
        String utf16FilePath = "testFiles/DataUtilTest_UTF16.html";
        String invalidCharsetFilePath = "testFiles/DataUtilTest_INVALID_CHARSET.html";

        // Test UTF-8 file
        Document utf8Document = DataUtil.load(new File(utf8FilePath), "UTF-8", null);
        assertNotNull(utf8Document);
        assertEquals(utf8FilePath, utf8Document.baseUri());

        // Test UTF-16 file
        Document utf16Document = DataUtil.load(new File(utf16FilePath), null, null);
        assertNotNull(utf16Document);
        assertEquals(utf16FilePath, utf16Document.baseUri());

        // Test invalid charset file
        Document invalidCharsetDocument = DataUtil.load(new File(invalidCharsetFilePath), "INVALID_CHARSET", null);
        assertNull(invalidCharsetDocument);

        // Test UTF-8 file with explicit parser
        Document utf8DocumentFromParser = DataUtil.load(new File(utf8FilePath), "UTF-8", null, Parser.xmlParser());
        assertEquals(utf8Document, utf8DocumentFromParser);
    }

    /**
     * Test parsing a stream with different charsets.
     */
    @Test
    public void testParseInputStream() throws IOException {
        String utf8Text = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<title>Utf-8 Test</title>\n</head>\n<body>\n</body>\n";
        String utf16Text = "\uFEFF<?xml version=\"1.0\" encoding=\"UTF-16\"?>\n<html>\n<head>\n<title>Utf-16 Test</title>\n</head>\n<body>\n</body>\n";
        String invalidCharsetText = "\uFEFF<?xml version=\"1.0\" encoding=\"INVALID_CHARSET\"?>\n<html>\n<head>\n<title>Utf-16 Test</title>\n</head>\n<body>\n</body>\n";

        ByteArrayInputStream utf8InputStream = new ByteArrayInputStream(utf8Text.getBytes(Charset.forName("UTF-8")));
        ByteArrayInputStream utf16InputStream = new ByteArrayInputStream(utf16Text.getBytes(Charset.forName("UTF-16")));
        ByteArrayInputStream invalidCharsetInputStream = new ByteArrayInputStream(invalidCharsetText.getBytes(Charset.forName("UTF-8")));

        // Test UTF-8 stream
        Document utf8Document = DataUtil.load(utf8InputStream, "UTF-8", null);
        assertNotNull(utf8Document);
        assertEquals(utf8Text, utf8Document.body().html());

        // Test UTF-16 stream
        Document utf16Document = DataUtil.load(utf16InputStream, null, null);
        assertNotNull(utf16Document);
        assertEquals(utf16Text, utf16Document.body().html());

        // Test invalid charset stream
        Document invalidCharsetDocument = DataUtil.load(invalidCharsetInputStream, "INVALID_CHARSET", null);
        assertNull(invalidCharsetDocument);

        // Test UTF-8 stream with explicit parser
        Document utf8DocumentFromParser = DataUtil.load(utf8InputStream, "UTF-8", null, Parser.xmlParser());
        assertEquals(utf8Document, utf8DocumentFromParser);
    }

    /**
     * Test writing to and reading from streams.
     */
    @Test
    public void testCrossStreams() throws IOException {
        String testString = "Test string.";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testString.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);
        String outputString = outputStream.toString();
        assertNotNull(outputString);
        assertEquals(testString, outputString);
    }

    /**
     * Test parsing an empty input stream.
     */
    @Test
    public void testParseEmptyInputStream() throws IOException {
        InputStream emptyInputStream = new ByteArrayInputStream(new byte[0]);
        Document document = DataUtil.parseInputStream(emptyInputStream, "UTF-8", null, Parser.htmlParser());
        assertNotNull(document);
        assertTrue(document.childNodeSize() == 0);
    }

    /**
     * Test detecting charset from BOM.
     */
    @Test
    public void testDetectCharsetFromBom() throws IOException {
        ByteBuffer utf32Bom = ByteBuffer.wrap(new byte[] { 0, 0, 0xFE, 0xFF });
        ByteBuffer utf16Bom = ByteBuffer.wrap(new byte[] { 0xFF, 0xFE, 0, 0 });
        ByteBuffer utf8Bom = ByteBuffer.wrap(new byte[] { 0xEF, 0xBB, 0xBF });

        BomCharset utf32Charset = DataUtil.detectCharsetFromBom(utf32Bom);
        BomCharset utf16Charset = DataUtil.detectCharsetFromBom(utf16Bom);
        BomCharset utf8Charset = DataUtil.detectCharsetFromBom(utf8Bom);

        assertEquals("UTF-32", utf32Charset.charset);
        assertEquals(0, utf32Charset.offset);

        assertEquals("UTF-16", utf16Charset.charset);
        assertEquals(0, utf16Charset.offset);

        assertEquals("UTF-8", utf8Charset.charset);
        assertEquals(3, utf8Charset.offset);
    }

    private Document newDocument() throws IOException {
        String testString = "Test string.";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testString.getBytes());
        Document document = DataUtil.parseInputStream(inputStream, "UTF-8", null, Parser.htmlParser());
        inputStream.close();
        return document;
    }

    /**
     * Test detecting charset from HTML meta tags.
     */
    @Test
    public void testGetCharsetFromContentType() throws IOException {
        Document document = newDocument();
        Element metaElement = document.selectFirst("meta[charset]");
        String charset = DataUtil.getCharsetFromContentType(metaElement.attr("content"));

        assertEquals("UTF-8", charset);
    }

    /**
     * Test validating charsets.
     */
    @Test
    public void testValidateCharset() {
        String csUtf8 = "UTF-8";
        String csUtf16 = "UTF-16";
        String csInvalidCharset = "INVALID_CHARSET";
        String csInvalidCharsetUpper = "INVALID_CHARSET".toUpperCase(Locale.ENGLISH);

        String csUtf8Trimmed = csUtf8.trim();
        String csUtf16Trimmed = csUtf16.trim();

        assertTrue(DataUtil.validateCharset(csUtf8) != null);
        assertTrue(DataUtil.validateCharset(csUtf8Trimmed) != null);
        assertTrue(DataUtil.validateCharset(csUtf16) != null);
        assertTrue(DataUtil.validateCharset(csUtf16Trimmed) != null);
        assertNull(DataUtil.validateCharset(csInvalidCharset));
        assertNull(DataUtil.validateCharset(csInvalidCharsetUpper));
    }

    /**
     * Test creating random mime boundaries.
     */
    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();

        assertEquals(DataUtil.boundaryLength, mimeBoundary.length());
        for (int i = 0; i < DataUtil.boundaryLength; i++) {
            Character randomChar = mimeBoundary.charAt(i);
            assertTrue(DataUtil.mimeBoundaryChars[Math.abs(RandomUtils.nextInt())] == randomChar);
        }
    }
}