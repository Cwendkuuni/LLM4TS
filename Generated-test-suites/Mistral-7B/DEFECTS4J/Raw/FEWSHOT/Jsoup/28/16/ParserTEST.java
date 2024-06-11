package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;

public class ParserTest {

    @Test
    public void testParseInput() {
        String html = "<html><body><div>foo</div></body></html>";
        String baseUri = "https://example.com";

        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput(html, baseUri);
        assertNotNull(doc);
        assertEquals("https://example.com", doc.baseUri());
        assertEquals(html, doc.html());

        Element div = doc.selectFirst("div");
        assertNotNull(div);
        assertEquals("foo", div.text());
    }

    @Test
    public void testParseInputWithTrackingErrors() {
        String html = "<html><body><div>foo</div></body></html>";
        String baseUri = "https://example.com";
        int maxErrors = 1;

        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(maxErrors);

        Document doc = parser.parseInput(html, baseUri);
        assertNotNull(doc);
        assertEquals("https://example.com", doc.baseUri());
        assertEquals(html, doc.html());

        List<ParseError> errors = parser.getErrors();
        assertNotNull(errors);
        assertEquals(0, errors.size());
    }

    @Test
    public void testParseInputWithErrors() {
        String html = "<html><body><div>foo<br></div></body></html>";
        String baseUri = "https://example.com";
        int maxErrors = 1;

        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(maxErrors);

        Document doc = parser.parseInput(html, baseUri);
        assertNotNull(doc);
        assertEquals("https://example.com", doc.baseUri());
        assertEquals(html, doc.html());

        List<ParseError> errors = parser.getErrors();
        assertNotNull(errors);
        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals(ParseError.BR_INSIDE_TAG, error.getErrorCode());
        assertEquals("Unexpected tag start: br", error.getMessage());
    }

    @Test
    public void testParseInputWithMaxErrorsReached() {
        String html = "<html><body><div>foo<br></div></body></html>";
        String baseUri = "https://example.com";
        int maxErrors = 1;

        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(maxErrors);

        Document doc = null;
        for (int i = 0; i < 3; i++) {
            doc = parser.parseInput(html, baseUri);
        }
        assertNull(doc);

        List<ParseError> errors = parser.getErrors();
        assertNotNull(errors);
        assertEquals(3, errors.size());
    }

    @Test
    public void testParseInputWithNullHtml() {
        String baseUri = "https://example.com";
        Parser parser = new Parser(new HtmlTreeBuilder());

        Document doc = parser.parseInput(null, baseUri);
        assertNull(doc);
    }

    @Test
    public void testParseInputWithEmptyHtml() {
        String baseUri = "https://example.com";
        Parser parser = new Parser(new HtmlTreeBuilder());

        Document doc = parser.parseInput("", baseUri);
        assertNotNull(doc);
        assertEquals("", doc.html());
    }

    @Test
    public void testParseInputWithNoBaseUri() {
        String html = "<html><body><div>foo</div></body></html>";
        Parser parser = new Parser(new HtmlTreeBuilder());

        Document doc = parser.parseInput(html, null);
        assertNotNull(doc);
        assertNull(doc.baseUri());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<div>foo</div>";
        String baseUri = "https://example.com";

        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        assertNotNull(doc);
        assertEquals(bodyHtml, doc.body().html());
    }

    @Test
    public void testParseBodyFragmentWithNullBodyHtml() {
        String baseUri = "https://example.com";

        Document doc = Parser.parseBodyFragment(null, baseUri);
        assertNull(doc);
    }

    @Test
    public void testParseBodyFragmentWithEmptyBodyHtml() {
        String baseUri = "https://example.com";

        Document doc = Parser.parseBodyFragment("", baseUri);
        assertNotNull(doc);
        assertEquals("", doc.body().html());
    }

    @Test
    public void testParseFragment() {
        String fragmentHtml = "<div>foo</div>";
        Element context = new Element("html");
        String baseUri = "https://example.com";

        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
        assertNotNull(nodes);
        assertEquals(1, nodes.size());

        Node node = nodes.get(0);
        assertEquals(Element.class, node.getClass());
        Element element = (Element) node;
        assertEquals("div", element.tagName());
        assertEquals("foo", element.text());

        assertEquals(context, element.parent());
        assertEquals(baseUri, element.baseUri());
    }

    @Test
    public void testParseFragmentWithNullFragmentHtml() {
        Element context = new Element("html");
        String baseUri = "https://example.com";

        List<Node> nodes = Parser.parseFragment(null, context, baseUri);
        assertNotNull(nodes);
        assertEquals(0, nodes.size());
    }

    @Test
    public void testParseFragmentWithEmptyFragmentHtml() {
        Element context = new Element("html");
        String baseUri = "https://example.com";

        List<Node> nodes = Parser.parseFragment("", context, baseUri);
        assertNotNull(nodes);
        assertEquals(0, nodes.size());
    }

    @Test
    public void testParseInputWithHtmlParser() {
        String html = "<html><body><div>foo</div></body></html>";
        String baseUri = "https://example.com";

        Parser parser = Parser.htmlParser();
        Document doc = parser.parseInput(html, baseUri);
        assertNotNull(doc);
        assertEquals("https://example.com", doc.baseUri());
        assertEquals(html, doc.html());

        Element div = doc.selectFirst("div");
        assertNotNull(div);
        assertEquals("foo", div.text());
    }

    @Test
    public void testParseInputWithXmlParser() {
        String xml = "<root><element>foo</element></root>";
        String baseUri = "https://example.com";

        Parser parser = Parser.xmlParser();
        Document doc = parser.parseInput(xml, baseUri);
        assertNotNull(doc);
        assertEquals("https://example.com", doc.baseUri());
        assertEquals(xml, doc.html());

        Element element = doc.selectFirst("element");
        assertNotNull(element);
        assertEquals("foo", element.text());
    }

    @Test
    public void testParseBodyFragmentWithRelaxedParsing() {
        String bodyHtml = "<div><script>alert('Hello World')</script>foo</div>";
        String baseUri = "https://example.com";

        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        assertNotNull(doc);
        assertEquals("", doc.body().html());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testParseBodyFragmentRelaxedDeprecated() {
        String bodyHtml = "<div><script>alert('Hello World')</script>foo</div>";
        String baseUri = "https://example.com";

        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
    }
}