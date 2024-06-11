package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        DocumentType docType = new DocumentType("html", "publicID", "systemID", null);
        assertEquals("html", docType.name());
        assertEquals("publicID", docType.publicId());
        assertEquals("systemID", docType.systemId());
        assertNull(docType.baseUri());
    }

    @Test
    public void testDocumentTypeCreationWithEmptyAttributes() {
        DocumentType docType = new DocumentType("html");
        assertEquals("html", docType.name());
        assertNull(docType.publicId());
        assertNull(docType.systemId());
        assertNull(docType.baseUri());
    }

    @Test
    public void testNodeName() {
        DocumentType docType = new DocumentType("html", null, null, null);
        assertEquals("#doctype", docType.nodeName());
    }

    @Test
    public void testOuterHtmlHead() throws IOException {
        DocumentType docType = new DocumentType("html", "publicID", "systemID", null);
        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, Document.OutputSettings.syntaxHtml5());
        assertEquals("<!doctype html>", accum.toString());

        docType.outerHtmlHead(accum, 0, Document.OutputSettings.syntaxXhtml());
        assertEquals("<!DOCTYPE html PUBLIC \"publicID\" SYSTEM \"\">", accum.toString());

        docType.outerHtmlHead(accum, 0, Document.OutputSettings.syntaxTransitional());
        assertEquals("<!DOCTYPE html PUBLIC \"publicID\" SYSTEM \"systemID\">", accum.toString());
    }

    @Test
    public void testHasAttribute() {
        DocumentType docType = new DocumentType("html", "publicID", "systemID", null);
        docType.attr("name", "html5");

        // Test with existing attribute
        assertTrue(docType.has("name"));
        assertEquals("html5", docType.attr("name"));

        // Test with non-existent attribute
        assertFalse(docType.has("nonExistentAttribute"));
    }
}