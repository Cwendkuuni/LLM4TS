package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;

public class DataUtilTest {

    @Test
    public void testLoad_withFile() throws IOException {
        // Arrange
        File file = new File("test.html");
        
        // Act
        Document document = DataUtil.load(file, "UTF-8", "");

        // Assert
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoad_withInputStream() throws IOException {
        // Arrange
        InputStream inputStream = new ByteArrayInputStream("test".getBytes());

        // Act
        Document document = DataUtil.load(inputStream, "UTF-8", "");

        // Assert
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoad_withInputStreamAndParser() throws IOException {
        // Arrange
        InputStream inputStream = new ByteArrayInputStream("test".getBytes());
        Parser parser = Parser.htmlParser();

        // Act
        Document document = DataUtil.load(inputStream, "UTF-8", "", parser);

        // Assert
        Assert.assertNotNull(document);
    }

    @Test
    public void testParseByteData_withCharset() {
        // Arrange
        ByteBuffer byteBuffer = ByteBuffer.wrap("test".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "";
        Parser parser = Parser.htmlParser();

        // Act
        Document document = DataUtil.parseByteData(byteBuffer, charsetName, baseUri, parser);

        // Assert
        Assert.assertNotNull(document);
    }

    @Test
    public void testParseByteData_withoutCharset() {
        // Arrange
        ByteBuffer byteBuffer = ByteBuffer.wrap("test".getBytes());
        String charsetName = null;
        String baseUri = "";
        Parser parser = Parser.htmlParser();

        // Act
        Document document = DataUtil.parseByteData(byteBuffer, charsetName, baseUri, parser);

        // Assert
        Assert.assertNotNull(document);
    }

    @Test
    public void testReadToByteBuffer_withMaxSize() throws IOException {
        // Arrange
        InputStream inputStream = new ByteArrayInputStream("test".getBytes());
        int maxSize = 10;

        // Act
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);

        // Assert
        Assert.assertNotNull(byteBuffer);
    }

    @Test
    public void testReadToByteBuffer_withoutMaxSize() throws IOException {
        // Arrange
        InputStream inputStream = new ByteArrayInputStream("test".getBytes());

        // Act
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);

        // Assert
        Assert.assertNotNull(byteBuffer);
    }

    @Test
    public void testGetCharsetFromContentType_withCharset() {
        // Arrange
        String contentType = "text/html; charset=UTF-8";

        // Act
        String charset = DataUtil.getCharsetFromContentType(contentType);

        // Assert
        Assert.assertNotNull(charset);
        Assert.assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentType_withoutCharset() {
        // Arrange
        String contentType = "text/html";

        // Act
        String charset = DataUtil.getCharsetFromContentType(contentType);

        // Assert
        Assert.assertNull(charset);
    }

}