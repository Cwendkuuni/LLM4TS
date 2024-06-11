package org.jsoup.parser;

import static org.junit.Assert.*;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;

import java.util.*;

/**
 * Test class for Parser.java
 */
public class ParserTest {

    @org.junit.Before
    public void setUp() {
        // Set up any initialization needed before running tests
    }

    @org.junit.After
    public void tearDown() {
        // Clean up after running tests
    }

    /**
     * Test the parse method with valid HTML.
     */
    @org.junit.Test
    public void testParseValidHtml() {
        String html = "<html><head><title>Test Title</title></head><body><h1>Test Heading</h1></body></html>";
        Parser parser = new Parser(html, "http://test.com/", false);
        Document doc = parser.parse();

        // Assertions for document structure and contents
        assertEquals("Test Title", doc.title().text());
        assertEquals("Test Heading", doc.body().select("h1").text());
        assertEquals("http://test.com/", doc.baseUri());
    }

    /**
     * Test the parseBodyFragment method with valid HTML fragment.
     */
    @org.junit.Test
    public void testParseBodyFragmentValidHtml() {
        String bodyHtml = "<h1>Test Heading</h1>";
        Parser parser = new Parser(bodyHtml, "http://test.com/", true);
        Document doc = parser.parse();

        // Assertions for document structure and contents
        assertEquals("Test Heading", doc.body().select("h1").text());
        assertEquals("http://test.com/", doc.baseUri());
        assertTrue(doc.head() == null);
    }

    /**
     * Test the parse method with invalid HTML.
     */
    @org.junit.Test(expected = RuntimeException.class)
    public void testParseInvalidHtml() {
        String html = "<html><body<title>Test Title</title>"; // missing '>' on title tag
        Parser parser = new Parser(html, "http://test.com/", false);
        parser.parse(); // should throw a RuntimeException
    }

    /**
     * Test the parseStartTag method with valid and invalid tags.
     */
    @org.junit.Test
    public void testParseStartTag() {
        // Set up a Parser instance for testing
        Parser parser = new Parser("", "http://test.com/", false);
        parser.tq = new TokenQueue("");
        parser.stack = new LinkedList<>();
        parser.doc = new Document("http://test.com/");

        // Test valid tags
        parser.parseStartTag();
        assertEquals(htmlTag, parser.last().tag());
        parser.parseStartTag();
        assertEquals(headTag, parser.last().tag());
        parser.parseStartTag();
        assertEquals(titleTag, parser.last().tag());
        parser.parseStartTag();
        assertEquals(bodyTag, parser.last().tag());

        // Test invalid tags
        parser.parseStartTag(); // should not match any tag, consume whitespace and continue
        parser.parseStartTag(); // should match 'html' but throw an exception since it's already on stack
        parser.parseStartTag(); // should not match any tag, consume whitespace and continue
    }

    /**
     * Test the parseComment method.
     */
    @org.junit.Test
    public void testParseComment() {
        // Set up a Parser instance for testing
        Parser parser = new Parser("", "http://test.com/", false);
        parser.tq = new TokenQueue("<!-- This is a test comment -->");
        parser.stack = new LinkedList<>();
        parser.doc = new Document("http://test.com/");

        parser.parseComment();

        // Assertions for the comment node
        assertEquals("This is a test comment", parser.last().text());
        assertEquals(Comment.class, parser.last().getClass());
    }

    /**
     * Test the parseEndTag method.
     */
    @org.junit.Test
    public void testParseEndTag() {
        // Set up a Parser instance for testing
        Parser parser = new Parser("", "http://test.com/", false);
        parser.tq = new TokenQueue("</body></html>");
        parser.stack = new LinkedList<>();
        parser.doc = new Document("http://test.com/");

        parser.parseEndTag();
        parser.parseEndTag();

        // Assertions for the current stack state
        assertEquals(htmlTag, parser.stack.getLast().tag());
        assertEquals(bodyTag, parser.stack.getLast().getPreviousSibling().tag());
    }

    /**
     * Test the parseAttribute method.
     */
    @org.junit.Test
    public void testParseAttribute() {
        // Set up a Parser instance for testing
        Parser parser = new Parser("", "http://test.com/", false);
        parser.tq = new TokenQueue("<div id='test' class='testClass'/>");
        parser.stack = new LinkedList<>();
        parser.doc = new Document("http://test.com/");

        Attribute attribute = parser.parseAttribute();

        // Assertions for the attribute
        assertEquals("id", attribute.key());
        assertEquals("test", attribute.value());

        attribute = parser.parseAttribute();
        assertEquals("class", attribute.key());
        assertEquals("testClass", attribute.value());

        parser.parseStartTag(); // consume '<' and 'div'
        assertEquals(null, parser.parseAttribute()); // no more attributes to parse
    }

    /**
     * Test the parseTextNode method.
     */
    @org.junit.Test
    public void testParseTextNode() {
        // Set up a Parser instance for testing
        Parser parser = new Parser("", "http://test.com/", false);
        parser.tq = new TokenQueue("Hello World");
        parser.stack = new LinkedList<>();
        parser.doc = new Document("http://test.com/");

        TextNode textNode = (TextNode) parser.parseTextNode();

        // Assertions for the text node
        assertEquals("Hello World", textNode.text());
        assertEquals(TextNode.class, textNode.getClass());
    }
}