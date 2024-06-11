import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;

import java.util.List;

public class ParserTest {
    private Parser parser;
    
    @Before
    public void setup() {
        parser = new Parser(new TreeBuilder());
    }

    @Test
    public void testParseInput() {
        String html = "<html><head><title>Test</title></head><body></body></html>";
        String baseUri = "http://example.com";

        Document doc = parser.parseInput(html, baseUri);

        Assert.assertNotNull(doc);
        Assert.assertEquals("Test", doc.title());
        Assert.assertEquals(baseUri, doc.baseUri());
    }
    
    @Test
    public void testGetTreeBuilder() {
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        
        Assert.assertNotNull(treeBuilder);
    }
    
    @Test
    public void testSetTreeBuilder() {
        TreeBuilder treeBuilder = new TreeBuilder();
        
        Parser newParser = parser.setTreeBuilder(treeBuilder);
        
        Assert.assertNotNull(newParser);
        Assert.assertEquals(treeBuilder, newParser.getTreeBuilder());
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
        
        Assert.assertNotNull(newParser);
        Assert.assertTrue(newParser.isTrackErrors());
    }
    
    @Test
    public void testGetErrors() {
        List<ParseError> errors = parser.getErrors();
        
        Assert.assertNotNull(errors);
    }
    
    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body></body></html>";
        String baseUri = "http://example.com";

        Document doc = Parser.parse(html, baseUri);

        Assert.assertNotNull(doc);
        Assert.assertEquals("Test", doc.title());
        Assert.assertEquals(baseUri, doc.baseUri());
    }
    
    @Test
    public void testParseFragment() {
        String fragmentHtml = "<div><p>Hello World</p></div>";
        Element context = new Element("div");
        String baseUri = "http://example.com";

        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);

        Assert.assertNotNull(nodes);
        Assert.assertEquals(1, nodes.size());
        Assert.assertEquals("Hello World", nodes.get(0).toString());
    }
    
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello World</p>";
        String baseUri = "http://example.com";

        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

        Assert.assertNotNull(doc);
        Assert.assertEquals("Hello World", doc.body().text());
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