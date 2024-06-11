package org.jsoup.helper;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html"); // replace with a valid file path
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(file, charsetName, baseUri);
        // add assertions here to test the loaded document
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String html = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes("UTF-8"));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        // add assertions here to test the loaded document
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String html = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes("UTF-8"));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.xmlParser(); // change parser as needed
        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
        // add assertions here to test the loaded document
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream in = new ByteArrayInputStream("test".getBytes());
        OutputStream out = new ByteArrayOutputStream();
        DataUtil.crossStreams(in, out);
        byte[] expectedOutput = "test".getBytes();
        assertArrayEquals(expectedOutput, out.toByteArray());
    }

    @Test
    public void testParseByteData() {
        ByteBuffer byteData = DataUtil.emptyByteBuffer();
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNull(document);

        byteData = ByteBuffer.wrap("<?xml version=\"1.0\" encoding=\"UTF-8\"?>".getBytes());
        charsetName = "UTF-8";
        document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        // add assertions here to test the loaded document

        byteData = ByteBuffer.wrap("<html><body></body></html>".getBytes("UTF-8"));
        charsetName = "UTF-8";
        document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        // add assertions here to test the loaded document

        byteData = ByteBuffer.wrap("<html><head><meta charset=\"ISO-8859-1\"></head><body></body></html>".getBytes("UTF-8"));
        charsetName = null;
        document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        // add assertions here to test the loaded document
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inStream = new ByteArrayInputStream("test".getBytes());
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inStream);
        byte[] expectedOutput = "test".getBytes();
        byteBuffer.rewind();
        assertArrayEquals(expectedOutput, byteBuffer.array());

        inStream = new ByteArrayInputStream(new byte[0]);
        byteBuffer = DataUtil.readToByteBuffer(inStream);
        assertEquals(0, byteBuffer.limit());

        inStream = new ByteArrayInputStream("test".getBytes("UTF-8"));
        byteBuffer = DataUtil.readToByteBuffer(inStream, 5);
        byte[] expectedOutputTruncated = "test".getBytes();
        byteBuffer.rewind();
        assertArrayEquals(expectedOutputTruncated, new byte[5]);
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File file = new File("test.html"); // replace with a valid file path
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
        // add assertions here to test the loaded data
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);

        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        int length = boundary.length();
        for (int i = 0; i < length; i++) {
            char c = boundary.charAt(i);
            assertTrue(Character.isAlphabetic(c) || Character.isDigit(c) || c == '-' || c == '_');
        }
    }
}