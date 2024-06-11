package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DocumentTypeTest {
  
    @Test
    public void testConstructor() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.example.com");
        assertEquals("html", doctype.nodeName());
        assertEquals("-//W3C//DTD HTML 4.01//EN", doctype.attr("publicId"));
        assertEquals("http://www.w3.org/TR/html4/strict.dtd", doctype.attr("systemId"));
        assertEquals("http://www.example.com", doctype.baseUri());
    }
  
    @Test
    public void testNodeName() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.example.com");
        assertEquals("#doctype", doctype.nodeName());
    }

    @Test
    public void testOuterHtmlHeadBlankPublicIdAndSystemId() {
        DocumentType doctype = new DocumentType("html", "", "", "");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        doctype.outerHtmlHead(accum, 0, outputSettings);
        assertEquals("<!DOCTYPE html>", accum.toString());
    }
  
    @Test
    public void testOuterHtmlHeadWithPublicId() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "", "");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        doctype.outerHtmlHead(accum, 0, outputSettings);
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\">", accum.toString());
    }
  
    @Test
    public void testOuterHtmlHeadWithSystemId() {
        DocumentType doctype = new DocumentType("html", "", "http://www.w3.org/TR/html4/strict.dtd", "");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        doctype.outerHtmlHead(accum, 0, outputSettings);
        assertEquals("<!DOCTYPE html \"http://www.w3.org/TR/html4/strict.dtd\">", accum.toString());
    }
  
    @Test
    public void testOuterHtmlHeadWithPublicIdAndSystemId() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        doctype.outerHtmlHead(accum, 0, outputSettings);
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">", accum.toString());
    }
  
    @Test
    public void testOuterHtmlTail() {
        DocumentType doctype = new DocumentType("html", "", "", "");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        doctype.outerHtmlTail(accum, 0, outputSettings);
        assertTrue(accum.toString().isEmpty());
    }
}