package org.jsoup.helper.test;

import org.jsoup.HelperException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
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
    public void testLoadFileWithDefaultCharset() throws IOException {
        File file = new File("test.html"); // replace with a valid file
        Document doc = DataUtil.load(file, null);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException, HelperException {
        File file = new File("test.html"); // replace with a valid file containing "charset=UTF-8" in the header
        Document doc = DataUtil.load(file, "UTF-8");
        Assert.assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidCharset() throws IOException {
        File file = new File("test.html"); // replace with a valid file
        Document doc = null;
        try {
            doc = DataUtil.load(file, "invalidCharset");
        } catch (HelperException e) {
            // HelperException is expected when charset is not supported, so test the IOException caused by it
            throw new IOException(e.getMessage());
        }
        Assert.fail("IOException expected");
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        byte[] inputData = "test data".getBytes(); // replace with test data
        InputStream inputStream = new ByteArrayInputStream(inputData);
        Document doc = DataUtil.load(inputStream, null);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException, HelperException {
        byte[] inputData = "test data".getBytes("UTF-8"); // replace with test data
        InputStream inputStream = new ByteArrayInputStream(inputData);
        Document doc = DataUtil.load(inputStream, "UTF-8");
        Assert.assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithInvalidCharset() throws IOException, HelperException {
        byte[] inputData = "test data".getBytes(); // replace with test data
        InputStream inputStream = new ByteArrayInputStream(inputData);
        Document doc = null;
        try {
            doc = DataUtil.load(inputStream, "invalidCharset");
        } catch (HelperException e) {
            // HelperException is expected when charset is not supported, so test the IOException caused by it
            throw new IOException(e.getMessage());
        }
        Assert.fail("IOException expected");
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException, HelperException {
        byte[] inputData = "test data".getBytes(); // replace with test data
        InputStream inputStream = new ByteArrayInputStream(inputData);
        Document doc = DataUtil.load(inputStream, null, Parser.xmlParser());
        Assert.assertNotNull(doc);
    }

    @Test
    public void testCrossStreams() throws IOException {
        File inputFile = new File("testInput.txt");
        File outputFile = new File("testOutput.txt");
        InputStream inputStream = new FileInputStream(inputFile);
        OutputStream outputStream = new FileOutputStream(outputFile);

        DataUtil.crossStreams(inputStream, outputStream);

        // Check if the files have been written correctly
        byte[] inputData = new byte[inputFile.length()];
        inputStream.read(inputData);
        byte[] outputData = new byte[outputFile.length()];
        outputFile.getBytes(0, outputData);

        Assert.assertArrayEquals(inputData, outputData);

        inputStream.close();
        outputStream.close();
    }

    @Test
    public void testParseInputStreamWithDefaultCharset() throws IOException, HelperException {
        byte[] inputData = "test data".getBytes(); // replace with test data
        InputStream inputStream = new ByteArrayInputStream(inputData);
        Document doc = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
        Assert.assertNotNull(doc);
    }

    @Test
    public void testParseInputStreamWithSpecifiedCharset() throws IOException, HelperException {
        byte[] inputData = "test data".getBytes("UTF-8"); // replace with test data
        InputStream inputStream = new ByteArrayInputStream(inputData);
        Document doc = DataUtil.parseInputStream(inputStream, "UTF-8", null, Parser.htmlParser());
        Assert.assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testParseInputStreamWithInvalidCharset() throws IOException, HelperException {
        byte[] inputData = "test data".getBytes(); // replace with test data
        InputStream inputStream = new ByteArrayInputStream(inputData);
        Document doc = null;
        try {
            doc = DataUtil.parseInputStream(inputStream, "invalidCharset", null, Parser.htmlParser());
        } catch (HelperException e) {
            // HelperException is expected when charset is not supported, so test the IOException caused by it
            throw new IOException(e.getMessage());
        }
        Assert.fail("IOException expected");
    }

    @Test
    public void testParseInputStreamWithParser() throws IOException, HelperException {
        byte[] inputData = "test data".getBytes(); // replace with test data
        InputStream inputStream = new ByteArrayInputStream(inputData);
        Document doc = DataUtil.parseInputStream(inputStream, null, null, Parser.xmlParser());
        Assert.assertNotNull(doc);
    }
}