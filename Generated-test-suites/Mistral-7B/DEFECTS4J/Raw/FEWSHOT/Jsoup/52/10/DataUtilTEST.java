package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;

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
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String expectedHtml = "<html><head></head><body></body></html>";
        File file = new File("test.html");
        file.getParentFile().mkdirs();
        FileUtils.writeStringToFile(file, expectedHtml, StandardCharsets.UTF_8);

        Document document = DataUtil.load(file, null, null);
        assertNotNull(document);
        assertEquals(expectedHtml, document.html());
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String expectedHtml = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(expectedHtml.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.load(inputStream, null, null);
        assertNotNull(document);
        assertEquals(expectedHtml, document.html());
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        String expectedHtml = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(expectedHtml.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.load(inputStream, "UTF-8", null);
        assertNotNull(document);
        assertEquals(expectedHtml, document.html());
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String expectedHtml = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(expectedHtml.getBytes(StandardCharsets.UTF_8));

        assertThrows(IllegalArgumentException.class, () -> DataUtil.load(inputStream, "invalid", null));
    }

    @Test
    public void testParseByteData() throws IOException {
        String expectedHtml = "<html><head></head><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(expectedHtml.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        assertNotNull(document);
        assertEquals(expectedHtml, document.html());
    }

    @Test
    public void testDetectCharsetFromBom() throws IOException {
        ByteBuffer byteData = ByteBuffer.wrap(new byte[]{(byte) 0xFF, (byte) 0xFE, (byte) 0x00, (byte) 0x00});
        String charsetName = DataUtil.detectCharsetFromBom(byteData, null);
        assertEquals("UTF-16", charsetName);

        byteData = ByteBuffer.wrap(new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0xFF, (byte) 0xFE});
        charsetName = DataUtil.detectCharsetFromBom(byteData, null);
        assertEquals("UTF-16", charsetName);

        byteData = ByteBuffer.wrap(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});
        charsetName = DataUtil.detectCharsetFromBom(byteData, null);
        assertEquals("UTF-8", charsetName);

        byteData = ByteBuffer.wrap(new byte[]{(byte) 0xFE, (byte) 0xFF, (byte) 0x00, (byte) 0x00});
        charsetName = DataUtil.detectCharsetFromBom(byteData, null);
        assertEquals("UTF-32", charsetName);

        byteData = ByteBuffer.wrap(new byte[]{0x00});
        charsetName = DataUtil.detectCharsetFromBom(byteData, null);
        assertNull(charsetName);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);

        contentType = "text/html; charset=UTF-16";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-16", charset);

        contentType = "text/html; charset=UTF-16BE";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-16BE", charset);

        contentType = "text/html; charset=UTF-16LE";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-16LE", charset);

        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(null, charset);
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        assertEquals(charset, DataUtil.validateCharset(charset));

        charset = "utf-8";
        assertEquals(charset, DataUtil.validateCharset(charset));

        charset = "UTF-8 (with BOM)";
        assertEquals(charset, DataUtil.validateCharset(charset));

        charset = "UTF-8\twith BOM";
        assertEquals(charset, DataUtil.validateCharset(charset));

        charset = "UTF-16";
        assertEquals(charset, DataUtil.validateCharset(charset));

        charset = "UTF-16BE";
        assertEquals(charset, DataUtil.validateCharset(charset));

        charset = "UTF-16LE";
        assertEquals(charset, DataUtil.validateCharset(charset));

        charset = "ISO-8859-1";
        assertEquals(charset, DataUtil.validateCharset(charset));

        charset = "INVALID_CHARSET";
        assertNull(DataUtil.validateCharset(charset));
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertNotNull(boundary);
        assertEquals(boundary.length(), 32);
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputData = "Test data.";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));
        OutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);
        byte[] outputData = outputStream.toByteArray();
        assertEquals(inputData, new String(outputData, StandardCharsets.UTF_8));
    }
}