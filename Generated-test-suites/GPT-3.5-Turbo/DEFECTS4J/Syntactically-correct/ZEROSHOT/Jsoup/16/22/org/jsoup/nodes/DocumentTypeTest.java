package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testNodeName() {
        DocumentType documentType = new DocumentType("doctype", "publicId", "systemId", "baseUri");
        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testOuterHtmlHeadWithPublicId() {
        DocumentType documentType = new DocumentType("doctype", "publicId", null, "baseUri");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        documentType.outerHtmlHead(accum, 0, out);
        assertEquals("<!DOCTYPE html PUBLIC \"publicId\">", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithSystemId() {
        DocumentType documentType = new DocumentType("doctype", null, "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        documentType.outerHtmlHead(accum, 0, out);
        assertEquals("<!DOCTYPE html systemId\">", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithPublicAndSystemId() {
        DocumentType documentType = new DocumentType("doctype", "publicId", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        documentType.outerHtmlHead(accum, 0, out);
        assertEquals("<!DOCTYPE html PUBLIC \"publicId\" systemId\">", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithoutPublicOrSystemId() {
        DocumentType documentType = new DocumentType("doctype", null, null, "baseUri");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        documentType.outerHtmlHead(accum, 0, out);
        assertEquals("<!DOCTYPE html>", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType documentType = new DocumentType("doctype", "publicId", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        documentType.outerHtmlTail(accum, 0, out);
        // No assertions needed as the method does not contain any logic
    }

}