package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Test class for DataUtil.
 */
public class DataUtilTest {

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        File file = new File("test.html"); // Replace with valid file path
        Document doc = DataUtil.load(file, null, null);
        assertNotNull(doc);
    }

    @Test
    public void testLoadFileWithCustomCharset() throws IOException {
        File file = new File("test.html"); // Replace with valid file path
        Document doc = DataUtil.load(file, "UTF-16", null);
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String testData = "test data";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        Document doc = DataUtil.load(inputStream, null, null);
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithCustomCharset() throws IOException {
        String testData = "test data";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes("UTF-16"));
        Document doc = DataUtil.load(inputStream, "UTF-16", null);
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithCustomParser() throws IOException {
        String testData = "test data";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        Document doc = DataUtil.load(inputStream, null, null, DataUtil.parser().xmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testCrossStreams() throws IOException {
        File inputFile = new File("input.txt");
        File outputFile = new File("output.txt");

        InputStream inputStream = new FileInputStream(inputFile);
        OutputStream outputStream = new FileOutputStream(outputFile);

        DataUtil.crossStreams(inputStream, outputStream);

        assertEquals(inputFile.length(), outputFile.length());

        inputStream.close();
        outputStream.close();
    }

    @Test(expected = IOException.class)
    public void testParseByteDataWithInvalidCharset() {
        String testData = "test data";
        ByteBuffer byteData = ByteBuffer.wrap(testData.getBytes());
        DataUtil.parseByteData(byteData, " invalid_charset", null, Parser.htmlParser());
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        String testData = "test data";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 10);

        assertEquals(10, byteBuffer.limit());
        byteBuffer.flip();
        byte[] data = new byte[byteBuffer.remaining()];
        byteBuffer.get(data);
        assertEquals(testData.substring(0, 10), new String(data, Charset.forName("UTF-8")));

        inputStream.close();
    }

    @Test
    public void testReadToByteBufferWithoutMaxSize() throws IOException {
        String testData = "test data";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);

        assertEquals(testData.length(), byteBuffer.limit());
        byteBuffer.flip();
        byte[] data = new byte[byteBuffer.remaining()];
        byteBuffer.get(data);
        assertEquals(testData, new String(data, Charset.forName("UTF-8")));

        inputStream.close();
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File file = new File("test.html"); // Replace with valid file path
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);

        assertEquals(file.length(), byteBuffer.limit());
        byteBuffer.flip();
        byte[] data = new byte[byteBuffer.remaining()];
        byteBuffer.get(data);

        Document doc = DataUtil.parser().htmlParser().parseInput(new String(data, Charset.forName("UTF-8")), null);
        // Add assertions based on expected content of the file

        byteBuffer.close();
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();

        assertEquals(0, byteBuffer.limit());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertFalse(boundary.isEmpty());
        assertEquals(32, boundary.length());
    }
}