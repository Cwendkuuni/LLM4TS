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

    @ org.junit.Before
    public void setUp() {
        // Set up anything that needs to be initialized before each test
    }

    @ org.junit.After
    public void tearDown() {
        // Clean up anything that was initialized in setUp
    }

    /**
     * Test of parse method, of class Parser.
     */
    @ org.junit.Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head>" +
                "<body><h1>Hello World</h1></body></html>";

        String baseUri = "http://example.com/";

        Parser parser = new Parser(html, baseUri, false);
        Document result = parser.parse();

        // Check that parsed HTML matches expected output
        Document expected = Jsoup.parse(html, baseUri);
        assertEquals(expected.html(), result.html());

        // Test specific nodes and attributes
        Element title = result.select("title").first();
        assertEquals("Test", title.text());
        Element h1 = result.select("h1").first();
        assertEquals("Hello World", h1.text());
    }

    /**
     * Test of parseBodyFragment method, of class Parser.
     */
    @ org.junit.Test
    public void testParseBodyFragment() {
        String bodyHtml = "<body><h1>Hello World</h1></body>";

        String baseUri = "http://example.com/";

        Parser parser = new Parser(bodyHtml, baseUri, true);
        Document result = parser.parse();

        // Check that parsed HTML matches expected output
        Document expected = Jsoup.parseBodyFragment(bodyHtml, baseUri);
        assertEquals(expected.html(), result.html());

        // Test specific nodes and attributes
        Element h1 = result.select("h1").first();
        assertEquals("Hello World", h1.text());
    }

    /**
     * Test of parse method with empty HTML.
     */
    @ org.junit.Test
    public void testParseEmptyHtml() {
        String html = "";

        String baseUri = "http://example.com/";

        Parser parser = new Parser(html, baseUri, false);
        Document result = parser.parse();

        // Check that parsed HTML matches expected output
        Document expected = new Document(baseUri);
        assertEquals(expected.html(), result.html());
    }

    /**
     * Test of parse method with invalid HTML.
     */
    @ org.junit.Test(expected = org.jsoup.ParseError.class)
    public void testParseInvalidHtml() {
        String html = "<html<title>Test</title>";

        String baseUri = "http://example.com/";

        Parser parser = new Parser(html, baseUri, false);
        parser.parse();
    }

    /**
     * Test of parseComment method.
     */
    @ org.junit.Test
    public void testParseComment() {
        String html = "<!-- This is a comment -->";

        String baseUri = "http://example.com/";

        Parser parser = new Parser(html, baseUri, false);
        Document result = parser.parse();

        // Check that parsed comment matches expected output
        Comment expected = new Comment("This is a comment", baseUri);
        Element comment = result.select("comment()").first();
        assertEquals(expected.data(), comment.text());
    }

    /**
     * Test of parseCdata method.
     */
    @ org.junit.Test
    public void testParseCdata() {
        String html = "<script><![CDATA[alert('Hello World!');]]></script>";

        String baseUri = "http://example.com/";

        Parser parser = new Parser(html, baseUri, false);
        Document result = parser.parse();

        // Check that parsed script tag and CDATA matches expected output
        DataNode expectedCdata = new DataNode("alert('Hello World!');", baseUri);
        Element script = result.select("script").first();
        Node cdata = script.child(0);
        assertEquals(expectedCdata.data(), cdata.data());
    }

    /**
     * Test of parseXmlDecl method.
     */
    @ org.junit.Test
    public void testParseXmlDecl() {
        String html = "<xml version='1.0' encoding='UTF-8' standalone='no'>";

        String baseUri = "http://example.com/";

        Parser parser = new Parser(html, baseUri, false);
        Document result = parser.parse();

        // Check that parsed XML declaration matches expected output
        XmlDeclaration expectedXmlDecl = new XmlDeclaration("1.0", "UTF-8", false);
        Element xmlDecl = result.select("xml-declaration()").first();
        assertEquals(expectedXmlDecl.version(), xmlDecl.attribute("version").value());
        assertEquals(expectedXmlDecl.encoding(), xmlDecl.attribute("encoding").value());
        assertEquals(expectedXmlDecl.isStandalone(), xmlDecl.attribute("standalone").value().equals("yes"));
    }

    /**
     * Test of parseEndTag method.
     */
    @ org.junit.Test
    public void testParseEndTag() {
        String html = "<div><p>Hello</p></div>";

        String baseUri = "http://example.com/";

        Parser parser = new Parser(html, baseUri, false);
        Document result = parser.parse();

        // Check that parsed end tag is removed correctly
        Element div = result.select("div").first();
        Element p = div.child(0);
        assertEquals(1, div.childSize());
        assertEquals("Hello", p.text());
        Element emptyDiv = result.select("div").last();
        assertNull(emptyDiv);
    }

    /**
     * Test of parseStartTag method.
     */
    @ org.junit.Test
    public void testParseStartTag() {
        String html = "<div><p>Hello</p><h1>World</h1></div>";

        String baseUri = "http://example.com/";

        Parser parser = new Parser(html, baseUri, false);
        Document result = parser.parse();

        // Check that parsed start tags are added correctly
        Element div = result.select("div").first();
        Element p = div.child(0);
        assertEquals("Hello", p.text());
        Element h1 = div.child(1);
        assertEquals("World", h1.text());
        assertEquals(2, div.childSize());
    }

    /**
     * Test of parseAttribute method.
     */
    @ org.junit.Test
    public void testParseAttribute() {
        String html = "<img src='image.jpg' alt='Description' title='Title'>";

        String baseUri = "http://example.com/";

        Parser parser = new Parser(html, baseUri, false);
        Document result = parser.parse();

        // Check that parsed attributes are added correctly
        Element img = result.select("img").first();
        assertEquals("image.jpg", img.src());
        assertEquals("Description", img.attr("alt"));
        assertEquals("Title", img.attr("title"));
    }

    /**
     * Test of parseTextNode method.
     */
    @ org.junit.Test
    public void testParseTextNode() {
        String html = "<p>Hello World</p>";

        String baseUri = "http://example.com/";

        Parser parser = new Parser(html, baseUri, false);
        Document result = parser.parse();

        // Check that parsed text nodes match expected output
        Element p = result.select("p").first();
        TextNode expectedText = new TextNode("Hello World", baseUri);
        TextNode text = p.child(0);
        assertEquals(expectedText.text(), text.text());
    }
}