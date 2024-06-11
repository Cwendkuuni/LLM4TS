package org.jsoup.parser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.helper.Validate;
import org.junit.Test;

import java.util.List;

public class XmlTreeBuilderTest {

    @Test
    public void testParseXmlWithEmptyInput() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse("", "");
        assertTrue(document.childNodes().isEmpty());
    }

    @Test
    public void testParseRootElementWithAttributes() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse("<root attr1=\"value1\" attr2=\"value2\"/>", "");
        Element rootElement = document.getRootElement();
        assertEquals("root", rootElement.tagName());
        List<Attribute> attributes = rootElement.attributes();
        assertEquals(2, attributes.size());
        assertEquals("value1", attributes.get(0).getValue());
        assertEquals("value2", attributes.get(1).getValue());
    }

    @Test
    public void testParseEmptyElement() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse("<empty/>", "");
        Element emptyElement = document.getRootElement();
        assertTrue(emptyElement.children().isEmpty());
        assertEquals("empty", emptyElement.tagName());
    }

    @Test
    public void testParseElementWithChildren() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse("<root><child1>Hello</child1><child2><grandChild>World</grandChild></child2></root>", "");
        Element rootElement = document.getRootElement();
        List<Element> children = rootElement.children();
        assertEquals(2, children.size());

        Element child1 = children.get(0);
        assertEquals("child1", child1.tagName());
        List<TextNode> textNodes = child1.childNodes();
        assertEquals(1, textNodes.size());
        TextNode textNode = textNodes.get(0);
        assertEquals("Hello", textNode.text());

        Element child2 = children.get(1);
        assertEquals("child2", child2.tagName());
        List<Element> grandChildren = child2.children();
        assertEquals(1, grandChildren.size());

        Element grandChild = grandChildren.get(0);
        assertEquals("grandChild", grandChild.tagName());
        List<TextNode> grandChildTextNodes = grandChild.childNodes();
        assertEquals(1, grandChildTextNodes.size());
        TextNode grandChildTextNode = grandChildTextNodes.get(0);
        assertEquals("World", grandChildTextNode.text());
    }

    @Test
    public void testParseXmlWithDoctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse("<!DOCTYPE html SYSTEM \"about:legacy-compat\"><html></html>", "");
        DocumentType doctypeNode = document.doctype();
        assertEquals("html", doctypeNode.name());
        assertEquals("about:legacy-compat", doctypeNode.systemId());
    }

    @Test
    public void testParseXmlWithComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse("<!-- This is a comment --><root></root>", "");
        Comment comment = document.getElementsByTag("comment").get(0);
        assertEquals("This is a comment", comment.text());
    }

    @Test
    public void testParseXmlWithSelfClosingTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse("<img src=\"image.png\" />", "");
        Element imgElement = document.getElementsByTag("img").get(0);
        assertTrue(imgElement.hasAttr("src"));
        assertEquals("image.png", imgElement.attr("src"));
        assertTrue(imgElement.children().isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidXmlInput() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parse("<html<head></html>", "");
        Validate.fail("Invalid XML input");
    }

    @Test(expected = NullPointerException.class)
    public void testNullInput() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parse(null, "");
        Validate.fail("Input should not be null");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyBaseUri() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parse("<html></html>", "");
        Validate.fail("Base URI should not be empty");
    }
}