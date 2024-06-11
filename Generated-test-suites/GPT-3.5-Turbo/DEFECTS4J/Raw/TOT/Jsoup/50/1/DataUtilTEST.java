
package org.jsoup.helper;

import org.junit.Test;
import org.junit.Assert;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class DataUtilTest {

    private static final String BASE_URI = "https://example.com";

    @Test
    public void testLoad_file_charset_baseUri() throws Exception {
        File file = new File("test.html");
        file.createNewFile();

        // Writing sample data to the file
        String html = "<html><head><title>Test HTML</title></head><body><h1>Hello, World!</h1></body></html>";
        byte[] htmlBytes = html.getBytes();
        FileOutputStream outputStream = new FileOutputStream(file);
        outputStream.write(htmlBytes);
        outputStream.close();

        Document document = DataUtil.load(file, "UTF-8", BASE_URI);

        Assert.assertNotNull(document);
        Assert.assertEquals("Test HTML", document.title());

        file.delete();
    }

    @Test
    public void testLoad_inputStream_charset_baseUri() throws Exception {
        String html = "<html><head><title>Test HTML</title></head><body><h1>Hello, World!</h1></body></html>";
        byte[] htmlBytes = html.getBytes();
        InputStream inputStream = new ByteArrayInputStream(htmlBytes);

        Document document = DataUtil.load(inputStream, "UTF-8", BASE_URI);

        Assert.assertNotNull(document);
        Assert.assertEquals("Test HTML", document.title());
    }

    @Test
    public void testLoad_inputStream_charset_baseUri_parser() throws Exception {
        String html = "<html><head><title>Test HTML</title></head><body><h1>Hello, World!</h1></body></html>";
        byte[] htmlBytes = html.getBytes();
        InputStream inputStream = new ByteArrayInputStream(htmlBytes);

        Document document = DataUtil.load(inputStream, "UTF-8", BASE_URI, Parser.htmlParser());

        Assert.assertNotNull(document);
        Assert.assertEquals("Test HTML", document.title());
    }

    @Test
    public void testCrossStreams() throws Exception {
        String testData = "This is a test string.";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);

        Assert.assertEquals(testData, outputStream.toString());
    }

    @Test
    public void testParseByteData() throws Exception {
        String html = "<html><head><title>Test HTML</title></head><body><h1>Hello, World!</h1></body></html>";
        byte[] htmlBytes = html.getBytes();
        ByteBuffer byteBuffer = ByteBuffer.wrap(htmlBytes);

        Document document = DataUtil.parseByteData(byteBuffer, "UTF-8", BASE_URI, Parser.htmlParser());

        Assert.assertNotNull(document);
        Assert.assertEquals("Test HTML", document.title());
    }

    @Test
    public void testReadToByteBuffer_withMaxSize() throws Exception {
        String testData = "This is a test string.";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 10);

        byte[] actualData = Arrays.copyOfRange(byteBuffer.array(), byteBuffer.arrayOffset(), byteBuffer.limit());
        Assert.assertArrayEquals("This is a ".getBytes(), actualData);
    }

    @Test
    public void testReadToByteBuffer() throws Exception {
        String testData = "This is a test string.";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);

        Assert.assertEquals(testData, new String(byteBuffer.array()));
    }

    @Test
    public void testReadFileToByteBuffer() throws Exception {
        File file = new File("test.txt");
        file.createNewFile();

        // Writing sample data to the file
        String testData = "This is a test string.";
        byte[] testBytes = testData.getBytes();
        FileOutputStream outputStream = new FileOutputStream(file);
        outputStream.write(testBytes);
        outputStream.close();

        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);

        byte[] actualData = Arrays.copyOfRange(byteBuffer.array(), byteBuffer.arrayOffset(), byteBuffer.limit());
        Assert.assertArrayEquals(testBytes, actualData);

        file.delete();
    }

    @Test
    public void testEmptyByteBuffer() throws Exception {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();

        Assert.assertEquals(0, byteBuffer.capacity());
        Assert.assertEquals(0, byteBuffer.limit());
    }

    @Test
    public void testGetCharsetFromContentType() throws Exception {
        String contentType1 = "text/html; charset=UTF-8";
        String contentType2 = "text/html; charset=ISO-8859-1";
       
        String charset1 = DataUtil.getCharsetFromContentType(contentType1);
        String charset2 = DataUtil.getCharsetFromContentType(contentType2);
       
        Assert.assertEquals("UTF-8", charset1);
        Assert.assertEquals("ISO-8859-1", charset2);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();

        Assert.assertNotNull(boundary);
        Assert.assertEquals(32, boundary.length());
    }
}

