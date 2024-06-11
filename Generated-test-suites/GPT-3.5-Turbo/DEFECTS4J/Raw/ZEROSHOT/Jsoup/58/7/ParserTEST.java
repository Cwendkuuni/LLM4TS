import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Parser;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.HtmlTreeBuilder;
import org.jsoup.parser.XmlTreeBuilder;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class ParserTest {
    
    private Parser parser;
    
    @Before
    public void setup() {
        parser = new Parser(new HtmlTreeBuilder());
    }
    
    @Test
    public void testParseInput() {
        String html = "<html><body><h1>Heading 1</h1></body></html>";
        String baseUri = "https://example.com";
        
        Document doc = parser.parseInput(html, baseUri);
        
        assertNotNull(doc);
        assertEquals("https://example.com", doc.baseUri());
        assertEquals("<html>\n <head></head>\n <body>\n  <h1>Heading 1</h1>\n </body>\n</html>", doc.outerHtml());
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
        
        parser.setTrackErrors(5);
        assertTrue(parser.isTrackErrors());
    }
    
    @Test
    public void testSetTrackErrors() {
        parser.setTrackErrors(10);
        
        assertTrue(parser.isTrackErrors());
        assertEquals(10, parser.getErrors().size());
    }
    
    @Test
    public void testGetErrors() {
        parser.setTrackErrors(5);
        List<ParseError> errors = parser.getErrors();
        
        assertNotNull(errors);
        assertEquals(5, errors.size());
    }
    
    @Test
    public void testSettings() {
        ParseSettings settings = new ParseSettings(false);
        
        parser.settings(settings);
        
        assertEquals(settings, parser.settings());
    }
    
    @Test
    public void testParse() {
        String html = "<html><body><h1>Heading 1</h1></body></html>";
        String baseUri = "https://example.com";
        
        Document doc = Parser.parse(html, baseUri);
        
        assertNotNull(doc);
        assertEquals("https://example.com", doc.baseUri());
        assertEquals("<html>\n <head></head>\n <body>\n  <h1>Heading 1</h1>\n </body>\n</html>", doc.outerHtml());
    }
    
    @Test
    public void testParseFragment() {
        String fragmentHtml = "<p>Paragraph</p>";
        Element context = new Element("div");
        String baseUri = "https://example.com";
        
        List<Node> nodeList = Parser.parseFragment(fragmentHtml, context, baseUri);
        
        assertNotNull(nodeList);
        assertEquals(1, nodeList.size());
        assertTrue(nodeList.get(0) instanceof Element);
        assertEquals("p", ((Element)nodeList.get(0)).tagName());
        assertEquals("Paragraph", ((Element)nodeList.get(0)).text());
    }
    
    @Test
    public void testParseXmlFragment() {
        String fragmentXml = "<element attr=\"value\">Content</element>";
        String baseUri = "https://example.com";
        
        List<Node> nodeList = Parser.parseXmlFragment(fragmentXml, baseUri);
        
        assertNotNull(nodeList);
        assertEquals(1, nodeList.size());
        assertTrue(nodeList.get(0) instanceof Element);
        assertEquals("element", ((Element)nodeList.get(0)).tagName());
        assertEquals("value", ((Element)nodeList.get(0)).attr("attr"));
        assertEquals("Content", ((Element)nodeList.get(0)).text());
    }
    
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Paragraph</p>";
        String baseUri = "https://example.com";
        
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        assertNotNull(doc);
        assertEquals("https://example.com", doc.baseUri());
        assertEquals("<html>\n <head></head>\n <body>\n  <p>Paragraph</p>\n </body>\n</html>", doc.outerHtml());
    }
    
    @Test
    public void testUnescapeEntities() {
        String string = "Hello &amp;amp; World";
        boolean inAttribute = true;
        
        String unescapedString = Parser.unescapeEntities(string, inAttribute);
        
        assertEquals("Hello &amp; World", unescapedString);
    }
    
    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<p>Paragraph</p>";
        String baseUri = "https://example.com";
        
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        
        assertNotNull(doc);
        assertEquals("https://example.com", doc.baseUri());
        assertEquals("<html>\n <head></head>\n <body>\n  <p>Paragraph</p>\n </body>\n</html>", doc.outerHtml());
    }
    
    @Test
    public void testHtmlParser() {
        Parser parser = Parser.htmlParser();
        
        assertNotNull(parser);
        assertTrue(parser.getTreeBuilder() instanceof HtmlTreeBuilder);
    }
    
    @Test
    public void testXmlParser() {
        Parser parser = Parser.xmlParser();
        
        assertNotNull(parser);
        assertTrue(parser.getTreeBuilder() instanceof XmlTreeBuilder);
    }
}