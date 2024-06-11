package org.jsoup.helper;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Random;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import org.junit.Test;

public class DataUtilTest {

    @Test
    public void testLoadFileWithDefaultCharset() throws Exception {
        File inputFile = new File("testData/inputWithUtf8Charset.html");
        Document document = DataUtil.load(inputFile, null, null);
        assertEquals("UTF-8", document.outputSettings().charset());
        assertEquals("Test Data with UTF-8 Charset", document.title());
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws Exception {
        File inputFile = new File("testData/inputWithUtf8Charset.html");
        Document document = DataUtil.load(inputFile, "ISO-8859-1", null);
        assertEquals("ISO-8859-1", document.outputSettings().charset());
        assertEquals("Test Data with UTF-8 Charset", document.title());
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws Exception {
        String inputData = "Test Data with UTF-8 Charset";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(inputStream, null, null);
        assertEquals("UTF-8", document.outputSettings().charset());
        assertEquals("Test Data with UTF-8 Charset", document.title());
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws Exception {
        String inputData = "Test Data with UTF-8 Charset";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(inputStream, "ISO-8859-1", null);
        assertEquals("ISO-8859-1", document.outputSettings().charset());
        assertEquals("Test Data with UTF-8 Charset", document.title());
    }

    @Test
    public void testLoadInputStreamWithParser() throws Exception {
        String inputData = "<?xml version='1.0' encoding='UTF-8'?>\n<root>\n<element>Test Data with UTF-8 Charset</element>\n</root>";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(inputStream, null, null, Parser.xmlParser());
        assertEquals("UTF-8", document.outputSettings().charset());
        assertEquals("Test Data with UTF-8 Charset", document.title());
        assertEquals("Test Data with UTF-8 Charset", document.elementText());
    }

    @Test
    public void testCrossStreams() throws Exception {
        String inputData = "Test Data with UTF-8 Charset";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));
        DataUtil.crossStreams(inputStream, outputStream);
        assertEquals(inputData, outputStream.toString(StandardCharsets.UTF_8.toString()));
    }

    @Test
    public void testParseByteDataWithDefaultCharset() throws Exception {
        String inputData = "Test Data with UTF-8 Charset";
        ByteBuffer byteData = ByteBuffer.wrap(inputData.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        assertEquals("UTF-8", document.outputSettings().charset());
        assertEquals("Test Data with UTF-8 Charset", document.title());
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() throws Exception {
        String inputData = "Test Data with UTF-8 Charset";
        String charsetName = "ISO-8859-1";
        ByteBuffer byteData = ByteBuffer.wrap(inputData.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseByteData(byteData, charsetName, null, Parser.htmlParser());
        assertEquals("ISO-8859-1", document.outputSettings().charset());
        assertEquals("Test Data with UTF-8 Charset", document.title());
    }

    @Test
    public void testReadToByteBufferFromFile() throws Exception {
        File inputFile = new File("testData/inputWithUtf8Charset.html");
        ByteBuffer byteData = DataUtil.readFileToByteBuffer(inputFile);
        assertEquals(inputFile.length(), byteData.remaining());
    }

    @Test
    public void testReadToByteBufferFromInputStream() throws Exception {
        String inputData = "Test Data with UTF-8 Charset";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        assertEquals(inputData.length(), byteData.remaining());
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        assertTrue(byteBuffer.remaining() == 0);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);

        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        assertEquals(charset, DataUtil.validateCharset(charset));

        charset = "UTF-8\t";
        assertEquals(charset, DataUtil.validateCharset(charset));

        charset = "UTF-8\r\n";
        assertEquals(charset, DataUtil.validateCharset(charset));

        charset = "UTF-8\n";
        assertEquals(charset, DataUtil.validateCharset(charset));

        charset = "UTF-8\r";
        assertEquals(charset, DataUtil.validateCharset(charset));

        charset = "UTF-8\t\r\n";
        assertEquals(charset, DataUtil.validateCharset(charset));

        charset = "UTF-8\n\r";
        assertEquals(charset, DataUtil.validateCharset(charset));

        charset = "euc-jp";
        assertEquals("euc-jp", DataUtil.validateCharset(charset));

        charset = "EUC-JP";
        assertEquals("euc-jp", DataUtil.validateCharset(charset));

        charset = "EUC-JP\t";
        assertEquals("euc-jp", DataUtil.validateCharset(charset));

        charset = "EUC-JP\r\n";
        assertEquals("euc-jp", DataUtil.validateCharset(charset));

        charset = "EUC-JP\n";
        assertEquals("euc-jp", DataUtil.validateCharset(charset));

        charset = "EUC-JP\r";
        assertEquals("euc-jp", DataUtil.validateCharset(charset));

        charset = "EUC-JP\t\r\n";
        assertEquals("euc-jp", DataUtil.validateCharset(charset));

        charset = "EUC-JP\n\r";
        assertEquals("euc-jp", DataUtil.validateCharset(charset));

        charset = "invalidCharset";
        assertNull(DataUtil.validateCharset(charset));
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertTrue(boundary.length() == 32);
    }

    @Test
    public void testDetectCharsetFromBom() throws Exception {
        ByteArrayInputStream bomInputStream = new ByteArrayInputStream(new byte[] { 0x00, 0x00, (byte) 0xFE, (byte) 0xFF });
        String charset = DataUtil.detectCharsetFromBom(DataUtil.readToByteBuffer(bomInputStream), null);
        assertEquals("UTF-32", charset);

        bomInputStream = new ByteArrayInputStream(new byte[] { (byte) 0xFE, (byte) 0xFF, 0x00, 0x00 });
        charset = DataUtil.detectCharsetFromBom(DataUtil.readToByteBuffer(bomInputStream), null);
        assertEquals("UTF-32", charset);

        bomInputStream = new ByteArrayInputStream(new byte[] { (byte) 0xFF, 0xFE, 0x00, 0x00 });
        charset = DataUtil.detectCharsetFromBom(DataUtil.readToByteBuffer(bomInputStream), null);
        assertEquals("UTF-32", charset);

        bomInputStream = new ByteArrayInputStream(new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF });
        charset = DataUtil.detectCharsetFromBom(DataUtil.readToByteBuffer(bomInputStream), null);
        assertEquals("UTF-8", charset);
    }
}