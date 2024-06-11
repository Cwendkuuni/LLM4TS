package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.junit.Test;

public class DocumentTypeTest {
    @Test
    public void testConstructor() {
        DocumentType docType = new DocumentType("html", "-//W3C//DTD HTML 4.01 Transitional//EN", "http://www.w3.org/TR/html4/loose.dtd", "http://example.com");
        
        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr("name"));
        assertEquals("-//W3C//DTD HTML 4.01 Transitional//EN", docType.attr("publicId"));
        assertEquals("http://www.w3.org/TR/html4/loose.dtd", docType.attr("systemId"));
    }
    
    @Test
    public void testOuterHtmlHead() {
        DocumentType docType = new DocumentType("html", "-//W3C//DTD HTML 4.01 Transitional//EN", "http://www.w3.org/TR/html4/loose.dtd", "http://example.com");
        
        StringBuilder sb = new StringBuilder();
        docType.outerHtmlHead(sb, 0, new Document.OutputSettings(Syntax.html));
        
        assertEquals("<!doctype html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">", sb.toString());
    }
    
    @Test
    public void testOuterHtmlHead_Html5() {
        DocumentType docType = new DocumentType("html", null, null, "http://example.com");
        
        StringBuilder sb = new StringBuilder();
        docType.outerHtmlHead(sb, 0, new Document.OutputSettings(Syntax.html));
        
        assertEquals("<!doctype html>", sb.toString());
    }
    
    @Test
    public void testOuterHtmlHead_Xml() {
        DocumentType docType = new DocumentType("xml", "-//W3C//DTD XML 1.0//EN", "http://www.w3.org/TR/xml1/DTD/xml1.dtd", "http://example.com");
        
        StringBuilder sb = new StringBuilder();
        docType.outerHtmlHead(sb, 0, new Document.OutputSettings(Syntax.xml));
        
        assertEquals("<!DOCTYPE xml PUBLIC \"-//W3C//DTD XML 1.0//EN\" \"http://www.w3.org/TR/xml1/DTD/xml1.dtd\">", sb.toString());
    }
    
    @Test
    public void testOuterHtmlTail() {
        DocumentType docType = new DocumentType("html", "-//W3C//DTD HTML 4.01 Transitional//EN", "http://www.w3.org/TR/html4/loose.dtd", "http://example.com");
        
        StringBuilder sb = new StringBuilder();
        docType.outerHtmlTail(sb, 0, new Document.OutputSettings(Syntax.html));
        
        assertEquals("", sb.toString());
    }
    
    @Test
    public void testHas() {
        DocumentType docType = new DocumentType("html", "-//W3C//DTD HTML 4.01 Transitional//EN", "http://www.w3.org/TR/html4/loose.dtd", "http://example.com");
        
        assertTrue(docType.has("name"));
        assertTrue(docType.has("publicId"));
        assertTrue(docType.has("systemId"));
        assertFalse(docType.has("missingAttribute"));
    }
}