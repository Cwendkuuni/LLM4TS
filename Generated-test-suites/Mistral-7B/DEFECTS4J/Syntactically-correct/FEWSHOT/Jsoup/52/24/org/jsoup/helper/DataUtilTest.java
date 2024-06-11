package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

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
import java.util.Random;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html"); // replace with actual test file
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";
        Document document = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String testData = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String testData = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";
        Parser parser = Parser.xmlParser();
        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
        assertNotNull(document);
    }

    @Test
    public void testParseByteData() throws IOException {
        String testData = "<html><head></head><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(testData.getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";
        Parser parser = Parser.htmlParser();
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(document);
    }

    @Test
    public void testDetectCharsetFromBom() {
        byte[] bomUtf32 = {0x00, 0x00, (byte) 0xFE, (byte) 0xFF};
        ByteBuffer byteData = ByteBuffer.wrap(bomUtf32);
        String charsetName = DataUtil.detectCharsetFromBom(byteData, null);
        assertEquals("UTF-32", charsetName);

        byte[] bomUtf16BE = {(byte) 0xFE, (byte) 0xFF, 0x00, 0x00};
        ByteBuffer byteDataBE = ByteBuffer.wrap(bomUtf16BE);
        charsetName = DataUtil.detectCharsetFromBom(byteDataBE, null);
        assertEquals("UTF-16BE", charsetName);

        byte[] bomUtf16LE = {0xFF, 0xFE, 0x00, 0x00};
        ByteBuffer byteDataLE = ByteBuffer.wrap(bomUtf16LE);
        charsetName = DataUtil.detectCharsetFromBom(byteDataLE, null);
        assertEquals("UTF-16LE", charsetName);

        byte[] bomUtf8 = {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};
        ByteBuffer byteDataUTF8 = ByteBuffer.wrap(bomUtf8);
        charsetName = DataUtil.detectCharsetFromBom(byteDataUTF8, null);
        assertEquals("UTF-8", charsetName);

        ByteBuffer byteData = ByteBuffer.wrap(new byte[0]);
        charsetName = DataUtil.detectCharsetFromBom(byteData, null);
        assertNull(charsetName);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=GB2312";
        String charsetName = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("GB2312", charsetName);

        contentType = "text/html; charset=UTF-8";
        charsetName = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charsetName);

        contentType = "text/html; charset=ISO-8859-1";
        charsetName = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("ISO-8859-1", charsetName);

        contentType = "text/html";
        charsetName = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charsetName);
    }

    @Test
    public void testValidateCharset() {
        String charsetName = "UTF-32";
        String validatedCharset = DataUtil.validateCharset(charsetName);
        assertEquals("UTF-32", validatedCharset);

        charsetName = "UTF-16BE";
        validatedCharset = DataUtil.validateCharset(charsetName);
        assertEquals("UTF-16BE", validatedCharset);

        charsetName = "UTF-16LE";
        validatedCharset = DataUtil.validateCharset(charsetName);
        assertEquals("UTF-16LE", validatedCharset);

        charsetName = "UTF-8";
        validatedCharset = DataUtil.validateCharset(charsetName);
        assertEquals("UTF-8", validatedCharset);

        charsetName = "GB2312";
        validatedCharset = DataUtil.validateCharset(charsetName);
        assertEquals("GB2312", validatedCharset);

        charsetName = "ISO-8859-1";
        validatedCharset = DataUtil.validateCharset(charsetName);
        assertEquals("ISO-8859-1", validatedCharset);

        charsetName = "unknown";
        validatedCharset = DataUtil.validateCharset(charsetName);
        assertNull(validatedCharset);

        charsetName = "";
        validatedCharset = DataUtil.validateCharset(charsetName);
        assertNull(validatedCharset);

        charsetName = null;
        validatedCharset = DataUtil.validateCharset(charsetName);
        assertNull(validatedCharset);
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        for (int i = 0; i < mimeBoundary.length(); i++) {
            char c = mimeBoundary.charAt(i);
            assertTrue(Character.isAlphabetic(c) || Character.isDigit(c) || c == '-' || c == '_');
        }
    }
}