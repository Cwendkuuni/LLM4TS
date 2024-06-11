package org.jsoup.parser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.jsoup.nodes.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Attribute;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.XmlTreeBuilder;

import java.util.List;
import java.util.ArrayList;

public class XmlTreeBuilderTest {

    @Test
    public void testXmlTreeBuilderInitialisation() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        assertNotNull(xmlTreeBuilder);
    }

    @Test
    public void testParseFragmentEmptyString() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        List<Node> nodes = xmlTreeBuilder.parseFragment("", "", new ParseErrorList());
        assertEquals(0, nodes.size());
    }

    @Test
    public void testParseFragmentSingleComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        List<Node> nodes = xmlTreeBuilder.parseFragment("<!-- This is a comment -->", "", new ParseErrorList());
        assertEquals(1, nodes.size());
        Node node = nodes.get(0);
        assertTrue(node instanceof Comment);
        assertEquals("This is a comment", node.text());
    }

    @Test
    public void testParseFragmentSingleTextNode() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        List<Node> nodes = xmlTreeBuilder.parseFragment("<text>Hello World</text>", "", new ParseErrorList());
        assertEquals(1, nodes.size());
        Node node = nodes.get(0);
        assertTrue(node instanceof TextNode);
        assertEquals("Hello World", node.text());
    }

    @Test
    public void testParseFragmentSingleStartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        List<Node> nodes = xmlTreeBuilder.parseFragment("<tag>Hello World</tag>", "", new ParseErrorList());
        assertEquals(1, nodes.size());
        Node node = nodes.get(0);
        assertTrue(node instanceof Element);
        Element element = (Element) node;
        assertEquals("tag", element.tagName());
        assertEquals(0, element.childNodes().size());
    }

    @Test
    public void testParseFragmentSingleStartTagWithAttributes() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        List<Node> nodes = xmlTreeBuilder.parseFragment("<tag attr1=\"value1\" attr2=\"value2\"/>", "", new ParseErrorList());
        assertEquals(1, nodes.size());
        Node node = nodes.get(0);
        assertTrue(node instanceof Element);
        Element element = (Element) node;
        assertEquals("tag", element.tagName());
        List<Attribute> attributes = element.attributes();
        assertEquals(2, attributes.size());
        for (int i = 0; i < attributes.size(); i++) {
            Attribute attribute = attributes.get(i);
            String attributeName = attribute.getKey();
            String attributeValue = attribute.getValue();
            if (i == 0) {
                assertEquals("attr1", attributeName);
                assertEquals("value1", attributeValue);
            } else {
                assertEquals("attr2", attributeName);
                assertEquals("value2", attributeValue);
            }
        }
    }

    @Test
    public void testParseFragmentSingleStartTagWithChildren() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<tag><child>Hello</child><child>World</child></tag>";
        List<Node> nodes = xmlTreeBuilder.parseFragment(input, "", new ParseErrorList());
        Element rootElement = (Element) nodes.get(0);
        List<Node> children = rootElement.childNodes();
        assertEquals(2, children.size());
        for (int i = 0; i < children.size(); i++) {
            Node childNode = children.get(i);
            if (i == 0) {
                assertTrue(childNode instanceof Element);
                Element childElement = (Element) childNode;
                assertEquals("child", childElement.tagName());
                assertEquals("Hello", childElement.childNodes().get(0).text());
            } else {
                assertTrue(childNode instanceof Element);
                Element childElement = (Element) childNode;
                assertEquals("child", childElement.tagName());
                assertEquals("World", childElement.childNodes().get(0).text());
            }
        }
    }

    @Test
    public void testParseFragmentMultipleRootElements() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<tag1/><tag2/>";
        List<Node> nodes = xmlTreeBuilder.parseFragment(input, "", new ParseErrorList());
        assertEquals(2, nodes.size());
        for (int i = 0; i < nodes.size(); i++) {
            Node node = nodes.get(i);
            assertTrue(node instanceof Element);
            if (i == 0) {
                assertEquals("tag1", node.tagName());
                assertNull(node.childNodes());
            } else {
                assertEquals("tag2", node.tagName());
                assertNull(node.childNodes());
            }
        }
    }

    @Test
    public void testParseFragmentSelfClosingTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<tag xmlns:x='http://schemas.microsoft.com/winfx/2006/xaml' xmlns:local='clr-namespace:MyNamespace'></tag>";
        List<Node> nodes = xmlTreeBuilder.parseFragment(input, "", new ParseErrorList());
        Element rootElement = (Element) nodes.get(0);
        assertTrue(rootElement.tagName().equals("tag"));
        assertTrue(rootElement.isSelfClosing());
        assertNull(rootElement.childNodes());
    }

    @Test
    public void testParseFragmentDoctypeDeclaration() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<!DOCTYPE mydoc [ <!ATTLIST mytag attr CDATA #REQUIRED> ]>";
        List<Node> nodes = xmlTreeBuilder.parseFragment(input, "", new ParseErrorList());
        Document document = nodes.get(0);
        DocumentType documentType = document.doctype();
        assertEquals("mydoc", documentType.name());
        assertEquals("", documentType.publicId());
        assertEquals("", documentType.systemId());
        assertEquals("", documentType.internalSubset());
        List<Attribute> attributes = documentType.attributes();
        assertEquals(1, attributes.size());
        Attribute attribute = attributes.get(0);
        assertEquals("ATTLIST", attribute.getKey());
        assertEquals("mytag", attribute.getValue());
        List<Node> nodes2 = documentType.getNodeChildren();
        assertEquals(1, nodes2.size());
        Element node = (Element) nodes2.get(0);
        assertEquals("mytag", node.tagName());
        List<Attribute> attributes2 = node.attributes();
        assertEquals(1, attributes2.size());
        Attribute attribute2 = attributes2.get(0);
        assertEquals("attr", attribute2.getKey());
        assertEquals("CDATA", attribute2.getValue());
    }

    @Test
    public void testParseFragmentParseError() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        List<Node> nodes = xmlTreeBuilder.parseFragment("<tag < error />", "", new ParseErrorList());
        assertEquals(1, nodes.size());
        Element element = (Element) nodes.get(0);
        assertEquals("tag", element.tagName());
        List<Node> children = element.childNodes();
        assertEquals(1, children.size());
        Node node = children.get(0);
        assertTrue(node instanceof ParseError);
        ParseError parseError = (ParseError) node;
        assertEquals(13, parseError.line());
        assertEquals(1, parseError.column());
        assertEquals("Error: < expected >", parseError.getMessage());
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element element1 = new Element("tag1", "");
        Element element2 = new Element("tag2", "");
        Stack<Element> stack = new Stack<>();
        stack.push(element1);
        stack.push(element2);
        xmlTreeBuilder.popStackToClose(new Token.EndTag("tag1"));
        assertEquals(1, stack.size());
        assertSame(element2, stack.peek());
        xmlTreeBuilder.popStackToClose(new Token.EndTag("tag2"));
        assertEquals(0, stack.size());
    }
}