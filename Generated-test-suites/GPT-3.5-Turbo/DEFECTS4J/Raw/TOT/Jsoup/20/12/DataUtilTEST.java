package org.jsoup.helper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.*;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DataUtilTest {

    @Test
    public void loadFile_ValidInput_ReturnsDocument() throws IOException {
        // Arrange
        File testFile = new File("test.html");
        BufferedWriter writer = new BufferedWriter(new FileWriter(testFile));
        writer.write("<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>");
        writer.close();

        // Act
        Document result = DataUtil.load(testFile, "UTF-8", "");

        // Assert
        assertNotNull(result);
        assertNotNull(result.title());
        assertNotNull(result.head());
        assertNotNull(result.body());
    }

    @Test(expected = IOException.class)
    public void loadFile_InvalidFile_ThrowsIOException() throws IOException {
        // Arrange
        File testFile = new File("nonexistent.html");

        // Act
        DataUtil.load(testFile, "UTF-8", "");
    }

    @Test
    public void loadInputStream_ValidInput_ReturnsDocument() throws IOException {
        // Arrange
        InputStream inputStream = new ByteArrayInputStream("<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>".getBytes());

        // Act
        Document result = DataUtil.load(inputStream, "UTF-8", "");

        // Assert
        assertNotNull(result);
        assertNotNull(result.title());
        assertNotNull(result.head());
        assertNotNull(result.body());
    }

    @Test(expected = IOException.class)
    public void loadInputStream_InvalidInputStream_ThrowsIOException() throws IOException {
        // Arrange
        InputStream inputStream = new FileInputStream(new File("nonexistent.html"));

        // Act
        DataUtil.load(inputStream, "UTF-8", "");
    }

    @Test
    public void loadInputStreamWithParser_ValidInput_ReturnsDocument() throws IOException {
        // Arrange
        InputStream inputStream = new ByteArrayInputStream("<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>".getBytes());

        // Act
        Document result = DataUtil.load(inputStream, "UTF-8", "", Parser.htmlParser());

        // Assert
        assertNotNull(result);
        assertNotNull(result.title());
        assertNotNull(result.head());
        assertNotNull(result.body());
    }

    @Test
    public void parseByteData_NullCharsetName_ReturnsDocument() {
        // Arrange
        ByteBuffer byteData = ByteBuffer.wrap("<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>".getBytes());

        // Act
        Document result = DataUtil.parseByteData(byteData, null, "", Parser.htmlParser());

        // Assert
        assertNotNull(result);
        assertNotNull(result.title());
        assertNotNull(result.head());
        assertNotNull(result.body());
    }

    @Test
    public void parseByteData_InvalidCharsetFromMeta_ReturnsDocument() {
        // Arrange
        ByteBuffer byteData = ByteBuffer.wrap("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html;charset=invalidcharset\"></head><body><h1>Hello World</h1></body></html>".getBytes());

        // Act
        Document result = DataUtil.parseByteData(byteData, "UTF-8", "", Parser.htmlParser());

        // Assert
        assertNotNull(result);
        assertNotNull(result.title());
        assertNotNull(result.head());
        assertNotNull(result.body());
    }

    @Test
    public void parseByteData_ValidCharsetName_ReturnsDocument() {
        // Arrange
        ByteBuffer byteData = ByteBuffer.wrap("<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>".getBytes());

        // Act
        Document result = DataUtil.parseByteData(byteData, "UTF-8", "", Parser.htmlParser());

        // Assert
        assertNotNull(result);
        assertNotNull(result.title());
        assertNotNull(result.head());
        assertNotNull(result.body());
    }

    @Test
    public void readToByteBuffer_ValidInputStream_ReturnsByteBuffer() throws IOException {
        // Arrange
        InputStream inputStream = new ByteArrayInputStream("<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>".getBytes());

        // Act
        ByteBuffer result = DataUtil.readToByteBuffer(inputStream);

        // Assert
        assertNotNull(result);
        assertEquals(85, result.capacity());
    }

    @Test
    public void getCharsetFromContentType_ValidContentType_ReturnsCharset() {
        // Arrange
        String contentType = "text/html; charset=UTF-8";

        // Act
        String result = DataUtil.getCharsetFromContentType(contentType);

        // Assert
        assertEquals("UTF-8", result);
    }

    @Test
    public void getCharsetFromContentType_InvalidContentType_ReturnsNull() {
        // Arrange
        String contentType = "text/html";

        // Act
        String result = DataUtil.getCharsetFromContentType(contentType);

        // Assert
        assertNull(result);
    }
}