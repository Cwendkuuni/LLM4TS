import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.*;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class ParserTest {
    
    private Parser parser;
    
    @Before
    public void setUp() {
        parser = new Parser(new HtmlTreeBuilder());
    }
    
    @Test
    public void testParseInput() {
        String html = "<html><head></head><body><h1>Title</h1><p>Paragraph</p></body></html>";
        String baseUri = "https://www.example.com";
        
        Document doc = parser.parseInput(html, baseUri);
        
        assertNotNull(doc);
        assertEquals("https://www.example.com", doc.baseUri());
        assertEquals("<html>\n <head></head>\n <body>\n  <h1>Title</h1>\n  <p>Paragraph</p>\n </body>\n</html>",
                doc.html());
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
        List<ParseError> errors = parser.getErrors();
        
        assertNotNull(errors);
        assertTrue(errors.isEmpty());
    }
    
    @Test
    public void testParse() {
        String html = "<html><head></head><body><h1>Title</h1><p>Paragraph</p></body></html>";
        String baseUri = "https://www.example.com";
        
        Document doc = Parser.parse(html, baseUri);
        
        assertNotNull(doc);
        assertEquals("https://www.example.com", doc.baseUri());
        assertEquals("<html>\n <head></head>\n <body>\n  <h1>Title</h1>\n  <p>Paragraph</p>\n </body>\n</html>",
                doc.html());
    }
    
    @Test
    public void testParseFragment() {
        String fragmentHtml = "<div><h2>Header</h2><p>Paragraph</p></div>";
        Element context = new Element("div");
        String baseUri = "https://www.example.com";
        
        List<Node> nodeList = Parser.parseFragment(fragmentHtml, context, baseUri);
        
        assertNotNull(nodeList);
        assertEquals(1, nodeList.size());
        
        Node node = nodeList.get(0);
        assertTrue(node instanceof Element);
        assertEquals("<div>\n <h2>Header</h2>\n <p>Paragraph</p>\n</div>", node.outerHtml());
    }
    
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h2>Header</h2><p>Paragraph</p>";
        String baseUri = "https://www.example.com";
        
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        assertNotNull(doc);
        assertEquals("https://www.example.com", doc.baseUri());
        assertEquals("<html>\n <head></head>\n <body>\n  <h2>Header</h2>\n  <p>Paragraph</p>\n </body>\n</html>",
                doc.html());
    }
    
    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<h2>Header</h2><p>Paragraph</p>";
        String baseUri = "https://www.example.com";
        
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        
        assertNotNull(doc);
        assertEquals("https://www.example.com", doc.baseUri());
        assertEquals("<html>\n <head></head>\n <body>\n  <h2>Header</h2>\n  <p>Paragraph</p>\n </body>\n</html>",
                doc.html());
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