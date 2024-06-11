package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DocumentTypeTest {
    @Test
    public void testConstructor() {
        DocumentType dt = new DocumentType("name", "publicId", "systemId", "baseUri");
        
        assertEquals("name", dt.attr("name"));
        assertEquals("publicId", dt.attr("publicId"));
        assertEquals("systemId", dt.attr("systemId"));
    }
    
    @Test
    public void testNodeName() {
        DocumentType dt = new DocumentType("name", "publicId", "systemId", "baseUri");
        assertEquals("#doctype", dt.nodeName());
    }
    
    @Test
    public void testOuterHtmlHead() {
        DocumentType dt = new DocumentType("name", "publicId", "systemId", "baseUri");
        
        // Test when syntax is html and both publicId and systemId are not present
        Document.OutputSettings out = new Document.OutputSettings();
        out.setSyntax(Document.OutputSettings.Syntax.html);
        
        StringBuilder accum = new StringBuilder();
        dt.outerHtmlHead(accum, 0, out);
        assertEquals("<!doctype", accum.toString());
        
        // Test when syntax is not html or either publicId or systemId is present
        out.setSyntax(Document.OutputSettings.Syntax.xml);
        dt.attr(DocumentType.PUBLIC_ID, "publicId");
        dt.attr(DocumentType.SYSTEM_ID, "systemId");

        accum = new StringBuilder();
        dt.outerHtmlHead(accum, 0, out);
        assertEquals("<!DOCTYPE name PUBLIC \"publicId\" \"systemId\">", accum.toString());
    }
    
    @Test
    public void testOuterHtmlTail() {
        DocumentType dt = new DocumentType("name", "publicId", "systemId", "baseUri");
        
        StringBuilder accum = new StringBuilder();
        dt.outerHtmlTail(accum, 0, new Document.OutputSettings());
        assertEquals("", accum.toString());
    }
    
    @Test
    public void testHas() {
        DocumentType dt = new DocumentType("name", "publicId", "systemId", "baseUri");
        
        assertFalse(dt.has(DocumentType.PUBLIC_ID));
        assertFalse(dt.has(DocumentType.SYSTEM_ID));
        
        dt.attr(DocumentType.PUBLIC_ID, "publicId");
        assertTrue(dt.has(DocumentType.PUBLIC_ID));
        
        dt.attr(DocumentType.SYSTEM_ID, "systemId");
        assertTrue(dt.has(DocumentType.SYSTEM_ID));
    }
}