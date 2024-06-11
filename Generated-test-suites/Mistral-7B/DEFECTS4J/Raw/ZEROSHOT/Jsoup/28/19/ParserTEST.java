package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.ParseError;
import org.jsoup.nodes.ParseErrorList;
import org.jsoup.nodes.TreeBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Test class for Parser.java
 */
public class ParserTest {

    @Test
    public void testParserInstance() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);
        Assert.assertNotNull(parser);
    }

    @Test
    public void testParseInput() {
        String html = "<html><body><h1>Test</h1></body></html>";
        String baseUri = "http://test.com";
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput(html, baseUri);
        Assert.assertNotNull(doc);
        Assert.assertEquals(1, doc.childNodes().size());
        Assert.assertEquals(1, doc.body().childNodes().size());
        Assert.assertEquals("Test", doc.body().childNode(0).text());
    }

    @Test
    public void testGetTreeBuilder() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);
        Assert.assertEquals(treeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testSetTreeBuilder() {
        TreeBuilder oldTreeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(oldTreeBuilder);
        TreeBuilder newTreeBuilder = new XmlTreeBuilder();
        parser.setTreeBuilder(newTreeBuilder);
        Assert.assertEquals(newTreeBuilder, parser.getTreeBuilder());
        Assert.assertNotEquals(oldTreeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        Assert.assertFalse(parser.isTrackErrors());
        parser.setTrackErrors(1);
        Assert.assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        Assert.assertEquals(Parser.DEFAULT_MAX_ERRORS, parser.getMaxErrors());
        parser.setTrackErrors(5);
        Assert.assertEquals(5, parser.getMaxErrors());
    }

    @Test
    public void testGetErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        List<ParseError> errors = new ArrayList<>();
        errors.add(new ParseError("Test", 1, 1, "Test Error"));
        parser.setErrors(errors);
        List<ParseError> result = parser.getErrors();
        Assert.assertEquals(1, result.size());
        Assert.assertEquals("Test", result.get(0).sourceName());
        Assert.assertEquals(1, result.get(0).line());
        Assert.assertEquals(1, result.get(0).column());
        Assert.assertEquals("Test Error", result.get(0).getMessage());
    }

    @Test(expected = AssertionError.class)
    public void testParseWithErrors() {
        String html = "<html><body><script><!--\n<!--\n--></script></body></html>";
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput(html, null);
        Assert.assertNotNull(doc);
        List<ParseError> errors = parser.getErrors();
        Assert.assertTrue(errors.size() > 0);
    }

    @Test
    public void testStaticParse() {
        String html = "<html><body><h1>Test</h1></body></html>";
        Document doc = Parser.parse(html, "http://test.com");
        Assert.assertNotNull(doc);
        Assert.assertEquals(1, doc.childNodes().size());
        Assert.assertEquals(1, doc.body().childNodes().size());
        Assert.assertEquals("Test", doc.body().childNode(0).text());
    }

    @Test
    public void testParseFragment() {
        String fragmentHtml = "<div><p>Test</p></div>";
        Element context = new Element("div");
        String baseUri = "http://test.com";
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
        Assert.assertNotNull(nodes);
        Assert.assertEquals(2, nodes.size());
        Assert.assertTrue(nodes.get(0) instanceof Element);
        Assert.assertEquals(1, ((Element) nodes.get(0)).childNodes().size());
        Assert.assertEquals("Test", ((Element) nodes.get(0)).childNode(0).text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<body><h1>Test</h1></body>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://test.com");
        Assert.assertNotNull(doc);
        Assert.assertEquals(1, doc.childNodes().size());
        Assert.assertEquals(1, doc.body().childNodes().size());
        Assert.assertEquals("Test", doc.body().childNode(0).text());
    }

    @Test
    public void testUnescapeHtmlEntities() {
        String str = "&lt;p&gt;Test&amp;amp;&#39;s &quot;ok&quot; &raquo;";
        String unescaped = Parser.unescapeHtml(str);
        Assert.assertEquals("<p>Test&amp;'s &quot;ok&quot; &raquo;</p>", unescaped);
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<body><h1>Test</h1></body>";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "http://test.com");
        Assert.assertNotNull(doc);
        Assert.assertEquals(1, doc.childNodes().size());
        Assert.assertEquals(1, doc.body().childNodes().size());
        Assert.assertEquals("Test", doc.body().childNode(0).text());
    }
}