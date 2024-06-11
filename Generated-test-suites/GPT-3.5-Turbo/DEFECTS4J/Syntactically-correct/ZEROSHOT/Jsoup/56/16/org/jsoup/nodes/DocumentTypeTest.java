package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.junit.Test;

public class DocumentTypeTest {
    
    @Test
    public void testNodeName() {
        DocumentType documentType = new DocumentType("doctype", "publicId", "systemId", "baseUri");
        assertEquals("#doctype", documentType.nodeName());
    }
    
    @Test
    public void testOuterHtmlHead() {
        DocumentType documentType = new DocumentType("doctype", "publicId", "systemId", "baseUri");
        Appendable accum;
        int depth;
        Document.OutputSettings out;
        // Test case for html syntax
        out = new Document.OutputSettings();
        out.syntax(Document.OutputSettings.Syntax.html);
        accum = new StringBuilder();
        depth = 0;
        try {
            documentType.outerHtmlHead(accum, depth, out);
            assertEquals("<!doctype", accum.toString());
        } catch (Exception e) {
            fail(e.getMessage());
        }
        // Test case for non-html syntax
        out = new Document.OutputSettings();
        accum = new StringBuilder();
        depth = 0;
        try {
            documentType.outerHtmlHead(accum, depth, out);
            assertEquals("<!DOCTYPE", accum.toString());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testOuterHtmlTail() {
        DocumentType documentType = new DocumentType("doctype", "publicId", "systemId", "baseUri");
        Appendable accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();
        documentType.outerHtmlTail(accum, depth, out);
        assertEquals("", accum.toString());
    }
    
    @Test
    public void testHas() {
        DocumentType documentType = new DocumentType("doctype", "publicId", "systemId", "baseUri");
        assertTrue(documentType.has("name"));
        assertTrue(documentType.has("publicId"));
        assertTrue(documentType.has("systemId"));
        assertFalse(documentType.has("nonexistentAttribute"));
    }
}