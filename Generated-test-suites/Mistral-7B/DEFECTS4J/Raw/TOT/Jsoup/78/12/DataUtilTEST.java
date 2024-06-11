package org.jsoup.helper.test;

import org.junit.Assert;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    @org.junit.Test
    public void testLoadFileWithValidCharset() throws IOException {
        String charsetName = "UTF-8";
        String filePath = "path/to/valid/file.html";
        Document document = DataUtil.load(new File(filePath), charsetName, "");
        Assert.assertNotNull(document);
        Assert.assertNotNull(document.body());
    }

    @org.junit.Test
    public void testLoadFileWithInvalidCharset() throws IOException {
        String charsetName = "INVALID_CHARSET";
        String filePath = "path/to/valid/file.html";
        try {
            Document document = DataUtil.load(new File(filePath), charsetName, "");
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            Assert.assertEquals("Error parsing file: Unsupported charset", e.getMessage());
        }
    }

    @org.junit.Test
    public void testLoadInputStreamWithValidCharset() throws IOException {
        String charsetName = "UTF-8";
        String content = "<html><head><meta charset='UTF-8'></head><body>Test</body></html>";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(inputStream, charsetName, "");
        Assert.assertNotNull(document);
        Assert.assertNotNull(document.body().text());
        Assert.assertEquals("Test", document.body().text());
    }

    @org.junit.Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String charsetName = "INVALID_CHARSET";
        String content = "<html><head><meta charset='UTF-8'></head><body>Test</body></html>";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
        try {
            Document document = DataUtil.load(inputStream, charsetName, "");
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            Assert.assertEquals("Error parsing input stream: Unsupported charset", e.getMessage());
        }
    }

    @org.junit.Test
    public void testParseInvalidInputStream() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{'I', 'n', 'v', 'a', 'l', 'i', 'd'});
        try {
            Document document = DataUtil.parseInputStream(inputStream, null, "", Parser.htmlParser());
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            Assert.assertEquals("Error parsing input stream: Premature end of data in input stream", e.getMessage());
        }
    }

    @org.junit.Test
    public void testCrossStreams() throws IOException {
        String inputFilePath = "path/to/input/file.txt";
        String outputFilePath = "path/to/output/file.txt";
        byte[] inputFileContent = "Input text".getBytes();

        File inputFile = new File(inputFilePath);
        File outputFile = new File(outputFilePath);

        try (InputStream inputStream = new FileInputStream(inputFile); OutputStream outputStream = new FileOutputStream(outputFile)) {
            DataUtil.crossStreams(inputStream, outputStream);
        }

        byte[] outputFileContent = new byte[outputFile.length()];
        try (InputStream outputStream = new FileInputStream(outputFile)) {
            outputStream.read(outputFileContent);
        }

        Assert.assertArrayEquals(inputFileContent, outputFileContent);
    }
}