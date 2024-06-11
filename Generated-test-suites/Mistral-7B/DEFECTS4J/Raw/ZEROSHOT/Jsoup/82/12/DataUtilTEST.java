package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * Test class for DataUtil.
 */
public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "This is a test file.";
        File testFile = new File("testFile.html");
        testFile.createNewFile();
        DataUtil.saveToFile(testFile, fileContent, StandardCharsets.UTF_8);

        Document doc = DataUtil.load(testFile, "UTF-8", null);
        assertEquals("This is a test file.", doc.body().text());
        testFile.delete();
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String testString = "This is a test string.";
        InputStream testInputStream = new ByteArrayInputStream(testString.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.load(testInputStream, "UTF-8", null);
        assertEquals(testString, doc.body().text());
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String testString = "<html><body><h1>This is a test string.</h1></body></html>";
        InputStream testInputStream = new ByteArrayInputStream(testString.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.load(testInputStream, "UTF-8", null, Parser.xmlParser());
        Element h1 = doc.select("h1").first();
        assertEquals("This is a test string.", h1.text());
    }

    @Test
    public void testParseInputStream() throws IOException {
        String testString = "<html><body><h1>This is a test string.</h1></body></html>";
        InputStream testInputStream = new ByteArrayInputStream(testString.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.parseInputStream(testInputStream, "UTF-8", null, Parser.xmlParser());
        Element h1 = doc.select("h1").first();
        assertEquals("This is a test string.", h1.text());
    }

    @Test
    public void testCrossStreams() throws IOException {
        String testString = "This is a test string.";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testString.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);
        assertEquals(testString, outputStream.toString());
    }

    @Test
    public void testParseInputStreamWithBOM() throws IOException {
        String testString = "\uFEFFThis is a test string.";
        InputStream testInputStream = new ByteArrayInputStream(testString.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.parseInputStream(testInputStream, null, null, Parser.htmlParser());
        assertEquals(testString.substring(1), doc.body().text()); // Remove BOM
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String testString = "This is a test string.";
        InputStream testInputStream = new ByteArrayInputStream(testString.getBytes(StandardCharsets.UTF_8));

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(testInputStream, 0);
        byte[] expectedByteArray = testString.getBytes(StandardCharsets.UTF_8);
        byte[] actualByteArray = new byte[expectedByteArray.length];
        byteBuffer.get(actualByteArray);

        assertArrayEquals(expectedByteArray, actualByteArray);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String expectedCharset = "UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertEquals(expectedCharset, charset);
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        String invalidCharset = "UTF-8\u0000";

        assertEquals(charset, DataUtil.validateCharset(charset));
        assertEquals(null, DataUtil.validateCharset(invalidCharset));
    }

    @Test
    public void testMimeBoundary() {
        String expectedBoundary = new String(new Random().ints(32, 0, (int) '~').limit(32).toArray())
                .replaceAll("\\s+", "");

        String boundary = DataUtil.mimeBoundary();
        assertEquals(expectedBoundary, boundary);
    }

    @Test
    public void testDetectCharsetFromBom() {
        String testStringUTF8 = "\u00EF\u00BB\u00BFThis is a test.";
        String testStringUTF16BE = "\uFEFF\u0000\u0000UTF-16\u0000\u0000This is a test.";
        String testStringUTF16LE = "\u0000\u0000\uFEFFUTF-16\u0000\u0000This is a test.";
        String testStringUTF32BE = "\u0000\u0000\u0000\u0000UTF-32\u0000\u0000\u0000This is a test.";
        String testStringUTF32LE = "\u0000\u0000\u0000\u0000\u0000UTF-32\u0000\u0000\u0000This is a test.";

        ByteBuffer byteBufferUTF8 = ByteBuffer.wrap(testStringUTF8.getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteBufferUTF16BE = ByteBuffer.wrap(testStringUTF16BE.getBytes(StandardCharsets.UTF_16_BE));
        ByteBuffer byteBufferUTF16LE = ByteBuffer.wrap(testStringUTF16LE.getBytes(StandardCharsets.UTF_16_LE));
        ByteBuffer byteBufferUTF32BE = ByteBuffer.wrap(testStringUTF32BE.getBytes(StandardCharsets.UTF_32_BE));
        ByteBuffer byteBufferUTF32LE = ByteBuffer.wrap(testStringUTF32LE.getBytes(StandardCharsets.UTF_32_LE));

        BomCharset utf8Bom = DataUtil.detectCharsetFromBom(byteBufferUTF8);
        BomCharset utf16BEBom = DataUtil.detectCharsetFromBom(byteBufferUTF16BE);
        BomCharset utf16LEBom = DataUtil.detectCharsetFromBom(byteBufferUTF16LE);
        BomCharset utf32BEBom = DataUtil.detectCharsetFromBom(byteBufferUTF32BE);
        BomCharset utf32LEBom = DataUtil.detectCharsetFromBom(byteBufferUTF32LE);

        assertEquals("UTF-8", utf8Bom.charset);
        assertEquals(false, utf8Bom.offset);
        assertEquals("UTF-16", utf16BEBom.charset);
        assertEquals(true, utf16BEBom.offset);
        assertEquals("UTF-16", utf16LEBom.charset);
        assertEquals(false, utf16LEBom.offset);
        assertEquals("UTF-32", utf32BEBom.charset);
        assertEquals(true, utf32BEBom.offset);
        assertEquals("UTF-32", utf32LEBom.charset);
        assertEquals(false, utf32LEBom.offset);
    }

    /**
     * Helper method to assert arrays are equal.
     *
     * @param expected the expected array
     * @param actual the actual array
     */
    private void assertArrayEquals(byte[] expected, byte[] actual) {
        assertArrayEquals(expected, actual, "Expected and actual byte arrays are not equal.");
    }
}