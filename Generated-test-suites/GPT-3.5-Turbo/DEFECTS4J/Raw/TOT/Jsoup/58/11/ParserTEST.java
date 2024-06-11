import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.ParseError;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class ParserTest {
    
    private Parser parser;
    
    @Before
    public void setUp() {
        parser = new Parser(new TreeBuilderMock());
    }
    
    @Test
    public void testParseInput() {
        String html = "<html><body><h1>Title</h1><p>Content</p></body></html>";
        String baseUri = "http://example.com";
        
        Document document = parser.parseInput(html, baseUri);
        
        Assert.assertNotNull(document);
        Assert.assertEquals("http://example.com", document.baseUri());
        Assert.assertEquals("<html>\n <head></head>\n <body>\n  <h1>Title</h1>\n  <p>Content</p>\n </body>\n</html>", document.outerHtml());
    }
    
    @Test
    public void testGetTreeBuilder() {
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        
        Assert.assertNotNull(treeBuilder);
        Assert.assertEquals(TreeBuilderMock.class, treeBuilder.getClass());
    }
    
    @Test
    public void testSetTreeBuilder() {
        TreeBuilder treeBuilder = new TreeBuilderMock();
        
        Parser newParser = parser.setTreeBuilder(treeBuilder);
        
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
        
        Assert.assertTrue(newParser.isTrackErrors());
        Assert.assertEquals(5, newParser.getMaxErrors());
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
        
        Parser newParser = parser.settings(settings);
        
        Assert.assertEquals(settings, newParser.settings());
    }
    
    @Test
    public void testParse() {
        String html = "<html><body><h1>Title</h1><p>Content</p></body></html>";
        String baseUri = "http://example.com";
        
        Document document = Parser.parse(html, baseUri);
        
        Assert.assertNotNull(document);
        Assert.assertEquals("http://example.com", document.baseUri());
        Assert.assertEquals("<html>\n <head></head>\n <body>\n  <h1>Title</h1>\n  <p>Content</p>\n </body>\n</html>", document.outerHtml());
    }
    
    @Test
    public void testParseFragment() {
        String fragmentHtml = "<h1>Title</h1><p>Content</p>";
        Element context = new Element("div");
        String baseUri = "http://example.com";
        
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
        
        Assert.assertNotNull(nodes);
        Assert.assertEquals(2, nodes.size());
    }
    
    @Test
    public void testParseXmlFragment() {
        String fragmentXml = "<root><child1>Value 1</child1><child2>Value 2</child2></root>";
        String baseUri = "http://example.com";
        
        List<Node> nodes = Parser.parseXmlFragment(fragmentXml, baseUri);
        
        Assert.assertNotNull(nodes);
        Assert.assertEquals(3, nodes.size());
    }
    
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Title</h1><p>Content</p>";
        String baseUri = "http://example.com";
        
        Document document = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        Assert.assertNotNull(document);
        Assert.assertEquals("http://example.com", document.baseUri());
        Assert.assertEquals("<html>\n <head></head>\n <body>\n  <h1>Title</h1>\n  <p>Content</p>\n </body>\n</html>", document.outerHtml());
    }
    
    @Test
    public void testUnescapeEntities() {
        String escapedString = "&lt;div&gt;Hello&lt;/div&gt;";
        boolean inAttribute = false;
        
        String unescapedString = Parser.unescapeEntities(escapedString, inAttribute);
        
        Assert.assertEquals("<div>Hello</div>", unescapedString);
    }
    
    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<h1>Title</h1><p>Content</p>";
        String baseUri = "http://example.com";
        
        Document document = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        
        Assert.assertNotNull(document);
        Assert.assertEquals("http://example.com", document.baseUri());
        Assert.assertEquals("<html>\n <head></head>\n <body>\n  <h1>Title</h1>\n  <p>Content</p>\n </body>\n</html>", document.outerHtml());
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

class TreeBuilderMock extends TreeBuilder {
    @Override
    public Document parse(String html, String baseUri, ParseErrorList errors, ParseSettings settings) {
        return null; // Mock implementation, not used in tests
    }
    
    @Override
    public List<Node> parseFragment(String fragmentHtml, Element context, String baseUri, ParseErrorList errors, ParseSettings settings) {
        return null; // Mock implementation, not used in tests
    }
    
    @Override
    public List<Node> parseFragment(String fragmentHtml, Element context, String baseUri, ParseErrorList errors) {
        return null; // Mock implementation, not used in tests
    }
    
    @Override
    public List<Node> parseXmlFragment(String fragmentXml, String baseUri, ParseErrorList errors, ParseSettings settings) {
        return null; // Mock implementation, not used in tests
    }
}