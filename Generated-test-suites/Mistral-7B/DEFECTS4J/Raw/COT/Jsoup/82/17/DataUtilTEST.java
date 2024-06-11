package org.jsoup.helper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

/**
 * Test class for DataUtil.
 */
public class DataUtilTest {

    private static final String DEFAULT_CHARSET = "UTF-8";
    private static final String TEST_FILE_PATH = "test.html";
    private static final String TEST_INPUT_STREAM_DATA = "Test Data";
    private static final String TEST_BASE_URI = "http://example.com";
    private static final String TEST_PARSER = "htmlParser";

    private DataUtil dataUtil;
    private Parser parser;
    private Document document;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        dataUtil = new DataUtil();
        parser = mock(Parser.class);
    }

    @After
    public void tearDown() {
        if (document != null) {
            document.outputSettings().clear();
            document.outputSettings().charset(Charset.forName(DEFAULT_CHARSET));
        }
    }

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        File testFile = new File(TEST_FILE_PATH);
        Document expectedDocument = new Document(TEST_BASE_URI);
        Document actualDocument = dataUtil.load(testFile, null, TEST_BASE_URI);
        assertEquals(expectedDocument, actualDocument);
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        File testFile = new File(TEST_FILE_PATH);
        Document expectedDocument = new Document(TEST_BASE_URI);
        Document actualDocument = dataUtil.load(testFile, "UTF-16", TEST_BASE_URI);
        assertEquals(expectedDocument, actualDocument);
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidCharset() throws IOException {
        File testFile = new File(TEST_FILE_PATH);
        dataUtil.load(testFile, "invalidCharset", TEST_BASE_URI);
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        InputStream testInputStream = new ByteArrayInputStream(TEST_INPUT_STREAM_DATA.getBytes(Charset.forName(DEFAULT_CHARSET)));
        Document expectedDocument = new Document(TEST_BASE_URI);
        Document actualDocument = dataUtil.load(testInputStream, null, TEST_BASE_URI, parser);
        assertEquals(expectedDocument, actualDocument);
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        InputStream testInputStream = new ByteArrayInputStream(TEST_INPUT_STREAM_DATA.getBytes(Charset.forName("UTF-16")));
        Document expectedDocument = new Document(TEST_BASE_URI);
        Document actualDocument = dataUtil.load(testInputStream, "UTF-16", TEST_BASE_URI, parser);
        assertEquals(expectedDocument, actualDocument);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        InputStream testInputStream = new ByteArrayInputStream(TEST_INPUT_STREAM_DATA.getBytes(Charset.forName("invalidCharset")));
        dataUtil.load(testInputStream, "invalidCharset", TEST_BASE_URI, parser);
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        InputStream testInputStream = new ByteArrayInputStream(TEST_INPUT_STREAM_DATA.getBytes(Charset.forName(DEFAULT_CHARSET)));
        Document expectedDocument = new Document(TEST_BASE_URI);
        Document actualDocument = dataUtil.load(testInputStream, null, TEST_BASE_URI, new Parser() {});
        assertEquals(expectedDocument, actualDocument);
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(TEST_INPUT_STREAM_DATA.getBytes(Charset.forName(DEFAULT_CHARSET)));
        OutputStream outputStream = new ByteArrayOutputStream();
        dataUtil.crossStreams(inputStream, outputStream);
        byte[] expectedOutput = TEST_INPUT_STREAM_DATA.getBytes(Charset.forName(DEFAULT_CHARSET));
        byte[] actualOutput = outputStream.toByteArray();
        assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testParseInputStreamWithDefaultCharset() throws IOException {
        InputStream testInputStream = new ByteArrayInputStream(TEST_INPUT_STREAM_DATA.getBytes(Charset.forName(DEFAULT_CHARSET)));
        Document expectedDocument = new Document(TEST_BASE_URI);
        Document actualDocument = dataUtil.parseInputStream(testInputStream, null, TEST_BASE_URI, parser);
        assertEquals(expectedDocument, actualDocument);
    }

    @Test
    public void testParseInputStreamWithSpecifiedCharset() throws IOException {
        InputStream testInputStream = new ByteArrayInputStream(TEST_INPUT_STREAM_DATA.getBytes(Charset.forName("UTF-16")));
        Document expectedDocument = new Document(TEST_BASE_URI);
        Document actualDocument = dataUtil.parseInputStream(testInputStream, "UTF-16", TEST_BASE_URI, parser);
        assertEquals(expectedDocument, actualDocument);
    }

    @Test(expected = IOException.class)
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        InputStream testInputStream = new ByteArrayInputStream(TEST_INPUT_STREAM_DATA.getBytes(Charset.forName("invalidCharset")));
        dataUtil.parseInputStream(testInputStream, "invalidCharset", TEST_BASE_URI, parser);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream testInputStream = new ByteArrayInputStream(TEST_INPUT_STREAM_DATA.getBytes(Charset.forName(DEFAULT_CHARSET)));
        ByteBuffer expectedByteBuffer = ByteBuffer.wrap(TEST_INPUT_STREAM_DATA.getBytes(Charset.forName(DEFAULT_CHARSET)));
        ByteBuffer actualByteBuffer = dataUtil.readToByteBuffer(testInputStream, 0);
        assertEquals(expectedByteBuffer, actualByteBuffer);
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer expectedByteBuffer = DataUtil.emptyByteBuffer();
        ByteBuffer actualByteBuffer = dataUtil.readToByteBuffer(new ByteArrayInputStream(new byte[0]), 0);
        assertEquals(expectedByteBuffer, actualByteBuffer);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=GBK";
        String expectedCharset = "GBK";
        String actualCharset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(expectedCharset, actualCharset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidContentType() {
        String contentType = "text/html";
        String actualCharset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(actualCharset);
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        String expectedCharset = "UTF-8";
        String actualCharset = DataUtil.validateCharset(charset);
        assertEquals(expectedCharset, actualCharset);
    }

    @Test
    public void testValidateCharsetWithInvalidCharset() {
        String charset = "InvalidCharset";
        String actualCharset = DataUtil.validateCharset(charset);
        assertNull(actualCharset);
    }

    @Test
    public void testMimeBoundary() {
        String expectedBoundary = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String actualBoundary = DataUtil.mimeBoundary();
        assertEquals(expectedBoundary, actualBoundary);
    }

    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer bomByteBuffer = ByteBuffer.wrap(new byte[]{0xFF, 0xFE, 0x00, 0x00});
        BomCharset expectedBomCharset = new BomCharset("UTF-16", false);
        BomCharset actualBomCharset = DataUtil.detectCharsetFromBom(bomByteBuffer);
        assertEquals(expectedBomCharset, actualBomCharset);
    }

    @Test
    public void testDetectCharsetFromBomWithInvalidBom() {
        ByteBuffer bomByteBuffer = ByteBuffer.wrap(new byte[]{0x00, 0x00, 0xFF, 0xFE});
        BomCharset actualBomCharset = DataUtil.detectCharsetFromBom(bomByteBuffer);
        assertNull(actualBomCharset);
    }
}