package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class DocumentTypeTest {
    @Test
    public void testConstructor() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.0//EN", "http://www.w3.org/TR/REC-html40/strict.dtd", "https://www.example.com");
        assertEquals("html", doctype.attr("name"));
        assertEquals("-//W3C//DTD HTML 4.0//EN", doctype.attr("publicId"));
        assertEquals("http://www.w3.org/TR/REC-html40/strict.dtd", doctype.attr("systemId"));
    }

    @Test
    public void testNodeName() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.0//EN", "http://www.w3.org/TR/REC-html40/strict.dtd", "https://www.example.com");
        assertEquals("#doctype", doctype.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.0//EN", "http://www.w3.org/TR/REC-html40/strict.dtd", "https://www.example.com");
        StringBuilder accum = new StringBuilder();
        doctype.outerHtmlHead(accum, 0, new Document.OutputSettings());
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.0//EN\" \"http://www.w3.org/TR/REC-html40/strict.dtd\">", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.0//EN", "http://www.w3.org/TR/REC-html40/strict.dtd", "https://www.example.com");
        StringBuilder accum = new StringBuilder();
        doctype.outerHtmlTail(accum, 0, new Document.OutputSettings());
        assertEquals("", accum.toString());
    }
}