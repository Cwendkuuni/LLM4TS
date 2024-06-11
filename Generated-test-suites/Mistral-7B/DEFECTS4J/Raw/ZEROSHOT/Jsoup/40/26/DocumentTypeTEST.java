package org.jsoup.nodes.test;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link DocumentType} class.
 */
public class DocumentTypeTest {

    private Document document;

    @Before
    public void setUp() {
        document = new Document();
    }

    @Test
    public void testDocumentTypeCreation() {
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", document.baseUri());
        assertEquals("#doctype", documentType.nodeName());
        assertEquals("html", documentType.attr("name"));
        assertEquals("publicId", documentType.attr("publicId"));
        assertEquals("systemId", documentType.attr("systemId"));
        assertEquals(document.baseUri(), documentType.baseUri());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDocumentTypeCreationWithEmptyName() {
        new DocumentType("", "", "", document.baseUri());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDocumentTypeCreationWithNullName() {
        new DocumentType(null, "", "", document.baseUri());
    }

    @Test
    public void testDocumentTypeOuterHtmlHead() {
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", document.baseUri());
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);
        String expected = "<!DOCTYPE html PUBLIC \"publicId\" systemId>";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlHeadWithEmptyPublicId() {
        DocumentType documentType = new DocumentType("html", "", "systemId", document.baseUri());
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);
        String expected = "<!DOCTYPE html>";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlHeadWithEmptySystemId() {
        DocumentType documentType = new DocumentType("html", "publicId", "", document.baseUri());
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);
        String expected = "<!DOCTYPE html PUBLIC \"publicId\">";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlTail() {
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", document.baseUri());
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlTail(accum, 0, null);
        assertEquals("", accum.toString());
    }

    @Test
    public void testDocumentTypeParsing() {
        String html = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">";
        Document parsedDocument = Parser.parseBodyFragment(html);
        Node node = parsedDocument.selectFirst("#doctype");
        assertEquals(DocumentType.class, node.getClass());
        DocumentType documentType = (DocumentType) node;
        assertEquals("html", documentType.attr("name"));
        assertEquals("-//W3C//DTD HTML 4.01 Transitional//EN", documentType.attr("publicId"));
        assertEquals("http://www.w3.org/TR/html4/loose.dtd", documentType.attr("systemId"));
    }

    @Test
    public void testDocumentTypeParsingWithEmptyPublicId() {
        String html = "<!DOCTYPE html>";
        Document parsedDocument = Parser.parseBodyFragment(html);
        Node node = parsedDocument.selectFirst("#doctype");
        assertEquals(DocumentType.class, node.getClass());
        DocumentType documentType = (DocumentType) node;
        assertEquals("html", documentType.attr("name"));
        assertEquals("", documentType.attr("publicId"));
        assertEquals("", documentType.attr("systemId"));
    }
}