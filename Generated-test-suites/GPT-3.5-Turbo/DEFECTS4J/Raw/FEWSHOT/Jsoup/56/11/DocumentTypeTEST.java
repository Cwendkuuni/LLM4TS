package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;

public class DocumentTypeTest {
    
    @Test
    public void testDocumentTypeConstructor() {
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
        StringBuilder accumulator = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.syntax(Document.OutputSettings.Syntax.html);
        try {
            documentType.outerHtmlHead(accumulator, 0, outputSettings);
        } catch (IOException e) {
            fail("IOException occurred");
        }
        assertEquals("<!doctype name PUBLIC \"publicId\" \"systemId\">", accumulator.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType documentType = new DocumentType("name", "publicId", "systemId", "baseUri");
        StringBuilder accumulator = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        documentType.outerHtmlTail(accumulator, 0, outputSettings);
        assertEquals("", accumulator.toString());
    }

    @Test
    public void testHas() {
        DocumentType documentType = new DocumentType("name", "publicId", "systemId", "baseUri");
        assertTrue(documentType.has("name"));
        assertTrue(documentType.has("publicId"));
        assertTrue(documentType.has("systemId"));
        assertFalse(documentType.has("someInvalidAttribute"));
    }
}