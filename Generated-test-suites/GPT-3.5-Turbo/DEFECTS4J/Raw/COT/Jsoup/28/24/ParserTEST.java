import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.TreeBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ParserTest {
    private TreeBuilder treeBuilder;
    private Parser parser;
    
    @Test
    public void testParseInput() {
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        String baseUri = "https://example.com";
        
        treeBuilder = new HtmlTreeBuilder();
        parser = new Parser(treeBuilder);
        Document doc = parser.parseInput(html, baseUri);
        
        Assert.assertNotNull(doc);
        Assert.assertEquals(html, doc.html());
    }
    
    @Test
    public void testGetTreeBuilder() {
        treeBuilder = new HtmlTreeBuilder();
        parser = new Parser(treeBuilder);
        
        Assert.assertEquals(treeBuilder, parser.getTreeBuilder());
    }
    
    @Test
    public void testSetTreeBuilder() {
        treeBuilder = new HtmlTreeBuilder();
        parser = new Parser(treeBuilder);
        
        TreeBuilder newTreeBuilder = new XmlTreeBuilder();
        parser.setTreeBuilder(newTreeBuilder);
        
        Assert.assertEquals(newTreeBuilder, parser.getTreeBuilder());
    }
    
    @Test
    public void testIsTrackErrors() {
        treeBuilder = new HtmlTreeBuilder();
        parser = new Parser(treeBuilder);
        
        Assert.assertFalse(parser.isTrackErrors());
    }
    
    @Test
    public void testSetTrackErrors() {
        treeBuilder = new HtmlTreeBuilder();
        parser = new Parser(treeBuilder);
        
        int maxErrors = 10;
        parser.setTrackErrors(maxErrors);
        
        Assert.assertTrue(parser.isTrackErrors());
        Assert.assertEquals(maxErrors, parser.getMaxErrors());
    }
    
    @Test
    public void testGetErrors() {
        treeBuilder = new HtmlTreeBuilder();
        parser = new Parser(treeBuilder);
        
        List<ParseError> errors = parser.getErrors();
        
        Assert.assertNotNull(errors);
        Assert.assertEquals(0, errors.size());
    }
    
    @Test
    public void testParse() {
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        String baseUri = "https://example.com";
        
        Document doc = Parser.parse(html, baseUri);
        
        Assert.assertNotNull(doc);
        Assert.assertEquals(html, doc.html());
    }
    
    @Test
    public void testParseFragment() {
        String fragmentHtml = "<h1>Hello, World!</h1>";
        Element context = new Element("div");
        String baseUri = "https://example.com";
        
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
        
        Assert.assertNotNull(nodes);
        Assert.assertEquals(1, nodes.size());
        Assert.assertEquals(fragmentHtml, nodes.get(0).outerHtml());
    }
    
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello, World!</h1>";
        String baseUri = "https://example.com";
        
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        Assert.assertNotNull(doc);
        Assert.assertEquals(bodyHtml, doc.body().html());
    }
    
    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<h1>Hello, World!</h1>";
        String baseUri = "https://example.com";
        
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        
        Assert.assertNotNull(doc);
        Assert.assertEquals(bodyHtml, doc.html());
    }
    
    @Test
    public void testHtmlParser() {
        Parser parser = Parser.htmlParser();
        
        Assert.assertNotNull(parser);
        Assert.assertEquals(HtmlTreeBuilder.class, parser.getTreeBuilder().getClass());
    }
    
    @Test
    public void testXmlParser() {
        Parser parser = Parser.xmlParser();
        
        Assert.assertNotNull(parser);
        Assert.assertEquals(XmlTreeBuilder.class, parser.getTreeBuilder().getClass());
    }
}