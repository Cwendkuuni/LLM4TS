package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {
    private Document document;

    @Before
    public void setup() {
        document = new Document("");
    }

    @After
    public void teardown() {
        document = null;
    }

    @Test
    public void testLoad_fileValidInput_loadsDocument() throws IOException {
        // Arrange
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";

        // Act
        Document loadedDocument = DataUtil.load(file, charsetName, baseUri);

        // Assert
        Assert.assertNotNull(loadedDocument);
    }

    @Test(expected = IOException.class)
    public void testLoad_fileInvalidInput_throwsIOException() throws IOException {
        // Arrange
        File file = new File("nonexistent.html");
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";

        // Act
        Document loadedDocument = DataUtil.load(file, charsetName, baseUri);

        // Assert (Exception thrown)
    }

    @Test
    public void testLoad_inputStreamValidInput_loadsDocument() throws IOException {
        // Arrange
        String inputData = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes());
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";

        // Act
        Document loadedDocument = DataUtil.load(inputStream, charsetName, baseUri);

        // Assert
        Assert.assertNotNull(loadedDocument);
    }

    @Test(expected = IOException.class)
    public void testLoad_inputStreamInvalidInput_throwsIOException() throws IOException {
        // Arrange
        InputStream inputStream = null;
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";

        // Act
        Document loadedDocument = DataUtil.load(inputStream, charsetName, baseUri);

        // Assert (Exception thrown)
    }

    @Test
    public void testLoad_inputStreamAndParserValidInput_loadsDocument() throws IOException {
        // Arrange
        String inputData = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes());
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        Parser parser = Parser.htmlParser();

        // Act
        Document loadedDocument = DataUtil.load(inputStream, charsetName, baseUri, parser);

        // Assert
        Assert.assertNotNull(loadedDocument);
    }

    @Test(expected = IOException.class)
    public void testLoad_inputStreamAndParserInvalidInput_throwsIOException() throws IOException {
        // Arrange
        InputStream inputStream = null;
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        Parser parser = Parser.htmlParser();

        // Act
        Document loadedDocument = DataUtil.load(inputStream, charsetName, baseUri, parser);

        // Assert (Exception thrown)
    }

    @Test
    public void testParseByteData_charsetNameNull_inputCharsetDetected() {
        // Arrange
        Charset charset = Charset.forName("UTF-8");
        ByteBuffer byteData = charset.encode("<html><head></head><body></body></html>");
        String charsetName = null;
        String baseUri = "https://www.example.com";
        Parser parser = Parser.htmlParser();

        // Act
        Document parsedDocument = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        // Assert
        Assert.assertNotNull(parsedDocument);
        Assert.assertEquals(charset, Charset.forName(parsedDocument.outputSettings().charset().name()));
    }

    @Test
    public void testParseByteData_charsetNameNotNull_inputCharsetUsed() {
        // Arrange
        Charset charset = Charset.forName("UTF-8");
        ByteBuffer byteData = charset.encode("<html><head></head><body></body></html>");
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        Parser parser = Parser.htmlParser();

        // Act
        Document parsedDocument = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        // Assert
        Assert.assertNotNull(parsedDocument);
        Assert.assertEquals(charset, Charset.forName(parsedDocument.outputSettings().charset().name()));
    }

    @Test
    public void testReadToByteBuffer_validInputStream_bytesReadToByteBuffer() throws IOException {
        // Arrange
        byte[] inputBytes = "<html><head></head><body></body></html>".getBytes();
        InputStream inputStream = new ByteArrayInputStream(inputBytes);

        // Act
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);

        // Assert
        Assert.assertEquals(inputBytes.length, byteBuffer.limit());
    }

    @Test
    public void testGetCharsetFromContentType_contentTypeValid_returnsCharset() {
        // Arrange
        String contentType = "text/html; charset=UTF-8";

        // Act
        String charset = DataUtil.getCharsetFromContentType(contentType);

        // Assert
        Assert.assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentType_contentTypeInvalid_returnsNull() {
        // Arrange
        String contentType = "text/html;";

        // Act
        String charset = DataUtil.getCharsetFromContentType(contentType);

        // Assert
        Assert.assertNull(charset);
    }

    @Test
    public void testGetCharsetFromContentType_contentTypeNull_returnsNull() {
        // Arrange
        String contentType = null;

        // Act
        String charset = DataUtil.getCharsetFromContentType(contentType);

        // Assert
        Assert.assertNull(charset);
    }
}