package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Token;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;

/**
 * XmlTreeBuilderTest class file for JUnit 4 test cases.
 */
public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        assertEquals(ParseSettings.preserveCase, settings);
    }

    @Test
    public void testParseMethodWithReader() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xmlString = "<root><child1>Hello</child1><child2><grandChild>World</grandChild></child2></root>";
        Reader reader = new StringReader(xmlString);
        String baseUri = "https://example.com";
        Document document = xmlTreeBuilder.parse(reader, baseUri);
        Element root = document.rootElement();
        assertNotNull(root);
        assertEquals("root", root.tagName());
        List<Node> nodes = root.children();
        assertEquals(2, nodes.size());
        Element child1 = (Element) nodes.get(0);
        assertEquals("child1", child1.tagName());
        Text child1Text = (Text) child1.child(0);
        assertEquals("Hello", child1Text.text());
        Element child2 = (Element) nodes.get(1);
        assertEquals("child2", child2.tagName());
        List<Node> child2Nodes = child2.children();
        assertEquals(1, child2Nodes.size());
        Element grandChild = (Element) child2Nodes.get(0);
        assertEquals("grandChild", grandChild.tagName());
        Text grandChildText = (Text) grandChild.child(0);
        assertEquals("World", grandChildText.text());
    }

    @Test
    public void testParseMethodWithString() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xmlString = "<root><child1>Hello</child1><child2><grandChild>World</grandChild></child2></root>";
        String baseUri = "https://example.com";
        Document document = xmlTreeBuilder.parse(xmlString, baseUri);
        Element root = document.rootElement();
        assertNotNull(root);
        assertEquals("root", root.tagName());
        List<Node> nodes = root.children();
        assertEquals(2, nodes.size());
        Element child1 = (Element) nodes.get(0);
        assertEquals("child1", child1.tagName());
        Text child1Text = (Text) child1.child(0);
        assertEquals("Hello", child1Text.text());
        Element child2 = (Element) nodes.get(1);
        assertEquals("child2", child2.tagName());
        List<Node> child2Nodes = child2.children();
        assertEquals(1, child2Nodes.size());
        Element grandChild = (Element) child2Nodes.get(0);
        assertEquals("grandChild", grandChild.tagName());
        Text grandChildText = (Text) grandChild.child(0);
        assertEquals("World", grandChildText.text());
    }

    @Test
    public void testInitialiseParseMethod() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xmlString = "<root><child1>Hello</child1><child2><grandChild>World</grandChild></child2></root>";
        Reader reader = new StringReader(xmlString);
        String baseUri = "https://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = new ParseSettings();
        xmlTreeBuilder.initialiseParse(reader, baseUri, errors, settings);
        assertEquals(baseUri, xmlTreeBuilder.baseUri);
        assertEquals(errors, xmlTreeBuilder.errors);
        assertEquals(settings, xmlTreeBuilder.settings);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProcessMethodWithInvalidToken() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token token = new Token(1, "invalid_token_type");
        xmlTreeBuilder.process(token);
    }

    @Test
    public void testInsertNodeMethod() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element root = new Element("root");
        Node node = new TextNode("Hello World");
        xmlTreeBuilder.insertNode(root);
        xmlTreeBuilder.insertNode(node);
        root.appendChild(node);
        assertEquals(1, root.childNodes().size());
    }

    @Test
    public void testInsertMethodWithStartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token startTag = new Token(1, "tagName", null, null);
        Element element = xmlTreeBuilder.insert(startTag);
        assertEquals("tagName", element.tagName());
    }

    @Test
    public void testInsertMethodWithCommentToken() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token commentToken = new Token(1, null, "<!-- This is a comment -->");
        Comment comment = xmlTreeBuilder.insert(commentToken);
        assertEquals("-->", comment.isClosedTag());
        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testParseFragmentMethod() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xmlFragment = "<root><child1>Hello</child1><child2><grandChild>World</grandChild></child2></root>";
        String baseUri = "https://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, baseUri, errors, settings);
        assertEquals(3, nodes.size());
        Element root = (Element) nodes.get(0);
        Element child1 = (Element) nodes.get(1);
        Element child2 = (Element) nodes.get(2);
        assertEquals("root", root.tagName());
        assertEquals("child1", child1.tagName());
        assertEquals("child2", child2.tagName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPopStackToCloseMethodWithInvalidEndTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token endTag = new Token(1, "invalidTagName");
        xmlTreeBuilder.popStackToClose(endTag);
    }
}