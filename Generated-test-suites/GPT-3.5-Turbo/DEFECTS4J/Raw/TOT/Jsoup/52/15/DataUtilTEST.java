package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

public class DataUtilTest {
    private File file;
    private String baseUri;
    private Parser parser;

    @Before
    public void setup() {
        file = new File("test.html");
        baseUri = "http://example.com";
        parser = Parser.htmlParser();
    }

    @Test
    public void load_file_ShouldLoadDocument() throws IOException {
        // Arrange
        String charsetName = "UTF-8";

        // Act
        Document document = DataUtil.load(file, charsetName, baseUri);

        // Assert
        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void load_inputStream_ShouldLoadDocument() throws IOException {
        // Arrange
        String charsetName = "UTF-8";
        String html = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));

        // Act
        Document document = DataUtil.load(inputStream, charsetName, baseUri);

        // Assert
        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void load_inputStreamWithParser_ShouldLoadDocument() throws IOException {
        // Arrange
        String charsetName = "UTF-8";
        String html = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));

        // Act
        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);

        // Assert
        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void crossStreams_ShouldCopyInputStreamToOutputStream() throws IOException {
        // Arrange
        String input = "This is test input.";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Act
        DataUtil.crossStreams(inputStream, outputStream);

        // Assert
        assertEquals(input, outputStream.toString(StandardCharsets.UTF_8));
    }

    @Test
    public void parseByteData_ShouldParseByteDataToDocument() {
        // Arrange
        String html = "<html><head></head><body></body></html>";
        String charsetName = "UTF-8";
        String input = "\uFEFF" + html; // Adding BOM to the input
        InputStream inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));

        // Act
        Document document = DataUtil.parseByteData(inputStream, charsetName, baseUri, parser);

        // Assert
        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void readToByteBuffer_ShouldReadInputStreamToByteBuffer() throws IOException {
        // Arrange
        String input = "This is test input.";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));

        // Act
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);

        // Assert
        assertNotNull(byteBuffer);
        assertEquals(input, new String(byteBuffer.array(), StandardCharsets.UTF_8));
    }

    @Test
    public void readFileToByteBuffer_ShouldReadFileToByteBuffer() throws IOException {
        // Arrange
        String input = "This is test input.";
        file.createNewFile();
        java.nio.file.Files.write(file.toPath(), input.getBytes());
        ByteBuffer expectedByteBuffer = ByteBuffer.wrap(input.getBytes());

        // Act
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);

        // Assert
        assertEquals(expectedByteBuffer, byteBuffer);
    }

    @Test
    public void emptyByteBuffer_ShouldReturnEmptyByteBuffer() {
        // Act
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();

        // Assert
        assertNotNull(byteBuffer);
        assertEquals(0, byteBuffer.capacity());
    }

    @Test
    public void getCharsetFromContentType_ShouldReturnCharsetFromContentType() {
        // Arrange
        String contentType = "text/html; charset=UTF-8";

        // Act
        String charset = DataUtil.getCharsetFromContentType(contentType);

        // Assert
        assertEquals("UTF-8", charset);
    }

    @Test
    public void getCharsetFromContentType_NotFound_ShouldReturnNull() {
        // Arrange
        String contentType = "text/html";

        // Act
        String charset = DataUtil.getCharsetFromContentType(contentType);

        // Assert
        assertNull(charset);
    }

    @Test
    public void mimeBoundary_ShouldCreateRandomMimeBoundary() {
        // Act
        String mimeBoundary = DataUtil.mimeBoundary();

        // Assert
        assertNotNull(mimeBoundary);
        assertEquals(DataUtil.boundaryLength, mimeBoundary.length());
        assertTrue(mimeBoundary.matches("[\\-\\_\\w]+"));
    }

    @Test
    public void detectCharsetFromBom_WithBom_ShouldDetectCharset() {
        // Arrange
        String charsetName = null;
        byte[] bom = new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF, 'A'};
        ByteBuffer byteBuffer = ByteBuffer.wrap(bom);

        // Act
        String detectedCharset = DataUtil.detectCharsetFromBom(byteBuffer, charsetName);

        // Assert
        assertEquals("UTF-8", detectedCharset);
    }

    @Test
    public void detectCharsetFromBom_NoBom_ShouldKeepOriginalCharset() {
        // Arrange
        String charsetName = "UTF-16";
        ByteBuffer byteBuffer = ByteBuffer.wrap("A".getBytes(StandardCharsets.UTF_16));

        // Act
        String detectedCharset = DataUtil.detectCharsetFromBom(byteBuffer, charsetName);

        // Assert
        assertEquals(charsetName, detectedCharset);
    }
}