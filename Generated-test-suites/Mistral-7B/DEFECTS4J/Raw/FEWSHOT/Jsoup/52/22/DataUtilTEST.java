package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class DataUtilTest {

    private static final String TEST_FILE_CONTENTS = "Test file contents.";
    private static final String CHARSET_UTF8 = "UTF-8";
    private static final String CHARSET_GB2312 = "GB2312";

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("testFile.txt");
        ByteBuffer byteData = DataUtil.readFileToByteBuffer(file);
        Document doc = DataUtil.parseByteData(byteData, CHARSET_UTF8, null, Parser.htmlParser());
        assertEquals("Test file contents.", doc.body().text());
    }

    @Test
    public void testLoadInputStream() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(TEST_FILE_CONTENTS.getBytes(CHARSET_UTF8));
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Document doc = DataUtil.load(inputStream, CHARSET_UTF8, null);
        assertEquals("Test file contents.", doc.body().text());
    }

    @Test
    public void testDetectCharsetFromBom() {
        String testDataUtf8 = "\uFEFFTest file contents.";
        ByteBuffer byteDataUtf8 = ByteBuffer.wrap(testDataUtf8.getBytes(CHARSET_UTF8));
        assertEquals(CHARSET_UTF32BE, DataUtil.detectCharsetFromBom(byteDataUtf8, null));
        assertEquals(CHARSET_UTF16BE, DataUtil.detectCharsetFromBom(ByteBuffer.wrap(testDataUtf8.getBytes(CHARSET_UTF16BE)), null));
        assertEquals(CHARSET_UTF8, DataUtil.detectCharsetFromBom(ByteBuffer.wrap(testDataUtf8.getBytes(CHARSET_UTF8)), CHARSET_UTF8));
    }

    @Test
    public void testLoadWithInvalidCharset() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(TEST_FILE_CONTENTS.getBytes(CHARSET_UTF8));

        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Document doc = null;

        try {
            doc = DataUtil.load(inputStream, "InvalidCharset", null);
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }

        assertNull(doc);
    }

    @Test
    public void testParseByteDataWithInvalidCharset() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(TEST_FILE_CONTENTS.getBytes(CHARSET_UTF8));

        ByteBuffer byteData = ByteBuffer.wrap(outputStream.toByteArray());
        Document doc = null;

        try {
            doc = DataUtil.parseByteData(byteData, "InvalidCharset", null, Parser.htmlParser());
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }

        assertNull(doc);
    }

    @Test
    public void testParseByteDataWithEmptyData() throws IOException {
        ByteBuffer byteData = DataUtil.emptyByteBuffer();
        Document doc = DataUtil.parseByteData(byteData, CHARSET_UTF8, null, Parser.htmlParser());
        assertNull(doc);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        assertEquals(CHARSET_UTF8, DataUtil.getCharsetFromContentType(contentType));

        contentType = "text/html; charset=gb2312";
        assertEquals(CHARSET_GB2312, DataUtil.getCharsetFromContentType(contentType));

        contentType = "text/html";
        assertNull(DataUtil.getCharsetFromContentType(contentType));
    }

    @Test
    public void testValidateCharset() {
        assertEquals(CHARSET_UTF8, DataUtil.validateCharset(CHARSET_UTF8));
        assertEquals(CHARSET_GB2312, DataUtil.validateCharset(CHARSET_GB2312));
        assertNull(DataUtil.validateCharset("InvalidCharset"));
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertTrue(boundary.length() == 32);
        for (int i = 0; i < boundary.length(); i++) {
            assertTrue(DataUtil.mimeBoundaryChars[new Random().nextInt(DataUtil.mimeBoundaryChars.length)] == boundary.charAt(i));
        }
    }
}