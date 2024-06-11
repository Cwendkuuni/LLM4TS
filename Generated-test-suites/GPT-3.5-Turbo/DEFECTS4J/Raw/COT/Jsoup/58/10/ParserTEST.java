
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;

import java.util.List;

public class ParserTest {
    
    private Parser parser;
    
    @Before
    public void setUp() {
        parser = new Parser(new TreeBuilder());
    }
    
    @Test
    public void testParseInput() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        Document document = parser.parseInput(html, baseUri);
        
        assertNotNull(document);
        assertEquals("<html>\n" +
                " <head></head>\n" +
                " <body>\n" +
                "  <h1>Hello World</h1>\n" +
                " </body>\n" +
                "</html>", document.outerHtml());
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
        
        assertSame(treeBuilder, updatedParser.getTreeBuilder());
    }
    
    @Test
    public void testIsTrackErrors() {
        boolean trackErrors = parser.isTrackErrors();
        
        assertFalse(trackErrors);
    }
    
    @Test
    public void testSetTrackErrors() {
        int maxErrors = 5;
        Parser updatedParser = parser.setTrackErrors(maxErrors);
        
        assertTrue(updatedParser.isTrackErrors());
        assertEquals(5, updatedParser.maxErrors);
    }
    
    @Test
    public void testGetErrors() {
        List<ParseError> errors = parser.getErrors();
        
        assertNotNull(errors);
        assertTrue(errors.isEmpty());
    }
    
    @Test
    public void testSettings() {
        ParseSettings settings = new ParseSettings(false, false);
        Parser updatedParser = parser.settings(settings);
        
        assertSame(settings, updatedParser.settings);
    }
    
    @Test
    public void testParse() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        Document document = Parser.parse(html, baseUri);
        
        assertNotNull(document);
        assertEquals("<html>\n" +
                " <head></head>\n" +
                " <body>\n" +
                "  <h1>Hello World</h1>\n" +
                " </body>\n" +
                "</html>", document.outerHtml());
    }
    
    @Test
    public void testParseFragment() {
        String fragmentHtml = "<div><p>Hello World</p></div>";
        Element context = new Element("div");
        String baseUri = "http://example.com";
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
        
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        Element element = (Element) nodes.get(0);
        assertEquals("div", element.tagName());
        assertEquals("<div>\n" +
                " <p>Hello World</p>\n" +
                "</div>", element.outerHtml());
    }
    
    @Test
    public void testParseXmlFragment() {
        String fragmentXml = "<root><child>Hello World</child></root>";
        String baseUri = "http://example.com";
        List<Node> nodes = Parser.parseXmlFragment(fragmentXml, baseUri);
        
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        Element element = (Element) nodes.get(0);
        assertEquals("root", element.tagName());
        assertEquals("<root>\n" +
                " <child>Hello World</child>\n" +
                "</root>", element.outerHtml());
    }
    
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello World</p>";
        String baseUri = "http://example.com";
        Document document = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        assertNotNull(document);
        assertEquals("<html>\n" +
                " <head></head>\n" +
                " <body>\n" +
                "  <p>Hello World</p>\n" +
                " </body>\n" +
                "</html>", document.outerHtml());
    }
    
    @Test
    public void testUnescapeEntities() {
        String string = "&lt;p&gt;Hello World&lt;/p&gt;";
        String unescapedString = Parser.unescapeEntities(string, false);
        
        assertEquals("<p>Hello World</p>", unescapedString);
    }
    
    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<p>Hello World</p>";
        String baseUri = "http://example.com";
        Document document = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        
        assertNotNull(document);
        assertEquals("<html>\n" +
                " <head></head>\n" +
                " <body>\n" +
                "  <p>Hello World</p>\n" +
                " </body>\n" +
                "</html>", document.outerHtml());
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

