package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.ParseSettings;

import java.util.ArrayList;
import java.util.List;

public class XmlTreeBuilderTest {

    @Test
    public void testParseXml() {
        String xmlInput = "<root><element attr=\"value\"><subElement/></element></root>";
        ParseSettings settings = new ParseSettings();
        Document doc = new XmlTreeBuilder().parse(xmlInput, "http://example.com");

        Element root = doc.getRootElement();
        assertEquals("root", root.tagName());

        List<Element> elements = new ArrayList<>();
        List<Node> children = root.children();
        for (Node node : children) {
            if (node instanceof Element) {
                elements.add((Element) node);
            }
        }
        assertEquals(1, elements.size());
        Element element = elements.get(0);
        assertEquals("element", element.tagName());
        assertEquals("value", element.attribute("attr"));

        List<Node> subElementChildren = element.children();
        assertEquals(1, subElementChildren.size());
        Element subElement = (Element) subElementChildren.get(0);
        assertTrue(subElement.tagName().isEmpty());
    }

    @Test
    public void testParseXmlWithDoctype() {
        String xmlInput = "<!DOCTYPE root [ <!ATTLIST element attr CDATA #REQUIRED> ]><root><element attr=\"value\"><subElement/></element></root>";
        ParseSettings settings = new ParseSettings();
        Document doc = new XmlTreeBuilder().parse(xmlInput, "http://example.com");

        Element root = doc.getRootElement();
        assertEquals("root", root.tagName());

        List<Element> elements = new ArrayList<>();
        List<Node> children = root.children();
        for (Node node : children) {
            if (node instanceof Element) {
                elements.add((Element) node);
            }
        }
        assertEquals(1, elements.size());
        Element element = elements.get(0);
        assertEquals("element", element.tagName());
        assertEquals("value", element.attribute("attr"));

        DocumentType doctype = (DocumentType) root.child(0);
        assertEquals("root", doctype.nodeName());
        assertEquals("http://www.w3.org/2001/XMLSchema.dtd", doctype.systemIdentifier());
        assertEquals("", doctype.publicIdentifier());

        List<AttributeSpec> attributes = doctype.attributeSpecs();
        assertEquals(1, attributes.size());
        AttributeSpec attribute = attributes.get(0);
        assertEquals("element", attribute.elementName());
        assertEquals("attr", attribute.attributeName());
        assertEquals("CDATA", attribute.attributeType());
        assertEquals(AttributeSpec.REQUIRED, attribute.required());
    }

    @Test
    public void testParseXmlWithComment() {
        String xmlInput = "<!-- This is a comment-->" +
                "<root><element attr=\"value\"><subElement/></element></root>";
        ParseSettings settings = new ParseSettings();
        Document doc = new XmlTreeBuilder().parse(xmlInput, "http://example.com");

        Element root = doc.getRootElement();
        List<Node> children = root.children();
        Comment comment = (Comment) children.get(0);
        assertEquals("This is a comment-->", comment.text());
        assertNull(comment.baseUri());

        List<Element> elements = new ArrayList<>();
        for (Node node : children) {
            if (node instanceof Element) {
                elements.add((Element) node);
            }
        }
        assertEquals(1, elements.size());
        Element element = elements.get(0);
        assertEquals("element", element.tagName());
        assertEquals("value", element.attribute("attr"));
    }

    @Test
    public void testParseXmlWithCharacter() {
        String xmlInput = "<root>&#x27;<element attr=\"value\"><subElement/></element>&#xA;</root>";
        ParseSettings settings = new ParseSettings();
        Document doc = new XmlTreeBuilder().parse(xmlInput, "http://example.com");

        Element root = doc.getRootElement();
        List<Node> children = root.children();
        Text text = (Text) children.get(0);
        assertEquals("'", text.text());
        assertNull(text.baseUri());

        List<Element> elements = new ArrayList<>();
        for (Node node : children) {
            if (node instanceof Element) {
                elements.add((Element) node);
            }
        }
        assertEquals(1, elements.size());
        Element element = elements.get(0);
        assertEquals("element", element.tagName());
        assertEquals("value", element.attribute("attr"));
    }

    @Test
    public void testParseXmlWithError() {
        String xmlInput = "<root><element attr=\"value\"<subElement/></element></root>";
        ParseSettings settings = new ParseSettings();
        Document doc = new XmlTreeBuilder().parse(xmlInput, "http://example.com");
        ParseErrorList errors = doc. parseErrorList();

        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals(Token.StartTag, error.token().type());
        assertEquals(6, error.line());
        assertEquals(2, error.column());
        assertEquals("element", error.element().tagName());
        assertEquals("value", error.element().attribute("attr"));
        assertEquals("<subElement", error.message());
    }

    @Test
    public void testParseXmlFragment() {
        String xmlFragment = "<element attr=\"value\">Content</element>";
        ParseSettings settings = new ParseSettings();
        List<Node> fragments = new XmlTreeBuilder().parseFragment(xmlFragment, "http://example.com", ParseErrorList.noTracking(), settings);

        Document doc = new Document();
        doc.appendChild(fragments.get(0));

        Element element = (Element) doc.getRootElement();
        assertEquals("element", element.tagName());
        assertEquals("value", element.attribute("attr"));
        assertEquals("Content", element.text());
    }
}