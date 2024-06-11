package org.jsoup.nodes.test;

import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.junit.Assert;
import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeConstruction() {
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", "baseUri");
        Assert.assertEquals("#doctype", documentType.nodeName());
        Assert.assertEquals("html", documentType.attr("name"));
        Assert.assertEquals("publicId", documentType.attr("publicId"));
        Assert.assertEquals("systemId", documentType.attr("systemId"));
        Assert.assertEquals("baseUri", documentType.baseUri());
    }

    @Test
    public void testEmptyNameConstructor() {
        try {
            new DocumentType("", "", "", "");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testNullNameConstructor() {
        try {
            new DocumentType(null, "", "", "");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);
        Assert.assertEquals("<!DOCTYPE html PUBLIC \"publicId\" \"systemId\">", accum.toString());
    }

    @Test
    public void testEmptyPublicIdAndSystemIdOuterHtmlHead() {
        DocumentType documentType = new DocumentType("html", "", "", "baseUri");
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);
        Assert.assertEquals("<!DOCTYPE html>", accum.toString());
    }

    @Test
    public void testNullOuterHtmlSettingsOuterHtmlHead() {
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);
        Assert.assertEquals("<!DOCTYPE html PUBLIC \"publicId\" \"systemId\">", accum.toString());

        Document.OutputSettings outputSettings = null;
        documentType.outerHtmlHead(accum, 0, outputSettings);
        Assert.assertEquals("<!DOCTYPE html PUBLIC \"publicId\" \"systemId\">", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlTail(accum, 0, null);
        Assert.assertEquals("", accum.toString());
    }

    @Test
    public void testNullDocumentTypeOuterHtmlTail() {
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        DocumentType nullDocumentType = null;
        nullDocumentType.outerHtmlTail(accum, 0, null);
        documentType.outerHtmlTail(accum, 0, null);
        Assert.assertEquals("", accum.toString());
    }

    @Test
    public void testNodeName() {
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", "baseUri");
        Assert.assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testCloneNodeWithChildren() {
        Document doc = new Document();
        Element root = new Element("html");
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", "baseUri");
        root.appendChild(documentType);
        doc.appendChild(root);

        Node clonedNode = documentType.cloneNode(true);
        Element clonedRoot = (Element) doc.get(1);
        clonedRoot.appendChild(clonedNode);

        Node clonedDocumentType = clonedNode;
        Assert.assertEquals("#doctype", clonedDocumentType.nodeName());
        Assert.assertEquals("html", clonedDocumentType.attr("name"));
        Assert.assertEquals("publicId", clonedDocumentType.attr("publicId"));
        Assert.assertEquals("systemId", clonedDocumentType.attr("systemId"));
        Assert.assertEquals("baseUri", clonedDocumentType.baseUri());
    }

    @Test
    public void testCloneNodeWithoutChildren() {
        Document doc = new Document();
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", "baseUri");
        Node clonedNode = documentType.cloneNode(false);

        Assert.assertEquals("#doctype", clonedNode.nodeName());
        Assert.assertNull(clonedNode.baseUri());
        Assert.assertNull(clonedNode.attributes());
    }
}