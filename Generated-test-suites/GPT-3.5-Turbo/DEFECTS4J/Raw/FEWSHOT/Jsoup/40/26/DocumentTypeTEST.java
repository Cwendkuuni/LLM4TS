package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class DocumentTypeTest {
    @Test
    public void testConstructor() {
        DocumentType docType = new DocumentType("doctype", "publicId", "systemId", "baseUri");
        
        assertEquals("doctype", docType.nodeName());
        assertEquals("doctype", docType.attr("name"));
        assertEquals("publicId", docType.attr("publicId"));
        assertEquals("systemId", docType.attr("systemId"));
        assertEquals("baseUri", docType.baseUri());
    }
    
    @Test
    public void testOuterHtmlHead() {
        DocumentType docType = new DocumentType("doctype", "publicId", "systemId", "baseUri");
        
        StringBuilder expected = new StringBuilder();
        expected.append("<!DOCTYPE doctype PUBLIC \"publicId\" \"systemId\">");
        
        StringBuilder actual = new StringBuilder();
        docType.outerHtmlHead(actual, 0, new Document.OutputSettings());
        
        assertEquals(expected.toString(), actual.toString());
    }
    
    @Test
    public void testOuterHtmlTail() {
        DocumentType docType = new DocumentType("doctype", "publicId", "systemId", "baseUri");
        
        StringBuilder expected = new StringBuilder();
        
        StringBuilder actual = new StringBuilder();
        docType.outerHtmlTail(actual, 0, new Document.OutputSettings());
        
        assertEquals(expected.toString(), actual.toString());
    }
}