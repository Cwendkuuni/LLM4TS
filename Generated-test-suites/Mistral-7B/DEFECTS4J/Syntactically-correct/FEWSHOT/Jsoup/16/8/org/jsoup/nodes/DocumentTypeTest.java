package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        DocumentType documentType = new DocumentType("html", "HTML_PUBLIC_ID", "HTML_SYSTEM_ID", "/baseUri");
        assertNotNull(documentType);
        assertEquals("html", documentType.nodeName());
        assertEquals("HTML_PUBLIC_ID", documentType.attr("publicId"));
        assertEquals("HTML_SYSTEM_ID", documentType.attr("systemId"));
        assertEquals("/baseUri", documentType.baseUri());
    }

    @Test
    public void testEmptyDocumentTypeCreation() {
        DocumentType documentType = new DocumentType("html", "", "", "");
        assertNotNull(documentType);
        assertEquals("html", documentType.nodeName());
        assertNull(documentType.attr("publicId"));
        assertNull(documentType.attr("systemId"));
        assertNull(documentType.baseUri());
    }

    @Test
    public void testDocumentTypeOuterHtmlHead() {
        DocumentType documentType = new DocumentType("html", "HTML_PUBLIC_ID", "HTML_SYSTEM_ID", "/baseUri");
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, new Document.OutputSettings());
        assertEquals("<!DOCTYPE html PUBLIC \"HTML_PUBLIC_ID\" SYSTEM \"HTML_SYSTEM_ID\">", accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlHeadWithEmptyPublicId() {
        DocumentType documentType = new DocumentType("html", "", "HTML_SYSTEM_ID", "/baseUri");
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, new Document.OutputSettings());
        assertEquals("<!DOCTYPE html SYSTEM \"HTML_SYSTEM_ID\">", accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlHeadWithEmptySystemId() {
        DocumentType documentType = new DocumentType("html", "HTML_PUBLIC_ID", "", "/baseUri");
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, new Document.OutputSettings());
        assertEquals("<!DOCTYPE html PUBLIC \"HTML_PUBLIC_ID\">", accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlHeadWithBothEmpty() {
        DocumentType documentType = new DocumentType("html", "", "", "");
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, new Document.OutputSettings());
        assertSame("", accum.toString());
    }
}