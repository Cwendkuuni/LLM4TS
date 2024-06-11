package org.jsoup.parser;

import static org.junit.Assert.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.ParseError;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParseInput() {
        String html = "<html><body><p>Hello World</p></body></html>";
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document document = parser.parseInput(html, null);
        assertEquals("Hello World", document.body().text());
    }

    @Test
    public void testParseInputWithTrackErrors() {
        String html = "<html><body><p>Hello World</p><script>alert('XSS');</script></body></html>";
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(1);
        Document document = parser.parseInput(html, null);
        List<ParseError> errors = parser.getErrors();
        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals(ParseError.TAG_START, error.getMessage());
        assertEquals("script", error.getTagName());
    }

    @Test
    public void testParseInputWithMaxErrors() {
        String html = "<html><body><p>Hello World</p><script>alert('XSS');</script></body></html>";
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(2);
        parser.setTreeBuilder(new HtmlTreeBuilder());
        Document document = parser.parseInput(html, null);
        List<ParseError> errors = parser.getErrors();
        assertEquals(2, errors.size());
        ParseError error2 = errors.get(1);
        assertEquals(ParseError.TAG_START, error2.getMessage());
        assertEquals("script", error2.getTagName());
        assertEquals(null, document.body().child(1));
    }

    @Test
    public void testParseInputWithBaseUri() {
        String html = "<img src=\"image.png\" alt=\"Image\">";
        String baseUri = "https://example.com";
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document document = parser.parseInput(html, baseUri);
        Element img = document.selectFirst("img");
        assertEquals("https://example.com/image.png", img.absUrl("src"));
    }

    @Test
    public void testParseInputWithDefaultBaseUri() {
        String html = "<img src=\"image.png\" alt=\"Image\">";
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document document = parser.parseInput(html, null);
        Element img = document.selectFirst("img");
        assertEquals("image.png", img.attr("src"));
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello World</p>";
        String baseUri = "https://example.com";
        Document document = Parser.parseBodyFragment(bodyHtml, baseUri);
        assertEquals("Hello World", document.body().text());
    }

    @Test
    public void testParseBodyFragmentWithBaseUri() {
        String bodyHtml = "<img src=\"image.png\" alt=\"Image\">";
        String baseUri = "https://example.com";
        Document document = Parser.parseBodyFragment(bodyHtml, baseUri);
        Element img = document.selectFirst("img");
        assertEquals("https://example.com/image.png", img.absUrl("src"));
    }

    @Test
    public void testParseFragment() {
        String fragmentHtml = "<p>Hello World</p>";
        Element context = Element.parse("<div></div>");
        String baseUri = "https://example.com";
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
        assertEquals(1, nodes.size());
        Node node = nodes.get(0);
        assertEquals(Element.TAG, node.nodeType());
        assertEquals("p", node.tagName());
        assertEquals("Hello World", node.text());
    }

    @Test
    public void testParseFragmentWithBaseUri() {
        String fragmentHtml = "<img src=\"image.png\" alt=\"Image\">";
        Element context = Element.parse("<div></div>");
        String baseUri = "https://example.com";
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
        assertEquals(1, nodes.size());
        Node node = nodes.get(0);
        assertEquals(Element.TAG, node.nodeType());
        assertEquals("img", node.tagName());
        assertEquals("https://example.com/image.png", node.absUrl("src"));
    }

    @Test
    public void testParseXmlFragment() {
        String fragmentXml = "<item><title>Item 1</title></item>";
        String baseUri = "https://example.com";
        List<Node> nodes = Parser.parseXmlFragment(fragmentXml, baseUri);
        assertEquals(1, nodes.size());
        Node node = nodes.get(0);
        assertEquals(Element.TAG, node.nodeType());
        assertEquals("item", node.tagName());
        Element title = (Element) node.child(0);
        assertEquals("Item 1", title.text());
    }

    @Test
    public void testUnescapeEntities() {
        String string = "&lt;p&gt;Hello &amp; World&lt;/p&gt;";
        String unescapedString = Parser.unescapeEntities(string, false);
        assertEquals("<p>Hello & World</p>", unescapedString);
    }

    @Test
    public void testUnescapeEntitiesInAttribute() {
        String string = "<input type=\"text\" value=\"&lt;p&gt;Hello &amp; World&lt;/p&gt;\">";
        String unescapedString = Parser.unescapeEntities(string, true);
        assertEquals("<input type=\"text\" value=\"<p>Hello & World</p>\">", unescapedString);
    }
}