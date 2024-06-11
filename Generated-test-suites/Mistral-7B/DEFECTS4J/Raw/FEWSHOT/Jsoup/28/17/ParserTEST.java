package org.jsoup.parser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.TreeBuilder;

import java.util.ArrayList;
import java.util.List;

public class ParserTest {

    @Test
    public void testCreateParserWithHtmlTreeBuilder() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);
        assertNotNull(parser);
    }

    @Test
    public void testCreateParserWithXmlTreeBuilder() {
        TreeBuilder treeBuilder = new XmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);
        assertNotNull(parser);
    }

    @Test
    public void testParseInputWithDefaults() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Document doc = Parser.parse(html);
        assertNotNull(doc);
        assertEquals("Hello World", doc.selectFirst("h1").text());
    }

    @Test
    public void testParseInputWithBaseUri() {
        String html = "<img src='img.png' />";
        String baseUri = "http://example.com/index.html";
        Document doc = Parser.parse(html, baseUri);
        assertNotNull(doc);
        assertEquals("http://example.com/img.png", doc.selectFirst("img").absUrl("src"));
    }

    @Test
    public void testParseInputWithMaxErrors() {
        String html = "<html><body><h1>Hello <script>alert('World')</script></h1></body></html>";
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(1);
        Document doc = parser.parseInput(html, null);
        List<ParseError> errors = parser.getErrors();
        assertNotNull(doc);
        assertTrue(errors.size() > 0);
        assertEquals("Unexpected '<' at position 84", errors.get(0).getMessage());
    }

    @Test
    public void testParseInputWithNoMaxErrors() {
        String html = "<html><body><h1>Hello <script>alert('World')</script></h1></body></html>";
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(0);
        Document doc = parser.parseInput(html, null);
        List<ParseError> errors = parser.getErrors();
        assertNotNull(doc);
        assertNull(errors);
    }

    @Test
    public void testSetTreeBuilder() {
        TreeBuilder oldTreeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(oldTreeBuilder);
        TreeBuilder newTreeBuilder = new XmlTreeBuilder();
        parser.setTreeBuilder(newTreeBuilder);
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        assertNotNull(treeBuilder);
        assertTrue(treeBuilder instanceof XmlTreeBuilder);
    }

    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        assertFalse(parser.isTrackErrors());
        parser.setTrackErrors(1);
        assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(0);
        int maxErrors = parser.getMaxErrors();
        assertEquals(0, maxErrors);
        parser.setTrackErrors(5);
        maxErrors = parser.getMaxErrors();
        assertEquals(5, maxErrors);
    }

    @Test
    public void testGetErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        List<ParseError> errors = new ArrayList<>();
        parser.setTrackErrors(5);
        ParseErrorList errorList = new ParseErrorList() {
            @Override
            public ParseError get(int index) {
                return errors.get(index);
            }

            @Override
            public int size() {
                return errors.size();
            }
        };
        parser.setErrors(errorList);
        parser.parseInput("<script>", null);
        errors.add(new ParseError(" Unexpected '<' at position 1", 1, 1));
        assertEquals(1, parser.getErrors().size());
        assertEquals(" Unexpected '<' at position 1", parser.getErrors().get(0).getMessage());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello World</p>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");
        assertNotNull(doc);
        assertEquals("Hello World", doc.select("p").text());
    }

    @Test
    public void testParseBodyFragmentRelaxedDeprecated() {
        String bodyHtml = "<p>Hello World</p>";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "http://example.com");
        assertNotNull(doc);
        assertEquals("Hello World", doc.select("p").text());
    }

    @Test
    public void testParseFragment() {
        String fragmentHtml = "<div><p>Hello World</p></div>";
        Element context = Jsoup.parse("<html><body></body></html>").body();
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, "http://example.com");
        assertNotNull(nodes);
        assertEquals(2, nodes.size());
        assertEquals("div", nodes.get(0).tagName());
        assertEquals("p", nodes.get(1).tagName());
        assertEquals("Hello World", nodes.get(1).text());
    }
}