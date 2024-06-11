package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.*;

import static org.junit.Assert.*;

public class ParserTest {

    private Document doc;

    @Before
    public void setUp() {
        doc = new Document("");
    }

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>";
        doc = Parser.parse(html, "http://example.com");
        assertNotNull(doc);
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello, World!</h1>";
        doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");
        assertNotNull(doc);
    }

    @Test
    public void testParseComment() {
        String html = "<!-- This is a comment -->";
        doc = Parser.parse(html, "http://example.com");
        Comment comment = (Comment) doc.childNode(0);
        assertEquals(" This is a comment ", comment.getData());
    }

    @Test
    public void testParseXmlDecl() {
        String html = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        doc = Parser.parse(html, "http://example.com");
        XmlDeclaration xmlDecl = (XmlDeclaration) doc.childNode(0);
        assertEquals("1.0", xmlDecl.getVersion());
        assertEquals("UTF-8", xmlDecl.getEncoding());
    }

    @Test
    public void testParseEndTag() {
        String html = "</body></html>";
        doc = Parser.parse(html, "http://example.com");
        Element body = doc.body();
        assertNull(body);
    }

    @Test
    public void testParseStartTag() {
        String html = "<body><h1>Hello, World!</h1></body>";
        doc = Parser.parse(html, "http://example.com");
        Element body = doc.body();
        assertNotNull(body);
        assertEquals("body", body.tagName());
    }

    @Test
    public void testParseTextNode() {
        String html = "Hello, World!";
        doc = Parser.parse(html, "http://example.com");
        TextNode textNode = (TextNode) doc.body().childNode(0);
        assertEquals("Hello, World!", textNode.getWholeText());
    }

    @Test
    public void testParseCdata() {
        String html = "<![CDATA[This is CDATA]]>";
        doc = Parser.parse(html, "http://example.com");
        TextNode textNode = (TextNode) doc.body().childNode(0);
        assertEquals("This is CDATA", textNode.getWholeText());
    }

    @Test
    public void testParseAttribute() {
        String html = "<div class=\"example\" id=\"test\"></div>";
        doc = Parser.parse(html, "http://example.com");
        Element div = doc.body().child(0);
        assertEquals("example", div.attr("class"));
        assertEquals("test", div.attr("id"));
    }

    @Test
    public void testAddChildToParent() {
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        doc = Parser.parse(html, "http://example.com");
        Element body = doc.body();
        Element h1 = body.child(0);
        assertNotNull(h1);
        assertEquals("h1", h1.tagName());
        assertEquals("Hello, World!", h1.text());
    }

    @Test
    public void testStackHasValidParent() {
        Parser parser = new Parser("", "http://example.com", false);
        parser.parseStartTag();
        parser.parseStartTag();
        parser.parseStartTag();
        parser.parseStartTag();
        Tag childTag = Tag.valueOf("h1");
        boolean validAncestor = parser.stackHasValidParent(childTag);
        assertFalse(validAncestor);
    }

    @Test
    public void testPopStackToSuitableContainer() {
        Parser parser = new Parser("", "http://example.com", false);
        parser.parseStartTag();
        parser.parseStartTag();
        Tag tag = Tag.valueOf("h1");
        Element parent = parser.popStackToSuitableContainer(tag);
        assertNotNull(parent);
        assertEquals("body", parent.tagName());
    }

    @Test
    public void testPopStackToClose() {
        Parser parser = new Parser("", "http://example.com", false);
        parser.parseStartTag();
        parser.parseStartTag();
        parser.parseStartTag();
        Tag tag = Tag.valueOf("h1");
        Element elToClose = parser.popStackToClose(tag);
        assertNotNull(elToClose);
        assertEquals("h1", elToClose.tagName());
    }

    @Test
    public void testLast() {
        Parser parser = new Parser("", "http://example.com", false);
        parser.parseStartTag();
        parser.parseStartTag();
        Element last = parser.last();
        assertNotNull(last);
        assertEquals("head", last.tagName());
    }

}