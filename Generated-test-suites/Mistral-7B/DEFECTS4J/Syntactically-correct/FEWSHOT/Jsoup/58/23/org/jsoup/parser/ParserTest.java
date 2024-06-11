package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;

import java.util.ArrayList;
import java.util.List;

public class ParserTest {

    @Test
    public void testParseInput() {
        String html = "<html><head></head><body><p>Hello, World!</p></body></html>";
        String baseUri = "http://example.com/";

        Parser parser = new Parser(new HtmlTreeBuilder());
        Document document = parser.parseInput(html, baseUri);

        assertEquals(html, document.outerHtml());
        assertNotNull(document.head());
        assertEquals(1, document.body().childNodeSize());
        assertEquals("Hello, World!", document.body().text());
    }

    @Test
    public void testParseInputWithErrors() {
        String html = "<html><body><script>throw new Error('Test Error')</script></body></html>";
        String baseUri = "http://example.com/";

        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(1);
        Document document = parser.parseInput(html, baseUri);

        assertNotNull(document);
        List<ParseError> errors = parser.getErrors();
        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals(ParseError.SYNTAX_ERR, error.getType());
        assertEquals("Test Error", error.getMessage());
    }

    @Test
    public void testParseInputNoErrors() {
        String html = "<html><body></body></html>";
        String baseUri = "http://example.com/";

        Parser parser = new Parser(new HtmlTreeBuilder());
        Document document = parser.parseInput(html, baseUri);

        assertNotNull(document);
        List<ParseError> errors = parser.getErrors();
        assertNull(errors);
    }

    @Test
    public void testParseInputNullHtml() {
        String baseUri = "http://example.com/";
        Parser parser = new Parser(new HtmlTreeBuilder());

        assertNull(parser.parseInput(null, baseUri));
    }

    @Test
    public void testParseInputEmptyHtml() {
        String baseUri = "http://example.com/";

        Parser parser = new Parser(new HtmlTreeBuilder());
        Document document = parser.parseInput("", baseUri);

        assertNotNull(document);
        assertEquals("", document.outerHtml());
        List<ParseError> errors = parser.getErrors();
        assertNull(errors);
    }

    @Test
    public void testParseInputWithBaseUri() {
        String html = "<img src=\"image.jpg\" alt=\"Image\">";
        String baseUri = "http://example.com/";

        Parser parser = new Parser(new HtmlTreeBuilder());
        Document document = parser.parseInput(html, baseUri);

        assertEquals("<img src=\"image.jpg\" alt=\"Image\">", document.outerHtml());
        Element img = document.selectFirst("img");
        assertEquals("image.jpg", img.attr("src"));
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello, World!</p>";
        String baseUri = "http://example.com/";

        Document document = Parser.parseBodyFragment(bodyHtml, baseUri);
        assertEquals("<html><head></head><body>" + bodyHtml + "</body></html>", document.outerHtml());
    }

    @Test
    public void testParseBodyFragmentEmpty() {
        String baseUri = "http://example.com/";

        Document document = Parser.parseBodyFragment("", baseUri);
        assertEquals("<html><head></head><body></body></html>", document.outerHtml());
    }

    @Test
    public void testParseFragment() {
        String fragmentHtml = "<p>Hello, World!</p>";
        String baseUri = "http://example.com/";
        Element context = Jsoup.parse("<html><body></body></html>").body();

        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
        assertEquals(1, nodes.size());
        Node node = nodes.get(0);
        assertEquals(Element.class, node.getClass());
        assertEquals("p", node.tagName());
        assertEquals("Hello, World!", node.text());
    }

    @Test
    public void testParseFragmentWithErrors() {
        String fragmentHtml = "<script>throw new Error('Test Error')</script>";
        String baseUri = "http://example.com/";
        Element context = Jsoup.parse("<html><body></body></html>").body();
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(1);

        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
        assertEquals(0, nodes.size());
        List<ParseError> errors = parser.getErrors();
        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals(ParseError.SYNTAX_ERR, error.getType());
        assertEquals("Test Error", error.getMessage());
    }

    @Test
    public void testParseXmlFragment() {
        String fragmentXml = "<book><title>Harry Potter and the Philosopher's Stone</title></book>";
        String baseUri = "http://example.com/";

        List<Node> nodes = Parser.parseXmlFragment(fragmentXml, baseUri);
        assertEquals(1, nodes.size());
        Node node = nodes.get(0);
        assertEquals(Element.class, node.getClass());
        assertEquals("book", node.tagName());
        Element title = (Element) node.child(0);
        assertEquals("Harry Potter and the Philosopher's Stone", title.text());
    }

    @Test
    public void testSetTreeBuilder() {
        String html = "<html><head></head><body><p>Hello, World!</p></body></html>";
        String baseUri = "http://example.com/";

        Parser parser = new Parser(new HtmlTreeBuilder());
        Document document = parser.parseInput(html, baseUri);

        TreeBuilder newTreeBuilder = new XmlTreeBuilder();
        parser.setTreeBuilder(newTreeBuilder);

        Document newDocument = parser.parseInput(html, baseUri);
        assertEquals("<xml><html><head></head><body><p>Hello, World!</p></body></html></xml>", newDocument.outerHtml());
    }

    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        assertFalse(parser.isTrackErrors());

        parser.setTrackErrors(1);
        assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrorsZero() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(0);
        assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrorsPositive() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(5);
        assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testGetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        assertEquals(HtmlTreeBuilder.class, treeBuilder.getClass());
    }

    @Test
    public void testSetParseSettings() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        ParseSettings settings = new ParseSettings();
        settings.setTagMode(TagMode.STRICT);
        parser.settings(settings);

        String html = "<div><p>Hello, World!</p></div>";
        String baseUri = "http://example.com/";

        Document document = parser.parseInput(html, baseUri);
        assertEquals("<html><head></head><body><div><p>Hello, World!</p></div></body></html>", document.outerHtml());
    }
}