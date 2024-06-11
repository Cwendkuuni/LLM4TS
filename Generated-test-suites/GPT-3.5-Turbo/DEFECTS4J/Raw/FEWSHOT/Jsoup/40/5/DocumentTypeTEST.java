package org.jsoup.nodes;

import org.jsoup.helper.Validate;
import org.junit.Test;

import static org.junit.Assert.*;

public class DocumentTypeTest {
    @Test
    public void testConstructor() {
        DocumentType documentType = new DocumentType("name", "publicId", "systemId", "baseUri");
        assertNotNull(documentType);
        assertEquals("name", documentType.attr("name"));
        assertEquals("publicId", documentType.attr("publicId"));
        assertEquals("systemId", documentType.attr("systemId"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithEmptyName() {
        new DocumentType("", "publicId", "systemId", "baseUri");
    }
    
    @Test
    public void testNodeName() {
        DocumentType documentType = new DocumentType("name", "publicId", "systemId", "baseUri");
        assertEquals("#doctype", documentType.nodeName());
    }
    
    @Test
    public void testOuterHtmlHead() {
        DocumentType documentType = new DocumentType("name", "publicId", "systemId", "baseUri");
        StringBuilder sb = new StringBuilder();
        Document.OutputSettings settings = new Document.OutputSettings();
        documentType.outerHtmlHead(sb, 0, settings);
        assertEquals("<!DOCTYPE name PUBLIC \"publicId\" \"systemId\">", sb.toString());
    }
    
    @Test
    public void testOuterHtmlTail() {
        DocumentType documentType = new DocumentType("name", "publicId", "systemId", "baseUri");
        StringBuilder sb = new StringBuilder();
        Document.OutputSettings settings = new Document.OutputSettings();
        documentType.outerHtmlTail(sb, 0, settings);
        assertEquals("", sb.toString());
    }
}