package org.jsoup.nodes.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", "baseUri");
        Assert.assertEquals("#doctype", documentType.nodeName());
        Assert.assertEquals("html", documentType.attr("name"));
        Assert.assertEquals("publicId", documentType.attr("publicId"));
        Assert.assertEquals("systemId", documentType.attr("systemId"));
        Assert.assertEquals("baseUri", documentType.baseUri());
    }

    @Test
    public void testEmptyDocumentTypeCreation() {
        DocumentType documentType = new DocumentType("html");
        Assert.assertEquals("#doctype", documentType.nodeName());
        Assert.assertNull(documentType.attr("publicId"));
        Assert.assertNull(documentType.attr("systemId"));
        Assert.assertNull(documentType.baseUri());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullNameDocumentTypeCreation() {
        new DocumentType(null, "", "", "");
    }

    @Test
    public void testDocumentTypeOuterHtmlHead() {
        Document document = new Document();
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, document.outputSettings());
        Assert.assertEquals("<!DOCTYPE html PUBLIC \"publicId\" systemId>\n", accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlHeadWithEmptyPublicId() {
        Document document = new Document();
        DocumentType documentType = new DocumentType("html", "", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, document.outputSettings());
        Assert.assertEquals("<!DOCTYPE html SYSTEM systemId>\n", accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlHeadWithEmptySystemId() {
        Document document = new Document();
        DocumentType documentType = new DocumentType("html", "publicId", "", "baseUri");
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, document.outputSettings());
        Assert.assertEquals("<!DOCTYPE html PUBLIC \"publicId\" \">\n", accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlHeadWithEmptyPublicIdAndSystemId() {
        Document document = new Document();
        DocumentType documentType = new DocumentType("html", "", "", "baseUri");
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, document.outputSettings());
        Assert.assertEquals("<!DOCTYPE html>\n", accum.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullAccumulatorDocumentTypeOuterHtmlHead() {
        Document document = new Document();
        DocumentType documentType = new DocumentType("html", "", "", "");
        documentType.outerHtmlHead(null, 0, document.outputSettings());
    }

    @Test
    public void testDocumentTypeOuterHtmlTail() {
        Document document = new Document();
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlTail(accum, 0, document.outputSettings());
        // expected empty string
        Assert.assertEquals("", accum.toString());
    }
}