package org.jsoup.parser;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.*;

import org.junit.Test;

import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ParserTest {
    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        String baseUri = "https://www.example.com";
        Document expectedResult = new Document(baseUri);
        Element htmlElement = new Element(Tag.valueOf("html"), baseUri);
        Element headElement = new Element(Tag.valueOf("head"), baseUri);
        Element titleElement = new Element(Tag.valueOf("title"), baseUri);
        TextNode titleTextNode = new TextNode("Test", baseUri);
        Element bodyElement = new Element(Tag.valueOf("body"), baseUri);
        Element h1Element = new Element(Tag.valueOf("h1"), baseUri);
        TextNode h1TextNode = new TextNode("Hello World!", baseUri);

        titleElement.appendChild(titleTextNode);
        headElement.appendChild(titleElement);
        h1Element.appendChild(h1TextNode);
        bodyElement.appendChild(h1Element);
        htmlElement.appendChild(headElement);
        htmlElement.appendChild(bodyElement);
        expectedResult.appendChild(htmlElement);

        Document actualResult = Parser.parse(html, baseUri);
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World!</h1>";
        String baseUri = "https://www.example.com";
        Document expectedResult = Document.createShell(baseUri);
        Element bodyElement = new Element(Tag.valueOf("body"), baseUri);
        Element h1Element = new Element(Tag.valueOf("h1"), baseUri);
        TextNode h1TextNode = new TextNode("Hello World!", baseUri);
        
        h1Element.appendChild(h1TextNode);
        bodyElement.appendChild(h1Element);
        expectedResult.body().appendChild(bodyElement);
        
        Document actualResult = Parser.parseBodyFragment(bodyHtml, baseUri);
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testParseComment() {
        LinkedList<Element> stack = new LinkedList<>();
        stack.add(new Element(Tag.valueOf("body"), "https://www.example.com"));
        String baseUri = "https://www.example.com";
        String html = "<!-- This is a comment -->";
        Comment expectedResult = new Comment(" This is a comment ", baseUri);

        Parser parser = new Parser(html, baseUri, false);
        parser.parseComment();
        Comment actualResult = (Comment) stack.getLast().childNode(0);
        
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testParseXmlDecl() {
        LinkedList<Element> stack = new LinkedList<>();
        stack.add(new Element(Tag.valueOf("body"), "https://www.example.com"));
        String baseUri = "https://www.example.com";
        String html = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>";
        XmlDeclaration expectedResult = new XmlDeclaration("xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"", baseUri, true);

        Parser parser = new Parser(html, baseUri, false);
        parser.parseXmlDecl();
        XmlDeclaration actualResult = (XmlDeclaration) stack.getLast().childNode(0);
        
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testParseEndTag() {
        LinkedList<Element> stack = new LinkedList<>();
        stack.add(new Element(Tag.valueOf("body"), "https://www.example.com"));
        stack.add(new Element(Tag.valueOf("h1"), "https://www.example.com"));
        stack.add(new Element(Tag.valueOf("div"), "https://www.example.com"));
        stack.add(new Element(Tag.valueOf("html"), "https://www.example.com"));
        String tagName = "div";
        
        Parser parser = new Parser("", "https://www.example.com", false);
        Element actualResult = parser.popStackToClose(Tag.valueOf(tagName));
        assertEquals(tagName, actualResult.tagName());
        assertEquals(1, stack.size());
    }

    @Test
    public void testParseStartTag() {
        LinkedList<Element> stack = new LinkedList<>();
        stack.add(new Element(Tag.valueOf("body"), "https://www.example.com"));
        stack.add(new Element(Tag.valueOf("h1"), "https://www.example.com"));
        stack.add(new Element(Tag.valueOf("html"), "https://www.example.com"));
        String tagName = "div";
        Attributes attributes = new Attributes();
        attributes.put("class", "test");
        Element child = new Element(Tag.valueOf(tagName), "https://www.example.com", attributes);
        stack.getLast().appendChild(child);

        Parser parser = new Parser("<div class=\"test\">", "https://www.example.com", false);
        boolean isEmptyElement = false;
        Element actualResult = parser.addChildToParent(child, isEmptyElement);
        assertEquals(tagName, actualResult.tagName());
    }
}