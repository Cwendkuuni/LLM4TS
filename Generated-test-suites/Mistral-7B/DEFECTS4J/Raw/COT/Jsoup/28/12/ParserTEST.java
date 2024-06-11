import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ParserTest {

    @Test
    public void testParseInput() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        String html = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        Document doc = parser.parseInput(html, baseUri);

        Assert.assertNotNull(doc);
        Assert.assertEquals("Hello World", doc.getElementsByTag("h1").text());
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

        Assert.assertNotSame(parser.getTreeBuilder(), newTreeBuilder);
        Assert.assertSame(parser.getTreeBuilder(), newTreeBuilder);
    }

    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());

        Assert.assertFalse(parser.isTrackErrors());

        parser.setTrackErrors(1);

        Assert.assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors_withNegativeValue() {
        Parser parser = new Parser(new HtmlTreeBuilder());

        parser.setTrackErrors(-1);

        Assert.assertFalse(parser.isTrackErrors());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetTrackErrors_withZeroValue() {
        Parser parser = new Parser(new HtmlTreeBuilder());

        parser.setTrackErrors(0);
    }

    @Test
    public void testGetErrors_whenNoErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());

        List<ParseError> errors = parser.getErrors();

        Assert.assertNotNull(errors);
        Assert.assertTrue(errors.isEmpty());
    }

    @Test
    public void testGetErrors_whenErrorsPresent() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        String html = "<html><body><script>alert('XSS')</script></body></html>";

        parser.parseInput(html, "http://example.com");

        List<ParseError> errors = parser.getErrors();

        Assert.assertNotNull(errors);
        Assert.assertFalse(errors.isEmpty());
        Assert.assertEquals(1, errors.size());
        Assert.assertEquals("Malformed document starting with script or style", errors.get(0).getMessage());
    }

    @Test
    public void testParse_whenEmptyHtml() {
        Document doc = Parser.parse("", "http://example.com");

        Assert.assertNotNull(doc);
        Assert.assertTrue(doc.body().text().isEmpty());
    }

    @Test
    public void testParseFragment_withValidHtml() {
        String fragmentHtml = "<p>Test fragment</p>";
        Element context = Jsoup.parse("<html><body></body></html>").body();

        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, "http://example.com");

        Assert.assertNotNull(nodes);
        Assert.assertEquals(1, nodes.size());
        Assert.assertTrue(nodes.get(0) instanceof Element);
        Assert.assertEquals("Test fragment", nodes.get(0).text());

        // check context is not modified
        Assert.assertEquals(0, context.childrenSize());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseFragment_withNullContext() {
        String fragmentHtml = "<p>Test fragment</p>";

        Parser.parseFragment(fragmentHtml, null, "http://example.com");
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Test body fragment</p>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");

        Assert.assertNotNull(doc);
        Assert.assertNotNull(doc.body());
        Assert.assertEquals("Test body fragment", doc.body().text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<p>Test body fragment</p>";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "http://example.com");

        Assert.assertNotNull(doc);
        Assert.assertNotNull(doc.body());
        Assert.assertEquals("Test body fragment", doc.body().text());
    }

    @Test(expected = DeprecationWarning.class)
    public void testParseBodyFragmentRelaxed_warning() {
        String bodyHtml = "<p>Test body fragment</p>";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "http://example.com");
    }

    @Test
    public void testHtmlParser() {
        Parser parser = Parser.htmlParser();

        Assert.assertTrue(parser instanceof Parser);
        Assert.assertTrue(parser.getTreeBuilder() instanceof HtmlTreeBuilder);
    }

    @Test
    public void testXmlParser() {
        Parser parser = Parser.xmlParser();

        Assert.assertTrue(parser instanceof Parser);
        Assert.assertTrue(parser.getTreeBuilder() instanceof XmlTreeBuilder);
    }
}