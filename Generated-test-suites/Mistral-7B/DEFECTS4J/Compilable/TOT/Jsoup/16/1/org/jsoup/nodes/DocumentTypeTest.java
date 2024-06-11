package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.nodes.DocumentType;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        String name = "html";
        String publicId = "PUBLIC";
        String systemId = "system.id";
        String baseUri = "baseUri";

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        assertEquals("#doctype", documentType.nodeName());
        assertEquals(name, documentType.attr("name"));
        assertEquals(publicId, documentType.attr("publicId"));
        assertEquals(systemId, documentType.attr("systemId"));
        assertEquals(baseUri, documentType.baseUri());
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType documentType = new DocumentType("html", "PUBLIC \"test\"", "test.dtd", "baseUri");
        StringBuilder accumulator = new StringBuilder();
        documentType.outerHtmlHead(accumulator, 0, null);

        String expected = "<!DOCTYPE html PUBLIC \"test\" system-id=\"test.dtd\">";
        assertEquals(expected, accumulator.toString());
    }

    @Test
    public void testEmptyAttributes() {
        DocumentType documentType = new DocumentType("html", "", "", "");

        assertEquals("#doctype", documentType.nodeName());
        assertNull(documentType.attr("publicId"));
        assertNull(documentType.attr("systemId"));
        assertNull(documentType.baseUri());
    }

    @Test
    public void testNullValues() {
        DocumentType documentType = new DocumentType("html", null, null, null);

        assertEquals("#doctype", documentType.nodeName());
        assertNull(documentType.attr("publicId"));
        assertNull(documentType.attr("systemId"));
        assertNull(documentType.baseUri());
    }

    @Test
    public void testErrorScenario() {
        try {
            new DocumentType("", null, null, null);
            fail("Exception expected");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testErrorScenario2() {
        DocumentType documentType = new DocumentType("html", null, null, null);
        documentType.outerHtmlHead(null, 0, null);
        fail("NullPointerException expected");
    }
}