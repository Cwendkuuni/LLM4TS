package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        DocumentType docType = new DocumentType("html", "publicId", "systemId", null);
        assertNotNull(docType);
        assertEquals("html", docType.attr("name"));
        assertEquals("publicId", docType.attr("publicId"));
        assertEquals("systemId", docType.attr("systemId"));
        assertEquals("#doctype", docType.nodeName());
    }

    @Test
    public void testDocumentTypeWithEmptyPublicIdAndSystemId() {
        DocumentType docType = new DocumentType("html", "", "", null);
        assertNotNull(docType);
        assertEquals("html", docType.attr("name"));
        assertNull(docType.attr("publicId"));
        assertNull(docType.attr("systemId"));
        assertEquals("#doctype", docType.nodeName());
    }

    @Test
    public void testDocumentTypeWithNullName() {
        try {
            new DocumentType(null, "", "", null);
            fail("Should have thrown an exception");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testDocumentTypeOuterHtmlHead() {
        DocumentType docType = new DocumentType("html", "publicId", "systemId", null);
        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);

        String expected = "<!DOCTYPE html PUBLIC \"publicId\"systemId>\n";
        assertTrue(accum.toString().equals(expected));
    }

    @Test
    public void testDocumentTypeOuterHtmlHeadWithEmptyPublicId() {
        DocumentType docType = new DocumentType("html", "", "systemId", null);
        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);

        String expected = "<!DOCTYPE html>\n";
        assertTrue(accum.toString().equals(expected));
    }

    @Test
    public void testDocumentTypeOuterHtmlHeadWithEmptySystemId() {
        DocumentType docType = new DocumentType("html", "publicId", "", null);
        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);

        String expected = "<!DOCTYPE html PUBLIC \"publicId\">\n";
        assertTrue(accum.toString().equals(expected));
    }

    @Test
    public void testDocumentTypeOuterHtmlHeadWithNullBaseUri() {
        DocumentType docType = new DocumentType("html", "publicId", "systemId", null);
        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);

        String expected = "<!DOCTYPE html PUBLIC \"publicId\"systemId>\n";
        assertTrue(accum.toString().equals(expected));

        DocumentType docTypeNullBaseUri = new DocumentType("html", "publicId", "systemId", null);
        StringBuilder accumNullBaseUri = new StringBuilder();
        docTypeNullBaseUri.outerHtmlHead(accumNullBaseUri, 0, null);

        String expectedNullBaseUri = "<!DOCTYPE html PUBLIC \"publicId\"systemId>\n";
        assertTrue(accumNullBaseUri.toString().equals(expectedNullBaseUri));
    }
}