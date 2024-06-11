
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.ParseError;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ParserTest {
    private Parser parser = new Parser(new HtmlTreeBuilder());

    @Test
    public void testParseInput() {
        String html = "<html><body><h1>Hello, world!</h1></body></html>";
        String baseUri = "http://example.com";
        Document document = parser.parseInput(html, baseUri);

        Assert.assertEquals("<html>\n <head></head>\n <body>\n  <h1>Hello, world!</h1>\n </body>\n</html>", document.toString());
    }

    @Test
    public void testGetTreeBuilder() {
        TreeBuilder treeBuilder = parser.getTreeBuilder();

        Assert.assertNotNull(treeBuilder);
    }

    @Test
    public void testSetTreeBuilder() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
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

        Assert.assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testGetErrors() {
        List<ParseError> errors = parser.getErrors();

        Assert.assertNotNull(errors);
        Assert.assertTrue(errors.isEmpty());
    }

    @Test
    public void testParse() {
        String html = "<html><body><h1>Hello, world!</h1></body></html>";
        String baseUri = "http://example.com";
        Document document = Parser.parse(html, baseUri);

        Assert.assertEquals("<html>\n <head></head>\n <body>\n  <h1>Hello, world!</h1>\n </body>\n</html>", document.toString());
    }

    @Test
    public void testParseFragment() {
        String fragmentHtml = "<div>Hello, world!</div>";
        Element context = Jsoup.parse("<html><body></body></html>").body();
        String baseUri = "http://example.com";
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);

        Assert.assertEquals(1, nodes.size());
        Assert.assertEquals("<div>Hello, world!</div>", nodes.get(0).toString());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<div>Hello, world!</div>";
        String baseUri = "http://example.com";
        Document document = Parser.parseBodyFragment(bodyHtml, baseUri);

        Assert.assertEquals("<html>\n <head></head>\n <body>\n  <div>Hello, world!</div>\n </body>\n</html>", document.toString());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<div>Hello, world!</div>";
        String baseUri = "http://example.com";
        Document document = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);

        Assert.assertEquals("<html>\n <head></head>\n <body>\n  <div>Hello, world!</div>\n </body>\n</html>", document.toString());
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
