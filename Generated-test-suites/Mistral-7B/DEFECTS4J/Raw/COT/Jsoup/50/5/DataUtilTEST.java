package org.jsoup.helper;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    private ByteArrayOutputStream byteArrayOutputStream;
    private ByteArrayInputStream byteArrayInputStream;
    private Document document;

    @Before
    public void setUp() {
        byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    }

    @Test
    public void testLoad_File_DefaultCharset() throws IOException {
        File file = new File("test.html"); // Replace with actual test file
        ByteBuffer byteData = DataUtil.readFileToByteBuffer(file);
        document = DataUtil.load(file, null, "/");
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoad_File_SpecifiedCharset() throws IOException {
        String charsetName = "UTF-16";
        File file = new File("test.html"); // Replace with actual test file
        ByteBuffer byteData = DataUtil.readFileToByteBuffer(file);
        document = DataUtil.load(file, charsetName, "/");
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoad_InputStream_DefaultCharset() throws IOException {
        String testData = "Test Data";
        byte[] inputData = testData.getBytes(StandardCharsets.UTF_8);
        InputStream in = new ByteArrayInputStream(inputData);
        document = DataUtil.load(in, null, "/");
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoad_InputStream_SpecifiedCharset() throws IOException {
        String testData = "Test Data";
        String charsetName = "UTF-16";
        byte[] inputData = testData.getBytes(StandardCharsets.UTF_8);
        InputStream in = new ByteArrayInputStream(inputData);
        document = DataUtil.load(in, charsetName, "/");
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoad_InputStream_Parser() throws IOException {
        String testData = "Test Data";
        String charsetName = "UTF-16";
        byte[] inputData = testData.getBytes(StandardCharsets.UTF_8);
        InputStream in = new ByteArrayInputStream(inputData);
        document = DataUtil.load(in, charsetName, "/", Parser.xmlParser());
        Assert.assertNotNull(document);
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        OutputStream out = new ByteArrayOutputStream();
        DataUtil.crossStreams(in, out);
        Assert.assertEquals("", out.toString());
    }

    @Test
    public void testParseByteData_DefaultCharset() throws IOException {
        String testData = "Test Data";
        byte[] inputData = testData.getBytes(StandardCharsets.UTF_8);
        ByteBuffer byteData = ByteBuffer.wrap(inputData);
        document = DataUtil.parseByteData(byteData, null, "/", Parser.htmlParser());
        Assert.assertNotNull(document);
    }

    @Test
    public void testParseByteData_SpecifiedCharset() throws IOException {
        String testData = "Test Data";
        String charsetName = "UTF-16";
        byte[] inputData = testData.getBytes(StandardCharsets.UTF_8);
        ByteBuffer byteData = ByteBuffer.wrap(inputData);
        document = DataUtil.parseByteData(byteData, charsetName, "/", Parser.htmlParser());
        Assert.assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testParseByteData_UnsupportedCharset() throws IOException {
        String testData = "Test Data";
        String charsetName = "ABCDEFGH";
        byte[] inputData = testData.getBytes(StandardCharsets.UTF_8);
        ByteBuffer byteData = ByteBuffer.wrap(inputData);
        document = DataUtil.parseByteData(byteData, charsetName, "/", Parser.htmlParser());
    }

    @Test
    public void testReadToByteBuffer_InputStream() throws IOException {
        String testData = "Test Data";
        byte[] inputData = testData.getBytes(StandardCharsets.UTF_8);
        InputStream in = new ByteArrayInputStream(inputData);
        ByteBuffer byteData = DataUtil.readToByteBuffer(in);
        Assert.assertNotNull(byteData);
    }

    @Test
    public void testReadToByteBuffer_InputSteam_MaxSize() throws IOException {
        String testData = "Test Data";
        byte[] inputData = testData.getBytes(StandardCharsets.UTF_8);
        InputStream in = new ByteArrayInputStream(inputData);
        ByteBuffer byteData = DataUtil.readToByteBuffer(in, 10);
        Assert.assertNotNull(byteData);
    }

    @Test
    public void testReadFileToByteBuffer_File() throws IOException {
        File file = new File("test.html"); // Replace with actual test file
        ByteBuffer byteData = DataUtil.readFileToByteBuffer(file);
        Assert.assertNotNull(byteData);
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        Assert.assertNull(byteBuffer.array());
        Assert.assertEquals(0, byteBuffer.limit());
    }

    @Test
    public void testGetCharsetFromContentType_SupportedCharset() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentType_UnsupportedCharset() {
        String contentType = "text/html; charset=ABCDEFGH";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertNull(charset);
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        Assert.assertEquals(32, mimeBoundary.length());
    }
}