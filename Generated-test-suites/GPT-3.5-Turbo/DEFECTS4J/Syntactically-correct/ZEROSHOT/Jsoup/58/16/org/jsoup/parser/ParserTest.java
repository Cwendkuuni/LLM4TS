import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class ParserTest {
    private Parser parser;
    
    @Before
    public void setUp() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        parser = new Parser(treeBuilder);
    }
    
    @Test
    public void testParseInput() {
        String html = "<html><head></head><body><div>Hello World</div></body></html>";
        String baseUri = "http://example.com";
        Document doc = parser.parseInput(html, baseUri);
        
        Assert.assertNotNull(doc);
        Assert.assertEquals(html, doc.html());
        Assert.assertEquals(baseUri, doc.baseUri());
    }
    
    @Test
    public void testGetTreeBuilder() {
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        Assert.assertNotNull(treeBuilder);
    }
    
    @Test
    public void testSetTreeBuilder() {
        TreeBuilder newTreeBuilder = new XmlTreeBuilder();
        Parser newParser = parser.setTreeBuilder(newTreeBuilder);
        
        Assert.assertEquals(newTreeBuilder, newParser.getTreeBuilder());
    }
    
    @Test
    public void testIsTrackErrors() {
        boolean trackErrors = parser.isTrackErrors();
        Assert.assertFalse(trackErrors);
    }
    
    @Test
    public void testSetTrackErrors() {
        int maxErrors = 5;
        Parser newParser = parser.setTrackErrors(maxErrors);
        
        Assert.assertTrue(newParser.isTrackErrors());
        Assert.assertEquals(maxErrors, newParser.getTrackErrors());
    }
    
    @Test
    public void testGetErrors() {
        List<ParseError> errors = parser.getErrors();
        Assert.assertNotNull(errors);
    }
    
    @Test
    public void testSettings() {
        ParseSettings settings = new ParseSettings(true);
        Parser newParser = parser.settings(settings);
        
        Assert.assertEquals(settings, newParser.settings());
    }
    
    @Test
    public void testParse() {
        String html = "<html><head></head><body><div>Hello World</div></body></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        
        Assert.assertNotNull(doc);
        Assert.assertEquals(html, doc.html());
        Assert.assertEquals(baseUri, doc.baseUri());
    }
    
    @Test
    public void testParseFragment() {
        String fragmentHtml = "<div>Hello World</div>";
        Element context = new Element("body");
        String baseUri = "http://example.com";
        List<Node> nodeList = Parser.parseFragment(fragmentHtml, context, baseUri);
        
        Assert.assertNotNull(nodeList);
        Assert.assertEquals(1, nodeList.size());
    }
    
    @Test
    public void testParseXmlFragment() {
        String fragmentXml = "<root><child>Hello World</child></root>";
        String baseUri = "http://example.com";
        List<Node> nodeList = Parser.parseXmlFragment(fragmentXml, baseUri);
        
        Assert.assertNotNull(nodeList);
        Assert.assertEquals(1, nodeList.size());
    }
    
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<div>Hello World</div>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        Assert.assertNotNull(doc);
        Assert.assertEquals(bodyHtml, doc.body().html());
    }
    
    @Test
    public void testUnescapeEntities() {
        String string = "&lt;div&gt;Hello World&lt;/div&gt;";
        boolean inAttribute = false;
        String unescapedString = Parser.unescapeEntities(string, inAttribute);
        
        Assert.assertEquals("<div>Hello World</div>", unescapedString);
    }
    
    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<div>Hello World</div>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        
        Assert.assertNotNull(doc);
        Assert.assertEquals(bodyHtml, doc.html());
    }
    
    @Test
    public void testHtmlParser() {
        Parser newParser = Parser.htmlParser();
        Assert.assertNotNull(newParser);
    }
    
    @Test
    public void testXmlParser() {
        Parser newParser = Parser.xmlParser();
        Assert.assertNotNull(newParser);
    }
}