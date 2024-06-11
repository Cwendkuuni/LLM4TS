package org.jsoup.helper.test;

import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Random;

public class DataUtilTest {

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        File file = new File("test.html"); // Replace with the path to your test file
        String baseUri = "http://example.com";
        Document document = DataUtil.load(file, null, baseUri);
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        File file = new File("test.html"); // Replace with the path to your test file
        String charsetName = "UTF-16";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(file, charsetName, baseUri);
        Assert.assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidCharset() throws IOException {
        File file = new File("test.html"); // Replace with the path to your test file
        String charsetName = "InvalidCharset";
        String baseUri = "http://example.com";
        Document document = null;
        try {
            document = DataUtil.load(file, charsetName, baseUri);
        } catch (IllegalArgumentException e) {
            fail("Expected IOException, but got " + e.getClass().getName());
        }
        fail("Expected IOException, but test passed");
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String testData = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        String baseUri = "http://example.com";
        Document document = DataUtil.load(inputStream, null, baseUri, Parser.htmlParser());
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        String testData = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes("UTF-16"));
        String charsetName = "UTF-16";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(inputStream, charsetName, baseUri, Parser.htmlParser());
        Assert.assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String testData = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        String charsetName = "InvalidCharset";
        String baseUri = "http://example.com";
        Document document = null;
        try {
            document = DataUtil.load(inputStream, charsetName, baseUri, Parser.htmlParser());
        } catch (IllegalArgumentException e) {
            fail("Expected IOException, but got " + e.getClass().getName());
        }
        fail("Expected IOException, but test passed");
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String testData = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(inputStream, charsetName, baseUri, Parser.xmlParser());
        Assert.assertNotNull(document);
    }

    @Test
    public void testCrossStreams() throws IOException {
        File inputFile = new File("test.in"); // Replace with the path to your test input file
        File outputFile = new File("test.out"); // Replace with the path to your test output file
        InputStream inputStream = new FileInputStream(inputFile);
        OutputStream outputStream = new FileOutputStream(outputFile);

        DataUtil.crossStreams(inputStream, outputStream);

        byte[] expectedOutput = new byte[inputFile.length()];
        FileInputStream expectedInputStream = new FileInputStream(inputFile);
        expectedInputStream.read(expectedOutput);
        expectedInputStream.close();

        byte[] output = new byte[outputFile.length()];
        FileInputStream outputInputStream = new FileInputStream(outputFile);
        outputInputStream.read(output);
        outputInputStream.close();

        Assert.assertArrayEquals(expectedOutput, output);
    }

    @Test
    public void testParseByteData() throws IOException {
        String testData = "<html><body></body></html>";
        ByteBuffer byteData = ByteBuffer.allocate(testData.length() + 1);
        byteData.put(testData.getBytes());
        byteData.put((byte) 0xFFFE); // UTF-16 BOM
        Document document = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        Assert.assertNotNull(document);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String testData = "This is a test.";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        byte[] expected = testData.getBytes();
        byte[] actual = new byte[byteBuffer.limit()];
        byteBuffer.get(actual);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        String testData = "This is a test.";
        File file = new File("test.txt");
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(testData);
        fileWriter.close();
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
        byte[] expected = testData.getBytes();
        byte[] actual = new byte[byteBuffer.limit()];
        byteBuffer.get(actual);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        Assert.assertEquals(0, byteBuffer.limit());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidCharset() {
        String contentType = "text/html; charset=InvalidCharset";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertNull(charset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        Assert.assertTrue(boundary.length() > 0);
        Assert.assertTrue(boundary.matches("[^-_0-9a-zA-Z]+"));
    }
}