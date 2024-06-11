import org.jsoup.nodes.Document;
import org.jsoup.parser.HtmlTreeBuilder;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class ParserTest {

    private Parser parser;

    @Before
    public void setup() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        parser = new Parser(treeBuilder);
    }

    @Test
    public void testParseInput() {
        String html = "<html><body><p>Hello World!</p></body></html>";
        String baseUri = "http://example.com";
        
        Document doc = parser.parseInput(html, baseUri);
        
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        // Additional assertions can be added to test the content of the parsed Document
    }

    @Test
    public void testSetTreeBuilder() {
        TreeBuilder newTreeBuilder = new HtmlTreeBuilder();
        parser.setTreeBuilder(newTreeBuilder);
        
        assertEquals(newTreeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testSetTrackErrors() {
        parser.setTrackErrors(10);
        
        assertTrue(parser.isTrackErrors());
        assertEquals(10, parser.getErrors().size());
    }

    @Test
    public void testParse() {
        String html = "<html><body><p>Hello World!</p></body></html>";
        String baseUri = "http://example.com";
        
        Document doc = Parser.parse(html, baseUri);
        
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        // Additional assertions can be added to test the content of the parsed Document
    }

    @Test
    public void testParseFragment() {
        String fragmentHtml = "<p>Hello World!</p>";
        Document doc = Parser.parseBodyFragment(fragmentHtml, "http://example.com");
        
        assertNotNull(doc.body());
        assertEquals(1, doc.body().children().size());
        // Additional assertions can be added to test the parsed fragment
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello World!</p>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");
        
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertEquals(1, doc.body().children().size());
        // Additional assertions can be added to test the parsed body fragment
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<p>Hello World!</p>";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "http://example.com");
        
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        // Additional assertions can be added to test the content of the parsed Document
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