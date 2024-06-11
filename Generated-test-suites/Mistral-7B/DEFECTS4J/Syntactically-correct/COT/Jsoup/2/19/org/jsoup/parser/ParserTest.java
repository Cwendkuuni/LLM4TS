package org.jsoup.parser;

import static org.junit.Assert.*;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

/**
 * Test class for Parser.java
 */
public class ParserTest {

    private Parser parser;

    @Before
    public void setUp() {
        String html = "<html><head><title>Test Page</title></head><body><h1>Hello World</h1></body></html>";
        parser = new Parser(html, "http://example.com");
    }

    // Test for the parse method
    @Test
    public void testParse() {
        Document doc = parser.parse();
        assertEquals("Test Page", doc.title());
        assertEquals("Hello World", doc.select("h1").text());
    }

    // Test for the parseBodyFragment method
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<body><p>This is a body fragment.</p></body>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");
        Element body = doc.body();
        assertEquals("This is a body fragment.", body.text());
    }

    // Test for the parseComment method
    @Test
    public void testParseComment() {
        String html = "<html><!-- This is a comment --><body></body></html>";
        parser = new Parser(html, "http://example.com");
        parser.parse();
        assertEquals("This is a comment", parser.last().child(0).text());
    }

    // Test for the parseXmlDecl method
    @Test
    public void testParseXmlDecl() {
        String html = "<xml version=\"1.0\" encoding=\"UTF-8\">";
        parser = new Parser(html, "http://example.com");
        parser.parse();
        assertTrue(parser.last().child(0) instanceof XmlDeclaration);
    }

    // Test for the parseEndTag method
    @Test(expected = AssertionError.class)
    public void testParseEndTagInvalidTag() {
        parser = new Parser("<html><body></body></badtag>", "http://example.com");
        parser.parse();
    }

    // Test for the parseEndTag method with valid tag
    @Test
    public void testParseEndTagValidTag() {
        parser = new Parser("<html><body><p>This is a paragraph.</p></body></p>", "http://example.com");
        parser.parse();
        assertNull(parser.last().child(0));
    }

    // Test for the parseStartTag method
    @Test
    public void testParseStartTag() {
        parser = new Parser("<html><head><title>Test Page</title></head><body><h1>Hello World</h1></body></html>", "http://example.com");
        parser.parse();
        assertEquals("Test Page", parser.last().child(0).child(0).text());
        assertEquals("Hello World", parser.last().child(1).child(0).text());
    }

    // Test for the parseAttribute method
    @Test
    public void testParseAttribute() {
        String html = "<img src='image.png' alt='An image' title='Image title'>";
        parser = new Parser(html, "http://example.com");
        parser.parse();
        Element img = parser.last().child(0);
        assertEquals("image.png", img.attr("src"));
        assertEquals("An image", img.attr("alt"));
        assertEquals("Image title", img.attr("title"));
    }

    // Test for the parseTextNode method
    @Test
    public void testParseTextNode() {
        String html = "<html><body><p>This is a text node.</p></body></html>";
        parser = new Parser(html, "http://example.com");
        parser.parse();
        Element p = parser.last().child(0);
        assertEquals("This is a text node.", p.child(0).text());
    }

    // Test for the parseCdata method
    @Test
    public void testParseCdata() {
        String html = "<html><![CDATA[This is some CDATA]]><body></body></html>";
        parser = new Parser(html, "http://example.com");
        parser.parse();
        assertEquals("This is some CDATA", parser.last().child(0).text());
    }

    // Test for the addChildToParent method
    @Test
    public void testAddChildToParent() {
        parser = new Parser("<html><body><div><p>This is a paragraph.</p></div></body></html>", "http://example.com");
        parser.parse();
        Element div = parser.last().child(0);
        assertEquals(1, div.childNodeSize());
        Element p = div.child(0);
        assertEquals("This is a paragraph.", p.text());
    }

    // Test for the stackHasValidParent method
    @Test
    public void testStackHasValidParent() {
        parser = new Parser("<html><body><p><div></div></p></body></html>", "http://example.com");
        parser.parse();
        assertTrue(parser.stackHasValidParent(Tag.BODY));
        assertFalse(parser.stackHasValidParent(Tag.HTML));
    }

    // Test for the popStackToSuitableContainer method
    @Test
    public void testPopStackToSuitableContainer() {
        parser = new Parser("<html><body></body><head></head></html>", "http://example.com");
        parser.parse();
        Element head = parser.popStackToSuitableContainer(Tag.HEAD);
        assertNotNull(head);
        assertEquals(0, head.childNodeSize());
    }

    // Test for the popStackToClose method
    @Test
    public void testPopStackToClose() {
        parser = new Parser("<html><body><p><div></div></p></body></div>", "http://example.com");
        parser.parse();
        Element div = parser.popStackToClose(Tag.DIV);
        assertNotNull(div);
        assertEquals(1, div.childNodeSize());
    }
}