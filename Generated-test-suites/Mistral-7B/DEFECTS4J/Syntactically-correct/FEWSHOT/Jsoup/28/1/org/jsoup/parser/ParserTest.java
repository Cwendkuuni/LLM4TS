package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.ParsedText;
import org.jsoup.nodes.XmlTree;
import org.jsoup.nodes.HtmlTree;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.HtmlTreeBuilder;
import org.jsoup.parser.XmlTreeBuilder;

public class ParserTest {

    @Test
    public void testParseInput() {
        String html = "<html><head></head><body><h1>Hello</h1></body></html>";
        Parser parser = Parser.htmlParser();
        Document doc = parser.parseInput(html, null);

        assertNotNull(doc);
        assertEquals("html", doc.tagName());

        Element head = doc.head();
        assertNull(head);

        Element body = doc.body();
        assertNotNull(body);
        assertEquals("body", body.tagName());

        Element h1 = body.child(0);
        assertNotNull(h1);
        assertEquals("h1", h1.tagName());
        assertEquals("Hello", h1.text());
    }

    @Test
    public void testParseInputWithBaseUri() {
        String html = "<img src=\"image.jpg\" alt=\"Image\"/>";
        String baseUri = "https://example.com";
        Parser parser = Parser.htmlParser();
        Document doc = parser.parseInput(html, baseUri);

        assertNotNull(doc);
        Element img = doc.select("img").first();
        assertNotNull(img);
        assertEquals("img", img.tagName());
        assertEquals("image.jpg", img.attr("src"));
        assertEquals("Image", img.attr("alt"));
    }

    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        assertTrue(parser.isTrackErrors());
        parser.setTrackErrors(0);
        assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testSetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder newTreeBuilder = new XmlTreeBuilder();
        parser.setTreeBuilder(newTreeBuilder);
        assertEquals(newTreeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testGetErrors() {
        String invalidHtml = "<html><h1<script>alert(\"XSS\")</script></h1>";
        Parser parser = Parser.htmlParser();
        Document doc = parser.parseInput(invalidHtml, null);

        List<ParseError> errors = parser.getErrors();
        assertNotNull(errors);
        assertTrue(errors.size() > 0);
        ParseError error = errors.get(0);
        assertEquals("Error parsing start tag : : h1<script>alert(\"XSS\")</script>", error.getMessage());
        assertEquals(27, error.getLine());
        assertEquals(1, error.getColumn());

        errors = parser.getErrors();
        assertNotNull(errors);
        assertTrue(errors.size() > 0);
        error = errors.get(0);
        assertEquals("Error parsing end tag : : h1", error.getMessage());
        assertEquals(27, error.getLine());
        assertEquals(8, error.getColumn());
    }

    @Test
    public void testParseStaticFunctions() {
        String html = "<html><head></head><body><h1>Hello</h1></body></html>";

        Document doc = Parser.parse(html, null);
        assertNotNull(doc);
        assertEquals("html", doc.tagName());

        Element body = Parser.parse(html, null, "body").first();
        assertNotNull(body);
        assertEquals("body", body.tagName());

        List<Node> fragmentNodes = Parser.parseFragment("<p>Text</p>", null, null);
        assertNotNull(fragmentNodes);
        assertEquals(1, fragmentNodes.size());
        Node node = fragmentNodes.get(0);
        assertEquals("p", node.tagName());
        assertEquals("Text", node.text());
        assertEquals(0, node.childSize());

        Document docWithBodyFragment = Parser.parseBodyFragment("<p>Text</p>", null);
        Element bodyElement = docWithBodyFragment.body();
        assertNotNull(bodyElement);
        assertEquals("body", bodyElement.tagName());
        Node nodeInBody = bodyElement.child(0);
        assertEquals("p", nodeInBody.tagName());
        assertEquals("Text", nodeInBody.text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        DeprecatedParseBodyFragmentTest deprecatedTest = new DeprecatedParseBodyFragmentTest();
        deprecatedTest.testParseBodyFragmentRelaxed();
    }

    // Add other test cases as needed for static functions and getters/setters
}

class DeprecatedParseBodyFragmentTest {

    @Test
    @Deprecated
    public void testParseBodyFragmentRelaxed() {
        String html = "<html><body><p>Text</p></body></html>";
        Document doc = Parser.parseBodyFragment(html, null);
        assertNotNull(doc);
        Element bodyElement = doc.body();
        assertNotNull(bodyElement);
        Node nodeInBody = bodyElement.child(0);
        assertEquals("p", nodeInBody.tagName());
        assertEquals("Text", nodeInBody.text());
    }
}