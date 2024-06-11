package org.jsoup.helper;

import static org.junit.Assert.*;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testLoadDocumentFromFileWithDefaultCharset() throws IOException {
        File inputFile = new File("input/testFile.html");
        Document document = DataUtil.load(inputFile, null, "");
        assertNotNull(document);
        assertEquals("UTF-8", document.outputSettings().charset());
    }

    @Test
    public void testLoadDocumentFromFileWithSpecifiedCharset() throws IOException {
        File inputFile = new File("input/testFile.html");
        Document document = DataUtil.load(inputFile, "UTF-16", "");
        assertNotNull(document);
        assertEquals("UTF-16", document.outputSettings().charset());
    }

    @Test
    public void testLoadDocumentFromInputStreamWithDefaultCharset() throws IOException {
        File inputFile = new File("input/testFile.html");
        InputStream inputStream = new FileInputStream(inputFile);
        Document document = DataUtil.load(inputStream, null, "");
        assertNotNull(document);
        assertEquals("UTF-8", document.outputSettings().charset());
        inputStream.close();
    }

    @Test
    public void testLoadDocumentFromInputStreamWithSpecifiedCharset() throws IOException {
        File inputFile = new File("input/testFile.html");
        InputStream inputStream = new FileInputStream(inputFile);
        Document document = DataUtil.load(inputStream, "UTF-16", "");
        assertNotNull(document);
        assertEquals("UTF-16", document.outputSettings().charset());
        inputStream.close();
    }

    @Test
    public void testLoadDocumentFromInputStreamWithParser() throws IOException {
        File inputFile = new File("input/testFile.xml");
        InputStream inputStream = new FileInputStream(inputFile);
        Document document = DataUtil.load(inputStream, null, "", Parser.xmlParser());
        assertNotNull(document);
        assertEquals("UTF-8", document.outputSettings().charset());
        inputStream.close();
    }

    @Test
    public void testDetectCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String detectedCharset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", detectedCharset);

        contentType = "text/html; charset=ISO-8859-1";
        detectedCharset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("ISO-8859-1", detectedCharset);

        contentType = "text/html";
        detectedCharset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(detectedCharset);
    }

    @Test
    public void testReadToByteBufferFromInputStream() throws IOException {
        File inputFile = new File("input/testFile.html");
        InputStream inputStream = new FileInputStream(inputFile);
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        int remaining = byteData.remaining();
        assertEquals(0, remaining);
        inputStream.close();
    }

    @Test
    public void testReadToByteBufferFromInputStreamWithMaxSize() throws IOException {
        File inputFile = new File("input/testFile.html");
        InputStream inputStream = new FileInputStream(inputFile);
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, 1024);
        int remaining = byteData.remaining();
        assertTrue(remaining > 0);
        inputStream.close();
    }

    @Test
    public void testParseByteDataWithDefaultCharset() {
        String docData = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></head><body>";
        ByteBuffer byteData = ByteBuffer.wrap(docData.getBytes("UTF-8"));
        Document document = DataUtil.parseByteData(byteData, null, "", Parser.htmlParser());
        assertNotNull(document);
        assertEquals("UTF-8", document.outputSettings().charset());
    }

    @Test
    public void testParseByteDataWithDetectedCharset() {
        String docData = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\"></head><body>";
        ByteBuffer byteData = ByteBuffer.wrap(docData.getBytes("UTF-8"));
        Document document = DataUtil.parseByteData(byteData, null, "", Parser.htmlParser());
        assertNotNull(document);
        assertEquals("ISO-8859-1", document.outputSettings().charset());
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() {
        String docData = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></head><body>";
        ByteBuffer byteData = ByteBuffer.wrap(docData.getBytes("UTF-8"));
        Document document = DataUtil.parseByteData(byteData, "UTF-16", "", Parser.htmlParser());
        assertNull(document);
    }

    @Test
    public void testParseByteDataWithBOM() {
        String docData = "\ufeff<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></head><body>";
        ByteBuffer byteData = ByteBuffer.wrap(docData.getBytes("UTF-8"));
        Document document = DataUtil.parseByteData(byteData, null, "", Parser.htmlParser());
        assertNotNull(document);
        assertEquals("UTF-8", document.outputSettings().charset());
        byteData.rewind();
        docData = Charset.forName("UTF-8").decode(byteData).toString();
        assertEquals(docData.substring(1), docData);
    }
}