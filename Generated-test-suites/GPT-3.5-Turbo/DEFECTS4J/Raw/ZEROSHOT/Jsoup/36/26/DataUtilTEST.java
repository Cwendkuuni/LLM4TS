package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class DataUtilTest {

    @Test
    public void load_File_ValidInput_ReturnsDocument() throws IOException {
        // Arrange
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        // Act
        Document document = DataUtil.load(file, charsetName, baseUri);

        // Assert
        Assert.assertNotNull(document);
    }

    @Test
    public void load_InputStream_ValidInput_ReturnsDocument() throws IOException {
        // Arrange
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        // Act
        Document document = DataUtil.load(inputStream, charsetName, baseUri);

        // Assert
        Assert.assertNotNull(document);
    }

    @Test
    public void load_InputStreamAndParser_ValidInput_ReturnsDocument() throws IOException {
        // Arrange
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        // Act
        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);

        // Assert
        Assert.assertNotNull(document);
    }

    @Test
    public void parseByteData_ValidInput_ReturnsDocument() {
        // Arrange
        ByteBuffer byteData = ByteBuffer.wrap("<html><body><h1>Hello, World!</h1></body></html>".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        // Act
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        // Assert
        Assert.assertNotNull(document);
    }

    @Test
    public void readToByteBuffer_InputStreamAndMaxSize_ValidInput_ReturnsByteBuffer() throws IOException {
        // Arrange
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        int maxSize = 1024;

        // Act
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);

        // Assert
        Assert.assertNotNull(byteBuffer);
    }

    @Test
    public void readToByteBuffer_InputStream_ValidInput_ReturnsByteBuffer() throws IOException {
        // Arrange
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());

        // Act
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);

        // Assert
        Assert.assertNotNull(byteBuffer);
    }

    @Test
    public void getCharsetFromContentType_ValidInput_ReturnsCharset() {
        // Arrange
        String contentType = "text/html; charset=UTF-8";

        // Act
        String charset = DataUtil.getCharsetFromContentType(contentType);

        // Assert
        Assert.assertEquals("UTF-8", charset);
    }
}