package org.jsoup.parser;

import org.jsoup.nodes.*;
import org.apache.commons.lang.Validate;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ParserTest {

    private Parser parser;

    @Before
    public void setUp() {
        parser = new Parser("<html><body><h1>Title</h1><p>Paragraph</p></body></html>", "");
    }

    @Test
    public void testParse() {
        Document doc = parser.parse();
        
        assertNotNull(doc);
        assertEquals("html", doc.nodeName());
        assertEquals("body", doc.child(0).nodeName());
        
        Element body = doc.body();
        assertEquals("body", body.nodeName());
        assertEquals("h1", body.child(0).nodeName());
        assertEquals("Title", body.child(0).text());
        assertEquals("p", body.child(1).nodeName());
        assertEquals("Paragraph", body.child(1).text());
    }

    @Test
    public void testParseComment() {
        parser.parseComment();
        
        Element body = parser.last();
        assertEquals(1, body.childNodeSize());
        assertTrue(body.childNode(0) instanceof Comment);
    }

    @Test
    public void testParseXmlDecl() {
        parser.parseXmlDecl();
        
        Element body = parser.last();
        assertEquals(1, body.childNodeSize());
        assertTrue(body.childNode(0) instanceof XmlDeclaration);
    }

    @Test
    public void testParseEndTag() {
        Element h1 = new Element(Tag.valueOf("h1"), "");
        parser.stack.add(h1);
        
        parser.parseEndTag();
        
        assertEquals(0, parser.stack.size());
        assertEquals("body", parser.last().nodeName());
    }

    @Test
    public void testParseStartTag() {
        parser.parseStartTag();
        
        Element body = parser.last();
        assertEquals(1, body.childNodeSize());
        assertTrue(body.childNode(0) instanceof Element);
    }

    @Test
    public void testParseTextNode() {
        parser.parseTextNode();
        
        Element body = parser.last();
        assertEquals(1, body.childNodeSize());
        assertTrue(body.childNode(0) instanceof TextNode);
    }

    @Test
    public void testParseCdata() {
        parser.parseCdata();
        
        Element body = parser.last();
        assertEquals(1, body.childNodeSize());
        assertTrue(body.childNode(0) instanceof TextNode);
    }

    @Test
    public void testAddChildToParent() {
        Element parent = new Element(Tag.valueOf("body"), "");
        Element child = new Element(Tag.valueOf("h1"), "");
        
        Element result = parser.addChildToParent(child, false);
        
        assertEquals(parent, result);
        assertEquals(1, parent.childNodeSize());
        assertEquals("h1", parent.childNode(0).nodeName());
    }

    @Test
    public void testStackHasValidParent() {
        Element root = new Element(Tag.valueOf("html"), "");
        parser.stack.add(root);
        parser.stack.add(new Element(Tag.valueOf("body"), ""));
        
        assertTrue(parser.stackHasValidParent(Tag.valueOf("h1")));
        assertFalse(parser.stackHasValidParent(Tag.valueOf("title")));
    }

    @Test
    public void testPopStackToSuitableContainer() {
        Element parent = new Element(Tag.valueOf("body"), "");
        parser.stack.add(parent);
        parser.stack.add(new Element(Tag.valueOf("h1"), ""));
        
        Element result = parser.popStackToSuitableContainer(Tag.valueOf("p"));
        
        assertEquals(1, parser.stack.size());
        assertEquals(parent, result);
        assertEquals("h1", parser.last().nodeName());
    }

    @Test
    public void testPopStackToClose() {
        Element parent = new Element(Tag.valueOf("body"), "");
        Element h1 = new Element(Tag.valueOf("h1"), "");
        parser.stack.add(parent);
        parser.stack.add(h1);
        
        parser.popStackToClose(Tag.valueOf("h1"));
        
        assertEquals(1, parser.stack.size());
        assertEquals(parent, parser.stack.getLast());
    }

    @Test
    public void testLast() {
        Element parent = new Element(Tag.valueOf("body"), "");
        parser.stack.add(parent);
        
        Element result = parser.last();
        
        assertEquals(parent, result);
    }
}