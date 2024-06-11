import org.jsoup.Jsoup;
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
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

public class ParserTest {
    
    private Parser parser;
    private TreeBuilder treeBuilder;
    private ParseErrorList errors;
    private ParseSettings settings;
    
    @Before
    public void setUp() {
        treeBuilder = new HtmlTreeBuilder();
        parser = new Parser(treeBuilder);
        errors = ParseErrorList.noTracking();
        settings = treeBuilder.defaultSettings();
    }
    
    @Test
    public void testGetTreeBuilder() {
        assertEquals(treeBuilder, parser.getTreeBuilder());
    }
    
    @Test
    public void testSetTreeBuilder() {
        TreeBuilder newTreeBuilder = new XmlTreeBuilder();
        parser.setTreeBuilder(newTreeBuilder);
        assertEquals(newTreeBuilder, parser.getTreeBuilder());
    }
    
    @Test
    public void testIsTrackErrors() {
        assertFalse(parser.isTrackErrors());
        parser.setTrackErrors(1);
        assertTrue(parser.isTrackErrors());
    }
    
    @Test
    public void testSetTrackErrors() {
        parser.setTrackErrors(2);
        assertEquals(2, parser.getErrors().size());
        parser.setTrackErrors(0);
        assertEquals(0, parser.getErrors().size());
    }
    
    @Test
    public void testGetErrors() {
        assertEquals(errors, parser.getErrors());
        parser.setTrackErrors(1);
        assertTrue(parser.getErrors() instanceof List);
    }
    
    @Test
    public void testSettings() {
        ParseSettings newSettings = new ParseSettings(false, true);
        parser.settings(newSettings);
        assertEquals(newSettings, parser.settings());
    }
    
    @Test
    public void testParseInput() {
        String html = "<html><body>Hello, World!</body></html>";
        String baseUri = "http://www.example.com";
        
        Document document = parser.parseInput(html, baseUri);
        assertNotNull(document);
        assertEquals(html, document.html());
        assertEquals(baseUri, document.baseUri());
    }
    
    @Test
    public void testParse() {
        String html = "<html><body>Hello, World!</body></html>";
        String baseUri = "http://www.example.com";
        
        Document document = Parser.parse(html, baseUri);
        assertNotNull(document);
        assertEquals(html, document.html());
        assertEquals(baseUri, document.baseUri());
    }
    
    @Test
    public void testParseFragment() {
        String fragmentHtml = "<div><p>Hello, World!</p></div>";
        Element context = new Element("div");
        String baseUri = "http://www.example.com";
        
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertEquals("div", nodes.get(0).nodeName());
        assertEquals("p", nodes.get(0).child(0).nodeName());
    }
    
    @Test
    public void testParseXmlFragment() {
        String fragmentXml = "<xml><message>Hello, World!</message></xml>";
        String baseUri = "http://www.example.com";
        
        List<Node> nodes = Parser.parseXmlFragment(fragmentXml, baseUri);
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertEquals("xml", nodes.get(0).nodeName());
        assertEquals("message", nodes.get(0).child(0).nodeName());
    }
    
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<div><p>Hello, World!</p></div>";
        String baseUri = "http://www.example.com";
        
        Document document = Parser.parseBodyFragment(bodyHtml, baseUri);
        assertNotNull(document);
        assertEquals(1, document.body().children().size());
        assertEquals("div", document.body().child(0).nodeName());
        assertEquals("p", document.body().child(0).child(0).nodeName());
    }
    
    @Test
    public void testUnescapeEntities() {
        String html = "Hello, &lt;World&gt;!";
        String unescaped = Parser.unescapeEntities(html, true);
        assertEquals("Hello, <World>!", unescaped);
    }
    
    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<div><p>Hello, World!</p></div>";
        String baseUri = "http://www.example.com";
        
        Document document = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        assertNotNull(document);
        assertEquals(bodyHtml, document.html());
        assertEquals(baseUri, document.baseUri());
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