package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parse.Parser;
import org.jsoup.parse.TreeBuilder;
import org.jsoup.parse.ParseError;
import org.jsoup.parse.ParseErrorList;

public class ParserTest {

    @Test
    public void testParser() {
        Parser parser = Parser.htmlParser();
        Document doc = parser.parseInput("<html><body><h1>Hello World</h1></body></html>", null);
        assertNotNull(doc);
        assertEquals("html", doc.tagName());
        assertEquals("HEAD", doc.head().tagName());
        Element body = doc.body();
        assertEquals("h1", body.tagName());
        assertEquals("Hello World", body.text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<ul><li>Item 1</li><li>Item 2</li></ul>";
        Parser parser = Parser.htmlParser();
        Document doc = parser.parseBodyFragment(bodyHtml, null);
        assertNotNull(doc);
        assertEquals("html", doc.tagName());
        Element body = doc.body();
        assertEquals("ul", body.tagName());
        List<Node> list = body.getElementsByTag("li");
        assertEquals(2, list.size());
        assertEquals("Item 1", list.get(0).text());
        assertEquals("Item 2", list.get(1).text());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseBodyFragmentNullBodyHtml() {
        Parser parser = Parser.htmlParser();
        parser.parseBodyFragment(null, null);
    }

    @Test
    public void testParseFragment() {
        String fragmentHtml = "<ul><li>Item 1</li><li>Item 2</li></ul>";
        Parser parser = Parser.htmlParser();
        List<Node> nodes = parser.parseFragment(fragmentHtml, null, null);
        assertNotNull(nodes);
        assertEquals(2, nodes.size());
        Element ul = (Element) nodes.get(0);
        assertEquals("ul", ul.tagName());
        List<Node> list = ul.getElementsByTag("li");
        assertEquals(2, list.size());
        assertEquals("Item 1", list.get(0).text());
        assertEquals("Item 2", list.get(1).text());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseFragmentNullFragmentHtml() {
        Parser parser = Parser.htmlParser();
        parser.parseFragment(null, null, null);
    }

    @Test
    public void testParseInput() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Parser parser = Parser.htmlParser();
        Document doc = parser.parseInput(html, null);
        assertNotNull(doc);
        assertEquals("html", doc.tagName());
        assertEquals("HEAD", doc.head().tagName());
        Element body = doc.body();
        assertEquals("h1", body.tagName());
        assertEquals("Hello World", body.text());
    }

    @Test
    public void testParseInputWithErrors() {
        String html = "<html><body><h1>Hello <script>World</script></h1></body></html>";
        Parser parser = Parser.htmlParser();
        Document doc = parser.parseInput(html, null);
        assertNotNull(doc);
        assertSame(ParseErrorList.class, parser.getErrors().getClass());
        List<ParseError> errors = parser.getErrors();
        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals(13, error.getLine());
        assertEquals(1, error.getColumn());
        assertEquals("The start tag <script> was not closed properly", error.getMessage());
    }

    @Test
    public void testSetTreeBuilder() {
        TreeBuilder treeBuilder1 = new HtmlTreeBuilder();
        Parser parser1 = new Parser(treeBuilder1);

        TreeBuilder treeBuilder2 = new XmlTreeBuilder();
        Parser parser2 = new Parser(treeBuilder2);

        parser1.setTreeBuilder(treeBuilder2);
        assertEquals(treeBuilder2, parser1.getTreeBuilder());
    }

    @Test
    public void testTrackErrors() {
        String html = "<html><body><h1>Hello <script>World</script></h1></body></html>";
        Parser parser = Parser.htmlParser();
        parser.setTrackErrors(5);
        Document doc = parser.parseInput(html, null);
        assertEquals(5, parser.getErrors().size());
        List<ParseError> errors = parser.getErrors();
        assertEquals(1, errors.get(0).getLine());
        assertEquals(13, errors.get(1).getLine());
        assertEquals("The start tag <script> was not closed properly", errors.get(0).getMessage());
        assertEquals("Unexpected character '<' (code 60): expected a name", errors.get(1).getMessage());
    }

    @Test
    public void testParseInputWithBaseUri() {
        String html = "<img src=\"image.jpg\" alt=\"Image\">";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document doc = parser.parseInput(html, baseUri);
        assertEquals("http://example.com/image.jpg", doc.imageUri("image.jpg"));
    }

    @Test
    public void testParseInputWithTrailingSlash() {
        String html = "<html><head><title>Hello</title></head><body><a href=\"/\">Home</a></body></html>";
        Parser parser = Parser.htmlParser();
        Document doc = parser.parseInput(html, "/");
        Element link = doc.select("a").first();
        assertEquals("/", link.attr("href"));
    }

    @Test
    public void testParseInputWithFragment() {
        String html = "<html><head><title>Hello</title></head><body><a name=\"foo\"></a><p id=\"bar\"></p></body></html>";
        Parser parser = Parser.htmlParser();
        Document doc = parser.parseInput(html, null);
        Element link = doc.select("a[name=foo]").first();
        Element p = doc.select("p[id=bar]").first();
        assertEquals("foo", link.attr("name"));
        assertEquals("bar", p.attr("id"));
    }

    @Test
    public void testParseInputWithEmptyString() {
        Parser parser = Parser.htmlParser();
        Document doc = parser.parseInput("", null);
        assertEquals("", doc.outerHtml());
    }

    @Test
    public void testParseInputWithNull() {
        Parser parser = Parser.htmlParser();
        assertNull(parser.parseInput(null, null));
    }
}