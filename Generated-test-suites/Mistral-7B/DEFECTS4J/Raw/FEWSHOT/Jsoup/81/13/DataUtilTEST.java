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
import java.util.Random;

public class DataUtilTest {

    @Test
    public void testLoadDocumentFromInputStream() throws IOException {
        String htmlContent = "<html><head><meta charset=\"UTF-8\"></head><body><div>Test</div></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(inputStream, "UTF-8", null);
        assertEquals("<html><head><meta charset=\"UTF-8\"></head><body><div>Test</div></body></html>", document.html());
    }

    @Test
    public void testLoadDocumentFromInputStreamWithDefaultCharset() throws IOException {
        String htmlContent = "<html><head></head><body><div>Test</div></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes());
        Document document = DataUtil.load(inputStream, null, null);
        assertEquals("<html><head></head><body><div>Test</div></body></html>", document.html());
    }

    @Test
    public void testLoadDocumentFromInputStreamWithInvalidCharset() throws IOException {
        String htmlContent = "<html><head><meta charset=\"invalidCharset\"></head><body><div>Test</div></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(inputStream, "invalidCharset", null);
        assertEquals("<html><head></head><body><div>Test</div></body></html>", document.html()); // The default charset UTF-8 should be used.
    }

    @Test
    public void testParseInputStream() throws IOException {
        String htmlContent = "<html><head><meta charset=\"UTF-8\"></head><body><div>Test</div></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseInputStream(inputStream, "UTF-8", null, Parser.htmlParser());
        assertEquals("<html><head><meta charset=\"UTF-8\"></head><body><div>Test</div></body></html>", document.html());
    }

    @Test
    public void testParseInputStreamWithEmptyInput() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Document document = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
        assertEquals("<html></html>", document.html());
    }

    @Test
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String htmlContent = "<html><head><meta charset=\"invalidCharset\"></head><body><div>Test</div></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseInputStream(inputStream, "invalidCharset", null, Parser.htmlParser());
        assertEquals("<html><head></head><body><div>Test</div></body></html>", document.html()); // The default charset UTF-8 should be used.
    }

    @Test
    public void testParseInputStreamWithXmlDeclarationAndCharset() throws IOException {
        String xmlContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root><child>Test</child></root>";
        InputStream inputStream = new ByteArrayInputStream(xmlContent.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseInputStream(inputStream, null, null, Parser.xmlParser());
        assertEquals("<root><child>Test</child></root>", document.html());
    }

    @Test
    public void testParseInputStreamWithMetaCharset() throws IOException {
        String htmlContent = "<html><head><meta charset=\"UTF-8\"></head><body><div>Test</div></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
        assertEquals("<html><head><meta charset=\"UTF-8\"></head><body><div>Test</div></body></html>", document.html());
    }

    @Test
    public void testParseInputStreamWithMetaCharsetInvalid() throws IOException {
        String htmlContent = "<html><head><meta charset=\"invalidCharset\"></head><body><div>Test</div></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
        assertEquals("<html><head></head><body><div>Test</div></body></html>", document.html()); // The default charset UTF-8 should be used.
    }

    @Test
    public void testParseInputStreamWithContentType() throws IOException {
        String htmlContent = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></head><body><div>Test</div></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
        assertEquals("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></head><body><div>Test</div></body></html>", document.html());
    }

    @Test
    public void testParseInputStreamWithContentTypeInvalidCharset() throws IOException {
        String htmlContent = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=invalidCharset\"></head><body><div>Test</div></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
        assertEquals("<html><head></head><body><div>Test</div></body></html>", document.html()); // The default charset UTF-8 should be used.
    }

    @Test
    public void testParseInputStreamWithXmlDeclaration() throws IOException {
        String xmlContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root><child>Test</child></root>";
        InputStream inputStream = new ByteArrayInputStream(xmlContent.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseInputStream(inputStream, "UTF-8", null, Parser.xmlParser());
        assertEquals("<root><child>Test</child></root>", document.html());
    }

    @Test
    public void testParseInputStreamWithXmlDeclarationAndInvalidCharset() throws IOException {
        String xmlContent = "<?xml version=\"1.0\" encoding=\"invalidCharset\"?>\n<root><child>Test</child></root>";
        InputStream inputStream = new ByteArrayInputStream(xmlContent.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseInputStream(inputStream, "invalidCharset", null, Parser.xmlParser());
        assertEquals("<root><child>Test</child></root>", document.html()); // The default charset UTF-8 should be used.
    }

    @Test
    public void testParseInputStreamWithNoCharset() throws IOException {
        String htmlContent = "<html><head></head><body><div>Test</div></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
        assertEquals("<html><head></head><body><div>Test</div></body></html>", document.html());
    }

    @Test
    public void testParseInputStreamWithNoCharsetAndInvalidContentType() throws IOException {
        String htmlContent = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/plain\"></head><body><div>Test</div></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
        assertEquals("<html><head><meta http-equiv=\"Content-Type\" content=\"text/plain\"></head><body><div>Test</div></body></html>", document.html());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeInvalid() {
        String contentType = "text/html; charset=invalidCharset";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testGetCharsetFromContentTypeInvalidWithInvalidCharset() {
        String contentType = "text/html; charset=INVALID_CHARSET";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithEmptyContentType() {
        String contentType = "text/html";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        String validatedCharset = DataUtil.validateCharset(charset);
        assertEquals("UTF-8", validatedCharset);
    }

    @Test
    public void testValidateCharsetWithInvalidCharset() {
        String charset = "invalidCharset";
        String validatedCharset = DataUtil.validateCharset(charset);
        assertNull(validatedCharset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertNotNull(boundary);
        assertTrue(boundary.length() > 0);
    }
}