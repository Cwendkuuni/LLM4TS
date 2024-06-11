package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.Comment;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder treeBuilder;

    @Before
    public void setUp() {
        treeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testInitialiseParse() {
        String input = "<root></root>";
        String baseUri = "http://www.example.com";
        ParseErrorList errors = new ParseErrorList();
        
        treeBuilder.initialiseParse(input, baseUri, errors);
        
        assertTrue(treeBuilder.stack.size() == 1);
        assertTrue(treeBuilder.stack.get(0) instanceof Document);
    }

    @Test
    public void testProcessStartTag() {
        String input = "<tag></tag>";
        StartTag startTag = new StartTag(input);
        
        assertTrue(treeBuilder.stack.isEmpty());
        
        treeBuilder.process(startTag);
        
        assertTrue(treeBuilder.stack.size() == 1);
        assertTrue(treeBuilder.stack.get(0).nodeName().equals("tag"));
    }

    @Test
    public void testProcessEndTag() {
        String input = "</tag>";
        EndTag endTag = new EndTag(input);
        
        Element element = new Element("tag");
        treeBuilder.stack.add(element);
        
        assertTrue(treeBuilder.stack.size() == 1);
        
        treeBuilder.process(endTag);
        
        assertTrue(treeBuilder.stack.isEmpty());
    }

    @Test
    public void testProcessComment() {
        String input = "<!-- Comment -->";
        Comment comment = new Comment(input);
        
        assertTrue(treeBuilder.stack.isEmpty());
        
        treeBuilder.process(comment);
        
        assertTrue(treeBuilder.stack.size() == 1);
        assertTrue(treeBuilder.stack.get(0) instanceof Comment);
    }

    @Test
    public void testProcessCharacter() {
        String input = "Text";
        Character character = new Character(input);
        
        assertTrue(treeBuilder.stack.isEmpty());
        
        treeBuilder.process(character);
        
        assertTrue(treeBuilder.stack.size() == 1);
        assertTrue(treeBuilder.stack.get(0) instanceof TextNode);
    }

    @Test
    public void testProcessDoctype() {
        String input = "<!DOCTYPE html>";
        Doctype doctype = new Doctype(input);
        
        assertTrue(treeBuilder.stack.isEmpty());
        
        treeBuilder.process(doctype);
        
        assertTrue(treeBuilder.stack.size() == 1);
        assertTrue(treeBuilder.stack.get(0) instanceof DocumentType);
    }

    @Test
    public void testInsertNode() {
        String input = "<tag>Text</tag>";
        StartTag startTag = new StartTag(input);
        treeBuilder.process(startTag);
        
        TextNode node = new TextNode("Text");
        
        treeBuilder.insertNode(node);
        
        Element element = treeBuilder.stack.get(0);
        assertTrue(element.childNode(0).equals(node));
    }

    @Test
    public void testInsertStartTag() {
        String input = "<tag></tag>";
        StartTag startTag = new StartTag(input);
        
        assertTrue(treeBuilder.stack.isEmpty());
        
        Element element = treeBuilder.insert(startTag);
        
        assertTrue(treeBuilder.stack.size() == 1);
        assertTrue(treeBuilder.stack.get(0).nodeName().equals("tag"));
        assertTrue(element.nodeName().equals("tag"));
    }

    @Test
    public void testInsertSelfClosingStartTag() {
        String input = "<tag/>";
        StartTag startTag = new StartTag(input);
        
        assertTrue(treeBuilder.stack.isEmpty());
        
        Element element = treeBuilder.insert(startTag);
        
        assertTrue(treeBuilder.stack.isEmpty());
        assertTrue(element.nodeName().equals("tag"));
        assertTrue(element.selfClosing());
    }

    @Test
    public void testInsertComment() {
        String input = "<!-- Comment -->";
        Comment comment = new Comment(input);
        
        assertTrue(treeBuilder.stack.isEmpty());
        
        treeBuilder.insert(comment);
        
        assertTrue(treeBuilder.stack.size() == 1);
        assertTrue(treeBuilder.stack.get(0) instanceof Comment);
    }

    @Test
    public void testInsertCharacter() {
        String input = "Text";
        Character character = new Character(input);
        
        assertTrue(treeBuilder.stack.isEmpty());
        
        treeBuilder.insert(character);
        
        assertTrue(treeBuilder.stack.size() == 1);
        assertTrue(treeBuilder.stack.get(0) instanceof TextNode);
    }

    @Test
    public void testInsertDoctype() {
        String input = "<!DOCTYPE html>";
        Doctype doctype = new Doctype(input);
        
        assertTrue(treeBuilder.stack.isEmpty());
        
        treeBuilder.insert(doctype);
        
        assertTrue(treeBuilder.stack.size() == 1);
        assertTrue(treeBuilder.stack.get(0) instanceof DocumentType);
    }

    @Test
    public void testPopStackToClose() {
        String input = "</tag>";
        EndTag endTag = new EndTag(input);
        
        Element element = new Element("tag");
        treeBuilder.stack.add(element);
        
        assertTrue(treeBuilder.stack.size() == 1);
        
        treeBuilder.popStackToClose(endTag);
        
        assertTrue(treeBuilder.stack.isEmpty());
    }

    @Test
    public void testPopStackToCloseNotFound() {
        String input = "</tag>";
        EndTag endTag = new EndTag(input);
        
        Element element = new Element("otherTag");
        treeBuilder.stack.add(element);
        
        assertTrue(treeBuilder.stack.size() == 1);
        
        treeBuilder.popStackToClose(endTag);
        
        assertTrue(treeBuilder.stack.size() == 1);
    }

    @Test
    public void testPopStackToCloseMultiple() {
        String input = "</tag>";
        EndTag endTag = new EndTag(input);
        
        Element element1 = new Element("tag");
        Element element2 = new Element("tag");
        
        treeBuilder.stack.add(element1);
        treeBuilder.stack.add(element2);
        
        assertTrue(treeBuilder.stack.size() == 2);
        
        treeBuilder.popStackToClose(endTag);
        
        assertTrue(treeBuilder.stack.isEmpty());
    }
}