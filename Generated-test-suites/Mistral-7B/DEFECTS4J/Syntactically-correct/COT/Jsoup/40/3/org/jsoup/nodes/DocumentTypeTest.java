package org.jsoup.nodes.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Document;

/**
 * Test class for DocumentType.
 */
public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", "baseUri");
        Assert.assertEquals("#doctype", documentType.nodeName());
        Assert.assertEquals("html", documentType.attr("name"));
        Assert.assertEquals("publicId", documentType.attr("publicId"));
        Assert.assertEquals("systemId", documentType.attr("systemId"));
        Assert.assertEquals("baseUri", documentType.baseUri().toString());
    }

    @Test
    public void testEmptyNameCreation() {
        try {
            new DocumentType("", "publicId", "systemId", "baseUri");
            fail("Expected IllegalArgumentException due to empty name");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testNullNameCreation() {
        try {
            new DocumentType(null, "publicId", "systemId", "baseUri");
            fail("Expected IllegalArgumentException due to null name");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testOuterHtmlHead() {
        Document document = new Document();
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", "baseUri");
        Element documentTypeElement = document.parseBodyFragment("<!DOCTYPE html>");
        documentTypeElement.outerHtmlHead(new StringBuilder(), 0, null);

        StringBuilder expectedHead = new StringBuilder("<!DOCTYPE html>");
        StringBuilder actualHead = documentTypeElement.outerHtml();

        assertEquals(expectedHead.toString(), actualHead.toString());
    }

    @Test
    public void testEmptyPublicIdOuterHtmlHead() {
        Document document = new Document();
        DocumentType documentType = new DocumentType("html", "", "systemId", "baseUri");
        Element documentTypeElement = document.parseBodyFragment("<!DOCTYPE html>");
        documentTypeElement.outerHtmlHead(new StringBuilder(), 0, null);

        StringBuilder expectedHead = new StringBuilder("<!DOCTYPE html>");
        StringBuilder actualHead = documentTypeElement.outerHtml();

        assertEquals(expectedHead.toString(), actualHead.toString());
    }

    @Test
    public void testEmptySystemIdOuterHtmlHead() {
        Document document = new Document();
        DocumentType documentType = new DocumentType("html", "publicId", "", "baseUri");
        Element documentTypeElement = document.parseBodyFragment("<!DOCTYPE html>");
        documentTypeElement.outerHtmlHead(new StringBuilder(), 0, null);

        StringBuilder expectedHead = new StringBuilder("<!DOCTYPE html>");
        StringBuilder actualHead = documentTypeElement.outerHtml();

        assertEquals(expectedHead.toString(), actualHead.toString());
    }

    @Test
    public void testNonEmptyPublicIdAndSystemIdOuterHtmlHead() {
        Document document = new Document();
        DocumentType documentType = new DocumentType("html", "publicId1", "systemId1", "baseUri");
        Element documentTypeElement = document.parseBodyFragment("<!DOCTYPE html PUBLIC \"publicId1\" \"systemId1\">");
        documentTypeElement.outerHtmlHead(new StringBuilder(), 0, null);

        StringBuilder expectedHead = new StringBuilder("<!DOCTYPE html PUBLIC \"publicId1\" \"systemId1\">");
        StringBuilder actualHead = documentTypeElement.outerHtml();

        assertEquals(expectedHead.toString(), actualHead.toString());
    }
}