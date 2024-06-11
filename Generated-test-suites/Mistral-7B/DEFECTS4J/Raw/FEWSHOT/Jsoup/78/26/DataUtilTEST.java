package org.jsoup.helper;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testLoad() throws IOException {
        File testFile = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(testFile, charsetName, baseUri);
        assertNotNull(doc);
    }

    @Test
    public void testLoadWithNullCharsetName() throws IOException {
        File testFile = new File("test.html");
        String baseUri = "http://example.com";
        try {
            DataUtil.load(testFile, null, baseUri);
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testLoadWithInvalidCharsetName() throws IOException {
        File testFile = new File("test.html");
        String baseUri = "http://example.com";
        try {
            DataUtil.load(testFile, "invalidCharset", baseUri);
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testLoadWithEmptyInputStream() throws IOException {
        InputStream testInput = new ByteArrayInputStream(new byte[0]);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(testInput, charsetName, baseUri);
        assertEquals(new Document(baseUri), doc);
    }

    @Test
    public void testParseInputStream() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        // Test with a null input stream
        try {
            Document doc = DataUtil.parseInputStream(null, charsetName, baseUri, parser);
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }

        // Test with an empty input stream
        InputStream testInput = new ByteArrayInputStream(new byte[0]);
        Document doc = DataUtil.parseInputStream(testInput, charsetName, baseUri, parser);
        assertEquals(new Document(baseUri), doc);

        // Test with a valid input stream
        InputStream testInput2 = new FileInputStream(new File("test.html"));
        Document doc2 = DataUtil.parseInputStream(testInput2, charsetName, baseUri, parser);
        assertNotNull(doc2);
        testInput2.close();
    }

    @Test
    public void testCrossStreams() throws IOException {
        File testFile = new File("test.html");
        InputStream input = new FileInputStream(testFile);
        OutputStream output = new ByteArrayOutputStream();

        DataUtil.crossStreams(input, output);

        input.close();
        assertEquals(new String(output.toByteArray()), DataUtil.readFileIntoString(testFile));
        output.close();
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        File testFile = new File("test.html");
        InputStream input = new FileInputStream(testFile);

        ByteBuffer buffer = DataUtil.readToByteBuffer(input);
        assertEquals(DataUtil.readFileToByteBuffer(testFile).remaining(), buffer.remaining());
        input.close();
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        File testFile = new File("test.html");
        InputStream input = new FileInputStream(testFile);

        ByteBuffer buffer = DataUtil.readToByteBuffer(input, 10);
        assertEquals(10, buffer.limit());
        input.close();
    }

    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer byteDataUtf32 = DataUtil.readToByteBuffer(new ByteArrayInputStream(new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0xFE, (byte) 0xFF}));
        BomCharset bomCharsetUtf32 = DataUtil.detectCharsetFromBom(byteDataUtf32);
        assertEquals("UTF-32", bomCharsetUtf32.charset);

        ByteBuffer byteDataUtf16 = DataUtil.readToByteBuffer(new ByteArrayInputStream(new byte[]{(byte) 0xFF, (byte) 0xFE, (byte) 0x00, (byte) 0x00}));
        BomCharset bomCharsetUtf16 = DataUtil.detectCharsetFromBom(byteDataUtf16);
        assertEquals("UTF-16", bomCharsetUtf16.charset);

        ByteBuffer byteDataUtf8 = DataUtil.readToByteBuffer(new ByteArrayInputStream(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF}));
        BomCharset bomCharsetUtf8 = DataUtil.detectCharsetFromBom(byteDataUtf8);
        assertEquals("UTF-8", bomCharsetUtf8.charset);

        ByteBuffer byteDataInvalid = DataUtil.readToByteBuffer(new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03}));
        assertNull(DataUtil.detectCharsetFromBom(byteDataInvalid));
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType1 = "text/html; charset=UTF-8";
        String charset1 = DataUtil.getCharsetFromContentType(contentType1);
        assertEquals("UTF-8", charset1);

        String contentType2 = "text/html";
        String charset2 = DataUtil.getCharsetFromContentType(contentType2);
        assertNull(charset2);

        String contentType3 = "text/html; charset=gb2312";
        String charset3 = DataUtil.getCharsetFromContentType(contentType3);
        assertEquals("gb2312", charset3);
    }

    @Test
    public void testValidateCharset() {
        String charset1 = "UTF-8";
        String charset2 = "INVALID_CHARSET";
        String charset3 = "gb2312";
        String charset4 = null;

        String validatedCharset1 = DataUtil.validateCharset(charset1);
        assertEquals(charset1, validatedCharset1);

        String validatedCharset2 = DataUtil.validateCharset(charset2);
        assertNull(validatedCharset2);

        String validatedCharset3 = DataUtil.validateCharset(charset3);
        assertEquals(charset3, validatedCharset3);

        String validatedCharset4 = DataUtil.validateCharset(charset4);
        assertNull(validatedCharset4);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertFalse(boundary.isEmpty());
    }

    private static Document newDocument() {
        Parser parser = Parser.htmlParser();
        return parser.parseInput("", null);
    }

    private void assertNotNull(Document doc) {
        assertNotNull(doc);
    }

    private void assertNull(Document doc) {
        assertNull(doc);
    }

    private void assertSame(Document doc1, Document doc2) {
        assertSame(doc1, doc2);
    }

    private void assertArrayEquals(byte[] expected, byte[] actual) {
        assertArrayEquals(expected, actual, "byte array comparison failed");
    }

    private void assertFalse(boolean condition) {
        assertFalse(condition, "Expected false, but got true");
    }

    private void fail(String message) {
        fail(message, new Throwable[0]);
    }

    private void fail(String message, Throwable cause) {
        fail(message);
        throw cause;
    }

    private String readFileIntoString(File file) throws IOException {
        InputStream input = new FileInputStream(file);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
        input.close();
        return output.toString();
    }
}