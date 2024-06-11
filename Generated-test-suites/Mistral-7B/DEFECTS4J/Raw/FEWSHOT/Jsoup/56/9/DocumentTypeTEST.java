package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        String name = "html";
        String publicId = "publicId";
        String systemId = "systemId";
        String baseUri = "baseUri";

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        assertEquals(name, documentType.attr(DocumentType.NAME));
        assertEquals(publicId, documentType.attr(DocumentType.PUBLIC_ID));
        assertEquals(systemId, documentType.attr(DocumentType.SYSTEM_ID));
        assertEquals(baseUri, documentType.baseUri());
    }

    @Test
    public void testDocumentTypeCreationWithoutAttributes() {
        String name = "html";

        DocumentType documentType = new DocumentType(name, null, null, null);

        assertEquals(name, documentType.attr(DocumentType.NAME));
        assertNull(documentType.attr(DocumentType.PUBLIC_ID));
        assertNull(documentType.attr(DocumentType.SYSTEM_ID));
        assertNull(documentType.baseUri());
    }

    @Test
    public void testNodeName() {
        DocumentType documentType = new DocumentType("html", null, null, null);
        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testOuterHtmlHead() throws IOException {
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", null);

        Appendable accumulator = new StringBuffer();
        documentType.outerHtmlHead(accumulator, 0, Document.OutputSettings.syntaxHtml());

        assertEquals("<!doctype html\n", accumulator.toString());

        documentType.outerHtmlHead(accumulator, 0, Document.OutputSettings.syntaxXml());
        assertEquals("<!DOCTYPE html\n", accumulator.toString());

        documentType.outerHtmlHead(accumulator, 0, Document.OutputSettings.syntaxHtml(true));
        assertEquals("<!doctype html\n", accumulator.toString());

        documentType.outerHtmlHead(accumulator, 0, Document.OutputSettings.syntaxXml(true));
        assertEquals("<!DOCTYPE html\n", accumulator.toString());
    }

    @Test
    public void testHasAttribute() {
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", null);

        assertTrue(documentType.has(DocumentType.NAME));
        assertTrue(documentType.has(DocumentType.PUBLIC_ID));
        assertTrue(documentType.has(DocumentType.SYSTEM_ID));

        DocumentType documentTypeWithoutPublicId = new DocumentType("html", null, "systemId", null);
        assertFalse(documentTypeWithoutPublicId.has(DocumentType.PUBLIC_ID));

        DocumentType documentTypeWithoutSystemId = new DocumentType("html", "publicId", null, null);
        assertFalse(documentTypeWithoutSystemId.has(DocumentType.SYSTEM_ID));

        DocumentType documentTypeWithoutName = new DocumentType(null, "publicId", "systemId", null);
        assertFalse(documentTypeWithoutName.has(DocumentType.NAME));

        DocumentType documentTypeWithoutAny = new DocumentType(null, null, null, null);
        assertFalse(documentTypeWithoutAny.has(DocumentType.NAME));
        assertFalse(documentTypeWithoutAny.has(DocumentType.PUBLIC_ID));
        assertFalse(documentTypeWithoutAny.has(DocumentType.SYSTEM_ID));
    }
}