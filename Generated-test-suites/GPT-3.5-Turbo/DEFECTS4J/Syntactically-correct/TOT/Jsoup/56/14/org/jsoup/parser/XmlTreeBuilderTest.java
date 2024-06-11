package org.jsoup.parser;

import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    // Test case for initialiseParse() method

    @Test
    public void testInitialiseParse() {
        String input = "<html><head></head><body></body></html>";
        String baseUri = "https://www.example.com";
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = xmlTreeBuilder.defaultSettings();

        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);

        assertEquals(input, xmlTreeBuilder.doc.body().html());
        assertEquals(baseUri, xmlTreeBuilder.doc.baseUri());
        assertFalse(errors.hasErrors());
        assertEquals(settings, xmlTreeBuilder.settings);
    }

    // Test case for process() method

    @Test
    public void testProcess() {
        Token.StartTag startTag = new Token.StartTag("div");
        Element element = new Element(Tag.valueOf("div"), "https://www.example.com");
        xmlTreeBuilder.stack.add(element);

        boolean result = xmlTreeBuilder.process(startTag);

        assertTrue(result);
        assertEquals(startTag.name(), element.tagName());
        assertEquals(xmlTreeBuilder.stack.get(0), element);
    }

    // Test case for insertNode() method

    @Test
    public void testInsertNode() {
        Element element = new Element(Tag.valueOf("div"), "https://www.example.com");
        xmlTreeBuilder.stack.add(element);

        TextNode textNode = new TextNode("Hello, World!", "https://www.example.com");
        xmlTreeBuilder.insertNode(textNode);

        assertEquals(textNode, xmlTreeBuilder.currentElement().childNode(0));
    }

    // Test case for insert() method

    @Test
    public void testInsertStartTag() {
        Token.StartTag startTag = new Token.StartTag("div");
        Element element = new Element(Tag.valueOf("div"), "https://www.example.com");
        xmlTreeBuilder.stack.add(element);

        Element result = xmlTreeBuilder.insert(startTag);

        assertEquals(startTag.name(), result.tagName());
        assertTrue(result.children().isEmpty());
        assertTrue(xmlTreeBuilder.tokeniser.selfClosingFlagAcknowledged());
    }

    @Test
    public void testInsertSelfClosingTag() {
        Token.StartTag startTag = new Token.StartTag("link").selfClosing();
        Element element = new Element(Tag.valueOf("link"), "https://www.example.com");
        xmlTreeBuilder.stack.add(element);
        
        Element result = xmlTreeBuilder.insert(startTag);
        
        assertEquals(startTag.name(), result.tagName());
        assertTrue(result.children().isEmpty());
        assertFalse(result.tag().isKnownTag());
        assertTrue(result.tag().isSelfClosing());
        assertTrue(xmlTreeBuilder.tokeniser.selfClosingFlagAcknowledged());
    }

    // Test case for insert(Token.Comment) method

    @Test
    public void testInsertComment() {
        Token.Comment commentToken = new Token.Comment("This is a comment");
        Comment comment = new Comment(commentToken.getData(), xmlTreeBuilder.baseUri);

        xmlTreeBuilder.insert(commentToken);

        assertEquals(comment, xmlTreeBuilder.currentElement().childNode(0));
    }

    @Test
    public void testInsertXmlDeclarationComment() {
        Token.Comment commentToken = new Token.Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        Comment comment = new Comment(commentToken.getData(), xmlTreeBuilder.baseUri);
        Tag tag = Tag.valueOf("xml");

        xmlTreeBuilder.insert(commentToken);

        assertEquals(comment, xmlTreeBuilder.currentElement().childNode(0));
        assertEquals(tag, xmlTreeBuilder.currentElement().childNode(0).nodeName());
        assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof XmlDeclaration);
    }

    // Test case for insert(Token.Character) method

    @Test
    public void testInsertCharacter() {
        Token.Character characterToken = new Token.Character("Hello, World!");
        TextNode textNode = new TextNode(characterToken.getData(), xmlTreeBuilder.baseUri);

        xmlTreeBuilder.insert(characterToken);

        assertEquals(textNode, xmlTreeBuilder.currentElement().childNode(0));
    }

    // Test case for insert(Token.Doctype) method

    @Test
    public void testInsertDoctype() {
        Token.Doctype doctype = new Token.Doctype("html", "", "", xmlTreeBuilder.baseUri);
        DocumentType documentType = new DocumentType("html", "", "", xmlTreeBuilder.baseUri);

        xmlTreeBuilder.insert(doctype);

        assertEquals(documentType, xmlTreeBuilder.currentElement().childNode(0));
    }

    // Test case for popStackToClose() method

    @Test
    public void testPopStackToClose() {
        Token.EndTag endTag = new Token.EndTag("body");
        Element element1 = new Element(Tag.valueOf("div"), "https://www.example.com");
        Element element2 = new Element(Tag.valueOf("body"), "https://www.example.com");
        xmlTreeBuilder.stack.add(element1);
        xmlTreeBuilder.stack.add(element2);

        xmlTreeBuilder.popStackToClose(endTag);

        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals(element1, xmlTreeBuilder.stack.get(0));
    }

    @Test
    public void testPopStackToCloseNotFound() {
        Token.EndTag endTag = new Token.EndTag("html");
        Element element1 = new Element(Tag.valueOf("div"), "https://www.example.com");
        Element element2 = new Element(Tag.valueOf("body"), "https://www.example.com");
        xmlTreeBuilder.stack.add(element1);
        xmlTreeBuilder.stack.add(element2);

        xmlTreeBuilder.popStackToClose(endTag);

        assertEquals(2, xmlTreeBuilder.stack.size());
        assertEquals(element2, xmlTreeBuilder.stack.get(1));
    }

    // Test case for parseFragment() method

    @Test
    public void testParseFragment() {
        String inputFragment = "<p>Hello, World!</p>";
        String baseUri = "https://www.example.com";
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = xmlTreeBuilder.defaultSettings();

        xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings);

        assertEquals(1, xmlTreeBuilder.doc.childNodes().size());
        assertTrue(xmlTreeBuilder.doc.childNode(0) instanceof Element);
        assertEquals("p", xmlTreeBuilder.doc.childNode(0).nodeName());
    }

}