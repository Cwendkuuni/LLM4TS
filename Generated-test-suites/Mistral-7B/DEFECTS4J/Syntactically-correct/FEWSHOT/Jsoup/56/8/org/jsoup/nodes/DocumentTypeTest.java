package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        DocumentType docType = new DocumentType("html", "publicID", "systemID", null);
        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr(NAME));
        assertNotNull(docType.attr(PUBLIC_ID));
        assertNotNull(docType.attr(SYSTEM_ID));
        assertNull(docType.getElementsByTag("script").first());
    }

    @Test
    public void testDocumentTypeCreationWithoutIDs() {
        DocumentType docType = new DocumentType("html");
        assertEquals("#doctype", docType.nodeName());
        assertNull(docType.attr(PUBLIC_ID));
        assertNull(docType.attr(SYSTEM_ID));
        assertNull(docType.getElementsByTag("script").first());
    }

    @Test
    public void testDocumentTypeOuterHtml() throws IOException {
        DocumentType docType = new DocumentType("html", "publicID", "systemID", null);

        Appendable appendable = new StringBuilder();
        docType.outerHtmlHead(appendable, 0, null);
        docType.outerHtmlTail(appendable, 0, null);

        assertEquals("<!DOCTYPE html PUBLIC \"publicID\" \"systemID\">", appendable.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlHtml5() throws IOException {
        DocumentType docType = new DocumentType("html");

        Appendable appendable = new StringBuilder();
        docType.outerHtmlHead(appendable, 0, null);
        docType.outerHtmlTail(appendable, 0, null);

        assertEquals("<!doctype html>", appendable.toString());
    }

    @Test
    public void testDocumentTypeHasAttribute() {
        DocumentType docType = new DocumentType("html", "publicID", "systemID", null);
        assertTrue(docType.has(NAME));
        assertTrue(docType.has(PUBLIC_ID));
        assertTrue(docType.has(SYSTEM_ID));
    }

    @Test
    public void testDocumentTypeNoAttribute() {
        DocumentType docType = new DocumentType("html");
        assertFalse(docType.has(PUBLIC_ID));
        assertFalse(docType.has(SYSTEM_ID));
    }
}