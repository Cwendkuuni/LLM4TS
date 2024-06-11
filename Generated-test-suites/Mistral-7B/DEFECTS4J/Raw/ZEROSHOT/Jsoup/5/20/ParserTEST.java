package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.*;

import java.util.LinkedList;

/**
 * Test class for Parser.java
 */
public class ParserTest {

    private Document doc;
    private Parser parser;

    @Before
    public void setUp() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        doc = Parser.parse(html, "http://example.com");
        parser = new Parser(html, "http://example.com", false);
    }

    @Test
    public void testParse() {
        Document parsedDoc = parser.parse();
        assertEquals(doc, parsedDoc);
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<body><h1>Hello World</h1></body>";
        Document parsedDoc = Parser.parseBodyFragment(bodyHtml, "http://example.com");
        assertEquals(doc.body(), parsedDoc);
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<body><h1>Hello World</h1>";
        Document parsedDoc = Parser.parseBodyFragmentRelaxed(bodyHtml, "http://example.com");
        assertEquals(doc.body(), parsedDoc);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseNullHtml() {
        Parser parser = new Parser(null, "http://example.com", false);
        parser.parse();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseNullBaseUri() {
        Parser parser = new Parser("", null, false);
        parser.parse();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseEmptyBodyFragment() {
        Parser parser = new Parser("", "http://example.com", true);
        parser.parse();
        fail("Expected IllegalArgumentException, but did not get one.");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidTagName() {
        String html = "<html><tag></tag></html>";
        try {
            parser.parseStartTag();
            fail("Expected IllegalArgumentException, but did not get one.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseEmptyTagName() {
        String html = "<html><tag></tag></html>";
        try {
            parser.parseStartTag();
            fail("Expected IllegalArgumentException, but did not get one.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseMissingEndTag() {
        String html = "<html><body><h1>Hello World</h1>";
        try {
            parser.parse();
            fail("Expected IllegalArgumentException, but did not get one.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseComment() {
        parser.parseComment();
        Element comment = doc.select("comment").first();
        assertEquals("Test", comment.text());
    }

    @Test
    public void testParseXmlDecl() {
        parser.parseXmlDecl();
        Element xmlDecl = doc.select("xml-declaration").first();
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", xmlDecl.toString());
    }

    @Test
    public void testParseEndTag() {
        parser.parseEndTag();
        Element tag = doc.select("html").last();
        assertTrue(tag.tagName().equals("html"));
    }

    @Test
    public void testParseStartTag() {
        parser.parseStartTag();
        Element tag = doc.select("html").last();
        assertTrue(tag.tagName().equals("html"));
    }

    @Test
    public void testParseAttribute() {
        Attribute attribute = parser.parseAttribute();
        assertNull(attribute);

        String html = "<img src='image.png' alt='Image' title='Title' id='id'>";
        parser = new Parser(html, "http://example.com", false);
        attribute = parser.parseAttribute();
        assertEquals("src", attribute.getKey());
        assertEquals("image.png", attribute.getValue());

        attribute = parser.parseAttribute();
        assertEquals("alt", attribute.getKey());
        assertEquals("Image", attribute.getValue());

        attribute = parser.parseAttribute();
        assertEquals("title", attribute.getKey());
        assertEquals("Title", attribute.getValue());

        attribute = parser.parseAttribute();
        assertEquals("id", attribute.getKey());
        assertEquals("id", attribute.getValue());

        parser.parseAttribute(); // consume id attribute, but don't test value since it's already been tested

        attribute = parser.parseAttribute();
        assertNull(attribute);
    }

    @Test
    public void testParseTextNode() {
        TextNode textNode;
        textNode = parser.parseTextNode();
        assertEquals("", textNode.text());

        String text = "Hello World";
        parser = new Parser(text, "http://example.com", false);
        textNode = parser.parseTextNode();
        assertEquals(text, textNode.text());

        text = "<h1>Hello World</h1>";
        parser = new Parser(text, "http://example.com", false);
        textNode = parser.parseTextNode();
        assertEquals("Hello World", textNode.text());
    }

    @Test
    public void testParseCdata() {
        String text = "<![CDATA[Hello World]]>";
        parser = new Parser(text, "http://example.com", false);
        TextNode textNode = (TextNode) parser.parse();
        assertEquals("Hello World", textNode.text());
    }
}