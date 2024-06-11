
package org.jsoup.helper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

public class DataUtilTest {

    @Test
    public void testLoadWithFile() throws IOException {
        // Arrange
        File file = new File("test.html");

        // Act
        Document document = DataUtil.load(file, "UTF-8", "");

        // Assert
        Assert.assertNotNull(document);
        Assert.assertEquals("test.html", document.title());
    }

    @Test
    public void testLoadWithInputStream() throws IOException {
        // Arrange
        String html = "<html><head><title>Test HTML</title></head><body><h1>Hello, World!</h1></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes());

        // Act
        Document document = DataUtil.load(inputStream, "UTF-8", "");

        // Assert
        Assert.assertNotNull(document);
        Assert.assertEquals("Test HTML", document.title());
    }

    @Test
    public void testLoadWithParser() throws IOException {
        // Arrange
        String html = "<html><head><title>Test HTML</title></head><body><h1>Hello, World!</h1></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes());

        // Act
        Document document = DataUtil.load(inputStream, "UTF-8", "", Parser.htmlParser());

        // Assert
        Assert.assertNotNull(document);
        Assert.assertEquals("Test HTML", document.title());
    }

    @Test
    public void testCrossStreams() throws IOException {
        // Arrange
        String input = "Hello, World!";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Act
        DataUtil.crossStreams(inputStream, outputStream);

        // Assert
        Assert.assertEquals(input, outputStream.toString());
    }

    @Test
    public void testParseByteData() throws IOException {
        // Arrange
        String html = "<html><head><title>Test HTML</title></head><body><h1>Hello, World!</h1></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(html.getBytes());

        // Act
        Document document = DataUtil.parseByteData(byteData, "UTF-8", "", Parser.htmlParser());

        // Assert
        Assert.assertNotNull(document);
        Assert.assertEquals("Test HTML", document.title());
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        // Arrange
        String input = "Hello, World!";
        int maxSize = 5;
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());

        // Act
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);

        // Assert
        Assert.assertEquals(maxSize, byteBuffer.limit());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        // Arrange
        String input = "Hello, World!";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());

        // Act
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);

        // Assert
        Assert.assertEquals(input.length(), byteBuffer.limit());
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        // Arrange
        File file = new File("test.html");

        // Act
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);

        // Assert
        Assert.assertTrue(byteBuffer.limit() > 0);
    }

    @Test
    public void testEmptyByteBuffer() {
        // Arrange

        // Act
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();

        // Assert
        Assert.assertEquals(0, byteBuffer.limit());
    }

    @Test
    public void testGetCharsetFromContentType() {
        // Arrange
        String contentType = "text/html; charset=UTF-8";

        // Act
        String charset = DataUtil.getCharsetFromContentType(contentType);

        // Assert
        Assert.assertEquals("UTF-8", charset);
    }

    @Test
    public void testMimeBoundary() {
        // Arrange

        // Act
        String mimeBoundary = DataUtil.mimeBoundary();

        // Assert
        Assert.assertNotNull(mimeBoundary);
        Assert.assertEquals(DataUtil.boundaryLength, mimeBoundary.length());
    }

    @Test
    public void testDetectCharsetFromBom() {
        // Arrange
        ByteBuffer byteData = ByteBuffer.wrap(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});

        // Act
        String charset = DataUtil.detectCharsetFromBom(byteData, "UTF-8");

        // Assert
        Assert.assertEquals("UTF-8", charset);
    }
}
