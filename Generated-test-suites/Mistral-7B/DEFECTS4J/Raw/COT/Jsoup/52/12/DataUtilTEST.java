package org.jsoup.helper;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import org.junit.Test;

/**
 * Test class for DataUtil.
 */
public class DataUtilTest {

    @Test
    public void testLoadFromFile() throws IOException {
        File inputFile = new File("test.html");
        Document document = DataUtil.load(inputFile, "UTF-8", "");
        assertNotNull(document);
    }

    @Test
    public void testLoadFromInputStreamWithDefaultCharset() throws IOException {
        String htmlContent = "<html><head><meta charset='UTF-16'></head><body>Test</body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(inputStream, "", "");
        assertNotNull(document);
    }

    @Test
    public void testLoadFromInputStreamWithSpecifiedCharset() throws IOException {
        String htmlContent = "<html><head><meta charset='UTF-16'></head><body>Test</body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(inputStream, "UTF-16", "");
        assertNotNull(document);
    }

    @Test
    public void testLoadFromInputStreamWithParser() throws IOException {
        String htmlContent = "<html><head><meta charset='UTF-16'></head><body>Test</body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(inputStream, "", "", Parser.xmlParser());
        assertNotNull(document);
    }

    @Test
    public void testCrossStreams() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream("Test".getBytes(StandardCharsets.UTF_8));
        DataUtil.crossStreams(inputStream, outputStream);
        byte[] output = outputStream.toByteArray();
        assertEquals("Test", new String(output, StandardCharsets.UTF_8));
    }

    @Test
    public void testParseByteDataWithDefaultCharset() {
        String htmlContent = "<html><head><meta charset='UTF-16'></head><body>Test</body></html>";
        byte[] inputData = htmlContent.getBytes(StandardCharsets.UTF_8);
        ByteBuffer byteBuffer = ByteBuffer.wrap(inputData);
        Document document = DataUtil.parseByteData(byteBuffer, "UTF-8", "", Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithDetectedCharset() {
        String htmlContent = "<html><head><meta charset='UTF-16'></head><body>Test</body></html>";
        byte[] inputData = htmlContent.getBytes(StandardCharsets.UTF_8);
        ByteBuffer byteBuffer = ByteBuffer.wrap(inputData);
        Document document = DataUtil.parseByteData(byteBuffer, "UTF-8", "", Parser.htmlParser());
        String charset = document.outputSettings().charset();
        assertEquals("UTF-16", charset);
    }

    @Test(expected = IOException.class)
    public void testLoadFromFileWithIOException() throws IOException {
        File inputFile = new File("test.html_not_exists");
        Document document = DataUtil.load(inputFile, "UTF-8", "");
        fail("Expected an IOException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadFromInputStreamWithInvalidCharset() {
        String htmlContent = "<html><head><meta charset='UTF-16'></head><body>Test</body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        try {
            Document document = DataUtil.load(inputStream, "InvalidCharset", "");
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertTrue(boundary.length() >= 32);
    }
}