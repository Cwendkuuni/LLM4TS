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
    public void testParserConstructor() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        Assert.assertNotNull(parser);
    }

    @Test
    public void testParseInput() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput(html, null);
        Assert.assertNotNull(doc);
        Assert.assertEquals("Hello World", doc.getElementsByTag("h1").text());
    }

    @Test
    public void testParseInputWithTrackErrors() {
        String html = "<html><body><h1>Hello World</h1><script>alert('Hello')</script></body></html>";
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(1);
        Document doc = parser.parseInput(html, null);
        List<ParseError> errors = parser.getErrors();
        Assert.assertEquals(1, errors.size());
        Assert.assertEquals("Encountered an unexpected token (<script>), expected </startTag> or <endTag> or <empty> or <text>" +
                " or <comment> or <doctype> or <cdata>", errors.get(0).getMessage());
        Assert.assertNull(doc);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInputNullHtml() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.parseInput(null, null);
    }

    @Test
    public void testParseInputEmptyString() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput("", null);
        Assert.assertNull(doc);
    }

    @Test
    public void testParseInputWithBaseUri() {
        String baseUri = "http://example.com";
        String html = "<html><body><a href='about.html'></a></body></html>";
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput(html, baseUri);
        Element link = doc.getElementsByTag("a").first();
        String href = link.attr("href");
        Assert.assertEquals("about.html", href);
    }

    @Test
    public void testGetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        Assert.assertEquals(HtmlTreeBuilder.class, treeBuilder.getClass());
    }

    @Test
    public void testSetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder treeBuilder = new XmlTreeBuilder();
        parser.setTreeBuilder(treeBuilder);
        TreeBuilder newTreeBuilder = parser.getTreeBuilder();
        Assert.assertEquals(XmlTreeBuilder.class, newTreeBuilder.getClass());
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
        parser.setTrackErrors(5);
        Assert.assertEquals(5, parser.getMaxErrors());
    }

    @Test
    public void testGetErrors() {
        String html = "<html><body><h1>Hello World</h1><script>alert('Hello')</script></body></html>";
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(1);
        Document doc = parser.parseInput(html, null);
        List<ParseError> errors = parser.getErrors();
        Assert.assertEquals(1, errors.size());
        Assert.assertEquals("Encountered an unexpected token (<script>), expected </startTag> or <endTag> or <empty> or <text>" +
                " or <comment> or <doctype> or <cdata>", errors.get(0).getMessage());
    }

    @Test
    public void testSettings() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        ParseSettings settings = parser.settings();
        Assert.assertNotNull(settings);
    }

    @Test
    public void testParseStaticParse() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Document doc = Parser.parse(html, null);
        Assert.assertNotNull(doc);
        Assert.assertEquals("Hello World", doc.getElementsByTag("h1").text());
    }

    @Test
    public void testParseFragment() {
        String fragmentHtml = "<div><p>Hello World</p></div>";
        Element context = Jsoup.parse("<html></html>").body();
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, null);
        Assert.assertEquals(1, nodes.size());
        Assert.assertEquals("Hello World", nodes.get(0).text());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseFragmentNullFragmentHtml() {
        Element context = Jsoup.parse("<html></html>").body();
        Parser.parseFragment(null, context, null);
    }

    @Test
    public void testParseXmlFragment() {
        String fragmentXml = "<xml><tag>Hello World</tag></xml>";
        List<Node> nodes = Parser.parseXmlFragment(fragmentXml, null);
        Assert.assertEquals(1, nodes.size());
        Assert.assertEquals("Hello World", nodes.get(0).text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<body><p>Hello World</p></body>";
        Document doc = Parser.parseBodyFragment(bodyHtml, null);
        Assert.assertNotNull(doc);
        Assert.assertEquals("Hello World", doc.body().text());
    }

    @Test
    public void testUnescapeEntities() {
        String htmlEscaped = "&amp;<>&quot;\"&apos;'&gt;>";
        String unescaped = Parser.unescapeEntities(htmlEscaped, false);
        Assert.assertEquals("<&>&\"'>", unescaped);
    }

    @Test
    public void testParseBodyFragmentRelaxedDeprecated() {
        String bodyHtml = "<body><p>Hello World</p></body>";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, null);
        Assert.assertNotNull(doc);
        Assert.assertEquals("Hello World", doc.body().text());
    }

    @Test
    public void testHtmlParser() {
        Parser parser = Parser.htmlParser();
        Assert.assertEquals(HtmlTreeBuilder.class, parser.getTreeBuilder().getClass());
    }

    @Test
    public void testXmlParser() {
        Parser parser = Parser.xmlParser();
        Assert.assertEquals(XmlTreeBuilder.class, parser.getTreeBuilder().getClass());
    }
}