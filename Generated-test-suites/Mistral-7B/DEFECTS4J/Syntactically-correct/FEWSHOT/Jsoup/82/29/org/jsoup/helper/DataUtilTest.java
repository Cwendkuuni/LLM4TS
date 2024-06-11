package org.jsoup.helper;

import static org.junit.Assert.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String filePath = "test.html"; // Replace this with the actual path to your test file
        String charsetName = StandardCharsets.UTF_8.name();
        String baseUri = "http://example.com";
        Document document = DataUtil.load(new File(filePath), charsetName, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body><p>Test</p></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        String charsetName = StandardCharsets.UTF_8.name();
        String baseUri = "http://example.com";
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testParseInputStream() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body><p>Test</p></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        String charsetName = StandardCharsets.UTF_8.name();
        String baseUri = "http://example.com";
        Document document = DataUtil.parseInputStream(inputStream, charsetName, baseUri, Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testParseInputStreamWithCustomParser() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body><p>Test</p></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        String charsetName = StandardCharsets.UTF_8.name();
        String baseUri = "http://example.com";
        Parser parser = Parser.xmlParser(); // Use a different parser
        Document document = DataUtil.parseInputStream(inputStream, charsetName, baseUri, parser);
        assertNotNull(document);
    }

    @Test
    public void testCrossStreams() throws IOException {
        String source = "Source data.";
        String target = "Target data.";
        String charsetName = StandardCharsets.UTF_8.name();

        // Create input and output streams
        ByteArrayInputStream inputStream = new ByteArrayInputStream(source.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Cross streams
        DataUtil.crossStreams(inputStream, outputStream);

        // Check if the data has been transferred correctly
        byte[] expectedOutput = target.getBytes(StandardCharsets.UTF_8);
        byte[] actualOutput = outputStream.toByteArray();
        assertArrayEquals(expectedOutput, actualOutput);

        // Close streams
        inputStream.close();
        outputStream.close();
    }

    @Test
    public void testParseInputStreamWithBomAndUtf8Charset() throws IOException {
        String html = "\uFEFF<html><head><meta charset=\"UTF-8\"></head><body><p>Test</p></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        String charsetName = StandardCharsets.UTF_8.name();
        String baseUri = "http://example.com";
        Document document = DataUtil.parseInputStream(inputStream, charsetName, baseUri, Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testParseInputStreamWithBomAndInvalidCharset() throws IOException {
        String html = "\uFEFF<html><head><meta charset=\"InvalidCharset\"></head><body><p>Test</p></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        String charsetName = "InvalidCharset";
        String baseUri = "http://example.com";
        Document document = null;
        try {
            document = DataUtil.parseInputStream(inputStream, charsetName, baseUri, Parser.htmlParser());
        } catch (IOException e) {
            // Expected exception
        }
        assertNull(document);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body><p>Test</p></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 0);
        // You can add more assertions here to verify the contents of the ByteBuffer
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String expectedCharset = "UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(expectedCharset, charset);
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        String validatedCharset = DataUtil.validateCharset(charset);
        assertEquals(charset, validatedCharset);
    }

    @Test
    public void testValidateCharsetWithInvalidCharset() {
        String charset = "InvalidCharset";
        String validatedCharset = DataUtil.validateCharset(charset);
        assertNull(validatedCharset);
    }

    @Test
    public void testMimeBoundary() {
        String expectedMimeBoundary = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890_-1234567890";
        String mimeBoundary = DataUtil.mimeBoundary();
        assertEquals(expectedMimeBoundary, mimeBoundary);
    }
}