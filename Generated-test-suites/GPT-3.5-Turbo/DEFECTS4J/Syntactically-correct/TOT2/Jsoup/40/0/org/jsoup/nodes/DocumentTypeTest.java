package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeConstructor() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "https://www.example.com");
        assertNotNull(doctype);
        assertEquals("html", doctype.attr("name"));
        assertEquals("-//W3C//DTD HTML 4.01//EN", doctype.attr("publicId"));
        assertEquals("http://www.w3.org/TR/html4/strict.dtd", doctype.attr("systemId"));
    }

    @Test
    public void testNodeName() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "https://www.example.com");
        assertEquals("#doctype", doctype.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "https://www.example.com");
        StringBuilder sb = new StringBuilder();
        doctype.outerHtmlHead(sb, 0, outputSettings);
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">", sb.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "https://www.example.com");
        StringBuilder sb = new StringBuilder();
        doctype.outerHtmlTail(sb, 0, outputSettings);
        assertEquals("", sb.toString());
    }
}