
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class ParserTest {
    
    private Parser parser;
    
    @Before
    public void setUp() {
        parser = new Parser(new HtmlTreeBuilder());
    }
    
    @After
    public void tearDown() {
        parser = null;
    }
    
    @Test
    public void testParseInput() {
        String html = "<html><body><h1>Hello World!</h1></body></html>";
        String baseUri = "https://example.com";
        Document doc = parser.parseInput(html, baseUri);
        
        assertNotNull(doc);
        assertEquals("Hello World!", doc.select("h1").text());
    }
    
    @Test
    public void testGetTreeBuilder() {
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        
        assertNotNull(treeBuilder);
        assertTrue(treeBuilder instanceof HtmlTreeBuilder);
    }
    
    @Test
    public void testSetTreeBuilder() {
        TreeBuilder treeBuilder = new XmlTreeBuilder();
        parser.setTreeBuilder(treeBuilder);
        
        assertEquals(treeBuilder, parser.getTreeBuilder());
    }
    
    @Test
    public void testIsTrackErrors() {
        assertFalse(parser.isTrackErrors());
        
        parser.setTrackErrors(10);
        assertTrue(parser.isTrackErrors());
        
        parser.setTrackErrors(0);
        assertFalse(parser.isTrackErrors());
    }
    
    @Test
    public void testSetTrackErrors() {
        parser.setTrackErrors(5);
        assertTrue(parser.isTrackErrors());
        
        parser.setTrackErrors(0);
        assertFalse(parser.isTrackErrors());
    }
    
    @Test
    public void testGetErrors() {
        List<ParseError> errors = parser.getErrors();
        
        assertNotNull(errors);
        assertTrue(errors.isEmpty());
    }
    
    @Test
    public void testParse() {
        String html = "<html><body><h1>Hello World!</h1></body></html>";
        String baseUri = "https://example.com";
        Document doc = Parser.parse(html, baseUri);
        
        assertNotNull(doc);
        assertEquals("Hello World!", doc.select("h1").text());
    }
    
    @Test
    public void testParseFragment() {
        String fragmentHtml = "<div><span>Test</span></div>";
        Element context = new Element("div");
        String baseUri = "https://example.com";
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
        
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertEquals("Test", nodes.get(0).text());
    }
    
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<div><span>Test</span></div>";
        String baseUri = "https://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        assertNotNull(doc);
        assertEquals("Test", doc.select("span").text());
    }
    
    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<h1>Hello World!</h1>";
        String baseUri = "https://example.com";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        
        assertNotNull(doc);
        assertEquals("Hello World!", doc.select("h1").text());
    }
    
    @Test
    public void testHtmlParser() {
        Parser htmlParser = Parser.htmlParser();
        
        assertNotNull(htmlParser.getTreeBuilder());
        assertTrue(htmlParser.getTreeBuilder() instanceof HtmlTreeBuilder);
    }
    
    @Test
    public void testXmlParser() {
        Parser xmlParser = Parser.xmlParser();
        
        assertNotNull(xmlParser.getTreeBuilder());
        assertTrue(xmlParser.getTreeBuilder() instanceof XmlTreeBuilder);
    }
}

