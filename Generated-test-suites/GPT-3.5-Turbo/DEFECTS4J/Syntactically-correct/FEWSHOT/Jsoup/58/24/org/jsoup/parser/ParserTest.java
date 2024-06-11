
package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.junit.Test;

public class ParserTest {
    
    @Test
    public void testGetTreeBuilder() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);
        assertEquals(treeBuilder, parser.getTreeBuilder());
    }
    
    @Test
    public void testSetTreeBuilder() {
        TreeBuilder treeBuilder1 = new HtmlTreeBuilder();
        TreeBuilder treeBuilder2 = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder1);
        parser.setTreeBuilder(treeBuilder2);
        assertEquals(treeBuilder2, parser.getTreeBuilder());
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
        parser.setTrackErrors(10);
        assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testGetErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        List<ParseError> errors = parser.getErrors();
        assertNotNull(errors);
        assertEquals(0, errors.size());

        parser.setTrackErrors(10);
        errors = parser.getErrors();
        assertNotNull(errors);
        assertEquals(0, errors.size());
    }

    @Test
    public void testSettings() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        ParseSettings settings = new ParseSettings(false);
        parser.settings(settings);
        assertEquals(settings, parser.settings());
    }

    @Test
    public void testParseInput() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        String html = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "https://www.example.com";
        Document document = parser.parseInput(html, baseUri);
        assertNotNull(document);
        assertEquals(html, document.html());
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testParse() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "https://www.example.com";
        Document document = Parser.parse(html, baseUri);
        assertNotNull(document);
        assertEquals(html, document.html());
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testParseFragment() {
        String fragmentHtml = "<h1>Hello World</h1>";
        Element context = Jsoup.parse("").body();
        String baseUri = "https://www.example.com";
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertEquals(fragmentHtml, nodes.get(0).outerHtml());
    }

    @Test
    public void testParseXmlFragment() {
        String fragmentXml = "<h1>Hello World</h1>";
        String baseUri = "https://www.example.com";
        List<Node> nodes = Parser.parseXmlFragment(fragmentXml, baseUri);
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertEquals(fragmentXml, nodes.get(0).outerHtml());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World</h1>";
        String baseUri = "https://www.example.com";
        Document document = Parser.parseBodyFragment(bodyHtml, baseUri);
        assertNotNull(document);
        assertEquals(bodyHtml, document.body().html());
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testUnescapeEntities() {
        String escaped = "&lt;h1&gt;Hello World&lt;/h1&gt;";
        String unescaped = "<h1>Hello World</h1>";
        assertEquals(unescaped, Parser.unescapeEntities(escaped, false));
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<h1>Hello World</h1>";
        String baseUri = "https://www.example.com";
        Document document = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        assertNotNull(document);
        assertEquals(bodyHtml, document.html());
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testHtmlParser() {
        Parser parser = Parser.htmlParser();
        assertNotNull(parser);
        assertTrue(parser.getTreeBuilder() instanceof HtmlTreeBuilder);
    }

    @Test
    public void testXmlParser() {
        Parser parser = Parser.xmlParser();
        assertNotNull(parser);
        assertTrue(parser.getTreeBuilder() instanceof XmlTreeBuilder);
    }
}
