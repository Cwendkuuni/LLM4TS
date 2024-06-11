package org.jsoup.helper;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {
    @Test
    public void testLoadFile() throws IOException {
        String content = "This is a test file";
        File file = new File("testFile.html");
        FileUtils.writeStringToFile(file, content, "UTF-8");
        Document doc = DataUtil.load(file, null, null);
        assertEquals("This is a test file", doc.body().text());
        FileUtils.deleteQuietly(file);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String content = "This is a test file";
        InputStream input = new ByteArrayInputStream(content.getBytes());
        Document doc = DataUtil.load(input, null, null);
        assertEquals("This is a test file", doc.body().text());
    }

    @Test
    public void testLoadInputStreamWithCharset() throws IOException {
        String content = "This is a test file";
        String charset = "UTF-8";
        InputStream input = new ByteArrayInputStream(content.getBytes(charset));
        Document doc = DataUtil.load(input, charset, null);
        assertEquals("This is a test file", doc.body().text());
    }

    @Test
    public void testLoadInputStreamWithBaseUri() throws IOException {
        String content = "<html><head><base href=\"/test\"></head><body><p>This is a test file</p></body></html>";
        InputStream input = new ByteArrayInputStream(content.getBytes());
        Document doc = DataUtil.load(input, null, "/test");
        assertEquals("This is a test file", doc.body().text());
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String content = "<html><head><base href=\"/test\"></head><body><p>This is a test file</p></body></html>";
        InputStream input = new ByteArrayInputStream(content.getBytes());
        Parser parser = Parser.xmlParser();
        Document doc = DataUtil.load(input, null, null, parser);
        assertEquals("This is a test file", doc.body().text());
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputData = "This is a test file";
        String outputData = "";
        InputStream input = new ByteArrayInputStream(inputData.getBytes());
        OutputStream output = new ByteArrayOutputStream();
        DataUtil.crossStreams(input, output);
        outputData = output.toString();
        assertEquals(inputData, outputData);
    }

    @Test
    public void testParseInputStream() throws IOException {
        String content = "This is a test file";
        InputStream input = new ByteArrayInputStream(content.getBytes());
        Document doc = DataUtil.parseInputStream(input, null, null, Parser.htmlParser());
        assertEquals("This is a test file", doc.body().text());
    }

    @Test
    public void testParseInputStreamWithCharset() throws IOException {
        String content = "This is a test file";
        String charset = "UTF-8";
        InputStream input = new ByteArrayInputStream(content.getBytes(charset));
        Document doc = DataUtil.parseInputStream(input, charset, null, Parser.htmlParser());
        assertEquals("This is a test file", doc.body().text());
    }

    @Test
    public void testParseInputStreamWithBaseUri() throws IOException {
        String content = "<html><head><base href=\"/test\"></head><body><p>This is a test file</p></body></html>";
        InputStream input = new ByteArrayInputStream(content.getBytes());
        Document doc = DataUtil.parseInputStream(input, null, "/test", Parser.htmlParser());
        assertEquals("This is a test file", doc.body().text());
    }

    @Test
    public void testParseInputStreamWithParser() throws IOException {
        String content = "<html><head><base href=\"/test\"></head><body><p>This is a test file</p></body></html>";
        InputStream input = new ByteArrayInputStream(content.getBytes());
        Parser parser = Parser.xmlParser();
        Document doc = DataUtil.parseInputStream(input, null, null, parser);
        assertEquals("This is a test file", doc.body().text());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String content = "This is a test file";
        InputStream input = new ByteArrayInputStream(content.getBytes());
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(input);
        byte[] expectedBytes = content.getBytes();
        for (int i = 0; i < expectedBytes.length; i++) {
            assertEquals(expectedBytes[i], byteBuffer.get(i));
        }
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        String content = "This is a test file";
        InputStream input = new ByteArrayInputStream(content.getBytes());
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(input, 10);
        assertEquals(10, byteBuffer.position());
    }

    @Test(expected = IOException.class)
    public void testReadToByteBufferWithNegativeMaxSize() throws IOException {
        String content = "This is a test file";
        InputStream input = new ByteArrayInputStream(content.getBytes());
        DataUtil.readToByteBuffer(input, -1);
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        String content = "This is a test file";
        File file = new File("testFile.html");
        FileUtils.writeStringToFile(file, content, "UTF-8");
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
        byte[] expectedBytes = content.getBytes();
        for (int i = 0; i < expectedBytes.length; i++) {
            assertEquals(expectedBytes[i], byteBuffer.get(i));
        }
        FileUtils.deleteQuietly(file);
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        assertEquals(0, byteBuffer.limit());
        assertEquals(0, byteBuffer.position());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String expectedCharset = "UTF-8";
        assertEquals(expectedCharset, DataUtil.getCharsetFromContentType(contentType));

        contentType = "text/html";
        assertNull(DataUtil.getCharsetFromContentType(contentType));
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        assertEquals(charset, DataUtil.validateCharset(charset));

        charset = "gb2312";
        charset = DataUtil.validateCharset(charset);
        assertNull(charset);

        charset = "invalidCharset";
        assertNull(DataUtil.validateCharset(charset));
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertNotNull(boundary);
        assertFalse(boundary.isEmpty());
    }

    @Test
    public void testDetectCharsetFromBom() {
        String testData = "EF BB GF FE UTF-8 aa bb cc dd";
        ByteBuffer byteData = ByteBuffer.wrap(testData.getBytes());
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteData);
        assertEquals("UTF-8", bomCharset.charset);
        assertEquals(0, bomCharset.offset);
    }

    @Test
    public void testDetectCharsetFromBomUnknown() {
        String testData = "EF BB GF FE unknown aa bb cc dd";
        ByteBuffer byteData = ByteBuffer.wrap(testData.getBytes());
        assertNull(DataUtil.detectCharsetFromBom(byteData));
    }
}