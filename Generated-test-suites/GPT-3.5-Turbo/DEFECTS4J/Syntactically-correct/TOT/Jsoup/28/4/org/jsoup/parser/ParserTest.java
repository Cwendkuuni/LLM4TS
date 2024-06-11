import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class ParserTest {
    
    private Parser parser;
    private TreeBuilder treeBuilder;
    
    @Before
    public void setUp() {
        treeBuilder = new HtmlTreeBuilder();
        parser = new Parser(treeBuilder);
    }
    
    @Test
    public void testParseInput_typicalCase() {
        String html = "<html><head></head><body>Hello World!</body></html>";
        String baseUri = "https://www.example.com";
        
        Document doc = parser.parseInput(html, baseUri);
        
        assertNotNull(doc);
        assertEquals(html, doc.html());
        assertEquals(baseUri, doc.baseUri());
    }
    
    @Test
    public void testParseInput_emptyHtml() {
        String html = "";
        String baseUri = "https://www.example.com";
        
        Document doc = parser.parseInput(html, baseUri);
        
        assertNotNull(doc);
        assertEquals(html, doc.html());
        assertEquals(baseUri, doc.baseUri());
    }
    
    @Test
    public void testParseInput_nullHtml() {
        String html = null;
        String baseUri = "https://www.example.com";
        
        Document doc = parser.parseInput(html, baseUri);
        
        assertNull(doc);
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
    public void testIsTrackErrors_true() {
        parser.setTrackErrors(5);
        
        assertTrue(parser.isTrackErrors());
    }
    
    @Test
    public void testIsTrackErrors_false() {
        parser.setTrackErrors(0);
        
        assertFalse(parser.isTrackErrors());
    }
    
    @Test
    public void testSetTrackErrors() {
        int maxErrors = 3;
        parser.setTrackErrors(maxErrors);
        
        assertEquals(maxErrors, parser.isTrackErrors());
    }
    
    @Test
    public void testGetErrors() {
        parser.setTrackErrors(5);
        String html = "<html><head></head><body>Hello World!</body></html>";
        String baseUri = "https://www.example.com";
        
        parser.parseInput(html, baseUri);
        List<ParseError> errors = parser.getErrors();
        
        assertNotNull(errors);
    }
    
    @Test
    public void testParse_typicalCase() {
        String html = "<html><head></head><body>Hello World!</body></html>";
        String baseUri = "https://www.example.com";
        
        Document doc = Parser.parse(html, baseUri);
        
        assertNotNull(doc);
        assertEquals(html, doc.html());
        assertEquals(baseUri, doc.baseUri());
    }
    
    @Test
    public void testParse_emptyHtml() {
        String html = "";
        String baseUri = "https://www.example.com";
        
        Document doc = Parser.parse(html, baseUri);
        
        assertNotNull(doc);
        assertEquals(html, doc.html());
        assertEquals(baseUri, doc.baseUri());
    }
    
    @Test
    public void testParse_nullHtml() {
        String html = null;
        String baseUri = "https://www.example.com";
        
        Document doc = Parser.parse(html, baseUri);
        
        assertNull(doc);
    }
    
    @Test
    public void testParseFragment_typicalCase() {
        String fragmentHtml = "<div><p>Sample text</p></div>";
        Element context = new Element("div");
        String baseUri = "https://www.example.com";
        
        List<Node> nodeList = Parser.parseFragment(fragmentHtml, context, baseUri);
        
        assertNotNull(nodeList);
        assertEquals(1, nodeList.size());
        assertTrue(nodeList.get(0) instanceof Element);
        assertEquals("div", nodeList.get(0).nodeName());
        assertEquals(fragmentHtml, nodeList.get(0).outerHtml());
    }
    
    @Test
    public void testParseFragment_emptyFragmentHtml() {
        String fragmentHtml = "";
        Element context = new Element("div");
        String baseUri = "https://www.example.com";
        
        List<Node> nodeList = Parser.parseFragment(fragmentHtml, context, baseUri);
        
        assertNotNull(nodeList);
        assertEquals(0, nodeList.size());
    }
    
    @Test
    public void testParseFragment_nullFragmentHtml() {
        String fragmentHtml = null;
        Element context = new Element("div");
        String baseUri = "https://www.example.com";
        
        List<Node> nodeList = Parser.parseFragment(fragmentHtml, context, baseUri);
        
        assertNull(nodeList);
    }
    
    @Test
    public void testParseBodyFragment_typicalCase() {
        String bodyHtml = "<p>Sample text</p>";
        String baseUri = "https://www.example.com";
        
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        assertNotNull(doc);
        assertEquals(html, doc.html());
        assertEquals(baseUri, doc.baseUri());
    }
    
    @Test
    public void testParseBodyFragment_emptyBodyHtml() {
        String bodyHtml = "";
        String baseUri = "https://www.example.com";
        
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        assertNotNull(doc);
        assertEquals(html, doc.html());
        assertEquals(baseUri, doc.baseUri());
    }
    
    @Test
    public void testParseBodyFragment_nullBodyHtml() {
        String bodyHtml = null;
        String baseUri = "https://www.example.com";
        
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        assertNotNull(doc);
        assertEquals(html, doc.html());
        assertEquals(baseUri, doc.baseUri());
    }
    
    @Test
    public void testParseBodyFragmentRelaxed_typicalCase() {
        String bodyHtml = "<p>Sample text</p>";
        String baseUri = "https://www.example.com";
        
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        
        assertNotNull(doc);
        assertEquals(html, doc.html());
        assertEquals(baseUri, doc.baseUri());
    }
    
    @Test
    public void testParseBodyFragmentRelaxed_emptyBodyHtml() {
        String bodyHtml = "";
        String baseUri = "https://www.example.com";
        
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        
        assertNotNull(doc);
        assertEquals(html, doc.html());
        assertEquals(baseUri, doc.baseUri());
    }
    
    @Test
    public void testParseBodyFragmentRelaxed_nullBodyHtml() {
        String bodyHtml = null;
        String baseUri = "https://www.example.com";
        
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        
        assertNull(doc);
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