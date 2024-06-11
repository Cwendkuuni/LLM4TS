package org.jsoup.nodes;
import org.junit.Test;

import static org.junit.Assert.*;

public class DocumentTypeTest {

    @Test
    public void testNodeName() {
        DocumentType documentType = new DocumentType("doctype", "publicId", "systemId", "baseUri");
        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType documentType = new DocumentType("doctype", "publicId", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();

        documentType.outerHtmlHead(accum, 0, out);
        assertEquals("<!DOCTYPE doctype PUBLIC \"publicId\" \"systemId\">", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType documentType = new DocumentType("doctype", "publicId", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();

        documentType.outerHtmlTail(accum, 0, out);
        assertEquals("", accum.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyNameThrowsIllegalArgumentException() {
        new DocumentType("", "publicId", "systemId", "baseUri");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullNameThrowsIllegalArgumentException() {
        new DocumentType(null, "publicId", "systemId", "baseUri");
    }
    
    // Additional test cases for edge cases and exceptions can be added here
    
}