package org.jsoup.helper;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "This is a test file.";
        File testFile = new File("testFile.html");
        testFile.createNewFile();
        DataUtil.saveToFile(testFile.getAbsolutePath(), fileContent);

        Document document = DataUtil.load(testFile, "UTF-8", null);
        Assert.assertEquals(fileContent, document.body().text());
        testFile.delete();
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String testString = "This is a test string.";
        InputStream inputStream = new ByteArrayInputStream(testString.getBytes("UTF-8"));

        Document document = DataUtil.load(inputStream, "UTF-8", null);
        Assert.assertEquals(testString, document.body().text());
    }

    @Test
    public void testLoadInputStreamWithBaseUri() throws IOException {
        String testString = "This is a test string.";
        InputStream inputStream = new ByteArrayInputStream(testString.getBytes("UTF-8"));
        String baseUri = "https://example.com/";

        Document document = DataUtil.load(inputStream, "UTF-8", baseUri);
        Assert.assertEquals(testString, document.body().text());
    }

    @Test
    public void testParseInputStream() throws IOException {
        String testString = "This is a test string.";
        InputStream inputStream = new ByteArrayInputStream(testString.getBytes("UTF-8"));

        Document document = DataUtil.parseInputStream(inputStream, "UTF-8", null, Parser.htmlParser());
        Assert.assertEquals(testString, document.body().text());
    }

    @Test
    public void testCrossStreams() throws IOException {
        String testString = "This is a test string.";
        InputStream inputStream = new ByteArrayInputStream(testString.getBytes("UTF-8"));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);
        Assert.assertEquals(testString, outputStream.toString());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String testString = "This is a test string.";
        InputStream inputStream = new ByteArrayInputStream(testString.getBytes("UTF-8"));
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);

        byte[] expectedBytes = testString.getBytes("UTF-8");
        byte[] actualBytes = new byte[byteBuffer.limit()];
        byteBuffer.get(actualBytes);

        Assert.assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        String testString = "This is a test string.";
        InputStream inputStream = new ByteArrayInputStream(testString.getBytes("UTF-8"));
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 10);

        Assert.assertEquals(10, byteBuffer.limit());
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        String testString = "This is a test string.";
        File testFile = new File("testFile.html");
        testFile.createNewFile();
        DataUtil.saveToFile(testFile.getAbsolutePath(), testString);

        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(testFile);
        String expectedString = new String(testString.getBytes("UTF-8"), "UTF-8");
        String actualString = Charset.forName("UTF-8").decode(byteBuffer).toString();

        Assert.assertEquals(expectedString, actualString);
        testFile.delete();
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();

        Assert.assertEquals(0, byteBuffer.limit());
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
        String charset = "UTF-8";
        String invalidCharset = "UTF-8!";

        Assert.assertEquals(charset, DataUtil.validateCharset(charset));
        Assert.assertNull(DataUtil.validateCharset(invalidCharset));
    }

    @Test
    public void testMimeBoundary() {
        String expectedMimeBoundary = "---_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ01234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String mimeBoundary = DataUtil.mimeBoundary();

        Assert.assertEquals(expectedMimeBoundary, mimeBoundary);
    }
}