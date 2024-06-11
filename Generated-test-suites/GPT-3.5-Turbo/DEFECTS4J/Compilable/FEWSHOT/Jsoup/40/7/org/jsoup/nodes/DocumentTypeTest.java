package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class DocumentTypeTest {
    @Test
    public void testConstructor() {
        DocumentType dt = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Transitional//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd");
        
        assertEquals("html", dt.attr("name"));
        assertEquals("-//W3C//DTD XHTML 1.0 Transitional//EN", dt.attr("publicId"));
        assertEquals("http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", dt.attr("systemId"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorEmptyName() {
        DocumentType dt = new DocumentType("", "-//W3C//DTD XHTML 1.0 Transitional//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd");
    }
    
    @Test
    public void testNodeName() {
        DocumentType dt = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Transitional//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd");
        
        assertEquals("#doctype", dt.nodeName());
    }
    
    @Test
    public void testOuterHtmlHead() {
        DocumentType dt = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Transitional//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd");
        
        StringBuilder accum = new StringBuilder();
        dt.outerHtmlHead(accum, 0, null);
        
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">", accum.toString());
    }
    
    @Test
    public void testOuterHtmlTail() {
        DocumentType dt = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Transitional//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd");
        
        StringBuilder accum = new StringBuilder();
        dt.outerHtmlTail(accum, 0, null);
        
        assertEquals("", accum.toString());
    }
}