package org.jsoup.nodes.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.helper.StringUtil;

import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        String name = "html";
        String publicId = "HTML";
        String systemId = "http://www.w3.org/TR/html4/strict.dtd";
        String baseUri = "https://www.example.com";

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        assertEquals("#doctype", documentType.nodeName());
        assertEquals(name, documentType.attr("name"));
        assertEquals(publicId, documentType.attr("publicId"));
        assertEquals(systemId, documentType.attr("systemId"));
        assertEquals(baseUri, documentType.baseUri());
    }

    @Test
    public void testEmptyDocumentTypeCreation() {
        DocumentType documentType = new DocumentType("", "", "", "");

        assertEquals("#doctype", documentType.nodeName());
        assertNull(documentType.attr("name"));
        assertNull(documentType.attr("publicId"));
        assertNull(documentType.attr("systemId"));
        assertNull(documentType.baseUri());
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType documentType = new DocumentType("html", "HTML", "http://www.w3.org/TR/html4/strict.dtd", "https://www.example.com");

        StringBuilder accum = new StringBuilder();

        documentType.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html PUBLIC \"HTML\" http://www.w3.org/TR/html4/strict.dtd>", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithPublicIdOnly() {
        DocumentType documentType = new DocumentType("html", "HTML", "", "https://www.example.com");

        StringBuilder accum = new StringBuilder();

        documentType.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html PUBLIC \"HTML\"", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithSystemIdOnly() {
        DocumentType documentType = new DocumentType("html", "", "http://www.w3.org/TR/html4/strict.dtd", "https://www.example.com");

        StringBuilder accum = new StringBuilder();

        documentType.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html <systemId>", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithBothPublicIdAndSystemId() {
        DocumentType documentType = new DocumentType("html", "HTML PUBLIC", "http://www.w3.org/TR/html4/strict.dtd", "https://www.example.com");

        StringBuilder accum = new StringBuilder();

        documentType.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html PUBLIC \"HTML PUBLIC\" <publicId> <systemId>", accum.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullName() {
        DocumentType documentType = new DocumentType(null, "HTML", "http://www.w3.org/TR/html4/strict.dtd", "https://www.example.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullPublicId() {
        DocumentType documentType = new DocumentType("html", null, "http://www.w3.org/TR/html4/strict.dtd", "https://www.example.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullSystemId() {
        DocumentType documentType = new DocumentType("html", "HTML", null, "https://www.example.com");
    }

    @Test(expected = NullPointerException.class)
    public void testNullBaseUri() {
        DocumentType documentType = new DocumentType("html", "HTML", "http://www.w3.org/TR/html4/strict.dtd", null);
    }
}