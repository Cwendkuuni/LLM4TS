package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;

import java.util.*;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for Parser.java
 */
public class ParserTest {

    private Parser parser;

    @Before
    public void setUp() {
        String html = "<html><head><title>Test Page</title></head>" +
                       "<body><h1>Hello World</h1>" +
                       "<p>This is a <b>test</b>.</p>" +
                       "</body></html>";
        parser = new Parser(html, "http://example.com/", false);
    }

    @Test
    public void testParse() {
        Document doc = parser.parse();
        assertEquals("Test Page", doc.title());
        assertEquals("Hello World", doc.select("h1").text());
        assertEquals("This is a test.", doc.select("p").text());
        assertNotNull(doc.select("h1").html());
        assertNotNull(doc.select("b").html());
        assertNull(doc.select("p b").html());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>This is a <b>test</b>.</p>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com/");
        assertEquals("", doc.head().html());
        assertEquals("This is a test.", doc.select("p").text());
        assertNotNull(doc.select("p b").html());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullHtml() {
        Parser parser = new Parser(null, "http://example.com/", false);
        // the constructor should throw an IllegalArgumentException
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullBaseUri() {
        Parser parser = new Parser("<html></html>", null, false);
        // the constructor should throw an IllegalArgumentException
    }

    @Test
    public void testParseEmptyHtml() {
        String html = "";
        Document doc = Parser.parse(html, "http://example.com/");
        assertEquals("", doc.html());
        assertNull(doc.title());
    }

    @Test
    public void testParseComment() {
        String html = "<!-- Test Comment -->";
        Document doc = Parser.parse(html, "http://example.com/");
        Element comment = doc.select("!--").first();
        assertEquals("Test Comment", comment.data());
    }

    @Test
    public void testParseCdata() {
        String html = "<html><![CDATA[ <p>Test CDATA</p> ]]>";
        Document doc = Parser.parse(html, "http://example.com/");
        Element cdata = doc.select("p").first();
        assertEquals("<p>Test CDATA</p>", cdata.html());
    }

    @Test
    public void testParseXmlDecl() {
        String html = "<html xmlns:foo=\"bar\">";
        Document doc = Parser.parse(html, "http://example.com/");
        XmlDeclaration xmlDecl = doc.select("xml").first();
        assertEquals("http://example.com/", xmlDecl.baseUri());
        assertEquals("bar", xmlDecl.xmlns("foo"));
    }

    @Test
    public void testParseEndTag() {
        String html = "<html><body><p>Test</p></body></html>";
        Document doc = Parser.parse(html, "http://example.com/");
        Element p = doc.select("p").first();
        assertEquals(3, doc.body().childNodeSize());
        assertEquals("Test", p.text());
    }

    @Test
    public void testParseStartTag() {
        String html = "<html><body><h1>Test</h1></body></html>";
        Document doc = Parser.parse(html, "http://example.com/");
        Element h1 = doc.select("h1").first();
        assertEquals(1, doc.head().childNodeSize());
        assertEquals("Test", h1.text());
    }

    @Test
    public void testParseAttribute() {
        String html = "<a href=\"test.html\" title=\"Test Title\"></a>";
        Document doc = Parser.parse(html, "http://example.com/");
        Element a = doc.select("a").first();
        assertEquals("test.html", a.attr("href"));
        assertEquals("Test Title", a.attr("title"));
        assertNull(a.attr("unknown"));
    }

    @Test
    public void testParseTextNode() {
        String html = "<p>Test</p>";
        Document doc = Parser.parse(html, "http://example.com/");
        Element p = doc.select("p").first();
        assertEquals("Test", p.text());
    }
}