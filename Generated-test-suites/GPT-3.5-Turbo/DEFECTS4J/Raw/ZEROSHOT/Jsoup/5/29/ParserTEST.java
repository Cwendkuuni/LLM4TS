package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ParserTest {

    private Parser parser;

    @Before
    public void setUp() {
        parser = new Parser("<html><head><title>Test</title></head><body><h1>Hello</h1></body></html>", "https://www.example.com", false);
    }

    @Test
    public void testParse() {
        Document document = parser.parse();

        assertNotNull(document);
        assertEquals("Test", document.title());
        assertEquals("Hello", document.body().child(0).text());
    }

    @Test
    public void testParseBodyFragment() {
        Document document = Parser.parseBodyFragment("<h1>Hello</h1>", "https://www.example.com");

        assertNotNull(document);
        assertEquals("Hello", document.body().child(0).text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        Document document = Parser.parseBodyFragmentRelaxed("<h1>Hello</h1>", "https://www.example.com");

        assertNotNull(document);
        assertEquals("Hello", document.body().child(0).text());
    }

    @Test
    public void testParseComment() {
        parser.parseComment();

        Comment comment = (Comment) parser.last().childNode(0);

        assertNotNull(comment);
        assertEquals("Test comment", comment.getData());
    }

    @Test
    public void testParseXmlDecl() {
        parser.parseXmlDecl();

        XmlDeclaration declaration = (XmlDeclaration) parser.last().childNode(0);

        assertNotNull(declaration);
        assertEquals("version", declaration.attributes().asList().get(0).getKey());
        assertEquals("1.0", declaration.attributes().asList().get(0).getValue());
    }

    @Test
    public void testParseEndTag() {
        parser.parseStartTag();
        parser.parseEndTag();

        assertEquals(0, parser.stack.size());
    }

    @Test
    public void testParseStartTag() {
        parser.parseStartTag();

        Element element = parser.last();

        assertNotNull(element);
        assertEquals("h1", element.tagName());
    }

    @Test
    public void testParseTextNode() {
        parser.parseTextNode();

        TextNode textNode = (TextNode) parser.last().childNode(0);

        assertNotNull(textNode);
        assertEquals("Hello", textNode.getWholeText());
    }

    @Test
    public void testParseCdata() {
        parser.parseCdata();

        TextNode textNode = (TextNode) parser.last().childNode(0);

        assertNotNull(textNode);
        assertEquals("Test data", textNode.getWholeText());
    }

    @Test
    public void testParseAttribute() {
        Attribute attribute = parser.parseAttribute();

        assertNotNull(attribute);
        assertEquals("class", attribute.getKey());
        assertEquals("test", attribute.getValue());
    }

    @Test
    public void testAddChildToParent() {
        Element child = new Element(Tag.valueOf("h1"), "https://www.example.com");

        Element parent = parser.addChildToParent(child, false);

        assertNotNull(parent);
        assertEquals("h1", parent.tagName());
        assertEquals("Hello", parent.text());
    }

    @Test
    public void testStackHasValidParent() {
        parser.stack.add(parser.last());
        boolean result = parser.stackHasValidParent(Tag.valueOf("h2"));

        assertTrue(result);
    }

    @Test
    public void testPopStackToSuitableContainer() {
        Element element = new Element(Tag.valueOf("body"), "https://www.example.com");
        parser.stack.add(element);
        Element container = parser.popStackToSuitableContainer(Tag.valueOf("h1"));

        assertNotNull(container);
        assertEquals("body", container.tagName());
    }

    @Test
    public void testPopStackToClose() {
        Element element = new Element(Tag.valueOf("h1"), "https://www.example.com");
        parser.stack.add(element);
        Element closedElement = parser.popStackToClose(Tag.valueOf("h1"));

        assertNotNull(closedElement);
        assertEquals("h1", closedElement.tagName());
        assertTrue(parser.stack.isEmpty());
    }

    @Test
    public void testLast() {
        Element element = new Element(Tag.valueOf("h1"), "https://www.example.com");
        parser.stack.add(element);

        Element last = parser.last();

        assertNotNull(last);
        assertEquals("h1", last.tagName());
    }
}