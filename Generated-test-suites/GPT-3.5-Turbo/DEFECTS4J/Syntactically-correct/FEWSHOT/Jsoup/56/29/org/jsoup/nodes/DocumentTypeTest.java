package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.jsoup.Jsoup;

public class DocumentTypeTest {
    @Test
    public void testConstructor() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "https://www.example.com");
        assertEquals("html", doctype.nodeName());
        assertEquals("-//W3C//DTD HTML 4.01//EN", doctype.attr("publicId"));
        assertEquals("http://www.w3.org/TR/html4/strict.dtd", doctype.attr("systemId"));
        assertEquals("https://www.example.com", doctype.baseUri());
    }

    @Test
    public void testNodeName() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "https://www.example.com");
        assertEquals("#doctype", doctype.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "https://www.example.com");
        Document.OutputSettings settings = new Document.OutputSettings();
        settings.syntax(Document.OutputSettings.Syntax.html);
        StringBuilder sb = new StringBuilder();
        doctype.outerHtmlHead(sb, 0, settings);
        assertEquals("<!doctype HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">", sb.toString());
    }

    @Test
    public void testOuterHtmlHeadWithoutPublicIdAndSystemId() {
        DocumentType doctype = new DocumentType("html", null, null, "https://www.example.com");
        Document.OutputSettings settings = new Document.OutputSettings();
        settings.syntax(Document.OutputSettings.Syntax.html);
        StringBuilder sb = new StringBuilder();
        doctype.outerHtmlHead(sb, 0, settings);
        assertEquals("<!doctype html>", sb.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "https://www.example.com");
        StringBuilder sb = new StringBuilder();
        doctype.outerHtmlTail(sb, 0, null);
        assertEquals("", sb.toString());
    }

    @Test
    public void testHasWithAttributePresent() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "https://www.example.com");
        assertTrue(doctype.has("publicId"));
    }

    @Test
    public void testHasWithAttributeNotPresent() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "https://www.example.com");
        assertFalse(doctype.has("systemId"));
    }
}