package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testInitialiseParse() {
        String input = "<root><child></child></root>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);
        
        Document doc = xmlTreeBuilder.getDocument();
        Element root = doc.getElementsByTag("root").first();
        
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("root", root.nodeName());
        assertEquals(1, root.children().size());
    }

    @Test
    public void testProcessStartTag() {
        Token startTagToken = new Token.StartTag("<div>");
        
        xmlTreeBuilder.process(startTagToken);
        
        Element divElement = xmlTreeBuilder.currentElement();
        
        assertEquals("div", divElement.nodeName());
    }

    @Test
    public void testInsertComment() {
        Token.Comment commentToken = new Token.Comment("This is a comment");
        
        xmlTreeBuilder.insert(commentToken);
        
        Node insertedNode = xmlTreeBuilder.currentElement().childNode(0);
        
        assertEquals("This is a comment", ((TextNode) insertedNode).getWholeText());
    }

    @Test
    public void testInsertCharacter() {
        Token.Character characterToken = new Token.Character("Some text");
        
        xmlTreeBuilder.insert(characterToken);
        
        Node insertedNode = xmlTreeBuilder.currentElement().childNode(0);
        
        assertEquals("Some text", ((TextNode) insertedNode).getWholeText());
    }

    @Test
    public void testInsertDoctype() {
        Token.Doctype doctypeToken = new Token.Doctype("html", "", "");
        
        xmlTreeBuilder.insert(doctypeToken);
        
        Node insertedNode = xmlTreeBuilder.currentElement().childNode(0);
        
        assertEquals("<!DOCTYPE html>", insertedNode.outerHtml());
    }

    @Test
    public void testPopStackToClose() {
        Element divElement = new Element("div");
        xmlTreeBuilder.currentElement().appendChild(divElement);
        
        Token.EndTag endTagToken = new Token.EndTag("div");
        
        xmlTreeBuilder.popStackToClose(endTagToken);
        
        assertEquals(0, xmlTreeBuilder.currentElement().children().size());
    }
}