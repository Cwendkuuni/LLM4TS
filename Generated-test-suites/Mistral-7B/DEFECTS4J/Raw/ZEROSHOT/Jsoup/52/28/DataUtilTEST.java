package org.jsoup.helper;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Test class for DataUtil.
 */
public class DataUtilTest {

    private static final String TEST_FILE = "testData.html";
    private static final String TEST_FILE_UTF16LE = "testData_utf16le.html";
    private static final String TEST_FILE_UTF32BE = "testData_utf32be.html";
    private static final String TEST_FILE_UTF8 = "testData_utf8.html";
    private static final String TEST_FILE_UTF8_BOM = "testData_utf8_bom.html";
    private static final String TEST_CHARSET_GB2312 = "GB2312";
    private static final String TEST_CHARSET_EUC_JP = "EUC-JP";

    private DataUtil dataUtil;
    private Document doc;
    private Document docWithCharset;
    private Document docWithMeta;
    private Document docWithXml;
    private Document docWithInvalidCharset;

    @Before
    public void setUp() {
        dataUtil = new DataUtil();
    }

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        File testFile = new File(TEST_FILE);
        doc = dataUtil.load(testFile, null);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        File testFile = new File(TEST_FILE_UTF8);
        doc = dataUtil.load(testFile, "UTF-8");
        Assert.assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidCharset() throws IOException {
        File testFile = new File(TEST_FILE_UTF8);
        doc = dataUtil.load(testFile, "ASCII");
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream(TEST_FILE);
        doc = dataUtil.load(inputStream, null);
        Assert.assertNotNull(doc);
        inputStream.close();
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream(TEST_FILE_UTF8);
        doc = dataUtil.load(inputStream, "UTF-8");
        Assert.assertNotNull(doc);
        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream(TEST_FILE_UTF8);
        doc = dataUtil.load(inputStream, "ASCII");
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream(TEST_FILE_UTF8);
        doc = dataUtil.load(inputStream, "UTF-8", null, Parser.xmlParser());
        Assert.assertNotNull(doc);
        inputStream.close();
    }

    @Test
    public void testCrossStreams() throws IOException {
        File testFile = new File(TEST_FILE_UTF8);
        InputStream inputStream = new FileInputStream(testFile);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        dataUtil.crossStreams(inputStream, outputStream);
        inputStream.close();
        Assert.assertNotNull(outputStream.toByteArray());
    }

    @Test
    public void testParseByteDataWithDefaultCharset() {
        ByteArrayOutputStream byteData = new ByteArrayOutputStream();
        File testFile = new File(TEST_FILE);
        byte[] fileData = readFileToByteArray(testFile);
        byteData.write(fileData);
        ByteBuffer byteBuffer = ByteBuffer.wrap(byteData.toByteArray());
        doc = dataUtil.parseByteData(byteBuffer, null, null, Parser.htmlParser());
        Assert.assertNotNull(doc);
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() {
        ByteArrayOutputStream byteData = new ByteArrayOutputStream();
        File testFileUtf8 = new File(TEST_FILE_UTF8);
        byte[] fileDataUtf8 = readFileToByteArray(testFileUtf8);
        byteData.write(fileDataUtf8);
        ByteBuffer byteBuffer = ByteBuffer.wrap(byteData.toByteArray());
        docWithCharset = dataUtil.parseByteData(byteBuffer, "UTF-8", null, Parser.htmlParser());
        Assert.assertNotNull(docWithCharset);
    }

    @Test
    public void testParseByteDataWithInvalidCharset() {
        ByteArrayOutputStream byteData = new ByteArrayOutputStream();
        File testFileUtf16le = new File(TEST_FILE_UTF16LE);
        byte[] fileDataUtf16le = readFileToByteArray(testFileUtf16le);
        byteData.write(fileDataUtf16le);
        ByteBuffer byteBuffer = ByteBuffer.wrap(byteData.toByteArray());
        try {
            docWithInvalidCharset = dataUtil.parseByteData(byteBuffer, "GB2312", null, Parser.htmlParser());
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseByteDataWithMetaTag() {
        ByteArrayOutputStream byteData = new ByteArrayOutputStream();
        File testFileWithMeta = new File(TEST_FILE_UTF8_BOM);
        byte[] fileDataWithMeta = readFileToByteArray(testFileWithMeta);
        byteData.write(fileDataWithMeta);
        ByteBuffer byteBuffer = ByteBuffer.wrap(byteData.toByteArray());
        docWithMeta = dataUtil.parseByteData(byteBuffer, null, null, Parser.htmlParser());
        Element meta = docWithMeta.select("meta[charset]").first();
        Assert.assertNotNull(meta);
        String charset = meta.attr("charset");
        Assert.assertEquals("UTF-8", charset);
    }

    @Test
    public void testParseByteDataWithXmlDeclaration() {
        ByteArrayOutputStream byteData = new ByteArrayOutputStream();
        File testFileWithXml = new File(TEST_FILE_UTF8);
        byte[] fileDataWithXml = readFileToByteArray(testFileWithXml);
        byteData.write(fileDataWithXml);
        ByteBuffer byteBuffer = ByteBuffer.wrap(byteData.toByteArray());
        docWithXml = dataUtil.parseByteData(byteBuffer, null, null, Parser.xmlParser());
        Element xmlDeclaration = docWithXml.childNode(0);
        Assert.assertTrue(xmlDeclaration instanceof XmlDeclaration);
        String charset = xmlDeclaration.attr("encoding");
        Assert.assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals("UTF-8", charset);

        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertNull(charset);
    }

    @Test
    public void testValidateCharset() {
        String cs = "UTF-8";
        Assert.assertEquals(cs, DataUtil.validateCharset(cs));

        cs = "utf-8";
        Assert.assertEquals(cs, DataUtil.validateCharset(cs));

        cs = "UTF-8\t";
        Assert.assertEquals(cs, DataUtil.validateCharset(cs));

        cs = "UTF-8\n";
        Assert.assertEquals(cs, DataUtil.validateCharset(cs));

        cs = "UTF-8\r";
        Assert.assertEquals(cs, DataUtil.validateCharset(cs));

        cs = "UTF-8\r\n";
        Assert.assertEquals(cs, DataUtil.validateCharset(cs));

        cs = "UTF-8\r\t";
        Assert.assertEquals(cs, DataUtil.validateCharset(cs));

        cs = "UTF-8\t\r";
        Assert.assertEquals(cs, DataUtil.validateCharset(cs));

        cs = "UTF-8\t\r\n";
        Assert.assertEquals(cs, DataUtil.validateCharset(cs));

        cs = "UTF-16";
        Assert.assertEquals(cs, DataUtil.validateCharset(cs));

        cs = "gb2312";
        Assert.assertNull(DataUtil.validateCharset(cs));

        cs = "EUC-JP";
        Assert.assertEquals(cs, DataUtil.validateCharset(cs));

        cs = "invalid";
        Assert.assertNull(DataUtil.validateCharset(cs));
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        Assert.assertNotNull(boundary);
        Assert.assertEquals(boundary.length(), DataUtil.boundaryLength);
    }

    private byte[] readFileToByteArray(File file) throws IOException {
        RandomAccessFile randomAccessFile = null;
        byte[] bytes = null;

        try {
            randomAccessFile = new RandomAccessFile(file, "r");
            bytes = new byte[(int) randomAccessFile.length()];
            randomAccessFile.readFully(bytes);
        } finally {
            if (randomAccessFile != null)
                randomAccessFile.close();
        }

        return bytes;
    }
}