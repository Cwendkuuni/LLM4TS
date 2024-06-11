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
        String baseUri = "https://example.com";
        Parser parser = new Parser(new HtmlTreeBuilder());
        List<ParseError> errors = parser.parseInput(html, baseUri).errors();
        assertEquals(0, errors.size());
        Document doc = parser.parseInput(html, baseUri);
        assertEquals(1, doc.childNodes().size());

        Element body = doc.body();
        assertEquals(1, body.childNodes().size());
        Element h1 = body.child(0);
        assertEquals("Hello World", h1.text());
    }

    @Test
    public void testParseInputWithErrors() {
        String html = "<html><body><h1>Hello <script>World</script></h1></body></html>";
        String baseUri = "https://example.com";
        Parser parser = new Parser(new HtmlTreeBuilder());
        List<ParseError> errors = parser.parseInput(html, baseUri).errors();
        assertEquals(1, errors.size());
        Document doc = parser.parseInput(html, baseUri);
        assertNotNull(doc);
        assertEquals(1, doc.childNodes().size());
        Element body = doc.body();
        assertEquals(1, body.childNodes().size());
        Element h1 = body.child(0);
        assertNull(h1.text());
    }

    @Test
    public void testGetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        assertEquals(HtmlTreeBuilder.class, treeBuilder.getClass());
    }

    @Test
    public void testSetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder newTreeBuilder = new XmlTreeBuilder();
        parser.setTreeBuilder(newTreeBuilder);
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        assertEquals(XmlTreeBuilder.class, treeBuilder.getClass());
    }

    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(0);
        assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testGetErrors() {
        String html = "<html><body><h1>Hello <script>World</script></h1></body></html>";
        String baseUri = "https://example.com";
        Parser parser = new Parser(new HtmlTreeBuilder());
        List<ParseError> errors = parser.parseInput(html, baseUri).errors();
        List<ParseError> getErrors = parser.getErrors();
        assertEquals(errors, getErrors);
    }

    @Test
    public void testParse() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "https://example.com";
        Document doc = Parser.parse(html, baseUri);
        assertEquals(1, doc.childNodes().size());
        Element body = doc.body();
        assertEquals(1, body.childNodes().size());
        Element h1 = body.child(0);
        assertEquals("Hello World", h1.text());
    }

    @Test
    public void testParseFragment() {
        String fragmentHtml = "<h1>Hello World</h1>";
        Element context = new Element("div");
        String baseUri = "https://example.com";
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
        assertEquals(1, nodes.size());
        Element h1 = (Element) nodes.get(0);
        assertEquals("Hello World", h1.text());
    }

    @Test
    public void testParseXmlFragment() {
        String fragmentXml = "<xml><h1>Hello World</h1></xml>";
        String baseUri = "https://example.com";
        List<Node> nodes = Parser.parseXmlFragment(fragmentXml, baseUri);
        assertEquals(1, nodes.size());
        Element h1 = (Element) nodes.get(0);
        assertEquals("Hello World", h1.text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World</h1>";
        String baseUri = "https://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        assertEquals(1, doc.childNodes().size());
        Element body = doc.body();
        assertEquals(1, body.childNodes().size());
        Element h1 = body.child(0);
        assertEquals("Hello World", h1.text());
    }

    @Test
    public void testUnescapeEntities() {
        String string = "&lt;p&gt;<b>Hello &amp; World&lt;/b&gt;";
        String result = Parser.unescapeEntities(string, false);
        assertEquals("<p><b>Hello & World</b></p>", result);
    }
}