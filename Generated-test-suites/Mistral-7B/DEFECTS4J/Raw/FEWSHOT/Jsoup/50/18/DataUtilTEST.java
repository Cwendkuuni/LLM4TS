package org.jsoup.helper;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotEquals;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {
    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, null);
        // assertions on the loaded document go here
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException, UnsupportedEncodingException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        Document doc = DataUtil.load(file, charsetName);
        // assertions on the loaded document go here
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String text = "This is a test.";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(text.getBytes());
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Document doc = DataUtil.load(inputStream, null);
        // assertions on the loaded document go here
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException, UnsupportedEncodingException {
        String text = "This is a test.";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(text.getBytes(StandardCharsets.UTF_8.name()));
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Document doc = DataUtil.load(inputStream, StandardCharsets.UTF_8.name());
        // assertions on the loaded document go here
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException, UnsupportedEncodingException {
        String text = "This is a test.";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(text.getBytes(StandardCharsets.UTF_8.name()));
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Parser parser = Parser.xmlParser();
        Document doc = DataUtil.load(inputStream, StandardCharsets.UTF_8.name(), parser);
        // assertions on the loaded document go here
    }

    @Test
    public void testCrossStreams() throws IOException {
        File file = new File("test.html");
        File outputFile = new File("output.html");
        InputStream inputStream = new FileInputStream(file);
        OutputStream outputStream = new FileOutputStream(outputFile);

        DataUtil.crossStreams(inputStream, outputStream);

        Document doc = DataUtil.load(outputFile, null);
        // assertions on the loaded document go here

        inputStream.close();
        outputStream.close();
    }

    @Test
    public void testParseByteData() {
        ByteBuffer byteData = DataUtil.emptyByteBuffer();
        String charsetName = "UTF-8";
        String baseUri = "";
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNull(doc);

        byteData = DataUtil.readFileToByteBuffer(new File("test.html"));
        doc = DataUtil.parseByteData(byteData, null, "", parser);
        // assertions on the loaded document go here

        charsetName = "UTF-16";
        byteData.rewind();
        doc = DataUtil.parseByteData(byteData, charsetName, "", parser);
        // assertions on the loaded document go here

        byteData.rewind();
        byteData.put("UTF-8".getBytes()); // set charset in the buffer
        doc = DataUtil.parseByteData(byteData, null, "", parser);
        // assertions on the loaded document go here

        // test error cases
        byteData = DataUtil.emptyByteBuffer();
        doc = DataUtil.parseByteData(byteData, "UTF-8", "", parser);
        assertNull(doc);

        byteData = DataUtil.readFileToByteBuffer(new File("test.html"));
        byteData.flip(); // consume all bytes
        doc = DataUtil.parseByteData(byteData, null, "", parser);
        assertNull(doc);

        byteData = DataUtil.emptyByteBuffer();
        byteData.put("UTF-8".getBytes()); // set charset in the buffer
        doc = DataUtil.parseByteData(byteData, null, "", parser);
        assertNull(doc);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        File file = new File("test.html");
        InputStream inputStream = new FileInputStream(file);
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        // assertions on the read byte buffer go here

        inputStream.close();
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        File file = new File("test.html");
        InputStream inputStream = new FileInputStream(file);
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 1024);
        // assertions on the read byte buffer go here

        inputStream.close();
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File file = new File("test.html");
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
        // assertions on the read byte buffer go here
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);

        contentType = "text/html; charset=UTF-16";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-16", charset);

        contentType = null;
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertFalse(boundary.isEmpty());
        assertEquals(boundary.length(), 32);
        // more assertions on the generated boundary string go here
    }
}