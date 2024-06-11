package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {
    
    @Test
    public void testConstructorWithValidInputs() {
        // Typical use case
        DocumentType documentType = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Transitional//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", "http://www.example.com");
        assertEquals("html", documentType.nodeName());
        assertEquals("-//W3C//DTD XHTML 1.0 Transitional//EN", documentType.attr("publicId"));
        assertEquals("http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", documentType.attr("systemId"));
        assertEquals("http://www.example.com", documentType.baseUri());
    }
    
    @Test
    public void testConstructorWithNullInputs() {
        // Error scenario: Name is null
        try {
            DocumentType documentType = new DocumentType(null, "-//W3C//DTD XHTML 1.0 Transitional//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", "http://www.example.com");
            fail("Expected NullPointerException not thrown");
        } catch (NullPointerException e) {
            // Exception thrown as expected
        }

        // Error scenario: Public ID is null
        try {
            DocumentType documentType = new DocumentType("html", null, "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", "http://www.example.com");
            fail("Expected NullPointerException not thrown");
        } catch (NullPointerException e) {
            // Exception thrown as expected
        }
        
        // Error scenario: System ID is null
        try {
            DocumentType documentType = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Transitional//EN", null, "http://www.example.com");
            fail("Expected NullPointerException not thrown");
        } catch (NullPointerException e) {
            // Exception thrown as expected
        }

        // Error scenario: Base URI is null
        try {
            DocumentType documentType = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Transitional//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", null);
            fail("Expected NullPointerException not thrown");
        } catch (NullPointerException e) {
            // Exception thrown as expected
        }
    }
    
    @Test
    public void testNodeName() {
        // Typical use case
        DocumentType documentType = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Transitional//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", "http://www.example.com");
        assertEquals("#doctype", documentType.nodeName());
    }
    
    @Test
    public void testOuterHtmlHead() {
        // Typical use case
        DocumentType documentType = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Transitional//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", "http://www.example.com");
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.syntax(Document.OutputSettings.Syntax.html);
        StringBuilder sb = new StringBuilder();

        // HTML5 doctype
        documentType.outerHtmlHead(sb, 0, outputSettings);
        assertEquals("<!doctype", sb.toString());

        // Non-HTML5 doctype
        outputSettings.syntax(Document.OutputSettings.Syntax.xml);
        sb.setLength(0);
        documentType.outerHtmlHead(sb, 0, outputSettings);
        assertEquals("<!DOCTYPE", sb.toString());

        // Doctype without name
        outputSettings.syntax(Document.OutputSettings.Syntax.html);
        sb.setLength(0);
        documentType.removeAttr("name");
        documentType.outerHtmlHead(sb, 0, outputSettings);
        assertEquals("<!DOCTYPE PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">", sb.toString());
    }
    
    @Test
    public void testOuterHtmlTail() {
        // Empty method - no implementations to test
    }
    
    @Test
    public void testHas() {
        // Typical use case
        DocumentType documentType = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Transitional//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", "http://www.example.com");
        assertTrue(documentType.has("name"));
        assertTrue(documentType.has("publicId"));
        assertTrue(documentType.has("systemId"));
        
        // Edge case: Empty attribute
        documentType.removeAttr("name");
        assertFalse(documentType.has("name"));
    }
}