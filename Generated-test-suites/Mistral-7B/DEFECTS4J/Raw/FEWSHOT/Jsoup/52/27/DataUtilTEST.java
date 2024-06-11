package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Random;

public class DataUtilTest {

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        File file = new File("testFile.html");
        Document document = DataUtil.load(file, null, null);
        assertNotNull(document);
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        File file = new File("testFile.html");
        Document document = DataUtil.load(file, "UTF-8", null);
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String htmlContent = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes());
        Document document = DataUtil.load(inputStream, null, null);
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        String htmlContent = "<?xml version='1.0' encoding='UTF-8'?>\n<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes());
        Document document = DataUtil.load(inputStream, "UTF-8", null);
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithSpecifiedParser() throws IOException {
        String htmlContent = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes());
        Document document = DataUtil.load(inputStream, null, null, Parser.xmlParser());
        assertNotNull(document);
    }

    @Test
    public void testCrossStreams() throws IOException {
        File inputFile = new File("testInputFile.html");
        File outputFile = new File("testOutputFile.html");
        DataUtil.crossStreams(new FileInputStream(inputFile), new FileOutputStream(outputFile));
        assertTrue(outputFile.length() > 0);
    }

    @Test
    public void testParseByteDataWithDefaultCharset() {
        String htmlContent = "<html><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(htmlContent.getBytes());
        Document document = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() {
        String htmlContent = "<?xml version='1.0' encoding='UTF-8'?>\n<html><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(htmlContent.getBytes());
        Document document = DataUtil.parseByteData(byteData, "UTF-8", null, Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);

        String contentType2 = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType2);
        assertNull(charset);
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        String validCharset = DataUtil.validateCharset(charset);
        assertEquals(charset, validCharset);

        String invalidCharset = "invalidCharset";
        validCharset = DataUtil.validateCharset(invalidCharset);
        assertNull(validCharset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertNotNull(boundary);
        assertTrue(boundary.length() == 32);
    }
}