package org.jsoup.parser;

import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.ParseErrorList;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class XmlTreeBuilderTest {

    @Test
    public void testParseXmlWithRootElement() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xmlString = "<root><child>Hello World</child></root>";
        Document doc = xmlTreeBuilder.parse(xmlString, null);

        Element root = doc.rootElement();
        Element child = root.child(0);
        TextNode textNode = (TextNode) child.child(0);

        assertEquals("Hello World", textNode.text());
        assertEquals("root", root.tagName());
        assertNotNull(root);
    }

    @Test
    public void testParseXmlWithEmptyElement() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xmlString = "<element/>";
        Document doc = xmlTreeBuilder.parse(xmlString, null);
        Element element = doc.rootElement();
        assertNotNull(element);
        assertEquals("element", element.tagName());
    }

    @Test
    public void testParseXmlWithSelfClosingTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xmlString = "<img src=\"image.jpg\" alt=\"Image\" />";
        Document doc = xmlTreeBuilder.parse(xmlString, null);
        Element imgElement = doc.getElementsByTag("img").first();
        assertEquals("img", imgElement.tagName());
        assertNotNull(imgElement);
        assertEquals(0, imgElement.childNodes().size());
    }

    @Test
    public void testParseXmlWithComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xmlString = "<!-- This is a comment -->";
        Document doc = xmlTreeBuilder.parse(xmlString, null);
        List<Node> commentNodes = doc.getElementsByTag("comment");
        assertEquals(1, commentNodes.size());
        Element commentElement = (Element) commentNodes.get(0);
        assertEquals("--!", commentElement.tagName());
        assertEquals("This is a comment", commentElement.text());
    }

    @Test
    public void testParseXmlWithDoctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xmlString = "<!DOCTYPE html SYSTEM \"about:legacy-compat\">";
        Document doc = xmlTreeBuilder.parse(xmlString, null);
        DocumentType doctypeNode = doc.doctype();
        assertEquals("html", doctypeNode.nodeName());
        assertEquals("about:legacy-compat", doctypeNode.systemId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseXmlWithInvalidInput() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xmlString = "Invalid Input";
        xmlTreeBuilder.parse(xmlString, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseXmlWithNullBaseUri() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parse(null, null);
    }

    @Test
    public void testParseXmlFragment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xmlFragment = "<root><child>Hello</child><child>World</child></root>";
        List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, null, null, ParseSettings.preserveCase);
        Element rootElement = (Element) nodes.get(0);
        assertEquals("root", rootElement.tagName());
        assertEquals(2, rootElement.childNodes().size());
    }
}