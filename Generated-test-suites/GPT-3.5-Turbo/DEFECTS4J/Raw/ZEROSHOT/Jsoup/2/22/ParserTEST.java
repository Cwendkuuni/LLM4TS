package org.jsoup.parser;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ParserTest {

    private Parser parser;

    @Before
    public void setUp() {
        parser = new Parser("<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>", "http://example.com", false);
    }

    @Test
    public void testParse() {
        Document doc = parser.parse();
        assertNotNull(doc);

        Element html = doc.getElementsByTag("html").first();
        assertNotNull(html);

        Element head = doc.getElementsByTag("head").first();
        assertNotNull(head);

        Element title = doc.getElementsByTag("title").first();
        assertNotNull(title);
        assertEquals("Test", title.text());

        Element body = doc.getElementsByTag("body").first();
        assertNotNull(body);

        Element h1 = doc.getElementsByTag("h1").first();
        assertNotNull(h1);
        assertEquals("Hello World", h1.text());
    }

    @Test
    public void testParseComment() {
        parser.parseComment();
        Document doc = parser.parse();
        assertNotNull(doc);

        Comment comment = (Comment) doc.childNode(0);
        assertNotNull(comment);
        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testParseXmlDecl() {
        parser.parseXmlDecl();
        Document doc = parser.parse();
        assertNotNull(doc);

        XmlDeclaration decl = (XmlDeclaration) doc.childNode(0);
        assertNotNull(decl);
        assertEquals("version=\"1.0\" encoding=\"UTF-8\"", decl.getData());
    }

    @Test
    public void testParseEndTag() {
        parser.parseEndTag();
        Document doc = parser.parse();
        assertNotNull(doc);

        Element body = doc.getElementsByTag("body").first();
        assertNull(body);
    }

    @Test
    public void testParseStartTag() {
        parser.parseStartTag();
        Document doc = parser.parse();
        assertNotNull(doc);

        Element h2 = doc.getElementsByTag("h2").first();
        assertNotNull(h2);
        assertEquals("Header 2", h2.text());
    }

    @Test
    public void testParseTextNode() {
        parser.parseTextNode();
        Document doc = parser.parse();
        assertNotNull(doc);

        TextNode textNode = (TextNode) doc.childNode(0);
        assertNotNull(textNode);
        assertEquals("This is a text node", textNode.getWholeText());
    }

    @Test
    public void testParseCdata() {
        parser.parseCdata();
        Document doc = parser.parse();
        assertNotNull(doc);

        TextNode cdata = (TextNode) doc.childNode(0);
        assertNotNull(cdata);
        assertEquals("<![CDATA[This is a CDATA section]]>", cdata.getWholeText());
    }

    @Test
    public void testAddChildToParent() {
        Element child = new Element(Tag.valueOf("p"), "http://example.com");
        parser.addChildToParent(child, false);
        Document doc = parser.parse();
        assertNotNull(doc);

        Element parent = doc.getElementsByTag("body").first();
        assertNotNull(parent);
        assertEquals(child, parent.child(0));
    }

    @Test
    public void testStackHasValidParent() {
        parser.parseStartTag();
        boolean hasValidParent = parser.stackHasValidParent(Tag.valueOf("h2"));
        assertTrue(hasValidParent);
    }

    @Test
    public void testPopStackToSuitableContainer() {
        Element parent = new Element(Tag.valueOf("div"), "http://example.com");
        parser.addChildToParent(parent, false);
        Element child = new Element(Tag.valueOf("p"), "http://example.com");
        parser.addChildToParent(child, false);
        Element container = new Element(Tag.valueOf("div"), "http://example.com");
        parser.addChildToParent(container, false);
        Element result = parser.popStackToSuitableContainer(Tag.valueOf("p"));
        assertEquals(result, parent);
    }

    @Test
    public void testPopStackToClose() {
        Element parent = new Element(Tag.valueOf("div"), "http://example.com");
        parser.addChildToParent(parent, false);
        Element child = new Element(Tag.valueOf("p"), "http://example.com");
        parser.addChildToParent(child, false);
        Element container = new Element(Tag.valueOf("div"), "http://example.com");
        parser.addChildToParent(container, false);
        Element result = parser.popStackToClose(Tag.valueOf("p"));
        assertEquals(result, child);
    }

    @Test
    public void testLast() {
        Element parent = new Element(Tag.valueOf("div"), "http://example.com");
        parser.addChildToParent(parent, false);
        Element result = parser.last();
        assertEquals(result, parent);
    }
}