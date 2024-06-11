package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.ParseError;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.XmlTreeBuilder;

public class ParserTest {

    @Test
    public void testParseInput() {
        String html = "<html><head><title>Test</title></head><body><p>Hello World</p></body></html>";
        Parser parser = Parser.htmlParser();
        Document doc = parser.parseInput(html, null);
        Element title = doc.title();
        assertEquals("Test", title.text());
        assertEquals("Hello World", doc.body().text());

        String baseUri = "http://example.com";
        doc = parser.parseInput(html, baseUri);
        assertEquals(baseUri, doc.baseUri());

        // Test error tracking
        Parser parserWithErrors = new Parser(new HtmlTreeBuilder());
        parserWithErrors.setTrackErrors(1);
        String invalidHtml = "<html><body><p>Hello <script>alert('XSS')</script> World</p></body></html>";
        ParseErrorList errors = parserWithErrors.parseInput(invalidHtml, null).errors();
        assertEquals(1, errors.size());
        assertEquals("Error parsing start tag End Tag: script", errors.get(0).getMessage());
    }

    @Test
    public void testGetTreeBuilder() {
        Parser parser = Parser.htmlParser();
        assertEquals(HtmlTreeBuilder.class, parser.getTreeBuilder().getClass());
    }

    @Test
    public void testSetTreeBuilder() {
        Parser parser = Parser.htmlParser();
        TreeBuilder newTreeBuilder = new HtmlTreeBuilder();
        parser.setTreeBuilder(newTreeBuilder);
        assertEquals(newTreeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors() {
        Parser parser = Parser.htmlParser();
        assertFalse(parser.isTrackErrors());
        Parser parserWithErrors = new Parser(new HtmlTreeBuilder());
        parserWithErrors.setTrackErrors(1);
        assertTrue(parserWithErrors.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        Parser parser = Parser.htmlParser();
        parser.setTrackErrors(1);
        assertTrue(parser.isTrackErrors());
        parser.setTrackErrors(0);
        assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testGetErrors() {
        Parser parserWithErrors = new Parser(new HtmlTreeBuilder());
        parserWithErrors.setTrackErrors(1);
        String invalidHtml = "<html><body><p>Hello <script>alert('XSS')</script> World</p></body></html>";
        parseInputWithErrors(parserWithErrors, invalidHtml);
        List<ParseError> errors = parserWithErrors.getErrors();
        assertEquals(1, errors.size());
        assertEquals("Error parsing start tag End Tag: script", errors.get(0).getMessage());
    }

    @Test
    public void testSettings() {
        Parser parser = Parser.htmlParser();
        ParseSettings settings = parser.settings();
        settings.setElementNameTrim(true);
        parser = parser.settings(settings);
        String html = "<html><head><title>Test</title></head><body><p><textarea name='text'></textarea></p></body></html>";
        Document doc = parser.parseInput(html, null);
        Element title = doc.title();
        assertEquals("Test", title.text());
        Element textarea = doc.body().getElementsByTag("textarea").get(0);
        assertEquals("text", textarea.attr("name"));
    }

    @Test
    public void testParse() {
        Document doc = Parser.parse("<html><head><title>Test</title></head><body><p>Hello World</p></body></html>", null);
        Element title = doc.title();
        assertEquals("Test", title.text());
        assertEquals("Hello World", doc.body().text());
    }

    @Test
    public void testParseFragment() {
        String html = "<p>Hello World</p>";
        List<Node> nodes = Parser.parseFragment(html, null, null);
        Node node = nodes.get(0);
        assertTrue(node instanceof Element);
        Element p = (Element) node;
        assertEquals("p", p.tagName());
        assertEquals("Hello World", p.text());
    }

    @Test
    public void testParseFragmentWithContext() {
        String html = "<ul><li>Item 1</li><li>Item 2</li></ul>";
        Element parent = new Element("ul");
        List<Node> nodes = Parser.parseFragment(html, parent, null);
        assertEquals(2, nodes.size());
        for (Node node : nodes) {
            parent.appendChild(node);
        }
        assertEquals(2, parent.childNodes().size());
    }

    @Test
    public void testParseXmlFragment() {
        String xml = "<book><title>XML Test</title></book>";
        List<Node> nodes = Parser.parseXmlFragment(xml, null);
        Node node = nodes.get(0);
        assertTrue(node instanceof Element);
        Element book = (Element) node;
        assertEquals("book", book.tagName());
        assertEquals("XML Test", book.children().get(0).text());
    }

    @Test
    public void testParseBodyFragment() {
        String html = "<body><p>Hello World</p></body>";
        Document doc = Parser.parseBodyFragment(html, null);
        assertEquals("", doc.head().outerHtml());
        assertEquals("Hello World", doc.body().text());
    }

    @Test
    public void testUnescapeEntities() {
        String html = "<p>Hello &lt; world &gt;</p>";
        String unescaped = Parser.unescapeEntities(html, false);
        assertEquals("<p>Hello < world ></p>", unescaped);
        String unescapedAttribute = Parser.unescapeEntities("<a href='http://example.com/&abc;def' />", true);
        assertEquals("<a href='http://example.com/&abc;def' />", unescapedAttribute);
    }

    private void parseInputWithErrors(Parser parser, String html) {
        parser.parseInput(html, null);
    }
}