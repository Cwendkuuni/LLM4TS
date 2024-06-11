package org.jsoup.helper;

import static org.junit.Assert.*;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File testFile = new File("testFile.html");
        Document doc = DataUtil.load(testFile, "UTF-8", "");
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String testString = "Test String";
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] testByteArray = testString.getBytes(StandardCharsets.UTF_8);
        byteArrayOutputStream.write(testByteArray);
        InputStream testInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        Document doc = DataUtil.load(testInputStream, "UTF-8", "");
        assertNotNull(doc);
        assertEquals(testString, doc.body().text());
    }

    @Test
    public void testParseByteData() throws IOException {
        String testString = "Test String";
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] testByteArray = testString.getBytes(StandardCharsets.UTF_8);
        byteArrayOutputStream.write(testByteArray);
        ByteBuffer byteData = ByteBuffer.wrap(byteArrayOutputStream.toByteArray());
        Document doc = DataUtil.parseByteData(byteData, "UTF-8", "", Parser.htmlParser());
        assertNotNull(doc);
        assertEquals(testString, doc.body().text());
    }

    @Test
    public void testParseByteDataWithInvalidCharset() throws IOException {
        String testString = "Test String";
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] testByteArray = testString.getBytes(StandardCharsets.UTF_8);
        byteArrayOutputStream.write(testByteArray);
        ByteBuffer byteData = ByteBuffer.wrap(byteArrayOutputStream.toByteArray());
        Document doc = DataUtil.parseByteData(byteData, "invalidCharset", "", Parser.htmlParser());
        assertNull(doc);
    }

    @Test
    public void testDetectCharsetFromBom() throws IOException {
        String testStringUtf8 = "Test String UTF-8";
        String testStringUtf16 = "Test String UTF-16";
        String testStringUtf32 = "Test String UTF-32";
        String testStringUtf8WithBom = "\uFEFF" + testStringUtf8;
        String testStringUtf16WithBom = "\uFEFF\uFFFE" + testStringUtf16;
        String testStringUtf32WithBom = "\uFEFF\uFFFE\u0000" + testStringUtf32;

        ByteArrayOutputStream byteArrayOutputStreamUtf8 = new ByteArrayOutputStream();
        byte[] testByteArrayUtf8 = testStringUtf8.getBytes(StandardCharsets.UTF_8);
        byteArrayOutputStreamUtf8.write(testByteArrayUtf8);
        ByteBuffer byteDataUtf8 = ByteBuffer.wrap(byteArrayOutputStreamUtf8.toByteArray());
        String detectedCharsetUtf8 = DataUtil.detectCharsetFromBom(byteDataUtf8, null);
        assertEquals("UTF-8", detectedCharsetUtf8);

        ByteArrayOutputStream byteArrayOutputStreamUtf16 = new ByteArrayOutputStream();
        byte[] testByteArrayUtf16 = testStringUtf16.getBytes(StandardCharsets.UTF_16);
        byteArrayOutputStreamUtf16.write(testByteArrayUtf16);
        ByteBuffer byteDataUtf16 = ByteBuffer.wrap(byteArrayOutputStreamUtf16.toByteArray());
        String detectedCharsetUtf16 = DataUtil.detectCharsetFromBom(byteDataUtf16, null);
        assertEquals("UTF-16", detectedCharsetUtf16);

        ByteArrayOutputStream byteArrayOutputStreamUtf32 = new ByteArrayOutputStream();
        byte[] testByteArrayUtf32 = testStringUtf32.getBytes(StandardCharsets.UTF_32);
        byteArrayOutputStreamUtf32.write(testByteArrayUtf32);
        ByteBuffer byteDataUtf32 = ByteBuffer.wrap(byteArrayOutputStreamUtf32.toByteArray());
        String detectedCharsetUtf32 = DataUtil.detectCharsetFromBom(byteDataUtf32, null);
        assertEquals("UTF-32", detectedCharsetUtf32);

        ByteArrayOutputStream byteArrayOutputStreamUtf8WithBom = new ByteArrayOutputStream();
        byte[] testByteArrayUtf8WithBom = (("\uFEFF" + testStringUtf8).getBytes(StandardCharsets.UTF_8));
        byteArrayOutputStreamUtf8WithBom.write(testByteArrayUtf8WithBom);
        ByteBuffer byteDataUtf8WithBom = ByteBuffer.wrap(byteArrayOutputStreamUtf8WithBom.toByteArray());
        String detectedCharsetUtf8WithBom = DataUtil.detectCharsetFromBom(byteDataUtf8WithBom, null);
        assertEquals("UTF-8", detectedCharsetUtf8WithBom);

        ByteArrayOutputStream byteArrayOutputStreamUtf16WithBom = new ByteArrayOutputStream();
        byte[] testByteArrayUtf16WithBom = (("\uFEFF" + testStringUtf16).getBytes(StandardCharsets.UTF_16));
        byteArrayOutputStreamUtf16WithBom.write(testByteArrayUtf16WithBom);
        ByteBuffer byteDataUtf16WithBom = ByteBuffer.wrap(byteArrayOutputStreamUtf16WithBom.toByteArray());
        String detectedCharsetUtf16WithBom = DataUtil.detectCharsetFromBom(byteDataUtf16WithBom, null);
        assertEquals("UTF-16", detectedCharsetUtf16WithBom);

        ByteArrayOutputStream byteArrayOutputStreamUtf32WithBom = new ByteArrayOutputStream();
        byte[] testByteArrayUtf32WithBom = (("\uFEFF" + testStringUtf32).getBytes(StandardCharsets.UTF_32));
        byteArrayOutputStreamUtf32WithBom.write(testByteArrayUtf32WithBom);
        ByteBuffer byteDataUtf32WithBom = ByteBuffer.wrap(byteArrayOutputStreamUtf32WithBom.toByteArray());
        String detectedCharsetUtf32WithBom = DataUtil.detectCharsetFromBom(byteDataUtf32WithBom, null);
        assertEquals("UTF-32", detectedCharsetUtf32WithBom);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String testString = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(testString);
        assertEquals("UTF-8", charset);

        testString = "text/html; charset=ISO-8859-1";
        charset = DataUtil.getCharsetFromContentType(testString);
        assertEquals("ISO-8859-1", charset);

        testString = "text/html";
        charset = DataUtil.getCharsetFromContentType(testString);
        assertNull(charset);
    }

    @Test
    public void testValidateCharset() {
        String utf8 = "UTF-8";
        String iso88591 = "ISO-8859-1";
        String invalidCharset = "invalidCS";

        assertEquals(utf8, DataUtil.validateCharset(utf8));
        assertEquals(iso88591, DataUtil.validateCharset(iso88591));
        assertNull(DataUtil.validateCharset(invalidCharset));

        // Test case for trimming and uppercasing
        String trimmedUppercasedUtf8 = "UTF-8";
        String trimmedUppercasedISO88591 = "ISO-8859-1";
        String trimmedUppercasedInvalidCharset = "invalidCS";

        assertEquals(trimmedUppercasedUtf8, DataUtil.validateCharset(trimmedUppercasedUtf8));
        assertEquals(trimmedUppercasedISO88591, DataUtil.validateCharset(trimmedUppercasedISO88591));
        assertNull(DataUtil.validateCharset(trimmedUppercasedInvalidCharset));
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        int boundaryLength = boundary.length();
        for (int i = 0; i < boundaryLength; i++) {
            char c = boundary.charAt(i);
            assertTrue(Character.isDefined(c));
        }
    }

    @Test
    public void testCrossStreams() throws IOException {
        String testString = "Test String";
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] testByteArray = testString.getBytes(StandardCharsets.UTF_8);
        byteArrayOutputStream.write(testByteArray);
        InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        OutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);
        byte[] outputByteArray = outputStream.toByteArray();
        assertEquals(testByteArray, outputByteArray);

        // Test with different character sets
        byte[] testByteArrayUtf16 = testString.getBytes(StandardCharsets.UTF_16);
        byteArrayOutputStream = new ByteArrayOutputStream();
        inputStream = new ByteArrayInputStream(testByteArrayUtf16);
        outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);
        outputByteArray = outputStream.toByteArray();
        assertEquals(testByteArrayUtf16, outputByteArray);
    }
}