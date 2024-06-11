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

    @Test
    public void testInitialiseParse() {
        String input = "<root></root>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);

        assertTrue(xmlTreeBuilder.stack.contains(xmlTreeBuilder.doc));
    }

    @Test
    public void testProcess_StartTag() {
        Token token = new Token.StartTag("div");
        assertTrue(xmlTreeBuilder.process(token));

        Element element = xmlTreeBuilder.currentElement();
        assertNotNull(element);
        assertEquals("div", element.tagName());
        assertEquals(0, element.childNodeSize());
        assertFalse(token.isSelfClosing());
    }

    @Test
    public void testProcess_EndTag() {
        Token token = new Token.EndTag("div");
        assertTrue(xmlTreeBuilder.process(token));

        Element element = xmlTreeBuilder.currentElement();
        assertNull(element);
    }

    @Test
    public void testProcess_Comment() {
        Token token = new Token.Comment("Test comment");
        assertTrue(xmlTreeBuilder.process(token));

        Element element = xmlTreeBuilder.currentElement();
        assertNotNull(element);
        assertEquals("Test comment", element.childNode(0).outerHtml());
    }

    @Test
    public void testProcess_Character() {
        Token token = new Token.Character("Test character");
        assertTrue(xmlTreeBuilder.process(token));

        Element element = xmlTreeBuilder.currentElement();
        assertNotNull(element);
        assertEquals("Test character", element.childNode(0).outerHtml());
    }

    @Test
    public void testProcess_Doctype() {
        Token token = new Token.Doctype("html");
        assertTrue(xmlTreeBuilder.process(token));

        Element element = xmlTreeBuilder.currentElement();
        assertNotNull(element);
        assertEquals("<!DOCTYPE html>", element.childNode(0).outerHtml());
    }

    @Test
    public void testInsert_StartTag() {
        Token.StartTag startTag = new Token.StartTag("a");
        Element element = xmlTreeBuilder.insert(startTag);

        assertNotNull(element);
        assertEquals("a", element.tagName());
        assertEquals(0, element.childNodeSize());
        assertFalse(startTag.isSelfClosing());
        assertTrue(xmlTreeBuilder.stack.contains(element));
    }

    @Test
    public void testInsert_StartTagSelfClosing() {
        Token.StartTag startTag = new Token.StartTag("img", true);
        Element element = xmlTreeBuilder.insert(startTag);

        assertNotNull(element);
        assertEquals("img", element.tagName());
        assertEquals(0, element.childNodeSize());
        assertTrue(startTag.isSelfClosing());
        assertTrue(xmlTreeBuilder.stack.contains(xmlTreeBuilder.doc));
    }

    @Test
    public void testInsert_Comment() {
        Token.Comment commentToken = new Token.Comment("Test comment");
        xmlTreeBuilder.insert(commentToken);

        Element element = xmlTreeBuilder.currentElement();
        assertNotNull(element);
        assertEquals("<!--Test comment-->", element.childNode(0).outerHtml());
    }

    @Test
    public void testInsert_Character() {
        Token.Character characterToken = new Token.Character("Test character");
        xmlTreeBuilder.insert(characterToken);

        Element element = xmlTreeBuilder.currentElement();
        assertNotNull(element);
        assertEquals("Test character", element.childNode(0).outerHtml());
    }

    @Test
    public void testInsert_Doctype() {
        Token.Doctype doctype = new Token.Doctype("html");
        xmlTreeBuilder.insert(doctype);

        Element element = xmlTreeBuilder.currentElement();
        assertNotNull(element);
        assertEquals("<!DOCTYPE html>", element.childNode(0).outerHtml());
    }

    @Test
    public void testPopStackToClose_Found() {
        Token.EndTag endTag = new Token.EndTag("div");
        
        xmlTreeBuilder.stack.push(new Element("div"));
        xmlTreeBuilder.stack.push(new Element("span"));
        xmlTreeBuilder.stack.push(new Element("div"));
        
        xmlTreeBuilder.popStackToClose(endTag);
        
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals("span", xmlTreeBuilder.stack.peek().tagName());
    }

    @Test
    public void testPopStackToClose_NotFound() {
        Token.EndTag endTag = new Token.EndTag("div");

        xmlTreeBuilder.stack.push(new Element("span"));
        xmlTreeBuilder.stack.push(new Element("h1"));

        xmlTreeBuilder.popStackToClose(endTag);

        assertEquals(2, xmlTreeBuilder.stack.size());
    }

}