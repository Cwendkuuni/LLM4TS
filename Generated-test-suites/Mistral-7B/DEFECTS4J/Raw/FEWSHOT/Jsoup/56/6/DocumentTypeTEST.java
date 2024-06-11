package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import org.jsoup.helper.StringUtil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        DocumentType docType = new DocumentType("html", "", "", "");
        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr(DocumentType.NAME));
        assertNull(docType.attr(DocumentType.PUBLIC_ID));
        assertNull(docType.attr(DocumentType.SYSTEM_ID));

        docType = new DocumentType("html", "publicID", "systemID", "");
        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr(DocumentType.NAME));
        assertEquals("publicID", docType.attr(DocumentType.PUBLIC_ID));
        assertEquals("systemID", docType.attr(DocumentType.SYSTEM_ID));
    }

    @Test
    public void testDocumentTypeCreationWithBaseUri() {
        Document doc = new Document();
        DocumentType docType = new DocumentType("html", "", "", "http://example.com");
        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr(DocumentType.NAME));
        assertNull(docType.attr(DocumentType.PUBLIC_ID));
        assertNull(docType.attr(DocumentType.SYSTEM_ID));
        assertEquals("http://example.com", docType.baseUri());

        Element element = doc.createElement("html");
        element.appendChild(docType);
        assertEquals("http://example.com", element.baseUri());
    }

    @Test
    public void testOuterHtmlHead() throws IOException {
        DocumentType docType = new DocumentType("html", "", "", "");
        Appendable out = new StringBuilder();

        docType.outerHtmlHead(out, 0, Document.OutputSettings.syntaxHtml5());
        assertEquals("<!doctype html>", out.toString());

        docType = new DocumentType("html", "publicID", "systemID", "");
        out.setLength(0);
        docType.outerHtmlHead(out, 0, Document.OutputSettings.syntaxHtml5());
        assertEquals("<!DOCTYPE html PUBLIC \"publicID\" \"systemID\">", out.toString());
    }

    @Test
    public void testHasAttribute() {
        DocumentType docType = new DocumentType("html", "publicID", "systemID", "");

        assertTrue(docType.has(DocumentType.NAME));
        assertTrue(docType.has(DocumentType.PUBLIC_ID));
        assertTrue(docType.has(DocumentType.SYSTEM_ID));

        docType = new DocumentType("html", "", "", "");

        assertFalse(docType.has(DocumentType.PUBLIC_ID));
        assertFalse(docType.has(DocumentType.SYSTEM_ID));
    }
}