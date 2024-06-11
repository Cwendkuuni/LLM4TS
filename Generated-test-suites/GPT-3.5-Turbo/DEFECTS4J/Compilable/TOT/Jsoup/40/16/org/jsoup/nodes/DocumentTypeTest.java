package org.jsoup.nodes;

import org.junit.Test;

import static org.junit.Assert.*;

public class DocumentTypeTest {

    @Test
    public void testConstructor() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "https://www.example.com");

        assertEquals("html", doctype.attr("name"));
        assertEquals("-//W3C//DTD HTML 4.01//EN", doctype.attr("publicId"));
        assertEquals("http://www.w3.org/TR/html4/strict.dtd", doctype.attr("systemId"));
        assertEquals("https://www.example.com", doctype.baseUri());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorEmptyName() {
        new DocumentType("", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "https://www.example.com");
    }

    @Test
    public void testNodeName() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "https://www.example.com");
        assertEquals("#doctype", doctype.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        StringBuilder stringBuilder = new StringBuilder();

        DocumentType doctypeWithPublicId = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", null, "https://www.example.com");
        doctypeWithPublicId.outerHtmlHead(stringBuilder, 0, outputSettings);
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\">", stringBuilder.toString());

        stringBuilder.setLength(0); // clear the StringBuilder

        DocumentType doctypeNoPublicId = new DocumentType("html", null, "http://www.w3.org/TR/html4/strict.dtd", "https://www.example.com");
        doctypeNoPublicId.outerHtmlHead(stringBuilder, 0, outputSettings);
        assertEquals("<!DOCTYPE html \"http://www.w3.org/TR/html4/strict.dtd\">", stringBuilder.toString());

        stringBuilder.setLength(0); // clear the StringBuilder

        DocumentType doctypeWithAllAttributes = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "https://www.example.com");
        doctypeWithAllAttributes.outerHtmlHead(stringBuilder, 0, outputSettings);
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">", stringBuilder.toString());

        stringBuilder.setLength(0); // clear the StringBuilder

        DocumentType doctypeWithEmptyName = new DocumentType("", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "https://www.example.com");
        doctypeWithEmptyName.outerHtmlHead(stringBuilder, 0, outputSettings);
        assertEquals("<!DOCTYPE PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">", stringBuilder.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        StringBuilder stringBuilder = new StringBuilder();

        DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "https://www.example.com");
        doctype.outerHtmlTail(stringBuilder, 0, null);

        assertEquals("", stringBuilder.toString());
    }
}