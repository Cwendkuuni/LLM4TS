package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testNodeName() {
        DocumentType documentType = new DocumentType("name", "publicId", "systemId", "baseUri");
        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType documentType = new DocumentType("name", "publicId", "systemId", "baseUri");
        StringBuilder stringBuilder = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();

        documentType.outerHtmlHead(stringBuilder, 0, outputSettings);
        assertEquals("<!DOCTYPE html PUBLIC \"publicId\" systemId\">", stringBuilder.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType documentType = new DocumentType("name", "publicId", "systemId", "baseUri");
        StringBuilder stringBuilder = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();

        documentType.outerHtmlTail(stringBuilder, 0, outputSettings);
        assertEquals("", stringBuilder.toString());
    }
}