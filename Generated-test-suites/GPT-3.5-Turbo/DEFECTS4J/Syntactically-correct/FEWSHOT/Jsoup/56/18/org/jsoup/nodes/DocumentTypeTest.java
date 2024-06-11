package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.junit.Test;

public class DocumentTypeTest {
    @Test
    public void testNodeName() {
        DocumentType docType = new DocumentType("name", "publicId", "systemId", "baseUri");
        assertEquals("#doctype", docType.nodeName());
    }
    
    @Test
    public void testOuterHtmlHead() {
        DocumentType docType = new DocumentType("name", "publicId", "systemId", "baseUri");
        
        Document.OutputSettings htmlSettings = new Document.OutputSettings();
        htmlSettings.syntax(Document.OutputSettings.Syntax.html);
        StringBuilder htmlAccum = new StringBuilder();
        StringBuilder expectedHtmlAccum = new StringBuilder("<!doctype name public \"publicId\" \"systemId\">");
        docType.outerHtmlHead(htmlAccum, 0, htmlSettings);
        assertEquals(expectedHtmlAccum.toString(), htmlAccum.toString());

        Document.OutputSettings xmlSettings = new Document.OutputSettings();
        xmlSettings.syntax(Document.OutputSettings.Syntax.xml);
        StringBuilder xmlAccum = new StringBuilder();
        StringBuilder expectedXmlAccum = new StringBuilder("<!DOCTYPE name PUBLIC \"publicId\" \"systemId\">");
        docType.outerHtmlHead(xmlAccum, 0, xmlSettings);
        assertEquals(expectedXmlAccum.toString(), xmlAccum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType docType = new DocumentType("name", "publicId", "systemId", "baseUri");

        StringBuilder accum = new StringBuilder();
        docType.outerHtmlTail(accum, 0, new Document.OutputSettings());

        assertEquals("", accum.toString());
    }

    @Test
    public void testHas() {
        DocumentType docType = new DocumentType("name", "publicId", "systemId", "baseUri");

        assertTrue(docType.has(DocumentType.NAME));
        assertTrue(docType.has(DocumentType.PUBLIC_ID));
        assertTrue(docType.has(DocumentType.SYSTEM_ID));
    }
}