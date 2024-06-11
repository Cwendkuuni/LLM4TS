
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;


public class ParserTest {

    @Test
    public void testParseInput() {
        Parser parser = new Parser(new TreeBuilder());
        String html = "<html><body><h1>Title</h1><p>Paragraph</p></body></html>";
        String baseUri = "https://example.com";
        Document document = parser.parseInput(html, baseUri);
        
        assertNotNull(document);
        assertEquals("<html>\n <head></head>\n <body>\n  <h1>Title</h1> \n  <p>Paragraph</p> \n </body>\n</html>", document.toString());
    }
    
    @Test
    public void testGetTreeBuilder() {
        Parser parser = new Parser(new TreeBuilder());
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        
        assertNotNull(treeBuilder);
    }
    
    @Test
    public void testSetTreeBuilder() {
        Parser parser = new Parser(new TreeBuilder());
        TreeBuilder treeBuilder = new TreeBuilder();
        Parser newParser = parser.setTreeBuilder(treeBuilder);
        
        assertSame(treeBuilder, newParser.getTreeBuilder());
    }
    
    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new TreeBuilder());
        boolean isTrackErrors = parser.isTrackErrors();
        
        assertFalse(isTrackErrors);
    }
    
    @Test
    public void testSetTrackErrors() {
        Parser parser = new Parser(new TreeBuilder());
        Parser newParser = parser.setTrackErrors(5);
        
        assertTrue(newParser.isTrackErrors());
        assertEquals(5, newParser.maxErrors);
    }
    
    @Test
    public void testGetErrors() {
        Parser parser = new Parser(new TreeBuilder());
        List<ParseError> errors = parser.getErrors();
        
        assertNotNull(errors);
        assertEquals(0, errors.size());
    }
    
    @Test
    public void testSettings() {
        Parser parser = new Parser(new TreeBuilder());
        ParseSettings settings = new ParseSettings(true, true);
        Parser newParser = parser.settings(settings);
        
        assertSame(settings, newParser.settings);
    }
    
    @Test
    public void testSettings() {
        Parser parser = new Parser(new TreeBuilder());
        ParseSettings settings = parser.settings();
        
        assertNotNull(settings);
        assertTrue(settings.isTrackErrors());
    }
    
    @Test
    public void testParse() {
        String html = "<html><body><h1>Title</h1><p>Paragraph</p></body></html>";
        String baseUri = "https://example.com";
        Document document = Parser.parse(html, baseUri);
        
        assertNotNull(document);
        assertEquals("<html>\n <head></head>\n <body>\n  <h1>Title</h1> \n  <p>Paragraph</p> \n </body>\n</html>", document.toString());
    }
    
    @Test
    public void testParseFragment() {
        String fragmentHtml = "<h1>Title</h1><p>Paragraph</p>";
        Element context = new Element("div");
        String baseUri = "https://example.com";
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
        
        assertNotNull(nodes);
        assertEquals(2, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        assertTrue(nodes.get(1) instanceof Element);
        assertEquals("h1", ((Element) nodes.get(0)).tagName());
        assertEquals("p", ((Element) nodes.get(1)).tagName());
    }
    
    @Test
    public void testParseXmlFragment() {
        String fragmentXml = "<root><element>1</element><element>2</element></root>";
        String baseUri = "https://example.com";
        List<Node> nodes = Parser.parseXmlFragment(fragmentXml, baseUri);
        
        assertNotNull(nodes);
        assertEquals(3, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        assertTrue(nodes.get(1) instanceof Element);
        assertTrue(nodes.get(2) instanceof Element);
        assertEquals("root", ((Element) nodes.get(0)).tagName());
        assertEquals("element", ((Element) nodes.get(1)).tagName());
        assertEquals("element", ((Element) nodes.get(2)).tagName());
    }
    
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Heading</h1><p>Paragraph</p>";
        String baseUri = "https://example.com";
        Document document = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        assertNotNull(document);
        assertEquals("<html>\n <head></head>\n <body>\n  <h1>Heading</h1> \n  <p>Paragraph</p> \n </body>\n</html>", document.toString());
    }
    
    @Test
    public void testUnescapeEntities() {
        String string = "&lt;html&gt;&lt;body&gt;&lt;h1&gt;Title&lt;/h1&gt;&lt;p&gt;Paragraph&lt;/p&gt;&lt;/body&gt;&lt;/html&gt;";
        String unescapedString = Parser.unescapeEntities(string, false);
        
        assertEquals("<html><body><h1>Title</h1><p>Paragraph</p></body></html>", unescapedString);
    }
    
    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<h1>Heading</h1><p>Paragraph</p>";
        String baseUri = "https://example.com";
        Document document = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        
        assertNotNull(document);
        assertEquals("<html>\n <head></head>\n <body>\n  <h1>Heading</h1> \n  <p>Paragraph</p> \n </body>\n</html>", document.toString());
    }
    
    @Test
    public void testHtmlParser() {
        Parser parser = Parser.htmlParser();
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        
        assertNotNull(parser);
        assertNotNull(treeBuilder);
        assertTrue(treeBuilder instanceof HtmlTreeBuilder);
    }
    
    @Test
    public void testXmlParser() {
        Parser parser = Parser.xmlParser();
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        
        assertNotNull(parser);
        assertNotNull(treeBuilder);
        assertTrue(treeBuilder instanceof XmlTreeBuilder);
    }
}
