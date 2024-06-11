package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;

import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        String name = "html";
        String publicId = "publicId";
        String systemId = "systemId";
        String baseUri = "baseUri";
        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        assertEquals("#doctype", documentType.nodeName());
        assertEquals(name, documentType.attrs().get("name"));
        assertEquals(publicId, documentType.attrs().get("publicId"));
        assertEquals(systemId, documentType.attrs().get("systemId"));
        assertEquals(baseUri, documentType.baseUri());
    }

    @Test
    public void testDocumentTypeNullAttributes() {
        DocumentType documentType = new DocumentType("html", null, null, null);

        assertEquals("#doctype", documentType.nodeName());
        assertNull(documentType.attrs().get("publicId"));
        assertNull(documentType.attrs().get("systemId"));
        assertNull(documentType.baseUri());
    }

    @Test
    public void testDocumentTypeWithAttributes() {
        String name = "html";
        String publicId = "publicId";
        String systemId = "systemId";
        String baseUri = "baseUri";
        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        Attributes attrs = documentType.attrs();
        assertEquals("html", attrs.get("name"));
        assertEquals("publicId", attrs.get("publicId"));
        assertEquals("systemId", attrs.get("systemId"));
        assertEquals("baseUri", attrs.get("baseURI"));

        Document doc = new Document();
        Element documentTypeElement = doc.parseBodyFragment(documentType.outerHtml());

        Element doctypeNode = documentTypeElement.child(0);
        assertEquals("#doctype", doctypeNode.nodeName());
        assertEquals("html", doctypeNode.attrs().get("name"));
        assertEquals("publicId", doctypeNode.attrs().get("publicId"));
        assertEquals("systemId", doctypeNode.attrs().get("systemId"));
        assertNull(doctypeNode.baseUri());
    }

    @Test
    public void testDocumentTypeWithoutAttributes() {
        String name = "html";
        DocumentType documentType = new DocumentType(name, null, null, null);

        assertEquals("#doctype", documentType.nodeName());
        assertNull(documentType.attrs().get("publicId"));
        assertNull(documentType.attrs().get("systemId"));
        assertNull(documentType.baseUri());

        Document doc = new Document();
        Element documentTypeElement = doc.parseBodyFragment(documentType.outerHtml());

        Element doctypeNode = documentTypeElement.child(0);
        assertEquals("#doctype", doctypeNode.nodeName());
        assertEquals("html", doctypeNode.attrs().get("name"));
        assertNull(doctypeNode.attrs().get("publicId"));
        assertNull(doctypeNode.attrs().get("systemId"));
        assertNull(doctypeNode.baseUri());
    }

    @Test
    public void testDocumentTypeOuterHtml() {
        String name = "html";
        String publicId = "publicId";
        String systemId = "systemId";
        String baseUri = "baseUri";
        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);
        documentType.outerHtmlTail(accum, 0, null);

        String expected = "<!DOCTYPE html" +
                (StringUtil.isBlank(publicId) ? "" : " PUBLIC \"\"") +
                (StringUtil.isBlank(systemId) ? "" : " \"systemId\"") +
                ">";

        assertEquals(expected, documentType.outerHtml());
        assertEquals(expected, accum.toString());
    }
}