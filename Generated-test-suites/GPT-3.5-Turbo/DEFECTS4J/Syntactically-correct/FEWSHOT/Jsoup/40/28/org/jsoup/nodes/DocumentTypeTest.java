package org.jsoup.nodes;

import org.jsoup.helper.StringUtil;
import org.jsoup.helper.Validate;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {
    @Test
    public void testConstructor() {
        DocumentType dt = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "https://www.example.com");
        
        assertEquals("html", dt.attr("name"));
        assertEquals("-//W3C//DTD HTML 4.01//EN", dt.attr("publicId"));
        assertEquals("http://www.w3.org/TR/html4/strict.dtd", dt.attr("systemId"));
        assertEquals("https://www.example.com", dt.baseUri());
    }
    
    @Test
    public void testNodeName() {
        DocumentType dt = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "https://www.example.com");
        
        assertEquals("#doctype", dt.nodeName());
    }
    
    @Test
    public void testOuterHtmlHead() {
        DocumentType dt = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "https://www.example.com");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        
        dt.outerHtmlHead(accum, 0, out);
        
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">", accum.toString());
    }
    
    @Test
    public void testOuterHtmlTail() {
        DocumentType dt = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "https://www.example.com");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        
        dt.outerHtmlTail(accum, 0, out);
        
        assertEquals("", accum.toString());
    }
}