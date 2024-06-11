package org.jsoup.helper;

import static org.junit.Assert.*;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

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
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";
        Document doc = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithNullCharset() throws IOException {
        File file = new File("test.html");
        String baseUri = "http://test.com";
        DataUtil.load(file, null, baseUri);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String html = "<html><body></body></html>";
        InputStream in = new ByteArrayInputStream(html.getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";
        Document doc = DataUtil.load(in, charsetName, baseUri);
        assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithNullCharset() throws IOException {
        String html = "<html><body></body></html>";
        InputStream in = new ByteArrayInputStream(html.getBytes());
        String baseUri = "http://test.com";
        DataUtil.load(in, null, baseUri);
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String html = "<html><body></body></html>";
        InputStream in = new ByteArrayInputStream(html.getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.load(in, charsetName, baseUri, parser);
        assertNotNull(doc);
    }

    @Test
    public void testCrossStreams() throws IOException {
        File inputFile = new File("testInput.txt");
        File outputFile = new File("testOutput.txt");
        InputStream inputStream = new FileInputStream(inputFile);
        OutputStream outputStream = new FileOutputStream(outputFile);

        DataUtil.crossStreams(inputStream, outputStream);

        byte[] outputBytes = new byte[(int) outputFile.length()];
        FileInputStream outputStreamFile = new FileInputStream(outputFile);
        outputStreamFile.read(outputBytes);
        outputStreamFile.close();

        byte[] inputBytes = new byte[(int) inputFile.length()];
        FileInputStream inputStreamFile = new FileInputStream(inputFile);
        inputStreamFile.read(inputBytes);
        inputStreamFile.close();

        assertArrayEquals(outputBytes, inputBytes);

        outputFile.delete();
        inputFile.delete();
    }

    @Test
    public void testParseByteData() {
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";
        Parser parser = Parser.htmlParser();
        String html = "<html><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(html.getBytes());
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(doc);
    }

    @Test
    public void testParseByteDataWithInvalidCharset() {
        String charsetName = "InvalidCharset";
        String baseUri = "http://test.com";
        Parser parser = Parser.htmlParser();
        String html = "<html><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(html.getBytes());
        try {
            Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String html = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        byte[] expectedData = html.getBytes();
        byte[] actualData = new byte[byteData.limit()];
        byteData.get(actualData, 0, byteData.limit());
        assertArrayEquals(expectedData, actualData);
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        String html = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        int maxSize = 10;
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, maxSize);
        byte[] expectedData = html.substring(0, maxSize).getBytes();
        byte[] actualData = new byte[byteData.limit()];
        byteData.get(actualData, 0, byteData.limit());
        assertArrayEquals(expectedData, actualData);
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File file = new File("test.html");
        ByteBuffer byteData = DataUtil.readFileToByteBuffer(file);
        byte[] expectedData = new byte[(int) file.length()];
        FileInputStream fileInputStream = new FileInputStream(file);
        fileInputStream.read(expectedData);
        fileInputStream.close();
        byte[] actualData = new byte[byteData.limit()];
        byteData.get(actualData, 0, byteData.limit());
        assertArrayEquals(expectedData, actualData);
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteData = DataUtil.emptyByteBuffer();
        assertEquals(0, byteData.limit());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String expectedCharset = "UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(expectedCharset, charset);
    }

    @Test
    public void testGetCharsetFromContentTypeInvalidCharset() {
        String contentType = "text/html; charset=InvalidCharset";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(null, charset);
    }

    @Test
    public void testMimeBoundary() {
        String expectedBoundary = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890_-1234567890";
        String boundary = DataUtil.mimeBoundary();
        assertEquals(expectedBoundary, boundary);
    }
}