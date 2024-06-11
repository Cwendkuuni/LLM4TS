package org.jsoup.nodes.test;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

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
        // Test with valid arguments
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", document.baseUri());
        assertNotNull(documentType);
        assertEquals("html", documentType.nodeName());
        assertEquals("publicId", documentType.attr("publicId"));
        assertEquals("systemId", documentType.attr("systemId"));
        assertEquals(document.baseUri(), documentType.baseUri());

        // Test with empty name
        try {
            new DocumentType("", "publicId", "systemId", document.baseUri());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test with null baseUri
        try {
            new DocumentType("html", "publicId", "systemId", null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testDocumentTypeOuterHtml() {
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", document.baseUri());

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);
        documentType.outerHtmlTail(accum, 0, null);

        String expected = "<!DOCTYPE html PUBLIC \"publicId\" \"systemId\">";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testDocumentTypeParsing() {
        String html = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">";
        Document parsedDocument = new Parser().parse(html);
        Node node = parsedDocument.selectFirst("#doctype");

        assertSame(node.getClass(), DocumentType.class);
        DocumentType documentType = (DocumentType) node;
        assertEquals("html", documentType.nodeName());
        assertEquals("-//W3C//DTD HTML 4.01//EN", documentType.attr("publicId"));
        assertEquals("http://www.w3.org/TR/html4/strict.dtd", documentType.attr("systemId"));
        assertEquals("http://www.w3.org/TR/html4/strict.dtd", documentType.baseUri());
    }

    @Test
    public void testDocumentTypeHasNoChildren() {
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", document.baseUri());
        assertNull(documentType.childNodes().size());
    }

    @Test
    public void testDocumentTypeHasNoParent() {
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", document.baseUri());
        assertNull(documentType.parentNode());
    }

    @Test
    public void testDocumentTypeCanHaveTextChildren() {
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", document.baseUri());
        Element root = new Element("html");
        TextNode textNode = new TextNode("Hello World");
        root.appendChild(textNode);
        documentType.appendChild(root);

        Node child = documentType.childNodes().get(0);
        assertSame(child.getClass(), Element.class);
        Element element = (Element) child;
        assertSame(element.childNodes().get(0).getClass(), TextNode.class);
    }
}