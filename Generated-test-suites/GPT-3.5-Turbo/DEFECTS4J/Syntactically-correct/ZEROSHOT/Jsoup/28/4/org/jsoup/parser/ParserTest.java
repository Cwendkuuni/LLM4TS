
package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import java.util.List;

public class ParserTest {
    private Parser parser;

    @Before
    public void setup() {
        parser = new Parser(new TreeBuilder());
    }

    @Test
    public void testParseInput() {
        String html = "<html><body><p>Hello, World!</p></body></html>";
        String baseUri = "https://www.example.com";

        Document doc = parser.parseInput(html, baseUri);

        Assert.assertEquals("html", doc.nodeName());
        Assert.assertEquals("body", doc.body().nodeName());
        Assert.assertEquals("p", doc.select("p").first().nodeName());
        Assert.assertEquals("Hello, World!", doc.select("p").first().text());
    }

    @Test
    public void testGetTreeBuilder() {
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        Assert.assertNotNull(treeBuilder);
    }

    @Test
    public void testSetTreeBuilder() {
        TreeBuilder treeBuilder = new TreeBuilder();
        parser.setTreeBuilder(treeBuilder);

        Assert.assertEquals(treeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors() {
        boolean trackErrors = parser.isTrackErrors();
        Assert.assertFalse(trackErrors);
    }

    @Test
    public void testSetTrackErrors() {
        int maxErrors = 10;
        parser.setTrackErrors(maxErrors);

        boolean trackErrors = parser.isTrackErrors();
        Assert.assertTrue(trackErrors);
    }

    @Test
    public void testGetErrors() {
        List<ParseError> errors = parser.getErrors();
        Assert.assertNotNull(errors);
    }

    @Test
    public void testParse() {
        String html = "<html><body><p>Hello, World!</p></body></html>";
        String baseUri = "https://www.example.com";

        Document doc = Parser.parse(html, baseUri);

        Assert.assertEquals("html", doc.nodeName());
        Assert.assertEquals("body", doc.body().nodeName());
        Assert.assertEquals("p", doc.select("p").first().nodeName());
        Assert.assertEquals("Hello, World!", doc.select("p").first().text());
    }

    @Test
    public void testParseFragment() {
        String fragmentHtml = "<p>Hello, World!</p>";
        Element context = new Element("div");
        String baseUri = "https://www.example.com";

        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);

        Assert.assertEquals(1, nodes.size());
        Assert.assertEquals("p", nodes.get(0).nodeName());
        Assert.assertEquals("Hello, World!", ((Element) nodes.get(0)).text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello, World!</p>";
        String baseUri = "https://www.example.com";

        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

        Assert.assertEquals("body", doc.body().nodeName());
        Assert.assertEquals("p", doc.select("p").first().nodeName());
        Assert.assertEquals("Hello, World!", doc.select("p").first().text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<p>Hello, World!</p>";
        String baseUri = "https://www.example.com";

        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);

        Assert.assertEquals("body", doc.body().nodeName());
        Assert.assertEquals("p", doc.select("p").first().nodeName());
        Assert.assertEquals("Hello, World!", doc.select("p").first().text());
    }

    @Test
    public void testHtmlParser() {
        Parser htmlParser = Parser.htmlParser();
        Assert.assertNotNull(htmlParser);
        Assert.assertEquals(HtmlTreeBuilder.class, htmlParser.getTreeBuilder().getClass());
    }

    @Test
    public void testXmlParser() {
        Parser xmlParser = Parser.xmlParser();
        Assert.assertNotNull(xmlParser);
        Assert.assertEquals(XmlTreeBuilder.class, xmlParser.getTreeBuilder().getClass());
    }
}
