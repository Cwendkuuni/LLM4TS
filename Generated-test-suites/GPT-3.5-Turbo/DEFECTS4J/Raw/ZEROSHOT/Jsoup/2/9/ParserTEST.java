package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ParserTest {

    private Parser parser;
    private Document doc;

    @Before
    public void setUp() {
        parser = new Parser("<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>", "http://example.com");
        doc = parser.parse();
    }

    @Test
    public void testParse() {
        assertNotNull(doc);
    }

    @Test
    public void testParseBodyFragment() {
        Document fragment = Parser.parseBodyFragment("<div>Hello, World!</div>", "http://example.com");
        assertNotNull(fragment);
    }

    @Test
    public void testParseComment() {
        parser.parseComment();
        Elements comments = doc.select("comment");
        assertEquals(1, comments.size());
        assertEquals("<!-- -->", comments.first().toString());
    }

    @Test
    public void testParseXmlDecl() {
        parser.parseXmlDecl();
        Elements xmlDecls = doc.select("xmldeclaration");
        assertEquals(1, xmlDecls.size());
        assertEquals("<?xml ?>", xmlDecls.first().toString());
    }

    @Test
    public void testParseEndTag() {
        parser.parseEndTag();
        Elements endTags = doc.select("endtag");
        assertEquals(1, endTags.size());
        assertEquals("</ >", endTags.first().toString());
    }

    @Test
    public void testParseStartTag() {
        parser.parseStartTag();
        Elements startTags = doc.select("starttag");
        assertEquals(1, startTags.size());
        assertEquals("< >", startTags.first().toString());
    }

    @Test
    public void testParseTextNode() {
        parser.parseTextNode();
        Elements textNodes = doc.select("textnode");
        assertEquals(1, textNodes.size());
        assertEquals("Hello, World!", textNodes.first().toString());
    }

    @Test
    public void testParseCdata() {
        parser.parseCdata();
        Elements cdataNodes = doc.select("textnode");
        assertEquals(1, cdataNodes.size());
        assertEquals("CDATA", cdataNodes.first().toString());
    }

    @Test
    public void testAddChildToParent() {
        Element child = new Element("div");
        Element parent = parser.addChildToParent(child, false);
        assertNotNull(parent);
        assertEquals(child, parent.child(0));
    }

    @Test
    public void testStackHasValidParent() {
        boolean validParent = parser.stackHasValidParent(new Tag("div"));
        assertEquals(true, validParent);
    }

    @Test
    public void testPopStackToSuitableContainer() {
        Element container = parser.popStackToSuitableContainer(new Tag("div"));
        assertNull(container);
    }

    @Test
    public void testPopStackToClose() {
        Element closeTag = parser.popStackToClose(new Tag("div"));
        assertNull(closeTag);
    }

    @Test
    public void testLast() {
        Element lastElement = parser.last();
        assertNotNull(lastElement);
    }
}