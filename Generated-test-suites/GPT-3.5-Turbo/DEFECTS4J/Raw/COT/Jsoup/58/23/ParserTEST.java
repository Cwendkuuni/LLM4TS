
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

public class ParserTest {
    private static Parser parser;

    @BeforeClass
    public static void setUp() {
        parser = new Parser(new HtmlTreeBuilder());
    }
    
    @Test
    public void testParseInput() {
        String html = "<html><head></head><body><h1>Hello, world!</h1></body></html>";
        String baseUri = "https://www.example.com";
        Document document = parser.parseInput(html, baseUri);
        
        Assert.assertNotNull(document);
        Assert.assertEquals(baseUri, document.baseUri());
        Assert.assertNotNull(document.head());
        Assert.assertNotNull(document.body());
        Assert.assertEquals("Hello, world!", document.select("h1").text());
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
        Assert.assertFalse(parser.isTrackErrors());
    }
    
    @Test
    public void testSetTrackErrors() {
        int maxErrors = 10;
        parser.setTrackErrors(maxErrors);
        
        Assert.assertTrue(parser.isTrackErrors());
        Assert.assertEquals(maxErrors, parser.getTrackErrors());
    }
    
    @Test
    public void testGetErrors() {
        List<ParseError> errorList = parser.getErrors();
        
        Assert.assertNotNull(errorList);
    }
    
    @Test
    public void testSettings() {
        ParseSettings settings = new ParseSettings(false);
        parser.settings(settings);
        
        Assert.assertEquals(settings, parser.settings());
    }
    
    @Test
    public void testParse() {
        String html = "<html><head></head><body><h1>Hello, world!</h1></body></html>";
        String baseUri = "https://www.example.com";
        Document document = Parser.parse(html, baseUri);
        
        Assert.assertNotNull(document);
        Assert.assertEquals(baseUri, document.baseUri());
        Assert.assertNotNull(document.head());
        Assert.assertNotNull(document.body());
        Assert.assertEquals("Hello, world!", document.select("h1").text());
    }
    
    @Test
    public void testParseFragment() {
        String fragmentHtml = "<div>Hello, world!</div>";
        Element context = new Element("body");
        String baseUri = "https://www.example.com";
        List<Node> nodeList = Parser.parseFragment(fragmentHtml, context, baseUri);
        
        Assert.assertNotNull(nodeList);
        Assert.assertEquals(1, nodeList.size());
        Assert.assertEquals("Hello, world!", nodeList.get(0).outerHtml());
    }
    
    @Test
    public void testParseXmlFragment() {
        String fragmentXml = "<div>Hello, world!</div>";
        String baseUri = "https://www.example.com";
        List<Node> nodeList = Parser.parseXmlFragment(fragmentXml, baseUri);
        
        Assert.assertNotNull(nodeList);
        Assert.assertEquals(1, nodeList.size());
        Assert.assertEquals("Hello, world!", nodeList.get(0).outerHtml());
    }
    
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello, world!</h1>";
        String baseUri = "https://www.example.com";
        Document document = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        Assert.assertNotNull(document);
        Assert.assertEquals(baseUri, document.baseUri());
        Assert.assertNotNull(document.head());
        Assert.assertNotNull(document.body());
        Assert.assertEquals("Hello, world!", document.select("h1").text());
    }
    
    @Test
    public void testUnescapeEntities() {
        String string = "&lt;div&gt;Hello, world!&lt;/div&gt;";
        boolean inAttribute = false;
        String unescapedString = Parser.unescapeEntities(string, inAttribute);
        
        Assert.assertEquals("<div>Hello, world!</div>", unescapedString);
    }
    
    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<h1>Hello, world!</h1>";
        String baseUri = "https://www.example.com";
        Document document = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        
        Assert.assertNotNull(document);
        Assert.assertEquals(baseUri, document.baseUri());
        Assert.assertNotNull(document.head());
        Assert.assertNotNull(document.body());
        Assert.assertEquals("Hello, world!", document.select("h1").text());
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
