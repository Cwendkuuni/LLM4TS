package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import java.util.List;

public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        ParseSettings settings = builder.defaultSettings();
        assertNotNull(settings);
    }

    @Test
    public void testParse() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String input = "<xml><tag>content</tag></xml>";
        String baseUri = "https://www.example.com";
        Document doc = builder.parse(input, baseUri);
        assertNotNull(doc);
        assertEquals("https://www.example.com", doc.baseUri());
        assertNotNull(doc.childNode(0));
        assertTrue(doc.childNode(0) instanceof Element);
        Element element = (Element) doc.childNode(0);
        assertEquals("xml", element.nodeName());
        assertNotNull(element.childNode(0));
        assertTrue(element.childNode(0) instanceof Element);
        Element nestedElement = (Element) element.childNode(0);
        assertEquals("tag", nestedElement.nodeName());
        assertNotNull(nestedElement.childNode(0));
        assertTrue(nestedElement.childNode(0) instanceof TextNode);
        TextNode textNode = (TextNode) nestedElement.childNode(0);
        assertEquals("content", textNode.text());
    }

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String input = "<xml><tag>content</tag></xml>";
        String baseUri = "https://www.example.com";
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = new ParseSettings(true, true);
        builder.initialiseParse(input, baseUri, errors, settings);
        assertNotNull(builder.stack);
        assertNotNull(builder.doc);
        assertNotNull(builder.tokeniser);
    }

    @Test
    public void testProcess() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token startTag = new Token.StartTag();
        startTag.type = Token.TokenType.StartTag;
        builder.process(startTag);
        assertEquals(1, builder.stack.size());
        Token endTag = new Token.EndTag();
        endTag.type = Token.TokenType.EndTag;
        builder.process(endTag);
        assertEquals(0, builder.stack.size());
        Token comment = new Token.Comment();
        comment.type = Token.TokenType.Comment;
        builder.process(comment);
        assertNotNull(builder.currentElement());
        assertTrue(builder.currentElement().childNode(0) instanceof Comment);
        Token character = new Token.Character();
        character.type = Token.TokenType.Character;
        builder.process(character);
        assertNotNull(builder.currentElement());
        assertTrue(builder.currentElement().childNode(0) instanceof TextNode);
        Token doctype = new Token.Doctype();
        doctype.type = Token.TokenType.Doctype;
        builder.process(doctype);
        assertNotNull(builder.currentElement());
        assertTrue(builder.currentElement().childNode(0) instanceof DocumentType);
        Token eof = new Token.EOF();
        eof.type = Token.TokenType.EOF;
        builder.process(eof);
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element element = new Element(Tag.valueOf("tag"), "https://www.example.com");
        builder.insertNode(element);
        assertNotNull(builder.currentElement());
        assertEquals("tag", builder.currentElement().nodeName());
        assertNotNull(builder.currentElement().childNode(0));
        assertSame(element, builder.currentElement().childNode(0));
    }

    @Test
    public void testInsert() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("tag");
        builder.insert(startTag);
        assertNotNull(builder.currentElement());
        assertEquals("tag", builder.currentElement().nodeName());
        assertTrue(builder.currentElement().childNode(0) instanceof Element);
        Element element = (Element) builder.currentElement().childNode(0);
        assertEquals("tag", element.nodeName());
        Token.Comment commentToken = new Token.Comment();
        commentToken.data("This is a comment.");
        builder.insert(commentToken);
        assertNotNull(builder.currentElement());
        assertEquals("tag", builder.currentElement().nodeName());
        assertTrue(builder.currentElement().childNode(1) instanceof Comment);
        Comment comment = (Comment) builder.currentElement().childNode(1);
        assertEquals("This is a comment.", comment.getData());
        Token.Character characterToken = new Token.Character();
        characterToken.data("This is a text node.");
        builder.insert(characterToken);
        assertNotNull(builder.currentElement());
        assertEquals("tag", builder.currentElement().nodeName());
        assertTrue(builder.currentElement().childNode(2) instanceof TextNode);
        TextNode textNode = (TextNode) builder.currentElement().childNode(2);
        assertEquals("This is a text node.", textNode.text());
        Token.Doctype doctypeToken = new Token.Doctype();
        doctypeToken.name("html");
        builder.insert(doctypeToken);
        assertNotNull(builder.currentElement());
        assertEquals("tag", builder.currentElement().nodeName());
        assertTrue(builder.currentElement().childNode(3) instanceof DocumentType);
        DocumentType documentType = (DocumentType) builder.currentElement().childNode(3);
        assertEquals("html", documentType.getName());
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element element1 = new Element(Tag.valueOf("tag1"), "https://www.example.com");
        Element element2 = new Element(Tag.valueOf("tag2"), "https://www.example.com");
        Element element3 = new Element(Tag.valueOf("tag3"), "https://www.example.com");
        Element element4 = new Element(Tag.valueOf("tag4"), "https://www.example.com");
        builder.stack.add(element1);
        builder.stack.add(element2);
        builder.stack.add(element3);
        builder.stack.add(element4);
        Token.EndTag endTag = new Token.EndTag();
        endTag.name("tag2");
        builder.popStackToClose(endTag);
        assertEquals(2, builder.stack.size());
        assertEquals(element1, builder.stack.get(0));
        assertEquals(element4, builder.stack.get(1));
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String input = "<tag>content</tag>";
        String baseUri = "https://www.example.com";
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = new ParseSettings(true, true);
        List<Node> nodes = builder.parseFragment(input, baseUri, errors, settings);
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        Element element = (Element) nodes.get(0);
        assertEquals("tag", element.nodeName());
        assertNotNull(element.childNode(0));
        assertTrue(element.childNode(0) instanceof TextNode);
        TextNode textNode = (TextNode) element.childNode(0);
        assertEquals("content", textNode.text());
    }

}