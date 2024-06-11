package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        DocumentType documentType = new DocumentType("html", "HTML TDG", null, null);
        assertEquals("#doctype", documentType.nodeName());
        assertEquals("html", documentType.attr(NAME));
        assertNull(documentType.attr(PUBLIC_ID));
        assertNull(documentType.attr(SYSTEM_ID));
    }

    @Test
    public void testDocumentTypeCreationWithPublicId() {
        DocumentType documentType = new DocumentType("html", "HTML TDG", "publicId", null);
        assertEquals("#doctype", documentType.nodeName());
        assertEquals("html", documentType.attr(NAME));
        assertEquals("publicId", documentType.attr(PUBLIC_ID));
        assertNull(documentType.attr(SYSTEM_ID));
    }

    @Test
    public void testDocumentTypeCreationWithSystemId() {
        DocumentType documentType = new DocumentType("html", "HTML TDG", null, "systemId");
        assertEquals("#doctype", documentType.nodeName());
        assertEquals("html", documentType.attr(NAME));
        assertNull(documentType.attr(PUBLIC_ID));
        assertEquals("systemId", documentType.attr(SYSTEM_ID));
    }

    @Test
    public void testDocumentTypeCreationWithPublicIdAndSystemId() {
        DocumentType documentType = new DocumentType("html", "HTML TDG", "publicId", "systemId");
        assertEquals("#doctype", documentType.nodeName());
        assertEquals("html", documentType.attr(NAME));
        assertEquals("publicId", documentType.attr(PUBLIC_ID));
        assertEquals("systemId", documentType.attr(SYSTEM_ID));
    }

    @Test
    public void testHasAttribute() {
        DocumentType documentType = new DocumentType("html", "HTML TDG", null, null);
        assertTrue(documentType.has(NAME));
        assertTrue(documentType.has(PUBLIC_ID));
        assertTrue(documentType.has(SYSTEM_ID));

        DocumentType documentTypeNoName = new DocumentType(null, null, null, null);
        assertFalse(documentTypeNoName.has(NAME));
        assertFalse(documentTypeNoName.has(PUBLIC_ID));
        assertFalse(documentTypeNoName.has(SYSTEM_ID));
    }

    @Test
    public void testOuterHtmlHead() throws IOException {
        DocumentType documentType = new DocumentType("html", "HTML TDG", null, null);
        StringBuilder sb = new StringBuilder();
        documentType.outerHtmlHead(sb, 0, Document.OutputSettings.syntax(Syntax.html, null, null, null, null, null));
        assertEquals("<!DOCTYPE html>", sb.toString());

        DocumentType documentTypePublic = new DocumentType("html", "HTML TDG", "publicId", null);
        StringBuilder sbPublic = new StringBuilder();
        documentTypePublic.outerHtmlHead(sbPublic, 0, Document.OutputSettings.syntax(Syntax.html, null, null, null, null, null));
        assertEquals("<!DOCTYPE html PUBLIC \"publicId\" >", sbPublic.toString());

        DocumentType documentTypeSystem = new DocumentType("html", "HTML TDG", null, "systemId");
        StringBuilder sbSystem = new StringBuilder();
        documentTypeSystem.outerHtmlHead(sbSystem, 0, Document.OutputSettings.syntax(Syntax.html, null, null, null, null, null));
        assertEquals("<!DOCTYPE html SYSTEM \"systemId\" >", sbSystem.toString());

        DocumentType documentTypePublicSystem = new DocumentType("html", "HTML TDG", "publicId", "systemId");
        StringBuilder sbPublicSystem = new StringBuilder();
        documentTypePublicSystem.outerHtmlHead(sbPublicSystem, 0, Document.OutputSettings.syntax(Syntax.html, null, null, null, null, null));
        assertEquals("<!DOCTYPE html PUBLIC \"publicId\" SYSTEM \"systemId\" >", sbPublicSystem.toString());
    }
}