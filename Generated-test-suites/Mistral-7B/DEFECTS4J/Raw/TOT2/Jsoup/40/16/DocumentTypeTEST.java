package org.jsoup.nodes; // imports same as in the given class

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class DocumentTypeTest { // Test class name

    @Test
    public void testDocumentTypeCreation() {
        String name = "html";
        String publicId = "–‐‐‐‐‐‐‐‐‐‐‐‐‐‐‐‐‐‐‐‐‐‐‐‐‐‐‐";
        String systemId = "W3C HTML 4.01 Frameset";
        String baseUri = "http://example.com/";

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri); // Creating a new DocumentType object

        assertEquals("#doctype", documentType.nodeName()); // Checking node name
        assertNotNull(documentType.attr("name")); // Checking attribute existence
        assertEquals(name, documentType.attr("name")); // Checking attribute value
        assertNotNull(documentType.attr("publicId")); // Checking attribute existence
        assertEquals(publicId, documentType.attr("publicId")); // Checking attribute value
        assertNotNull(documentType.attr("systemId")); // Checking attribute existence
        assertEquals(systemId, documentType.attr("systemId")); // Checking attribute value
        assertEquals(baseUri, documentType.baseUri()); // Checking base URI
    }
}