package org.jsoup.parser;

import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;

import java.util.ArrayList;
import java.util.List;

public class ParserTest {
    @Test
    public void testParseInput() {
        String html = "<html><head></head><body><div>Test</div></body></html>";
        String baseUri = "https://example.com";
        Parser parser = new Parser(new HtmlTreeBuilder());
        List<ParseError> errors = new ArrayList<>();

        Document document = parser.parseInput(html, baseUri);
        assertEquals("Test", document.body().text());
        assertEquals(0, errors.size());
    }

    @Test
    public void testParseInputWithErrors() {
        String html = "<html><body><div>Test<script>alert('Hello')</script></div></body></html>";
        String baseUri = "https://example.com";
        Parser parser = new Parser(new HtmlTreeBuilder());
        List<ParseError> errors = ParseErrorList.tracking(5);

        Document document = parser.parseInput(html, baseUri);
        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals("Error parsing start tag : script", error.getMessage());
        assertEquals("script", error.getLine(), 1);
        assertEquals(13, error.getColumn(), 1);
        assertEquals("https://example.com:13", error.getAbsolvedUrl(), "https://example.com:13");
    }

    @Test
    public void testSetTreeBuilder() {
        TreeBuilder treeBuilder = new XmlTreeBuilder();
        Parser parser = new Parser(new HtmlTreeBuilder());
        Parser parserWithXmlTreeBuilder = parser.setTreeBuilder(treeBuilder);
        assertEquals(treeBuilder, parserWithXmlTreeBuilder.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        assertFalse(parser.isTrackErrors());

        parser = parser.setTrackErrors(5);
        assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testParse() {
        String html = "<html><head></head><body><div>Test</div></body></html>";
        String baseUri = "https://example.com";
        Document document = Parser.parse(html, baseUri);
        assertEquals("Test", document.body().text());
    }

    @Test
    public void testParseFragment() {
        String fragmentHtml = "<div>Test</div>";
        Element context = new Element("html");
        String baseUri = "https://example.com";
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
        assertEquals(1, nodes.size());
        assertEquals(Element.class, nodes.get(0).getClass());
        assertEquals("Test", nodes.get(0).text());
    }

    @Test
    public void testParseXmlFragment() {
        String fragmentXml = "<xml><test>Test</test></xml>";
        String baseUri = "https://example.com";
        List<Node> nodes = Parser.parseXmlFragment(fragmentXml, baseUri);
        assertEquals(1, nodes.size());
        assertEquals(Element.class, nodes.get(0).getClass());
        assertEquals("Test", nodes.get(0).child(0).text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<body><div>Test</div></body>";
        String baseUri = "https://example.com";
        Document document = Parser.parseBodyFragment(bodyHtml, baseUri);
        assertEquals("Test", document.body().text());
    }

    @Test
    public void testUnescapeEntities() {
        String html = "&lt;Test&gt;";
        String string = Parser.unescapeEntities(html, false);
        assertEquals("<Test>", string);
    }
}