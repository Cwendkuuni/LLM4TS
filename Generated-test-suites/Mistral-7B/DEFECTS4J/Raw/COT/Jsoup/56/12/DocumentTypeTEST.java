package org.jsoup.nodes.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.html.HtmlDocument;

/**
 * Test class for {@link DocumentType} class.
 */
public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        DocumentType docType = new DocumentType("html", "HTML", "HTML5", null);
        assertNotNull(docType);
        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr(DocumentType.NAME));
        assertFalse(docType.has(DocumentType.PUBLIC_ID));
        assertFalse(docType.has(DocumentType.SYSTEM_ID));
    }

    @Test
    public void testDocumentTypeCreationWithPublicId() {
        DocumentType docType = new DocumentType("html", "PUBLIC -1 -//W3C//DTD HTML 5//EN", "html5.dtd", null);
        assertNotNull(docType);
        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr(DocumentType.NAME));
        assertTrue(docType.has(DocumentType.PUBLIC_ID));
        assertEquals("PUBLIC -1 -//W3C//DTD HTML 5//EN", docType.attr(DocumentType.PUBLIC_ID));
        assertFalse(docType.has(DocumentType.SYSTEM_ID));
    }

    @Test
    public void testDocumentTypeCreationWithSystemId() {
        DocumentType docType = new DocumentType("html", null, null, "http://example.com/dtd/html5.dtd");
        assertNotNull(docType);
        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr(DocumentType.NAME));
        assertFalse(docType.has(DocumentType.PUBLIC_ID));
        assertTrue(docType.has(DocumentType.SYSTEM_ID));
        assertEquals("http://example.com/dtd/html5.dtd", docType.attr(DocumentType.SYSTEM_ID));
    }

    @Test
    public void testDocumentTypeCreationWithPublicIdAndSystemId() {
        DocumentType docType = new DocumentType("html", "PUBLIC -1 -//W3C//DTD HTML 5//EN", "html5.dtd", "http://example.com/dtd/html5.dtd");
        assertNotNull(docType);
        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr(DocumentType.NAME));
        assertTrue(docType.has(DocumentType.PUBLIC_ID));
        assertEquals("PUBLIC -1 -//W3C//DTD HTML 5//EN", docType.attr(DocumentType.PUBLIC_ID));
        assertTrue(docType.has(DocumentType.SYSTEM_ID));
        assertEquals("http://example.com/dtd/html5.dtd", docType.attr(DocumentType.SYSTEM_ID));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDocumentTypeCreationWithNullName() {
        new DocumentType(null, null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDocumentTypeCreationWithBlankPublicId() {
        new DocumentType("html", "", null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDocumentTypeCreationWithBlankSystemId() {
        new DocumentType("html", null, "", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDocumentTypeCreationWithBlankBaseUri() {
        new DocumentType("html", null, null, "");
    }

    @Test
    public void testOuterHtmlHead() throws IOException {
        DocumentType docType = new DocumentType("html", null, null, null);
        Document htmlDoc = new HtmlDocument();
        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 1, null);
        assertEquals("<!DOCTYPE html>", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithHtml5() throws IOException {
        DocumentType docType = new DocumentType("html", null, null, null);
        HtmlDocument htmlDoc = new HtmlDocument();
        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 1, htmlDoc.outputSettings());
        assertEquals("<!doctype html>", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithPublicId() throws IOException {
        DocumentType docType = new DocumentType("html", "PUBLIC -1 -//W3C//DTD HTML 5//EN", null, null);
        Document htmlDoc = new HtmlDocument();
        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 1, null);
        assertEquals("<!DOCTYPE html PUBLIC \"-1 -//W3C//DTD HTML 5//EN\" \"\">", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithPublicIdAndSystemId() throws IOException {
        DocumentType docType = new DocumentType("html", "PUBLIC -1 -//W3C//DTD HTML 5//EN", "html5.dtd", "http://example.com/dtd/html5.dtd");
        Document htmlDoc = new HtmlDocument();
        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 1, null);
        assertEquals("<!DOCTYPE html PUBLIC \"-1 -//W3C//DTD HTML 5//EN\" \"http://example.com/dtd/html5.dtd\" >", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() throws IOException {
        DocumentType docType = new DocumentType("html", null, null, null);
        Document htmlDoc = new HtmlDocument();
        StringBuilder accum = new StringBuilder();
        docType.outerHtmlTail(accum, 1, null);
        // empty string as per javadoc
        assertEquals("", accum.toString());
    }

    @Test
    public void testNodeName() {
        DocumentType docType = new DocumentType("html", null, null, null);
        assertEquals("#doctype", docType.nodeName());
    }

    @Test
    public void testHasAttribute() {
        DocumentType docType = new DocumentType("html", null, null, null);
        assertTrue(docType.has(DocumentType.NAME));
        assertFalse(docType.has("invalid"));
    }
}