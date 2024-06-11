package org.jsoup.parser;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.*;
import static org.junit.Assert.*;

public class ParserTest {

    private Parser parser;

    @Before
    public void setUp() {
        String html = "<html><head><title>Test Title</title></head><body><h1>Test Heading</h1></body></html>";
        parser = new Parser(html, "http://test.com");
    }

    

    @Test
    public void testParse() {
        Document doc = parser.parse();
        assertEquals("Test Title", doc.title().text());
        assertEquals("Test Heading", doc.body().getElementsByTag("h1").text());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseNullHtml() {
        new Parser(null, "http://test.com", false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseNullBaseUri() {
        new Parser("<html></html>", null, false);
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Body Fragment</p>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://test.com");
        assertEquals("<p>Body Fragment</p>", doc.body().html());
    }

    @Test
    public void testParseComment() {
        parser.parseComment();
        assertEquals("", parser.last().html());
        Element comment = parser.last();
        assertTrue(comment.tagName().equals("comment"));
    }

    @Test
    public void testParseXmlDecl() {
        parser.parseXmlDecl();
        Element xmlDecl = parser.last();
        assertTrue(xmlDecl.tagName().equals("xml-declaration"));
    }

    @Test
    public void testParseEndTag() {
        parser.parseStartTag("div");
        parser.parseEndTag("div");
        assertEquals(null, parser.last());
    }

    @Test
    public void testParseStartTag() {
        parser.parseStartTag("p");
        Element p = parser.last();
        assertTrue(p.tagName().equals("p"));

        parser.parseStartTag("p"); // test overwriting previous tag
        p = parser.last();
        assertTrue(p.tagName().equals("p"));

        parser.parseStartTag("div"); // test opening new tag
        Element div = parser.last();
        assertTrue(div.tagName().equals("div"));
    }

    @Test
    public void testParseAttribute() {
        parser.parseStartTag("a");
        parser.parseAttribute();
        parser.parseAttribute(); // test overwriting previous attribute
        Attribute attr = parser.last();
        String key = attr.key();
        String value = attr.value();
        assertTrue(StringUtils.isNotEmpty(key));
        assertTrue(StringUtils.isNotEmpty(value));
    }

    @Test
    public void testParseTextNode() {
        parser.parseTextNode();
        TextNode textNode = parser.last();
        assertTrue(textNode != null);
        String text = textNode.text();
        assertTrue(StringUtils.isNotEmpty(text));
    }

    @Test
    public void testParseCdata() {
        parser.parseCdata();
        TextNode cdataNode = parser.last();
        assertTrue(cdataNode != null);
        String cdata = cdataNode.text();
        assertTrue(StringUtils.isNotEmpty(cdata));
    }

    @Test
    public void testAddChildToParent() {
        parser.parseStartTag("div");
        parser.parseStartTag("p");
        Element parentDiv = parser.last();
        Element childP = parser.last();
        parentDiv.appendChild(childP);
        assertEquals(childP, parentDiv.child(0));
    }

    @Test
    public void testStackHasValidParent() {
        parser.parseStartTag("html");
        parser.parseStartTag("body");
        parser.parseStartTag("div");
        parser.parseStartTag("p");

        assertTrue(Parser.stackHasValidParent(Tag.valueOf("p"), parser.last()));
        assertTrue(Parser.stackHasValidParent(Tag.valueOf("div"), parser.last()));
        assertTrue(Parser.stackHasValidParent(Tag.valueOf("body"), parser.last()));
        assertTrue(Parser.stackHasValidParent(Tag.valueOf("html"), parser.last()));

        assertFalse(Parser.stackHasValidParent(Tag.valueOf("input"), parser.last()));
    }

    @Test
    public void testPopStackToSuitableContainer() {
        parser.parseStartTag("html");
        parser.parseStartTag("head");
        parser.parseStartTag("title");
        parser.parseStartTag("body");
        parser.parseStartTag("div");

        Element container = Parser.popStackToSuitableContainer(Tag.valueOf("title"));
        Element title = parser.last();
        container.appendChild(title);
        assertEquals(title, container.child(0));

        container = Parser.popStackToSuitableContainer(Tag.valueOf("div"));
        Element div = parser.last();
        container.appendChild(div);
        assertEquals(div, container.child(0));

        container = Parser.popStackToSuitableContainer(Tag.valueOf("body"));
        Element body = parser.last();
        container.appendChild(body);
        assertEquals(body, container.child(0));

        container = Parser.popStackToSuitableContainer(Tag.valueOf("head"));
        Element head = parser.last();
        container.appendChild(head);
        assertEquals(head, container.child(0));

        container = Parser.popStackToSuitableContainer(Tag.valueOf("title"));
        assertTrue(container == null);
    }

    @Test
    public void testPopStackToClose() {
        parser.parseStartTag("html");
        parser.parseStartTag("head");
        parser.parseStartTag("title");
        parser.parseStartTag("body");
        parser.parseStartTag("div");
        parser.parseEndTag("div");
        parser.parseEndTag("body");
        parser.parseEndTag("html");

        Element divToClose = Parser.popStackToClose(Tag.valueOf("div"));
        assertEquals(parser.last(), divToClose);

        parser.parseStartTag("html");
        parser.parseStartTag("head");
        parser.parseStartTag("title");
        parser.parseStartTag("body");
        parser.parseStartTag("div");
        parser.parseStartTag("p");
        parser.parseEndTag("p");
        parser.parseEndTag("div");
        parser.parseEndTag("body");
        parser.parseEndTag("html");

        Element pToClose = Parser.popStackToClose(Tag.valueOf("p"));
        assertEquals(parser.last(), pToClose);

        parser.parseStartTag("html");
        parser.parseStartTag("head");
        parser.parseStartTag("title");
        parser.parseStartTag("body");
        parser.parseStartTag("div");
        parser.parseStartTag("p");
        parser.parseEndTag("p");
        parser.parseEndTag("div");
        parser.parseEndTag("body");
        parser.parseEndTag("html");

        Element divToCloseAgain = Parser.popStackToClose(Tag.valueOf("div"));
        assertEquals(parser.last(), divToCloseAgain);
    }

    
}