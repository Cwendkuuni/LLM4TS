package org.jsoup.nodes;

import org.junit.*;
import static org.junit.Assert.*;

public class DocumentTypeTest {

    @Test
    public void testConstructor() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.01 Transitional//EN", "http://www.w3.org/TR/html4/loose.dtd", "http://www.example.com");
        assertEquals("html", doctype.attr("name"));
        assertEquals("-//W3C//DTD HTML 4.01 Transitional//EN", doctype.attr("publicId"));
        assertEquals("http://www.w3.org/TR/html4/loose.dtd", doctype.attr("systemId"));
        assertEquals("http://www.example.com", doctype.baseUri());
    }

    @Test
    public void testNodeName() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.01 Transitional//EN", "http://www.w3.org/TR/html4/loose.dtd", "http://www.example.com");
        assertEquals("#doctype", doctype.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.01 Transitional//EN", "http://www.w3.org/TR/html4/loose.dtd", "http://www.example.com");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        doctype.outerHtmlHead(accum, 0, out);
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" http://www.w3.org/TR/html4/loose.dtd>", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.01 Transitional//EN", "http://www.w3.org/TR/html4/loose.dtd", "http://www.example.com");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        doctype.outerHtmlTail(accum, 0, out);
        assertEquals("", accum.toString());
    }
}