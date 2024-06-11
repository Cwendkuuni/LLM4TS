package org.jsoup.parser;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Test;

public class XmlTreeBuilderTest {

    @Test
    public void testParse() {
        String xmlString = "<root><element attr=\"value\">Content</element></root>";
        Document xmlDoc = XmlTreeBuilder.parse(new StringReader(xmlString), "baseUri");
        assertNotNull(xmlDoc);
        Element root = xmlDoc.getRootElement();
        assertEquals("root", root.tagName());
        List<Node> elements = root.children();
        assertEquals(1, elements.size());
        Element element = (Element) elements.get(0);
        assertEquals("element", element.tagName());
        assertNotNull(element.attr("attr"));
        assertEquals("value", element.attr("attr"));
        assertEquals("Content", element.text());
    }

    @Test
    public void testProcessStartTag() {
        String xmlString = "<root><element attr=\"value\"></element></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = new Document();
        xmlTreeBuilder.initialiseParse(new StringReader(xmlString), "baseUri", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        xmlTreeBuilder.runParser();

        Element root = doc.getRootElement();
        assertEquals("root", root.tagName());

        List<Node> elements = root.children();
        assertEquals(1, elements.size());
        Element element = (Element) elements.get(0);
        assertEquals("element", element.tagName());
        assertNotNull(element.attr("attr"));
        assertEquals("value", element.attr("attr"));
        assertNull(element.text());
    }

    @Test
    public void testProcessEndTag() {
        String xmlString = "<root><element attr=\"value\"><subElement /></element></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = new Document();
        xmlTreeBuilder.initialiseParse(new StringReader(xmlString), "baseUri", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        xmlTreeBuilder.runParser();

        Element root = doc.getRootElement();
        assertEquals("root", root.tagName());

        List<Node> elements = root.children();
        assertEquals(1, elements.size());
        Element element = (Element) elements.get(0);
        assertEquals("element", element.tagName());
        assertNotNull(element.attr("attr"));
        assertEquals("value", element.attr("attr"));
        assertEquals(1, element.childNodes().size());
        assertTrue(element.childNodes().get(0) instanceof Element);
        assertEquals("subElement", element.childNodes().get(0).tagName());
        assertNull(element.text());
    }

    @Test
    public void testProcessComment() {
        String xmlString = "<-- This is a comment -->";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = new Document();
        xmlTreeBuilder.initialiseParse(new StringReader(xmlString), "baseUri", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        xmlTreeBuilder.runParser();

        List<Node> comments = doc.childNodes();
        assertEquals(1, comments.size());
        Comment comment = (Comment) comments.get(0);
        assertEquals("This is a comment", comment.text());
    }

    @Test
    public void testProcessCharacter() {
        String xmlString = "<root>&amp;Character;</root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = new Document();
        xmlTreeBuilder.initialiseParse(new StringReader(xmlString), "baseUri", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        xmlTreeBuilder.runParser();

        Element root = doc.getRootElement();
        assertEquals("root", root.tagName());
        List<Node> textNodes = root.childNodes();
        assertEquals(1, textNodes.size());
        TextNode textNode = (TextNode) textNodes.get(0);
        assertEquals("&amp;Character;", textNode.text());
    }

    @Test
    public void testProcessDoctype() {
        String xmlString = "<!DOCTYPE root [ <!ATTLIST element attr CDATA #REQUIRED > ]><root></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = new Document();
        xmlTreeBuilder.initialiseParse(new StringReader(xmlString), "baseUri", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        xmlTreeBuilder.runParser();

        DocumentType doctype = doc.doctype();
        assertEquals("root", doctype.name());
        Element root = doc.getRootElement();
        assertEquals("root", root.tagName());
        List<Attribute> attributes = doctype.attributes();
        assertEquals(1, attributes.size());
        Attribute attribute = attributes.get(0);
        assertEquals("element", attribute.getName());
        assertEquals("attr", attribute.getName());
        assertEquals(AttributeSpec.CDATA, attribute.getType());
        assertEquals(true, attribute.isRequired());
    }

    @Test (expected = AssertionError.class)
    public void testProcessUnexpectedTokenType() {
        String xmlString = "<root><![CDATA[Hello World]]>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = new Document();
        xmlTreeBuilder.initialiseParse(new StringReader(xmlString), "baseUri", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        xmlTreeBuilder.runParser();

        Validate.fail("Unexpected token type: " + Token.Type.CDATA);
    }

    @Test
    public void testInsertNode() {
        String xmlString = "<root><element attr=\"value\">Content</element></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = new Document();
        Element root = doc.add(new Element("root"));
        Element element = xmlTreeBuilder.insert(xmlTreeBuilder.parseFragment("<element attr=\"value\">Content</element>", "baseUri", ParseErrorList.noTracking(), ParseSettings.preserveCase).get(0));
        xmlTreeBuilder.insertNode(element);
        root.appendChild(element);

        assertEquals("root", root.tagName());
        assertEquals("element", element.tagName());
        assertEquals("value", element.attr("attr"));
        assertEquals("Content", element.text());
    }

    @Test
    public void testPopStackToClose() {
        String xmlString = "<root><element><subElement /></element></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = new Document();
        Element root = doc.add(new Element("root"));
        Element element = xmlTreeBuilder.insert(xmlTreeBuilder.parseFragment("<element><subElement />", "baseUri", ParseErrorList.noTracking(), ParseSettings.preserveCase).get(0));
        Element subElement = xmlTreeBuilder.insert(xmlTreeBuilder.parseFragment("<subElement />", "baseUri", ParseErrorList.noTracking(), ParseSettings.preserveCase).get(0));
        xmlTreeBuilder.insertNode(element);
        xmlTreeBuilder.insertNode(subElement);
        root.appendChild(element);
        element.appendChild(subElement);

        xmlTreeBuilder.popStackToClose(xmlTreeBuilder.parseFragment("<element />", "baseUri", ParseErrorList.noTracking(), ParseSettings.preserveCase).get(0));

        assertEquals(1, root.childNodes().size());
        assertEquals(0, element.childNodes().size());
    }
}