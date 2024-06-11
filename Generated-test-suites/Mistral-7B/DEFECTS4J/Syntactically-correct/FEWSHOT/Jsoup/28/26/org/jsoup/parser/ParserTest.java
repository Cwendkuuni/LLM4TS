package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.util.List;

public class ParserTest {

    @Test
    public void testParseInput() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Parser parser = Parser.htmlParser();
        Document doc = parser.parseInput(html, null);
        assertNotNull(doc);
        assertTrue(doc.body().text().contains("Hello World"));
    }

    @Test
    public void testGetTreeBuilder() {
        Parser parser = new Parser(null);
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        assertNotNull(treeBuilder);
    }

    @Test
    public void testSetTreeBuilder() {
        Parser parser = new Parser(null);
        TreeBuilder newTreeBuilder = new HtmlTreeBuilder();
        parser.setTreeBuilder(newTreeBuilder);
        TreeBuilder oldTreeBuilder = parser.getTreeBuilder();
        assertSame(newTreeBuilder, oldTreeBuilder);
    }

    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(null);
        assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        Parser parser = new Parser(null);
        parser.setTrackErrors(5);
        assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testGetErrors() {
        Parser parser = new Parser(null);
        List<ParseError> errors = parser.getErrors();
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testParse() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        List<ParseError> errors = Parser.parse(html, null, ParseErrorList.tracking(1)).getErrors();
        assertTrue(errors.size() <= 1);
    }

    @Test(expected = NullPointerException.class)
    public void testParseWithNullBaseUri() {
        Parser.parse("", null);
    }

    @Test
    public void testParseFragment() {
        String fragmentHtml = "<p>Hello World</p>";
        List<Node> nodes = Parser.parseFragment(fragmentHtml, null, null);
        assertNotNull(nodes);
        assertTrue(nodes.size() > 0);
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<body><p>Hello World</p></body>";
        Document doc = Parser.parseBodyFragment(bodyHtml, null);
        assertNotNull(doc);
        Element body = doc.body();
        assertTrue(body.text().contains("Hello World"));
    }

    @Test(expected = NullPointerException.class)
    public void testParseBodyFragmentWithNullBaseUri() {
        Parser.parseBodyFragment("", null);
    }

    @Test
    public void testUnescapeHtmlEntities() {
        String html = "&lt;p&gt;Hello &amp; World&lt;/p&gt;";
        String unescaped = Parser.unescapeHtmlEntities(html);
        assertTrue(unescaped.contains("<p>Hello & World</p>"));
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<body><p>Hello World</p></body>";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, null);
        assertNotNull(doc);
        Element body = doc.body();
        assertTrue(body.text().contains("Hello World"));
    }
}