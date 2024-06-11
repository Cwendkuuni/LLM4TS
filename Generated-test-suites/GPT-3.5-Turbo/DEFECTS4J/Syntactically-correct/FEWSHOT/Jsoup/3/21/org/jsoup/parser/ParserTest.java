package org.jsoup.parser;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ParserTest {
    @Test
    public void testParse() {
        Document doc = Parser.parse("<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>", "http://example.com");
        assertEquals("Test", doc.title());
        assertEquals("Hello World", doc.body().text());
    }

    @Test
    public void testParseBodyFragment() {
        Document doc = Parser.parseBodyFragment("<h1>Hello World</h1>", "http://example.com");
        assertEquals("Hello World", doc.body().text());
    }

    @Test
    public void testParseTextNode() {
        Parser parser = new Parser("", "http://example.com", false);
        parser.parseTextNode();
        assertNull(parser.last().childNode(0));
    }

    @Test
    public void testParseCdata() {
        Parser parser = new Parser("", "http://example.com", false);
        parser.parseCdata();
        assertNull(parser.last().childNode(0));
    }

    @Test
    public void testParseComment() {
        Parser parser = new Parser("", "http://example.com", false);
        parser.parseComment();
        assertNull(parser.last().childNode(0));
    }

    @Test
    public void testParseXmlDecl() {
        Parser parser = new Parser("", "http://example.com", false);
        parser.parseXmlDecl();
        assertNull(parser.last().childNode(0));
    }

    @Test
    public void testParseEndTag() {
        Parser parser = new Parser("", "http://example.com", false);
        parser.parseEndTag();
        assertNull(parser.last());
    }

    @Test
    public void testParseStartTag() {
        Parser parser = new Parser("", "http://example.com", false);
        parser.parseStartTag();
        assertNull(parser.last());
    }

    @Test
    public void testParseAttribute() {
        Parser parser = new Parser("", "http://example.com", false);
        Attribute attribute = parser.parseAttribute();
        assertNull(attribute);
    }

    @Test
    public void testAddChildToParent() {
        Parser parser = new Parser("", "http://example.com", false);
        Element child = new Element(Tag.valueOf("div"), "http://example.com");
        parser.addChildToParent(child, false);
        assertEquals(child, parser.last());
    }

    @Test
    public void testPopStackToSuitableContainer() {
        Parser parser = new Parser("", "http://example.com", false);
        Element parent = new Element(Tag.valueOf("div"), "http://example.com");
        Element child = new Element(Tag.valueOf("span"), "http://example.com");
        parser.stack.addLast(parent);
        Element suitableContainer = parser.popStackToSuitableContainer(child.tag());
        assertEquals(parent, suitableContainer);
    }

    @Test
    public void testStackHasValidParent() {
        Parser parser = new Parser("", "http://example.com", false);
        Tag childTag = Tag.valueOf("span");
        boolean validAncestor = parser.stackHasValidParent(childTag);
        assertFalse(validAncestor);
    }

    @Test
    public void testPopStackToClose() {
        Parser parser = new Parser("", "http://example.com", false);
        Element el = new Element(Tag.valueOf("div"), "http://example.com");
        parser.stack.addLast(el);
        Element closed = parser.popStackToClose(Tag.valueOf("div"));
        assertEquals(el, closed);
    }

    @Test
    public void testLast() {
        Parser parser = new Parser("", "http://example.com", false);
        Element el = new Element(Tag.valueOf("div"), "http://example.com");
        parser.stack.addLast(el);
        assertEquals(el, parser.last());
    }
}