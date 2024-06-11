package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

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
        StringBuilder builder = new StringBuilder();
        documentType.outerHtmlHead(builder, 0, null);
        assertEquals("<!DOCTYPE html PUBLIC \"publicId\" systemId\">", builder.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType documentType = new DocumentType("name", "publicId", "systemId", "baseUri");
        StringBuilder builder = new StringBuilder();
        documentType.outerHtmlTail(builder, 0, null);
        assertEquals("", builder.toString());
    }
}