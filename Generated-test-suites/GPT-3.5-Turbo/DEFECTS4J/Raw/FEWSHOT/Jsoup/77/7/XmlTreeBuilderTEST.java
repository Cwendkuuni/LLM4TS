package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setup() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testDefaultSettings() {
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        assertTrue(settings.preserveCase());
    }

    @Test
    public void testParseWithReaderAndBaseUri() {
        Reader reader = new StringReader("<root>Hello World</root>");
        String baseUri = "http://example.com";
        Document document = xmlTreeBuilder.parse(reader, baseUri);

        assertEquals("Hello World", document.text());
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testParseWithStringAndBaseUri() {
        String input = "<root>Hello World</root>";
        String baseUri = "http://example.com";
        Document document = xmlTreeBuilder.parse(input, baseUri);

        assertEquals("Hello World", document.text());
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testInitialiseParse() {
        Reader reader = new StringReader("<root>Hello World</root>");
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;

        xmlTreeBuilder.initialiseParse(reader, baseUri, errors, settings);

        assertEquals(baseUri, xmlTreeBuilder.baseUri);
        assertEquals(errors, xmlTreeBuilder.errors);
        assertEquals(settings, xmlTreeBuilder.settings);
    }

    @Test
    public void testProcessStartTag() {
        Token.StartTag startTag = new Token.StartTag("div", new Attributes());
        xmlTreeBuilder.insert(startTag);

        Element element = xmlTreeBuilder.currentElement();
        assertEquals("div", element.nodeName());
    }

    @Test
    public void testProcessEndTag() {
        Element element = new Element(Tag.valueOf("div"), "http://example.com");
        xmlTreeBuilder.stack.add(element);

        Token.EndTag endTag = new Token.EndTag("div");
        xmlTreeBuilder.process(endTag);

        assertTrue(xmlTreeBuilder.stack.isEmpty());
    }

    @Test
    public void testProcessComment() {
        Token.Comment comment = new Token.Comment("This is a comment");
        xmlTreeBuilder.insert(comment);

        List<Node> childNodes = xmlTreeBuilder.currentElement().childNodes();
        assertEquals(1, childNodes.size());
        assertTrue(childNodes.get(0) instanceof Comment);
        assertEquals("This is a comment", ((Comment) childNodes.get(0)).getData());
    }

    @Test
    public void testProcessCharacter() {
        Token.Character character = new Token.Character("Hello World");
        xmlTreeBuilder.insert(character);

        List<Node> childNodes = xmlTreeBuilder.currentElement().childNodes();
        assertEquals(1, childNodes.size());
        assertTrue(childNodes.get(0) instanceof TextNode);
        assertEquals("Hello World", ((TextNode) childNodes.get(0)).getWholeText());
    }

    @Test
    public void testProcessDoctype() {
        Token.Doctype doctype = new Token.Doctype("html", "", "");
        xmlTreeBuilder.insert(doctype);

        List<Node> childNodes = xmlTreeBuilder.currentElement().childNodes();
        assertEquals(1, childNodes.size());
        assertTrue(childNodes.get(0) instanceof DocumentType);
        assertEquals("html", ((DocumentType) childNodes.get(0)).getName());
    }

    @Test
    public void testInsertNode() {
        Element element = new Element(Tag.valueOf("div"), "http://example.com");
        xmlTreeBuilder.stack.add(element);

        Element childElement = new Element(Tag.valueOf("p"), "http://example.com");
        xmlTreeBuilder.insertNode(childElement);

        assertEquals(1, element.childNodeSize());
        assertEquals(childElement, element.childNode(0));
    }

    @Test
    public void testInsertStartTag() {
        Token.StartTag startTag = new Token.StartTag("div", new Attributes());
        Element element = xmlTreeBuilder.insert(startTag);

        assertEquals("div", element.nodeName());
    }

    @Test
    public void testInsertComment() {
        Token.Comment comment = new Token.Comment("This is a comment");
        Comment parsedComment = xmlTreeBuilder.insert(comment);

        assertEquals("This is a comment", parsedComment.getData());
    }

    @Test
    public void testInsertCharacter() {
        Token.Character character = new Token.Character("Hello World");
        TextNode parsedCharacter = xmlTreeBuilder.insert(character);

        assertEquals("Hello World", parsedCharacter.getWholeText());
    }

    @Test
    public void testInsertDoctype() {
        Token.Doctype doctype = new Token.Doctype("html", "", "");
        DocumentType parsedDoctype = xmlTreeBuilder.insert(doctype);

        assertEquals("html", parsedDoctype.getName());
    }

    @Test
    public void testPopStackToClose() {
        Element element1 = new Element(Tag.valueOf("div"), "http://example.com");
        Element element2 = new Element(Tag.valueOf("div"), "http://example.com");
        xmlTreeBuilder.stack.add(element1);
        xmlTreeBuilder.stack.add(element2);

        Token.EndTag endTag = new Token.EndTag("div");
        xmlTreeBuilder.popStackToClose(endTag);

        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals(element1, xmlTreeBuilder.stack.get(0));
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<div><p>Hello World</p></div>";
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;

        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings);

        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        Element element = (Element) nodes.get(0);
        assertEquals("div", element.nodeName());
        assertEquals(1, element.children().size());
        assertTrue(element.child(0) instanceof Element);
        Element childElement = element.child(0);
        assertEquals("p", childElement.nodeName());
        assertEquals("Hello World", childElement.text());
    }
}