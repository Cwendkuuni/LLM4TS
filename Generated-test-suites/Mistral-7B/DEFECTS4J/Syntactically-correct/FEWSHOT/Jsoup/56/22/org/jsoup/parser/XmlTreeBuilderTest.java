package org.jsoup.parser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.junit.Test;

public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        ParseSettings parseSettings = xmlTreeBuilder.defaultSettings();
        assertEquals(ParseSettings.preserveCase, parseSettings);
    }

    @Test
    public void testParseWithBaseUri() {
        String xmlInput = "<root><element attr=\"value\">Hello World</element></root>";
        String baseUri = "http://example.com";

        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document xmlDoc = xmlTreeBuilder.parse(xmlInput, baseUri);

        Element rootElement = xmlDoc.rootElement();
        assertEquals("root", rootElement.tagName());
        assertEquals(baseUri, rootElement.baseUri());

        Element element = rootElement.child(0);
        assertEquals("element", element.tagName());
        assertEquals("value", element.attribute("attr").getValue());
        assertEquals("Hello World", element.text());
    }

    @Test
    public void testProcessStartTag() {
        String xmlInput = "<root><element attr=\"value\">Hello World</element></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

        Element rootElement = new Element(Tag.ROOT, null, null);
        Element element = new Element(Tag.ELEMENT, null, null);

        xmlTreeBuilder.initialiseParse(xmlInput, null, null, xmlTreeBuilder.defaultSettings());

        Token startTagToken = new Token.StartTag("element", null, new Attribute[]{new Attribute("attr", "value")});
        xmlTreeBuilder.process(startTagToken);

        assertSame(element, xmlTreeBuilder.currentElement());
        assertSame(element, stack.peek());
    }

    @Test
    public void testProcessEndTag() {
        String xmlInput = "<root><element><subElement/></element></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

        Element rootElement = new Element(Tag.ROOT, null, null);
        Element element = new Element(Tag.ELEMENT, null, null);
        Element subElement = new Element(Tag.SUBELEMENT, null, null);

        xmlTreeBuilder.initialiseParse(xmlInput, null, null, xmlTreeBuilder.defaultSettings());

        Token startTagToken = new Token.StartTag("element", null, null);
        xmlTreeBuilder.process(startTagToken);
        assertSame(element, xmlTreeBuilder.currentElement());
        assertSame(element, stack.peek());

        Token endTagToken = new Token.EndTag("element");
        xmlTreeBuilder.process(endTagToken);
        assertSame(rootElement, xmlTreeBuilder.currentElement());
        assertNull(stack.peek());

        Token startTagTokenSubElement = new Token.StartTag("subElement", null, null);
        xmlTreeBuilder.process(startTagTokenSubElement);
        assertSame(subElement, xmlTreeBuilder.currentElement());
        assertSame(element, stack.peek());

        xmlTreeBuilder.process(endTagToken);
        assertSame(element, xmlTreeBuilder.currentElement());
        assertNull(stack.peek());
    }

    @Test
    public void testProcessComment() {
        String xmlInput = "<! -- This is a comment --> <root></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

        xmlTreeBuilder.initialiseParse(xmlInput, null, null, xmlTreeBuilder.defaultSettings());

        Token commentToken = new Token.Comment("-- This is a comment --");
        xmlTreeBuilder.process(commentToken);

        Comment comment = (Comment) xmlTreeBuilder.currentElement();
        assertEquals("-- This is a comment --", comment.text());

        Token startTagToken = new Token.StartTag("root", null, null);
        xmlTreeBuilder.process(startTagToken);
        assertSame(null, xmlTreeBuilder.currentElement());
        assertNull(stack.peek());
    }

    @Test
    public void testProcessCharacter() {
        String xmlInput = "<root>Hello World</root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

        xmlTreeBuilder.initialiseParse(xmlInput, null, null, xmlTreeBuilder.defaultSettings());

        Token characterToken = new Token.Character('H');
        xmlTreeBuilder.process(characterToken);

        TextNode textNode = (TextNode) xmlTreeBuilder.currentElement();
        assertEquals("H", textNode.text());

        xmlTreeBuilder.process(characterToken);
        assertEquals("He", textNode.text());

        xmlTreeBuilder.process(characterToken);
        assertEquals("Hel", textNode.text());

        xmlTreeBuilder.process(new Token.Character('\n'));
        Element rootElement = (Element) xmlTreeBuilder.currentElement();
        TextNode textNode2 = (TextNode) rootElement.child(0);
        assertEquals("Hello World\n", textNode2.text());
        assertNull(stack.peek());
    }

    @Test
    public void testProcessDoctype() { //TODO: test with different public and system identifiers
        String xmlInput = "<!DOCTYPE root SYSTEM \"http://example.com/dtd.dtd\"> <root></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

        xmlTreeBuilder.initialiseParse(xmlInput, null, null, xmlTreeBuilder.defaultSettings());

        Token doctypeToken = new Token.Doctype("root", null, null, null);
        xmlTreeBuilder.process(doctypeToken);

        DocumentType documentType = (DocumentType) xmlTreeBuilder.currentElement();
        assertEquals("root", documentType.tagName());
        assertEquals("http://example.com/dtd.dtd", documentType.systemIdentifier());

        Token startTagToken = new Token.StartTag("root", null, null);
        xmlTreeBuilder.process(startTagToken);
        assertSame(null, xmlTreeBuilder.currentElement());
        assertNull(stack.peek());
    }

    @Test
    public void testPopStackToClose() {
        String xmlInput = "<root><element><subElement/></element></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

        Element rootElement = new Element(Tag.ROOT, null, null);
        Element element = new Element(Tag.ELEMENT, null, null);
        Element subElement = new Element(Tag.SUBELEMENT, null, null);

        xmlTreeBuilder.initialiseParse(xmlInput, null, null, xmlTreeBuilder.defaultSettings());

        Token startTagTokenElement = new Token.StartTag("element", null, null);
        xmlTreeBuilder.process(startTagTokenElement);
        assertSame(element, xmlTreeBuilder.currentElement());
        assertSame(element, stack.peek());

        Token startTagTokenSubElement = new Token.StartTag("subElement", null, null);
        xmlTreeBuilder.process(startTagTokenSubElement);
        assertSame(subElement, xmlTreeBuilder.currentElement());
        assertSame(element, stack.peek());

        Token endTagTokenElement = new Token.EndTag("element");
        xmlTreeBuilder.process(endTagTokenElement);

        xmlTreeBuilder.popStackToClose(endTagTokenElement);
        assertSame(rootElement, xmlTreeBuilder.currentElement());
        assertNull(stack.peek());
    }

    @Test
    public void testParseFragment() {
        String xmlFragment = "<root><element><subElement/></element></root>";
        String baseUri = "http://example.com";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

        List<Node> parsedNodes = xmlTreeBuilder.parseFragment(xmlFragment, baseUri, null, xmlTreeBuilder.defaultSettings());

        Element rootElement = (Element) parsedNodes.get(0);
        Element element = (Element) rootElement.child(0);
        Element subElement = (Element) element.child(0);

        assertEquals(3, parsedNodes.size());
        assertEquals("root", rootElement.tagName());
        assertEquals("element", element.tagName());
        assertEquals("subElement", subElement.tagName());
        assertEquals(baseUri, rootElement.baseUri());

        assertEquals("element", element.parent().tagName());
        assertEquals("root", element.parent().parent().tagName());
        assertEquals(null, element.parent().parent().parent());
    }

    private List<Element> stack = new ArrayList<>();
}