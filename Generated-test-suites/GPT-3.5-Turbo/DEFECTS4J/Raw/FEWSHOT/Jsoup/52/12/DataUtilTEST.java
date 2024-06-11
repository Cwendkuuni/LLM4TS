package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(file, charsetName, baseUri);
        assertEquals(baseUri, doc.baseUri());
        assertNotNull(doc.html());
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream in = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(in, charsetName, baseUri);
        assertEquals(baseUri, doc.baseUri());
        assertNotNull(doc.html());
    }

    @Test
    public void testLoadInputStreamParser() throws IOException {
        InputStream in = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.load(in, charsetName, baseUri, parser);
        assertEquals(baseUri, doc.baseUri());
        assertNotNull(doc.html());
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream in = getClass().getResourceAsStream("test.html");
        OutputStream out = new ByteArrayOutputStream();
        DataUtil.crossStreams(in, out);
        assertNotNull(out.toString());
    }

    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.wrap("<html><body><h1>Test</h1></body></html>".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertEquals(baseUri, doc.baseUri());
        assertNotNull(doc.html());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream in = getClass().getResourceAsStream("test.html");
        int maxSize = 0;
        ByteBuffer byteData = DataUtil.readToByteBuffer(in, maxSize);
        assertNotNull(byteData);
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        InputStream in = getClass().getResourceAsStream("test.html");
        int maxSize = 1024;
        ByteBuffer byteData = DataUtil.readToByteBuffer(in, maxSize);
        assertNotNull(byteData);
    }

    @Test
    public void testReadToByteBufferFromFile() throws IOException {
        File file = new File("test.html");
        ByteBuffer byteData = DataUtil.readFileToByteBuffer(file);
        assertNotNull(byteData);
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteData = DataUtil.emptyByteBuffer();
        assertNotNull(byteData);
        assertEquals(0, byteData.capacity());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        assertNotNull(mimeBoundary);
        assertEquals(DataUtil.boundaryLength, mimeBoundary.length());
    }

    @Test
    public void testDetectCharsetFromBom() {
        String charsetName = "UTF-8";
        ByteBuffer byteData = ByteBuffer.wrap("\uFEFF<html><body><h1>Test</h1></body></html>".getBytes());
        charsetName = DataUtil.detectCharsetFromBom(byteData, charsetName);
        assertEquals("UTF-16", charsetName);
    }

}