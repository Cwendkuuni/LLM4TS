package org.jsoup.parser;

import static org.junit.Assert.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.junit.Test;

import java.util.List;

public class XmlTreeBuilderTest {

    @Test
    public void testParse() {
        String xml = "<root><element attr=\"value\"><subElement/><anotherElement/></element></root>";
        Document doc = XmlTreeBuilder.parse(xml, "http://test.com");
        Element root = doc.elementSibling(0);
        assertNotNull(root);
        assertEquals("root", root.nodeName());
        List<Node> children = root.childNodes();
        assertEquals(1, children.size());
        Element element = (Element) children.get(0);
        assertEquals("element", element.nodeName());
        List<Node> elementChildren = element.childNodes();
        assertEquals(2, elementChildren.size());
        assertNull(elementChildren.get(0).nodeName()); // subElement is an empty element
        Element subElement = (Element) elementChildren.get(1);
        assertNull(subElement.attributes().getNamedItem("attr")); // attributes should not be parsed for empty elements
        assertEquals("anotherElement", elementChildren.get(1).nodeName());
    }

    @Test
    public void testParseWithAttributes() {
        String xml = "<root attr=\"value1\" xmlns:x=\"http://test.com/namespace\"><element attr=\"value2\" x:subElement><anotherElement attr=\"value3\"/></element></root>";
        Document doc = XmlTreeBuilder.parse(xml, "http://test.com");
        Element root = doc.elementSibling(0);
        assertEquals("root", root.nodeName());
        Element element = (Element) root.childNodes().get(0);
        assertEquals("element", element.nodeName());
        assertEquals("value1", root.attributeValue("attr"));
        assertEquals("value2", element.attributeValue("attr"));
        Element xnamespace = element.elementSibling("x");
        assertNotNull(xnamespace);
        Element subElement = (Element) element.childNodes().get(0);
        assertEquals("subElement", subElement.nodeName());
        assertEquals("value3", element.childNodes().get(1).attributeValue("attr"));
    }

    @Test
    public void testParseWithDoctype() {
        String xml = "<!DOCTYPE root [ <!ATTLIST root attr CDATA #REQUIRED > ]><root><element attr=\"value\"></element></root>";
        Document doc = XmlTreeBuilder.parse(xml, "http://test.com");
        DocumentType doctype = doc.doctype();
        assertNotNull(doctype);
        assertEquals("root", doctype.nodeName());
        Element root = doc.elementSibling(0);
        assertNotNull(root);
        assertEquals("root", root.nodeName());
        Element element = (Element) root.childNodes().get(0);
        assertEquals("element", element.nodeName());
        assertEquals("value", element.attributeValue("attr"));
    }

    @Test
    public void testParseWithComments() {
        String xml = "<root><!-- This is a comment --><element><!-- This is also a comment --> <subElement/><!-- And another comment --> </element></root>";
        Document doc = XmlTreeBuilder.parse(xml, "http://test.com");
        Element root = doc.elementSibling(0);
        List<Node> children = root.childNodes();
        Element element = (Element) children.get(0);
        List<Node> elementChildren = element.childNodes();
        assertEquals(1, elementChildren.size());
        Element subElement = (Element) elementChildren.get(0);
        assertEquals(3, root.childNodes().size());
        assertEquals(1, children.get(0).childNodes().size());
    }

    @Test
    public void testParseFragment() {
        String xmlFragment = "<element attr=\"value\"><subElement/><anotherElement/></element>";
        Document doc = XmlTreeBuilder.parseFragment(xmlFragment, "http://test.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        Element root = doc.elementSibling(0);
        assertEquals("element", root.nodeName());
        List<Node> children = root.childNodes();
        assertEquals(2, children.size());
        Element subElement = (Element) children.get(0);
        assertEquals("subElement", subElement.nodeName());
        Element anotherElement = (Element) children.get(1);
        assertEquals("anotherElement", anotherElement.nodeName());
    }
}