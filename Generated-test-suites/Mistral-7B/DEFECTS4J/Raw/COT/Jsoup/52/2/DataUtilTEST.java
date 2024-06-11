package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.nio.file.Files;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import org.junit.Test;

public class DataUtilTest {

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        File inputFile = new File("test.html");
        Document doc = DataUtil.load(inputFile, null, null);
        assertNotNull(doc);
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException, UnsupportedCharsetException {
        File inputFile = new File("test.html");
        Document doc = DataUtil.load(inputFile, "UTF-16", null);
        assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidCharset() throws IOException {
        File inputFile = new File("test.html");
        Document doc = DataUtil.load(inputFile, "INVALID_CHARSET", null);
        assertNull(doc);
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String inputData = "test data";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes());
        Document doc = DataUtil.load(inputStream, null, null);
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        String inputData = "test data";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes("UTF-16"));
        Document doc = DataUtil.load(inputStream, "UTF-16", null);
        assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String inputData = "test data";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes());
        Document doc = DataUtil.load(inputStream, "INVALID_CHARSET", null);
        assertNull(doc);
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String inputData = "test data";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes());
        Document doc = DataUtil.load(inputStream, null, null, DataUtil.Parser.xmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testCrossStreams() throws IOException {
        File inputFile = new File("test.html");
        File outputFile = new File("output.html");
        RandomAccessFile input = new RandomAccessFile(inputFile, "r");
        RandomAccessFile output = new RandomAccessFile(outputFile, "rw");

        DataUtil.crossStreams(input, output);

        assert(Files.exists(outputFile.toPath()));
        assert(Files.size(outputFile.toPath()) > 0);

        input.close();
        output.close();
    }

    @Test(expected = IOException.class)
    public void testParseByteDataWithInvalidCharset() {
        ByteBuffer byteData = DataUtil.emptyByteBuffer();
        Document doc = DataUtil.parseByteData(byteData, "INVALID_CHARSET", null, DataUtil.Parser.htmlParser());
        assertNull(doc);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);

        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);

        contentType = "text/html; charset=gb2312";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("gb2312", charset);
    }

    @Test(expected = IllegalCharsetNameException.class)
    public void testValidateCharsetWithInvalidCharset() {
        String invalidCharset = "INVALID_CHARSET";
        String charset = DataUtil.validateCharset(invalidCharset);
        assertNull(charset);
        assertThrows(IllegalCharsetNameException.class, () -> DataUtil.validateCharset(invalidCharset));
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertNotNull(boundary);
        assert(boundary.length() > 0);

        for (int i = 0; i < boundary.length(); i++) {
            char c = boundary.charAt(i);
            assertTrue(Character.isAlphabetic(c) || Character.isDigit(c) || c == '-' || c == '_');
        }
    }
}