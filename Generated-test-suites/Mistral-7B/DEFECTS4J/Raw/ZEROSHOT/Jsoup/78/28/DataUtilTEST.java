package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.jsoup.Jsoup;
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
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @org.junit.Test
    public void testLoadFromFileWithDefaultCharset() throws IOException {
        String filePath = "testData/dataUtilTestFile.html"; // Replace with the actual file path
        Document document = DataUtil.load(new File(filePath), null);
        assertEquals("Expected document content", Jsoup.parse(new File(filePath)).text(), document.text());
    }

    @org.junit.Test
    public void testLoadFromFileWithSpecifiedCharset() throws IOException {
        String filePath = "testData/dataUtilTestFile.html"; // Replace with the actual file path
        String charset = "UTF-16"; // Replace with the actual charset
        Document document = DataUtil.load(new File(filePath), charset);
        assertEquals("Expected document content", Jsoup.parse(new File(filePath), charset).text(), document.text());
    }

    @org.junit.Test
    public void testLoadFromInputStreamWithDefaultCharset() throws IOException {
        String testString = "Test string for DataUtilTest.";
        InputStream inputStream = new ByteArrayInputStream(testString.getBytes());
        Document document = DataUtil.load(inputStream, null);
        assertEquals("Expected document content", testString, document.text());
    }

    @org.junit.Test
    public void testLoadFromInputStreamWithSpecifiedCharset() throws IOException {
        String testString = "Test string for DataUtilTest.";
        String charset = "UTF-16"; // Replace with the actual charset
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(testString.getBytes(StandardCharsets.UTF_8));
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Document document = DataUtil.load(inputStream, charset);
        assertEquals("Expected document content", testString, document.text());
    }

    @org.junit.Test
    public void testLoadFromInputStreamWithInvalidCharset() throws IOException {
        String testString = "Test string for DataUtilTest.";
        String charset = "InvalidCharset";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(testString.getBytes(StandardCharsets.UTF_8));
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        assertThrows(IllegalArgumentException.class, () -> DataUtil.load(inputStream, charset));
    }

    @org.junit.Test
    public void testParseInputStreamWithDefaultCharset() throws IOException {
        String testString = "Test string for DataUtilTest.";
        InputStream inputStream = new ByteArrayInputStream(testString.getBytes());
        Document document = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
        assertEquals("Expected document content", testString, document.text());
    }

    @org.junit.Test
    public void testParseInputStreamWithSpecifiedCharset() throws IOException {
        String testString = "Test string for DataUtilTest.";
        String charset = "UTF-16"; // Replace with the actual charset
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(testString.getBytes(StandardCharsets.UTF_8));
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Document document = DataUtil.parseInputStream(inputStream, charset, null, Parser.htmlParser());
        assertEquals("Expected document content", testString, document.text());
    }

    @org.junit.Test
    public void testCrossStreams() throws IOException {
        String testString = "Test string for DataUtilTest.";
        byte[] inputData = testString.getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(inputData);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        DataUtil.crossStreams(input, output);
        assertEquals("Expected output content", testString, output.toString());
    }

    @org.junit.Test
    public void testDetectCharsetFromBomUTF32() throws IOException {
        String testString = "Test string for DataUtilTest.";
        ByteBuffer byteData = ByteBuffer.wrap(testString.getBytes(StandardCharsets.UTF_32));
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteData);
        assertEquals("Expected charset", "UTF-32", bomCharset.charset);
    }

    @org.junit.Test
    public void testDetectCharsetFromBomUTF16BE() throws IOException {
        String testString = "Test string for DataUtilTest.";
        ByteBuffer byteData = ByteBuffer.wrap(testString.getBytes(StandardCharsets.UTF_16));
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteData);
        assertEquals("Expected charset", "UTF-16", bomCharset.charset);
    }

    @org.junit.Test
    public void testDetectCharsetFromBomUTF8() throws IOException {
        String testString = "Test string for DataUtilTest.";
        ByteBuffer byteData = ByteBuffer.wrap(testString.getBytes(StandardCharsets.UTF_8));
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteData);
        assertEquals("Expected charset", "UTF-8", bomCharset.charset);
    }

    @org.junit.Test
    public void testDetectCharsetFromBomInvalid() throws IOException {
        String testString = "Test string for DataUtilTest.";
        ByteBuffer byteData = ByteBuffer.wrap(testString.getBytes(StandardCharsets.UTF_8));
        assertNull("Expected null", DataUtil.detectCharsetFromBom(byteData));
    }

    @org.junit.Test
    public void testGetCharsetFromContentType() throws IOException {
        String testString = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(testString);
        assertEquals("Expected charset", "UTF-8", charset);
    }

    @org.junit.Test
    public void testGetCharsetFromContentTypeInvalid() throws IOException {
        String testString = "text/html; charset=InvalidCharset";
        String charset = DataUtil.getCharsetFromContentType(testString);
        assertNull("Expected null", charset);
    }

    @org.junit.Test
    public void testValidateCharsetUTF8() throws IOException {
        String charset = "UTF-8";
        String validatedCharset = DataUtil.validateCharset(charset);
        assertEquals("Expected charset", charset, validatedCharset);
    }

    @org.junit.Test
    public void testValidateCharsetInvalidUTF8() throws IOException {
        String charset = "InvalidCharset";
        String validatedCharset = DataUtil.validateCharset(charset);
        assertNull("Expected null", validatedCharset);
    }

    @org.junit.Test
    public void testValidateCharsetUTF16() throws IOException {
        String charset = "UTF-16";
        String validatedCharset = DataUtil.validateCharset(charset);
        assertEquals("Expected charset", charset, validatedCharset);
    }

    @org.junit.Test
    public void testValidateCharsetInvalidUTF16() throws IOException {
        String charset = "InvalidCharset";
        String validatedCharset = DataUtil.validateCharset(charset);
        assertNull("Expected null", validatedCharset);
    }

    @org.junit.Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        assertNotNull("Expected non-empty string", mimeBoundary);
        assertEquals("Expected length", 32, mimeBoundary.length());
    }
}