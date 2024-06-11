package org.jsoup.helper;

import static org.junit.Assert.*;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    @Test
    public void testLoadFileWithValidCharset() throws IOException {
        String charsetName = "UTF-8";
        File inputFile = new File("validInputFile.html");
        Document doc = DataUtil.load(inputFile, charsetName, "");
        assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidCharset() throws IOException {
        String charsetName = "InvalidCharset";
        File inputFile = new File("validInputFile.html");
        try {
            Document doc = DataUtil.load(inputFile, charsetName, "");
            fail("Expected IOException but no exception was thrown.");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testLoadInputStreamWithValidCharset() throws IOException {
        String charsetName = "UTF-8";
        InputStream inputStream = new ByteArrayInputStream("Valid Input Stream".getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, charsetName, "");
        assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String charsetName = "InvalidCharset";
        InputStream inputStream = new ByteArrayInputStream("Valid Input Stream".getBytes(StandardCharsets.UTF_8));
        try {
            Document doc = DataUtil.load(inputStream, charsetName, "");
            fail("Expected IOException but no exception was thrown.");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testParseInputStreamWithValidCharset() throws IOException {
        String charsetName = "UTF-8";
        InputStream inputStream = new ByteArrayInputStream("Valid Input Stream".getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseInputStream(inputStream, charsetName, "", DataUtil.Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String charsetName = "InvalidCharset";
        InputStream inputStream = new ByteArrayInputStream("Valid Input Stream".getBytes(StandardCharsets.UTF_8));
        try {
            Document doc = DataUtil.parseInputStream(inputStream, charsetName, "", DataUtil.Parser.htmlParser());
            fail("Expected IOException but no exception was thrown.");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputData = "Input Data";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));
        OutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(inputStream, outputStream);
        byte[] expectedOutput = inputData.getBytes(StandardCharsets.UTF_8);
        byte[] output = outputStream.toByteArray();
        assertArrayEquals(expectedOutput, output);
    }

    @Test
    public void testParseInputStreamWithBomUtf16() throws IOException {
        String charsetName = "UTF-16";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0xFF, (byte) 0xFE, 0x00, 0x01, 0x02, 0x03, 0x04, 0x05});
        Document doc = DataUtil.parseInputStream(inputStream, charsetName, "", DataUtil.Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testParseInputStreamWithBomUtf32() throws IOException {
        String charsetName = "UTF-32";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0xFE, (byte) 0xFF, 0x00, 0x01, 0x02, 0x03, 0x04, 0x05});
        Document doc = DataUtil.parseInputStream(inputStream, charsetName, "", DataUtil.Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testParseInputStreamWithoutBom() throws IOException {
        String charsetName = "UTF-8";
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Valid Input Stream".getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseInputStream(inputStream, charsetName, "", DataUtil.Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String expected = "UTF-8";
        assertEquals(expected, DataUtil.getCharsetFromContentType(contentType));
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidCharset() {
        String contentType = "text/html; charset=InvalidCharset";
        String expected = null;
        assertEquals(expected, DataUtil.getCharsetFromContentType(contentType));
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        String expected = "UTF-8";
        assertEquals(expected, DataUtil.validateCharset(charset));
    }

    @Test
    public void testValidateCharsetWithNullInput() {
        String charset = null;
        String expected = null;
        assertEquals(expected, DataUtil.validateCharset(charset));
    }

    @Test
    public void testMimeBoundary() {
        String expected = "RandomString";
        String generated = DataUtil.mimeBoundary();
        assertEquals(expected.length(), generated.length());
        for (int i = 0; i < expected.length(); i++) {
            assertEquals(expected.charAt(i), generated.charAt(i));
        }
    }
}