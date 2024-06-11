import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.XmlTreeBuilder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ParserTest {
    
    private Parser parser;
    
    @Before
    public void setUp() {
        parser = new Parser(new TreeBuilder());
    }
    
    @Test
    public void testParseInput() {
        String html = "<html><head></head><body></body></html>";
        String baseUri = "http://example.com";
        
        Document document = parser.parseInput(html, baseUri);
        
        assertNotNull(document);
        assertEquals("html", document.tag().getName());
    }
    
    @Test
    public void testGetTreeBuilder() {
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        
        assertNotNull(treeBuilder);
    }
    
    @Test
    public void testSetTreeBuilder() {
        TreeBuilder treeBuilder = new TreeBuilder();
        
        Parser updatedParser = parser.setTreeBuilder(treeBuilder);
        
        assertNotNull(updatedParser);
        assertEquals(treeBuilder, updatedParser.getTreeBuilder());
    }
    
    @Test
    public void testIsTrackErrors() {
        assertFalse(parser.isTrackErrors());
    }
    
    @Test
    public void testSetTrackErrors() {
        int maxErrors = 5;
        
        Parser updatedParser = parser.setTrackErrors(maxErrors);
        
        assertNotNull(updatedParser);
        assertTrue(updatedParser.isTrackErrors());
    }
    
    @Test
    public void testGetErrors() {
        List<ParseError> errors = new ArrayList<>();
        errors.add(new ParseError("Error 1"));
        errors.add(new ParseError("Error 2"));
        ParseErrorList errorList = new ParseErrorList(errors);
        
        parser.setTrackErrors(10);
        
        List<ParseError> parsedErrors = parser.getErrors();
        
        assertNotNull(parsedErrors);
        assertEquals(2, parsedErrors.size());
        assertEquals("Error 1", parsedErrors.get(0).getMessage());
        assertEquals("Error 2", parsedErrors.get(1).getMessage());
    }
    
    @Test
    public void testSettings() {
        ParseSettings settings = new ParseSettings(false, false);
        
        Parser updatedParser = parser.settings(settings);
        
        assertNotNull(updatedParser);
        assertEquals(settings, updatedParser.settings());
    }
    
    @Test
    public void testSettings_default() {
        ParseSettings defaultSettings = parser.settings();
        
        assertNotNull(defaultSettings);
        assertFalse(defaultSettings.normalizeTagStructure());
        assertFalse(defaultSettings.preserveWhitespace());
    }
    
    @Test
    public void testParse() {
        String html = "<html><head></head><body></body></html>";
        String baseUri = "http://example.com";
        
        Document document = Parser.parse(html, baseUri);
        
        assertNotNull(document);
        assertEquals("html", document.tag().getName());
    }
    
    @Test
    public void testParseFragment() {
        String fragmentHtml = "<p>Hello <b>World</b></p>";
        Element context = new Element("div");
        String baseUri = "http://example.com";
        
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
        
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertEquals("p", nodes.get(0).nodeName());
    }
    
    @Test
    public void testParseFragment_withErrorList() {
        String fragmentHtml = "<p>Hello <b>World</p>";
        Element context = new Element("div");
        String baseUri = "http://example.com";
        List<ParseError> errors = new ArrayList<>();
        ParseErrorList errorList = new ParseErrorList(errors);
        
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri, errorList);
        
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertEquals("p", nodes.get(0).nodeName());
        assertEquals(1, errorList.size());
    }
    
    @Test
    public void testParseXmlFragment() {
        String fragmentXml = "<root><child1>Hello</child1><child2>World</child2></root>";
        String baseUri = "http://example.com";
        
        List<Node> nodes = Parser.parseXmlFragment(fragmentXml, baseUri);
        
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertEquals("root", nodes.get(0).nodeName());
    }
    
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<div><p>Hello World</p></div>";
        String baseUri = "http://example.com";
        
        Document document = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        assertNotNull(document);
        Element body = document.body();
        assertNotNull(body);
        assertEquals("div", body.nodeName());
        assertEquals(1, body.children().size());
        assertEquals("p", body.child(0).nodeName());
        assertEquals("Hello World", body.child(0).text());
    }
    
    @Test
    public void testUnescapeEntities() {
        String escapedString = "&lt;div&gt;Hello World&lt;/div&gt;";
        
        String unescapedString = Parser.unescapeEntities(escapedString, true);
        
        assertNotNull(unescapedString);
        assertEquals("<div>Hello World</div>", unescapedString);
    }
    
    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<div><p>Hello World</p></div>";
        String baseUri = "http://example.com";
        
        Document document = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        
        assertNotNull(document);
        assertEquals("html", document.tag().getName());
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