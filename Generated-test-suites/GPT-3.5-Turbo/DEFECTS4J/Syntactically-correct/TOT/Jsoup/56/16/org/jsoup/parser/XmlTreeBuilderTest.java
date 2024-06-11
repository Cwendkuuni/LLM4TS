package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testDefaultSettings() {
        ParseSettings defaultSettings = xmlTreeBuilder.defaultSettings();
        assertNotNull(defaultSettings);
        assertEquals(ParseSettings.preserveCase, defaultSettings);
    }

    @Test
    public void testParseWithBaseUri() {
        String input = "<body>Hello World</body>";
        String baseUri = "http://example.com";
        Document document = xmlTreeBuilder.parse(input, baseUri);
        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
        Element bodyElement = document.body();
        assertNotNull(bodyElement);
        assertEquals("Hello World", bodyElement.text());
    }

    @Test
    public void testInitialiseParse() {
        String input = "<body>Hello World</body>";
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);
        assertEquals(input, xmlTreeBuilder.getTokeniser().input());
        assertEquals(baseUri, xmlTreeBuilder.getBaseUri());
    }

    @Test
    public void testProcess_WhenTokenTypeIsStartTag() {
        Token token = new Token.StartTag("div");
        boolean result = xmlTreeBuilder.process(token);
        assertTrue(result);
    }

    @Test
    public void testProcess_WhenTokenTypeIsEndTag() {
        Token token = new Token.EndTag("div");
        boolean result = xmlTreeBuilder.process(token);
        assertTrue(result);
    }

    @Test
    public void testProcess_WhenTokenTypeIsComment() {
        Token token = new Token.Comment("This is a comment");
        boolean result = xmlTreeBuilder.process(token);
        assertTrue(result);
    }

    @Test
    public void testProcess_WhenTokenTypeIsCharacter() {
        Token token = new Token.Character("This is a character");
        boolean result = xmlTreeBuilder.process(token);
        assertTrue(result);
    }

    @Test
    public void testProcess_WhenTokenTypeIsDoctype() {
        Token token = new Token.Doctype("html");
        boolean result = xmlTreeBuilder.process(token);
        assertTrue(result);
    }

    @Test
    public void testInsertNode() {
        Element element = new Element("div");
        xmlTreeBuilder.insertNode(element);
        assertEquals(element, xmlTreeBuilder.getCurrentElement().child(0));
    }

    @Test
    public void testInsert_WhenTokenTypeIsStartTag() {
        Token.StartTag startTag = new Token.StartTag("div");
        Element element = xmlTreeBuilder.insert(startTag);
        assertNotNull(element);
        assertEquals("div", element.tagName());
        assertTrue(element.attributes().isEmpty());
    }

    @Test
    public void testInsert_WhenTokenTypeIsEndTag() {
        Token.EndTag endTag = new Token.EndTag("div");
        xmlTreeBuilder.insert(endTag);
        assertNull(xmlTreeBuilder.getCurrentElement());
    }

    @Test
    public void testInsert_WhenTokenTypeIsComment_WithoutBogus() {
        Token.Comment comment = new Token.Comment("This is a comment");
        xmlTreeBuilder.insert(comment);
        assertEquals(1, xmlTreeBuilder.getCurrentElement().childNodeSize());
        assertTrue(xmlTreeBuilder.getCurrentElement().childNode(0) instanceof Comment);
        assertEquals("This is a comment", xmlTreeBuilder.getCurrentElement().childNode(0).toString());
    }

    @Test
    public void testInsert_WhenTokenTypeIsComment_WithBogus() {
        Token.Comment comment = new Token.Comment("<!DOCTYPE html>");
        xmlTreeBuilder.insert(comment);
        assertEquals(1, xmlTreeBuilder.getCurrentElement().childNodeSize());
        assertTrue(xmlTreeBuilder.getCurrentElement().childNode(0) instanceof XmlDeclaration);
        assertEquals("<!DOCTYPE html>", xmlTreeBuilder.getCurrentElement().childNode(0).toString());
    }

    @Test
    public void testInsert_WhenTokenTypeIsCharacter() {
        Token.Character character = new Token.Character("This is a character");
        xmlTreeBuilder.insert(character);
        assertEquals(1, xmlTreeBuilder.getCurrentElement().childNodeSize());
        assertTrue(xmlTreeBuilder.getCurrentElement().childNode(0) instanceof TextNode);
        assertEquals("This is a character", xmlTreeBuilder.getCurrentElement().childNode(0).toString());
    }

    @Test
    public void testInsert_WhenTokenTypeIsDoctype() {
        String name = "html";
        String publicIdentifier = "-//W3C//DTD XHTML 1.0 Strict//EN";
        String systemIdentifier = "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd";
        Token.Doctype doctype = new Token.Doctype(name, publicIdentifier, systemIdentifier);
        xmlTreeBuilder.insert(doctype);
        assertEquals(1, xmlTreeBuilder.getCurrentElement().childNodeSize());
        assertTrue(xmlTreeBuilder.getCurrentElement().childNode(0) instanceof DocumentType);
        assertEquals(name, xmlTreeBuilder.getCurrentElement().childNode(0).nodeName());
        assertEquals(publicIdentifier, ((DocumentType) xmlTreeBuilder.getCurrentElement().childNode(0)).publicIdentifier());
        assertEquals(systemIdentifier, ((DocumentType) xmlTreeBuilder.getCurrentElement().childNode(0)).systemIdentifier());
    }

    @Test
    public void testPopStackToClose_WhenElementExists() {
        Element el1 = new Element("div");
        Element el2 = new Element("span");
        Element el3 = new Element("div");
        xmlTreeBuilder.getStack().add(el1);
        xmlTreeBuilder.getStack().add(el2);
        xmlTreeBuilder.getStack().add(el3);
        Token.EndTag endTag = new Token.EndTag("div");
        xmlTreeBuilder.popStackToClose(endTag);
        assertEquals(1, xmlTreeBuilder.getStack().size());
        assertEquals(el1, xmlTreeBuilder.getStack().get(0));
    }

    @Test
    public void testPopStackToClose_WhenElementDoesNotExist() {
        Element el1 = new Element("div");
        Element el2 = new Element("span");
        xmlTreeBuilder.getStack().add(el1);
        xmlTreeBuilder.getStack().add(el2);
        Token.EndTag endTag = new Token.EndTag("p");
        xmlTreeBuilder.popStackToClose(endTag);
        assertEquals(2, xmlTreeBuilder.getStack().size());
        assertEquals(el2, xmlTreeBuilder.getStack().get(1));
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<div>Hello World</div>";
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;

        xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings);
        assertNotNull(xmlTreeBuilder.getDoc());
        assertEquals(1, xmlTreeBuilder.getDoc().childNodes().size());
        assertTrue(xmlTreeBuilder.getDoc().childNode(0) instanceof Element);
        Element element = (Element) xmlTreeBuilder.getDoc().childNode(0);
        assertEquals("div", element.tagName());
        assertEquals("Hello World", element.text());
    }
}