package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;

public class ParserTest {

    @Test
    public void testParseDocument() {
        String html = "<html><head><title>Test</title></head><body><p>Hello, world!</p></body></html>";
        Document doc = Parser.htmlParser().parseInput(html, null);
        assertEquals("Test", doc.title().text());
        assertEquals("Hello, world!", doc.body().text());
    }

    @Test
    public void testParseDocumentWithErrors() {
        String html = "<html><head><title>Test</title></head><body><p>Hello, <script>world!</script></p></body></html>";
        Parser parser = Parser.htmlParser();
        parser.setTrackErrors(1);
        Document doc = parser.parseInput(html, null);
        List<ParseError> errors = parser.getErrors();
        assertEquals(1, errors.size());
        assertEquals("Found a script tag in <p>, but scripts are not allowed in this context.", errors.get(0).getMessage());
        assertNull(doc.body().child(0).child(0).text());
    }

    @Test
    public void testParseDocumentWithMaxErrors() {
        String html = "<html><head><title>Test</title></head><body><p>Hello, <script>world!</script></p></body></html>";
        Parser parser = Parser.htmlParser();
        parser.setTrackErrors(3);
        Document doc = parser.parseInput(html, null);
        List<ParseError> errors = parser.getErrors();
        assertEquals(3, errors.size());
        assertEquals("Found a script tag in <p>, but scripts are not allowed in this context.", errors.get(0).getMessage());
        assertEquals("Found a script tag in <p>, but scripts are not allowed in this context.", errors.get(1).getMessage());
        assertEquals("Found a script tag in <p>, but scripts are not allowed in this context.", errors.get(2).getMessage());
        assertNull(doc.body().child(0).child(0).text());
    }

    @Test
    public void testParseDocumentWithNoErrors() {
        String html = "<html><head><title>Test</title></head><body><p>Hello, world!</p></body></html>";
        Parser parser = Parser.htmlParser();
        parser.setTrackErrors(0);
        Document doc = parser.parseInput(html, null);
        List<ParseError> errors = parser.getErrors();
        assertTrue(errors.isEmpty());
        assertEquals("Hello, world!", doc.body().text());
    }

    @Test
    public void testParseFragment() {
        String fragmentHtml = "<p>Hello, fragment!</p>";
        Element context = Jsoup.parseBodyFragment(null, null).body();
        List<Node> nodes = Parser.htmlParser().parseFragment(fragmentHtml, context, null);
        assertEquals(1, nodes.size());
        assertEquals("Hello, fragment!", nodes.get(0).text());
    }

    @Test
    public void testParseFragmentWithErrors() {
        String fragmentHtml = "<p>Hello, <script>fragment!</script></p>";
        Element context = Jsoup.parseBodyFragment(null, null).body();
        Parser parser = Parser.htmlParser();
        parser.setTrackErrors(1);
        List<Node> nodes = parser.parseFragment(fragmentHtml, context, null);
        List<ParseError> errors = parser.getErrors();
        assertEquals(1, errors.size());
        assertEquals("Found a script tag in <p>, but scripts are not allowed in this context.", errors.get(0).getMessage());
        assertNull(nodes.get(0).child(0).text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello, body fragment!</p>";
        Document doc = Parser.parseBodyFragment(bodyHtml, null);
        assertEquals("Hello, body fragment!", doc.body().text());
    }

    @Test
    public void testParseBodyFragmentWithErrors() {
        String bodyHtml = "<p>Hello, <script>body fragment!</script></p>";
        Document doc = Parser.htmlParser().parseBodyFragment(bodyHtml, null);
        List<ParseError> errors = Parser.htmlParser().getErrors();
        assertEquals(1, errors.size());
        assertEquals("Found a script tag in <p>, but scripts are not allowed in this context.", errors.get(0).getMessage());
        assertNull(doc.body().child(0).text());
    }

    @Test
    public void testUnescapeHTML() {
        String htmlEscaped = "&lt;p&gt;Hello, &amp;world!&lt;/p&gt;";
        String htmlUnescaped = Parser.htmlParser().unescapeEntities(htmlEscaped);
        assertEquals("<p>Hello, world!</p>", htmlUnescaped);
    }

    @Test
    public void testParseRelaxedBodyFragment() {
        String bodyHtml = "<script>alert('Hello, world!');</script>";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, null);
        assertNotNull(doc);
        assertNotNull(doc.body());
        assertEquals("alert('Hello, world!');", doc.body().html());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testParseBodyFragmentWithNullBaseUri() {
        String bodyHtml = "<p>Hello, body fragment!</p>";
        Parser.parseBodyFragment(bodyHtml, null);
    }
}