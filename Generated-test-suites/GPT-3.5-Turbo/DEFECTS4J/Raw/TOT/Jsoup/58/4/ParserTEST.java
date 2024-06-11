import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Test;

import javax.naming.ldap.PagedResultsControl;
import java.beans.IntrospectionException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ParserTest {

    @Test
    public void testParseInput() {
        Parser parser = new Parser(new HtmlTreeBuilder());

        String html = "<html><body><p>Hello, World!</p></body></html>";
        String baseUri = "https://www.example.com";

        Document document = parser.parseInput(html, baseUri);

        Assert.assertNotNull(document);
        Assert.assertEquals(html, document.html());
    }

    @Test
    public void testGetTreeBuilder() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);

        Assert.assertEquals(treeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testSetTreeBuilder() {
        TreeBuilder treeBuilder1 = new HtmlTreeBuilder();
        TreeBuilder treeBuilder2 = new XmlTreeBuilder();
        Parser parser = new Parser(treeBuilder1);

        parser.setTreeBuilder(treeBuilder2);

        Assert.assertEquals(treeBuilder2, parser.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());

        Assert.assertFalse(parser.isTrackErrors());

        parser.setTrackErrors(10);

        Assert.assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());

        parser.setTrackErrors(5);

        Assert.assertTrue(parser.isTrackErrors());
        Assert.assertEquals(5, parser.getErrors().size());

        parser.setTrackErrors(0);

        Assert.assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testGetErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(3);

        List<ParseError> errors = parser.getErrors();

        Assert.assertEquals(3, errors.size());
    }

    @Test
    public void testSettings() {
        Parser parser = new Parser(new HtmlTreeBuilder());

        ParseSettings settings = new ParseSettings(false, true, true, false);

        parser.settings(settings);

        Assert.assertEquals(settings, parser.settings());
    }

    @Test
    public void testParse() {
        String html = "<html><body><div><p>Hello, World!</p></div></body></html>";
        String baseUri = "https://www.example.com";

        Document document = Parser.parse(html, baseUri);

        Assert.assertNotNull(document);
        Assert.assertEquals(html, document.html());
    }

    @Test
    public void testParseFragment() {
        Element context = new Element("div");
        context.html("<p>Test</p>");

        String fragmentHtml = "<span>Fragment</span>";
        String baseUri = "https://www.example.com";

        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);

        Assert.assertNotNull(nodes);
        Assert.assertEquals(1, nodes.size());
        Assert.assertEquals("span", nodes.get(0).nodeName());
        Assert.assertEquals("Fragment", nodes.get(0).text());
    }

    @Test
    public void testParseXmlFragment() {
        String fragmentXml = "<xml><element>Test</element></xml>";
        String baseUri = "https://www.example.com";

        List<Node> nodes = Parser.parseXmlFragment(fragmentXml, baseUri);

        Assert.assertNotNull(nodes);
        Assert.assertEquals(1, nodes.size());
        Assert.assertEquals("element", nodes.get(0).nodeName());
        Assert.assertEquals("Test", nodes.get(0).text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello, World!</p>";
        String baseUri = "https://www.example.com";

        Document document = Parser.parseBodyFragment(bodyHtml, baseUri);

        Assert.assertNotNull(document);
        Elements elements = document.body().select("p");
        Assert.assertEquals(1, elements.size());
        Assert.assertEquals("Hello, World!", elements.get(0).text());
    }

    @Test
    public void testUnescapeEntities() {
        String escapedString = "&lt;p&gt;Hello, World!&lt;/p&gt;";
        String unescapedString = "<p>Hello, World!</p>";

        String result = Parser.unescapeEntities(escapedString, false);

        Assert.assertEquals(unescapedString, result);
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<p>Hello, World!</p>";
        String baseUri = "https://www.example.com";

        Document document = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);

        Assert.assertNotNull(document);
        Elements elements = document.body().select("p");
        Assert.assertEquals(1, elements.size());
        Assert.assertEquals("Hello, World!", elements.get(0).text());
    }

    @Test
    public void testHtmlParser() {
        Parser parser = Parser.htmlParser();

        Assert.assertNotNull(parser);
        Assert.assertTrue(parser.getTreeBuilder() instanceof HtmlTreeBuilder);
    }

    @Test
    public void testXmlParser() {
        Parser parser = Parser.xmlParser();

        Assert.assertNotNull(parser);
        Assert.assertTrue(parser.getTreeBuilder() instanceof XmlTreeBuilder);
    }
}