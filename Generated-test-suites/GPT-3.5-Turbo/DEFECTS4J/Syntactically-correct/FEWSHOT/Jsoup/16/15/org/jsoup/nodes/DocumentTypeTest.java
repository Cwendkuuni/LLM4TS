package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;

import org.jsoup.helper.StringUtil;
import org.junit.Test;

public class DocumentTypeTest {
    @Test
    public void testDocumentTypeConstructor() {
        DocumentType docType = new DocumentType("name", "publicId", "systemId", "baseUri");
        assertEquals("name", docType.attr("name"));
        assertEquals("publicId", docType.attr("publicId"));
        assertEquals("systemId", docType.attr("systemId"));
    }

    @Test
    public void testNodeName() {
        DocumentType docType = new DocumentType("name", "publicId", "systemId", "baseUri");
        assertEquals("#doctype", docType.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType docType = new DocumentType("name", "publicId", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();
        docType.outerHtmlHead(accum, depth, out);
        assertEquals("<!DOCTYPE html PUBLIC \"publicId\" systemId\">", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType docType = new DocumentType("name", "publicId", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();
        docType.outerHtmlTail(accum, depth, out);
        assertEquals("", accum.toString());
    }
}