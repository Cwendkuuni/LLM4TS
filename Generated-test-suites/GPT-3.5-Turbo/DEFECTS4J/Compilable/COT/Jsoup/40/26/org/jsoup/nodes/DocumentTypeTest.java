package org.jsoup.nodes;
import org.junit.Test;

import static org.junit.Assert.*;

public class DocumentTypeTest {

    @Test
    public void testConstructor() {
        DocumentType documentType = new DocumentType("name", "publicId", "systemId", "baseUri");
        assertEquals("name", documentType.attr("name"));
        assertEquals("publicId", documentType.attr("publicId"));
        assertEquals("systemId", documentType.attr("systemId"));
    }

    @Test
    public void testNodeName() {
        DocumentType documentType = new DocumentType("name", "publicId", "systemId", "baseUri");
        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType documentType = new DocumentType("name", "publicId", "systemId", "baseUri");

        StringBuilder stringBuilder = new StringBuilder();
        documentType.outerHtmlHead(stringBuilder, 0, new Document.OutputSettings());
        assertEquals("<!DOCTYPE name PUBLIC \"publicId\" \"systemId\">", stringBuilder.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType documentType = new DocumentType("name", "publicId", "systemId", "baseUri");

        StringBuilder stringBuilder = new StringBuilder();
        documentType.outerHtmlTail(stringBuilder, 0, new Document.OutputSettings());
        assertEquals("", stringBuilder.toString());
    }

    // Additional Test Cases for potential edge cases and exception handling scenarios

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithEmptyName() {
        new DocumentType("", "publicId", "systemId", "baseUri");
    }

    @Test
    public void testConstructorWithEmptyPublicId() {
        DocumentType documentType = new DocumentType("name", "", "systemId", "baseUri");
        assertEquals("", documentType.attr("publicId"));
    }

    @Test
    public void testConstructorWithEmptySystemId() {
        DocumentType documentType = new DocumentType("name", "publicId", "", "baseUri");
        assertEquals("", documentType.attr("systemId"));
    }

    @Test
    public void testConstructorWithEmptyBaseUri() {
        DocumentType documentType = new DocumentType("name", "publicId", "systemId", "");
        assertEquals("", documentType.baseUri());
    }

}