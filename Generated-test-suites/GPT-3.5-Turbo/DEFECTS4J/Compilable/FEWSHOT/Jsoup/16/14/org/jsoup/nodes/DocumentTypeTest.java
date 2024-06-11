package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DocumentTypeTest {
    
    @Test
    public void testConstructor() {
        DocumentType documentType = new DocumentType("doctype", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "https://www.example.com");
        
        assertEquals("doctype", documentType.attr("name"));
        assertEquals("-//W3C//DTD HTML 4.01//EN", documentType.attr("publicId"));
        assertEquals("http://www.w3.org/TR/html4/strict.dtd", documentType.attr("systemId"));
        assertEquals("https://www.example.com", documentType.baseUri());
    }
    
    @Test
    public void testNodeName() {
        DocumentType documentType = new DocumentType("doctype", null, null, null);
        
        assertEquals("#doctype", documentType.nodeName());
    }
    
    @Test
    public void testOuterHtmlHead() {
        DocumentType documentType1 = new DocumentType("doctype",
            "-//W3C//DTD HTML 4.01 Transitional//EN",
            "http://www.w3.org/TR/html4/loose.dtd", null);
        StringBuilder accumulator1 = new StringBuilder();
        Document.OutputSettings outputSettings1 = new Document.OutputSettings();
        
        documentType1.outerHtmlHead(accumulator1, 0, outputSettings1);
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" http://www.w3.org/TR/html4/loose.dtd\">", accumulator1.toString());
        
        DocumentType documentType2 = new DocumentType("doctype",
            "-//W3C//DTD HTML 4.01//EN",
            "http://www.w3.org/TR/html4/strict.dtd", null);
        StringBuilder accumulator2 = new StringBuilder();
        Document.OutputSettings outputSettings2 = new Document.OutputSettings();
        
        documentType2.outerHtmlHead(accumulator2, 0, outputSettings2);
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" http://www.w3.org/TR/html4/strict.dtd\">", accumulator2.toString());
    }
    
    @Test
    public void testOuterHtmlTail() {
        DocumentType documentType = new DocumentType("doctype", null, null, null);
        StringBuilder accumulator = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        
        documentType.outerHtmlTail(accumulator, 0, outputSettings);
        
        assertEquals("", accumulator.toString());
    }
}