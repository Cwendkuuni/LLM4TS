package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;
import org.jsoup.parser.XmlTreeBuilder;

import java.util.List;

public class XmlTreeBuilderTest {

    @Test
    public void testParseDocument() {
        String xml = "<root><element attr=\"value\">Content</element></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse(xml, "http://example.com");

        Element root = document.getRootElement();
        assertEquals("root", root.tagName());
        List<Node> children = root.children();
        assertEquals(1, children.size());
        Element element = (Element) children.get(0);
        assertEquals("element", element.tagName());
        assertEquals("value", element.attribute("attr"));
        assertEquals("Content", element.text());
    }

    @Test
    public void testParseDocumentWithSelfClosingTag() {
        String xml = "<root><element /></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse(xml, "http://example.com");

        Element root = document.getRootElement();
        assertEquals("root", root.tagName());
        List<Node> children = root.children();
        assertEquals(1, children.size());
        Element element = (Element) children.get(0);
        assertEquals("element", element.tagName());
        assertNull(element.text());
    }

    @Test
    public void testParseDocumentWithEmptyElement() {
        String xml = "<root></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse(xml, "http://example.com");

        Element root = document.getRootElement();
        assertEquals("root", root.tagName());
        List<Node> children = root.children();
        assertEquals(0, children.size());
    }

    @Test
    public void testParseDocumentWithWhitespaces() {
        String xml = "<root><element attr=\"value\"> Content </element></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse(xml, "http://example.com");

        Element root = document.getRootElement();
        assertEquals("root", root.tagName());
        List<Node> children = root.children();
        assertEquals(1, children.size());
        Element element = (Element) children.get(0);
        assertEquals("element", element.tagName());
        assertEquals("value", element.attribute("attr"));
        assertEquals(" Content ", element.text().trim());
    }

    @Test
    public void testParseDocumentWithComments() {
        String xml = "<root><!-- comment --><element attr=\"value\">Content</element></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse(xml, "http://example.com");

        Element root = document.getRootElement();
        assertEquals("root", root.tagName());
        List<Node> children = root.children();
        assertEquals(1, children.size());
        Element element = (Element) children.get(0);
        assertEquals("element", element.tagName());
        assertEquals("value", element.attribute("attr"));
        assertEquals("Content", element.text());

        Comment comment = (Comment) root.child(0);
        assertEquals("comment", comment.tagName());
        assertEquals("", comment.text());
    }

    @Test
    public void testParseDocumentWithDoctype() {
        String xml = "<!DOCTYPE root [ <!ATTLIST element attr CDATA #REQUIRED > ]><root><element attr=\"value\">Content</element></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse(xml, "http://example.com");

        DocumentType doctype = document.doctype();
        assertEquals("root", doctype.nodeName());
        assertEquals("[", doctype.content().toString());
        Element root = document.getRootElement();
        assertEquals("root", root.tagName());
        List<Node> children = root.children();
        assertEquals(1, children.size());
        Element element = (Element) children.get(0);
        assertEquals("element", element.tagName());
        assertEquals("value", element.attribute("attr"));
        assertEquals("Content", element.text());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseDocumentWithInvalidInput() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parse("", "http://example.com");
    }

    @Test
    public void testParseFragment() {
        String xmlFragment = "<element attr=\"value\">Content</element>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);

        assertEquals(1, nodes.size());
        Element element = (Element) nodes.get(0);
        assertEquals("element", element.tagName());
        assertEquals("value", element.attribute("attr"));
        assertEquals("Content", element.text());
    }
}