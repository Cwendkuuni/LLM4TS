import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ParserTest {

    @Test
    public void testParseInput() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        String html = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        Document doc = parser.parseInput(html, baseUri);
        Assert.assertNotNull(doc);
        Assert.assertEquals("Hello World", doc.select("h1").text());
    }

    @Test
    public void testGetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        Assert.assertSame(treeBuilder, parser.treeBuilder);
    }

    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        Assert.assertFalse(parser.isTrackErrors());

        parser.setTrackErrors(5);
        Assert.assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(5);
        Assert.assertEquals(5, parser.maxErrors);
    }

    @Test
    public void testGetErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        List<ParseError> errors = new ArrayList<>();
        errors.add(new ParseError("Test Error", 1, 1));
        parser.errors = errors;
        List<ParseError> testErrors = parser.getErrors();
        Assert.assertEquals(1, testErrors.size());
        Assert.assertEquals("Test Error", testErrors.get(0).getMessage());
    }

    @Test(expected = NullPointerException.class)
    public void testParseInputNullHtml() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.parseInput(null, "http://example.com");
    }

    @Test(expected = NullPointerException.class)
    public void testParseInputNullBaseUri() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        String html = "<html><body><h1>Hello World</h1></body></html>";
        parser.parseInput(html, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInputEmptyHtml() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.parseInput("", "http://example.com");
    }

    @Test
    public void testParseStaticFunction() {
        Document doc = Parser.parse("<html><body><h1>Hello World</h1></body></html>", "http://example.com");
        Assert.assertNotNull(doc);
        Assert.assertEquals("Hello World", doc.select("h1").text());
    }

    @Test
    public void testParseBodyFragment() {
        Document doc = Parser.parseBodyFragment("<html><body><h1>Hello World</h1></body></html>", "http://example.com");
        Assert.assertNotNull(doc);
        Assert.assertEquals("Hello World", doc.select("h1").text());
    }

    @Test
    public void testUnescapeEntities() {
        String html = "&lt;h1&gt;Hello World&lt;/h1&gt;";
        String unescapedHtml = Parser.unescapeEntities(html, false);
        Document doc = Jsoup.parse(unescapedHtml);
        Assert.assertEquals("Hello World", doc.select("h1").text());
    }

    @Test
    public void testParseFragment() {
        Element context = Jsoup.parse("<html><body><div id='container'></div></body></html>").body();
        List<Node> nodes = Parser.parseFragment("<p>Hello World</p>", context, "http://example.com");
        Assert.assertEquals(1, nodes.size());
        Assert.assertTrue(nodes.get(0) instanceof Element);
        Assert.assertEquals("Hello World", nodes.get(0).text());
    }

    @Test(expected = NullPointerException.class)
    public void testParseFragmentNullContext() {
        Parser.parseFragment("<p>Hello World</p>", null, "http://example.com");
    }
}