package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {

    @Test
    public void testParse() {
        String xmlString = "<root><child>Hello</child></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse(new StringReader(xmlString), null);
        Element root = document.getRootElement();
        assertNotNull(root);
        List<Node> childNodes = root.children();
        assertTrue(childNodes.size() > 0);
        Element child = (Element) childNodes.get(0);
        assertNotNull(child);
        assertEquals("Hello", child.text());
    }

    @Test
    public void testCommentProcessing() {
        String xmlString = "<root><!-- This is a comment --></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse(new StringReader(xmlString), null);
        List<Node> nodes = document.childNodes();
        Element root = document.getRootElement();
        assertTrue(nodes.size() > 1);
        Node commentNode = nodes.get(1);
        assertTrue(commentNode instanceof Comment);
        assertEquals("This is a comment", commentNode.text());
    }

    @Test
    public void testSelfClosingTags() {
        String xmlString = "<root><img src=\"image.png\" /></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse(new StringReader(xmlString), null);
        Element root = document.getRootElement();
        List<Node> childNodes = root.children();
        assertTrue(childNodes.size() > 0);
        Node imgNode = childNodes.get(0);
        assertTrue(imgNode instanceof Element);
        Element imgElement = (Element) imgNode;
        assertTrue(imgElement.isSelfClosing());
        assertTrue(imgElement.tagName().equals("img"));
    }

    @Test
    public void testIgnoringUnknownTags() {
        String xmlString = "<root><unknownTag /> </root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse(new StringReader(xmlString), null);
        Element root = document.getRootElement();
        List<Node> childNodes = root.children();
        assertTrue(childNodes.size() > 0);
        Node unknownNode = childNodes.get(0);
        assertTrue(unknownNode instanceof Element);
        Element unknownElement = (Element) unknownNode;
        assertFalse(unknownElement.isSelfClosing());
        assertFalse(unknownElement.tagName().isKnownTag());
    }

    @Test
    public void testDoctypeProcessing() {
        String xmlString = "<!DOCTYPE root [ <!ATTLIST root attr CDATA #REQUIRED > ]><root></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse(new StringReader(xmlString), null);
        Element root = document.getRootElement();
        List<Node> childNodes = root.children();
        assertTrue(childNodes.size() > 0);
        int index = 0;
        while (index < childNodes.size()) {
            Node node = childNodes.get(index);
            if (node instanceof DocumentType) {
                DocumentType docType = (DocumentType) node;
                assertTrue(docType.toString().equals("<!DOCTYPE root [ <!ATTLIST root attr CDATA #REQUIRED > ]>"));
                break;
            }
            index++;
        }
    }

    @Test
    public void testParseFragment() {
        String xmlFragment = "<child1><child2><child3>Hello</child3></child2></child1>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, null, null, ParseSettings.xmlParserSettings());
        Node rootNode = nodes.get(0);
        assertTrue(rootNode instanceof Element);
        Element root = (Element) rootNode;
        assertEquals("child1", root.tagName());
        List<Node> childNodes = root.children();
        assertTrue(childNodes.size() > 0);
        Node child1Node = childNodes.get(0);
        assertTrue(child1Node instanceof Element);
        Element child1 = (Element) child1Node;
        assertEquals("child2", child1.tagName());
        List<Node> grandChildNodes = child1.children();
        assertTrue(grandChildNodes.size() > 0);
        Node child3Node = grandChildNodes.get(0);
        assertTrue(child3Node instanceof TextNode);
        TextNode child3 = (TextNode) child3Node;
        assertEquals("Hello", child3.text());
    }
}