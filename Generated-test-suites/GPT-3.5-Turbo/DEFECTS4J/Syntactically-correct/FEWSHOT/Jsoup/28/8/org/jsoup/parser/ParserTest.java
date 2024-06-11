import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.HtmlTreeBuilder;
import org.jsoup.parser.XmlTreeBuilder;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ParserTest {
    private Parser parser;
    private TreeBuilder treeBuilder;
    
    @Before
    public void setUp() {
        treeBuilder = new HtmlTreeBuilder();
        parser = new Parser(treeBuilder);
    }
    
    @Test
    public void testParseInput() {
        Document doc = parser.parseInput("<html><body><h1>Title</h1></body></html>", "http://example.com");
        Element body = doc.body();
        assertEquals("Title", body.getElementsByTag("h1").text());
    }
    
    @Test
    public void testGetTreeBuilder() {
        assertEquals(treeBuilder, parser.getTreeBuilder());
    }
    
    @Test
    public void testSetTreeBuilder() {
        TreeBuilder newTreeBuilder = new XmlTreeBuilder();
        parser.setTreeBuilder(newTreeBuilder);
        assertEquals(newTreeBuilder, parser.getTreeBuilder());
    }
    
    @Test
    public void testIsTrackErrors() {
        assertFalse(parser.isTrackErrors());
        parser.setTrackErrors(5);
        assertTrue(parser.isTrackErrors());
    }
    
    @Test
    public void testSetTrackErrors() {
        parser.setTrackErrors(5);
        assertTrue(parser.isTrackErrors());
        parser.setTrackErrors(0);
        assertFalse(parser.isTrackErrors());
    }
    
    @Test
    public void testGetErrors() {
        assertNull(parser.getErrors());
        parser.setTrackErrors(5);
        List<ParseError> errors = parser.getErrors();
        assertEquals(0, errors.size());
    }
    
    @Test
    public void testParse() {
        Document doc = Parser.parse("<html><body><h1>Title</h1></body></html>", "http://example.com");
        Element body = doc.body();
        assertEquals("Title", body.getElementsByTag("h1").text());
    }
    
    @Test
    public void testParseFragment() {
        Element context = new Element("div");
        List<Node> nodes = Parser.parseFragment("<p>Paragraph</p>", context, "http://example.com");
        assertEquals(1, nodes.size());
        assertEquals("Paragraph", nodes.get(0).text());
    }
    
    @Test
    public void testParseBodyFragment() {
        Document doc = Parser.parseBodyFragment("<p>Paragraph</p>", "http://example.com");
        Element body = doc.body();
        assertEquals(1, body.children().size());
        assertEquals("Paragraph", body.getElementsByTag("p").text());
    }
    
    @Test
    public void testParseBodyFragmentRelaxed() {
        Document doc = Parser.parseBodyFragmentRelaxed("<p>Paragraph</p>", "http://example.com");
        Element body = doc.body();
        assertEquals(1, body.children().size());
        assertEquals("Paragraph", body.getElementsByTag("p").text());
    }
    
    @Test
    public void testHtmlParser() {
        Parser htmlParser = Parser.htmlParser();
        assertTrue(htmlParser.getTreeBuilder() instanceof HtmlTreeBuilder);
    }
    
    @Test
    public void testXmlParser() {
        Parser xmlParser = Parser.xmlParser();
        assertTrue(xmlParser.getTreeBuilder() instanceof XmlTreeBuilder);
    }
}