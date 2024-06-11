import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class ParserTest {
    
    private Parser parser;
    
    @Before
    public void setUp() {
        parser = new Parser(new TreeBuilder());
    }
    
    @Test
    public void testParseInput_typicalCase() {
        String html = "<html><body><h1>Title</h1><p>Paragraph</p></body></html>";
        String baseUri = "https://example.com";
        
        Document result = parser.parseInput(html, baseUri);
        
        Assert.assertNotNull(result);
        Assert.assertEquals("https://example.com", result.baseUri());
        Assert.assertEquals("Title", result.body().select("h1").text());
        Assert.assertEquals("Paragraph", result.body().select("p").text());
    }
    
    @Test
    public void testParseInput_emptyHtml() {
        String html = "";
        String baseUri = "https://example.com";
        
        Document result = parser.parseInput(html, baseUri);
        
        Assert.assertNotNull(result);
        Assert.assertEquals("https://example.com", result.baseUri());
        Assert.assertTrue(result.body().children().isEmpty());
    }
    
    @Test
    public void testParseInput_nullHtml() {
        String baseUri = "https://example.com";
        
        Document result = parser.parseInput(null, baseUri);
        
        Assert.assertNull(result);
    }
    
    @Test
    public void testGetTreeBuilder_typicalCase() {
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        
        Assert.assertNotNull(treeBuilder);
    }
    
    @Test
    public void testSetTreeBuilder_typicalCase() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        
        parser.setTreeBuilder(treeBuilder);
        
        Assert.assertEquals(treeBuilder, parser.getTreeBuilder());
    }
    
    @Test
    public void testIsTrackErrors_enabled() {
        parser.setTrackErrors(5);
        
        boolean result = parser.isTrackErrors();
        
        Assert.assertTrue(result);
    }
    
    @Test
    public void testIsTrackErrors_disabled() {
        parser.setTrackErrors(0);
        
        boolean result = parser.isTrackErrors();
        
        Assert.assertFalse(result);
    }
    
    @Test
    public void testSetTrackErrors_typicalCase() {
        parser.setTrackErrors(10);
        
        int maxErrors = parser.getMaxErrors();
        
        Assert.assertEquals(10, maxErrors);
    }
    
    @Test
    public void testGetErrors_typicalCase() {
        String html = "<h1>Invalid HTML</h2>";
        String baseUri = "https://example.com";
        
        parser.setTrackErrors(3);
        Document result = parser.parseInput(html, baseUri);
        List<ParseError> errors = parser.getErrors();
        
        Assert.assertNotNull(errors);
        Assert.assertEquals(2, errors.size());
        Assert.assertEquals("Unexpected closing tag h2", errors.get(0).getErrorMessage());
        Assert.assertEquals("Unexpected closing tag /h2", errors.get(1).getErrorMessage());
    }
    
    @Test
    public void testParse_typicalCase() {
        String html = "<html><body><h1>Title</h1><p>Paragraph</p></body></html>";
        String baseUri = "https://example.com";
        
        Document result = Parser.parse(html, baseUri);
        
        Assert.assertNotNull(result);
        Assert.assertEquals("https://example.com", result.baseUri());
        Assert.assertEquals("Title", result.body().select("h1").text());
        Assert.assertEquals("Paragraph", result.body().select("p").text());
    }
    
    @Test
    public void testParseFragment_typicalCase() {
        String fragmentHtml = "<h1>Title</h1><p>Paragraph</p>";
        Element context = new Element("div");
        String baseUri = "https://example.com";
        
        List<Node> result = Parser.parseFragment(fragmentHtml, context, baseUri);
        
        Assert.assertNotNull(result);
        Assert.assertEquals(2, result.size());
        Assert.assertEquals("h1", result.get(0).nodeName());
        Assert.assertEquals("p", result.get(1).nodeName());
    }
    
    @Test
    public void testParseBodyFragment_typicalCase() {
        String bodyHtml = "<h1>Title</h1><p>Paragraph</p>";
        String baseUri = "https://example.com";
        
        Document result = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        Assert.assertNotNull(result);
        Assert.assertEquals("https://example.com", result.baseUri());
        Assert.assertEquals("Title", result.body().select("h1").text());
        Assert.assertEquals("Paragraph", result.body().select("p").text());
    }
    
    @Test
    public void testParseBodyFragmentRelaxed_typicalCase() {
        String bodyHtml = "<h1>Title</h1><p>Paragraph</p>";
        String baseUri = "https://example.com";
        
        Document result = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        
        Assert.assertNotNull(result);
        Assert.assertEquals("https://example.com", result.baseUri());
        Assert.assertEquals("Title", result.body().select("h1").text());
        Assert.assertEquals("Paragraph", result.body().select("p").text());
    }
    
    @Test
    public void testHtmlParser_typicalCase() {
        Parser htmlParser = Parser.htmlParser();
        TreeBuilder treeBuilder = htmlParser.getTreeBuilder();
        
        Assert.assertNotNull(htmlParser);
        Assert.assertTrue(treeBuilder instanceof HtmlTreeBuilder);
    }
    
    @Test
    public void testXmlParser_typicalCase() {
        Parser xmlParser = Parser.xmlParser();
        TreeBuilder treeBuilder = xmlParser.getTreeBuilder();
        
        Assert.assertNotNull(xmlParser);
        Assert.assertTrue(treeBuilder instanceof XmlTreeBuilder);
    }
}