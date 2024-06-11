package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {

    // Test constructor
    @Test
    public void testConstructor() {
        DocumentType docType = new DocumentType("doctype", "publicId", "systemId", "baseUri");
        assertEquals("doctype", docType.attr(DocumentType.NAME));
        assertEquals("publicId", docType.attr(DocumentType.PUBLIC_ID));
        assertEquals("systemId", docType.attr(DocumentType.SYSTEM_ID));
    }

    // Test nodeName method
    @Test
    public void testNodeName() {
        DocumentType docType = new DocumentType("doctype", "publicId", "systemId", "baseUri");
        assertEquals("#doctype", docType.nodeName());
    }

    // Test outerHtmlHead method for HTML syntax
    @Test
    public void testOuterHtmlHeadHtml() throws Exception {
        DocumentType docType = new DocumentType("doctype", "publicId", "systemId", "baseUri");
        Document.OutputSettings settings = new Document.OutputSettings();
        settings.syntax(Document.OutputSettings.Syntax.html);
        
        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, settings);
        
        assertEquals("<!doctype", accum.toString());
    }

    // Test outerHtmlHead method for non-HTML syntax
    @Test
    public void testOuterHtmlHeadNonHtml() throws Exception {
        DocumentType docType = new DocumentType("doctype", "publicId", "systemId", "baseUri");
        Document.OutputSettings settings = new Document.OutputSettings();
        settings.syntax(Document.OutputSettings.Syntax.xml);
        
        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, settings);
        
        assertEquals("<!DOCTYPE", accum.toString());
    }

    // Test outerHtmlTail method
    @Test
    public void testOuterHtmlTail() {
        DocumentType docType = new DocumentType("doctype", "publicId", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        
        docType.outerHtmlTail(accum, 0, null);
        
        assertEquals("", accum.toString());
    }

    // Test has method with non-blank attribute
    @Test
    public void testHasAttributeNonBlank() {
        DocumentType docType = new DocumentType("doctype", "publicId", "systemId", "baseUri");
        
        assertTrue(docType.has(DocumentType.NAME));
    }

    // Test has method with blank attribute
    @Test
    public void testHasAttributeBlank() {
        DocumentType docType = new DocumentType("", "", "", "");
        
        assertFalse(docType.has(DocumentType.NAME));
    }
}