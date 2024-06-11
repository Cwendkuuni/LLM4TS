package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Random;

/**
 * JUnit test file for DataUtil class.
 */
public class DataUtilTest {

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        File inputFile = new File("inputFile.html");
        Document doc = DataUtil.load(inputFile, null, null);
        assertNotNull(doc);
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        File inputFile = new File("inputFile.html");
        Document doc = DataUtil.load(inputFile, "UTF-16", null);
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String testData = "Test data...";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, null, null);
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        String testData = "Test data...";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, "UTF-16", null);
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String testData = "Test data...";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, null, null, Parser.xmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testCrossStreams() throws IOException {
        File inputFile = new File("inputFile.html");
        InputStream inputStream = new FileInputStream(inputFile);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(inputStream, outputStream);
        inputStream.close();
        outputStream.close();
    }

    @Test
    public void testParseByteDataWithDefaultCharset() throws IOException {
        String testData = "Test data...";
        ByteBuffer byteData = ByteBuffer.wrap(testData.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() throws IOException {
        String testData = "Test data...";
        ByteBuffer byteData = ByteBuffer.wrap(testData.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseByteData(byteData, "UTF-16", null, Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String testData = "Test data...";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        assertEquals(testData.length(), byteData.limit());
        inputStream.close();
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeNoCharset() {
        String contentType = "text/html";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertNotNull(boundary);
        assertNotNull(boundary.length() > 0);
    }
}