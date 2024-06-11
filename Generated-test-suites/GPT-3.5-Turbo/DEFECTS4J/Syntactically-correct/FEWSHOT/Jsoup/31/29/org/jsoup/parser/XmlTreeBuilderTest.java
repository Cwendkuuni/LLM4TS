package org.jsoup.parser;

import org.jsoup.nodes.*;
import org.jsoup.parser.Token.*;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder treeBuilder;

    @Before
    public void setUp() {
        treeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testInitialiseParse() {
        // Test input, baseUri, and errors initialization
        String input = "<root><child1></child1><child2></child2></root>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();

        treeBuilder.initialiseParse(input, baseUri, errors);

        // Verify stack size after initialization
        assertEquals(1, treeBuilder.stack.size());

        // Verify document is placed on the stack
        assertSame(treeBuilder.doc, treeBuilder.stack.getFirst());

        // Verify errors object is passed correctly
        assertSame(errors, treeBuilder.errors);
    }

    @Test
    public void testProcessStartTag() {
        // Test insert method call for StartTag token
        StartTag startTag = new StartTag("div");
        Element result = treeBuilder.insert(startTag);

        // Verify element is inserted correctly
        assertNotNull(result);
        assertEquals("div", result.nodeName());

        // Verify stack size after insertion
        assertEquals(2, treeBuilder.stack.size());
        assertSame(result, treeBuilder.stack.getLast());
    }

    @Test
    public void testProcessEndTag() {
        // Setup stack with two elements
        Element element1 = new Element("div");
        Element element2 = new Element("span");
        treeBuilder.stack.add(element1);
        treeBuilder.stack.add(element2);

        // Test popStackToClose method call for EndTag token
        EndTag endTag = new EndTag("div");
        treeBuilder.popStackToClose(endTag);

        // Verify stack is correctly updated
        assertEquals(1, treeBuilder.stack.size());
        assertSame(element2, treeBuilder.stack.getLast());
    }

    @Test
    public void testProcessComment() {
        // Test insert method call for Comment token
        Comment comment = new Comment("This is a comment");
        treeBuilder.insert(comment);

        // Verify comment is inserted correctly
        assertEquals(1, treeBuilder.doc.childNodeSize());
        assertTrue(treeBuilder.doc.childNode(0) instanceof Comment);
        assertEquals("This is a comment", ((Comment) treeBuilder.doc.childNode(0)).getData());
    }

    @Test
    public void testProcessCharacter() {
        // Test insert method call for Character token
        Character character = new Character("Character data");
        treeBuilder.insert(character);

        // Verify character is inserted correctly
        assertEquals(1, treeBuilder.doc.childNodeSize());
        assertTrue(treeBuilder.doc.childNode(0) instanceof TextNode);
        assertEquals("Character data", ((TextNode) treeBuilder.doc.childNode(0)).getWholeText());
    }

    @Test
    public void testProcessDoctype() {
        // Test insert method call for Doctype token
        Doctype doctype = new Doctype("html", "", "", "");
        treeBuilder.insert(doctype);

        // Verify doctype is inserted correctly
        assertEquals(1, treeBuilder.doc.childNodeSize());
        assertTrue(treeBuilder.doc.childNode(0) instanceof DocumentType);
        assertEquals("html", ((DocumentType) treeBuilder.doc.childNode(0)).getName());
    }
}