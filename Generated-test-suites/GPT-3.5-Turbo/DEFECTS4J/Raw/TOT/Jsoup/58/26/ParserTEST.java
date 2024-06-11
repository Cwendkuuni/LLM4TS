import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.TreeBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class ParserTest {
    private Parser parser;
    
    @Before
    public void setup() {
        parser = new Parser(new TreeBuilder());
    }
    
    @Test
    public void testParseInput() {
        String html = "<html><body><h1>Hello world!</h1></body></html>";
        String baseUri = "http://example.com";
        
        Document document = parser.parseInput(html, baseUri);
        
        Assert.assertNotNull(document);
        Assert.assertEquals("http://example.com", document.baseUri());
        Assert.assertEquals("<html>\n <head></head>\n <body>\n  <h1>Hello world!</h1>\n </body>\n</html>", document.outerHtml());
    }
    
    @Test
    public void testGetTreeBuilder() {
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        
        Assert.assertNotNull(treeBuilder);
    }
    
    @Test
    public void testSetTreeBuilder() {
        Parser newParser = parser.setTreeBuilder(new TreeBuilder());
        
        Assert.assertEquals(new TreeBuilder(), newParser.getTreeBuilder());
    }
    
    @Test
    public void testIsTrackErrors() {
        boolean trackErrors = parser.isTrackErrors();
        
        Assert.assertFalse(trackErrors);
    }
    
    @Test
    public void testSetTrackErrors() {
        Parser newParser = parser.setTrackErrors(5);
        
        Assert.assertTrue(newParser.isTrackErrors());
        Assert.assertEquals(5, newParser.getErrors().size());
    }
    
    @Test
    public void testGetErrors() {
        List<ParseError> errors = parser.getErrors();
        
        Assert.assertNotNull(errors);
        Assert.assertTrue(errors.isEmpty());
    }
    
    @Test
    public void testSettings() {
        ParseSettings settings = new ParseSettings(true, true);
        
        Parser newParser = parser.settings(settings);
        
        Assert.assertEquals(settings, newParser.settings());
    }
    
    @Test
    public void testParse() {
        String html = "<html><body><h1>Hello world!</h1></body></html>";
        String baseUri = "http://example.com";
        
        Document document = Parser.parse(html, baseUri);
        
        Assert.assertNotNull(document);
        Assert.assertEquals("http://example.com", document.baseUri());
        Assert.assertEquals("<html>\n <head></head>\n <body>\n  <h1>Hello world!</h1>\n </body>\n</html>", document.outerHtml());
    }
    
    @Test
    public void testParseFragment() {
        String fragmentHtml = "<h1>Hello world!</h1>";
        Element context = new Element("div");
        String baseUri = "http://example.com";
        
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
        
        Assert.assertNotNull(nodes);
        Assert.assertEquals(1, nodes.size());
        Assert.assertEquals("<h1>Hello world!</h1>", nodes.get(0).outerHtml());
    }
    
    @Test
    public void testParseXmlFragment() {
        String fragmentXml = "<tag>Hello world!</tag>";
        String baseUri = "http://example.com";
        
        List<Node> nodes = Parser.parseXmlFragment(fragmentXml, baseUri);
        
        Assert.assertNotNull(nodes);
        Assert.assertEquals(1, nodes.size());
        Assert.assertEquals("<tag>Hello world!</tag>", nodes.get(0).outerHtml());
    }
    
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello world!</h1>";
        String baseUri = "http://example.com";
        
        Document document = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        Assert.assertNotNull(document);
        Assert.assertEquals("<html>\n <head></head>\n <body>\n  <h1>Hello world!</h1>\n </body>\n</html>", document.outerHtml());
    }
    
    @Test
    public void testUnescapeEntities() {
        String escapedString = "&lt;h1&gt;Hello world!&lt;/h1&gt;";
        boolean inAttribute = false;
        
        String unescapedString = Parser.unescapeEntities(escapedString, inAttribute);
        
        Assert.assertEquals("<h1>Hello world!</h1>", unescapedString);
    }
    
    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<h1>Hello world!</h1>";
        String baseUri = "http://example.com";
        
        Document document = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        
        Assert.assertNotNull(document);
        Assert.assertEquals("<html>\n <head></head>\n <body>\n  <h1>Hello world!</h1>\n </body>\n</html>", document.outerHtml());
    }
}