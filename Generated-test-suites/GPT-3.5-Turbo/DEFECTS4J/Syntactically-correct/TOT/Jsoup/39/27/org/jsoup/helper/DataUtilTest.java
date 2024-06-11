package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import static org.junit.Assert.*;

public class DataUtilTest {

    // Common test variables
    private File testFile;
    private String charsetName;
    private String baseUri;

    @Before
    public void setUp() {
        // Set up common test variables
        testFile = new File("test.html");
        charsetName = "UTF-8";
        baseUri = "http://example.com";
    }

    @After
    public void tearDown() {
        // Clean up test file
        if (testFile.exists()) {
            testFile.delete();
        }
    }

    @Test
    public void testLoadWithFile_WhenValidFileAndCharset_ThenReturnDocument() throws IOException {
        // Arrange
        String html = "<html><body><h1>Test</h1></body></html>";
        FileUtils.writeStringToFile(testFile, html, charsetName);

        // Act
        Document document = DataUtil.load(testFile, charsetName, baseUri);

        // Assert
        assertNotNull(document);
        assertEquals(html, document.html());
    }

    @Test(expected = IOException.class)
    public void testLoadWithFile_WhenInvalidFile_ThenThrowIOException() throws IOException {
        // Arrange: Invalid file path

        // Act
        Document document = DataUtil.load(new File("invalid.html"), charsetName, baseUri);

        // Assert: IOException is thrown
    }

    @Test
    public void testLoadWithInputStream_WhenValidInputStreamAndCharset_ThenReturnDocument() throws IOException {
        // Arrange
        String html = "<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = IOUtils.toInputStream(html, charsetName);

        // Act
        Document document = DataUtil.load(inputStream, charsetName, baseUri);

        // Assert
        assertNotNull(document);
        assertEquals(html, document.html());
    }

    @Test(expected = IOException.class)
    public void testLoadWithInputStream_WhenInvalidInputStream_ThenThrowIOException() throws IOException {
        // Arrange: Invalid input stream

        // Act
        Document document = DataUtil.load(null, charsetName, baseUri);

        // Assert: IOException is thrown
    }

    @Test
    public void testLoadWithInputStreamAndParser_WhenValidInputStreamAndCharset_ThenReturnDocument() throws IOException {
        // Arrange
        String html = "<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = IOUtils.toInputStream(html, charsetName);
        Parser parser = Parser.htmlParser();

        // Act
        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);

        // Assert
        assertNotNull(document);
        assertEquals(html, document.html());
    }

    @Test
    public void testLoadWithInputStreamAndParser_WhenValidInputStreamAndCharset_ThenReturnDocumentWithDifferentParser() throws IOException {
        // Arrange
        String xml = "<root><element>Test</element></root>";
        InputStream inputStream = IOUtils.toInputStream(xml, charsetName);
        Parser parser = Parser.xmlParser();

        // Act
        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);

        // Assert
        assertNotNull(document);
        assertEquals(xml, document.outerHtml());
    }

    @Test(expected = IOException.class)
    public void testLoadWithInputStreamAndParser_WhenInvalidInputStream_ThenThrowIOException() throws IOException {
        // Arrange: Invalid input stream

        // Act
        Document document = DataUtil.load(null, charsetName, baseUri, Parser.htmlParser());

        // Assert: IOException is thrown
    }

    @Test
    public void testParseByteData_WhenNoCharset_ThenParseWithDefaultCharset() {
        // Arrange
        ByteBuffer byteData = ByteBuffer.wrap("<html><body><h1>Test</h1></body></html>".getBytes());
        String expectedCharset = "UTF-8";

        // Act
        Document document = DataUtil.parseByteData(byteData, null, baseUri, Parser.htmlParser());

        // Assert
        assertNotNull(document);
        assertEquals(expectedCharset, document.outputSettings().charset().name());
    }

    @Test
    public void testParseByteData_WhenValidCharset_ThenParseWithGivenCharset() {
        // Arrange
        ByteBuffer byteData = ByteBuffer.wrap("<html><body><h1>Test</h1></body></html>".getBytes());
        String givenCharset = "ISO-8859-1";

        // Act
        Document document = DataUtil.parseByteData(byteData, givenCharset, baseUri, Parser.htmlParser());

        // Assert
        assertNotNull(document);
        assertEquals(givenCharset, document.outputSettings().charset().name());
    }

    @Test
    public void testReadToByteBuffer_WhenValidInputStreamAndMaxSize_ThenReturnByteBuffer() throws IOException {
        // Arrange
        String html = "<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = IOUtils.toInputStream(html, charsetName);
        int maxSize = 100;

        // Act
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);

        // Assert
        assertNotNull(byteBuffer);
        assertEquals(html, new String(byteBuffer.array()));
    }

    @Test
    public void testReadToByteBuffer_WhenValidInputStreamAndMaxSize_ThenReturnByteBufferWithMaxSize() throws IOException {
        // Arrange
        String html = "<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = IOUtils.toInputStream(html, charsetName);
        int maxSize = 10;

        // Act
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);

        // Assert
        assertNotNull(byteBuffer);
        assertEquals(maxSize, byteBuffer.limit());
    }

    @Test
    public void testReadToByteBuffer_WhenValidInputStreamAndNoMaxSize_ThenReturnByteBuffer() throws IOException {
        // Arrange
        String html = "<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = IOUtils.toInputStream(html, charsetName);

        // Act
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);

        // Assert
        assertNotNull(byteBuffer);
        assertEquals(html, new String(byteBuffer.array()));
    }

    @Test
    public void testGetCharsetFromContentType_WhenValidContentType_ThenReturnCharset() {
        // Arrange
        String contentType = "text/html; charset=utf-8";
        String expectedCharset = "UTF-8";

        // Act
        String charset = DataUtil.getCharsetFromContentType(contentType);

        // Assert
        assertNotNull(charset);
        assertEquals(expectedCharset, charset);
    }

    @Test
    public void testGetCharsetFromContentType_WhenNoCharset_ThenReturnNull() {
        // Arrange
        String contentType = "text/html";

        // Act
        String charset = DataUtil.getCharsetFromContentType(contentType);

        // Assert
        assertNull(charset);
    }

    @Test
    public void testGetCharsetFromContentType_WhenUnsupportedCharset_ThenReturnNull() {
        // Arrange
        String contentType = "text/html; charset=invalid";

        // Act
        String charset = DataUtil.getCharsetFromContentType(contentType);

        // Assert
        assertNull(charset);
    }

    @Test
    public void testGetCharsetFromContentType_WhenCharsetWithQuotes_ThenReturnCharsetWithoutQuotes() {
        // Arrange
        String contentType = "text/html; charset=\"UTF-8\"";
        String expectedCharset = "UTF-8";

        // Act
        String charset = DataUtil.getCharsetFromContentType(contentType);

        // Assert
        assertNotNull(charset);
        assertEquals(expectedCharset, charset);
    }

    @Test
    public void testGetCharsetFromContentType_WhenCharsetWithSingleQuotes_ThenReturnCharsetWithoutQuotes() {
        // Arrange
        String contentType = "text/html; charset='UTF-8'";
        String expectedCharset = "UTF-8";

        // Act
        String charset = DataUtil.getCharsetFromContentType(contentType);

        // Assert
        assertNotNull(charset);
        assertEquals(expectedCharset, charset);
    }

}