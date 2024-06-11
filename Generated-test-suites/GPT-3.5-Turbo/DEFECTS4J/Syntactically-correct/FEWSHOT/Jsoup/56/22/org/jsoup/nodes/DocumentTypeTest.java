package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeConstructor() {
        DocumentType documentType = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Transitional//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", "http://www.example.com");
        assertEquals("html", documentType.nodeName());
        assertEquals("-//W3C//DTD XHTML 1.0 Transitional//EN", documentType.attr("publicId"));
        assertEquals("http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", documentType.attr("systemId"));
        assertEquals("http://www.example.com", documentType.baseUri());
    }

    @Test
    public void testNodeName() {
        DocumentType documentType = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Transitional//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", "http://www.example.com");
        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType documentType = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Transitional//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", "http://www.example.com");
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, new Document.OutputSettings(Syntax.html));
        assertEquals("<!doctype html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadNoPublicId() {
        DocumentType documentType = new DocumentType("html", null, "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", "http://www.example.com");
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, new Document.OutputSettings(Syntax.html));
        assertEquals("<!doctype html \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadNoSystemId() {
        DocumentType documentType = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Transitional//EN", null, "http://www.example.com");
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, new Document.OutputSettings(Syntax.html));
        assertEquals("<!doctype html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\">", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType documentType = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Transitional//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", "http://www.example.com");
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlTail(accum, 0, new Document.OutputSettings(Syntax.html));
        assertEquals("", accum.toString());
    }

    @Test
    public void testHas() {
        DocumentType documentType = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Transitional//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", "http://www.example.com");
        assertTrue(documentType.has("publicId"));
        assertTrue(documentType.has("systemId"));
        assertFalse(documentType.has("name"));
    }

}