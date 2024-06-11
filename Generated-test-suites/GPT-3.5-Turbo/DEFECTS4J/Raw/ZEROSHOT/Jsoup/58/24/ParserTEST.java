import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import java.util.List;

public class ParserTest {
    private Parser parser;
    
    @Before
    public void setUp() {
        parser = new Parser(new TreeBuilder());
    }
    
    @Test
    public void testParseInput() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        Document doc = parser.parseInput(html, baseUri);
        
        Assert.assertNotNull(doc);
        Assert.assertEquals("http://example.com", doc.baseUri());
        Assert.assertEquals("<html>\n <head></head>\n <body>\n  <h1>Hello World</h1>\n </body>\n</html>", doc.outerHtml());
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
        parser.setTrackErrors(5);
        
        Assert.assertTrue(parser.isTrackErrors());
    }
    
    @Test
    public void testGetErrors() {
        List<ParseError> errors = parser.getErrors();
        
        Assert.assertNotNull(errors);
        Assert.assertEquals(0, errors.size());
    }
    
    @Test
    public void testSettings() {
        ParseSettings settings = new ParseSettings(true, true);
        
        parser.settings(settings);
        
        Assert.assertEquals(settings, parser.settings());
    }
    
    @Test
    public void testParse() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        
        Assert.assertNotNull(doc);
        Assert.assertEquals("http://example.com", doc.baseUri());
        Assert.assertEquals("<html>\n <head></head>\n <body>\n  <h1>Hello World</h1>\n </body>\n</html>", doc.outerHtml());
    }
    
    @Test
    public void testParseFragment() {
        String fragmentHtml = "<div>Hello World</div>";
        Element context = new Element("div");
        String baseUri = "http://example.com";
        List<Node> nodeList = Parser.parseFragment(fragmentHtml, context, baseUri);
        
        Assert.assertNotNull(nodeList);
        Assert.assertEquals(1, nodeList.size());
        Assert.assertEquals("<div>Hello World</div>", nodeList.get(0).outerHtml());
    }
    
    @Test
    public void testParseXmlFragment() {
        String fragmentXml = "<div>Hello World</div>";
        String baseUri = "http://example.com";
        List<Node> nodeList = Parser.parseXmlFragment(fragmentXml, baseUri);
        
        Assert.assertNotNull(nodeList);
        Assert.assertEquals(1, nodeList.size());
        Assert.assertEquals("<div>Hello World</div>", nodeList.get(0).outerHtml());
    }
    
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<div>Hello World</div>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        Assert.assertNotNull(doc);
        Assert.assertEquals("http://example.com", doc.baseUri());
        Assert.assertEquals("<html>\n <head></head>\n <body>\n  <div>Hello World</div>\n </body>\n</html>", doc.outerHtml());
    }
    
    @Test
    public void testUnescapeEntities() {
        String string = "&lt;div&gt;Hello World&lt;/div&gt;";
        boolean inAttribute = false;
        String unescaped = Parser.unescapeEntities(string, inAttribute);
        
        Assert.assertEquals("<div>Hello World</div>", unescaped);
    }
    
    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<div>Hello World</div>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        
        Assert.assertNotNull(doc);
        Assert.assertEquals("http://example.com", doc.baseUri());
        Assert.assertEquals("<html>\n <head></head>\n <body>\n  <div>Hello World</div>\n </body>\n</html>", doc.outerHtml());
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