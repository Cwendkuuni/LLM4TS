package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testConstructor() {
        DocumentType docType = new DocumentType("DOCTYPE", "publicId", "systemId", "baseUri");
        
        assertEquals("DOCTYPE", docType.nodeName());
        assertEquals("DOCTYPE", docType.attr("name"));
        assertEquals("publicId", docType.attr("publicId"));
        assertEquals("systemId", docType.attr("systemId"));
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType docType = new DocumentType("DOCTYPE", "publicId", "systemId", "baseUri");
        
        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, new Document.OutputSettings());
        
        assertEquals("<!DOCTYPE DOCTYPE PUBLIC \"publicId\" \"systemId\">", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType docType = new DocumentType("DOCTYPE", "publicId", "systemId", "baseUri");
        
        StringBuilder accum = new StringBuilder();
        docType.outerHtmlTail(accum, 0, new Document.OutputSettings());
        
        assertTrue(accum.toString().isEmpty());
    }
}