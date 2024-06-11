package org.jsoup.nodes.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.Document.OutputSettings;
import org.junit.Test;

/**
 * Test class for DocumentType.
 */
public class DocumentTypeTest {

    /**
     * Test constructor with valid parameters.
     */
    @Test
    public void testDocumentTypeConstruction() {
        String name = "html";
        String publicId = "12345";
        String systemId = "http://example.com/dtd.dtd";
        String baseUri = "http://example.com";

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        assertEquals("#doctype", documentType.nodeName());
        assertEquals(name, documentType.attr(DocumentType.NAME));
        assertEquals(publicId, documentType.attr(DocumentType.PUBLIC_ID));
        assertEquals(systemId, documentType.attr(DocumentType.SYSTEM_ID));
        assertEquals(baseUri, documentType.baseUri());
    }

    /**
     * Test constructor without publicId and systemId.
     */
    @Test
    public void testDocumentTypeConstructionWithoutPublicIdAndSystemId() {
        String name = "html5";
        String baseUri = "http://example.com";

        DocumentType documentType = new DocumentType(name, null, null, baseUri);

        assertEquals("#doctype", documentType.nodeName());
        assertEquals(name, documentType.attr(DocumentType.NAME));
        assertNull(documentType.attr(DocumentType.PUBLIC_ID));
        assertNull(documentType.attr(DocumentType.SYSTEM_ID));
        assertEquals(baseUri, documentType.baseUri());
    }

    /**
     * Test setters and getters for name, publicId, and systemId.
     */
    @Test
    public void testDocumentTypeSettersAndGetters() {
        String name = "html";
        String publicId = "12345";
        String systemId = "http://example.com/dtd.dtd";
        String newName = "xhtml";
        String newPublicId = "67890";
        String newSystemId = "http://example2.com/dtd.dtd";

        DocumentType documentType = new DocumentType(name, publicId, systemId, null);

        documentType.attr(DocumentType.NAME, newName);
        documentType.attr(DocumentType.PUBLIC_ID, newPublicId);
        documentType.attr(DocumentType.SYSTEM_ID, newSystemId);

        assertEquals(newName, documentType.attr(DocumentType.NAME));
        assertEquals(newPublicId, documentType.attr(DocumentType.PUBLIC_ID));
        assertEquals(newSystemId, documentType.attr(DocumentType.SYSTEM_ID));
    }

    /**
     * Test nodeName method with different output settings.
     */
    @Test
    public void testDocumentTypeNodeNameWithDifferentOutputSettings() {
        DocumentType documentType = new DocumentType("html", null, null, null);

        OutputSettings htmlOutput = new OutputSettings().syntax(Document.OutPut.HTML);
        OutputSettings xmlOutput = new OutputSettings().syntax(Document.OutPut.XML);

        assertEquals("<!doctype html>", documentType.outerHtml(htmlOutput, -1, null));
        assertEquals("<!DOCTYPE html>", documentType.outerHtml(xmlOutput, -1, null));
    }

    /**
     * Test has method with different attributes.
     */
    @Test
    public void testDocumentTypeHasMethod() {
        DocumentType documentType = new DocumentType("html", null, null, null);

        Attributes attrs = new Attributes();
        attrs.put("pubId", "12345");
        attrs.put("sysId", "http://example.com/dtd.dtd");
        Node nodeWithAttributes = new Element("html").attributes(attrs);
        documentType.setParent((Element) nodeWithAttributes);

        assertTrue(documentType.has(DocumentType.PUBLIC_ID));
        assertTrue(documentType.has(DocumentType.SYSTEM_ID));
        assertFalse(documentType.has("nonExistent"));
    }
}