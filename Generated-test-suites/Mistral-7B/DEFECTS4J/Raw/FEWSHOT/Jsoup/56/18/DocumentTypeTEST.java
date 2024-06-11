package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        DocumentType docType = new DocumentType("html", "publicID", "systemID", null);
        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr(NAME));
        assertNotNull(docType.attr(PUBLIC_ID));
        assertNotNull(docType.attr(SYSTEM_ID));
        assertNull(docType.baseUri());
    }

    @Test
    public void testDocumentTypeCreationWithEmptyAttributes() {
        DocumentType docType = new DocumentType("html", "", "", null);
        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr(NAME));
        assertNull(docType.attr(PUBLIC_ID));
        assertNull(docType.attr(SYSTEM_ID));
        assertNull(docType.baseUri());
    }

    @Test
    public void testDocumentTypeCreationWithoutAttributes() {
        DocumentType docType = new DocumentType("html", null, null, null);
        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr(NAME));
        assertNull(docType.attr(PUBLIC_ID));
        assertNull(docType.attr(SYSTEM_ID));
        assertNull(docType.baseUri());
    }

    @Test
    public void testDocumentTypeStringRepresentation() {
        DocumentType docType = new DocumentType("html", "publicID", "systemID", null);
        assertEquals("html", docType.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlHead() throws IOException {
        DocumentType docType = new DocumentType("html", "publicID", "systemID", null);
        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, Document.OutputSettings.syntax(Syntax.html, true));
        assertEquals("<!doctype html PUBLIC \"publicID\" systemID>", accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlHeadWithEmptyAttributes() throws IOException {
        DocumentType docType = new DocumentType("html", "", "", null);
        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, Document.OutputSettings.syntax(Syntax.html, true));
        assertEquals("<!doctype html>", accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlHeadWithNoSyntaxSpecified() throws IOException {
        DocumentType docType = new DocumentType("html", "publicID", "systemID", null);
        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);
        assertEquals("<!DOCTYPE html PUBLIC \"publicID\" systemID>", accum.toString());
    }

    @Test
    public void testDocumentTypeHasAttribute() {
        DocumentType docType = new DocumentType("html", "publicID", "systemID", null);
        assertTrue(docType.has(NAME));
        assertTrue(docType.has(PUBLIC_ID));
        assertTrue(docType.has(SYSTEM_ID));

        DocumentType docTypeWithoutPublicId = new DocumentType("html", "", "systemID", null);
        assertTrue(docTypeWithoutPublicId.has(NAME));
        assertFalse(docTypeWithoutPublicId.has(PUBLIC_ID));
        assertTrue(docTypeWithoutPublicId.has(SYSTEM_ID));

        DocumentType docTypeWithoutSystemId = new DocumentType("html", "publicID", "", null);
        assertTrue(docTypeWithoutSystemId.has(NAME));
        assertTrue(docTypeWithoutSystemId.has(PUBLIC_ID));
        assertFalse(docTypeWithoutSystemId.has(SYSTEM_ID));

        DocumentType docTypeWithoutName = new DocumentType("", "publicID", "systemID", null);
        assertFalse(docTypeWithoutName.has(NAME));
        assertTrue(docTypeWithoutName.has(PUBLIC_ID));
        assertTrue(docTypeWithoutName.has(SYSTEM_ID));

        DocumentType docTypeWithoutAnyAttribute = new DocumentType("", "", "", null);
        assertFalse(docTypeWithoutAnyAttribute.has(NAME));
        assertFalse(docTypeWithoutAnyAttribute.has(PUBLIC_ID));
        assertFalse(docTypeWithoutAnyAttribute.has(SYSTEM_ID));
    }

    @Test
    public void testDocumentTypeAttributesAreEqual() {
        DocumentType docType1 = new DocumentType("html", "publicID1", "systemID1", null);
        DocumentType docType2 = new DocumentType("html", "publicID2", "systemID2", null);

        assertNotSame(docType1, docType2);

        DocumentType docType3 = new DocumentType("html", "publicID1", "systemID1", null);
        assertEquals(docType1, docType3);

        DocumentType docType4 = new DocumentType("html", "publicID1", "systemID1", null);
        docType4.attr(NAME, "html5");
        assertNotEquals(docType1, docType4);
    }
}