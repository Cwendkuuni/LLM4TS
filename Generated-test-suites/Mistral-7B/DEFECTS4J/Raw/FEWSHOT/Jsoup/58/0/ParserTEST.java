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
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput(html, null);
        assertEquals(html, doc.html());
        assertEquals("Hello World", doc.select("h1").text());
    }

    @Test
    public void testParseInputWithTrackErrors() {
        String html = "<html><body><h1>Hello World</h1><script>alert('Hello')</script></body></html>";
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(1);
        List<ParseError> errors = parser.parseInput(html, null).errors();
        assertEquals(1, errors.size());
        assertEquals("Error parsing at line 3 column 9: Unexpected tag 'script'", errors.get(0).getMessage());
    }

    @Test
    public void testParseInputWithMaxErrors() {
        String html = "<html><body><h1>Hello World</h1><script>alert('Hello')</script><script>alert('Hello')</script></body></html>";
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(2);
        List<ParseError> errors = parser.parseInput(html, null).errors();
        assertEquals(2, errors.size());
    }

    @Test
    public void testParseInputWithNullHtml() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        List<ParseError> errors = parser.parseInput(null, null).errors();
        assertNull(errors);
    }

    @Test
    public void testParseInputWithEmptyHtml() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput("", null);
        assertTrue(doc.empty());
    }

    @Test
    public void testGetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        assertEquals(new HtmlTreeBuilder(), parser.getTreeBuilder());
    }

    @Test
    public void testSetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder treeBuilder = new XmlTreeBuilder();
        parser.setTreeBuilder(treeBuilder);
        assertEquals(treeBuilder, parser.getTreeBuilder());
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
        parser.setTrackErrors(1);
        assertTrue(parser.isTrackErrors());

        parser.setTrackErrors(0);
        assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testGetErrors() {
        String html = "<html><body><h1>Hello World</h1><script>alert('Hello')</script></body></html>";
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(1);
        List<ParseError> errors = parser.parseInput(html, null).errors();
        List<ParseError> errorsFromParser = parser.getErrors();
        assertEquals(errors, errorsFromParser);
    }

    @Test
    public void testStaticParse() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Document doc = Parser.parse(html, null);
        assertEquals(html, doc.html());
        assertEquals("Hello World", doc.select("h1").text());
    }

    @Test
    public void testStaticParseFragment() {
        String fragmentHtml = "<p>Hello World</p>";
        Element context = new Element("div");
        Document doc = Parser.parseFragment(fragmentHtml, context, null).get(0).ownerDocument();
        assertEquals("Hello World", doc.select("p").text());
    }

    @Test
    public void testStaticParseXmlFragment() {
        String fragmentXml = "<xml><tag>Hello World</tag></xml>";
        Document doc = Parser.parseXmlFragment(fragmentXml, null);
        assertEquals("Hello World", doc.select("tag").text());
    }

    @Test
    public void testStaticParseBodyFragment() {
        String bodyHtml = "<body><p>Hello World</p></body>";
        Document doc = Parser.parseBodyFragment(bodyHtml, null);
        assertEquals("<html><body><p>Hello World</p></body></html>", doc.html());
    }

    @Test
    public void testStaticUnescapeEntities() {
        String html = "&lt;p&gt;Hello &amp; World&lt;/p&gt;";
        String unescapedHtml = Parser.unescapeEntities(html, false);
        assertEquals("<p>Hello & World</p>", unescapedHtml);
    }

    @Test(expected = NullPointerException.class)
    public void testStaticParseBodyFragmentNullHtml() {
        Parser.parseBodyFragment(null, null);
    }
}