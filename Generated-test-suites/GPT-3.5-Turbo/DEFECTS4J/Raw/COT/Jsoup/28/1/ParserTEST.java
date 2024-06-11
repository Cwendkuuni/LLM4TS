import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ParserTest {

    @Test
    public void testParseInput() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        String html = "<html><body><h1>Title</h1><p>Paragraph</p></body></html>";
        String baseUri = "http://example.com";
        Document doc = parser.parseInput(html, baseUri);

        Assert.assertNotNull("Parsed document should not be null", doc);
        Assert.assertEquals("The number of child elements in the body should be 2", 2, doc.body().children().size());
        Assert.assertEquals("The title should be 'Title'", "Title", doc.selectFirst("h1").text());
        Assert.assertEquals("The paragraph should be 'Paragraph'", "Paragraph", doc.selectFirst("p").text());
    }

    @Test
    public void testGetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder treeBuilder = parser.getTreeBuilder();

        Assert.assertNotNull("TreeBuilder should not be null", treeBuilder);
        Assert.assertTrue("TreeBuilder should be of type HtmlTreeBuilder", treeBuilder instanceof HtmlTreeBuilder);
    }

    @Test
    public void testSetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder treeBuilder = new XmlTreeBuilder();
        parser.setTreeBuilder(treeBuilder);

        Assert.assertEquals("The TreeBuilder should be set to the new instance", treeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        boolean isTrackErrors = parser.isTrackErrors();

        Assert.assertFalse("Parsing error tracking should be disabled by default", isTrackErrors);
    }

    @Test
    public void testSetTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(10);

        Assert.assertTrue("Parsing error tracking should be enabled when maxErrors is greater than 0", parser.isTrackErrors());
    }

    @Test
    public void testGetErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        List<ParseError> errors = parser.getErrors();

        Assert.assertNotNull("Error list should not be null", errors);
        Assert.assertEquals("Error list size should be 0", 0, errors.size());
    }

    @Test
    public void testParse() {
        String html = "<html><body><h1>Title</h1><p>Paragraph</p></body></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);

        Assert.assertNotNull("Parsed document should not be null", doc);
        Assert.assertEquals("The number of child elements in the body should be 2", 2, doc.body().children().size());
        Assert.assertEquals("The title should be 'Title'", "Title", doc.selectFirst("h1").text());
        Assert.assertEquals("The paragraph should be 'Paragraph'", "Paragraph", doc.selectFirst("p").text());
    }

    @Test
    public void testParseFragment() {
        String fragmentHtml = "<div><h2>Subtitle</h2><p>Content</p></div>";
        Element context = new Element("div");
        String baseUri = "http://example.com";
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);

        Assert.assertNotNull("Parsed nodes should not be null", nodes);
        Assert.assertEquals("The number of nodes should be 2", 2, nodes.size());
        Assert.assertEquals("The subtitle should be 'Subtitle'", "Subtitle", ((Element) nodes.get(0)).selectFirst("h2").text());
        Assert.assertEquals("The content should be 'Content'", "Content", ((Element) nodes.get(1)).selectFirst("p").text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Title</h1><p>Paragraph</p>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

        Assert.assertNotNull("Parsed document should not be null", doc);
        Assert.assertEquals("The number of child elements in the body should be 2", 2, doc.body().children().size());
        Assert.assertEquals("The title should be 'Title'", "Title", doc.selectFirst("h1").text());
        Assert.assertEquals("The paragraph should be 'Paragraph'", "Paragraph", doc.selectFirst("p").text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<h1>Title</h1><p>Paragraph</p>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);

        Assert.assertNotNull("Parsed document should not be null", doc);
        Assert.assertEquals("The number of child elements in the body should be 2", 2, doc.body().children().size());
        Assert.assertEquals("The title should be 'Title'", "Title", doc.selectFirst("h1").text());
        Assert.assertEquals("The paragraph should be 'Paragraph'", "Paragraph", doc.selectFirst("p").text());
    }

    @Test
    public void testHtmlParser() {
        Parser parser = Parser.htmlParser();
        TreeBuilder treeBuilder = parser.getTreeBuilder();

        Assert.assertNotNull("TreeBuilder should not be null", treeBuilder);
        Assert.assertTrue("TreeBuilder should be of type HtmlTreeBuilder", treeBuilder instanceof HtmlTreeBuilder);
    }

    @Test
    public void testXmlParser() {
        Parser parser = Parser.xmlParser();
        TreeBuilder treeBuilder = parser.getTreeBuilder();

        Assert.assertNotNull("TreeBuilder should not be null", treeBuilder);
        Assert.assertTrue("TreeBuilder should be of type XmlTreeBuilder", treeBuilder instanceof XmlTreeBuilder);
    }
}