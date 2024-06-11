package org.jsoup.nodes;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {

    @Test
    public void testNodeName() {
        DocumentType documentType = new DocumentType("name", "publicId", "systemId", "baseUri");
        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType documentType = new DocumentType("name", "publicId", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        documentType.outerHtmlHead(accum, 0, out);
        assertEquals("<!DOCTYPE html PUBLIC \"publicId\" systemId\">", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType documentType = new DocumentType("name", "publicId", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        documentType.outerHtmlTail(accum, 0, out);
        assertEquals("", accum.toString());
    }
  
    // Potential edge cases and exception handling scenarios:
    // 1. Edge case: Empty name
    // 2. Edge case: Empty publicId
    // 3. Edge case: Empty systemId
    // 4. Edge case: Empty baseUri
    // 5. Exception: Invalid publicId format
    // 6. Exception: Invalid systemId format

    @Test
    public void testEmptyName() {
        DocumentType documentType = new DocumentType("", "publicId", "systemId", "baseUri");
        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testEmptyPublicId() {
        DocumentType documentType = new DocumentType("name", "", "systemId", "baseUri");
        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testEmptySystemId() {
        DocumentType documentType = new DocumentType("name", "publicId", "", "baseUri");
        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testEmptyBaseUri() {
        DocumentType documentType = new DocumentType("name", "publicId", "systemId", "");
        assertEquals("#doctype", documentType.nodeName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPublicIdFormat() {
        DocumentType documentType = new DocumentType("name", "invalid", "systemId", "baseUri");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidSystemIdFormat() {
        DocumentType documentType = new DocumentType("name", "publicId", "invalid", "baseUri");
    }

}