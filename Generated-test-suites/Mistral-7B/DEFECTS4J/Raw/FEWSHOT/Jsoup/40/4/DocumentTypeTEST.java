package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        DocumentType documentType = new DocumentType("html", "HTML TedNG", null, null);
        assertEquals("#doctype", documentType.nodeName());
        assertEquals("html", documentType.attr("name"));
        assertNull(documentType.attr("publicId"));
        assertNull(documentType.attr("systemId"));

        documentType = new DocumentType("html", null, "http://www.w3.org/TR/REC-html40/strict.dtd", null);
        assertEquals("#doctype", documentType.nodeName());
        assertEquals("html", documentType.attr("name"));
        assertEquals("http://www.w3.org/TR/REC-html40/strict.dtd", documentType.attr("systemId"));
        assertNull(documentType.attr("publicId"));

        documentType = new DocumentType("html", "HTML TedNG", "http://www.w3.org/TR/REC-html40/strict.dtd", null);
        assertEquals("#doctype", documentType.nodeName());
        assertEquals("html", documentType.attr("name"));
        assertEquals("http://www.w3.org/TR/REC-html40/strict.dtd", documentType.attr("systemId"));
        assertEquals("HTML TedNG", documentType.attr("publicId"));
    }

    @Test
    public void testDocumentTypeOuterHtmlHead() {
        DocumentType documentType = new DocumentType("html", "HTML TedNG", null, null);
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);
        assertEquals("<!DOCTYPE html>", accum.toString());

        documentType = new DocumentType("html", null, "http://www.w3.org/TR/REC-html40/strict.dtd", null);
        accum.setLength(0);
        documentType.outerHtmlHead(accum, 0, null);
        assertEquals("<!DOCTYPE html PUBLIC \"\" http://www.w3.org/TR/REC-html40/strict.dtd>", accum.toString());

        documentType = new DocumentType("html", "HTML TedNG", "http://www.w3.org/TR/REC-html40/strict.dtd", null);
        accum.setLength(0);
        documentType.outerHtmlHead(accum, 0, null);
        assertEquals("<!DOCTYPE html PUBLIC \"HTML TedNG\" http://www.w3.org/TR/REC-html40/strict.dtd>", accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlTail() {
        DocumentType documentType = new DocumentType("html", "HTML TedNG", null, null);
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlTail(accum, 0, null);
        assertTrue(accum.toString().isEmpty());

        documentType = new DocumentType("html", null, "http://www.w3.org/TR/REC-html40/strict.dtd", null);
        accum.setLength(0);
        documentType.outerHtmlTail(accum, 0, null);
        assertTrue(accum.toString().isEmpty());

        documentType = new DocumentType("html", "HTML TedNG", "http://www.w3.org/TR/REC-html40/strict.dtd", null);
        accum.setLength(0);
        documentType.outerHtmlTail(accum, 0, null);
        assertTrue(accum.toString().isEmpty());
    }

    @Test
    public void testDocumentTypeNodeName() {
        DocumentType documentType = new DocumentType("html", "HTML TedNG", null, null);
        assertEquals("#doctype", documentType.nodeName());

        documentType = new DocumentType("html", null, "http://www.w3.org/TR/REC-html40/strict.dtd", null);
        assertEquals("#doctype", documentType.nodeName());

        documentType = new DocumentType("html", "HTML TedNG", "http://www.w3.org/TR/REC-html40/strict.dtd", null);
        assertEquals("#doctype", documentType.nodeName());
    }
}