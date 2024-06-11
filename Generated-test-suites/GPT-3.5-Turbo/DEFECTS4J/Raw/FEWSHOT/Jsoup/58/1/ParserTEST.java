import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.HtmlTreeBuilder;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.CharacterReader;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ParserTest {
    private Parser parser;
    private TreeBuilder treeBuilder;
    private ParseSettings settings;

    @Before
    public void setup() {
        treeBuilder = new HtmlTreeBuilder();
        settings = treeBuilder.defaultSettings();
        parser = new Parser(treeBuilder);
    }
    
    @Test
    public void testConstructor() {
        assertNotNull(parser);
        assertEquals(treeBuilder, parser.getTreeBuilder());
        assertEquals(settings, parser.settings());
    }
    
    @Test
    public void testParseInput() {
        String html = "<html><body><h1>Hello World!</h1></body></html>";       
        String baseUri = "http://www.example.com";
        
        Document doc = parser.parseInput(html, baseUri);
        
        assertNotNull(doc);
        assertEquals("<html>\n <head></head>\n <body>\n  <h1>Hello World!</h1>\n </body>\n</html>", doc.html());
    }
    
    @Test
    public void testGetTreeBuilder() {
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        
        assertNotNull(treeBuilder);
        assertTrue(treeBuilder instanceof HtmlTreeBuilder);
    }
    
    @Test
    public void testSetTreeBuilder() {
        TreeBuilder newTreeBuilder = new XmlTreeBuilder();
        
        parser.setTreeBuilder(newTreeBuilder);
        
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        
        assertNotNull(treeBuilder);
        assertTrue(treeBuilder instanceof XmlTreeBuilder);
    }
    
    @Test
    public void testIsTrackErrors() {
        assertFalse(parser.isTrackErrors());
        
        parser.setTrackErrors(10);
        
        assertTrue(parser.isTrackErrors());
    }
    
    @Test
    public void testSetTrackErrors() {
        parser.setTrackErrors(5);
        
        assertTrue(parser.isTrackErrors());
        assertEquals(5, parser.getErrors().size());
    }
    
    @Test
    public void testGetErrors() {
        List<ParseError> errors = parser.getErrors();
        
        assertNotNull(errors);
        assertTrue(errors.isEmpty());
    }
    
    @Test
    public void testSettings() {
        ParseSettings newSettings = ParseSettings.preserveCase();
        
        parser.settings(newSettings);
        
        assertEquals(newSettings, parser.settings());
    }
    
    @Test
    public void testParse() {
        String html = "<html><body><h1>Hello World!</h1></body></html>";       
        String baseUri = "http://www.example.com";
        
        Document doc = Parser.parse(html, baseUri);
        
        assertNotNull(doc);
        assertEquals("<html>\n <head></head>\n <body>\n  <h1>Hello World!</h1>\n </body>\n</html>", doc.html());
    }
    
    @Test
    public void testParseFragment() {
        String fragmentHtml = "<h1>Hello World!</h1>";
        Element context = new Element("div");
        String baseUri = "http://www.example.com";
        
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
        
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        assertEquals("h1", ((Element) nodes.get(0)).tagName());
    }
    
    @Test
    public void testParseXmlFragment() {
        String fragmentXml = "<h1>Hello World!</h1>";
        String baseUri = "http://www.example.com";
        
        List<Node> nodes = Parser.parseXmlFragment(fragmentXml, baseUri);
        
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        assertEquals("h1", ((Element) nodes.get(0)).tagName());
    }
    
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World!</h1>";
        String baseUri = "http://www.example.com";
        
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        assertNotNull(doc);
        assertEquals("<html>\n <head></head>\n <body>\n  <h1>Hello World!</h1>\n </body>\n</html>", doc.html());
    }
    
    @Test
    public void testUnescapeEntities() {
        String string = "&lt;h1&gt;Hello World!&lt;/h1&gt;";
        boolean inAttribute = false;
        
        String unescapedString = Parser.unescapeEntities(string, inAttribute);
        
        assertEquals("<h1>Hello World!</h1>", unescapedString);
    }
    
    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<h1>Hello World!</h1>";
        String baseUri = "http://www.example.com";
        
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        
        assertNotNull(doc);
        assertEquals("<html>\n <head></head>\n <body>\n  <h1>Hello World!</h1>\n </body>\n</html>", doc.html());
    }
    
    @Test
    public void testHtmlParser() {
        Parser htmlParser = Parser.htmlParser();
        
        assertNotNull(htmlParser);
        assertTrue(htmlParser.getTreeBuilder() instanceof HtmlTreeBuilder);
    }
    
    @Test
    public void testXmlParser() {
        Parser xmlParser = Parser.xmlParser();
        
        assertNotNull(xmlParser);
        assertTrue(xmlParser.getTreeBuilder() instanceof XmlTreeBuilder);
    }
}