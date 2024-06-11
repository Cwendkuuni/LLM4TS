package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class DocumentTypeTest {
    @Test
    public void testNodeName() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.example.com");
        assertEquals("#doctype", doctype.nodeName());
    }
    
    @Test
    public void testOuterHtmlHead() {
        DocumentType doctypeWithoutPublicIdAndSystemId = new DocumentType("html", "", "", "http://www.example.com");
        DocumentType doctypeWithoutPublicId = new DocumentType("html", "", "http://www.w3.org/TR/html4/strict.dtd", "http://www.example.com");
        DocumentType doctypeWithoutSystemId = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "", "http://www.example.com");
        DocumentType doctypeWithPublicIdAndSystemId = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.example.com");
        Document.OutputSettings htmlSyntaxOutputSettings = new Document.OutputSettings(Syntax.html);
        Document.OutputSettings xmlSyntaxOutputSettings = new Document.OutputSettings(Syntax.xml);

        StringBuilder accum = new StringBuilder();
        doctypeWithoutPublicIdAndSystemId.outerHtmlHead(accum, 0, htmlSyntaxOutputSettings);
        assertEquals("<!doctype html>", accum.toString());
        accum.setLength(0);

        doctypeWithoutPublicIdAndSystemId.outerHtmlHead(accum, 0, xmlSyntaxOutputSettings);
        assertEquals("<!DOCTYPE html>", accum.toString());
        accum.setLength(0);

        doctypeWithoutPublicId.outerHtmlHead(accum, 0, htmlSyntaxOutputSettings);
        assertEquals("<!doctype html PUBLIC \"\">", accum.toString());
        accum.setLength(0);

        doctypeWithoutPublicId.outerHtmlHead(accum, 0, xmlSyntaxOutputSettings);
        assertEquals("<!DOCTYPE html PUBLIC \"\">", accum.toString());
        accum.setLength(0);

        doctypeWithoutSystemId.outerHtmlHead(accum, 0, htmlSyntaxOutputSettings);
        assertEquals("<!doctype html \"http://www.w3.org/TR/html4/strict.dtd\">", accum.toString());
        accum.setLength(0);

        doctypeWithoutSystemId.outerHtmlHead(accum, 0, xmlSyntaxOutputSettings);
        assertEquals("<!DOCTYPE html \"http://www.w3.org/TR/html4/strict.dtd\">", accum.toString());
        accum.setLength(0);

        doctypeWithPublicIdAndSystemId.outerHtmlHead(accum, 0, htmlSyntaxOutputSettings);
        assertEquals("<!doctype html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">", accum.toString());
        accum.setLength(0);

        doctypeWithPublicIdAndSystemId.outerHtmlHead(accum, 0, xmlSyntaxOutputSettings);
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">", accum.toString());
        accum.setLength(0);
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.example.com");
        Document.OutputSettings htmlSyntaxOutputSettings = new Document.OutputSettings(Syntax.html);

        StringBuilder accum = new StringBuilder();
        doctype.outerHtmlTail(accum, 0, htmlSyntaxOutputSettings);
        assertEquals("", accum.toString());
        accum.setLength(0);
    }

    @Test
    public void testHas() {
        DocumentType doctypeWithPublicIdAndSystemId = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.example.com");
        DocumentType doctypeWithoutPublicId = new DocumentType("html", "", "http://www.w3.org/TR/html4/strict.dtd", "http://www.example.com");
        DocumentType doctypeWithoutSystemId = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "", "http://www.example.com");
        DocumentType doctypeWithoutPublicIdAndSystemId = new DocumentType("html", "", "", "http://www.example.com");

        assertEquals(true, doctypeWithPublicIdAndSystemId.has("name"));
        assertEquals(true, doctypeWithPublicIdAndSystemId.has("publicId"));
        assertEquals(true, doctypeWithPublicIdAndSystemId.has("systemId"));
        assertEquals(false, doctypeWithoutPublicId.has("publicId"));
        assertEquals(false, doctypeWithoutSystemId.has("systemId"));
        assertEquals(false, doctypeWithoutPublicIdAndSystemId.has("publicId"));
        assertEquals(false, doctypeWithoutPublicIdAndSystemId.has("systemId"));
    }
}