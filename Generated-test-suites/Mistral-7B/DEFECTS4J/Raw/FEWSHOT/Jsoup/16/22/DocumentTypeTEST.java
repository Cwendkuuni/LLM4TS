package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        DocumentType documentType = new DocumentType("html", "HTML", "HTML5", null);
        assertEquals("#doctype", documentType.nodeName());
        assertEquals("html", documentType.attr("name"));
        assertNull(documentType.attr("publicId"));
        assertNull(documentType.attr("systemId"));
    }

    @Test
    public void testDocumentTypeCreationWithPublicId() {
        DocumentType documentType = new DocumentType("html", "PUBLIC -//W3C//DTD HTML 5//EN", "HTML5", null);
        assertEquals("#doctype", documentType.nodeName());
        assertEquals("html", documentType.attr("name"));
        assertEquals("PUBLIC -//W3C//DTD HTML 5//EN", documentType.attr("publicId"));
        assertNull(documentType.attr("systemId"));
    }

    @Test
    public void testDocumentTypeCreationWithSystemId() {
        DocumentType documentType = new DocumentType("html", null, null, "http://example.com/");
        assertEquals("#doctype", documentType.nodeName());
        assertEquals("html", documentType.attr("name"));
        assertNull(documentType.attr("publicId"));
        assertEquals("http://example.com/", documentType.baseUri());
    }

    @Test
    public void testDocumentTypeCreationWithPublicIdAndSystemId() {
        DocumentType documentType = new DocumentType("html", "PUBLIC -//W3C//DTD HTML 5//EN", "http://www.w3.org/TR/html5/javaScript.dtd", "http://example.com/");
        assertEquals("#doctype", documentType.nodeName());
        assertEquals("html", documentType.attr("name"));
        assertEquals("PUBLIC -//W3C//DTD HTML 5//EN", documentType.attr("publicId"));
        assertEquals("http://www.w3.org/TR/html5/javaScript.dtd", documentType.attr("systemId"));
        assertEquals("http://example.com/", documentType.baseUri());
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType documentType = new DocumentType("html", "PUBLIC -//W3C//DTD HTML 5//EN", "http://www.w3.org/TR/html5/javaScript.dtd", null);
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 5//EN\" \"http://www.w3.org/TR/html5/javaScript.dtd\">", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithEmptyPublicId() {
        DocumentType documentType = new DocumentType("html", null, null, null);
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html>", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithEmptySystemId() {
        DocumentType documentType = new DocumentType("html", null, null, null);
        Document doc = new Document();
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, doc.outputSettings());

        assertEquals("<!DOCTYPE html>" + System.lineSeparator(), accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithBaseUri() {
        DocumentType documentType = new DocumentType("html", null, null, "http://example.com/");
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html>" + System.lineSeparator() + " <!DOCTYPE html>", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType documentType = new DocumentType("html", null, null, null);
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlTail(accum, 0, null);

        assertEquals("", accum.toString());
    }

    @Test
    public void testBaseUri() {
        DocumentType documentType = new DocumentType("html", null, null, "http://example.com/");
        assertEquals("http://example.com/", documentType.baseUri());

        DocumentType documentType2 = new DocumentType("html", null, null, null);
        assertEquals(null, documentType2.baseUri());
    }
}