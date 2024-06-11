package org.jsoup.helper.test;

import org.junit.Assert;
import org.jsoup.helper.DataUtil;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        String filePath = "testData/sample.html";
        File inputFile = new File(filePath);
        Document doc = DataUtil.load(inputFile, null, null);
        Assert.assertNotNull(doc);
        // You can add assertions here to test the contents of the Document
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        String filePath = "testData/sample_utf16.html";
        File inputFile = new File(filePath);
        Document doc = DataUtil.load(inputFile, "UTF-16", null);
        Assert.assertNotNull(doc);
        // You can add assertions here to test the contents of the Document
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidCharset() throws IOException {
        String filePath = "testData/sample.html";
        File inputFile = new File(filePath);
        Document doc = DataUtil.load(inputFile, "invalidCharset", null);
        // The test should fail with an IOException since the charset is invalid
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String inputData = "<html><body>Test Data</body></html>";
        byte[] inputBytes = inputData.getBytes(StandardCharsets.UTF_8);
        InputStream inputStream = new ByteArrayInputStream(inputBytes);
        Document doc = DataUtil.load(inputStream, null, null);
        Assert.assertNotNull(doc);
        // You can add assertions here to test the contents of the Document
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        String inputData = "<html><body>Test Data</body></html>";
        byte[] inputBytes = inputData.getBytes(StandardCharsets.UTF_16LE);
        InputStream inputStream = new ByteArrayInputStream(inputBytes);
        Document doc = DataUtil.load(inputStream, "UTF-16", null);
        Assert.assertNotNull(doc);
        // You can add assertions here to test the contents of the Document
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String inputData = "<html><body>Test Data</body></html>";
        byte[] inputBytes = inputData.getBytes(StandardCharsets.UTF_8);
        InputStream inputStream = new ByteArrayInputStream(inputBytes);
        Document doc = DataUtil.load(inputStream, "invalidCharset", null);
        // The test should fail with an IOException since the charset is invalid
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String inputData = "<xml><data>Test Data</data></xml>";
        byte[] inputBytes = inputData.getBytes(StandardCharsets.UTF_8);
        InputStream inputStream = new ByteArrayInputStream(inputBytes);
        Document doc = DataUtil.load(inputStream, null, null, DataUtil.Parser.xmlParser());
        Assert.assertNotNull(doc);
        // You can add assertions here to test the contents of the Document
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputData = "Test Data";
        String outputData = "Output Data";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));
        OutputStream output = outputStream;

        DataUtil.crossStreams(inputStream, output);

        byte[] outputBytes = outputStream.toByteArray();
        Assert.assertEquals(outputData, new String(outputBytes, StandardCharsets.UTF_8));
    }
}