package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.junit.Test;

public class DocumentTypeTest {
    @Test
    public void testConstructor() {
        DocumentType documentType = new DocumentType("DOCTYPE", "publicId", "systemId", "baseUri");

        assertEquals("baseUri", documentType.baseUri());
        assertEquals("DOCTYPE", documentType.attr("name"));
        assertEquals("publicId", documentType.attr("publicId"));
        assertEquals("systemId", documentType.attr("systemId"));
    }

    @Test
    public void testNodeName() {
        DocumentType documentType = new DocumentType("DOCTYPE", "publicId", "systemId", "baseUri");

        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType documentType = new DocumentType("DOCTYPE", "publicId", "systemId", "baseUri");

        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();

        documentType.outerHtmlHead(accum, 0, out);

        assertEquals("<!DOCTYPE DOCTYPE PUBLIC \"publicId\" \"systemId\">", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType documentType = new DocumentType("DOCTYPE", "publicId", "systemId", "baseUri");

        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();

        documentType.outerHtmlTail(accum, 0, out);

        assertEquals("", accum.toString());
    }
}