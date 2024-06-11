package org.jsoup.nodes.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assume.assumeFalse;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.xml.XmlDocument;
import org.jsoup.parser.Parser;
import org.jsoup.selenium.htmlunit.HtmlUnitJsoup;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class DocumentTypeTest {

    private Document doc;

    @Before
    public void setUp() {
        doc = HtmlUnitJsoup.parse("<html><head></head><body></body></html>");
    }

    @Test
    public void testDocumentTypeCreation() {
        DocumentType documentType = new DocumentType("html", "HTML", "WWW.W3.org", doc.baseUri());
        assertNotNull(documentType);
        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testDocumentTypeCreationWithEmptyAttributes() {
        DocumentType documentType = new DocumentType("html", null, null, null);
        assertNotNull(documentType);
        assertEquals("#doctype", documentType.nodeName());
        assertFalse(documentType.has(DocumentType.PUBLIC_ID));
        assertFalse(documentType.has(DocumentType.SYSTEM_ID));
    }

    @Test
    public void testDocumentTypeCreationWithAllAttributes() {
        DocumentType documentType = new DocumentType("html", "PUBLIC", "DTD HTML", "DTD_LOCATION", doc.baseUri());
        assertNotNull(documentType);
        assertEquals("#doctype", documentType.nodeName());
        assertTrue(documentType.has(DocumentType.PUBLIC_KEY));
        assertEquals("PUBLIC", documentType.attr(DocumentType.PUBLIC_ID));
        assertEquals("DTD HTML", documentType.attr(DocumentType.NAME));
        assertEquals("DTD_LOCATION", documentType.attr(DocumentType.SYSTEM_ID));
    }

    @Test
    public void testDocumentTypeCreationWithInvalidBaseUri() {
        assumeFalse(doc.baseUri().endsWith("http://"));
        assertThrows(IllegalArgumentException.class, () -> new DocumentType("html", null, null, doc.baseUri()));
    }

    @Test
    public void testDocumentTypeOuterHtmlHead() throws IOException {
        DocumentType documentType = new DocumentType("html", null, null, null);
        Appendable appendable = new StringBuilder();
        documentType.outerHtmlHead(appendable, 0, Document.OutputSettings.syntaxHtml5());
        String expected = "<!DOCTYPE html>";
        assertEquals(expected, appendable.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlHeadWithPublicId() throws IOException {
        DocumentType documentType = new DocumentType("html", "PUBLIC", null, null, doc.baseUri());
        Appendable appendable = new StringBuilder();
        documentType.outerHtmlHead(appendable, 0, Document.OutputSettings.syntaxHtml5());
        String expected = "<!DOCTYPE html PUBLIC \"" + doc.baseUri() + "/-//W3C//DTD HTML 4.01//EN\"";
        assertEquals(expected, appendable.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlHeadWithSystemId() throws IOException {
        DocumentType documentType = new DocumentType("html", null, "DTD_LOCATION", null, doc.baseUri());
        Appendable appendable = new StringBuilder();
        documentType.outerHtmlHead(appendable, 0, Document.OutputSettings.syntaxHtml5());
        String expected = "<!DOCTYPE html SYSTEM \"" + doc.baseUri() + "/DTD_LOCATION\"";
        assertEquals(expected, appendable.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlHeadWithPublicAndSystemId() throws IOException {
        DocumentType documentType = new DocumentType("html", "PUBLIC", "DTD_LOCATION", null, doc.baseUri());
        Appendable appendable = new StringBuilder();
        documentType.outerHtmlHead(appendable, 0, Document.OutputSettings.syntaxHtml5());
        String expected = "<!DOCTYPE html PUBLIC \"" + doc.baseUri() + "/-//W3C//DTD HTML 4.01//EN\" SYSTEM \"" + doc.baseUri() + "/DTD_LOCATION\"";
        assertEquals(expected, appendable.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlTail() throws IOException {
        DocumentType documentType = new DocumentType("html", null, null, null);
        Appendable appendable = new StringBuilder();
        documentType.outerHtmlTail(appendable, 0, Document.OutputSettings.syntaxHtml5());
        assertEquals("", appendable.toString());
    }

    @Test
    public void testDocumentTypeHasAttribute() {
        DocumentType documentType = new DocumentType("html", "PUBLIC", "DTD_LOCATION", null, doc.baseUri());
        assertTrue(documentType.has(DocumentType.PUBLIC_KEY));
        assertTrue(documentType.has(DocumentType.NAME));
        assertTrue(documentType.has(DocumentType.SYSTEM_ID));
        assertFalse(documentType.has("unknownAttribute"));
    }

    @Test
    public void testDocumentTypeParse() throws IOException {
        String html = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" SYSTEM \"DTD_LOCATION\">";
        Document document = Parser.parse(html);
        List<Node> nodes = document.body().childNodes();
        Node firstNode = nodes.get(0);
        assertEquals(Node.DOCTYPE_NODE, firstNode.nodeType());
        DocumentType documentType = (DocumentType) firstNode;
        assertEquals("html", documentType.nodeName());
        assertTrue(documentType.has(DocumentType.PUBLIC_KEY));
        assertEquals("PUBLIC", documentType.attr(DocumentType.PUBLIC_ID));
        assertEquals("DTD HTML", documentType.attr(DocumentType.NAME));
        assertEquals("DTD_LOCATION", documentType.attr(DocumentType.SYSTEM_ID));
    }

    @Test
    public void testDocumentTypeParseWithEmptyAttributes() throws IOException {
        String html = "<!DOCTYPE html>";
        Document document = Parser.parse(html);
        List<Node> nodes = document.body().childNodes();
        Node firstNode = nodes.get(0);
        assertEquals(Node.DOCTYPE_NODE, firstNode.nodeType());
        DocumentType documentType = (DocumentType) firstNode;
        assertEquals("html", documentType.nodeName());
        assertFalse(documentType.has(DocumentType.PUBLIC_ID));
        assertFalse(documentType.has(DocumentType.SYSTEM_ID));
    }

    @Test
    public void testDocumentTypeParseWithInvalidXml() throws IOException {
        String html = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML <version>4.01//EN\" SYSTEM \"DTD_LOCATION\">";
        Document document = Parser.parse(html);
        List<Node> nodes = document.body().childNodes();
        Node firstNode = nodes.get(0);
        assertEquals(Element.ElementNode.class, firstNode.nodeType());
        Element rootElement = (Element) firstNode;
        List<Attribute> attributes = rootElement.attributes();
        assertEquals(2, attributes.size());
        Attribute nameAttribute = attributes.get(0);
        assertEquals("html", nameAttribute.value());
        Attribute versionAttribute = attributes.get(1);
        assertEquals("html", versionAttribute.name());
        assertEquals("<version>", versionAttribute.value());
    }
}