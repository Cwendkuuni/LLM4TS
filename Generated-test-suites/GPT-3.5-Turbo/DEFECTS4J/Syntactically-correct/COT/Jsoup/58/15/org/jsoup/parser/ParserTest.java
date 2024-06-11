
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ParserTest {

    @Test
    public void testParseInput() {
        // Test case 1: valid HTML input
        String html = "<html><head></head><body></body></html>";
        String baseUri = "http://example.com";
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document document = parser.parseInput(html, baseUri);
        Assert.assertNotNull(document);
        
        // Test case 2: invalid HTML input
        html = "<html><head><title></title></head><body>";
        document = parser.parseInput(html, baseUri);
        Assert.assertNull(document);
    }

    @Test
    public void testGetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        Assert.assertNotNull(treeBuilder);
    }

    @Test
    public void testSetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder treeBuilder = new XmlTreeBuilder();
        parser.setTreeBuilder(treeBuilder);
        TreeBuilder newTreeBuilder = parser.getTreeBuilder();
        Assert.assertEquals(treeBuilder, newTreeBuilder);
    }

    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        Assert.assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(10);
        Assert.assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testGetErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        List<ParseError> errors = parser.getErrors();
        Assert.assertNotNull(errors);
    }

    @Test
    public void testSettings() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        ParseSettings settings = new ParseSettings(true, true);
        parser.settings(settings);
        ParseSettings newSettings = parser.settings();
        Assert.assertEquals(settings, newSettings);
    }

    @Test
    public void testParse() {
        // Test case 1: valid HTML input
        String html = "<html><head></head><body></body></html>";
        String baseUri = "http://example.com";
        Document document = Parser.parse(html, baseUri);
        Assert.assertNotNull(document);
        
        // Test case 2: invalid HTML input
        html = "<html><head><title></title></head><body>";
        document = Parser.parse(html, baseUri);
        Assert.assertNotNull(document);
    }

    @Test
    public void testParseFragment() {
        // Test case 1: valid HTML fragment input
        String fragmentHtml = "<div><p>Paragraph 1</p><p>Paragraph 2</p></div>";
        Element context = new Element("div");
        String baseUri = "http://example.com";
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
        Assert.assertEquals(2, nodes.size());
        
        // Test case 2: invalid HTML fragment input
        fragmentHtml = "<div><p>Paragraph 1</div>";
        nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
        Assert.assertEquals(1, nodes.size());
    }

    @Test
    public void testParseXmlFragment() {
        // Test case 1: valid XML fragment input
        String fragmentXml = "<root><child1></child1><child2></child2></root>";
        String baseUri = "http://example.com";
        List<Node> nodes = Parser.parseXmlFragment(fragmentXml, baseUri);
        Assert.assertEquals(2, nodes.size());
        
        // Test case 2: invalid XML fragment input
        fragmentXml = "<root><child1></child1><child2>";
        nodes = Parser.parseXmlFragment(fragmentXml, baseUri);
        Assert.assertEquals(2, nodes.size());
    }

    @Test
    public void testParseBodyFragment() {
        // Test case 1: valid body HTML fragment input
        String bodyHtml = "<p>Paragraph</p>";
        String baseUri = "http://example.com";
        Document document = Parser.parseBodyFragment(bodyHtml, baseUri);
        Assert.assertNotNull(document);
        
        // Test case 2: invalid body HTML fragment input
        bodyHtml = "<p>Paragraph";
        document = Parser.parseBodyFragment(bodyHtml, baseUri);
        Assert.assertNotNull(document);
    }

    @Test
    public void testUnescapeEntities() {
        // Test case 1: HTML escaped string
        String string = "This &lt;span&gt; is escaped";
        boolean inAttribute = false;
        String unescaped = Parser.unescapeEntities(string, inAttribute);
        Assert.assertEquals("This <span> is escaped", unescaped);
        
        // Test case 2: HTML escaped string in attribute
        string = "This &quot;quote&quot; is escaped";
        inAttribute = true;
        unescaped = Parser.unescapeEntities(string, inAttribute);
        Assert.assertEquals("This \"quote\" is escaped", unescaped);
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        // Test case 1: valid HTML input
        String bodyHtml = "<html><head></head><body></body></html>";
        String baseUri = "http://example.com";
        Document document = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        Assert.assertNotNull(document);
        
        // Test case 2: invalid HTML input
        bodyHtml = "<html><head><title></title></head><body>";
        document = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        Assert.assertNotNull(document);
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

