package org.jsoup.nodes;

import static org.junit.Assert.*;
import static org.jsoup.helper.StringUtil.isBlank;

import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        DocumentType docType = new DocumentType("html", "publicID", "systemID", "baseUri");
        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr(NAME));
        assertEquals("publicID", docType.attr(PUBLIC_ID));
        assertEquals("systemID", docType.attr(SYSTEM_ID));
        assertEquals("baseUri", docType.baseUri());
    }

    @Test
    public void testDocumentTypeCreationWithEmptyAttributes() {
        DocumentType docType = new DocumentType("html");
        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr(NAME));
        assertTrue(isBlank(docType.attr(PUBLIC_ID)));
        assertTrue(isBlank(docType.attr(SYSTEM_ID)));
        assertNull(docType.baseUri());
    }

    @Test
    public void testOuterHtmlHead() throws IOException {
        DocumentType docType = new DocumentType("html", "publicID", "systemID", "baseUri");
        Appendable accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html PUBLIC \"publicID\" SYSTEM \"systemID\">", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithHtml5() throws IOException {
        DocumentType docType = new DocumentType("html");
        Appendable accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);

        assertEquals("<!doctype html>", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() throws IOException {
        DocumentType docType = new DocumentType("html", "publicID", "systemID", "baseUri");
        Appendable accum = new StringBuilder();
        docType.outerHtmlTail(accum, 0, null);

        assertTrue(isBlank(accum.toString()));
    }

    @Test
    public void testHasAttribute() {
        DocumentType docType = new DocumentType("html", "publicID", "systemID", "baseUri");
        assertTrue(docType.has(NAME));
        assertTrue(docType.has(PUBLIC_ID));
        assertTrue(docType.has(SYSTEM_ID));
        assertFalse(docType.has("nonExistentAttribute"));
    }

    @Test
    public void testHasAttributeWithEmptyAttribute() {
        DocumentType docType = new DocumentType("html");
        assertFalse(docType.has(NAME));
        assertFalse(docType.has(PUBLIC_ID));
        assertFalse(docType.has(SYSTEM_ID));
    }
}