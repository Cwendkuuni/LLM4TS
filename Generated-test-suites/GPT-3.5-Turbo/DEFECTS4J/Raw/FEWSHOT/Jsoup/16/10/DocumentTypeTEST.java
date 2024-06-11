package org.jsoup.nodes;

import org.jsoup.helper.StringUtil;
import static org.junit.Assert.assertEquals;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeConstructor() {
        DocumentType documentType = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.example.com");

        assertEquals("html", documentType.attr("name"));
        assertEquals("-//W3C//DTD HTML 4.01//EN", documentType.attr("publicId"));
        assertEquals("http://www.w3.org/TR/html4/strict.dtd", documentType.attr("systemId"));
        assertEquals("http://www.example.com", documentType.baseUri());
    }

    @Test
    public void testNodeName() {
        DocumentType documentType = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.example.com");

        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testOuterHtmlHeadWithPublicIdAndSystemId() {
        DocumentType documentType = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.example.com");

        StringBuilder accum = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();

        documentType.outerHtmlHead(accum, 0, outputSettings);

        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" http://www.w3.org/TR/html4/strict.dtd>", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithPublicId() {
        DocumentType documentType = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", null, "http://www.example.com");

        StringBuilder accum = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();

        documentType.outerHtmlHead(accum, 0, outputSettings);

        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\">", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithSystemId() {
        DocumentType documentType = new DocumentType("html", null, "http://www.w3.org/TR/html4/strict.dtd", "http://www.example.com");

        StringBuilder accum = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();

        documentType.outerHtmlHead(accum, 0, outputSettings);

        assertEquals("<!DOCTYPE html http://www.w3.org/TR/html4/strict.dtd>", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithNoPublicIdAndSystemId() {
        DocumentType documentType = new DocumentType("html", null, null, "http://www.example.com");

        StringBuilder accum = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();

        documentType.outerHtmlHead(accum, 0, outputSettings);

        assertEquals("<!DOCTYPE html>", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType documentType = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.example.com");

        StringBuilder accum = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();

        documentType.outerHtmlTail(accum, 0, outputSettings);

        assertEquals("", accum.toString());
    }
}