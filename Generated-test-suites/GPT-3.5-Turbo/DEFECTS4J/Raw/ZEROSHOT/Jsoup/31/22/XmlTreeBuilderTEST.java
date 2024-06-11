
package org.jsoup.parser;

import org.jsoup.helper.Validate;
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
        // Test input
        String input = "<html><head><title>Test</title></head><body>Hello, world!</body></html>";
        String baseUri = "https://www.example.com";
        ParseErrorList errors = new ParseErrorList();

        // Test method
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);

        // Assertions
        assertNotNull(xmlTreeBuilder.stack);
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals(xmlTreeBuilder.doc, xmlTreeBuilder.stack.get(0));
    }

    @Test
    public void testProcessStartTag() {
        // Test input
        String tagName = "div";
        Attributes attributes = new Attributes();
        attributes.put("class", "container");

        Token.StartTag startTag = new Token.StartTag(tagName, attributes);

        // Test method
        Element element = xmlTreeBuilder.insert(startTag);

        // Assertions
        assertNotNull(element);
        assertEquals(tagName, element.nodeName());
        assertEquals(attributes, element.attributes());
    }

    @Test
    public void testProcessComment() {
        // Test input
        String data = "This is a comment";
        Token.Comment commentToken = new Token.Comment(data);

        // Test method
        xmlTreeBuilder.insert(commentToken);

        // Assertions
        assertNotNull(xmlTreeBuilder.currentElement().childNode(0));
        assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof Comment);
        assertEquals(data, ((Comment) xmlTreeBuilder.currentElement().childNode(0)).getData());
    }

    @Test
    public void testProcessCharacter() {
        // Test input
        String data = "Hello, world!";
        Token.Character characterToken = new Token.Character(data);

        // Test method
        xmlTreeBuilder.insert(characterToken);

        // Assertions
        assertNotNull(xmlTreeBuilder.currentElement().childNode(0));
        assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof TextNode);
        assertEquals(data, ((TextNode) xmlTreeBuilder.currentElement().childNode(0)).getWholeText());
    }

    @Test
    public void testProcessDoctype() {
        // Test input
        String name = "html";
        String publicIdentifier = "-//W3C//DTD XHTML 1.0 Strict//EN";
        String systemIdentifier = "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd";
        Token.Doctype doctype = new Token.Doctype(name, publicIdentifier, systemIdentifier);

        // Test method
        xmlTreeBuilder.insert(doctype);

        // Assertions
        assertNotNull(xmlTreeBuilder.currentElement().childNode(0));
        assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof DocumentType);
        assertEquals(name, ((DocumentType) xmlTreeBuilder.currentElement().childNode(0)).getName());
        assertEquals(publicIdentifier, ((DocumentType) xmlTreeBuilder.currentElement().childNode(0)).getPublicIdentifier());
        assertEquals(systemIdentifier, ((DocumentType) xmlTreeBuilder.currentElement().childNode(0)).getSystemIdentifier());
    }

    @Test
    public void testPopStackToClose() {
        // Test input
        String elName = "div";
        Token.EndTag endTag = new Token.EndTag(elName);

        // Set up stack
        Element element1 = new Element(Tag.valueOf("h1"), "", new Attributes());
        Element element2 = new Element(Tag.valueOf("div"), "", new Attributes());
        Element element3 = new Element(Tag.valueOf("p"), "", new Attributes());

        xmlTreeBuilder.stack.add(element1);
        xmlTreeBuilder.stack.add(element2);
        xmlTreeBuilder.stack.add(element3);

        // Test method
        xmlTreeBuilder.popStackToClose(endTag);

        // Assertions
        assertFalse(xmlTreeBuilder.stack.contains(element2));
        assertTrue(xmlTreeBuilder.stack.contains(element1));
        assertTrue(xmlTreeBuilder.stack.contains(element3));
    }

}
