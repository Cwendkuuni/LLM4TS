import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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
        
        Document document = parser.parseInput(html, baseUri);
        
        assertNotNull(document);
        Element body = document.body();
        assertNotNull(body);
        List<Node> children = body.childNodes();
        assertNotNull(children);
        assertEquals(1, children.size());
        assertEquals("h1", ((Element) children.get(0)).tagName());
        assertEquals("Hello World", ((Element) children.get(0)).text());
    }
  
    @Test
    public void testGetTreeBuilder() {
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        assertNotNull(treeBuilder);
    }
  
    @Test
    public void testSetTreeBuilder() {
        TreeBuilder treeBuilder = new TreeBuilder();
        parser.setTreeBuilder(treeBuilder);
        
        assertEquals(treeBuilder, parser.getTreeBuilder());
    }
  
    @Test
    public void testIsTrackErrors() {
        boolean trackErrors = parser.isTrackErrors();
        assertFalse(trackErrors);
    }
  
    @Test
    public void testSetTrackErrors() {
        int maxErrors = 10;
        Parser newParser = parser.setTrackErrors(maxErrors);
        
        assertEquals(maxErrors, newParser.isTrackErrors());
    }
  
    @Test
    public void testGetErrors() {
        List<ParseError> errors = parser.getErrors();
        assertNotNull(errors);
        assertEquals(0, errors.size());
    }
  
    @Test
    public void testSettings() {
        ParseSettings settings = new ParseSettings();
        Parser newParser = parser.settings(settings);
        
        assertEquals(settings, newParser.settings());
    }
  
    @Test
    public void testSettings() {
        ParseSettings settings = parser.settings();
        assertNotNull(settings);
    }
  
    @Test
    public void testParse() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        
        Document document = Parser.parse(html, baseUri);
        
        assertNotNull(document);
        Element body = document.body();
        assertNotNull(body);
        List<Node> children = body.childNodes();
        assertNotNull(children);
        assertEquals(1, children.size());
        assertEquals("h1", ((Element) children.get(0)).tagName());
        assertEquals("Hello World", ((Element) children.get(0)).text());
    }
  
    @Test
    public void testParseFragment() {
        String fragmentHtml = "<h1>Hello World</h1>";
        Element context = null;
        String baseUri = "http://example.com";
        
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
        
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertEquals("h1", ((Element) nodes.get(0)).tagName());
        assertEquals("Hello World", ((Element) nodes.get(0)).text());
    }
  
    @Test
    public void testParseFragmentWithErrors() {
        String fragmentHtml = "<div><p>Hello<div>World</div></p></div>";
        Element context = null;
        String baseUri = "http://example.com";
        
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
        
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertEquals("div", ((Element) nodes.get(0)).tagName());
        List<Node> children = ((Element) nodes.get(0)).childNodes();
        assertNotNull(children);
        assertEquals(2, children.size());
        assertEquals("p", ((Element) children.get(0)).tagName());
        assertEquals("Hello", ((Element) children.get(0)).text());
        assertEquals("div", ((Element) children.get(1)).tagName());
        assertEquals("World", ((Element) children.get(1)).text());
    }
    
    @Test
    public void testParseXmlFragment() {
        String fragmentXml = "<tag>Hello World</tag>";
        String baseUri = "http://example.com";
        
        List<Node> nodes = Parser.parseXmlFragment(fragmentXml, baseUri);
        
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertEquals("tag", ((Element) nodes.get(0)).tagName());
        assertEquals("Hello World", ((Element) nodes.get(0)).text());
    }
  
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World</h1>";
        String baseUri = "http://example.com";
        
        Document document = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        assertNotNull(document);
        Element body = document.body();
        assertNotNull(body);
        List<Node> children = body.childNodes();
        assertNotNull(children);
        assertEquals(1, children.size());
        assertEquals("h1", ((Element) children.get(0)).tagName());
        assertEquals("Hello World", ((Element) children.get(0)).text());
    }
  
    @Test
    public void testUnescapeEntities() {
        String string = "&lt;div&gt;Hello World&lt;/div&gt;";
        boolean inAttribute = false;
        
        String unescapedString = Parser.unescapeEntities(string, inAttribute);
        
        assertNotNull(unescapedString);
        assertEquals("<div>Hello World</div>", unescapedString);
    }
  
    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<h1>Hello World</h1>";
        String baseUri = "http://example.com";
        
        Document document = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        
        assertNotNull(document);
        Element body = document.body();
        assertNotNull(body);
        List<Node> children = body.childNodes();
        assertNotNull(children);
        assertEquals(1, children.size());
        assertEquals("h1", ((Element) children.get(0)).tagName());
        assertEquals("Hello World", ((Element) children.get(0)).text());
    }
  
    @Test
    public void testHtmlParser() {
        Parser htmlParser = Parser.htmlParser();
        
        assertNotNull(htmlParser);
        TreeBuilder treeBuilder = htmlParser.getTreeBuilder();
        assertNotNull(treeBuilder);
        assertTrue(treeBuilder instanceof HtmlTreeBuilder);
    }
  
    @Test
    public void testXmlParser() {
        Parser xmlParser = Parser.xmlParser();
        
        assertNotNull(xmlParser);
        TreeBuilder treeBuilder = xmlParser.getTreeBuilder();
        assertNotNull(treeBuilder);
        assertTrue(treeBuilder instanceof XmlTreeBuilder);
    }
}