package org.jsoup.parser;

import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.Parser;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class XmlTreeBuilderTest {

    @Test
    public void testParseEmptyXml() {
        Document document = new XmlTreeBuilder().parse("", "http://example.com");
        assertNotNull(document);
        assertNotNull(document.childNodes());
        assertEquals(0, document.childNodes().size());
    }

    @Test
    public void testParseSimpleXml() {
        Document document = new XmlTreeBuilder()
                .parse("<root><child>Hello World</child></root>", "http://example.com");
        assertNotNull(document);
        assertEquals("root", document.tagName());
        Element rootElement = document.child(0);
        assertEquals("child", rootElement.tagName());
        List<Node> childNodes = rootElement.childNodes();
        assertEquals(1, childNodes.size());
        TextNode textNode = (TextNode) childNodes.get(0);
        assertEquals("Hello World", textNode.text());
    }

    @Test
    public void testParseXmlWithDoctype() {
        Document document = new XmlTreeBuilder()
                .parse("<!DOCTYPE html SYSTEM \"about:legacy-compat\">\n<html><head></head><body></body></html>", "http://example.com");
        assertNotNull(document);
        DocumentType doctype = document.doctype();
        assertEquals("html", doctype.getName());
        assertEquals("about:legacy-compat", doctype.getSystemIdentifier());
        assertEquals(null, document.childNodes().get(0));
    }

    @Test
    public void testParseXmlWithComments() {
        Document document = new XmlTreeBuilder()
                .parse("<root><!-- comment1 --><child>Hello World</child><!-- comment2 --></root>", "http://example.com");
        assertNotNull(document);
        List<Node> commentNodes = document.getElementsByTag("comment");
        assertEquals(2, commentNodes.size());
        Element childElement = document.child(0);
        assertEquals("child", childElement.tagName());
        List<Node> childNodes = childElement.childNodes();
        assertEquals(0, childNodes.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidXml() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parse("<html<tag", "http://example.com");
    }

    @Test
    public void testParseXmlFragment() {
        String xmlFragment = "<root><child1><child2>Hello World</child2></child1></root>";
        List<Node> nodes = new XmlTreeBuilder().parseFragment(xmlFragment, "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase).childNodes();
        assertEquals(3, nodes.size());
        Element rootElement = (Element) nodes.get(0);
        assertEquals("root", rootElement.tagName());
        List<Node> childNodes = rootElement.childNodes();
        assertEquals(1, childNodes.size());
        Element child1Element = (Element) childNodes.get(0);
        assertEquals("child1", child1Element.tagName());
        List<Node> grandChildNodes = child1Element.childNodes();
        assertEquals(1, grandChildNodes.size());
        TextNode textNode = (TextNode) grandChildNodes.get(0);
        assertEquals("Hello World", textNode.text());
    }
}