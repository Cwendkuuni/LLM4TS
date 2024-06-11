package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document.OutputSettings;
import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testConstructor() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.example.com");
        assertEquals("html", doctype.attr(NAME));
        assertEquals("-//W3C//DTD HTML 4.01//EN", doctype.attr(PUBLIC_ID));
        assertEquals("http://www.w3.org/TR/html4/strict.dtd", doctype.attr(SYSTEM_ID));
        assertEquals("http://www.example.com", doctype.baseUri());
    }
    
    @Test
    public void testNodeName() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.example.com");
        assertEquals("#doctype", doctype.nodeName());
    }
    
    @Test
    public void testOuterHtmlHead() {
        DocumentType doctype1 = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.example.com");
        DocumentType doctype2 = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", null, "http://www.example.com");
        DocumentType doctype3 = new DocumentType("html", null, "http://www.w3.org/TR/html4/strict.dtd", "http://www.example.com");
        DocumentType doctype4 = new DocumentType("html", null, null, "http://www.example.com");
        
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">", doctype1.outerHtml());
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\">", doctype2.outerHtml());
        assertEquals("<!DOCTYPE html \"http://www.w3.org/TR/html4/strict.dtd\">", doctype3.outerHtml());
        assertEquals("<!DOCTYPE html>", doctype4.outerHtml());
    }
    
    @Test
    public void testOuterHtmlTail() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.example.com");
        StringBuilder accum = new StringBuilder();
        doctype.outerHtmlTail(accum, 0, new Document.OutputSettings());
        assertEquals("", accum.toString());
    }
    
    @Test
    public void testHas() {
        DocumentType doctype1 = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.example.com");
        DocumentType doctype2 = new DocumentType("html", "-", null, "http://www.example.com");
        DocumentType doctype3 = new DocumentType("html", null, "http://www.w3.org/TR/html4/strict.dtd", "http://www.example.com");
        DocumentType doctype4 = new DocumentType("html", null, null, "http://www.example.com");
        
        assertTrue(doctype1.has(NAME));
        assertTrue(doctype1.has(PUBLIC_ID));
        assertTrue(doctype1.has(SYSTEM_ID));
        
        assertTrue(doctype2.has(NAME));
        assertTrue(doctype2.has(PUBLIC_ID));
        assertFalse(doctype2.has(SYSTEM_ID));
        
        assertTrue(doctype3.has(NAME));
        assertFalse(doctype3.has(PUBLIC_ID));
        assertTrue(doctype3.has(SYSTEM_ID));
        
        assertTrue(doctype4.has(NAME));
        assertFalse(doctype4.has(PUBLIC_ID));
        assertFalse(doctype4.has(SYSTEM_ID));
    }
}