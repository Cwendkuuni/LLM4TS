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
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document document = parser.parseInput(html, "http://example.com");
        Assert.assertNotNull(document);
        Assert.assertEquals("Hello World", document.head().text());
        Assert.assertEquals("Hello World", document.body().text());
    }

    @Test
    public void testParseInputWithErrors() {
        String html = "<html><body><h1><script>alert('Hello')</script></h1></body></html>";
        Parser parser = new Parser(new HtmlTreeBuilder());
        List<ParseError> errors = new ArrayList<>();
        Document document = parser.parseInput(html, "http://example.com", errors);
        Assert.assertNotNull(document);
        Assert.assertEquals(1, errors.size());
        Assert.assertEquals("Expected ';', but was '{'", errors.get(0).getMessage());
    }

    @Test
    public void testGetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        Assert.assertSame(treeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testSetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder newTreeBuilder = new XmlTreeBuilder();
        parser.setTreeBuilder(newTreeBuilder);
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        Assert.assertNotSame(treeBuilder, parser.getTreeBuilder());
        Assert.assertSame(newTreeBuilder, parser.getTreeBuilder());
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
        parser.setTrackErrors(1);
        List<ParseError> errors = new ArrayList<>();
        Document document = parser.parseInput("<html><body><script></script></body></html>", "http://example.com", errors);
        Assert.assertEquals(1, errors.size());
        parser.setTrackErrors(0);
        document = parser.parseInput("<html><body><script></script></body></html>", "http://example.com", errors);
        Assert.assertEquals(0, errors.size());
    }

    @Test
    public void testGetErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        List<ParseError> errors = new ArrayList<>();
        Document document = parser.parseInput("<html><body><script></script></body></html>", "http://example.com", errors);
        Assert.assertEquals(0, errors.size());
        parser.setTrackErrors(1);
        document = parser.parseInput("<html><body><script></script></body></html>", "http://example.com", errors);
        Assert.assertEquals(1, errors.size());
        Assert.assertEquals("Unexpected end of input", errors.get(0).getMessage());
    }

    @Test
    public void testSettings() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        ParseSettings settings = parser.settings();
        Assert.assertNotNull(settings);
        parser.settings(new ParseSettings.Builder().build());
        settings = parser.settings();
        Assert.assertNotSame(settings, parser.settings());
    }

    @Test
    public void testStaticParse() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Document document = Parser.parse(html, "http://example.com");
        Assert.assertNotNull(document);
        Assert.assertEquals("Hello World", document.head().text());
        Assert.assertEquals("Hello World", document.body().text());
    }

    @Test
    public void testStaticParseWithErrors() {
        String html = "<html><body><h1><script>alert('Hello')</script></h1></body></html>";
        List<ParseError> errors = new ArrayList<>();
        Document document = Parser.parse(html, "http://example.com", errors);
        Assert.assertNotNull(document);
        Assert.assertEquals(1, errors.size());
        Assert.assertEquals("Expected ';', but was '{'", errors.get(0).getMessage());
    }

    @Test
    public void testStaticParseFragment() {
        String fragment = "<div>Hello World</div>";
        Element context = Jsoup.parse("<html><body></body></html>").body();
        List<Node> nodes = Parser.parseFragment(fragment, context, "http://example.com");
        Assert.assertEquals(1, nodes.size());
        Assert.assertTrue(nodes.get(0) instanceof Element);
        Assert.assertEquals("Hello World", nodes.get(0).text());
    }

    @Test
    public void testStaticParseXmlFragment() {
        String fragment = "<xml><tag>Hello World</tag></xml>";
        List<Node> nodes = Parser.parseXmlFragment(fragment, "http://example.com");
        Assert.assertEquals(1, nodes.size());
        Assert.assertTrue(nodes.get(0) instanceof Element);
        Assert.assertEquals("Hello World", nodes.get(0).text());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseBodyFragmentNullHtml() {
        Parser.parseBodyFragment(null, "http://example.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseBodyFragmentNullBaseUri() {
        Parser.parseBodyFragment("<html><body></body></html>", null);
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<div>Hello World</div>";
        Document document = Parser.parseBodyFragment(bodyHtml, "http://example.com");
        Assert.assertNotNull(document);
        Assert.assertTrue(document.body().childNodes().hasText());
        Assert.assertEquals("Hello World", document.body().childNodes().text());
    }
}