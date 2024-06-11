package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        // Test with valid arguments
        DocumentType docType = new DocumentType("html", "publicID", "systemID", null);
        assertNotNull(docType);
        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr("name"));
        assertNotNull(docType.attr("publicId"));
        assertNotNull(docType.attr("systemId"));
        assertNull(docType.attr("id")); // by default, id attribute is null

        // Test with null arguments
        DocumentType docTypeNullBaseUri = new DocumentType("html", null, null, null);
        assertNotNull(docTypeNullBaseUri);
        DocumentType docTypeNullPublicId = new DocumentType("html", null, "systemID", null);
        assertNotNull(docTypeNullPublicId);
        DocumentType docTypeNullSystemId = new DocumentType("html", "publicID", null, null);
        assertNotNull(docTypeNullSystemId);
    }

    @Test
    public void testDocumentTypeOuterHtml() {
        DocumentType docType = new DocumentType("html", "publicID", "systemID", null);
        StringBuilder accum = new StringBuilder();

        docType.outerHtmlHead(accum, 0, null);
        assertTrue(accum.toString().startsWith("<!DOCTYPE html"));
        assertTrue(accum.toString().contains(" PUBLIC \"" + docType.attr("publicId") + "\""));
        assertTrue(accum.toString().contains(docType.attr("systemId") + "\""));

        docType.outerHtmlTail(accum, 0, null);
        assertTrue(accum.toString().endsWith(">"));

        accum.setLength(0);
        docType.outerHtmlHead(accum, 0, null);
        docType.outerHtmlTail(accum, 0, null);
        assertEquals("<!DOCTYPE html>", accum.toString());

        docType.attr("id", "testId");
        accum.setLength(0);
        docType.outerHtmlHead(accum, 0, null);
        docType.outerHtmlTail(accum, 0, null);
        assertTrue(accum.toString().contains(" id=\"testId\""));
    }
}