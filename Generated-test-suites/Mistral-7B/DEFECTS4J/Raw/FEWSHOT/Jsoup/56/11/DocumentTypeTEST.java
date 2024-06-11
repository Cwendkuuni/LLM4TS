package org.jsoup.nodes;

import static org.junit.Assert.*;
import static org.jsoup.helper.StringUtil.*;

import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", null);
        assertEquals("#doctype", documentType.nodeName());
        assertEquals("html", documentType.attr(DocumentType.NAME));
        assertFalse(documentType.has(DocumentType.PUBLIC_ID));
        assertFalse(documentType.has(DocumentType.SYSTEM_ID));

        DocumentType documentType1 = new DocumentType("html", null, null, null);
        assertEquals("#doctype", documentType1.nodeName());
        assertEquals("html", documentType1.attr(DocumentType.NAME));
        assertFalse(documentType1.has(DocumentType.PUBLIC_ID));
        assertFalse(documentType1.has(DocumentType.SYSTEM_ID));
    }

    @Test
    public void testDocumentTypeCreationWithAllAttributes() {
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", "http://example.com");
        assertEquals("#doctype", documentType.nodeName());
        assertEquals("html", documentType.attr(DocumentType.NAME));
        assertTrue(documentType.has(DocumentType.PUBLIC_ID));
        assertEquals("publicId", documentType.attr(DocumentType.PUBLIC_ID));
        assertTrue(documentType.has(DocumentType.SYSTEM_ID));
        assertEquals("systemId", documentType.attr(DocumentType.SYSTEM_ID));
        assertEquals("http://example.com", documentType.baseUri());
    }

    @Test
    public void testOutputHtmlHead() {
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", null);
        StringBuilder sb = new StringBuilder();
        documentType.outerHtmlHead(sb, 0, Document.OutputSettings.syntaxHtml());
        String expected = "<!doctype html>";
        assertEquals(expected, sb.toString());

        DocumentType documentType1 = new DocumentType("html", null, null, null);
        StringBuilder sb1 = new StringBuilder();
        documentType1.outerHtmlHead(sb1, 0, Document.OutputSettings.syntaxHtml());
        String expected1 = "<!doctype html>";
        assertEquals(expected1, sb1.toString());

        DocumentType documentType2 = new DocumentType("html", "publicId", "systemId", null);
        StringBuilder sb2 = new StringBuilder();
        documentType2.outerHtmlHead(sb2, 0, Document.OutputSettings.syntaxHtml(true));
        String expected2 = "<!doctype html PUBLIC \"publicId\"";
        assertEquals(expected2, sb2.toString());

        DocumentType documentType3 = new DocumentType("html", "publicId", "systemId", null);
        StringBuilder sb3 = new StringBuilder();
        documentType3.outerHtmlHead(sb3, 0, Document.OutputSettings.syntaxXhtml());
        String expected3 = "<!DOCTYPE html PUBLIC \"publicId\"";
        assertEquals(expected3, sb3.toString());
    }

    @Test
    public void testHasAttribute() {
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", null);
        DocumentType documentType1 = new DocumentType("html", null, null, null);

        assertTrue(documentType.has(DocumentType.NAME));
        assertFalse(documentType.has("nonExistentAttribute"));

        DocumentType documentType2 = new DocumentType("html", "publicId", null, null);
        assertTrue(documentType2.has(DocumentType.NAME));
        assertFalse(documentType2.has(DocumentType.SYSTEM_ID));

        DocumentType documentType3 = new DocumentType("html", null, "systemId", null);
        assertTrue(documentType3.has(DocumentType.NAME));
        assertTrue(documentType3.has(DocumentType.SYSTEM_ID));

        DocumentType documentType4 = new DocumentType("html", null, null, "http://example.com");
        assertTrue(documentType4.has(DocumentType.NAME));
        assertTrue(documentType4.has(DocumentType.BASE_URI));

        DocumentType documentType5 = new DocumentType("html", null, null, null);
        assertFalse(documentType5.has(DocumentType.BASE_URI));
    }
}