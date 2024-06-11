package org.jsoup.helper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    public static String testFile = "path_to_test_file";
    public static String baseUri = "https://www.example.com";

    @Test
    public void testLoadFile() throws IOException {
        File file = new File(testFile);
        if (!file.exists()) {
            Assert.fail("Test file does not exist");
        }

        Document document = DataUtil.load(file, StandardCharsets.UTF_8.name(), baseUri);
        Assert.assertNotNull("Document is null", document);

        // Test document attributes
        Assert.assertTrue("Document base URI is incorrect", document.baseUri().equals(baseUri));
        Assert.assertTrue("Document title is incorrect", document.title().equals("Example Domain"));
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inputStream = new FileInputStream(testFile);

        Document document = DataUtil.load(inputStream, StandardCharsets.UTF_8.name(), baseUri);
        Assert.assertNotNull("Document is null", document);

        // Test document attributes
        Assert.assertTrue("Document base URI is incorrect", document.baseUri().equals(baseUri));
        Assert.assertTrue("Document title is incorrect", document.title().equals("Example Domain"));

        inputStream.close();
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        InputStream inputStream = new FileInputStream(testFile);

        Document document = DataUtil.load(inputStream, StandardCharsets.UTF_8.name(), baseUri, Parser.htmlParser());
        Assert.assertNotNull("Document is null", document);

        // Test document attributes
        Assert.assertTrue("Document base URI is incorrect", document.baseUri().equals(baseUri));
        Assert.assertTrue("Document title is incorrect", document.title().equals("Example Domain"));

        inputStream.close();
    }

    @Test
    public void testCrossStreams() throws IOException {
        String testData = "This is a test";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);

        String output = outputStream.toString();

        Assert.assertEquals("Output stream data is incorrect", testData, output);

        inputStream.close();
        outputStream.close();
    }

    @Test
    public void testParseByteDataWithCharset() {
        String testData = "<html><head><title>Test</title></head><body>Test</body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(testData.getBytes());

        Document document = DataUtil.parseByteData(byteData, StandardCharsets.UTF_8.name(), baseUri, Parser.htmlParser());

        Assert.assertNotNull("Document is null", document);
    }

    @Test
    public void testParseByteDataWithoutCharset() {
        String testData = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></head><body>Test</body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(testData.getBytes());

        Document document = DataUtil.parseByteData(byteData, null, baseUri, Parser.htmlParser());

        Assert.assertNotNull("Document is null", document);
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        int maxSize = 100;
        InputStream inputStream = new FileInputStream(testFile);

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);

        Assert.assertNotNull("Byte buffer is null", byteBuffer);
        Assert.assertTrue("Buffer size exceeds max size", byteBuffer.limit() <= maxSize);

        inputStream.close();
    }

    @Test
    public void testReadToByteBufferWithoutMaxSize() throws IOException {
        InputStream inputStream = new FileInputStream(testFile);

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);

        Assert.assertNotNull("Byte buffer is null", byteBuffer);

        inputStream.close();
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File file = new File(testFile);
        if (!file.exists()) {
            Assert.fail("Test file does not exist");
        }

        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);

        Assert.assertNotNull("Byte buffer is null", byteBuffer);
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();

        Assert.assertNotNull("Byte buffer is null", byteBuffer);
        Assert.assertEquals("Byte buffer size is not 0", 0, byteBuffer.limit());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";

        String charset = DataUtil.getCharsetFromContentType(contentType);

        Assert.assertNotNull("Charset is null", charset);
        Assert.assertEquals("Charset is incorrect", "UTF-8", charset);
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();

        Assert.assertNotNull("Mime boundary is null", mimeBoundary);
        Assert.assertEquals("Mime boundary length is incorrect", DataUtil.boundaryLength, mimeBoundary.length());
    }
}