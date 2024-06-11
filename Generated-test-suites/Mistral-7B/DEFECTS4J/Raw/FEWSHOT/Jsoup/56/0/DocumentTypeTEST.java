package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        DocumentType docType = new DocumentType("html", "public-ident-1", "system-ident-1", null);
        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr(NAME));
        assertNotNull(docType.attr(PUBLIC_ID));
        assertNotNull(docType.attr(SYSTEM_ID));
    }

    @Test
    public void testDocumentTypeCreationWithNullBaseUri() {
        DocumentType docType = new DocumentType("html", "public-ident-1", "system-ident-1", null);
        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr(NAME));
        assertNotNull(docType.attr(PUBLIC_ID));
        assertNotNull(docType.attr(SYSTEM_ID));
        assertNull(docType.baseUri());
    }

    @Test
    public void testDocumentTypeCreationWithOnlyName() {
        DocumentType docType = new DocumentType("html");
        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr(NAME));
        assertNull(docType.attr(PUBLIC_ID));
        assertNull(docType.attr(SYSTEM_ID));
    }

    @Test
    public void testDocumentTypeCreationWithOnlyPublicId() {
        DocumentType docType = new DocumentType("html", "public-ident-1");
        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr(NAME));
        assertNotNull(docType.attr(PUBLIC_ID));
        assertNull(docType.attr(SYSTEM_ID));
    }

    @Test
    public void testDocumentTypeCreationWithOnlySystemId() {
        DocumentType docType = new DocumentType("html", null, "system-ident-1");
        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr(NAME));
        assertNull(docType.attr(PUBLIC_ID));
        assertNotNull(docType.attr(SYSTEM_ID));
    }

    @Test
    public void testDocumentTypeCreationWithPublicAndSystemIds() {
        DocumentType docType = new DocumentType("html", "public-ident-1", "system-ident-1");
        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr(NAME));
        assertNotNull(docType.attr(PUBLIC_ID));
        assertNotNull(docType.attr(SYSTEM_ID));
    }

    @Test
    public void testDocumentTypeOuterHtmlHead() throws IOException {
        DocumentType docType = new DocumentType("html", "public-ident-1", "system-ident-1", null);
        Appendable accum = new StringBuilder();
        docType.outerHtmlHead(accum, 1, null);

        assertEquals("<!DOCTYPE html PUBLIC \"public-ident-1\" \"system-ident-1\">", accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlHeadHtml5() throws IOException {
        DocumentType docType = new DocumentType("html");
        Appendable accum = new StringBuilder();
        docType.outerHtmlHead(accum, 1, null);

        assertEquals("<!doctype html>", accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlTail() {
        DocumentType docType = new DocumentType("html", "public-ident-1", "system-ident-1", null);
        Appendable accum = new StringBuilder();
        docType.outerHtmlTail(accum, 1, null);

        assertEquals("", accum.toString());
    }

    @Test
    public void testDocumentTypeHasAttribute() {
        DocumentType docType = new DocumentType("html", "public-ident-1", "system-ident-1", null);

        // Name
        assertTrue(docType.has(NAME));
        assertEquals("html", docType.attr(NAME));

        // Public ID
        assertTrue(docType.has(PUBLIC_ID));
        assertNotNull(docType.attr(PUBLIC_ID));

        // System ID
        assertTrue(docType.has(SYSTEM_ID));
        assertNotNull(docType.attr(SYSTEM_ID));
    }

    @Test
    public void testDocumentTypeWithoutAnyAttribute() {
        DocumentType docType = new DocumentType("html");

        // Name
        assertTrue(docType.has(NAME));
        assertEquals("html", docType.attr(NAME));

        // Public ID
        assertFalse(docType.has(PUBLIC_ID));
        assertNull(docType.attr(PUBLIC_ID));

        // System ID
        assertFalse(docType.has(SYSTEM_ID));
        assertNull(docType.attr(SYSTEM_ID));
    }
}