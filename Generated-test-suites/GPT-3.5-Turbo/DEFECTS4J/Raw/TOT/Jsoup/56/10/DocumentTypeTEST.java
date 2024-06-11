package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {
    
    @Test
    public void testDocumentTypeConstructor() {
        DocumentType documentType = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.example.com");
        assertNotNull(documentType);
    }
    
    @Test
    public void testDocumentTypeName() {
        DocumentType documentType = new DocumentType("html", null, null, null);
        assertEquals("#doctype", documentType.nodeName());
    }
    
    @Test
    public void testDocumentTypeOuterHtmlHeadHtml5() {
        DocumentType documentType = new DocumentType("html", null, null, null);
        Document.OutputSettings outputSettings = new Document.OutputSettings().syntax(Document.OutputSettings.Syntax.html);
        StringBuilder sb = new StringBuilder();
        try {
            documentType.outerHtmlHead(sb, 0, outputSettings);
        } catch (Exception e) {
            fail("Exception thrown while executing outerHtmlHead");
        }
        assertEquals("<!doctype", sb.toString());
    }
    
    @Test
    public void testDocumentTypeOuterHtmlHeadNotHtml5() {
        DocumentType documentType = new DocumentType("html", null, null, null);
        Document.OutputSettings outputSettings = new Document.OutputSettings().syntax(Document.OutputSettings.Syntax.xml);
        StringBuilder sb = new StringBuilder();
        try {
            documentType.outerHtmlHead(sb, 0, outputSettings);
        } catch (Exception e) {
            fail("Exception thrown while executing outerHtmlHead");
        }
        assertEquals("<!DOCTYPE", sb.toString());
    }
    
    @Test
    public void testDocumentTypeOuterHtmlHeadWithName() {
        DocumentType documentType = new DocumentType("html", null, null, null);
        documentType.attr(DocumentType.NAME, "html");
        Document.OutputSettings outputSettings = new Document.OutputSettings().syntax(Document.OutputSettings.Syntax.xml);
        StringBuilder sb = new StringBuilder();
        try {
            documentType.outerHtmlHead(sb, 0, outputSettings);
        } catch (Exception e) {
            fail("Exception thrown while executing outerHtmlHead");
        }
        assertEquals("<!DOCTYPE html", sb.toString());
    }
    
    @Test
    public void testDocumentTypeOuterHtmlHeadWithPublicId() {
        DocumentType documentType = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", null, null);
        documentType.attr(DocumentType.PUBLIC_ID, "-//W3C//DTD HTML 4.01//EN");
        Document.OutputSettings outputSettings = new Document.OutputSettings().syntax(Document.OutputSettings.Syntax.xml);
        StringBuilder sb = new StringBuilder();
        try {
            documentType.outerHtmlHead(sb, 0, outputSettings);
        } catch (Exception e) {
            fail("Exception thrown while executing outerHtmlHead");
        }
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\"", sb.toString());
    }
    
    @Test
    public void testDocumentTypeOuterHtmlHeadWithSystemId() {
        DocumentType documentType = new DocumentType("html", null, "http://www.w3.org/TR/html4/strict.dtd", null);
        documentType.attr(DocumentType.SYSTEM_ID, "http://www.w3.org/TR/html4/strict.dtd");
        Document.OutputSettings outputSettings = new Document.OutputSettings().syntax(Document.OutputSettings.Syntax.xml);
        StringBuilder sb = new StringBuilder();
        try {
            documentType.outerHtmlHead(sb, 0, outputSettings);
        } catch (Exception e) {
            fail("Exception thrown while executing outerHtmlHead");
        }
        assertEquals("<!DOCTYPE html \"http://www.w3.org/TR/html4/strict.dtd\"", sb.toString());
    }
    
    @Test
    public void testDocumentTypeOuterHtmlTail() {
        DocumentType documentType = new DocumentType("html", null, null, null);
        StringBuilder sb = new StringBuilder();
        documentType.outerHtmlTail(sb, 0, null);
        assertEquals("", sb.toString());
    }
    
    @Test
    public void testHas() {
        DocumentType documentType = new DocumentType("html", null, null, null);
        assertFalse(documentType.has(DocumentType.NAME));
        documentType.attr(DocumentType.NAME, "html");
        assertTrue(documentType.has(DocumentType.NAME));
    }
}