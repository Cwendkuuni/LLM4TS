package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {

    @Test
    public void testConstructor() {
        DocumentType doctype = new DocumentType("name", "publicId", "systemId", "baseUri");
        
        assertEquals("name", doctype.attr("name"));
        assertEquals("publicId", doctype.attr("publicId"));
        assertEquals("systemId", doctype.attr("systemId"));
    }

    @Test
    public void testNodeName() {
        DocumentType doctype = new DocumentType("name", "publicId", "systemId", "baseUri");
        
        assertEquals("#doctype", doctype.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType doctype = new DocumentType("name", "publicId", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        
        doctype.outerHtmlHead(accum, 0, out);
        
        assertEquals("<!DOCTYPE name PUBLIC \"publicId\" \"systemId\">", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType doctype = new DocumentType("name", "publicId", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        
        doctype.outerHtmlTail(accum, 0, out);
        
        assertEquals("", accum.toString());
    }
}