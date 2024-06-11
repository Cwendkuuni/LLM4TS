package org.jsoup.helper;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Random;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html"); // Assuming a test file exists in the classpath
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(doc);
        // Additional asserts based on the content of the test file
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String htmlContent = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(Charset.forName("UTF-8")));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(doc);
        // Additional asserts based on the content of the input stream
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String htmlContent = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(Charset.forName("UTF-8")));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.load(inputStream, charsetName, baseUri, parser);
        assertNotNull(doc);
        // Additional asserts based on the content of the input stream
    }

    @Test
    public void testParseInputStream() throws IOException {
        String htmlContent = "<html><head></head><body></body></html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(Charset.forName("UTF-8")));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        DataUtil.parseInputStream(inputStream, charsetName, baseUri, Parser.htmlParser());
        InputStream parsedInputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Document doc = DataUtil.load(parsedInputStream, charsetName, baseUri);
        assertNotNull(doc);
        // Additional asserts based on the content of the input stream and the parsed document
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputText = "Hello, World!";
        InputStream inputStream = new ByteArrayInputStream(inputText.getBytes(Charset.forName("UTF-8")));
        OutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(inputStream, outputStream);
        String outputText = outputStream.toString();
        assertTrue(outputText.equals(inputText));
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertTrue(charset != null && charset.equalsIgnoreCase("UTF-8"));
    }

    @Test
    public void testGetCharsetFromContentTypeNull() {
        String contentType = "text/html";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        String validCharset = DataUtil.validateCharset(charset);
        assertTrue(validCharset != null && validCharset.equalsIgnoreCase("UTF-8"));

        String invalidCharset = "invalidCharset";
        String validatedCharset = DataUtil.validateCharset(invalidCharset);
        assertNull(validatedCharset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertTrue(boundary.length() == 32);
        for (int i = 0; i < boundary.length(); i++) {
            assertTrue(Character.isAlphabetic(boundary.charAt(i)) || Character.isDigit(boundary.charAt(i)) || boundary.charAt(i) == '-');
        }
    }

    @Test
    public void testDetectCharsetFromBomUtf32() throws IOException {
        ByteBuffer byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream(new byte[]{0x00, 0x00, (byte) 0xFE, (byte) 0xFF}));
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteData);
        assertTrue(bomCharset != null);
        assertTrue(bomCharset.charset.equalsIgnoreCase("UTF-32"));
        assertFalse(bomCharset.offset);
    }

    @Test
    public void testDetectCharsetFromBomUtf16() throws IOException {
        ByteBuffer byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream(new byte[]{0xFE, 0xFF}));
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteData);
        assertTrue(bomCharset != null);
        assertTrue(bomCharset.charset.equalsIgnoreCase("UTF-16"));
        assertFalse(bomCharset.offset);
    }

    @Test
    public void testDetectCharsetFromBomUtf8() throws IOException {
        ByteBuffer byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF}));
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteData);
        assertTrue(bomCharset != null);
        assertTrue(bomCharset.charset.equalsIgnoreCase("UTF-8"));
        assertTrue(bomCharset.offset);
    }

    @Test(expected = IOException.class)
    public void testDetectCharsetFromBomInvalid() throws IOException {
        ByteBuffer byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream(new byte[]{0x00, 0x00, 0x00, 0x00}));
        DataUtil.detectCharsetFromBom(byteData);
    }
}