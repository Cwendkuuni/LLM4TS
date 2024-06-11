package org.jsoup.nodes.test;

import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Assert;
import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        DocumentType documentType = new DocumentType("html", "publicID", "systemID", "baseUri");
        Assert.assertEquals("#doctype", documentType.nodeName());
        Assert.assertEquals("html", documentType.attr("name"));
        Assert.assertEquals("publicID", documentType.attr("publicId"));
        Assert.assertEquals("systemID", documentType.attr("systemId"));
        Assert.assertEquals("baseUri", documentType.baseUri());
    }

    @Test
    public void testEmptyDocumentTypeCreation() {
        DocumentType documentType = new DocumentType("html");
        Assert.assertEquals("#doctype", documentType.nodeName());
        Assert.assertNull(documentType.attr("name"));
        Assert.assertNull(documentType.attr("publicId"));
        Assert.assertNull(documentType.attr("systemId"));
        Assert.assertNull(documentType.baseUri());
    }

    @Test
    public void testDocumentTypeOuterHtmlHead() {
        DocumentType documentType = new DocumentType("html", "publicID", "systemID", "baseUri");
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);
        Assert.assertEquals("<!DOCTYPE html PUBLIC \"publicID\" SYSTEM \"systemID\">", accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlHeadWithNameOnly() {
        DocumentType documentType = new DocumentType("html");
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);
        Assert.assertEquals("<!DOCTYPE html>", accum.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testDocumentTypeOuterHtmlHeadWithNullAccumulator() {
        DocumentType documentType = new DocumentType("html", "publicID", "systemID", "baseUri");
        documentType.outerHtmlHead(null, 0, null);
    }

    @Test(expected = NullPointerException.class)
    public void testDocumentTypeOuterHtmlHeadWithNullOutputSettings() {
        DocumentType documentType = new DocumentType("html", "publicID", "systemID", "baseUri");
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);
    }

    @Test
    public void testDocumentTypeOuterHtmlTail() {
        DocumentType documentType = new DocumentType("html", "publicID", "systemID", "baseUri");
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlTail(accum, 0, null);
        Assert.assertEquals("", accum.toString());
    }

    @Test
    public void testDocumentTypeCloneNode() {
        DocumentType originalDocumentType = new DocumentType("html", "publicID", "systemID", "baseUri");
        DocumentType clonedDocumentType = (DocumentType) originalDocumentType.cloneNode(true);
        Assert.assertEquals(originalDocumentType.nodeName(), clonedDocumentType.nodeName());
        Assert.assertEquals(originalDocumentType.attr("name"), clonedDocumentType.attr("name"));
        Assert.assertEquals(originalDocumentType.attr("publicId"), clonedDocumentType.attr("publicId"));
        Assert.assertEquals(originalDocumentType.attr("systemId"), clonedDocumentType.attr("systemId"));
        Assert.assertEquals(originalDocumentType.baseUri(), clonedDocumentType.baseUri());
    }

    @Test
    public void testDocumentTypeToString() {
        DocumentType documentType = new DocumentType("html", "publicID", "systemID", "baseUri");
        String documentTypeString = documentType.toString();
        Assert.assertEquals("<html></html>", documentTypeString);
    }

    @Test
    public void testDocumentTypeToStringWithNameAndPublicId() {
        DocumentType documentType = new DocumentType("html", "publicID", "systemID", "baseUri");
        String documentTypeString = documentType.toString();
        Assert.assertTrue(documentTypeString.startsWith("<!DOCTYPE html PUBLIC \""));
        Assert.assertTrue(documentTypeString.endsWith(">"));
    }

    @Test
    public void testDocumentTypeToStringWithNameOnly() {
        DocumentType documentType = new DocumentType("html");
        String documentTypeString = documentType.toString();
        Assert.assertEquals("<!DOCTYPE html>", documentTypeString);
    }
}