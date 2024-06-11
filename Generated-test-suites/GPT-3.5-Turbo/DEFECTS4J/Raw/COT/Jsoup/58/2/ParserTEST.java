import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.HtmlTreeBuilder;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.Tokeniser;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Test;

public class ParserTest {
    
    @Test
    public void testParseInput() {
        Parser parser = new Parser(new TreeBuilder());
        Document document = parser.parseInput("<html><head></head><body></body></html>", "http://example.com");
        assertNotNull(document);
        assertEquals("http://example.com", document.baseUri());
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
        parser.setTreeBuilder(treeBuilder);
        assertEquals(treeBuilder, parser.getTreeBuilder());
    }
    
    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new TreeBuilder());
        assertFalse(parser.isTrackErrors());
    }
    
    @Test
    public void testSetTrackErrors() {
        Parser parser = new Parser(new TreeBuilder());
        parser.setTrackErrors(10);
        assertTrue(parser.isTrackErrors());
    }
    
    @Test
    public void testGetErrors() {
        Parser parser = new Parser(new TreeBuilder());
        assertNull(parser.getErrors());
    }
    
    @Test
    public void testSettings() {
        Parser parser = new Parser(new TreeBuilder());
        ParseSettings settings = new ParseSettings(false, false);
        parser.settings(settings);
        assertEquals(settings, parser.settings());
    }
    
    @Test
    public void testParse() {
        Document document = Parser.parse("<html><head></head><body></body></html>", "http://example.com");
        assertNotNull(document);
        assertEquals("http://example.com", document.baseUri());
    }
    
    @Test
    public void testParseFragment() {
        Element context = new Element("div");
        List<Node> nodes = Parser.parseFragment("<p>Hello world</p>", context, "http://example.com");
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        assertEquals("p", nodes.get(0).nodeName());
    }
    
    @Test
    public void testParseXmlFragment() {
        List<Node> nodes = Parser.parseXmlFragment("<tag>value</tag>", "http://example.com");
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        assertEquals("tag", nodes.get(0).nodeName());
    }
    
    @Test
    public void testParseBodyFragment() {
        Document document = Parser.parseBodyFragment("<div>Hello world</div>", "http://example.com");
        assertNotNull(document);
        Element body = document.body();
        assertNotNull(body);
        assertEquals("Hello world", body.text());
    }
    
    @Test
    public void testUnescapeEntities() {
        String unescaped = Parser.unescapeEntities("&lt;div&gt;Hello world&lt;/div&gt;", false);
        assertEquals("<div>Hello world</div>", unescaped);
    }
    
    @Test
    public void testParseBodyFragmentRelaxed() {
        Document document = Parser.parseBodyFragmentRelaxed("<html><head></head><body></body></html>", "http://example.com");
        assertNotNull(document);
        assertEquals("http://example.com", document.baseUri());
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