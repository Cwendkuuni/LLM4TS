package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.jsoup.nodes.Document.OutputSettings;
import org.junit.Test;

public class DocumentTypeTest {
    
    @Test
    public void testDocumentTypeConstructor() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Strict//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd", "http://www.example.com");
        assertEquals("html", doctype.attr("name"));
        assertEquals("-//W3C//DTD XHTML 1.0 Strict//EN", doctype.attr("publicId"));
        assertEquals("http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd", doctype.attr("systemId"));
        assertEquals("http://www.example.com", doctype.baseUri());
    }
    
    @Test
    public void testNodeName() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Strict//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd", "http://www.example.com");
        assertEquals("#doctype", doctype.nodeName());
    }
    
    @Test
    public void testOuterHtmlHeadWithBlankAttr() {
        DocumentType doctype = new DocumentType("", "", "", "http://www.example.com");
        StringBuilder accum = new StringBuilder();
        OutputSettings out = new Document.OutputSettings();
        doctype.outerHtmlHead(accum, 0, out);
        assertEquals("<!DOCTYPE>", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithNonBlankAttr() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Strict//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd", "http://www.example.com");
        StringBuilder accum = new StringBuilder();
        OutputSettings out = new Document.OutputSettings();
        doctype.outerHtmlHead(accum, 0, out);
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">", accum.toString());
    }
    
    @Test
    public void testOuterHtmlTail() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Strict//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd", "http://www.example.com");
        StringBuilder accum = new StringBuilder();
        OutputSettings out = new Document.OutputSettings();
        doctype.outerHtmlTail(accum, 0, out);
        assertEquals("", accum.toString());
    }
}
