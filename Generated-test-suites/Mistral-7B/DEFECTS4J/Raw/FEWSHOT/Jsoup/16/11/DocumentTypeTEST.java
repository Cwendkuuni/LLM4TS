package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        String name = "html";
        String publicId = "somePublicId";
        String systemId = "someSystemId";
        String baseUri = "http://example.com";

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        assertEquals("html", documentType.nodeName());
        assertEquals("#doctype", documentType.nodeName());
        assertEquals(name, documentType.attr("name"));
        assertEquals(publicId, documentType.attr("publicId"));
        assertEquals(systemId, documentType.attr("systemId"));
        assertEquals(baseUri, documentType.baseUri());
    }

    @Test
    public void testEmptyDocumentTypeCreation() {
        DocumentType documentType = new DocumentType("", "", "", "");

        assertEquals("", documentType.nodeName());
        assertEquals("#doctype", documentType.nodeName());
        assertNull(documentType.attr("name"));
        assertNull(documentType.attr("publicId"));
        assertNull(documentType.attr("systemId"));
        assertNull(documentType.baseUri());
    }

    @Test
    public void testDocumentTypeOuterHtml() {
        DocumentType documentType = new DocumentType("html", "somePublicId", "someSystemId", "http://example.com");

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html", accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlWithPublicId() {
        DocumentType documentType = new DocumentType("html", "somePublicId", "someSystemId", "http://example.com");

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html PUBLIC \"somePublicId\"", accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlWithSystemId() {
        DocumentType documentType = new DocumentType("html", "somePublicId", "someSystemId", "http://example.com");

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html PUBLIC \"somePublicId\" systemID=\"someSystemId\">", accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlWithPublicIdAndSystemId() {
        DocumentType documentType = new DocumentType("html", "somePublicId", "someSystemId", "http://example.com");

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html PUBLIC \"somePublicId\" systemID=\"someSystemId\">", accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlTail() {
        DocumentType documentType = new DocumentType("html", "somePublicId", "someSystemId", "http://example.com");

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlTail(accum, 0, null);

        // The outerHtmlTail method does not add anything to the accumulator in this class, so the result should be empty.
        assertEquals("", accum.toString());
    }

    @Test
    public void testDocumentTypeChildNodes() {
        DocumentType documentType = new DocumentType("html", "somePublicId", "someSystemId", "http://example.com");
        Element comment = new Element("!-- A comment -->");
        TextNode textNode = new TextNode("Some text.");

        documentType.appendChild(comment);
        documentType.appendChild(textNode);

        Element child = documentType.child(0);
        assertSame(comment, child);

        child = documentType.child(1);
        assertSame(textNode, child);
    }
}