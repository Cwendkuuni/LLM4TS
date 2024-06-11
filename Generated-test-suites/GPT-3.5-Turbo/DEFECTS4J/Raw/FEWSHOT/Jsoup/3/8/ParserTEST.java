package org.jsoup.parser;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.*;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ParserTest {
    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parse(html, baseUri);

        assertEquals(1, doc.children().size());
        Element htmlElement = doc.child(0);
        assertEquals("html", htmlElement.tagName());

        Element headElement = htmlElement.child(0);
        assertEquals("head", headElement.tagName());

        Element titleElement = headElement.child(0);
        assertEquals("title", titleElement.tagName());
        assertEquals("Test", titleElement.text());

        Element bodyElement = htmlElement.child(1);
        assertEquals("body", bodyElement.tagName());

        Element h1Element = bodyElement.child(0);
        assertEquals("h1", h1Element.tagName());
        assertEquals("Hello World!", h1Element.text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World!</h1>";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

        Element bodyElement = doc.body();
        assertEquals("body", bodyElement.tagName());

        Element h1Element = bodyElement.child(0);
        assertEquals("h1", h1Element.tagName());
        assertEquals("Hello World!", h1Element.text());
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("'foo'"));
        assertEquals("foo 'bar'", Util.stripLeadingAndTrailingQuotes("foo 'bar'"));
        assertEquals("'foo' bar", Util.stripLeadingAndTrailingQuotes("'foo' bar"));
        assertEquals("'foo' and 'bar'", Util.stripLeadingAndTrailingQuotes("'foo' and 'bar'"));
        assertEquals("", Util.stripLeadingAndTrailingQuotes(""));
    }

    @Test
    public void testStripLeadingHyphens() {
        assertEquals("f", Util.stripLeadingHyphens("-f"));
        assertEquals("foo", Util.stripLeadingHyphens("--foo"));
        assertEquals("-foo", Util.stripLeadingHyphens("---foo"));
        assertNull(Util.stripLeadingHyphens(null));
    }

    @Test
    public void testParseComment() {
        Parser parser = new Parser("", "", false);
        parser.parseComment();

        assertEquals(1, parser.last().childNodeSize());
        assertTrue(parser.last().childNode(0) instanceof Comment);
    }

    @Test
    public void testParseXmlDecl() {
        Parser parser = new Parser("", "", false);
        parser.parseXmlDecl();

        assertEquals(1, parser.last().childNodeSize());
        assertTrue(parser.last().childNode(0) instanceof XmlDeclaration);
    }

    @Test
    public void testParseEndTag() {
        Parser parser = new Parser("", "", false);
        parser.parseEndTag();

        assertNull(parser.popStackToClose(null));
    }

    @Test
    public void testParseStartTag() {
        Parser parser = new Parser("", "", false);
        parser.parseStartTag();

        assertEquals(1, parser.stack.size());
        assertTrue(parser.last() instanceof Element);
    }

    @Test
    public void testParseAttribute() {
        Parser parser = new Parser("", "", false);
        Attribute attribute = parser.parseAttribute();

        assertNull(attribute);
    }

    @Test
    public void testParseTextNode() {
        Parser parser = new Parser("", "", false);
        parser.parseTextNode();

        assertEquals(1, parser.last().childNodeSize());
        assertTrue(parser.last().childNode(0) instanceof TextNode);
    }

    @Test
    public void testParseCdata() {
        Parser parser = new Parser("", "", false);
        parser.parseCdata();

        assertEquals(1, parser.last().childNodeSize());
        assertTrue(parser.last().childNode(0) instanceof TextNode);
    }

    @Test
    public void testAddChildToParent() {
        Parser parser = new Parser("", "", false);
        Element child = new Element(Tag.valueOf("div"), "");
        boolean isEmptyElement = false;
        Element parent = parser.addChildToParent(child, isEmptyElement);

        assertEquals(1, parent.childNodeSize());
        assertEquals(child, parent.childNode(0));
    }

    @Test
    public void testStackHasValidParent() {
        Parser parser = new Parser("", "", false);
        Tag childTag = Tag.valueOf("div");
        boolean validAncestor = parser.stackHasValidParent(childTag);

        assertTrue(validAncestor);
    }

    @Test
    public void testPopStackToSuitableContainer() {
        Parser parser = new Parser("", "", false);
        Tag tag = Tag.valueOf("div");
        Element parent = parser.popStackToSuitableContainer(tag);

        assertNull(parent);
    }

    @Test
    public void testPopStackToClose() {
        Parser parser = new Parser("", "", false);
        Tag tag = Tag.valueOf("div");
        Element element = parser.popStackToClose(tag);

        assertNull(element);
    }

    @Test
    public void testLast() {
        Parser parser = new Parser("", "", false);
        Element element = parser.last();

        assertNull(element);
    }
}