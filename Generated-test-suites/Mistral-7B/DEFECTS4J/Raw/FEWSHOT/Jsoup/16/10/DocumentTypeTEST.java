package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        DocumentType documentType = new DocumentType("html", "HTML_PUBLIC_ID", "HTML_SYSTEM_ID", null);

        assertEquals("#doctype", documentType.nodeName());
        assertEquals("html", documentType.attr("name"));
        assertEquals("HTML_PUBLIC_ID", documentType.attr("publicId"));
        assertEquals("HTML_SYSTEM_ID", documentType.attr("systemId"));
        assertEquals(null, documentType.baseUri());
    }

    @Test
    public void testDocumentTypeWithPublicId() {
        DocumentType documentType = new DocumentType("html", "HTML_PUBLIC_ID", "HTML_SYSTEM_ID", null);
        StringBuilder accumulator = new StringBuilder();

        documentType.outerHtmlHead(accumulator, 0, null);

        assertEquals("<!DOCTYPE html PUBLIC \"HTML_PUBLIC_ID\"", accumulator.toString());
    }

    @Test
    public void testDocumentTypeWithSystemId() {
        DocumentType documentType = new DocumentType("html", null, "HTML_SYSTEM_ID", null);
        StringBuilder accumulator = new StringBuilder();

        documentType.outerHtmlHead(accumulator, 0, null);

        assertEquals("<!DOCTYPE html SYSTEM \"HTML_SYSTEM_ID\">", accumulator.toString());
    }

    @Test
    public void testDocumentTypeWithBothPublicIdAndSystemId() {
        DocumentType documentType = new DocumentType("html", "HTML_PUBLIC_ID", "HTML_SYSTEM_ID", null);
        StringBuilder accumulator = new StringBuilder();

        documentType.outerHtmlHead(accumulator, 0, null);

        assertEquals("<!DOCTYPE html PUBLIC \"HTML_PUBLIC_ID\" SYSTEM \"HTML_SYSTEM_ID\">", accumulator.toString());
    }

    @Test
    public void testDocumentTypeWithEmptyAttributes() {
        DocumentType documentType = new DocumentType("html", null, null, null);
        StringBuilder accumulator = new StringBuilder();

        documentType.outerHtmlHead(accumulator, 0, null);

        assertEquals("<!DOCTYPE html>", accumulator.toString());
    }

    @Test
    public void testDocumentTypeWithNullAttributes() {
        DocumentType documentType = new DocumentType("html", null, null, null);

        assertNull(documentType.attr("publicId"));
        assertNull(documentType.attr("systemId"));
    }

    @Test
    public void testDocumentTypeWithBaseUri() {
        String baseUri = "base_uri";
        DocumentType documentType = new DocumentType("html", "HTML_PUBLIC_ID", "HTML_SYSTEM_ID", baseUri);

        assertEquals(baseUri, documentType.baseUri());
    }
}