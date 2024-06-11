package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.ParseError;
import org.jsoup.parse.ParseErrorList;
import org.jsoup.parse.TreeBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * JUnit test file for Parser class.
 */
public class ParserTest {

    @Test
    public void testParseInput() {
        // Given
        String html = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);

        // When
        Document doc = parser.parseInput(html, baseUri);

        // Then
        Assert.assertNotNull(doc);
        Assert.assertEquals("<html><head></head><body><h1>Hello World</h1></body></html>", doc.html());
        Assert.assertEquals("Hello World", doc.title());
    }

    @Test
    public void testTrackErrors() {
        // Given
        String html = "<html><body><h1><script>throw new Error('Test Error')</script></h1></body></html>";
        String baseUri = "http://example.com";
        Parser parser = new Parser(new HtmlTreeBuilder());

        // When
        List<ParseError> errors = parser.parseInput(html, baseUri).getErrors();

        // Then
        Assert.assertEquals(1, errors.size());
        Assert.assertEquals("Error parsing startTag: 'h1'", errors.get(0).getMessage());
        Assert.assertEquals("Test Error", errors.get(0).getCause().getMessage());
    }

    @Test
    public void testTrackErrorsLimit() {
        // Given
        String html = "<html><body>";
        String baseUri = "http://example.com";
        Parser parser = new Parser(new HtmlTreeBuilder());

        // When
        parser.setTrackErrors(5);
        for (int i = 0; i < 10; i++) {
            List<ParseError> errors = parser.parseInput(html, baseUri).getErrors();
        }

        // Then
        List<ParseError> errors = parser.parseInput(html, baseUri).getErrors();
        Assert.assertEquals(5, errors.size());
    }

    @Test
    public void testParseFragment() {
        // Given
        String fragmentHtml = "<div><p>Hello World</p></div>";
        Element context = new Element("html");
        String baseUri = "http://example.com";
        Parser parser = new Parser(new HtmlTreeBuilder());

        // When
        List<Node> nodes = parser.parseFragment(fragmentHtml, context, baseUri);

        // Then
        Assert.assertNotNull(nodes);
        Assert.assertEquals(2, nodes.size());
        Element div = (Element) nodes.get(0);
        Element p = (Element) nodes.get(1);
        Assert.assertEquals("div", div.tagName());
        Assert.assertEquals("p", p.tagName());
        Assert.assertEquals("Hello World", p.text());
    }

    @Test
    public void testParseBodyFragment() {
        // Given
        String bodyHtml = "<body><p>Hello World</p></body>";
        String baseUri = "http://example.com";
        Parser parser = new Parser(new HtmlTreeBuilder());

        // When
        Document doc = parser.parseBodyFragment(bodyHtml, baseUri);

        // Then
        Assert.assertNotNull(doc);
        Assert.assertEquals(1, doc.numberOfChildren());
        Element body = doc.body();
        Assert.assertEquals("p", body.tagName());
        Assert.assertEquals("Hello World", body.text());
    }

    @Test
    public void testUnescapeHtmlEntities() {
        // Given
        String string = "&lt;p&gt;Hello World&amp; &nbsp; &rarr; &gt;";

        // When
        String unescapedString = Parser.unescapeHtml(string);

        // Then
        Assert.assertEquals("<p>Hello World&amp;   >", unescapedString);
    }

    @Test
    public void testParse() {
        // Given
        String html = "<html><body><h1>Hello World</h1></body></html>";

        // When
        Document doc = Parser.parse(html, "http://example.com");

        // Then
        Assert.assertNotNull(doc);
        Assert.assertEquals("<html><head></head><body><h1>Hello World</h1></body></html>", doc.html());
        Assert.assertEquals("Hello World", doc.title());
    }

    @Test
    public void testParseFragmentRelaxed() {
        // Given
        String fragmentHtml = "<body><p>Hello World</p></body>";

        // When
        Document doc = Parser.parseBodyFragmentRelaxed(fragmentHtml, "http://example.com");

        // Then
        Assert.assertNotNull(doc);
        Assert.assertEquals(1, doc.numberOfChildren());
        Element body = doc.body();
        Assert.assertEquals("p", body.tagName());
        Assert.assertEquals("Hello World", body.text());
    }

    @Test
    public void testHtmlParser() {
        // Given
        String html = "<html><body><h1>Hello World</h1></body></html>";

        // When
        Parser parser = Parser.htmlParser();
        Document doc = parser.parseInput(html, "http://example.com");

        // Then
        Assert.assertNotNull(doc);
        Assert.assertEquals("<html><head></head><body><h1>Hello World</h1></body></html>", doc.html());
        Assert.assertEquals("Hello World", doc.title());
    }

    @Test
    public void testXmlParser() {
        // Given
        String xml = "<xml><element>Hello World</element></xml>";

        // When
        Parser parser = Parser.xmlParser();
        Document doc = parser.parseInput(xml, "http://example.com");

        // Then
        Assert.assertNotNull(doc);
        Assert.assertEquals("<xml><element>Hello World</element></xml>", doc.html());
        Assert.assertNull(doc.title());
    }
}