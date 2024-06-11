package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DocumentTypeTest {
    @Test
    public void testDocumentTypeConstructor() {
        DocumentType docType = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.example.com");

        assertEquals("html", docType.nodeName());
        assertEquals("-//W3C//DTD HTML 4.01//EN", docType.attr(DocumentType.PUBLIC_ID));
        assertEquals("http://www.w3.org/TR/html4/strict.dtd", docType.attr(DocumentType.SYSTEM_ID));
        assertEquals("http://www.example.com", docType.baseUri());
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType docType = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.example.com");

        Appendable accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, new Document.OutputSettings(Document.OutputSettings.Syntax.html));
        assertEquals("<!doctype html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">", accum.toString());

        accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, new Document.OutputSettings(Document.OutputSettings.Syntax.xml));
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">", accum.toString());

    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType docType = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.example.com");

        Appendable accum = new StringBuilder();
        docType.outerHtmlTail(accum, 0, new Document.OutputSettings(Document.OutputSettings.Syntax.html));
        assertEquals("", accum.toString());

        accum = new StringBuilder();
        docType.outerHtmlTail(accum, 0, new Document.OutputSettings(Document.OutputSettings.Syntax.xml));
        assertEquals("", accum.toString());
    }

    @Test
    public void testHas() {
        DocumentType docType = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.example.com");

        assertEquals(true, docType.has(DocumentType.NAME));
        assertEquals(true, docType.has(DocumentType.PUBLIC_ID));
        assertEquals(true, docType.has(DocumentType.SYSTEM_ID));
        assertEquals(false, docType.has("invalidAttribute"));
    }
}