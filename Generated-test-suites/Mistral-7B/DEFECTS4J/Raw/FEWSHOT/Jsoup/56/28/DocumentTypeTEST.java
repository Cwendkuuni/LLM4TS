package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        DocumentType docType = new DocumentType("html", "publicId", "systemId", null);
        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr(Node.NAME));
        assertEquals("publicId", docType.attr(DocumentType.PUBLIC_ID));
        assertEquals("systemId", docType.attr(DocumentType.SYSTEM_ID));
    }

    @Test
    public void testDocumentTypeCreationWithNullAttributes() {
        DocumentType docType = new DocumentType("html");
        assertEquals("#doctype", docType.nodeName());
        assertNull(docType.attr(DocumentType.PUBLIC_ID));
        assertNull(docType.attr(DocumentType.SYSTEM_ID));
    }

    @Test
    public void testDocumentTypeCreationWithAllAttributes() {
        DocumentType docType = new DocumentType("html", "publicId", "systemId", "baseUri");
        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr(Node.NAME));
        assertEquals("publicId", docType.attr(DocumentType.PUBLIC_ID));
        assertEquals("systemId", docType.attr(DocumentType.SYSTEM_ID));
        assertEquals("baseUri", docType.baseUri());
    }

    @Test
    public void testDocumentTypeToString() {
        DocumentType docType = new DocumentType("html", "publicId", "systemId", null);
        assertEquals("<!DOCTYPE html PUBLIC \"publicId\" SYSTEM>", docType.toString());
    }

    @Test
    public void testDocumentTypeHtml5ToString() {
        DocumentType docType = new DocumentType("html");
        assertEquals("<!doctype html>", docType.toString());
    }

    @Test
    public void testDocumentTypeHasAttribute() {
        DocumentType docType = new DocumentType("html", "publicId", "systemId", null);
        DocumentType docTypeWithPublicId = new DocumentType("html", "publicId", null, null);
        DocumentType docTypeWithSystemId = new DocumentType("html", null, "systemId", null);
        DocumentType docTypeWithName = new DocumentType("html", null, null, null);

        assertTrue(docType.has(DocumentType.NAME));
        assertTrue(docTypeWithPublicId.has(DocumentType.PUBLIC_ID));
        assertTrue(docTypeWithSystemId.has(DocumentType.SYSTEM_ID));
        assertFalse(docTypeWithName.has(DocumentType.PUBLIC_ID));
        assertFalse(docTypeWithName.has(DocumentType.SYSTEM_ID));
    }
}