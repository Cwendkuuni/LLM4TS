package org.jsoup.parser;


import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Test;

import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ParserTest {

    @Test
    public void parseTest() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello</h1></body></html>";
        String baseUri = "http://www.example.com";
        Document doc = Parser.parse(html, baseUri);

        assertNotNull(doc);
        assertEquals("Test", doc.title());
        assertNotNull(doc.body());
        assertEquals("Hello", doc.body().select("h1").text());
    }

    @Test
    public void parseBodyFragmentTest() {
        String bodyHtml = "<h1>Hello</h1>";
        String baseUri = "http://www.example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

        assertNotNull(doc);
        assertNull(doc.title());
        assertNotNull(doc.body());
        assertEquals("Hello", doc.body().select("h1").text());
    }

    @Test
    public void parseBodyFragmentRelaxedTest() {
        String bodyHtml = "<h1>Hello</h1>";
        String baseUri = "http://www.example.com";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);

        assertNotNull(doc);
        assertNull(doc.title());
        assertNotNull(doc.body());
        assertEquals("Hello", doc.body().select("h1").text());
    }

    @Test
    public void parseCommentTest() {
        Parser parser = new Parser("", "", false);
        parser.parseComment();

        // Verify that a Comment node is added to the document
        Document doc = parser.doc;
        assertNotNull(doc);
        assertNotNull(doc.childNodes());
        assertEquals(1, doc.childNodes().size());
        assertTrue(doc.childNode(0) instanceof Comment);
    }

    @Test
    public void parseXmlDeclTest() {
        Parser parser = new Parser("", "", false);
        parser.parseXmlDecl();

        // Verify that an XmlDeclaration node is added to the document
        Document doc = parser.doc;
        assertNotNull(doc);
        assertNotNull(doc.childNodes());
        assertEquals(1, doc.childNodes().size());
        assertTrue(doc.childNode(0) instanceof XmlDeclaration);
    }

    @Test
    public void parseEndTagTest() {
        Parser parser = new Parser("", "", false);
        parser.parseEndTag();

        // Verify that an Element node is removed from the stack
        LinkedList<Element> stack = parser.stack;
        assertNotNull(stack);
        assertEquals(0, stack.size());
    }

    @Test
    public void parseStartTagTest() {
        Parser parser = new Parser("", "", false);
        parser.parseStartTag();

        // Verify that an Element node is added to the stack
        LinkedList<Element> stack = parser.stack;
        assertNotNull(stack);
        assertEquals(1, stack.size());
        assertTrue(stack.get(0) instanceof Element);
    }

    @Test
    public void parseTextNodeTest() {
        Parser parser = new Parser("", "", false);
        parser.parseTextNode();

        // Verify that a TextNode is added to the stack
        LinkedList<Element> stack = parser.stack;
        assertNotNull(stack);
        assertEquals(1, stack.size());
        assertTrue(stack.get(0) instanceof TextNode);
    }

    @Test
    public void parseCdataTest() {
        Parser parser = new Parser("", "", false);
        parser.parseCdata();

        // Verify that a TextNode is added to the stack
        LinkedList<Element> stack = parser.stack;
        assertNotNull(stack);
        assertEquals(1, stack.size());
        assertTrue(stack.get(0) instanceof TextNode);
    }

    @Test
    public void addChildToParentTest() {
        Parser parser = new Parser("", "", false);
        Element child = new Element(Tag.valueOf("div"), "");
        boolean isEmptyElement = false;
        Element parent = parser.addChildToParent(child, isEmptyElement);

        // Verify that the child element is added to the parent element and returned as the parent
        assertNotNull(parent);
        assertEquals(1, parent.childNodeSize());
        assertEquals(child, parent.childNode(0));
    }

    @Test
    public void stackHasValidParentTest() {
        Parser parser = new Parser("", "", false);
        LinkedList<Element> stack = parser.stack;
        stack.add(new Element(Tag.valueOf("html"), ""));
        stack.add(new Element(Tag.valueOf("body"), ""));

        // Verify that the stack has a valid parent for the given child tag
        Tag childTag = Tag.valueOf("h1");
        boolean validParent = parser.stackHasValidParent(childTag);
        assertTrue(validParent);
    }

    @Test
    public void popStackToSuitableContainerTest() {
        Parser parser = new Parser("", "", false);
        LinkedList<Element> stack = parser.stack;
        stack.add(new Element(Tag.valueOf("div"), ""));

        // Verify that the stack is popped to a suitable container for the given child tag
        Tag tag = Tag.valueOf("h1");
        Element parent = parser.popStackToSuitableContainer(tag);
        assertNotNull(parent);
        assertEquals(0, stack.size());
    }

    @Test
    public void popStackToCloseTest() {
        Parser parser = new Parser("", "", false);
        LinkedList<Element> stack = parser.stack;
        stack.add(new Element(Tag.valueOf("div"), ""));

        // Verify that the stack is popped to close the given tag
        Tag tag = Tag.valueOf("div");
        Element elToClose = parser.popStackToClose(tag);
        assertNotNull(elToClose);
        assertEquals(0, stack.size());
    }

    @Test
    public void lastTest() {
        Parser parser = new Parser("", "", false);
        LinkedList<Element> stack = parser.stack;
        stack.add(new Element(Tag.valueOf("body"), ""));

        // Verify that the last element in the stack is returned
        Element lastElement = parser.last();
        assertNotNull(lastElement);
        assertEquals(stack.getLast(), lastElement);
    }
}