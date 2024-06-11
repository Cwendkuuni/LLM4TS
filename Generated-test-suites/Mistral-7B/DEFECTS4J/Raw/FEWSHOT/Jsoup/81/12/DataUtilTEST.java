package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        File file = new File("testFile.html");
        Document document = DataUtil.load(file, null);
        assertNotNull(document);
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        File file = new File("testFile.html");
        Document document = DataUtil.load(file, "UTF-8");
        assertNotNull(document);
    }

    @Test
    public void testParseInputStreamWithDefaultCharset() throws IOException {
        String htmlText = "<html><head><meta charset='UTF-8'></head><body>Test</body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(htmlText.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testParseInputStreamWithSpecifiedCharset() throws IOException {
        String htmlText = "<html><head><meta charset='UTF-8'></head><body>Test</body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(htmlText.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseInputStream(inputStream, "UTF-8", null, Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testParseInputStreamWithParser() throws IOException {
        String xmlText = "<xml><root><child>Test</child></root></xml>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(xmlText.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseInputStream(inputStream, null, null, Parser.xmlParser());
        assertNotNull(document);
    }

    @Test
    public void testCrossStreams() throws IOException {
        String sourceText = "Source text.";
        String destinationText = "Destination text.";

        ByteArrayInputStream sourceInputStream = new ByteArrayInputStream(sourceText.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream destinationOutputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(sourceInputStream, destinationOutputStream);

        assertEquals(sourceText, destinationText);
    }

    @Test
    public void testParseInputStreamWithBom() throws IOException {
        String utf16Text = "\ufefftest";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(utf16Text.getBytes(StandardCharsets.UTF_16LE));
        Document document = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testParseInputStreamWithBomAndSpecifiedCharset() throws IOException {
        String utf16Text = "\ufefftest";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(utf16Text.getBytes(StandardCharsets.UTF_16LE));
        Document document = DataUtil.parseInputStream(inputStream, "UTF-16LE", null, Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testParseInputStreamWithInvalidBom() throws IOException {
        String invalidBomText = "\ufeffinvalidtext";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(invalidBomText.getBytes(StandardCharsets.UTF_16LE));
        Document document = null;

        try {
            document = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }

        assertNull(document);
    }

    @Test
    public void testParseInputStreamWithInvalidBomAndSpecifiedCharset() throws IOException {
        String invalidBomText = "\ufeffinvalidtext";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(invalidBomText.getBytes(StandardCharsets.UTF_16LE));
        Document document = null;

        try {
            document = DataUtil.parseInputStream(inputStream, "UTF-16LE", null, Parser.htmlParser());
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }

        assertNull(document);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentTypeText = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentTypeText);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidCharset() {
        String contentTypeText = "text/html; charset=UTF-123";
        String charset = DataUtil.getCharsetFromContentType(contentTypeText);
        assertNull(charset);
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-16";
        String validatedCharset = DataUtil.validateCharset(charset);
        assertEquals("UTF-16", validatedCharset);
    }

    @Test
    public void testValidateCharsetWithInvalidCharset() {
        String charset = "UTF-123";
        String validatedCharset = DataUtil.validateCharset(charset);
        assertNull(validatedCharset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertFalse(boundary.isEmpty());
    }
}