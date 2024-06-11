package org.jsoup.helper;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.io.UncheckedIOException;
import java.nio.ByteBuffer;
import java.nio.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws Exception {
        String baseUri = "http://example.com";
        String filePath = "testFile.html";
        File file = new File(filePath);
        String charset = "UTF-8";

        Document doc = DataUtil.load(file, charset, baseUri);
        assertNotNull(doc);
    }

    @Test
    public void testLoadInvalidFile() throws Exception {
        String baseUri = "http://example.com";
        String filePath = "nonExistentFile.html";
        File file = new File(filePath);
        String charset = "UTF-8";

        try {
            Document doc = DataUtil.load(file, charset, baseUri);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testLoadInputStream() throws Exception {
        String charset = "UTF-8";
        String testString = "This is a test";
        InputStream inputStream = new ByteArrayInputStream(testString.getBytes(StandardCharsets.UTF_8));
        String baseUri = "http://example.com";

        Document doc = DataUtil.load(inputStream, charset, baseUri);
        assertNotNull(doc);
        assertEquals(testString, doc.body().text());
    }

    @Test
    public void testLoadInputStreamWithParser() throws Exception {
        String charset = "UTF-8";
        String testString = "This is a test";
        InputStream inputStream = new ByteArrayInputStream(testString.getBytes(StandardCharsets.UTF_8));
        String baseUri = "http://example.com";
        Parser parser = Parser.xmlParser();

        Document doc = DataUtil.load(inputStream, charset, baseUri, parser);
        assertNotNull(doc);
        assertEquals(testString, doc.body().text());
    }

    @Test
    public void testCrossStreams() throws Exception {
        String sourceString = "Source Content";
        String targetString = "Target Content";
        InputStream inputStream = new ByteArrayInputStream(sourceString.getBytes(StandardCharsets.UTF_8));
        OutputStream outputStream = new FileOutputStream("testFile.txt");

        DataUtil.crossStreams(inputStream, outputStream);

        File file = new File("testFile.txt");
        InputStream input = new FileInputStream(file);
        String resultString = new String(input.readAllBytes());
        assertEquals(targetString, resultString);
    }

    @Test
    public void testParseInputStream() throws Exception {
        String charset = "UTF-8";
        String testString = "This is a test";
        InputStream inputStream = new ByteArrayInputStream(testString.getBytes(StandardCharsets.UTF_8));
        String baseUri = "http://example.com";

        Document doc = DataUtil.parseInputStream(inputStream, charset, baseUri, Parser.htmlParser());
        assertNotNull(doc);
        assertEquals(testString, doc.body().text());
    }

    @Test
    public void testParseInputStreamWithParser() throws Exception {
        String charset = "UTF-8";
        String testString = "This is a test";
        InputStream inputStream = new ByteArrayInputStream(testString.getBytes(StandardCharsets.UTF_8));
        String baseUri = "http://example.com";
        Parser parser = Parser.xmlParser();

        Document doc = DataUtil.parseInputStream(inputStream, charset, baseUri, parser);
        assertNotNull(doc);
        assertEquals(testString, doc.body().text());
    }

    @Test
    public void testParseInputStreamWithMismatchedParser() throws Exception {
        String charset = "UTF-8";
        String testString = "This is a test";
        InputStream inputStream = new ByteArrayInputStream(testString.getBytes(StandardCharsets.UTF_8));
        String baseUri = "http://example.com";
        Parser parser = Parser.xmlParser();

        try {
            Document doc = DataUtil.parseInputStream(inputStream, charset, baseUri, Parser.htmlParser());
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testReadToByteBuffer() throws Exception {
        String testString = "This is a test";
        InputStream inputStream = new ByteArrayInputStream(testString.getBytes(StandardCharsets.UTF_8));
        int maxSize = testString.length();

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);
        byte[] expectedByteArray = testString.getBytes(StandardCharsets.UTF_8);

        byte[] actualByteArray = new byte[byteBuffer.limit()];
        byteBuffer.get(actualByteArray);

        assertArrayEquals(expectedByteArray, actualByteArray);
    }

    @Test
    public void testReadToByteBufferWithInterruptedThread() throws Exception {
        String testString = "This is a test";
        InputStream inputStream = new ByteArrayInputStream(testString.getBytes(StandardCharsets.UTF_8));
        int maxSize = testString.length();

        Thread thread = new Thread(() -> {
            try {
                DataUtil.readToByteBuffer(inputStream, maxSize);
            } catch (InterruptedException e) {
                // Expected
            }
        });
        thread.start();
        thread.interrupt();
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        assertEquals(0, byteBuffer.limit());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String expectedCharset = "UTF-8";

        String resultCharset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(expectedCharset, resultCharset);
    }

    @Test
    public void testGetCharsetFromContentTypeNoCharsetFound() {
        String contentType = "text/html";
        String expectedCharset = null;

        String resultCharset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(expectedCharset, resultCharset);
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        String charsetValidation = "UTF-8";

        String resultCharset = DataUtil.validateCharset(charset);
        assertEquals(charsetValidation, resultCharset);
    }

    @Test
    public void testValidateCharsetInvalidCharset() {
        String charset = "InvalidCharset";
        String charsetValidation = null;

        String resultCharset = DataUtil.validateCharset(charset);
        assertEquals(charsetValidation, resultCharset);
    }

    @Test
    public void testMimeBoundary() {
        String expectedMimeBoundary = "---------------------------12345678901234567890123456789012";
        String resultMimeBoundary = DataUtil.mimeBoundary();

        assertEquals(expectedMimeBoundary, resultMimeBoundary);
    }

    @Test
    public void testDetectCharsetFromBom() throws Exception {
        String charsetExpectedForUtf32 = "UTF-32";
        String charsetExpectedForUtf16Be = "UTF-16";
        String charsetExpectedForUtf8 = "UTF-8";

        byte[] bomUtf32 = {0x00, 0x00, 0xFE, 0xFF};
        byte[] bomUtf16Be = {0xFE, 0xFF, 0x00, 0x00};
        byte[] bomUtf8 = {0xEF, 0xBB, 0xBF};

        ByteBuffer byteBufferUtf32 = ByteBuffer.wrap(bomUtf32);
        BomCharset utf32BomCharset = DataUtil.detectCharsetFromBom(byteBufferUtf32);
        assertEquals(charsetExpectedForUtf32, utf32BomCharset.charset);

        ByteBuffer byteBufferUtf16Be = ByteBuffer.wrap(bomUtf16Be);
        BomCharset utf16BeBomCharset = DataUtil.detectCharsetFromBom(byteBufferUtf16Be);
        assertEquals(charsetExpectedForUtf16Be, utf16BeBomCharset.charset);

        ByteBuffer byteBufferUtf8 = ByteBuffer.wrap(bomUtf8);
        BomCharset utf8BomCharset = DataUtil.detectCharsetFromBom(byteBufferUtf8);
        assertEquals(charsetExpectedForUtf8, utf8BomCharset.charset);

        ByteBuffer byteBufferWithoutBom = ByteBuffer.allocate(0);
        BomCharset noBomCharset = DataUtil.detectCharsetFromBom(byteBufferWithoutBom);
        assertNull(noBomCharset);
    }
}

class ByteArrayInputStream extends InputStream {
    private final byte[] data;
    private int position;

    ByteArrayInputStream(byte[] data) {
        this.data = data;
    }

    @Override
    public int read() throws IOException {
        if (position < data.length) {
            return data[position++];
        }
        return -1;
    }
}