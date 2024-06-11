package org.jsoup.helper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class DataUtilTest {

    private static final String TEST_FILE_PATH = "test.html";
    private static final String TEST_CHARSET = "UTF-8";
    private Document doc;

    @Before
    public void setUp() throws Exception {
        when(DataUtil.Parser.htmlParser()).thenReturn(mock(Parser.class));
        doc = new Document("baseUri");
    }

    @After
    public void tearDown() throws Exception {
        DataUtil.saveFileFromString("test.html", "", "UTF-8"); // clear test file
    }

    @Test
    public void testLoadFileWithValidCharset() throws IOException {
        File testFile = new File(TEST_FILE_PATH);
        DataUtil.saveFileFromString("test.html", "<html><head><meta charset=\"UTF-8\"></head><body></body></html>", TEST_CHARSET);

        Document actual = DataUtil.load(testFile, TEST_CHARSET, "baseUri");
        assertEquals("<html><head><meta charset=\"UTF-8\"></head><body></body></html>", actual.html());
    }

    @Test
    public void testLoadFileWithInvalidCharset() throws IOException {
        File testFile = new File(TEST_FILE_PATH);
        DataUtil.saveFileFromString("test.html", "<html><head><meta charset=\"ISO-8859-1\"></head><body></body></html>", TEST_CHARSET);

        Document actual = DataUtil.load(testFile, null, "baseUri");
        assertEquals("<html><head><meta charset=\"ISO-8859-1\"></head><body></body></html>", actual.html());
    }

    @Test
    public void testLoadInputStreamWithValidCharset() throws IOException {
        String testInput = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        InputStream testInputStream = new ByteArrayInputStream(testInput.getBytes(TEST_CHARSET));

        Document actual = DataUtil.load(testInputStream, TEST_CHARSET, "baseUri");
        assertEquals("<html><head><meta charset=\"UTF-8\"></head><body></body></html>", actual.html());
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String testInput = "<html><head><meta charset=\"ISO-8859-1\"></head><body></body></html>";
        InputStream testInputStream = new ByteArrayInputStream(testInput.getBytes("ISO-8859-1"));

        Document actual = DataUtil.load(testInputStream, null, "baseUri");
        assertEquals("<html><head><meta charset=\"ISO-8859-1\"></head><body></body></html>", actual.html());
    }

    @Test
    public void testLoadInputStreamWithNullCharset() throws IOException {
        String testInput = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        InputStream testInputStream = new ByteArrayInputStream(testInput.getBytes(TEST_CHARSET));

        Document actual = DataUtil.load(testInputStream, null, "baseUri");
        assertEquals("<html><head><meta charset=\"UTF-8\"></head><body></body></html>", actual.html());
    }

    @Test(expected = IOException.class)
    public void testLoadInvalidInputStream() throws IOException {
        InputStream testInputStream = mock(InputStream.class);
        when(testInputStream.read()).thenThrow(new IOException("Invalid InputStream"));

        try {
            DataUtil.load(testInputStream, TEST_CHARSET, "baseUri");
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testParseInputStreamWithValidCharset() throws IOException {
        String testInput = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        InputStream testInputStream = new ByteArrayInputStream(testInput.getBytes(TEST_CHARSET));

        Document actual = DataUtil.parseInputStream(testInputStream, TEST_CHARSET, "baseUri", DataUtil.Parser.htmlParser());
        assertEquals("<html><head><meta charset=\"UTF-8\"></head><body></body></html>", actual.html());
    }

    @Test
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String testInput = "<html><head><meta charset=\"ISO-8859-1\"></head><body></body></html>";
        InputStream testInputStream = new ByteArrayInputStream(testInput.getBytes("ISO-8859-1"));

        Document actual = DataUtil.parseInputStream(testInputStream, null, "baseUri", DataUtil.Parser.htmlParser());
        assertEquals("<html><head><meta charset=\"ISO-8859-1\"></head><body></body></html>", actual.html());
    }

    @Test
    public void testParseInputStreamWithNullCharset() throws IOException {
        String testInput = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        InputStream testInputStream = new ByteArrayInputStream(testInput.getBytes(TEST_CHARSET));

        Document actual = DataUtil.parseInputStream(testInputStream, null, "baseUri", DataUtil.Parser.htmlParser());
        assertEquals("<html><head><meta charset=\"UTF-8\"></head><body></body></html>", actual.html());
    }

    @Test
    public void testParseInputStreamWithInvalidParser() throws IOException {
        String testInput = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        InputStream testInputStream = new ByteArrayInputStream(testInput.getBytes(TEST_CHARSET));

        try {
            DataUtil.parseInputStream(testInputStream, TEST_CHARSET, "baseUri", DataUtil.Parser.xmlParser());
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testCrossStreams() throws IOException {
        String input = "input";
        String output = "output";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        OutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);
        assertEquals(input, output);
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer actual = DataUtil.emptyByteBuffer();
        assertTrue(actual.capacity() == 0);
    }

    @Test
    public void testReadToByteBufferWithValidFile() throws IOException {
        String testContent = "test content";
        File testFile = new File(TEST_FILE_PATH);
        DataUtil.saveFileFromString(TEST_FILE_PATH, testContent, TEST_CHARSET);

        ByteBuffer actual = DataUtil.readToByteBuffer(testFile);
        assertEquals(testContent.getBytes(TEST_CHARSET).length, actual.limit());
    }

    @Test
    public void testReadToByteBufferWithInvalidFile() {
        File testFile = new File(TEST_FILE_PATH);

        ByteBuffer actual = DataUtil.readToByteBuffer(testFile);
        assertEquals(0, actual.limit());
    }

    @Test
    public void testReadToByteBufferWithNullFile() {
        ByteBuffer actual = DataUtil.readToByteBuffer(null);
        assertEquals(0, actual.limit());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String testContentType = "text/html; charset=UTF-8";
        String actual = DataUtil.getCharsetFromContentType(testContentType);
        assertEquals("UTF-8", actual);
    }

    @Test
    public void testGetCharsetFromContentTypeWithNullContentType() {
        String actual = DataUtil.getCharsetFromContentType(null);
        assertNull(actual);
    }

    @Test
    public void testValidateCharset() {
        String actual = DataUtil.validateCharset("UTF-8");
        assertEquals("UTF-8", actual);

        String actual2 = DataUtil.validateCharset("ISO-8859-1");
        assertEquals("ISO-8859-1", actual2);

        String actual3 = DataUtil.validateCharset("ISO-8859-12");
        assertNull(actual3);
    }

    @Test
    public void testMimeBoundary() {
        String actual = DataUtil.mimeBoundary();
        int length = actual.length();
        for (int i = 0; i < length; i++) {
            assertTrue(mimeBoundaryChars[i] == actual.charAt(i) || mimeBoundaryChars[i - boundaryLength] == actual.charAt(i));
        }
    }

    @Test
    public void testDetectCharsetFromBom() throws IOException {
        ByteBuffer testBomUtf32 = ByteBuffer.wrap(new byte[]{0x00, 0x00, 0xFE, 0xFF});
        ByteBuffer testBomUtf16BE = ByteBuffer.wrap(new byte[]{0xFE, 0xFF, 0x00, 0x00});
        ByteBuffer testBomUtf16LE = ByteBuffer.wrap(new byte[]{0xFF, 0xFE, 0x00, 0x00});
        ByteBuffer testBomUtf8 = ByteBuffer.wrap(new byte[]{0xEF, 0xBB, 0xBF});

        BomCharset actualUtf32 = DataUtil.detectCharsetFromBom(testBomUtf32);
        BomCharset actualUtf16BE = DataUtil.detectCharsetFromBom(testBomUtf16BE);
        BomCharset actualUtf16LE = DataUtil.detectCharsetFromBom(testBomUtf16LE);
        BomCharset actualUtf8 = DataUtil.detectCharsetFromBom(testBomUtf8);

        assertTrue(actualUtf32 != null && actualUtf32.charset.equals("UTF-32"));
        assertTrue(actualUtf16BE != null && actualUtf16BE.charset.equals("UTF-16") && actualUtf16BE.offset);
        assertTrue(actualUtf16LE != null && actualUtf16LE.charset.equals("UTF-16") && !actualUtf16LE.offset);
        assertTrue(actualUtf8 != null && actualUtf8.charset.equals("UTF-8") && !actualUtf8.offset);
    }

    @Test
    public void testSaveFileFromString() throws IOException {
        String testContent = "test content";
        DataUtil.saveFileFromString(TEST_FILE_PATH, testContent, TEST_CHARSET);

        File testFile = new File(TEST_FILE_PATH);
        String actual = new String(testFile.length() > 0 ? testFile.getBytes() : new byte[0], TEST_CHARSET);
        assertEquals(testContent, actual);
    }
}

class BomCharset {
    private final String charset;
    private final boolean offset;

    public BomCharset(String charset, boolean offset) {
        this.charset = charset;
        this.offset = offset;
    }
}