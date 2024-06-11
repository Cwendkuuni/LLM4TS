package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import org.jsoup.UncheckedIOException;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

/**
 * Test class for DataUtil.
 */
public class DataUtilTest {

    /**
     * Test method for {@link DataUtil#load(File, String, String)}.
     */
    @Test
    public void testLoadFile() throws IOException {
        String charset = "UTF-8";
        String baseUri = "http://example.com";
        File inputFile = new File("test.html");
        Document doc = DataUtil.load(inputFile, charset, baseUri);
        assertNotNull(doc);
    }

    /**
     * Test method for {@link DataUtil#load(InputStream, String, String)}.
     */
    @Test
    public void testLoadInputStream() throws IOException {
        String charset = "UTF-8";
        String baseUri = "http://example.com";
        InputStream inputStream = new ByteArrayInputStream("<html></html>".getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, charset, baseUri);
        assertNotNull(doc);
    }

    /**
     * Test method for {@link DataUtil#load(InputStream, String, String, Parser)}.
     */
    @Test
    public void testLoadInputStreamParser() throws IOException {
        String charset = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.xmlParser();
        InputStream inputStream = new ByteArrayInputStream("<?xml version='1.0' encoding='UTF-8'?>\n<html></html>".getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, charset, baseUri, parser);
        assertNotNull(doc);
    }

    /**
     * Test method for {@link DataUtil#crossStreams(InputStream, OutputStream)}.
     */
    @Test
    public void testCrossStreams() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Hello World".getBytes());
        OutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(inputStream, outputStream);
        String result = new String(outputStream.toByteArray(), StandardCharsets.UTF_8);
        assertNotNull(result);
        assertNotNull(inputStream.markSupported());
        inputStream.reset();
        assertNotNull(outputStream.markSupported());
        outputStream.reset();
    }

    /**
     * Test method for {@link DataUtil#parseInputStream(InputStream, String, String, Parser)}.
     */
    @Test
    public void testParseInputStream() throws IOException {
        String charset = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.xmlParser();
        InputStream inputStream = new ByteArrayInputStream("<html></html>".getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseInputStream(inputStream, charset, baseUri, parser);
        assertNotNull(doc);
    }

    /**
     * Test method for {@link DataUtil#readToByteBuffer(InputStream, int)}.
     */
    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Hello World".getBytes(StandardCharsets.UTF_8));
        ByteBuffer buffer = DataUtil.readToByteBuffer(inputStream, 10);
        assertNotNull(buffer);
        byte[] expected = "Hello World".getBytes(StandardCharsets.UTF_8);
        ByteBuffer expectedBuffer = ByteBuffer.wrap(expected);
        assert (buffer.compareTo(expectedBuffer) == 0);
    }

    /**
     * Test method for {@link DataUtil#getCharsetFromContentType(String)}.
     */
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNotNull(charset);
        assertEquals(charset, "UTF-8");

        contentType = "application/xml; charset=gb2312";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertNotNull(charset);
        assertEquals(charset, "gb2312");

        contentType = null;
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);

        contentType = "text/plain";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    /**
     * Test method for {@link DataUtil#validateCharset(String)}.
     */
    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        String validatedCharset = DataUtil.validateCharset(charset);
        assertNotNull(validatedCharset);
        assertEquals(charset, validatedCharset);

        charset = "UTF-16";
        validatedCharset = DataUtil.validateCharset(charset);
        assertNotNull(validatedCharset);
        assertEquals(charset, validatedCharset);

        charset = "EUC-JP";
        validatedCharset = DataUtil.validateCharset(charset);
        assertNotNull(validatedCharset);
        assertEquals(charset, validatedCharset);

        charset = "InvalidCharset";
        validatedCharset = DataUtil.validateCharset(charset);
        assertNull(validatedCharset);
    }

    /**
     * Test method for {@link DataUtil#mimeBoundary()}.
     */
    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertNotNull(boundary);
        int length = boundary.length();
        for (int i = 0; i < length; i++) {
            char c = boundary.charAt(i);
            assertTrue(Character.isAlphabetic(c) || Character.isDigit(c) || c == '-');
        }
    }

    /**
     * Test method for {@link DataUtil#detectCharsetFromBom(ByteBuffer)}.
     */
    @Test
    public void testDetectCharsetFromBom() {
        String charset = null;
        byte[] bomUtf16 = { (byte) 0xFE, (byte) 0xFF };
        byte[] bomUtf32 = { (byte) 0x00, (byte) 0x00, (byte) 0xFE, (byte) 0xFF };
        byte[] bomUtf8 = { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF };

        ByteBuffer byteDataUtf16 = ByteBuffer.wrap(bomUtf16);
        BomCharset utf16Bom = DataUtil.detectCharsetFromBom(byteDataUtf16);
        charset = utf16Bom != null ? utf16Bom.charset : null;
        assertEquals(charset, "UTF-16");

        ByteBuffer byteDataUtf32 = ByteBuffer.wrap(bomUtf32);
        BomCharset utf32Bom = DataUtil.detectCharsetFromBom(byteDataUtf32);
        charset = utf32Bom != null ? utf32Bom.charset : null;
        assertEquals(charset, "UTF-32");

        ByteBuffer byteDataUtf8 = ByteBuffer.wrap(bomUtf8);
        BomCharset utf8Bom = DataUtil.detectCharsetFromBom(byteDataUtf8);
        charset = utf8Bom != null ? utf8Bom.charset : null;
        assertEquals(charset, "UTF-8");

        ByteBuffer byteData = ByteBuffer.wrap(new byte[0]);
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteData);
        assertNull(bomCharset);
    }

    /**
     * Test method for {@link DataUtil#parseInput(InputStreamReader, String)}.
     */
    @Test
    public void testParseInput() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("<html></html>".getBytes(StandardCharsets.UTF_8));
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        String charset = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.parseInput(inputStreamReader, charset, baseUri, parser);
        assertNotNull(doc);
    }

    /**
     * Test method for {@link DataUtil#parseInput(InputStreamReader, String, Parser)}.
     */
    @Test
    public void testParseInputParser() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("<html></html>".getBytes(StandardCharsets.UTF_8));
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        String charset = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.xmlParser();
        Document doc = DataUtil.parseInput(inputStreamReader, charset, parser);
        assertNotNull(doc);
    }

    /**
     * Test method for {@link DataUtil#parseInput(String)}.
     */
    @Test
    public void testParseInputString() {
        String htmlInput = "<html></html>";
        String charset = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.parseInput(htmlInput, charset, baseUri, parser);
        assertNotNull(doc);
    }

    /**
     * Test method for {@link DataUtil#parseInput(String, Parser)}.
     */
    @Test
    public void testParseInputParserString() {
        String htmlInput = "<html></html>";
        String charset = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.xmlParser();
        Document doc = DataUtil.parseInput(htmlInput, charset, parser);
        assertNotNull(doc);
    }

    /**
     * Test method for {@link DataUtil#parseInput(InputStream, String, String, Parser)}.
     */
    @Test
    public void testParseInputCharsetBaseUriParser() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("<html></html>".getBytes(StandardCharsets.UTF_8));
        String charset = "UTF-16";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        try {
            Document doc = DataUtil.parseInput(inputStream, charset, baseUri, parser);
            fail("IOException expected");
        } catch (UncheckedIOException e) {
            // expected
        }
    }

    /**
     * Test method for {@link DataUtil#parseInput(InputStream, String, String, Parser)}.
     */
    @Test
    public void testParseInputCharsetBaseUriParserNullInputStream() {
        String charset = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        InputStream inputStream = null;
        try {
            Document doc = DataUtil.parseInput(inputStream, charset, baseUri, parser);
            fail("IOException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test method for {@link DataUtil#parseInput(InputStream, String, String, Parser)}.
     */
    @Test
    public void testParseInputBaseUriNull() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("<html></html>".getBytes(StandardCharsets.UTF_8));
        String charset = "UTF-8";
        String baseUri = null;
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.parseInput(inputStream, charset, baseUri, parser);
        assertNotNull(doc);
    }

    /**
     * Test method for {@link DataUtil#parseInput(InputStream, String, String, Parser)}.
     */
    @Test
    public void testParseInputCharsetNull() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("<html></html>".getBytes(StandardCharsets.UTF_8));
        String charset = null;
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.parseInput(inputStream, charset, baseUri, parser);
        assertNotNull(doc);
    }

    /**
     * Test method for {@link DataUtil#parseInput(InputStream, String, String, Parser)}.
     */
    @Test
    public void testParseInputParserNull() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("<html></html>".getBytes(StandardCharsets.UTF_8));
        String charset = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = null;
        try {
            Document doc = DataUtil.parseInput(inputStream, charset, baseUri, parser);
            fail("NullPointerException expected");
        } catch (NullPointerException e) {
            // expected
        }
    }
}