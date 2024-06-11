package org.jsoup.parser;

import static org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Token;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.Tokeniser;
import org.junit.Test;

public class TreeBuilderTest {

    @Test
    public void testParseEmptyString() {
        TreeBuilder treeBuilder = new DocumentBuilder();
        Document doc = treeBuilder.parse("", "", ParseErrorList.noTracking());
        assertNotNull(doc);
        assertEquals(0, doc.elementCount());
    }

    @Test
    public void testParseSingleTag() {
        TreeBuilder treeBuilder = new DocumentBuilder();
        Document doc = treeBuilder.parse("<html/>", "", ParseErrorList.noTracking());
        Element root = doc.body();
        assertNotNull(root);
        assertEquals("html", root.tagName());
        assertEquals(0, root.childElementCount());
    }

    @Test
    public void testParseMultipleTags() {
        TreeBuilder treeBuilder = new DocumentBuilder();
        Document doc = treeBuilder.parse("<html><head></head><body></body></html>", "", ParseErrorList.noTracking());
        Element root = doc.body();
        assertNotNull(root);
        assertEquals("html", root.tagName());
        assertEquals(1, root.childElementCount());

        Element head = root.child(0);
        assertNotNull(head);
        assertEquals("head", head.tagName());
        assertEquals(0, head.childElementCount());

        Element body = root.child(1);
        assertNotNull(body);
        assertEquals("body", body.tagName());
        assertEquals(0, body.childElementCount());
    }

    @Test
    public void testParseWithErrors() {
        TreeBuilder treeBuilder = new DocumentBuilder();
        ParseErrorList errors = new ParseErrorList();
        Document doc = treeBuilder.parse("<!--This is a comment--><html><h1 <tag>", "", errors);
        assertEquals(1, errors.size());
        assertEquals("Error parsing StartTag: invalid element name: tag (position:line 1:13)", errors.get(0).getMessage());

        Element root = doc.body();
        assertNotNull(root);
        assertEquals("html", root.tagName());
        assertEquals(0, root.childElementCount());
    }

    @Test
    public void testParseStartTag() {
        TreeBuilder treeBuilder = new DocumentBuilder();
        Token startTag = new Token.StartTag("html");
        treeBuilder.processStartTag(startTag);
        Element root = treeBuilder.currentElement();
        assertNotNull(root);
        assertEquals("html", root.tagName());
        assertEquals(0, root.childElementCount());
    }

    @Test
    public void testParseStartTagWithAttributes() {
        TreeBuilder treeBuilder = new DocumentBuilder();
        Attributes attributes = new Attributes();
        attributes.put("class", "myClass");
        Token startTag = new Token.StartTag("div", attributes);
        treeBuilder.processStartTag(startTag);
        Element root = treeBuilder.currentElement();
        assertNotNull(root);
        assertEquals("div", root.tagName());
        assertEquals("myClass", root.classes().get(0));
        assertEquals(0, root.childElementCount());
    }

    @Test
    public void testParseEndTag() {
        TreeBuilder treeBuilder = new DocumentBuilder();
        treeBuilder.processStartTag(new Token.StartTag("div"));
        treeBuilder.processStartTag(new Token.StartTag("p"));
        treeBuilder.processEndTag("p");
        Element root = treeBuilder.currentElement();
        assertNotNull(root);
        assertEquals("div", root.tagName());
        assertEquals(0, root.childElementCount());
    }

    @Test(expected = IllegalStateException.class)
    public void testProcessEndTagWithoutStartTag() {
        TreeBuilder treeBuilder = new DocumentBuilder();
        treeBuilder.processEndTag("div");
    }

    @Test
    public void testProcessStartTagWithInvalidName() {
        TreeBuilder treeBuilder = new DocumentBuilder();
        Token startTag = new Token.StartTag("tag");
        treeBuilder.process(startTag);
        assertFalse(treeBuilder.currentElement() instanceof Element);
    }

    @Test
    public void testProcessEndTagWithInvalidName() {
        TreeBuilder treeBuilder = new DocumentBuilder();
        TreeBuilder.Element currentElement = treeBuilder.currentElement();
        treeBuilder.processEndTag("div");
        assertFalse(treeBuilder.currentElement() instanceof Element);
        assertSame(currentElement, treeBuilder.currentElement());
    }

    @Test
    public void testProcessTokenWithInvalidTokenType() {
        TreeBuilder treeBuilder = new DocumentBuilder();
        Token token = new Token(1, 1, Token.TokenType.COMMENT, "This is a comment");
        assertFalse(treeBuilder.process(token));
    }

    @Test
    public void testProcessTokenWithText() {
        TreeBuilder treeBuilder = new DocumentBuilder();
        Token token = new Token(1, 1, Token.TokenType.TEXT, "Hello world");
        Element parent = treeBuilder.currentElement();
        treeBuilder.process(token);
        Element child = parent.child(0);
        assertEquals("Hello world", child.text());
    }

    @Test
    public void testProcessTokenWithWhitespace() {
        TreeBuilder treeBuilder = new DocumentBuilder();
        Token token = new Token(1, 1, Token.TokenType.TEXT, " \t  \n\t Hello world    ");
        Element parent = treeBuilder.currentElement();
        treeBuilder.process(token);
        Element child = parent.child(0);
        assertEquals("Hello world", child.text());
    }

    @Test
    public void testProcessTokenWithScriptOrStyle() {
        TreeBuilder treeBuilder = new DocumentBuilder();
        Token token = new Token.StartTag("script", new Attributes().src("script.js"));
        treeBuilder.process(token);
        Element script = treeBuilder.currentElement();
        assertEquals("script", script.tagName());
        assertEquals("script.js", script.getAttribute("src"));
        treeBuilder.processEndTag("script");
        assertEquals(0, treeBuilder.stack.size());
    }
}