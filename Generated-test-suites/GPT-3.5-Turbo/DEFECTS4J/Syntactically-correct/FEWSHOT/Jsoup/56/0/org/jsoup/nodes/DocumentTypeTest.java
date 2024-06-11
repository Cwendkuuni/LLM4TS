package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class DocumentTypeTest {
    @Test
    public void testConstructor() {
        DocumentType dt = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://example.com");
        assertEquals("#doctype", dt.nodeName());
        assertEquals("html", dt.attr("name"));
        assertEquals("-//W3C//DTD HTML 4.01//EN", dt.attr("publicId"));
        assertEquals("http://www.w3.org/TR/html4/strict.dtd", dt.attr("systemId"));
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType dt = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://example.com");
        StringBuilder accum = new StringBuilder();
        dt.outerHtmlHead(accum, 0, new Document.OutputSettings(Syntax.html));
        assertEquals("<!doctype html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">", accum.toString());

        accum = new StringBuilder();
        dt.outerHtmlHead(accum, 0, new Document.OutputSettings(Syntax.xml));
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType dt = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://example.com");
        StringBuilder accum = new StringBuilder();
        dt.outerHtmlTail(accum, 0, new Document.OutputSettings(Syntax.html));
        assertEquals("", accum.toString());

        accum = new StringBuilder();
        dt.outerHtmlTail(accum, 0, new Document.OutputSettings(Syntax.xml));
        assertEquals("", accum.toString());
    }

    @Test
    public void testHas() {
        DocumentType dt = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://example.com");
        assertEquals(true, dt.has("name"));
        assertEquals(true, dt.has("publicId"));
        assertEquals(true, dt.has("systemId"));
        assertEquals(false, dt.has("foo"));
    }
}