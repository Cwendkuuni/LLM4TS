package org.jsoup.nodes;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class DocumentTypeTest {
    @Test
    public void testDocumentTypeConstructor() {
        DocumentType documentType = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://example.com");
    
        assertEquals("html", documentType.attr("name"));
        assertEquals("-//W3C//DTD HTML 4.01//EN", documentType.attr("publicId"));
        assertEquals("http://www.w3.org/TR/html4/strict.dtd", documentType.attr("systemId"));
    }
    
    @Test
    public void testDocumentTypeConstructorBlankPublicId() {
        DocumentType documentType = new DocumentType("html", "", "http://www.w3.org/TR/html4/strict.dtd", "http://example.com");
    
        assertEquals("html", documentType.attr("name"));
        assertEquals("", documentType.attr("publicId"));
        assertEquals("http://www.w3.org/TR/html4/strict.dtd", documentType.attr("systemId"));
    }
    
    @Test
    public void testDocumentTypeConstructorBlankSystemId() {
        DocumentType documentType = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "", "http://example.com");
    
        assertEquals("html", documentType.attr("name"));
        assertEquals("-//W3C//DTD HTML 4.01//EN", documentType.attr("publicId"));
        assertEquals("", documentType.attr("systemId"));
    }
    
    @Test
    public void testDocumentTypeConstructorBlankPublicIdAndSystemId() {
        DocumentType documentType = new DocumentType("html", "", "", "http://example.com");
    
        assertEquals("html", documentType.attr("name"));
        assertEquals("", documentType.attr("publicId"));
        assertEquals("", documentType.attr("systemId"));
    }
    
    @Test
    public void testDocumentTypeNodeName() {
        DocumentType documentType = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://example.com");
    
        assertEquals("#doctype", documentType.nodeName());
    }
    
    @Test
    public void testDocumentTypeOuterHtmlHead() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.syntax(Document.OutputSettings.Syntax.html);
    
        DocumentType documentType = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://example.com");
        StringBuilder accum = new StringBuilder();
        try {
            documentType.outerHtmlHead(accum, 0, outputSettings);
        } catch (Exception e) {
            // Handle exception properly
        }
    
        assertEquals("<!doctype html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">", accum.toString());
    }
    
    @Test
    public void testDocumentTypeOuterHtmlHeadEmptyPublicId() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.syntax(Document.OutputSettings.Syntax.html);
    
        DocumentType documentType = new DocumentType("html", "", "http://www.w3.org/TR/html4/strict.dtd", "http://example.com");
        StringBuilder accum = new StringBuilder();
        try {
            documentType.outerHtmlHead(accum, 0, outputSettings);
        } catch (Exception e) {
            // Handle exception properly
        }
    
        assertEquals("<!doctype html \"http://www.w3.org/TR/html4/strict.dtd\">", accum.toString());
    }
    
    @Test
    public void testDocumentTypeOuterHtmlHeadEmptySystemId() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.syntax(Document.OutputSettings.Syntax.html);
    
        DocumentType documentType = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "", "http://example.com");
        StringBuilder accum = new StringBuilder();
        try {
            documentType.outerHtmlHead(accum, 0, outputSettings);
        } catch (Exception e) {
            // Handle exception properly
        }
    
        assertEquals("<!doctype html PUBLIC \"-//W3C//DTD HTML 4.01//EN\">", accum.toString());
    }
    
    @Test
    public void testDocumentTypeOuterHtmlHeadEmptyPublicIdAndSystemId() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.syntax(Document.OutputSettings.Syntax.html);
    
        DocumentType documentType = new DocumentType("html", "", "", "http://example.com");
        StringBuilder accum = new StringBuilder();
        try {
            documentType.outerHtmlHead(accum, 0, outputSettings);
        } catch (Exception e) {
            // Handle exception properly
        }
    
        assertEquals("<!doctype html>", accum.toString());
    }
    
    @Test
    public void testDocumentTypeOuterHtmlTail() {
        DocumentType documentType = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://example.com");
        StringBuilder accum = new StringBuilder();
        try {
            documentType.outerHtmlTail(accum, 0, null);
        } catch (Exception e) {
            // Handle exception properly
        }
    
        assertEquals("", accum.toString());
    }
    
    @Test
    public void testDocumentTypeHasAttribute() {
        DocumentType documentType = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://example.com");
    
        assertEquals(true, documentType.has("name"));
        assertEquals(true, documentType.has("publicId"));
        assertEquals(true, documentType.has("systemId"));
        assertEquals(false, documentType.has("invalidAttribute"));
    }
}