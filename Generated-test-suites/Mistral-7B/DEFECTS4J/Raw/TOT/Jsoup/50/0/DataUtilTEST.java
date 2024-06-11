package org.jsoup.helper.test;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "This is a test file.";
        String charsetName = StandardCharsets.UTF_8.name();
        File testFile = new File("testFile.html");
        try (FileWriter fileWriter = new FileWriter(testFile)) {
            fileWriter.write(fileContent);
        }

        Document doc = DataUtil.load(testFile, charsetName, null);
        Assert.assertNotNull(doc);
        Assert.assertEquals(fileContent, doc.body().text());

        testFile.delete(); // clean up the test file
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String inputStreamContent = "This is a test input stream.";
        String charsetName = StandardCharsets.UTF_8.name();

        byte[] inputData = inputStreamContent.getBytes(charsetName);
        InputStream testInputStream = new ByteArrayInputStream(inputData);

        Document doc = DataUtil.load(testInputStream, charsetName, null);
        Assert.assertNotNull(doc);
        Assert.assertEquals(inputStreamContent, doc.body().text());
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String inputStreamContent = "This is a test input stream.";
        String invalidCharsetName = "invalidCharset";

        byte[] inputData = inputStreamContent.getBytes(StandardCharsets.UTF_8);
        InputStream testInputStream = new ByteArrayInputStream(inputData);

        try {
            Document doc = DataUtil.load(testInputStream, invalidCharsetName, null);
            Assert.fail("Expected IOException for invalid charset.");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testLoadInputStreamWithNullCharset() throws IOException {
        String inputStreamContent = "This is a test input stream.";

        byte[] inputData = inputStreamContent.getBytes(StandardCharsets.UTF_8);
        InputStream testInputStream = new ByteArrayInputStream(inputData);

        try {
            Document doc = DataUtil.load(testInputStream, null, null);
            doc.outputSettings().charset(StandardCharsets.UTF_8.name()); // set charset for assertion
            Assert.assertEquals(inputStreamContent, doc.body().text());
        } finally {
            testInputStream.close(); // close the input stream
        }
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputContent = "This is a test input.";
        String outputContent = "This is a test output.";

        InputStream inputStream = new ByteArrayInputStream(inputContent.getBytes(StandardCharsets.UTF_8));
        OutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);

        Assert.assertEquals(inputContent, new String(outputStream.toByteArray(), StandardCharsets.UTF_8));
    }

    @Test
    public void testParseByteDataWithDefaultCharset() throws IOException {
        String testContent = "This is a test document with a default charset.";
        String charsetName = null;
        String baseUri = "http://example.com/";
        Parser parser = Parser.htmlParser();

        ByteBuffer byteData = ByteBuffer.wrap(testContent.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        Assert.assertNotNull(doc);
        Assert.assertEquals(testContent, doc.body().text());
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() throws IOException {
        String testContent = "This is a test document with a specified charset.";
        String charsetName = "UTF-16";
        String baseUri = "http://example.com/";
        Parser parser = Parser.htmlParser();

        ByteBuffer byteData = ByteBuffer.wrap(testContent.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        Assert.assertNotNull(doc);
        Assert.assertEquals(testContent, doc.body().text());

        try {
            doc = DataUtil.parseByteData(byteData, "invalidCharset", baseUri, parser);
            Assert.fail("Expected IOException for invalid charset.");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String expectedCharset = "UTF-8";

        String charset = DataUtil.getCharsetFromContentType(contentType);

        Assert.assertEquals(expectedCharset, charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidCharset() {
        String contentType = "text/html; charset=invalidCharset";
        String expectedCharset = null;

        String charset = DataUtil.getCharsetFromContentType(contentType);

        Assert.assertEquals(expectedCharset, charset);
    }

    @Test
    public void testMimeBoundary() {
        String expectedBoundary = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890_-1234567890";

        String boundary = DataUtil.mimeBoundary();

        Assert.assertEquals(expectedBoundary, boundary);
    }
}