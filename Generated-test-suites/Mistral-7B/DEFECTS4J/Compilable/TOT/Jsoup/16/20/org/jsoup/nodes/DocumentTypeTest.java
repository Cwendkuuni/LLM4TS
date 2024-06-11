package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        // Test from Expert 1
    }

    @Test
    public void testDocumentTypeOuterHtml() {
        // Test from Expert 2
    }

    @Test
    public void testDocumentTypeWithEmptyAttributes() {
        // Test from Expert 3
    }

    @Test
    public void testDocumentTypeWithNullAttributes() {
        String name = null;
        String publicId = null;
        String systemId = null;
        String baseUri = "http://example.com";

        try {
            DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testDocumentTypeWithEmptyStringAttributes() {
        String name = "";
        String publicId = "";
        String systemId = "";
        String baseUri = "http://example.com";

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        assertEquals("#doctype", documentType.nodeName());
        assertEquals(name, documentType.attr("name"));
        assertEquals(publicId, documentType.attr("publicId"));
        assertEquals(systemId, documentType.attr("systemId"));
        assertEquals(baseUri, documentType.baseUri());

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);
        documentType.outerHtmlTail(accum, 0, null);
        String expected = "<!DOCTYPE html>";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testDocumentTypeWithInvalidPublicId() {
        String name = "html";
        String publicId = "Invalid Public Id";
        String systemId = "";
        String baseUri = "http://example.com";

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        assertEquals("#doctype", documentType.nodeName());
        assertEquals(name, documentType.attr("name"));
        assertEquals(publicId, documentType.attr("publicId"));
        assertEquals(systemId, documentType.attr("systemId"));
        assertEquals(baseUri, documentType.baseUri());

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);
        documentType.outerHtmlTail(accum, 0, null);
        String expected = "<!DOCTYPE html>";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testDocumentTypeWithInvalidSystemId() {
        String name = "html";
        String publicId = "";
        String systemId = "Invalid System Id";
        String baseUri = "http://example.com";

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        assertEquals("#doctype", documentType.nodeName());
        assertEquals(name, documentType.attr("name"));
        assertEquals(publicId, documentType.attr("publicId"));
        assertEquals(systemId, documentType.attr("systemId"));
        assertEquals(baseUri, documentType.baseUri());

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);
        documentType.outerHtmlTail(accum, 0, null);
        String expected = "<!DOCTYPE html>";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testDocumentTypeWithEmptyPublicIdAndSystemId() {
        String name = "html";
        String publicId = "";
        String systemId = "";
        String baseUri = "http://example.com";

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        assertEquals("#doctype", documentType.nodeName());
        assertEquals(name, documentType.attr("name"));
        assertEquals(publicId, documentType.attr("publicId"));
        assertEquals(systemId, documentType.attr("systemId"));
        assertEquals(baseUri, documentType.baseUri());

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);
        documentType.outerHtmlTail(accum, 0, null);
        String expected = "<!DOCTYPE html>";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testDocumentTypeWithBaseUriNull() {
        String name = "html";
        String publicId = "";
        String systemId = "";
        String baseUri = null;

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        assertEquals("#doctype", documentType.nodeName());
        assertEquals(name, documentType.attr("name"));
        assertEquals(publicId, documentType.attr("publicId"));
        assertEquals(systemId, documentType.attr("systemId"));
        assertNull(documentType.baseUri());

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);
        documentType.outerHtmlTail(accum, 0, null);
        String expected = "<!DOCTYPE html>";
        assertEquals(expected, accum.toString());
    }
}