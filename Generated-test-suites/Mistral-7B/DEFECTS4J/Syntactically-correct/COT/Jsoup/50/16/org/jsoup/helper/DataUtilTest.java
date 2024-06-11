package org.jsoup.helper;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";
        Document document = DataUtil.load(file, charsetName, baseUri);
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoadFileInvalidCharset() throws IOException {
        File file = new File("test.html");
        String charsetName = "InvalidCharset";
        String baseUri = "http://test.com";
        try {
            DataUtil.load(file, charsetName, baseUri);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Invalid charset: InvalidCharset", e.getMessage());
        }
    }

    @Test
    public void testLoadFileNullBaseUri() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = null;
        Document document = DataUtil.load(file, charsetName, baseUri);
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoadFileEmptyBaseUri() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "";
        Document document = DataUtil.load(file, charsetName, baseUri);
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoadFileNullCharsetName() throws IOException {
        File file = new File("test.html");
        String charsetName = null;
        String baseUri = "http://test.com";
        Document document = DataUtil.load(file, charsetName, baseUri);
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String html = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamNullBaseUri() throws IOException {
        String html = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = null;
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamEmptyBaseUri() throws IOException {
        String html = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "";
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamNullCharsetName() throws IOException {
        String html = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        String charsetName = null;
        String baseUri = "http://test.com";
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        Assert.assertNotNull(document);
    }

    @Test
    public void testParseByteDataNullDocData() {
        ByteBuffer byteData = DataUtil.emptyByteBuffer();
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";
        Parser parser = DataUtil.Parser.htmlParser();
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        Assert.assertNull(document);
    }

    @Test
    public void testParseByteDataEmptyDocData() throws IOException {
        ByteBuffer byteData = DataUtil.emptyByteBuffer();
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";
        Parser parser = DataUtil.Parser.htmlParser();
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        Assert.assertNotNull(document);
        Assert.assertEquals(0, document.body().children().size());
    }

    @Test
    public void testParseByteDataWithBom() throws IOException {
        String html = "\ufeff<html><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(html.getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";
        Parser parser = DataUtil.Parser.htmlParser();
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        Assert.assertNotNull(document);
        Assert.assertEquals(0, document.body().children().size());
    }

    @Test
    public void testParseByteDataInvalidCharset() {
        String html = "<html><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(html.getBytes(StandardCharsets.UTF_8));
        String charsetName = "InvalidCharset";
        String baseUri = "http://test.com";
        Parser parser = DataUtil.Parser.htmlParser();
        Document document = null;
        try {
            document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Invalid charset: InvalidCharset", e.getMessage());
        }
        Assert.assertNull(document);
    }

    @Test
    public void testParseByteDataNullCharsetName() {
        String html = "<html><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(html.getBytes(StandardCharsets.UTF_8));
        String charsetName = null;
        String baseUri = "http://test.com";
        Parser parser = DataUtil.Parser.htmlParser();
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        Assert.assertNotNull(document);
    }

    @Test
    public void testReadToByteBufferNullInputStream() {
        InputStream inputStream = null;
        try {
            DataUtil.readToByteBuffer(inputStream);
            Assert.fail();
        } catch (NullPointerException e) {
            Assert.assertEquals("inputStream", e.getMessage());
        }
    }

    @Test
    public void testReadToByteBufferZeroMaxSize() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("test".getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 0);
        Assert.assertEquals(0, byteBuffer.limit());
    }

    @Test
    public void testReadToByteBufferValidMaxSize() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("test".getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 10);
        Assert.assertEquals(10, byteBuffer.limit());
        Assert.assertEquals("test".getBytes(StandardCharsets.UTF_8), byteBuffer.array());
    }

    @Test
    public void testReadToByteBufferNegativeMaxSize() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("test".getBytes(StandardCharsets.UTF_8));
        try {
            DataUtil.readToByteBuffer(inputStream, -1);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("maxSize must be 0 (unlimited) or larger", e.getMessage());
        }
    }

    @Test
    public void testReadFileToByteBufferNullFile() {
        File file = null;
        try {
            DataUtil.readFileToByteBuffer(file);
            Assert.fail();
        } catch (NullPointerException e) {
            Assert.assertEquals("file", e.getMessage());
        }
    }

    @Test
    public void testReadFileToByteBufferValidFile() throws IOException {
        String html = "<html><body></body></html>";
        File file = new File("test.html");
        writeFile(file, html);
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
        Assert.assertEquals(html.getBytes(StandardCharsets.UTF_8).length, byteBuffer.limit());
        Assert.assertEquals(html, Charset.forName("UTF-8").decode(byteBuffer).toString());
        file.delete();
    }

    @Test
    public void testReadFileToByteBufferEmptyFile() throws IOException {
        File file = new File("test.html");
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
        Assert.assertEquals(0, byteBuffer.limit());
        file.delete();
    }

    @Test
    public void testGetCharsetFromContentTypeNullContentType() {
        String contentType = null;
        Assert.assertNull(DataUtil.getCharsetFromContentType(contentType));
    }

    @Test
    public void testGetCharsetFromContentTypeInvalidContentType() {
        String contentType = "text/plain; charset=InvalidCharset";
        Assert.assertNull(DataUtil.getCharsetFromContentType(contentType));
    }

    @Test
    public void testGetCharsetFromContentTypeValidCharset() {
        String contentType = "text/html; charset=UTF-8";
        Assert.assertEquals("UTF-8", DataUtil.getCharsetFromContentType(contentType));
    }

    @Test
    public void testGetCharsetFromContentTypeUppercaseCharset() {
        String contentType = "text/html; charset=UTF-8";
        Assert.assertEquals("UTF-8", DataUtil.getCharsetFromContentType(contentType.toUpperCase(Locale.ENGLISH)));
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        Assert.assertTrue(boundary.length() == DataUtil.boundaryLength);
        for (int i = 0; i < DataUtil.boundaryLength; i++) {
            Assert.assertTrue(Character.isAlphabetic(boundary.charAt(i)) || Character.isDigit(boundary.charAt(i)));
        }
    }

    private void writeFile(File file, String content) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(content.getBytes(StandardCharsets.UTF_8));
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(outputStream.toByteArray());
        fileWriter.close();
    }
}