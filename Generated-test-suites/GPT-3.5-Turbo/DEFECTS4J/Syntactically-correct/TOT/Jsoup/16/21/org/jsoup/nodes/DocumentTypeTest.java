package org.jsoup.nodes;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeConstructor() {
        DocumentType documentType = new DocumentType("html", "-//W3C//DTD HTML 4.0//EN", "http://www.w3.org/TR/REC-html40/strict.dtd", "https://example.com");
        assertEquals("html", documentType.attr("name"));
        assertEquals("-//W3C//DTD HTML 4.0//EN", documentType.attr("publicId"));
        assertEquals("http://www.w3.org/TR/REC-html40/strict.dtd", documentType.attr("systemId"));
        assertEquals("https://example.com", documentType.baseUri());
    }

    @Test
    public void testDocumentTypeName() {
        DocumentType documentType = new DocumentType("html", null, null, null);
        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testDocumentTypeOuterHtmlHead() {
        DocumentType documentType = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", null, null);
        StringBuilder sb = new StringBuilder();
        documentType.outerHtmlHead(sb, 0, null);
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\">", sb.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlTail() {
        DocumentType documentType = new DocumentType("html", null, null, null);
        StringBuilder sb = new StringBuilder();
        documentType.outerHtmlTail(sb, 0, null);
        assertEquals("", sb.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlHeadWithSystemId() {
        DocumentType documentType = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", null);
        StringBuilder sb = new StringBuilder();
        documentType.outerHtmlHead(sb, 0, null);
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" http://www.w3.org/TR/html4/strict.dtd>", sb.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlHeadWithPublicAndSystemId() {
        DocumentType documentType = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", null);
        StringBuilder sb = new StringBuilder();
        documentType.outerHtmlHead(sb, 0, null);
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" http://www.w3.org/TR/html4/strict.dtd>", sb.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlHeadWithoutPublicAndSystemId() {
        DocumentType documentType = new DocumentType("html", null, null, null);
        StringBuilder sb = new StringBuilder();
        documentType.outerHtmlHead(sb, 0, null);
        assertEquals("<!DOCTYPE html>", sb.toString());
    }

}