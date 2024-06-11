
import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.nodes.*;

public class ParserTest {
    
    @Test
    public void testParseInput() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document document = parser.parseInput("<html><body><h1>Hello World</h1></body></html>", "https://www.example.com");
        assertNotNull(document);
        assertEquals("<html><head></head><body><h1>Hello World</h1></body></html>", document.outerHtml());
    }
    
    @Test
    public void testGetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        assertNotNull(treeBuilder);
    }
    
    @Test
    public void testSetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder treeBuilder = new XmlTreeBuilder();
        parser.setTreeBuilder(treeBuilder);
        assertEquals(treeBuilder, parser.getTreeBuilder());
    }
    
    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        assertFalse(parser.isTrackErrors());
    }
    
    @Test
    public void testSetTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(5);
        assertTrue(parser.isTrackErrors());
    }
    
    @Test
    public void testGetErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        List<ParseError> errors = parser.getErrors();
        assertNotNull(errors);
        assertEquals(0, errors.size());
    }
    
    @Test
    public void testSettings() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        ParseSettings settings = new ParseSettings(false, false);
        parser.settings(settings);
        assertEquals(settings, parser.settings());
    }
    
    @Test
    public void testParse() {
        Document document = Parser.parse("<html><body><h1>Hello World</h1></body></html>", "https://www.example.com");
        assertNotNull(document);
        assertEquals("<html><head></head><body><h1>Hello World</h1></body></html>", document.outerHtml());
    }
    
    @Test
    public void testParseFragment() {
        Element context = new Element(Tag.valueOf("div"), "");
        List<Node> nodes = Parser.parseFragment("<p>Hello World</p>", context, "https://www.example.com");
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        Element element = (Element) nodes.get(0);
        assertEquals("p", element.tagName());
        assertEquals("Hello World", element.text());
    }
    
    @Test
    public void testParseXmlFragment() {
        List<Node> nodes = Parser.parseXmlFragment("<tag>Hello World</tag>", "https://www.example.com");
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        Element element = (Element) nodes.get(0);
        assertEquals("tag", element.tagName());
        assertEquals("Hello World", element.text());
    }
    
    @Test
    public void testParseBodyFragment() {
        Document document = Parser.parseBodyFragment("<p>Hello World</p>", "https://www.example.com");
        assertNotNull(document);
        Element body = document.body();
        assertNotNull(body);
        assertEquals("p", body.child(0).tagName());
        assertEquals("Hello World", body.child(0).text());
    }
    
    @Test
    public void testUnescapeEntities() {
        String unescaped = Parser.unescapeEntities("&lt;p&gt;Hello World&lt;/p&gt;", true);
        assertEquals("<p>Hello World</p>", unescaped);
    }
    
    @Test
    public void testParseBodyFragmentRelaxed() {
        Document document = Parser.parseBodyFragmentRelaxed("<p>Hello World</p>", "https://www.example.com");
        assertNotNull(document);
        assertEquals("<html><head></head><body><p>Hello World</p></body></html>", document.outerHtml());
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
