
import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import java.util.List;


public class ParserTest {
    @Test
    public void testParseInput() {
        Parser parser = new Parser(new TreeBuilder());
        Document doc = parser.parseInput("<html><body><h1>Hello, World!</h1></body></html>", "http://example.com");
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        Element body = doc.body();
        assertNotNull(body);
        Element h1 = body.selectFirst("h1");
        assertNotNull(h1);
        assertEquals("Hello, World!", h1.ownText());
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
        Parser updatedParser = parser.setTreeBuilder(treeBuilder);
        assertEquals(treeBuilder, updatedParser.getTreeBuilder());
    }
    
    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new TreeBuilder());
        assertFalse(parser.isTrackErrors());
        parser.setTrackErrors(10);
        assertTrue(parser.isTrackErrors());
    }
    
    @Test
    public void testSetTrackErrors() {
        Parser parser = new Parser(new TreeBuilder());
        parser.setTrackErrors(10);
        assertTrue(parser.isTrackErrors());
        parser.setTrackErrors(0);
        assertFalse(parser.isTrackErrors());
    }
    
    @Test
    public void testGetErrors() {
        Parser parser = new Parser(new TreeBuilder());
        List<ParseError> errors = parser.getErrors();
        assertNotNull(errors);
        assertTrue(errors.isEmpty());
    }
    
    @Test
    public void testSettings() {
        Parser parser = new Parser(new TreeBuilder());
        ParseSettings settings = new ParseSettings(true, true);
        Parser updatedParser = parser.settings(settings);
        assertEquals(settings, updatedParser.settings());
    }
    
    @Test
    public void testParse() {
        Document doc = Parser.parse("<html><body><h1>Hello, World!</h1></body></html>", "http://example.com");
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        Element body = doc.body();
        assertNotNull(body);
        Element h1 = body.selectFirst("h1");
        assertNotNull(h1);
        assertEquals("Hello, World!", h1.ownText());
    }
    
    @Test
    public void testParseFragment() {
        Element context = new Element("div");
        List<Node> nodes = Parser.parseFragment("<p>Test</p>", context, "http://example.com");
        assertNotNull(nodes);
        assertFalse(nodes.isEmpty());
        assertEquals("http://example.com", context.baseUri());
    }
    
    @Test
    public void testParseXmlFragment() {
        List<Node> nodes = Parser.parseXmlFragment("<p>Test</p>", "http://example.com");
        assertNotNull(nodes);
        assertFalse(nodes.isEmpty());
    }
    
    @Test
    public void testParseBodyFragment() {
        Document doc = Parser.parseBodyFragment("<p>Test</p>", "http://example.com");
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        Element body = doc.body();
        assertNotNull(body);
        Element p = body.selectFirst("p");
        assertNotNull(p);
        assertEquals("Test", p.ownText());
    }
    
    @Test
    public void testUnescapeEntities() {
        String escaped = "&lt;p&gt;Test&lt;/p&gt;";
        String unescaped = Parser.unescapeEntities(escaped, true);
        assertEquals("<p>Test</p>", unescaped);
    }
    
    @Test
    public void testParseBodyFragmentRelaxed() {
        Document doc = Parser.parseBodyFragmentRelaxed("<html><body><h1>Hello, World!</h1></body></html>", "http://example.com");
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        Element body = doc.body();
        assertNotNull(body);
        Element h1 = body.selectFirst("h1");
        assertNotNull(h1);
        assertEquals("Hello, World!", h1.ownText());
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

