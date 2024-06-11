package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class ParserTest {
    private static final String HTML = "<html><body><div id=\"content\">Hello World</div></body></html>";
    private static final String BASE_URI = "https://www.example.com";

    private Parser parser;
    
    @Before
    public void setUp() {
        parser = new Parser(new HtmlTreeBuilder());
    }
    
    @Test
    public void testParseInput() {
        Document document = parser.parseInput(HTML, BASE_URI);
        
        assertNotNull(document);
        assertEquals("https://www.example.com", document.baseUri());
        assertEquals("<html>\n <head></head>\n <body>\n  <div id=\"content\">\n   Hello World\n  </div>\n </body>\n</html>", document.outerHtml());
    }
    
    @Test
    public void testGetTreeBuilder() {
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        
        assertNotNull(treeBuilder);
        assertTrue(treeBuilder instanceof HtmlTreeBuilder);
    }
    
    @Test
    public void testSetTreeBuilder() {
        Parser newParser = parser.setTreeBuilder(new XmlTreeBuilder());
        
        assertNotNull(newParser);
        assertTrue(newParser.getTreeBuilder() instanceof XmlTreeBuilder);
    }
    
    @Test
    public void testIsTrackErrors() {
        assertFalse(parser.isTrackErrors());
    }
    
    @Test
    public void testSetTrackErrors() {
        Parser newParser = parser.setTrackErrors(10);
        
        assertNotNull(newParser);
        assertTrue(newParser.isTrackErrors());
    }
    
    @Test
    public void testGetErrors() {
        List<ParseError> errors = parser.getErrors();
        
        assertNotNull(errors);
        assertTrue(errors.isEmpty());
    }
    
    @Test
    public void testSettings() {
        ParseSettings settings = new ParseSettings(true, true);
        Parser newParser = parser.settings(settings);
        
        assertNotNull(newParser);
        assertEquals(settings, newParser.settings());
    }
    
    @Test
    public void testParse() {
        Document document = Parser.parse(HTML, BASE_URI);
        
        assertNotNull(document);
        assertEquals("https://www.example.com", document.baseUri());
        assertEquals("<html>\n <head></head>\n <body>\n  <div id=\"content\">\n   Hello World\n  </div>\n </body>\n</html>", document.outerHtml());
    }
    
    @Test
    public void testParseFragment() {
        Element context = new Element("div");
        List<Node> nodes = Parser.parseFragment("<span>Hello World</span>", context, BASE_URI);
        
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        assertEquals("span", nodes.get(0).nodeName());
        assertEquals("Hello World", ((Element) nodes.get(0)).text());
    }
    
    @Test
    public void testParseXmlFragment() {
        List<Node> nodes = Parser.parseXmlFragment("<element>Hello World</element>", BASE_URI);
        
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        assertEquals("element", nodes.get(0).nodeName());
        assertEquals("Hello World", ((Element) nodes.get(0)).text());
    }
    
    @Test
    public void testParseBodyFragment() {
        Document document = Parser.parseBodyFragment("<span>Hello World</span>", BASE_URI);
        
        assertNotNull(document);
        assertEquals("https://www.example.com", document.baseUri());
        assertEquals("<html>\n <head></head>\n <body>\n  <span>\n   Hello World\n  </span>\n </body>\n</html>", document.outerHtml());
    }
    
    @Test
    public void testUnescapeEntities() {
        String escapedString = "Hello &lt;World&gt;";
        String unescapedString = Parser.unescapeEntities(escapedString, false);
        
        assertEquals("Hello <World>", unescapedString);
    }
    
    @Test
    public void testParseBodyFragmentRelaxed() {
        Document document = Parser.parseBodyFragmentRelaxed("<span>Hello World</span>", BASE_URI);
        
        assertNotNull(document);
        assertEquals("https://www.example.com", document.baseUri());
        assertEquals("<html>\n <head></head>\n <body>\n  <span>\n   Hello World\n  </span>\n </body>\n</html>", document.outerHtml());
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