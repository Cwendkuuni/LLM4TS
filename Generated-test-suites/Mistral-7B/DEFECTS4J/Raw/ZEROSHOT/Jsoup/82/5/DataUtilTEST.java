package org.jsoup.helper;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    @Test
    public void testLoadFileWithValidCharset() throws IOException {
        String filePath = "path/to/your/file.html";
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(new File(filePath), charsetName, baseUri);
        Assert.assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidCharset() throws IOException {
        String filePath = "path/to/your/file.html";
        String charsetName = "InvalidCharset";
        String baseUri = "http://example.com";
        Document doc = null;
        try {
            doc = DataUtil.load(new File(filePath), charsetName, baseUri);
        } catch (IOException e) {
            // Expected
        }
        Assert.assertNull(doc);
    }

    @Test
    public void testLoadInputStreamWithValidCharset() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        File file = new File("path/to/your/file.html");
        FileInputStream inputStream = new FileInputStream(file);
        Document doc = DataUtil.load(inputStream, charsetName, baseUri);
        Assert.assertNotNull(doc);
        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String charsetName = "InvalidCharset";
        String baseUri = "http://example.com";
        File file = new File("path/to/your/file.html");
        FileInputStream inputStream = new FileInputStream(file);
        Document doc = null;
        try {
            doc = DataUtil.load(inputStream, charsetName, baseUri);
        } catch (IOException e) {
            // Expected
        }
        Assert.assertNull(doc);
        inputStream.close();
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        File file = new File("path/to/your/file.html");
        FileInputStream inputStream = new FileInputStream(file);
        Parser parser = DataUtil.parser().xmlParser();
        Document doc = DataUtil.load(inputStream, charsetName, baseUri, parser);
        Assert.assertNotNull(doc);
        inputStream.close();
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputFilePath = "path/to/your/input.html";
        String outputFilePath = "path/to/your/output.html";

        File inputFile = new File(inputFilePath);
        File outputFile = new File(outputFilePath);
        FileInputStream inputStream = new FileInputStream(inputFile);
        FileOutputStream outputStream = new FileOutputStream(outputFile);

        DataUtil.crossStreams(inputStream, outputStream);
        Assert.assertTrue(outputFile.length() > 0);

        inputStream.close();
        outputStream.close();
    }

    @Test
    public void testParseInputStream() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        String testContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head><meta charset=\"UTF-8\"></meta></head><body><h1>Hello World</h1></body></html>";
        ByteBuffer byteBuffer = StandardCharsets.UTF_8.encode(testContent);
        InputStream inputStream = new ByteArrayInputStream(testContent.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.parseInputStream(inputStream, charsetName, baseUri);
        Assert.assertNotNull(doc);

        Element titleElement = doc.title();
        Assert.assertNotNull(titleElement);
        Assert.assertEquals("Hello World", titleElement.text());

        inputStream.close();
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String inputFilePath = "path/to/your/file.html";
        int maxSize = 1024;
        FileInputStream inputStream = new FileInputStream(new File(inputFilePath));

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);
        Assert.assertNotNull(byteBuffer);

        inputStream.close();
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String expectedCharset = "UTF-8";

        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals(expectedCharset, charset);
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-16";
        String expectedCharset = "UTF-16";

        String validatedCharset = DataUtil.validateCharset(charset);
        Assert.assertEquals(expectedCharset, validatedCharset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        int length = boundary.length();

        Assert.assertTrue(length > 0);
    }

    @Test
    public void testDetectCharsetFromBom() {
        String bomContent = "EF BBBF";
        ByteBuffer byteData = StandardCharsets.UTF_8.decode(bomContent);

        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteData);
        String expectedCharset = "UTF-8";

        Assert.assertNotNull(bomCharset);
        Assert.assertEquals(expectedCharset, bomCharset.charset);
        Assert.assertEquals(false, bomCharset.offset);
    }
}