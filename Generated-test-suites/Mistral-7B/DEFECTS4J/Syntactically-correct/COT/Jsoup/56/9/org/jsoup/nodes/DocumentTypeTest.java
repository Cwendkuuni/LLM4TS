package org.jsoup.nodes.test;

import static org.junit.Assert.*;

import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document.OutputSettings;
import org.junit.Test;

/**
 * Test class for DocumentType.
 */
public class DocumentTypeTest {

    @Test
    public void testDocumentTypeConstructor() {
        String name = "html";
        String publicId = "publicId";
        String systemId = "systemId";
        String baseUri = "http://example.com";

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        assertEquals("#doctype", documentType.nodeName());
        assertEquals(name, documentType.attr(DocumentType.NAME));
        assertEquals(publicId, documentType.attr(DocumentType.PUBLIC_ID));
        assertEquals(systemId, documentType.attr(DocumentType.SYSTEM_ID));
        assertEquals(baseUri, documentType.baseUri());
    }

    @Test
    public void testDocumentTypeConstructorWithEmptyAttributes() {
        DocumentType documentType = new DocumentType("html", "", "", "");

        assertEquals("#doctype", documentType.nodeName());
        assertNull(documentType.attr(DocumentType.PUBLIC_ID));
        assertNull(documentType.attr(DocumentType.SYSTEM_ID));
        assertEquals("", documentType.baseUri());
    }

    @Test
    public void testNodeName() {
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", "http://example.com");

        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testOuterHtmlHead() throws Exception {
        Document document = new Document();
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", "http://example.com");
        Element element = document.appendElement("html");
        element.appendChild(documentType);

        OutputSettings out = new OutputSettings();
        out.syntax(Document.OutputSettings.Syntax.html);

        Appendable accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, out);

        String expected = "<!doctype html>";
        assertEquals(expected, accum.toString());

        out.syntax(Document.OutputSettings.Syntax.xml);
        documentType.outerHtmlHead(accum, 0, out);

        expected = "<!DOCTYPE html>";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testOuterHtmlTail() throws Exception {
        Document document = new Document();
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", "http://example.com");
        Element element = document.appendElement("html");
        element.appendChild(documentType);

        Appendable accum = new StringBuilder();
        documentType.outerHtmlTail(accum, 0, new OutputSettings());

        assertEquals("", accum.toString());
    }

    @Test
    public void testHasAttribute() {
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", "http://example.com");

        assertTrue(documentType.has(DocumentType.NAME));
        assertFalse(documentType.has("nonExistentAttribute"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionWhenEmptyName() {
        new DocumentType("", "publicId", "systemId", "http://example.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionWhenNullName() {
        new DocumentType(null, "publicId", "systemId", "http://example.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionWhenEmptyPublicId() {
        new DocumentType("html", "", "systemId", "http://example.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionWhenNullPublicId() {
        new DocumentType("html", null, "systemId", "http://example.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionWhenEmptySystemId() {
        new DocumentType("html", "publicId", "", "http://example.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionWhenNullSystemId() {
        new DocumentType("html", "publicId", null, "http://example.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionWhenEmptyBaseUri() {
        new DocumentType("html", "publicId", "systemId", "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionWhenNullBaseUri() {
        new DocumentType("html", "publicId", "systemId", null);
    }
}