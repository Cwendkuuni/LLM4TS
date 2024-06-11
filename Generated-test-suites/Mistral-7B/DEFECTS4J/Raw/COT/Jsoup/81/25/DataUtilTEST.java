package org.jsoup.helper;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.jsoup.helper.DataUtil;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Random;

/**
 * Tests for DataUtil class
 */
public class DataUtilTest {

    private static final String TEST_CHARSET = "UTF-8";
    private static Document testDocument;

    @BeforeClass
    public static void setUp() throws Exception {
        String testFilePath = "testData/test.html"; // Replace with the path to your test HTML file
        testDocument = DataUtil.load(new File(testFilePath), TEST_CHARSET, null);
    }

    @Test
    public void testLoadFile() throws IOException {
        File testFile = new File("testData/test.html");
        Document document = DataUtil.load(testFile, TEST_CHARSET, null);
        assertNotNull(document);
        assertEquals(testDocument.title(), document.title());
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String testData = "<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes(TEST_CHARSET));
        Document document = DataUtil.load(inputStream, TEST_CHARSET, null);
        assertNotNull(document);
        assertEquals(1, document.childNodeSize());
        assertEquals("Test", document.title());
        inputStream.close();
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String testData = "<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes(TEST_CHARSET));
        Document document = DataUtil.load(inputStream, TEST_CHARSET, null, Parser.xmlParser());
        assertNotNull(document);
        assertEquals(1, document.childNodeSize());
        assertEquals("Test", document.title());
        inputStream.close();
    }

    @Test
    public void testCrossStreams() throws IOException {
        String testData = "Test data";
        File testInputFile = new File("testData/test_input.txt");
        File testOutputFile = new File("testData/test_output.txt");

        try (InputStream inputStream = new FileInputStream(testInputFile);
             OutputStream outputStream = new FileOutputStream(testOutputFile)) {
            DataUtil.crossStreams(inputStream, outputStream);
        }

        String expectedOutput = testData;
        String actualOutput = new String(Files.readAllBytes(testOutputFile.toPath()), Charset.forName(TEST_CHARSET));
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testParseInputStream() throws IOException {
        String testData = "<html><body><h1>Test</h1></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData.getBytes(TEST_CHARSET));
        Document document = DataUtil.parseInputStream(inputStream, TEST_CHARSET, null, Parser.htmlParser());
        assertNotNull(document);
        assertEquals(1, document.childNodeSize());
        assertEquals("Test", document.title());
        inputStream.close();
    }

    @Test
    public void testParseInputStreamWithCharsetAndBaseUri() throws IOException {
        String testData = "<html><body><h1>Test</h1></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData.getBytes(TEST_CHARSET));
        Document document = DataUtil.parseInputStream(inputStream, "UTF-16", null, Parser.htmlParser());
        assertNotNull(document);
        assertEquals(1, document.childNodeSize());
        assertEquals("Test", document.title());
        inputStream.close();
    }

    @Test
    public void testParseInputStreamWithParser() throws IOException {
        String testData = "<html><body><h1>Test</h1></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData.getBytes(TEST_CHARSET));
        Document document = DataUtil.parseInputStream(inputStream, TEST_CHARSET, null, Parser.xmlParser());
        assertNotNull(document);
        assertEquals(1, document.childNodeSize());
        assertEquals("Test", document.title());
        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String testData = "<html><body><h1>Test</h1></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData.getBytes("InvalidCharset".getBytes()));
        DataUtil.parseInputStream(inputStream, "InvalidCharset", null, Parser.htmlParser());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String testData = "Test data";
        File testFile = new File("testData/test_bytebuffer.txt");
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(testFile, "rw")) {
            randomAccessFile.writeBytes(testData.getBytes());
        }

        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(testFile);
        assertEquals(testData.length(), byteBuffer.remaining());

        byte[] testBytes = new byte[testData.length()];
        byteBuffer.get(testBytes);
        assertEquals(testData, new String(testBytes, TEST_CHARSET));

        testFile.delete();
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        String testData = "Test data";
        File testFile = new File("testData/test_bytebuffer_max_size.txt");
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(testFile, "rw")) {
            randomAccessFile.writeBytes(testData.getBytes());
        }

        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(testFile);
        assertEquals(testData.length(), byteBuffer.remaining());

        byte[] testBytes = new byte[testData.length()];
        byteBuffer.get(testBytes);
        assertEquals(testData, new String(testBytes, TEST_CHARSET));

        testFile.delete();
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        assertEquals(0, byteBuffer.limit());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String testContentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(testContentType);
        assertEquals("UTF-8", charset);

        testContentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(testContentType);
        assertNull(charset);
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        String validatedCharset = DataUtil.validateCharset(charset);
        assertEquals(charset, validatedCharset);

        charset = "gb2312";
        validatedCharset = DataUtil.validateCharset(charset);
        assertEquals("gb2312", validatedCharset);

        charset = null;
        validatedCharset = DataUtil.validateCharset(charset);
        assertNull(validatedCharset);

        charset = "GB2312";
        validatedCharset = DataUtil.validateCharset(charset);
        assertEquals("gb2312", validatedCharset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertNotNull(boundary);
        assertEquals(32, boundary.length());
    }
}