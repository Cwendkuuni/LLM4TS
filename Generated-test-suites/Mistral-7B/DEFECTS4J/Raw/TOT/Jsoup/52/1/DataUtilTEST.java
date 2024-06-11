package org.jsoup.helper.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "This is a test file.";
        String charsetName = "UTF-8";
        File inputFile = new File("test.html");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStream fos = outputStream;

        // Write the file content to the file
        fos.write(fileContent.getBytes(StandardCharsets.UTF_8));
        fos.close();

        // Test the DataUtil.load() method with the file
        Document document = DataUtil.load(inputFile, charsetName, "");
        Assert.assertNotNull(document);
        Assert.assertEquals(fileContent, document.body().text());

        // Clean up the test file
        inputFile.delete();
    }

    @Test
    public void testLoadInvalidCharsetFile() throws IOException {
        String fileContent = "This is a test file.";
        String charsetName = "ISO-8859-1"; // Invalid charset for the file content
        File inputFile = new File("test.html");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStream fos = outputStream;

        // Write the file content to the file
        fos.write(fileContent.getBytes(StandardCharsets.UTF_8));
        fos.close();

        try {
            // Test the DataUtil.load() method with the invalid charset
            Document document = DataUtil.load(inputFile, charsetName, "");
            Assert.fail("Expected IOException: Invalid charset");
        } catch (IOException e) {
            // Expected
        }

        // Clean up the test file
        inputFile.delete();
    }

    @Test
    public void testLoadEmptyFile() throws IOException {
        File inputFile = new File("test.html");
        String charsetName = "UTF-8";

        // Test the DataUtil.load() method with an empty file
        Document document = DataUtil.load(inputFile, charsetName, "");
        Assert.assertNull(document);

        // Clean up the test file
        inputFile.delete();
    }

    @Test
    public void testLoadInputstream() throws IOException {
        String charsetName = "UTF-8";
        String fileContent = "This is a test file.";
        byte[] inputStreamContent = fileContent.getBytes(StandardCharsets.UTF_8);

        InputStream inputStream = new ByteArrayInputStream(inputStreamContent);

        // Test the DataUtil.load() method with the input stream
        Document document = DataUtil.load(inputStream, charsetName, "");
        Assert.assertNotNull(document);
        Assert.assertEquals(fileContent, document.body().text());
    }

    @Test
    public void testLoadInvalidCharsetInputstream() throws IOException {
        String charsetName = "ISO-8859-1"; // Invalid charset for the file content
        String fileContent = "This is a test file.";
        byte[] inputStreamContent = fileContent.getBytes(StandardCharsets.UTF_8);

        InputStream inputStream = new ByteArrayInputStream(inputStreamContent);

        try {
            // Test the DataUtil.load() method with the invalid charset
            Document document = DataUtil.load(inputStream, charsetName, "");
            Assert.fail("Expected IOException: Invalid charset");
        } catch (IOException e) {
            // Expected
        }

        inputStream.close();
    }

    @Test
    public void testParseByteDataWithDefaultCharset() {
        String xmlContent = "<xml version=\"1.0\" encoding=\"ISO-8859-1\">\n<root><data>Test</data></root>\n</xml>";
        ByteBuffer byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream(xmlContent.getBytes(StandardCharsets.UTF_8)));

        String charsetName = null;
        Document document = DataUtil.parseByteData(byteData, charsetName, "", Parser.xmlParser());
        Assert.assertNotNull(document);
        Assert.assertEquals("<root><data>Test</data></root>", document.toString());
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() {
        String xmlContent = "<xml version=\"1.0\" encoding=\"UTF-8\">\n<root><data>Test</data></root>\n</xml>";
        ByteBuffer byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream(xmlContent.getBytes(StandardCharsets.UTF_8)));

        String charsetName = "UTF-8";
        Document document = DataUtil.parseByteData(byteData, charsetName, "", Parser.xmlParser());
        Assert.assertNotNull(document);
        Assert.assertEquals("<root><data>Test</data></root>", document.toString());
    }

    @Test
    public void testParseByteDataWithInvalidCharset() {
        String xmlContent = "<xml version=\"1.0\" encoding=\"ISO-8859-1\">\n<root><data>Test</data></root>\n</xml>";
        ByteBuffer byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream(xmlContent.getBytes(StandardCharsets.UTF_8)));

        String charsetName = "UTF-16"; // Invalid charset for the file content
        Document document = null;

        try {
            document = DataUtil.parseByteData(byteData, charsetName, "", Parser.xmlParser());
            Assert.fail("Expected IllegalArgumentException: Unsupported charset");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        byteData.rewind();
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputContent = "This is a test.";
        String outputContent = "";

        InputStream inputStream = new ByteArrayInputStream(inputContent.getBytes(StandardCharsets.UTF_8));
        OutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);

        outputContent = outputStream.toString(StandardCharsets.UTF_8.name());
        Assert.assertEquals(inputContent, outputContent);

        inputStream.close();
        outputStream.close();
    }
}