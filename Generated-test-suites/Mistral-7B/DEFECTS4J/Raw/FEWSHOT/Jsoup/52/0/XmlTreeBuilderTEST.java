package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.jsoup.nodes.*;
import org.jsoup.parser.XmlTreeBuilder;

import java.util.List;

public class XmlTreeBuilderTest {

    @Test
    public void testParseFragmentEmpty() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        List<Node> nodes = xmlTreeBuilder.parseFragment("", "", new ParseErrorList());
        assertNull(nodes);
    }

    @Test
    public void testParseFragmentWithSimpleElements() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xml = "<root><element>Hello</element></root>";
        List<Node> nodes = xmlTreeBuilder.parseFragment(xml, "", new ParseErrorList());
        Element root = (Element) nodes.get(0);
        Element element = (Element) root.childNodes().get(0);
        assertEquals("Hello", element.text());
    }

    @Test
    public void testParseFragmentWithSelfClosingElements() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xml = "<root><element /> </root>";
        List<Node> nodes = xmlTreeBuilder.parseFragment(xml, "", new ParseErrorList());
        Element root = (Element) nodes.get(0);
        assertEquals(1, root.childNodes().size());
        assertNull(root.childNodes().get(0).childNodes().get(0));
    }

    @Test
    public void testParseFragmentWithAttributes() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xml = "<root><element attr1='value1' attr2='value2' /> </root>";
        List<Node> nodes = xmlTreeBuilder.parseFragment(xml, "", new ParseErrorList());
        Element root = (Element) nodes.get(0);
        Element element = (Element) root.childNodes().get(0);
        assertEquals("value1", element.attr("attr1"));
        assertEquals("value2", element.attr("attr2"));
    }

    @Test
    public void testParseFragmentWithNestedElements() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xml = "<root><element><subElement>Nested</subElement></element></root>";
        List<Node> nodes = xmlTreeBuilder.parseFragment(xml, "", new ParseErrorList());
        Element root = (Element) nodes.get(0);
        Element element = (Element) root.childNodes().get(0);
        Element subElement = (Element) element.childNodes().get(0);
        assertEquals("Nested", subElement.text());
    }

    @Test
    public void testParseFragmentWithComments() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xml = "<root><!--- This is a comment ---><element>Hello</element></root>";
        List<Node> nodes = xmlTreeBuilder.parseFragment(xml, "", new ParseErrorList());
        Element root = (Element) nodes.get(0);
        assertEquals(1, root.childNodes().size());
        assertNull(root.childNodes().get(0).previousSibling());
    }

    @Test
    public void testParseFragmentWithDoctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xml = "<!DOCTYPE root [<!ATTLIST element attr CDATA #REQUIRED>]><root><element attr='value'>Hello</element></root>";
        List<Node> nodes = xmlTreeBuilder.parseFragment(xml, "", new ParseErrorList());
        Document doc = nodes.get(0).asDocument();
        DocumentType doctype = doc.doctype();
        assertEquals("root", doctype.getName());
        assertEquals("", doctype.getPublicIdentifier());
        assertEquals("", doctype.getSystemIdentifier());
        Element root = (Element) nodes.get(1);
        Element element = (Element) root.childNodes().get(0);
        assertEquals("value", element.attr("attr"));
    }
}