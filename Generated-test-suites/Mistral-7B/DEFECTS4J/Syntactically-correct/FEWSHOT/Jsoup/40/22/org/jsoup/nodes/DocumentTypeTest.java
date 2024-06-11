package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.jsoup.helper.StringUtil.isBlank;

import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        String name = "html";
        String publicId = "publicId";
        String systemId = "systemId";
        String baseUri = "baseUri";

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        assertNotNull(documentType);
        assertEquals(name, documentType.attr("name"));
        assertEquals(publicId, documentType.attr("publicId"));
        assertEquals(systemId, documentType.attr("systemId"));
        assertEquals(baseUri, documentType.baseUri());
    }

    @Test
    public void testDocumentTypeCreationWithEmptyValues() {
        String name = "";
        String publicId = "";
        String systemId = "";
        String baseUri = "";

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        assertNotNull(documentType);
        assertEquals(name, documentType.attr("name"));
        assertNull(documentType.attr("publicId"));
        assertNull(documentType.attr("systemId"));
        assertNull(documentType.baseUri());
    }

    @Test
    public void testDocumentTypeCreationWithNullValues() {
        String name = null;
        String publicId = null;
        String systemId = null;
        String baseUri = null;

        try {
            new DocumentType(name, publicId, systemId, baseUri);
            assert false : "Exception should have been thrown.";
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testDocumentTypeNodeName() {
        DocumentType documentType = new DocumentType("html", "", "", "");

        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testDocumentTypeOuterHtmlHead() {
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", "");

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html PUBLIC \"publicId\"systemId>\n", accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlHeadWithEmptyValues() {
        DocumentType documentType = new DocumentType("html", "", "", "");

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html>\n", accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlHeadWithNullPublicIdAndSystemId() {
        DocumentType documentType = new DocumentType("html", null, null, "");

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html>\n", accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlTail() {
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", "");

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlTail(accum, 0, null);

        assertEquals("", accum.toString());
    }
}