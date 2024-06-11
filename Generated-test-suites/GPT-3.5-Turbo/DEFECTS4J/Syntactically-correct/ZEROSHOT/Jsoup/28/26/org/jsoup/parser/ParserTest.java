import org.jsoup.parser.HtmlTreeBuilder;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Parser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ParserTest {

    private Parser parser;

    @Before
    public void setUp() {
        parser = new Parser(new HtmlTreeBuilder());
    }
    
    @Test
    public void testParseInput() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        
        Document doc = parser.parseInput(html, baseUri);
        
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("<html>\n" +
                " <head></head>\n" +
                " <body>\n" +
                "  <h1>Hello World</h1>\n" +
                " </body>\n" +
                "</html>", doc.toString());
    }
    
    @Test
    public void testGetTreeBuilder() {
        HtmlTreeBuilder treeBuilder = (HtmlTreeBuilder) parser.getTreeBuilder();
        
        assertNotNull(treeBuilder);
    }
    
    @Test
    public void testSetTreeBuilder() {
        HtmlTreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser newParser = parser.setTreeBuilder(treeBuilder);
        
        assertEquals(treeBuilder, newParser.getTreeBuilder());
    }
    
    @Test
    public void testIsTrackErrors() {
        assertFalse(parser.isTrackErrors());
    }
    
    @Test
    public void testSetTrackErrors() {
        Parser newParser = parser.setTrackErrors(10);
        assertTrue(newParser.isTrackErrors());
        assertEquals(10, newParser.getErrors().size());
    }
    
    @Test
    public void testGetErrors() {
        Parser newParser = parser.setTrackErrors(5);
        List<ParseError> errors = newParser.getErrors();
        
        assertNotNull(errors);
        assertEquals(5, errors.size());
    }
    
    @Test
    public void testParse() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        
        Document doc = Parser.parse(html, baseUri);
        
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("<html>\n" +
                " <head></head>\n" +
                " <body>\n" +
                "  <h1>Hello World</h1>\n" +
                " </body>\n" +
                "</html>", doc.toString());
    }
    
    @Test
    public void testParseFragment() {
        String fragmentHtml = "<h1>Hello World</h1>";
        Element context = new Element("div");
        String baseUri = "http://example.com";
        
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
        
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertEquals("<h1>Hello World</h1>", nodes.get(0).toString());
    }
    
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World</h1>";
        String baseUri = "http://example.com";
        
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("<html>\n" +
                " <head></head>\n" +
                " <body>\n" +
                "  <h1>Hello World</h1>\n" +
                " </body>\n" +
                "</html>", doc.toString());
    }
    
    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<h1>Hello World</h1>";
        String baseUri = "http://example.com";
        
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("<html>\n" +
                " <head></head>\n" +
                " <body>\n" +
                "  <h1>Hello World</h1>\n" +
                " </body>\n" +
                "</html>", doc.toString());
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
        assertFalse(xmlParser.getTreeBuilder() instanceof HtmlTreeBuilder);
    }
    
}