package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * Test class for DataUtil.
 */
public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        File inputFile = new File("input.html");

        Document document = DataUtil.load(inputFile, charsetName, baseUri);

        assertNotNull(document);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        InputStream inputStream = getClass().getResourceAsStream("/input.html");

        Document document = DataUtil.load(inputStream, charsetName, baseUri);

        assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        InputStream inputStream = getClass().getResourceAsStream("/input.html");

        Parser parser = DataUtil.Parser.xmlParser();

        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);

        assertNotNull(document);
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/input.html");
        OutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);

        InputStream testStream = new ByteArrayInputStream(outputStream.toByteArray());
        Document document = DataUtil.load(testStream, "UTF-8", null);

        assertNotNull(document);
    }

    @Test
    public void testParseByteData() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        ByteBuffer byteData = DataUtil.emptyByteBuffer();

        byte[] data = "<html><head><meta charset=\"UTF-8\"></meta></head><body></body></html>".getBytes();
        byteData.put(data);
        byteData.flip();

        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, DataUtil.Parser.htmlParser());

        assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithDetectedCharset() throws IOException {
        String charsetName = null;
        String baseUri = "http://example.com";
        ByteBuffer byteData = DataUtil.readFileToByteBuffer(new File("input.html"));

        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, DataUtil.Parser.htmlParser());

        assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testParseByteDataWithInvalidCharset() throws IOException {
        String charsetName = "invalidCharset";
        String baseUri = "http://example.com";
        ByteBuffer byteData = DataUtil.emptyByteBuffer();

        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, DataUtil.Parser.htmlParser());

        fail("Expected IOException but no exception was thrown");
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/input.html");
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);

        assertNotNull(byteData);
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/input.html");
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, 10);

        assertNotNull(byteData);
        assertTrue(byteData.remaining() <= 10);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeNull() {
        String contentType = "text/html";

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertNull(charset);
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";

        String validatedCharset = DataUtil.validateCharset(charset);

        assertEquals(charset, validatedCharset);
    }

    @Test
    public void testValidateCharsetInvalidCharset() {
        String charset = "invalidCharset";

        String validatedCharset = DataUtil.validateCharset(charset);

        assertNull(validatedCharset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();

        assertNotNull(boundary);
        assertFalse(boundary.isEmpty());
    }

}