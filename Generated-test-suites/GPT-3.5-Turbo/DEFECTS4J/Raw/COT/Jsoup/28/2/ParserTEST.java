import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.HtmlTreeBuilder;
import org.jsoup.parser.XmlTreeBuilder;

import java.util.List;

import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParseInput() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "https://example.com";
        
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput(html, baseUri);

        assertNotNull(doc);
        assertEquals("Hello World", doc.getElementsByTag("h1").first().text());
    }
    
    @Test
    public void testGetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder treeBuilder = parser.getTreeBuilder();

        assertNotNull(treeBuilder);
        assertTrue(treeBuilder instanceof HtmlTreeBuilder);
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
        parser.setTrackErrors(10);

        assertTrue(parser.isTrackErrors());
    }
    
    @Test
    public void testGetErrors() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "https://example.com";
        
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.parseInput(html, baseUri);
        List<ParseError> errors = parser.getErrors();

        assertNotNull(errors);
        assertEquals(0, errors.size());
    }
    
    @Test
    public void testParse() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "https://example.com";

        Document doc = Parser.parse(html, baseUri);

        assertNotNull(doc);
        assertEquals("Hello World", doc.getElementsByTag("h1").first().text());
    }
    
    @Test
    public void testParseFragment() {
        String fragmentHtml = "<div><p>Hello World</p></div>";
        Element context = new Element("div");
        String baseUri = "https://example.com";
        
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);

        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertEquals("Hello World", nodes.get(0).text());
    }
    
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello World</p>";
        String baseUri = "https://example.com";

        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

        assertNotNull(doc);
        assertEquals("Hello World", doc.getElementsByTag("p").first().text());
    }
    
    @Test
    public void testDeprecatedParseBodyFragmentRelaxed() {
        String bodyHtml = "<p>Hello World</p>";
        String baseUri = "https://example.com";

        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);

        assertNotNull(doc);
        assertEquals("Hello World", doc.getElementsByTag("p").first().text());
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
    
    // Additional test cases for edge cases and exception handling scenarios
    @Test
    public void testParseInputWithEmptyHtml() {
        String html = "";
        String baseUri = "https://example.com";
        
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput(html, baseUri);

        assertNotNull(doc);
        assertEquals("", doc.html());
    }
    
    @Test
    public void testParseInputWithNullBaseUri() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = null;
        
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput(html, baseUri);

        assertNotNull(doc);
        assertEquals("Hello World", doc.getElementsByTag("h1").first().text());
    }
    
    @Test
    public void testParseWithInvalidHtml() {
        String html = "<html><body><h1>Hello World</h1>";
        String baseUri = "https://example.com";

        Document doc = Parser.parse(html, baseUri);

        assertNotNull(doc);
        assertEquals(0, doc.getElementsByTag("h1").size());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseFragmentWithNullFragmentHtml() {
        String fragmentHtml = null;
        Element context = new Element("div");
        String baseUri = "https://example.com";
        
        Parser.parseFragment(fragmentHtml, context, baseUri);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseFragmentWithNullContext() {
        String fragmentHtml = "<div>Hello World</div>";
        Element context = null;
        String baseUri = "https://example.com";
        
        Parser.parseFragment(fragmentHtml, context, baseUri);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseBodyFragmentWithNullBodyHtml() {
        String bodyHtml = null;
        String baseUri = "https://example.com";

        Parser.parseBodyFragment(bodyHtml, baseUri);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseBodyFragmentWithNullBaseUri() {
        String bodyHtml = "<p>Hello World</p>";
        String baseUri = null;

        Parser.parseBodyFragment(bodyHtml, baseUri);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeprecatedParseBodyFragmentRelaxedWithNullBodyHtml() {
        String bodyHtml = null;
        String baseUri = "https://example.com";

        Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testDeprecatedParseBodyFragmentRelaxedWithNullBaseUri() {
        String bodyHtml = "<p>Hello World</p>";
        String baseUri = null;

        Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
    }
}