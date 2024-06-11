import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.HtmlTreeBuilder;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.List;

public class ParserTest {
    
    private Parser parser;
    
    @Before
    public void setUp() {
        parser = new Parser(new TreeBuilder());
    }
    
    @Test
    public void testParseInput() {
        String html = "<html><body><h1>Hello World!</h1></body></html>";
        String baseUri = "http://example.com";
        Document document = parser.parseInput(html, baseUri);
        
        assertNotNull(document);
        assertEquals(html, document.html());
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
        int maxErrors = 5;
        parser.setTrackErrors(maxErrors);
        
        assertTrue(parser.isTrackErrors());
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
        parser.settings(settings);
        
        assertEquals(settings, parser.settings());
    }
    
    @Test
    public void testParse() {
        String html = "<html><body><h1>Hello World!</h1></body></html>";
        String baseUri = "http://example.com";
        Document document = Parser.parse(html, baseUri);
        
        assertNotNull(document);
        assertEquals(html, document.html());
    }
    
    @Test
    public void testParseFragment() {
        String fragmentHtml = "<h1>Hello World!</h1>";
        Element context = new Element("div");
        String baseUri = "http://example.com";
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
        
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertEquals(fragmentHtml, nodes.get(0).outerHtml());
    }
    
    @Test
    public void testParseXmlFragment() {
        String fragmentXml = "<h1>Hello World!</h1>";
        String baseUri = "http://example.com";
        List<Node> nodes = Parser.parseXmlFragment(fragmentXml, baseUri);
        
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertEquals(fragmentXml, nodes.get(0).outerHtml());
    }
    
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World!</h1>";
        String baseUri = "http://example.com";
        Document document = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        assertNotNull(document);
        assertEquals(bodyHtml, document.body().html());
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
        String baseUri = "http://example.com";
        Document document = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        
        assertNotNull(document);
        assertEquals(bodyHtml, document.html());
    }
    
    @Test
    public void testHtmlParser() {
        Parser htmlParser = Parser.htmlParser();
        TreeBuilder treeBuilder = htmlParser.getTreeBuilder();
        
        assertNotNull(htmlParser);
        assertNotNull(treeBuilder);
    }
    
    @Test
    public void testXmlParser() {
        Parser xmlParser = Parser.xmlParser();
        TreeBuilder treeBuilder = xmlParser.getTreeBuilder();
        
        assertNotNull(xmlParser);
        assertNotNull(treeBuilder);
    }
}