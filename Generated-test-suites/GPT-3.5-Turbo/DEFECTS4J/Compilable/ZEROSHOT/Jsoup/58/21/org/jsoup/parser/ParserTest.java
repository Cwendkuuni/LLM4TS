package org.jsoup.parser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.HtmlTreeBuilder;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.ParseErrorList;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class ParserTest {
    private Parser parser;

    @Before
    public void setUp() {
        parser = new Parser(new HtmlTreeBuilder());
    }

    @Test
    public void testParseInput() {
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        String baseUri = "http://example.com";
        Document doc = parser.parseInput(html, baseUri);

        Assert.assertNotNull(doc);
        Assert.assertEquals("<html>\n" +
                " <head></head>\n" +
                " <body>\n" +
                "  <h1>\n" +
                "   Hello, World!\n" +
                "  </h1>\n" +
                " </body>\n" +
                "</html>", doc.toString());
    }

    @Test
    public void testGetTreeBuilder() {
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        Assert.assertNotNull(treeBuilder);
    }

    @Test
    public void testSetTreeBuilder() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser newParser = parser.setTreeBuilder(treeBuilder);

        Assert.assertNotNull(newParser);
        Assert.assertEquals(treeBuilder, newParser.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors() {
        boolean trackErrors = parser.isTrackErrors();
        Assert.assertFalse(trackErrors);
    }

    @Test
    public void testSetTrackErrors() {
        Parser newParser = parser.setTrackErrors(5);

        Assert.assertNotNull(newParser);
        Assert.assertTrue(newParser.isTrackErrors());
    }

    @Test
    public void testGetErrors() {
        List<ParseError> errors = parser.getErrors();
        Assert.assertNotNull(errors);
    }

    @Test
    public void testSettings() {
        ParseSettings settings = new ParseSettings(false, false);
        Parser newParser = parser.settings(settings);

        Assert.assertNotNull(newParser);
        Assert.assertEquals(settings, newParser.settings());
    }

    @Test
    public void testParse() {
        String html = "<div>Hello</div>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);

        Assert.assertNotNull(doc);
        Assert.assertEquals("<html>\n" +
                " <head></head>\n" +
                " <body>\n" +
                "  <div>\n" +
                "   Hello\n" +
                "  </div>\n" +
                " </body>\n" +
                "</html>", doc.toString());
    }

    @Test
    public void testParseFragment() {
        String fragmentHtml = "<p>Hello, <strong>World!</strong></p>";
        Element context = new Element("body");
        String baseUri = "http://example.com";
        List<Node> nodeList = Parser.parseFragment(fragmentHtml, context, baseUri);

        Assert.assertNotNull(nodeList);
        Assert.assertEquals(1, nodeList.size());
        Assert.assertEquals("Hello, ", nodeList.get(0).toString());
        Assert.assertEquals("<body>\n" +
                " <p>\n" +
                "  Hello, <strong>\n" +
                "   World!\n" +
                "  </strong>\n" +
                " </p>\n" +
                "</body>", context.toString());
    }

    @Test
    public void testParseXmlFragment() {
        String fragmentXml = "<root><span>Hello</span></root>";
        String baseUri = "http://example.com";
        List<Node> nodeList = Parser.parseXmlFragment(fragmentXml, baseUri);

        Assert.assertNotNull(nodeList);
        Assert.assertEquals(1, nodeList.size());
        Assert.assertEquals("<root>\n" +
                " <span>\n" +
                "  Hello\n" +
                " </span>\n" +
                "</root>", nodeList.get(0).toString());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello, <strong>World!</strong></p>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

        Assert.assertNotNull(doc);
        Assert.assertEquals("<html>\n" +
                " <head></head>\n" +
                " <body>\n" +
                "  <p>\n" +
                "   Hello, <strong>\n" +
                "    World!\n" +
                "   </strong>\n" +
                "  </p>\n" +
                " </body>\n" +
                "</html>", doc.toString());
    }

    @Test
    public void testUnescapeEntities() {
        String string = "&lt;p&gt;Hello, &amp;lt;strong&amp;gt;World!&amp;lt;/strong&amp;gt;&lt;/p&gt;";
        boolean inAttribute = false;
        String unescapedString = Parser.unescapeEntities(string, inAttribute);

        Assert.assertNotNull(unescapedString);
        Assert.assertEquals("<p>Hello, &lt;strong&gt;World!&lt;/strong&gt;</p>", unescapedString);
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<p>Hello, <strong>World!</strong></p>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);

        Assert.assertNotNull(doc);
        Assert.assertEquals("<html>\n" +
                " <head></head>\n" +
                " <body>\n" +
                "  <p>\n" +
                "   Hello, <strong>\n" +
                "    World!\n" +
                "   </strong>\n" +
                "  </p>\n" +
                " </body>\n" +
                "</html>", doc.toString());
    }

    @Test
    public void testHtmlParser() {
        Parser htmlParser = Parser.htmlParser();
        Assert.assertNotNull(htmlParser);
        Assert.assertTrue(htmlParser.getTreeBuilder() instanceof HtmlTreeBuilder);
    }

    @Test
    public void testXmlParser() {
        Parser xmlParser = Parser.xmlParser();
        Assert.assertNotNull(xmlParser);
        Assert.assertTrue(xmlParser.getTreeBuilder() instanceof XmlTreeBuilder);
    }
}