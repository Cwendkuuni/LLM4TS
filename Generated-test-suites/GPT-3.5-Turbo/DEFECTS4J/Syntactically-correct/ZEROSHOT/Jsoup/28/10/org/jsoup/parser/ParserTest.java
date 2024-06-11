import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.HtmlTreeBuilder;
import org.jsoup.parser.ParseErrorList;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {
    
    private Parser parser;
    private TreeBuilder treeBuilder;
    private String html;
    private String baseUri;

    @Before
    public void setUp() {
        treeBuilder = new HtmlTreeBuilder();
        parser = new Parser(treeBuilder);
        html = "<html><body><h1>Hello World!</h1></body></html>";
        baseUri = "http://example.com";
    }
    
    @Test
    public void testParseInput() {
        Document doc = parser.parseInput(html, baseUri);
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("Hello World!", doc.body().select("h1").text());
    }
    
    @Test
    public void testGetTreeBuilder() {
        assertEquals(treeBuilder, parser.getTreeBuilder());
    }
    
    @Test
    public void testSetTreeBuilder() {
        TreeBuilder newTreeBuilder = new HtmlTreeBuilder();
        Parser newParser = parser.setTreeBuilder(newTreeBuilder);
        assertEquals(newTreeBuilder, newParser.getTreeBuilder());
    }
    
    @Test
    public void testIsTrackErrors() {
        assertFalse(parser.isTrackErrors());
        parser.setTrackErrors(10);
        assertTrue(parser.isTrackErrors());
    }
    
    @Test
    public void testSetTrackErrors() {
        Parser newParser = parser.setTrackErrors(5);
        assertTrue(newParser.isTrackErrors());
        assertEquals(5, newParser.getErrors().size());
    }
    
    @Test
    public void testGetErrors() {
        List<ParseError> errors = parser.getErrors();
        assertNotNull(errors);
        assertEquals(0, errors.size());
    }
    
    @Test
    public void testParse() {
        Document doc = Parser.parse(html, baseUri);
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("Hello World!", doc.body().select("h1").text());
    }
    
    @Test
    public void testParseFragment() {
        Element context = Jsoup.parse("<div></div>").body();
        List<Node> nodes = Parser.parseFragment(html, context, baseUri);
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertEquals("h1", nodes.get(0).nodeName());
    }
    
    @Test
    public void testParseBodyFragment() {
        Document doc = Parser.parseBodyFragment(html, baseUri);
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("Hello World!", doc.body().select("h1").text());
    }
    
    @Test
    public void testParseBodyFragmentRelaxed() {
        Document doc = Parser.parseBodyFragmentRelaxed(html, baseUri);
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("Hello World!", doc.body().select("h1").text());
    }
    
    @Test
    public void testHtmlParser() {
        Parser htmlParser = Parser.htmlParser();
        assertNotNull(htmlParser);
        assertEquals(HtmlTreeBuilder.class, htmlParser.getTreeBuilder().getClass());
    }
    
    @Test
    public void testXmlParser() {
        Parser xmlParser = Parser.xmlParser();
        assertNotNull(xmlParser);
        assertEquals(XmlTreeBuilder.class, xmlParser.getTreeBuilder().getClass());
    }
}