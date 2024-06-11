import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ParserTest {

    @Test
    public void testParseInput() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);

        String html = "<html><body><h1>My First Heading</h1><p>My first paragraph.</p></body></html>";
        String baseUri = "https://www.example.com";

        Document doc = parser.parseInput(html, baseUri);

        Assert.assertEquals("https://www.example.com", doc.baseUri());
        Assert.assertEquals("<html>\n" +
                " <head></head>\n" +
                " <body>\n" +
                "  <h1>\n" +
                "   My First Heading\n" +
                "  </h1>\n" +
                "  <p>\n" +
                "   My first paragraph.\n" +
                "  </p>\n" +
                " </body>\n" +
                "</html>", doc.outerHtml());
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
        Parser parser = new Parser(treeBuilder1);

        TreeBuilder treeBuilder2 = new XmlTreeBuilder();
        parser.setTreeBuilder(treeBuilder2);

        Assert.assertEquals(treeBuilder2, parser.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);

        Assert.assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);

        parser.setTrackErrors(5);

        Assert.assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testGetErrors() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);

        String html = "<html><body><h1>My First Heading</h2><p>My first paragraph.</p></body></html>";
        String baseUri = "https://www.example.com";

        Document doc = parser.parseInput(html, baseUri);

        List<ParseError> errors = parser.getErrors();

        Assert.assertEquals(1, errors.size());
        Assert.assertEquals(14, errors.get(0).pos());
        Assert.assertEquals("Unexpected <h2> tag", errors.get(0).getErrorMessage());
    }

    @Test
    public void testParse() {
        String html = "<html><body><h1>My First Heading</h1><p>My first paragraph.</p></body></html>";
        String baseUri = "https://www.example.com";

        Document doc = Parser.parse(html, baseUri);

        Assert.assertEquals("https://www.example.com", doc.baseUri());
        Assert.assertEquals("<html>\n" +
                " <head></head>\n" +
                " <body>\n" +
                "  <h1>\n" +
                "   My First Heading\n" +
                "  </h1>\n" +
                "  <p>\n" +
                "   My first paragraph.\n" +
                "  </p>\n" +
                " </body>\n" +
                "</html>", doc.outerHtml());
    }

    @Test
    public void testParseFragment() {
        String fragmentHtml = "<h1>My First Heading</h1><p>My first paragraph.</p>";
        Element context = new Element("div");
        String baseUri = "https://www.example.com";

        List<Node> nodeList = Parser.parseFragment(fragmentHtml, context, baseUri);

        Assert.assertEquals(2, nodeList.size());
        Assert.assertEquals("<h1>My First Heading</h1>", nodeList.get(0).outerHtml());
        Assert.assertEquals("<p>My first paragraph.</p>", nodeList.get(1).outerHtml());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>My First Heading</h1><p>My first paragraph.</p>";
        String baseUri = "https://www.example.com";

        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

        Assert.assertEquals("https://www.example.com", doc.baseUri());
        Assert.assertEquals("<html>\n" +
                " <head></head>\n" +
                " <body>\n" +
                "  <h1>\n" +
                "   My First Heading\n" +
                "  </h1>\n" +
                "  <p>\n" +
                "   My first paragraph.\n" +
                "  </p>\n" +
                " </body>\n" +
                "</html>", doc.outerHtml());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<h1>My First Heading</h1><p>My first paragraph.</p>";
        String baseUri = "https://www.example.com";

        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);

        Assert.assertEquals("https://www.example.com", doc.baseUri());
        Assert.assertEquals("<html>\n" +
                " <head></head>\n" +
                " <body>\n" +
                "  <h1>\n" +
                "   My First Heading\n" +
                "  </h1>\n" +
                "  <p>\n" +
                "   My first paragraph.\n" +
                "  </p>\n" +
                " </body>\n" +
                "</html>", doc.outerHtml());
    }

    @Test
    public void testHtmlParser() {
        Parser parser = Parser.htmlParser();

        Assert.assertTrue(parser.getTreeBuilder() instanceof HtmlTreeBuilder);
    }

    @Test
    public void testXmlParser() {
        Parser parser = Parser.xmlParser();

        Assert.assertTrue(parser.getTreeBuilder() instanceof XmlTreeBuilder);
    }
}