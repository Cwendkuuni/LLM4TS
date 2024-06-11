package org.jsoup.helper;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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

    private static final String TEST_FILE_NAME = "testFile.html";
    private static final String TEST_CHARSET = "UTF-8";
    private static final String TEST_BASE_URI = "http://example.com";
    private static final String TEST_INPUT_STREAM_DATA = "Test Data";

    private Document testDocument;

    @Before
    public void setUp() throws IOException {
        testDocument = createTestDocument();
    }

    @Test
    public void testLoadFile() throws IOException {
        File testFile = new File(TEST_FILE_NAME);
        Document document = DataUtil.load(testFile, TEST_CHARSET, TEST_BASE_URI);
        Assert.assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithNullCharset() throws IOException {
        File testFile = new File(TEST_FILE_NAME);
        DataUtil.load(testFile, null, TEST_BASE_URI);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(TEST_INPUT_STREAM_DATA.getBytes(TEST_CHARSET));
        InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        Document document = DataUtil.load(inputStream, TEST_CHARSET, TEST_BASE_URI);
        Assert.assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithNullCharset() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(TEST_INPUT_STREAM_DATA.getBytes(TEST_CHARSET));
        InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        DataUtil.load(inputStream, null, TEST_BASE_URI);
    }

    @Test
    public void testParseByteData() throws IOException {
        ByteBuffer byteData = DataUtil.readFileToByteBuffer(new File(TEST_FILE_NAME));
        Document document = DataUtil.parseByteData(byteData, TEST_CHARSET, TEST_BASE_URI, Parser.htmlParser());
        Assert.assertNotNull(document);
    }

    @Test
    public void testCrossStreams() throws IOException {
        File inputFile = new File("inputFile.txt");
        File outputFile = new File("outputFile.txt");
        InputStream inputStream = new FileInputStream(inputFile);
        OutputStream outputStream = new FileOutputStream(outputFile);

        DataUtil.crossStreams(inputStream, outputStream);

        byte[] expectedOutput = "Expected Output".getBytes();
        byte[] outputBytes = new byte[expectedOutput.length];
        FileReader fileReader = new FileReader(outputFile);
        fileReader.read(outputBytes);

        Assert.assertArrayEquals(expectedOutput, outputBytes);

        outputStream.close();
        inputStream.close();
        fileReader.close();
    }

    @Test
    public void testParseByteDataWithDetectedCharset() throws IOException {
        String testMetaCharset = "gb2312";
        ByteBuffer byteData = DataUtil.readFileToByteBuffer(new File(TEST_FILE_NAME));
        Document document = DataUtil.parseByteData(byteData, null, TEST_BASE_URI, Parser.htmlParser());
        Element metaElement = document.select("meta[http-equiv=content-type], meta[charset]").first();

        if (metaElement != null) {
            String charset = getCharsetFromContentType(metaElement.attr("content"));
            if (charset != null) {
                testCharset = charset;
            }
        }

        document = DataUtil.parseByteData(byteData, testCharset, TEST_BASE_URI, Parser.htmlParser());
        Assert.assertNotNull(document);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=EUC-JP";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals("EUC-JP", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithUnsupportedCharset() {
        String contentType = "text/html; charset=XYZ-ABC";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertNull(charset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        Assert.assertNotNull(boundary);
        Assert.assertEquals(boundary.length(), 32);
    }

    private Document createTestDocument() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write("<html><head><meta charset=\"UTF-8\"></meta></head><body>Test</body></html>".getBytes());
        InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        Document document = DataUtil.load(inputStream, null, TEST_BASE_URI);
        return document;
    }
}