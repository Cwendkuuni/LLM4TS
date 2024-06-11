package org.jsoup.parser;

import static org.junit.Assert.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.ParseErrorList;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParseInput() {
        String html = "<html><body><h1>Hello</h1></body></html>";
        String baseUri = "http://example.com";
        Parser parser = new Parser(new HtmlTreeBuilder());
        List<ParseError> errors = ParseErrorList.tracking(5);

        Document doc = parser.parseInput(html, baseUri);
        List<ParseError> parseErrors = parser.getErrors();

        assertEquals(0, parseErrors.size());
        assertEquals(1, doc.childNodes().size());
        assertEquals("Hello", doc.body().childNodes().get(0).text());
    }

    @Test
    public void testParseInputWithTrackedErrors() {
        String html = "<html><body><h1>Hello</h1><script>1</script></body></html>";
        String baseUri = "http://example.com";
        Parser parser = new Parser(new HtmlTreeBuilder());
        List<ParseError> errors = ParseErrorList.tracking(3);

        Document doc = parser.parseInput(html, baseUri);
        List<ParseError> parseErrors = parser.getErrors();

        assertEquals(1, parseErrors.size());
        assertEquals("Unexpected char: 1 (script start tag)", parseErrors.get(0).getMessage());
        assertEquals(1, doc.childNodes().size());
        assertEquals("Hello", doc.body().childNodes().get(0).text());
    }

    @Test(expected = NullPointerException.class)
    public void testParseInputWithNullHtml() {
        String baseUri = "http://example.com";
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.parseInput(null, baseUri);
    }

    @Test(expected = NullPointerException.class)
    public void testParseInputWithNullBaseUri() {
        String html = "<html><body><h1>Hello</h1></body></html>";
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.parseInput(html, null);
    }

    @Test
    public void testGetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder treeBuilder = parser.getTreeBuilder();

        assertTrue(treeBuilder instanceof HtmlTreeBuilder);
    }

    @Test
    public void testSetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder newTreeBuilder = new XmlTreeBuilder();

        parser.setTreeBuilder(newTreeBuilder);
        TreeBuilder treeBuilder = parser.getTreeBuilder();

        assertTrue(treeBuilder instanceof XmlTreeBuilder);
    }

    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        assertTrue(parser.isTrackErrors());

        parser = new Parser(new HtmlTreeBuilder()) {{
            this.maxErrors = 0;
        }};
        assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(3);
        assertTrue(parser.isTrackErrors());

        parser = new Parser(new HtmlTreeBuilder()) {{
            this.maxErrors = 0;
        }};
        parser.setTrackErrors(0);
        assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testGetErrors() {
        String html = "<html><body><h1>Hello</h1><script>1</script></body></html>";
        String baseUri = "http://example.com";
        Parser parser = new Parser(new HtmlTreeBuilder());
        List<ParseError> errors = ParseErrorList.tracking(3);

        parser.parseInput(html, baseUri);
        List<ParseError> parseErrors = parser.getErrors();

        assertEquals(1, parseErrors.size());
        assertEquals("Unexpected char: 1 (script start tag)", parseErrors.get(0).getMessage());
    }

    @Test
    public void testSetSettings() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        ParseSettings settings = new ParseSettings();

        parser.settings(settings);
        ParseSettings parsedSettings = parser.settings();

        assertEquals(settings, parsedSettings);
    }

    @Test
    public void testStaticParse() {
        String html = "<html><body><h1>Hello</h1></body></html>";
        String baseUri = "http://example.com";

        Document doc = Parser.parse(html, baseUri);
        assertEquals(1, doc.childNodes().size());
        assertEquals("Hello", doc.body().childNodes().get(0).text());
    }

    @Test
    public void testStaticParseFragment() {
        String fragmentHtml = "<div>Hello</div>";
        Element context = new Element("body");
        String baseUri = "http://example.com";

        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
        assertEquals(1, nodes.size());
        Node node = nodes.get(0);
        assertEquals("div", node.tagName());
        assertEquals("Hello", node.childNodes().get(0).text());
    }

    @Test
    public void testStaticParseXmlFragment() {
        String fragmentXml = "<xml><data>Hello</data></xml>";
        String baseUri = "http://example.com";

        List<Node> nodes = Parser.parseXmlFragment(fragmentXml, baseUri);
        assertEquals(1, nodes.size());
        Node node = nodes.get(0);
        assertEquals("data", node.tagName());
        assertEquals("Hello", node.childNodes().get(0).text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<div>Hello</div>";
        String baseUri = "http://example.com";

        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        assertEquals(1, doc.childNodes().size());
        assertEquals("div", doc.body().childNodes().get(0).tagName());
        assertEquals("Hello", doc.body().childNodes().get(0).childNodes().get(0).text());
    }

    @Test
    public void testUnescapeEntities() {
        String string = "&lt;Hello&gt; &amp; &apos; &quot; &eacute;";
        String unescapedString = Parser.unescapeEntities(string, false);
        assertEquals("<Hello>& & ' " + Character.toString((char) 233) + "", unescapedString);

        string = "<script>alert('Hello')</script>";
        unescapedString = Parser.unescapeEntities(string, true);
        assertEquals("alert('Hello')", unescapedString);
    }

    @Test(expected = NullPointerException.class)
    public void testParseBodyFragmentWithNullBodyHtml() {
        String baseUri = "http://example.com";
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseBodyFragment(null, baseUri);
    }

    @Test(expected = NullPointerException.class)
    public void testParseBodyFragmentWithNullBaseUri() {
        String bodyHtml = "<html><body><h1>Hello</h1></body></html>";
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.parseBodyFragment(bodyHtml, null);
    }
}