package org.jsoup.nodes;
import org.jsoup.nodes.DocumentType;

import org.junit.Assert;

import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testNodeName() {
        DocumentType documentType = new DocumentType("name", "publicId", "systemId", "baseUri");
        Assert.assertEquals("#doctype", documentType.nodeName());
    }
    
    @Test
    public void testOuterHtmlHead() {
        DocumentType documentType = new DocumentType("name", "publicId", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();
        documentType.outerHtmlHead(accum, depth, out);
        Assert.assertEquals("<!DOCTYPE html PUBLIC \"publicId\">", accum.toString());
    }
    
    @Test
    public void testOuterHtmlHead_NoPublicId() {
        DocumentType documentType = new DocumentType("name", "", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();
        documentType.outerHtmlHead(accum, depth, out);
        Assert.assertEquals("<!DOCTYPE html systemId\">", accum.toString());
    }
    
    @Test
    public void testOuterHtmlHead_NoSystemId() {
        DocumentType documentType = new DocumentType("name", "publicId", "", "baseUri");
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();
        documentType.outerHtmlHead(accum, depth, out);
        Assert.assertEquals("<!DOCTYPE html PUBLIC \"publicId\">", accum.toString());
    }
    
    @Test
    public void testOuterHtmlHead_NoPublicIdAndSystemId() {
        DocumentType documentType = new DocumentType("name", "", "", "baseUri");
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();
        documentType.outerHtmlHead(accum, depth, out);
        Assert.assertEquals("<!DOCTYPE html>", accum.toString());
    }
    
    @Test
    public void testOuterHtmlTail() {
        DocumentType documentType = new DocumentType("name", "publicId", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();
        documentType.outerHtmlTail(accum, depth, out);
        Assert.assertEquals("", accum.toString());
    }
    
}