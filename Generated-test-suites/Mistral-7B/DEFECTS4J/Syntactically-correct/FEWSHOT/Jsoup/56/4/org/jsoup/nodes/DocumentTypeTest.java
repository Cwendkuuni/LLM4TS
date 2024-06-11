package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        DocumentType documentType = new DocumentType("html", "HTMLpublicID", "HTMLsystemID", null);
        assertEquals("#doctype", documentType.nodeName());
        assertEquals("html", documentType.attr(NAME));
        assertEquals("HTMLpublicID", documentType.attr(PUBLIC_ID));
        assertEquals("HTMLsystemID", documentType.attr(SYSTEM_ID));
    }

    @Test
    public void testDocumentTypeCreationWithEmptyAttributes() {
        DocumentType documentType = new DocumentType("html", "", "", null);
        assertEquals("#doctype", documentType.nodeName());
        assertNull(documentType.attr(PUBLIC_ID));
        assertNull(documentType.attr(SYSTEM_ID));
    }

    @Test
    public void testOuterHtmlHead() throws IOException {
        DocumentType documentType = new DocumentType("html", "HTMLpublicID", "HTMLsystemID", null);
        Appendable accum = new StringBuilder();

        documentType.outerHtmlHead(accum, 0, Document.OutputSettings.syntaxHtml());

        assertEquals("<!doctype html PUBLIC \"HTMLpublicID\" \"HTMLsystemID\">", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithoutPublicIdAndSystemId() throws IOException {
        DocumentType documentType = new DocumentType("html", "", "", null);
        Appendable accum = new StringBuilder();

        documentType.outerHtmlHead(accum, 0, Document.OutputSettings.syntaxHtml());

        assertEquals("<!doctype html>", accum.toString());
    }

    @Test
    public void testHasAttribute() {
        DocumentType documentType = new DocumentType("html", "HTMLpublicID", "HTMLsystemID", null);

        assertTrue(documentType.has(NAME));
        assertTrue(documentType.has(PUBLIC_ID));
        assertTrue(documentType.has(SYSTEM_ID));

        DocumentType documentTypeWithEmptyName = new DocumentType("", "HTMLpublicID", "HTMLsystemID", null);

        assertFalse(documentTypeWithEmptyName.has(NAME));

        DocumentType documentTypeWithoutPublicId = new DocumentType("html", "", "HTMLsystemID", null);

        assertFalse(documentTypeWithoutPublicId.has(PUBLIC_ID));

        DocumentType documentTypeWithoutSystemId = new DocumentType("html", "HTMLpublicID", "", null);

        assertFalse(documentTypeWithoutSystemId.has(SYSTEM_ID));

        DocumentType documentTypeWithNullBaseUri = new DocumentType("html", "HTMLpublicID", "HTMLsystemID", null);

        assertFalse(documentTypeWithNullBaseUri.has(SYSTEM_ID));
    }
}