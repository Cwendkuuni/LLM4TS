package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

public class DataUtilTest {

    @Test
    public void testLoadDocumentFromFile() throws IOException {
        String filePath = "path/to/your/test/file.html";
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        Document document = DataUtil.load(new File(filePath), charsetName, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testLoadDocumentFromInputStream() throws IOException {
        String htmlContent = "<html><body><h1>Hello World</h1></body></html>";
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testLoadDocumentFromInputStreamWithCustomParser() throws IOException {
        String htmlContent = "<html xmlns:xhtml=\"http://www.w3.org/1999/xhtml\"><xhtml:html xml:lang=\"en\" xmlns:xml=\"http://www.w3.org/XML/1998/namespace\" xmlns:xhtml=\"http://www.w3.org/1999/xhtml\"><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/><title>Test Page</title></head><body><h1>Hello World</h1></body></xhtml:html>";
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Parser parser = Parser.xmlParser();
        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
        assertNotNull(document);
    }

    @Test
    public void testParseInputStream() throws IOException {
        String htmlContent = "<html><body><h1>Hello World</h1></body></html>";
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseInputStream(inputStream, charsetName, baseUri, Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testParseInputStreamWithCustomParser() throws IOException {
        String htmlContent = "<html xmlns:xhtml=\"http://www.w3.org/1999/xhtml\"><xhtml:html xml:lang=\"en\" xmlns:xml=\"http://www.w3.org/XML/1998/namespace\" xmlns:xhtml=\"http://www.w3.org/1999/xhtml\"><head><meta http-equiv=\"Content-Type\" content=\"text/xml; charset=UTF-8\"/><title>Test Page</title></head><body><h1>Hello World</h1></body></xhtml:html>";
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Parser parser = Parser.xmlParser();
        Document document = DataUtil.parseInputStream(inputStream, charsetName, baseUri, parser);
        assertNotNull(document);
    }

    @Test
    public void testCrossStreams() throws IOException {
        String source = "Source data";
        String target = "Target data";

        ByteArrayInputStream inputStreamSource = new ByteArrayInputStream(source.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream outputStreamTarget = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStreamSource, outputStreamTarget);

        assertEquals(source, outputStreamTarget.toString(StandardCharsets.UTF_8.name()));
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        byte[] expectedData = "Expected Data".getBytes(StandardCharsets.UTF_8);
        InputStream inputStream = new ByteArrayInputStream(expectedData);

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, expectedData.length);

        assertEquals(expectedData, byteBuffer.array());
    }

    @Test
    public void testReadToByteBufferWithMaxSizeGreaterThanInputData() throws IOException {
        byte[] expectedData = "Expected Data".getBytes(StandardCharsets.UTF_8);
        InputStream inputStream = new ByteArrayInputStream(expectedData);

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, expectedData.length + 10);

        assertEquals(expectedData, byteBuffer.array());
    }

    @Test
    public void testReadToByteBufferWithMaxSizeLessThanInputData() throws IOException {
        byte[] expectedData = "Expected Data".getBytes(StandardCharsets.UTF_8);
        InputStream inputStream = new ByteArrayInputStream(expectedData);

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, expectedData.length - 10);

        assertEquals(expectedData, byteBuffer.array());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String expectedCharset = "UTF-8";

        String resultCharset = DataUtil.getCharsetFromContentType(contentType);

        assertEquals(expectedCharset, resultCharset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidCharset() {
        String contentType = "text/html; charset=InvalidCharset";
        String expectedCharset = null;

        String resultCharset = DataUtil.getCharsetFromContentType(contentType);

        assertEquals(expectedCharset, resultCharset);
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        String expectedCharset = "UTF-8";

        String resultCharset = DataUtil.validateCharset(charset);

        assertEquals(expectedCharset, resultCharset);
    }

    @Test
    public void testValidateCharsetWithNullCharset() {
        String charset = null;
        String expectedCharset = null;

        String resultCharset = DataUtil.validateCharset(charset);

        assertEquals(expectedCharset, resultCharset);
    }

    @Test
    public void testMimeBoundary() {
        String expectedMimeBoundary = "-_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String resultMimeBoundary = DataUtil.mimeBoundary();

        assertEquals(expectedMimeBoundary, resultMimeBoundary);
    }
}