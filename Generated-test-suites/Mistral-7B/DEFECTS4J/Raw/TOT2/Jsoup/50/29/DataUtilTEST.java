package org.jsoup.helper;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.helper.DataUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String fileName = "testFile.html";
        File file = new File(fileName);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document expectedDoc = parseFile("testFile.html");
        Document actualDoc = DataUtil.load(file, charsetName, baseUri);
        Assert.assertEquals(expectedDoc, actualDoc);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String fileContent = "test content";
        byte[] fileData = fileContent.getBytes();
        InputStream inputStream = new ByteArrayInputStream(fileData);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document expectedDoc = parseFile("testFile.html");
        Document actualDoc = DataUtil.load(inputStream, charsetName, baseUri, Parser.htmlParser());
        Assert.assertEquals(expectedDoc, actualDoc);
    }

    @Test
    public void testLoadInputStreamWithCharsetDetectedFromMetaTag() throws IOException {
        String fileContent = "meta charset=\"ISO-8859-1\"\n<html><body>Test</body></html>";
        byte[] fileData = fileContent.getBytes();
        InputStream inputStream = new ByteArrayInputStream(fileData);
        String baseUri = "http://example.com";
        Document expectedDoc = parseFile("testFile.html");
        Document actualDoc = DataUtil.load(inputStream, null, baseUri, Parser.htmlParser());
        Assert.assertEquals(expectedDoc, actualDoc);
    }

    @Test
    public void testLoadInputStreamWithUnsupportedCharsetInMetaTag() throws IOException {
        String fileContent = "meta charset=\"X-MAC-ISA-1\"\n<html><body>Test</body></html>";
        byte[] fileData = fileContent.getBytes();
        InputStream inputStream = new ByteArrayInputStream(fileData);
        String baseUri = "http://example.com";
        try {
            DataUtil.load(inputStream, null, baseUri, Parser.htmlParser());
            Assert.fail("Expected IllegalArgumentException due to unsupported charset.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String fileContent = "test content";
        byte[] fileData = fileContent.getBytes();
        InputStream inputStream = new ByteArrayInputStream(fileData);
        String baseUri = "http://example.com";
        Document expectedDoc = parseFile("testFile.html");
        Document actualDoc = DataUtil.load(inputStream, null, baseUri, Parser.htmlParser());
        Assert.assertEquals(expectedDoc, actualDoc);
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputFile = "inputFile.html";
        String outputFile = "outputFile.html";
        File input = new File(inputFile);
        File output = new File(outputFile);
        InputStream inputStream = new FileInputStream(input);
        OutputStream outputStream = new FileOutputStream(output);
        DataUtil.crossStreams(inputStream, outputStream);
        Document expectedDoc = parseFile("outputFile.html");
        Document actualDoc = DataUtil.load(new File("outputFile.html"), null, null);
        Assert.assertEquals(expectedDoc, actualDoc);
    }

    @Test
    public void testParseByteDataWithUTF8Charset() {
        String charsetName = "UTF-8";
        ByteBuffer byteData = DataUtil.emptyByteBuffer();
        byteData.put((byte) 0xEF);
        byteData.put((byte) 0xBB);
        byteData.put((byte) 0xFF);
        byteData.flip();
        Document expectedDoc = parseFile("testFile.html");
        Document actualDoc = DataUtil.parseByteData(byteData, charsetName, null, Parser.htmlParser());
        Assert.assertEquals(expectedDoc, actualDoc);
    }

    @Test
    public void testParseByteDataWithOtherCharset() {
        String charsetName = "ISO-8859-1";
        ByteBuffer byteData = DataUtil.emptyByteBuffer();
        byteData.put((byte) 0xE2);
        byteData.put((byte) 0xA0);
        byteData.put((byte) 0x83);
        byteData.flip();
        Document expectedDoc = parseFile("testFile.html");
        try {
            Document actualDoc = DataUtil.parseByteData(byteData, charsetName, null, Parser.htmlParser());
            Assert.fail("Expected IllegalArgumentException due to unsupported charset.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String content = "test content";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes());
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        byte[] expectedBytes = content.getBytes();
        byte[] actualBytes = byteBuffer.array();
        Assert.assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        String fileName = "testFile.html";
        File file = new File(fileName);
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
        byte[] expectedBytes = Files.readAllBytes(file.toPath());
        byte[] actualBytes = byteBuffer.array();
        Assert.assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        Pattern boundaryPattern = Pattern.compile("--" + boundary);
        String boundaryTest = "--" + boundary + "--";
        Assert.assertTrue(boundaryPattern.matcher(boundaryTest).matches());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals(charset, "UTF-8");
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidCharset() {
        String contentType = "text/html; charset=X-MAC-ISA-1";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertNull(charset);
    }

    private Document parseFile(String fileName) throws IOException {
        File file = new File(fileName);
        Document doc = DataUtil.load(file, null, null);
        return doc;
    }

}