package org.jsoup.parser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.helper.Validate;
import org.junit.Test;

public class XmlTreeBuilderTest {

    @Test
    public void testXmlTreeBuilderDefaultSettings() {
        ParseSettings settings = new XmlTreeBuilder().defaultSettings();
        assertEquals(ParseSettings.preserveCase, settings);
    }

    @Test
    public void testParseXmlWithReader() {
        String xml = "<root><element attr=\"value\">Content</element></root>";
        String baseUri = "https://example.com";
        Document doc = new XmlTreeBuilder().parse(new StringReader(xml), baseUri);
        assertNotNull(doc);
        assertEquals("<root><element attr=\"value\">Content</element></root>", doc.html());

        Element root = doc.rootElement();
        assertNotNull(root);
        assertEquals("root", root.tagName());
        Element element = root.child(0);
        assertNotNull(element);
        assertEquals("element", element.tagName());
        assertEquals("value", element.attr("attr"));
        assertEquals("Content", element.text());
    }

    @Test
    public void testParseXmlWithString() {
        String xml = "<root><element attr=\"value\">Content</element></root>";
        String baseUri = "https://example.com";
        Document doc = new XmlTreeBuilder().parse(xml, baseUri);
        assertNotNull(doc);
        assertEquals("<root><element attr=\"value\">Content</element></root>", doc.html());

        Element root = doc.rootElement();
        assertNotNull(root);
        assertEquals("root", root.tagName());
        Element element = root.child(0);
        assertNotNull(element);
        assertEquals("element", element.tagName());
        assertEquals("value", element.attr("attr"));
        assertEquals("Content", element.text());
    }

    @Test
    public void testInitialiseParse() {
        String xml = "<root><element attr=\"value\">Content</element></root>";
        String baseUri = "https://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;

        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse(new StringReader(xml), baseUri, errors, settings);

        Document doc = xmlTreeBuilder.doc;
        assertNotNull(doc);
        Element root = doc.rootElement();
        assertNotNull(root);
        assertEquals("root", root.tagName());
        assertEquals(baseUri, doc.baseUri());
    }

    @Test
    public void testProcess() {
        String xml = "<root><element attr=\"value\">Content</element></root>";
        String baseUri = "https://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;

        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse(new StringReader(xml), baseUri, errors, settings);

        Token token;
        while ((token = xmlTreeBuilder.nextToken()) != null) {
            xmlTreeBuilder.process(token);
            if (token.type == Token.TokenType.StartTag) {
                assertNotNull(xmlTreeBuilder.doc.createElement(token.asStartTag().name()));
            }
        }
    }

    @Test
    public void testInsertNode() {
        String xml = "<root><element attr=\"value\">Content</element></root>";
        String baseUri = "https://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;

        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse(new StringReader(xml), baseUri, errors, settings);

        Element root = xmlTreeBuilder.doc.rootElement();
        Element element = new Element("element", baseUri, settings.normalizeAttributes(new Token.StartTag("element")));
        xmlTreeBuilder.insertNode(element);
        root.appendChild(element);
        assertEquals(1, root.childNodeSize());
    }

    @Test
    public void testInsertComment() {
        String xml = "<root><!-- comment --></root>";
        String baseUri = "https://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;

        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse(new StringReader(xml), baseUri, errors, settings);

        Token.Comment commentToken = xmlTreeBuilder.nextToken();
        xmlTreeBuilder.insert(commentToken);
    }

    @Test
    public void testInsertCharacter() {
        String xml = "<root>&#x304;Content</root>";
        String baseUri = "https://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;

        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse(new StringReader(xml), baseUri, errors, settings);

        Token.Character characterToken = xmlTreeBuilder.nextToken();
        xmlTreeBuilder.insert(characterToken);
    }

    @Test
    public void testInsertDocumentType() {
        String xml = "<!DOCTYPE root [ <!ATTLIST element attr CDATA #REQUIRED> ] ><root><element attr=\"value\">Content</element></root>";
        String baseUri = "https://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;

        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse(new StringReader(xml), baseUri, errors, settings);

        Token.Doctype doctypeToken = xmlTreeBuilder.nextToken();
        xmlTreeBuilder.insert(doctypeToken);
    }

    @Test
    public void testPopStackToClose() {
        String xml = "<root><a><b></b></a></root>";
        String baseUri = "https://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;

        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse(new StringReader(xml), baseUri, errors, settings);

        Element a = xmlTreeBuilder.insert(xmlTreeBuilder.nextToken().asStartTag());
        Element b = xmlTreeBuilder.insert(xmlTreeBuilder.nextToken().asStartTag());
        xmlTreeBuilder.popStackToClose(xmlTreeBuilder.nextToken().asEndTag());
        assertSame(a, xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1));
    }

    @Test
    public void testParseFragment() {
        String xmlFragment = "<element attr=\"value\">Content</element>";
        String baseUri = "https://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;

        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, baseUri, errors, settings);
        assertEquals(1, nodes.size());
        Node node = nodes.get(0);
        assertEquals(Element.class, node.getClass());
        Element element = (Element) node;
        assertEquals("element", element.tagName());
        assertEquals("value", element.attr("attr"));
        assertEquals("Content", element.text());
    }
}