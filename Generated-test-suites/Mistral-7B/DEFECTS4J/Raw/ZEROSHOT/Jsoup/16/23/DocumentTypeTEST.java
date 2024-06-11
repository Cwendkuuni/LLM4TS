package org.jsoup.nodes.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

/**
 * Tests for {@link DocumentType} class.
 */
public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        String name = "html";
        String publicId = "HTML";
        String systemId = "http://www.w3.org/TR/html4/strict.dtd";
        String baseUri = "http://example.com";

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        assertEquals("#doctype", documentType.nodeName());
        assertEquals(name, documentType.attr("name").getValue());
        assertEquals(publicId, documentType.attr("publicId").getValue());
        assertEquals(systemId, documentType.attr("systemId").getValue());
        assertEquals(baseUri, documentType.baseUri());
    }

    @Test
    public void testDocumentTypeCreationWithEmptyValues() {
        String name = "html";
        String publicId = "";
        String systemId = "";
        String baseUri = "http://example.com";

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        assertEquals("#doctype", documentType.nodeName());
        assertNull(documentType.attr("publicId"));
        assertNull(documentType.attr("systemId"));
        assertEquals(baseUri, documentType.baseUri());
    }

    @Test
    public void testDocumentTypeCreationWithOnlyPublicId() {
        String name = "html";
        String publicId = "HTML";
        String baseUri = "http://example.com";

        DocumentType documentType = new DocumentType(name, publicId, "", baseUri);

        assertEquals("#doctype", documentType.nodeName());
        assertEquals(publicId, documentType.attr("publicId").getValue());
        assertNull(documentType.attr("systemId"));
        assertEquals(baseUri, documentType.baseUri());
    }

    @Test
    public void testDocumentTypeCreationWithOnlySystemId() {
        String name = "html";
        String publicId = "";
        String systemId = "http://www.w3.org/TR/html4/strict.dtd";
        String baseUri = "http://example.com";

        DocumentType documentType = new DocumentType(name, "", systemId, baseUri);

        assertEquals("#doctype", documentType.nodeName());
        assertNull(documentType.attr("publicId"));
        assertEquals(systemId, documentType.attr("systemId").getValue());
        assertEquals(baseUri, documentType.baseUri());
    }

    @Test
    public void testDocumentTypeCreationWithAllValues() {
        String name = "html";
        String publicId = "HTML";
        String systemId = "http://www.w3.org/TR/html4/strict.dtd";
        String baseUri = "http://example.com";

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        assertEquals("#doctype", documentType.nodeName());
        assertEquals(publicId, documentType.attr("publicId").getValue());
        assertEquals(systemId, documentType.attr("systemId").getValue());
        assertEquals(baseUri, documentType.baseUri());
    }

    @Test
    public void testDocumentTypeOuterHtmlHead() {
        String name = "html";
        String publicId = "HTML";
        String systemId = "http://www.w3.org/TR/html4/strict.dtd";
        String baseUri = "http://example.com";

        Document document = new Document(baseUri);
        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        StringBuilder accum = new StringBuilder();

        documentType.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html PUBLIC \"HTML\" http://www.w3.org/TR/html4/strict.dtd>", accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlHeadWithEmptyPublicId() {
        String name = "html";
        String publicId = "";
        String systemId = "http://www.w3.org/TR/html4/strict.dtd";
        String baseUri = "http://example.com";

        Document document = new Document(baseUri);
        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        StringBuilder accum = new StringBuilder();

        documentType.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html", accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlHeadWithEmptySystemId() {
        String name = "html";
        String publicId = "HTML";
        String systemId = "";
        String baseUri = "http://example.com";

        Document document = new Document(baseUri);
        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        StringBuilder accum = new StringBuilder();

        documentType.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html PUBLIC \"HTML\"", accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlHeadWithOnlyPublicId() {
        String name = "html";
        String publicId = "HTML";
        String baseUri = "http://example.com";

        Document document = new Document(baseUri);
        DocumentType documentType = new DocumentType(name, publicId, "", baseUri);

        StringBuilder accum = new StringBuilder();

        documentType.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html PUBLIC \"HTML\"", accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlHeadWithOnlySystemId() {
        String name = "html";
        String publicId = "";
        String systemId = "http://www.w3.org/TR/html4/strict.dtd";
        String baseUri = "http://example.com";

        Document document = new Document(baseUri);
        DocumentType documentType = new DocumentType(name, "", systemId, baseUri);

        StringBuilder accum = new StringBuilder();

        documentType.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html <!ENTITY % NAME SYSTEM \"http://www.w3.org/TR/html4/strict.dtd\">", accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlHeadWithAllValues() {
        String name = "html";
        String publicId = "HTML";
        String systemId = "http://www.w3.org/TR/html4/strict.dtd";
        String baseUri = "http://example.com";

        Document document = new Document(baseUri);
        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        StringBuilder accum = new StringBuilder();

        documentType.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html PUBLIC \"HTML\" <!ENTITY % NAME SYSTEM \"http://www.w3.org/TR/html4/strict.dtd\">", accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlTail() {
        String name = "html";
        String publicId = "HTML";
        String systemId = "http://www.w3.org/TR/html4/strict.dtd";
        String baseUri = "http://example.com";

        Document document = new Document(baseUri);
        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        StringBuilder accum = new StringBuilder();

        documentType.outerHtmlTail(accum, 0, null);

        assertEquals("", accum.toString());
    }

    @Test
    public void testDocumentTypeAddAttribute() {
        String name = "html";
        String publicId = "HTML";
        String systemId = "http://www.w3.org/TR/html4/strict.dtd";
        String baseUri = "http://example.com";
        String attributeName = "quirkMode";
        String attributeValue = "true";

        Document document = new Document(baseUri);
        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);
        Attribute attribute = new Attribute("quirkMode", attributeValue);

        documentType.add(attribute);

        assertEquals("#doctype", documentType.nodeName());
        assertEquals(attributeValue, documentType.attr("quirkMode").getValue());
    }

    @Test
    public void testDocumentTypeAddChild() {
        String name = "html";
        String publicId = "HTML";
        String systemId = "http://www.w3.org/TR/html4/strict.dtd";
        String baseUri = "http://example.com";

        Document document = new Document(baseUri);
        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);
        Element element = new Element("html");

        documentType.add(element);

        assertEquals("#doctype", documentType.nodeName());
        assertEquals(documentType, element.parentNode());
        assertSame(document, documentType.ownerDocument());
    }

    @Test
    public void testDocumentTypeAddChildWithTextNode() {
        String name = "html";
        String publicId = "HTML";
        String systemId = "http://www.w3.org/TR/html4/strict.dtd";
        String baseUri = "http://example.com";

        Document document = new Document(baseUri);
        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);
        TextNode textNode = new TextNode("This is a test TextNode.");

        documentType.add(textNode);

        assertEquals("#doctype", documentType.nodeName());
        assertEquals(documentType, textNode.parentNode());
        assertSame(document, documentType.ownerDocument());
    }
}