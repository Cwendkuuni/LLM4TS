package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DocumentTypeTest {

    private DocumentType documentType;

    @Before
    public void setUp() {
        documentType = new DocumentType("name", "publicId", "systemId", "baseUri");
    }

    @Test
    public void testNodeName() {
        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.syntax(Syntax.html);
        StringBuilder accum = new StringBuilder();
        try {
            documentType.outerHtmlHead(accum, 0, outputSettings);
        } catch (Exception e) {
            fail("An exception occurred: " + e.getMessage());
        }
        assertEquals("<!doctype", accum.toString());
    }

    @Test
    public void testOuterHtmlHead_noPublicId_noSystemId() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.syntax(Syntax.html);
        documentType.attr(DocumentType.PUBLIC_ID, "");
        documentType.attr(DocumentType.SYSTEM_ID, "");
        StringBuilder accum = new StringBuilder();
        try {
            documentType.outerHtmlHead(accum, 0, outputSettings);
        } catch (Exception e) {
            fail("An exception occurred: " + e.getMessage());
        }
        assertEquals("<!doctype", accum.toString());
    }

    @Test
    public void testOuterHtmlHead_withName() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.syntax(Syntax.html);
        documentType.attr(DocumentType.PUBLIC_ID, "");
        documentType.attr(DocumentType.SYSTEM_ID, "");
        StringBuilder accum = new StringBuilder();
        try {
            documentType.outerHtmlHead(accum, 0, outputSettings);
        } catch (Exception e) {
            fail("An exception occurred: " + e.getMessage());
        }
        assertEquals("<!DOCTYPE name>", accum.toString());
    }

    @Test
    public void testOuterHtmlHead_withPublicId() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.syntax(Syntax.html);
        documentType.attr(DocumentType.NAME, "");
        documentType.attr(DocumentType.SYSTEM_ID, "");
        StringBuilder accum = new StringBuilder();
        try {
            documentType.outerHtmlHead(accum, 0, outputSettings);
        } catch (Exception e) {
            fail("An exception occurred: " + e.getMessage());
        }
        assertEquals("<!DOCTYPE PUBLIC \"publicId\">", accum.toString());
    }

    @Test
    public void testOuterHtmlHead_withSystemId() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.syntax(Syntax.html);
        documentType.attr(DocumentType.NAME, "");
        documentType.attr(DocumentType.PUBLIC_ID, "");
        StringBuilder accum = new StringBuilder();
        try {
            documentType.outerHtmlHead(accum, 0, outputSettings);
        } catch (Exception e) {
            fail("An exception occurred: " + e.getMessage());
        }
        assertEquals("<!DOCTYPE \"systemId\">", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlTail(accum, 0, new Document.OutputSettings());
        assertTrue(accum.toString().isEmpty());
    }

    @Test
    public void testHas_withAttributePresent() {
        documentType.attr("name", "value");
        assertTrue(documentType.has("name"));
    }

    @Test
    public void testHas_withAttributeBlank() {
        documentType.attr("name", "");
        assertFalse(documentType.has("name"));
    }

    @Test
    public void testHas_withAttributeNotPresent() {
        assertFalse(documentType.has("nonExistentAttribute"));
    }
}