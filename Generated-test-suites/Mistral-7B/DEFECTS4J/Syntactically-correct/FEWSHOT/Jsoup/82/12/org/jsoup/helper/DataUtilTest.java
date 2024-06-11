package org.jsoup.helper;

import static org.junit.Assert.*;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    @Test
    public void testLoadDocumentFromInputStream() throws Exception {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Test</h1></body></html>";
        String charset = "UTF-8";

        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, charset, null);

        assertEquals("UTF-8", doc.outputSettings().charset());
        assertEquals("<html><head><meta charset=\"UTF-8\"><body><h1>Test</h1></body></html>", doc.html());
    }

    @Test
    public void testLoadDocumentFromInputStreamWithInvalidCharset() throws Exception {
        String html = "<html><head><meta charset=\"GBK\"></head><body><h1>Test</h1></body></html>";
        String charset = "UTF-8"; // invalid charset

        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document doc = null;
        try {
            doc = DataUtil.load(inputStream, charset, null);
        } catch (IllegalArgumentException e) {
            // expected
        }

        assertNull(doc);
    }

    @Test
    public void testLoadDocumentFromInputStreamWithBom() throws Exception {
        String html = "\uFEFF<html><head><meta charset=\"UTF-8\"></head><body><h1>Test</h1></body></html>";

        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, null, null);

        assertEquals("UTF-8", doc.outputSettings().charset());
        assertEquals("<html><head><meta charset=\"UTF-8\"><body><h1>Test</h1></body></html>", doc.html());
    }

    @Test
    public void testLoadDocumentFromInputStreamWithNoBomOrCharset() throws Exception {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Test</h1></body></html>";

        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, null, null);

        assertEquals("UTF-8", doc.outputSettings().charset());
        assertEquals("<html><head><meta charset=\"UTF-8\"><body><h1>Test</h1></body></html>", doc.html());
    }

    @Test
    public void testLoadDocumentFromInputStreamWithMetaCharset() throws Exception {
        String html = "<html><head><meta charset=\"GBK\"></head><body><h1>Test</h1></body></html>";

        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, null, null);

        assertEquals("GBK", doc.outputSettings().charset());
        assertEquals("<html><head><meta charset=\"GBK\"><body><h1>Test</h1></body></html>", doc.html());
    }

    @Test
    public void testLoadDocumentFromInputStreamWithXmlDeclaration() throws Exception {
        String html = "<?xml encoding=\"UTF-8\"?>\n<html><body><h1>Test</h1></body></html>";

        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, null, null, DataUtil.Parser.xmlParser());

        assertEquals("UTF-8", doc.outputSettings().charset());
        assertEquals("<html><body><h1>Test</h1></body></html>", doc.html());
    }

    @Test
    public void testLoadDocumentFromInputStreamWithXmlDeclarationAndMetaCharset() throws Exception {
        String html = "<?xml encoding=\"UTF-8\"?>\n<html><head><meta charset=\"GBK\"></head><body><h1>Test</h1></body></html>";

        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, null, null, DataUtil.Parser.xmlParser());

        assertEquals("UTF-8", doc.outputSettings().charset());
        assertEquals("<html><head><meta charset=\"GBK\"><body><h1>Test</h1></body></html>", doc.html());
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();

        assertNotNull(boundary);
        assertTrue(boundary.length() >= 32);
    }
}