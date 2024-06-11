package org.jsoup.nodes;

import org.junit.Test;

import static org.junit.Assert.*;

public class DocumentTypeTest {

    @Test
    public void testConstructor() {
        DocumentType dt = new DocumentType("name", "publicId", "systemId", "baseUri");

        assertEquals("name", dt.attr("name"));
        assertEquals("publicId", dt.attr("publicId"));
        assertEquals("systemId", dt.attr("systemId"));
    }

    @Test
    public void testNodeName() {
        DocumentType dt = new DocumentType("name", "publicId", "systemId", "baseUri");

        assertEquals("#doctype", dt.nodeName());
    }
    
    @Test
    public void testOuterHtmlHead() {
        DocumentType dt = new DocumentType("name", "publicId", "systemId", "baseUri");

        StringBuilder accum = new StringBuilder();
        dt.outerHtmlHead(accum, 0, new Document.OutputSettings());
        
        assertEquals("<!DOCTYPE html PUBLIC \"publicId\" systemId\">", accum.toString());
    }
    
    @Test
    public void testOuterHtmlTail() {
        DocumentType dt = new DocumentType("name", "publicId", "systemId", "baseUri");

        StringBuilder accum = new StringBuilder();
        dt.outerHtmlTail(accum, 0, new Document.OutputSettings());
        
        assertEquals("", accum.toString());
    }

}