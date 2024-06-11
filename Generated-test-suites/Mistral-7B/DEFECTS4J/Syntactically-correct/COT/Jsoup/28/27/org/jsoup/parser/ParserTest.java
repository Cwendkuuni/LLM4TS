package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.junit.Test;

/**
 * Test class for Parser class.
 */
public class ParserTest {

    private final Parser parser = new Parser(new HtmlTreeBuilder());
    private final Parser parserWithTrackingErrors = new Parser(new HtmlTreeBuilder());
    private final Parser parserWithMaxErrors = new Parser(new HtmlTreeBuilder());

    @Test
    public void testParserConstructor() {
        assertNotNull(parser);
    }

    @Test
    public void testParseInput() {
        String html = "<html><head></head><body><p>Test</p></body></html>";
        Document doc = parser.parseInput(html, "http://example.com");
        assertNotNull(doc);
        assertNotNull(doc.body());
        assertTrue(doc.body().text().contains("Test"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInputNullHtml() {
        parser.parseInput(null, "http://example.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInputNullBaseUri() {
        parser.parseInput("<html></html>", null);
    }

    @Test
    public void testParseInputWithTrackingErrors() {
        String html = "<html><head><title>Test</title></head><body><script>throw new Error('Test Error')</script></body></html>";
        List<ParseError> errors = parserWithTrackingErrors.getErrors();
        Document doc = parserWithTrackingErrors.parseInput(html, "http://example.com");
        List<Node> nodes = doc.body().childNodes();
        assertNotNull(doc);
        assertNotNull(nodes.get(0).childNodes());
        assertTrue(nodes.get(0).childNodes().get(0).nodeName().equals("script"));
        assertTrue(errors.size() > 0);
        assertTrue(errors.get(0).getLine() > 0);
        assertTrue(errors.get(0).getMessage().contains("Test Error"));
    }

    @Test
    public void testParseInputWithMaxErrors() {
        String html = "<html><head><title>Test</title></head><body><script>throw new Error('Test Error 1')</script><script>throw new Error('Test Error 2')</script></body></html>";
        List<ParseError> errors = new ArrayList<>();
        Parser parserWithMaxErrorsAndErrors = new Parser(new HtmlTreeBuilder()) {{
            setMaxErrors(2);
            setTrackErrors(true);
        }};
        Document doc = parserWithMaxErrorsAndErrors.parseInput(html, "http://example.com");
        List<Node> nodes = doc.body().childNodes();
        List<ParseError> parseErrors = parserWithMaxErrorsAndErrors.getErrors();
        assertNotNull(doc);
        assertNotNull(nodes.get(0).childNodes());
        assertTrue(nodes.get(0).childNodes().size() == 2);
        assertTrue(parseErrors.size() == 2);
        assertTrue(errors.isEmpty());
        assertTrue(parseErrors.get(0).getLine() > 0);
        assertTrue(parseErrors.get(0).getMessage().contains("Test Error 1"));
        assertTrue(parseErrors.get(1).getLine() > 0);
        assertTrue(parseErrors.get(1).getMessage().contains("Test Error 2"));
    }

    @Test
    public void testGetTreeBuilder() {
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        assertNotNull(treeBuilder);
    }

    @Test
    public void testSetTreeBuilder() {
        TreeBuilder newTreeBuilder = new HtmlTreeBuilder();
        parser.setTreeBuilder(newTreeBuilder);
        TreeBuilder getTreeBuilder = parser.getTreeBuilder();
        assertSame(newTreeBuilder, getTreeBuilder);
    }

    @Test
    public void testIsTrackErrors() {
        assertTrue(parser.isTrackErrors());
        parser.setTrackErrors(0);
        assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        parser.setTrackErrors(5);
        assertTrue(parser.isTrackErrors());
        parser.setTrackErrors(0);
        assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testGetErrors() {
        String html = "<html><head><title>Test</title></head><body><script>throw new Error('Test Error')</script></body></html>";
        List<ParseError> errors = parserWithTrackingErrors.getErrors();
        Document doc = parserWithTrackingErrors.parseInput(html, "http://example.com");
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testStaticParse() {
        Document doc = Parser.parse("<html><head></head><body><p>Test</p></body></html>", "http://example.com");
        assertNotNull(doc);
        assertNotNull(doc.body());
        assertTrue(doc.body().text().contains("Test"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStaticParseNullHtml() {
        Parser.parse(null, "http://example.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStaticParseNullBaseUri() {
        Parser.parse("<html></html>", null);
    }

    @Test
    public void testStaticParseFragment() {
        String fragmentHtml = "<div>Test</div>";
        Element context = Jsoup.parse("<html><body></body></html>").body();
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, "http://example.com");
        assertNotNull(nodes);
        assertNotNull(nodes.get(0));
        assertTrue(nodes.get(0).nodeName().equals("div"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStaticParseFragmentNullFragmentHtml() {
        Parser.parseFragment(null, null, "http://example.com");
    }

    @Test
    public void testStaticParseBodyFragment() {
        String bodyHtml = "<p>Test</p>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");
        assertNotNull(doc);
        assertNotNull(doc.body());
        assertTrue(doc.body().text().contains("Test"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStaticParseBodyFragmentNullBodyHtml() {
        Parser.parseBodyFragment(null, "http://example.com");
    }

    @Test
    public void testUnescapeHtmlEntities() {
        String htmlEncoded = "&lt;p&gt;Test&amp;&#38;amp;&#38;lt;&#38;gt;";
        String unescaped = Parser.unescapeHtmlEntities(htmlEncoded);
        assertNotNull(unescaped);
        assertTrue(unescaped.contains("<p>Test&amp;&lt;&gt;"));
    }

    @Test
    public void testParseBodyFragmentRelaxedDeprecated() {
        String bodyHtml = "<p>Test</p>";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "http://example.com");
        assertNotNull(doc);
        assertNotNull(doc.body());
        assertTrue(doc.body().text().contains("Test"));
    }

    @Test(expected = DeprecationException.class)
    public void testParseBodyFragmentRelaxedDeprecatedWithError() {
        String bodyHtml = "<p>Test</p><script>throw new Error('Test Error')</script>";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "http://example.com");
    }
}