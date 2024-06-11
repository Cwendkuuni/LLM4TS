package org.jsoup.parser;

import static org.junit.Assert.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.ParseError;
import org.junit.Test;

import java.util.List;

public class ParserTest {

    @Test
    public void testParseInput() {
        String html = "<html><body><div>Hello, World!</div></body></html>";
        String baseUri = "http://example.com";

        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput(html, baseUri);

        assertEquals("html", doc.tagName());
        assertEquals(1, doc.childNodes().size());

        Element body = doc.body();
        assertEquals("body", body.tagName());
        assertEquals(1, body.childNodes().size());

        Element div = body.childNodes().get(0);
        assertEquals("div", div.tagName());
        assertEquals("Hello, World!", div.text());
    }

    @Test
    public void testParseInputWithTrackErrors() {
        String html = "<html><body><div>Hello, <script>World!</script></div></body></html>";
        String baseUri = "http://example.com";

        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(1);
        List<ParseError> errors = parser.parseInput(html, baseUri).errors();

        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals("Error parsing body: Unexpected char 63 (script tag not closed)", error.getMessage());
        assertEquals("script", error.selector());
    }

    @Test
    public void testParseInputWithNoTrackErrors() {
        String html = "<html><body><div>Hello, <script>World!</script></div></body></html>";
        String baseUri = "http://example.com";

        Parser parser = new Parser(new HtmlTreeBuilder());
        List<ParseError> errors = parser.parseInput(html, baseUri).errors();

        assertEquals(0, errors.size());
    }

    @Test
    public void testParseInputNullHtml() {
        String baseUri = "http://example.com";

        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput(null, baseUri);

        assertNull(doc);
    }

    @Test
    public void testParseInputEmptyHtml() {
        String baseUri = "http://example.com";

        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput("", baseUri);

        assertNotNull(doc);
        assertEquals(0, doc.childNodes().size());
    }

    @Test
    public void testParseInputWithBaseUri() {
        String html = "<img src=\"image.png\" alt=\"Image\">";
        String baseUri = "http://example.com";

        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput(html, baseUri);

        Element img = doc.selectFirst("img");
        assertEquals("http://example.com/image.png", img.attr("abs:src"));
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<div>Hello, World!</div>";
        String baseUri = "http://example.com";

        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        Element body = doc.body();
        Element div = body.childNodes().get(0);

        assertEquals("div", div.tagName());
        assertEquals("Hello, World!", div.text());
    }

    @Test
    public void testParseBodyFragmentWithTrackErrors() {
        String bodyHtml = "<div>Hello, <script>World!</script></div>";
        String baseUri = "http://example.com";

        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(1);
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

        List<ParseError> errors = doc.errors();
        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals("Error parsing body: Unexpected char 63 (script tag not closed)", error.getMessage());
        assertEquals("script", error.selector());
    }

    @Test
    public void testParseBodyFragmentWithNoTrackErrors() {
        String bodyHtml = "<div>Hello, <script>World!</script></div>";
        String baseUri = "http://example.com";

        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        List<ParseError> errors = doc.errors();

        assertEquals(0, errors.size());
    }

    @Test
    public void testParseFragment() {
        String fragmentHtml = "<div>Hello, World!</div>";
        Element context = new Element("html");
        String baseUri = "http://example.com";

        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
        Element div = (Element) nodes.get(0);

        assertEquals("div", div.tagName());
        assertEquals("Hello, World!", div.text());
    }

    @Test
    public void testParseFragmentWithTrackErrors() {
        String fragmentHtml = "<div>Hello, <script>World!</script></div>";
        Element context = new Element("html");
        String baseUri = "http://example.com";

        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(1);
        List<Node> nodes = parser.parseFragment(fragmentHtml, context, baseUri);

        List<ParseError> errors = parser.getErrors();
        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals("Error parsing fragment: Unexpected char 63 (script tag not closed)", error.getMessage());
        assertEquals("script", error.selector());
    }

    @Test
    public void testParseFragmentWithNoTrackErrors() {
        String fragmentHtml = "<div>Hello, <script>World!</script></div>";
        Element context = new Element("html");
        String baseUri = "http://example.com";

        Parser parser = new Parser(new HtmlTreeBuilder());
        List<Node> nodes = parser.parseFragment(fragmentHtml, context, baseUri);
        List<ParseError> errors = parser.getErrors();

        assertEquals(0, errors.size());
    }

    @Test
    public void testParseFragmentWithContext() {
        String fragmentHtml = "<button>Click me!</button>";
        Element context = new Element("body");
        String baseUri = "http://example.com";

        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
        Element button = (Element) nodes.get(0);

        assertEquals("button", button.tagName());
        assertEquals("Click me!", button.text());

        assertEquals(1, context.childNodes().size());
        Element body = context.childNodes().get(0);
        assertEquals("body", body.tagName());

        assertEquals(1, body.childNodes().size());
        assertEquals("button", body.childNodes().get(0).tagName());
    }

    @Test
    public void testParseFragmentWithContextAndTrackErrors() {
        String fragmentHtml = "<button>Click me!</button>";
        Element context = new Element("body");
        String baseUri = "http://example.com";

        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(1);
        List<Node> nodes = parser.parseFragment(fragmentHtml, context, baseUri);
        List<ParseError> errors = parser.getErrors();

        assertEquals(0, errors.size());

        Element button = (Element) nodes.get(0);
        assertEquals("button", button.tagName());
        assertEquals("Click me!", button.text());

        assertEquals(1, context.childNodes().size());
        Element body = context.childNodes().get(0);
        assertEquals("body", body.tagName());

        assertEquals(1, body.childNodes().size());
        assertEquals("button", body.childNodes().get(0).tagName());
    }
}