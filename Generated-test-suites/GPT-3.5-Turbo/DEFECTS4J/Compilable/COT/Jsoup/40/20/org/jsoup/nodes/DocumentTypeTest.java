package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {
    @Test
    public void testConstructor() {
        // Standard test case
        DocumentType docType = new DocumentType("name", "publicId", "systemId", "baseUri");
        assertNotNull(docType);
        assertEquals("name", docType.attr("name"));
        assertEquals("publicId", docType.attr("publicId"));
        assertEquals("systemId", docType.attr("systemId"));

        // Edge case: Empty name
        try {
            docType = new DocumentType("", "publicId", "systemId", "baseUri");
            fail("Expected exception not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("name must not be empty", e.getMessage());
        }

        // Edge case: Empty publicId
        try {
            docType = new DocumentType("name", "", "systemId", "baseUri");
            fail("Expected exception not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("publicId must not be empty", e.getMessage());
        }

        // Edge case: Empty systemId
        try {
            docType = new DocumentType("name", "publicId", "", "baseUri");
            fail("Expected exception not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("systemId must not be empty", e.getMessage());
        }

        // Edge case: Empty baseUri
        docType = new DocumentType("name", "publicId", "systemId", "");

        assertNotNull(docType);
        assertEquals("name", docType.attr("name"));
        assertEquals("publicId", docType.attr("publicId"));
        assertEquals("systemId", docType.attr("systemId"));
    }

    @Test
    public void testNodeName() {
        DocumentType docType = new DocumentType("name", "publicId", "systemId", "baseUri");

        assertEquals("#doctype", docType.nodeName());
    }
}
