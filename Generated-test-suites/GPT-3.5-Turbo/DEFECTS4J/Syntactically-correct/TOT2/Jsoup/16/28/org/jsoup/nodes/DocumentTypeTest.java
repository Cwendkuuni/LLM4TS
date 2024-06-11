package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.junit.Test;

public class DocumentTypeTest {
    
    @Test
    public void testConstructor() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Transitional//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-trans.dtd", "https://www.w3.org");
        assertEquals("html", doctype.attr("name"));
        assertEquals("-//W3C//DTD XHTML 1.0 Transitional//EN", doctype.attr("publicId"));
        assertEquals("http://www.w3.org/TR/xhtml1/DTD/xhtml1-trans.dtd", doctype.attr("systemId"));
        assertEquals("https://www.w3.org", doctype.baseUri());
    }
    
    @Test
    public void testNodeName() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Transitional//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-trans.dtd", "https://www.w3.org");
        assertEquals("#doctype", doctype.nodeName());
    }
    
    @Test
    public void testOuterHtmlHead() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Transitional//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-trans.dtd", "https://www.w3.org");
        StringBuilder accum = new StringBuilder();
        doctype.outerHtmlHead(accum, 0, new Document.OutputSettings());
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" http://www.w3.org/TR/xhtml1/DTD/xhtml1-trans.dtd\">", accum.toString());
    }
    
    @Test
    public void testOuterHtmlTail() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Transitional//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-trans.dtd", "https://www.w3.org");
        StringBuilder accum = new StringBuilder();
        doctype.outerHtmlTail(accum, 0, new Document.OutputSettings());
        assertEquals("", accum.toString());
    }
    
}