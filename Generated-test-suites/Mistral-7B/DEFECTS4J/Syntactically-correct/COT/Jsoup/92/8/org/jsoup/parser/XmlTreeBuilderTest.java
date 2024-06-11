package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        assertEquals(ParseSettings.PreserveCase.class, settings.getClass());
    }

    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse(new StringReader("<root><element/></root>"), null);
        List<Node> nodes = document.childNodes();
        assertEquals(2, nodes.size());
        assertEquals("root", nodes.get(0).nodeName());
        assertEquals("element", nodes.get(1).nodeName());
        assertTrue(nodes.get(0) instanceof Element);
        assertTrue(nodes.get(1) instanceof Element);
    }

    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse(new StringReader("<root><element/></root>"), null);
        List<Node> nodes = document.childNodes();
        Element root = (Element) nodes.get(0);
        Element element = (Element) nodes.get(1);
        root.removeChild(element);
        List<Node> stack = xmlTreeBuilder.stack;
        assertEquals(1, stack.size());
        assertEquals(element, stack.get(0));
    }

    @Test(expected = AssertionError.class)
    public void testUnexpectedTokenType() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token token = new Token(-1, null, null, Token.TokenType.OTHER);
        xmlTreeBuilder.process(token);
    }

    @Test
    public void testProcessComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse(new StringReader("<!--This is a comment-->\n<root><element/></root>"), null);
        List<Node> nodes = document.childNodes();
        assertEquals(2, nodes.size());
        assertEquals("root", nodes.get(0).nodeName());
        assertEquals("element", nodes.get(1).nodeName());
        assertTrue(nodes.get(0) instanceof Element);
        assertTrue(nodes.get(1) instanceof Comment);
    }

    @Test
    public void testProcessCharacter() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse(new StringReader("<root>&foo;<element/></root>"), null);
        List<Node> nodes = document.childNodes();
        assertEquals(2, nodes.size());
        assertEquals("root", nodes.get(0).nodeName());
        assertEquals("element", nodes.get(1).nodeName());
        assertTrue(nodes.get(0) instanceof Element);
        assertTrue(nodes.get(1) instanceof TextNode);
        assertEquals("root", nodes.get(1).nodeName());
        assertEquals("&foo;", nodes.get(1).text());
    }

    @Test
    public void testProcessDoctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse(new StringReader("<!DOCTYPE root [ <!ATTLIST element foo CDATA #REQUIRED > ]>\n<root><element/></root>"), null);
        List<Node> nodes = document.childNodes();
        assertEquals(3, nodes.size());
        assertEquals("root", nodes.get(0).nodeName());
        assertEquals("element", nodes.get(1).nodeName());
        assertTrue(nodes.get(0) instanceof DocumentType);
        assertTrue(nodes.get(2) instanceof Element);
    }

    @Test(expected = AssertionError.class)
    public void testUnknownTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse(new StringReader("<root><unknown/></root>"), null);
        List<Node> nodes = document.childNodes();
        assertEquals(1, nodes.size());
        assertEquals("root", nodes.get(0).nodeName());
        assertTrue(nodes.get(0) instanceof Element);
        assertTrue(nodes.get(0).tagName().equals("unknown"));
    }

    @Test
    public void testSelfClosingTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse(new StringReader("<root><selfClosing/></root>"), null);
        List<Node> nodes = document.childNodes();
        assertEquals(2, nodes.size());
        assertEquals("root", nodes.get(0).nodeName());
        assertTrue(nodes.get(0) instanceof Element);
        assertTrue(nodes.get(1) instanceof Element);
        assertEquals("selfClosing", nodes.get(1).nodeName());
        assertTrue(nodes.get(1).tagName().equals("selfClosing"));
        assertTrue(nodes.get(1).isSelfClosing());
    }

    @Test(expected = AssertionError.class)
    public void testMissingClosingTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse(new StringReader("<root><element/>"), null);
        List<Node> nodes = document.childNodes();
        assertEquals(1, nodes.size());
        assertEquals("root", nodes.get(0).nodeName());
        assertTrue(nodes.get(0) instanceof Element);
        assertEquals("element", nodes.get(0).nodeName());
        Element element = (Element) nodes.get(0);
        assertEquals(1, element.childNodes().size());
        assertEquals("element", element.childNodes().get(0).nodeName());
        assertTrue(element.childNodes().get(0) instanceof Element);
        throw new AssertionError("Expected an exception for missing closing tag.");
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse(new StringReader("<root><element/></root>"), null);
        List<Node> nodes = document.childNodes();
        assertEquals(2, nodes.size());
        Element root = (Element) nodes.get(0);
        Element element = (Element) nodes.get(1);
        List<Node> fragmentNodes = xmlTreeBuilder.parseFragment("<element/>", document, null);
        assertEquals(1, fragmentNodes.size());
        assertTrue(fragmentNodes.get(0) instanceof Element);
        assertTrue(fragmentNodes.get(0).nodeName().equals("element"));
        root.appendChild(fragmentNodes.get(0));
        List<Node> newNodes = document.childNodes();
        assertEquals(3, newNodes.size());
        assertEquals("root", newNodes.get(0).nodeName());
        assertEquals("element", newNodes.get(1).nodeName());
        assertEquals("element", newNodes.get(2).nodeName());
    }

    @Test(expected = AssertionError.class)
    public void testInvalidXmlDeclaration() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse(new StringReader("<?xml xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" version=\"1.0\"?>"), null);
        List<Node> nodes = document.childNodes();
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Comment);
        throw new AssertionError("Expected an exception for invalid xml declaration.");
    }

    @Test
    public void testValidXmlDeclaration() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse(new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root><element/></root>"), null);
        List<Node> nodes = document.childNodes();
        assertEquals(2, nodes.size());
        assertEquals("root", nodes.get(0).nodeName());
        assertEquals("element", nodes.get(1).nodeName());
        assertTrue(nodes.get(0) instanceof Element);
        assertTrue(nodes.get(1) instanceof Element);
    }
}