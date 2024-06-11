package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testLoadDocumentFromFileWithDefaultCharset() throws IOException {
        File testFile = new File("testFileWithDefaultCharset.html");
        Document document = DataUtil.load(testFile, null, null);
        assertNotNull(document);
    }

    @Test
    public void testLoadDocumentFromFileWithSpecifiedCharsetUtf8() throws IOException {
        File testFile = new File("testFileWithUtf8Charset.html");
        Document document = DataUtil.load(testFile, "UTF-8", null);
        assertNotNull(document);
    }

    @Test
    public void testLoadDocumentFromInputStreamWithDefaultCharset() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] testBytes = "Test document content".getBytes();
        byteArrayOutputStream.write(testBytes);
        InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        Document document = DataUtil.load(inputStream, null, null);
        assertNotNull(document);
    }

    @Test
    public void testLoadDocumentFromInputStreamWithSpecifiedCharsetUtf8() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] testBytes = "Test document content".getBytes("UTF-8");
        byteArrayOutputStream.write(testBytes);
        InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        Document document = DataUtil.load(inputStream, "UTF-8", null);
        assertNotNull(document);
    }

    @Test
    public void testLoadDocumentFromInputStreamWithSpecifiedCharsetInvalid() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] testBytes = "Test document content".getBytes();
        byteArrayOutputStream.write(testBytes);
        InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        try {
            Document document = DataUtil.load(inputStream, "InvalidCharset", null);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("DataUtil can't support charset 'InvalidCharset'", e.getMessage());
        }
    }

    @Test
    public void testParseByteDataWithDefaultCharset() {
        ByteBuffer byteData = ByteBuffer.wrap(new byte[0]);
        String charsetName = null;
        Document document = DataUtil.parseByteData(byteData, charsetName, null, Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithSpecifiedCharsetUtf8() {
        ByteBuffer byteData = ByteBuffer.wrap("Test document content".getBytes("UTF-8"));
        String charsetName = "UTF-8";
        Document document = DataUtil.parseByteData(byteData, charsetName, null, Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithSpecifiedCharsetInvalid() {
        ByteBuffer byteData = ByteBuffer.wrap("Test document content".getBytes());
        String charsetName = "InvalidCharset";
        try {
            Document document = DataUtil.parseByteData(byteData, charsetName, null, Parser.htmlParser());
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("DataUtil can't support charset 'InvalidCharset'", e.getMessage());
        }
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        assertTrue(mimeBoundary.length() >= boundaryLength);
    }

    @Test
    public void testDetectCharsetFromBomWithUtf8Bom() {
        ByteBuffer byteData = ByteBuffer.wrap(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF, 0});
        String charsetName = DataUtil.detectCharsetFromBom(byteData, null);
        assertEquals("UTF-8", charsetName);
    }

    @Test
    public void testDetectCharsetFromBomWithUtf16Lebom() {
        ByteBuffer byteData = ByteBuffer.wrap(new byte[]{(byte) 0xFF, (byte) 0xFE, 0, 0});
        String charsetName = DataUtil.detectCharsetFromBom(byteData, null);
        assertEquals("UTF-16", charsetName);
    }

    @Test
    public void testDetectCharsetFromBomWithUtf32Bebom() {
        ByteBuffer byteData = ByteBuffer.wrap(new byte[]{0, 0, (byte) 0xFE, (byte) 0xFF});
        String charsetName = DataUtil.detectCharsetFromBom(byteData, null);
        assertEquals("UTF-32", charsetName);
    }

    @Test
    public void testDetectCharsetFromBomWithEmptyBom() {
        ByteBuffer byteData = ByteBuffer.wrap(new byte[0]);
        String charsetName = DataUtil.detectCharsetFromBom(byteData, null);
        assertNull(charsetName);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "application/xml; charset=UTF-8";
        String charsetName = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charsetName);
    }

    @Test
    public void testGetCharsetFromContentTypeWithNoCharset() {
        String contentType = "application/xml; charset=null";
        String charsetName = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(null, charsetName);
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidCharset() {
        String contentType = "application/xml; charset=ISO-8859-123";
        String charsetName = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charsetName);
    }

    @Test
    public void testValidateCharsetWithValidCharsets() {
        String utf8Charset = "UTF-8";
        String utf16Charset = "UTF-16";
        String utf32Charset = "UTF-32";
        String asciiCharset = "US-ASCII";

        String validCharsets[] = {utf8Charset, utf16Charset, utf32Charset, asciiCharset};

        for (String charset : validCharsets) {
            String validatedCharset = DataUtil.validateCharset(charset);
            assertEquals(charset, validatedCharset);
        }
    }

    @Test
    public void testValidateCharsetWithInvalidCharsets() {
        String invalidCharsets[] = {"InvalidCharset", "UTF-8\t", "UTF-8's"};

        for (String charset : invalidCharsets) {
            String validatedCharset = DataUtil.validateCharset(charset);
            assertNull(validatedCharset);
        }
    }
}