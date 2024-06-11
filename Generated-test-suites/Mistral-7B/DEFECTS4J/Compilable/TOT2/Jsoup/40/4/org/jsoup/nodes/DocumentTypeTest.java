package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.junit.Test;

public class DocumentTypeTest {
    // Test case for constructor
    @Test
    public void testDocumentTypeConstruction() {
        DocumentType docType = new DocumentType("html", "HTML", "HTML5", null);
        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr("name"));
        assertEquals("HTML", docType.attr("publicId"));
        assertEquals("HTML5", docType.attr("systemId"));
        assertNull(docType.attr("quirkMode"));
    }

    // Test case for nodeName method
    @Test
    public void testNodeName() {
        DocumentType docType = new DocumentType("html", "HTML", "HTML5", null);
        assertEquals("#doctype", docType.nodeName());
    }
}