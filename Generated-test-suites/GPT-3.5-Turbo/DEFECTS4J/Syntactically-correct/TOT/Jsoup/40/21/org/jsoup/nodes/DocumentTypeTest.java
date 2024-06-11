package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {

    @Test
    public void testConstructor() {
        DocumentType documentType = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Transitional//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", "https://www.example.com");
        assertNotNull(documentType);
        assertEquals("html", documentType.attr("name"));
        assertEquals("-//W3C//DTD XHTML 1.0 Transitional//EN", documentType.attr("publicId"));
        assertEquals("http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", documentType.attr("systemId"));
        assertEquals("https://www.example.com", documentType.baseUri());
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testConstructorWithEmptyName() {
        DocumentType documentType = new DocumentType("", "-//W3C//DTD XHTML 1.0 Transitional//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", "https://www.example.com");
    }
    
    @Test
    public void testConstructorWithEmptyPublicId() {
        DocumentType documentType = new DocumentType("html", "", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", "https://www.example.com");
        assertNotNull(documentType);
        assertEquals("html", documentType.attr("name"));
        assertTrue(documentType.attr("publicId").isEmpty());
        assertEquals("http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", documentType.attr("systemId"));
        assertEquals("https://www.example.com", documentType.baseUri());
    }
    
    @Test
    public void testConstructorWithEmptySystemId() {
        DocumentType documentType = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Transitional//EN", "", "https://www.example.com");
        assertNotNull(documentType);
        assertEquals("html", documentType.attr("name"));
        assertEquals("-//W3C//DTD XHTML 1.0 Transitional//EN", documentType.attr("publicId"));
        assertTrue(documentType.attr("systemId").isEmpty());
        assertEquals("https://www.example.com", documentType.baseUri());
    }
    
    @Test
    public void testConstructorWithEmptyBaseUri() {
        DocumentType documentType = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Transitional//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", "");
        assertNotNull(documentType);
        assertEquals("html", documentType.attr("name"));
        assertEquals("-//W3C//DTD XHTML 1.0 Transitional//EN", documentType.attr("publicId"));
        assertEquals("http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", documentType.attr("systemId"));
        assertTrue(documentType.baseUri().isEmpty());
    }
    
    @Test
    public void testNodeName() {
        DocumentType documentType = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Transitional//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", "https://www.example.com");
        assertEquals("#doctype", documentType.nodeName());
    }
    
    @Test
    public void testOuterHtmlHeadWithPublicId() {
        DocumentType documentType = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Transitional//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", "https://www.example.com");
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();
        documentType.outerHtmlHead(accum, depth, out);
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">", accum.toString());
    }
    
    @Test
    public void testOuterHtmlHeadWithoutPublicId() {
        DocumentType documentType = new DocumentType("html", "", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", "https://www.example.com");
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();
        documentType.outerHtmlHead(accum, depth, out);
        assertEquals("<!DOCTYPE html \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">", accum.toString());
    }
    
    @Test
    public void testOuterHtmlHeadWithoutSystemId() {
        DocumentType documentType = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Transitional//EN", "", "https://www.example.com");
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();
        documentType.outerHtmlHead(accum, depth, out);
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\">", accum.toString());
    }
    
    @Test
    public void testOuterHtmlHeadWithoutPublicIdAndSystemId() {
        DocumentType documentType = new DocumentType("html", "", "", "https://www.example.com");
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();
        documentType.outerHtmlHead(accum, depth, out);
        assertEquals("<!DOCTYPE html>", accum.toString());
    }
    
    @Test
    public void testOuterHtmlTail() {
        DocumentType documentType = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Transitional//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", "https://www.example.com");
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();
        documentType.outerHtmlTail(accum, depth, out);
        assertEquals("", accum.toString());
    }
}