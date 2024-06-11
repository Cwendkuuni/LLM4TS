import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import java.util.List;

import static org.junit.Assert.*;
import org.junit.Test;

public class ParserTest {

    private Parser parser = new Parser(new TreeBuilder());

    @Test
    public void testGetTreeBuilder() {
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        assertNotNull(treeBuilder);
    }

    @Test
    public void testSetTreeBuilder() {
        TreeBuilder treeBuilder = new TreeBuilder();
        Parser newParser = parser.setTreeBuilder(treeBuilder);
        assertEquals(treeBuilder, newParser.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors() {
        boolean trackErrors = parser.isTrackErrors();
        assertFalse(trackErrors);
    }

    @Test
    public void testSetTrackErrors() {
        int maxErrors = 10;
        Parser newParser = parser.setTrackErrors(maxErrors);
        assertTrue(newParser.isTrackErrors());
        assertEquals(maxErrors, newParser.getMaxErrors());
    }

    @Test
    public void testGetErrors() {
        List<ParseError> errors = parser.getErrors();
        assertNotNull(errors);
    }

    @Test
    public void testSettings() {
        ParseSettings settings = new ParseSettings();
        Parser newParser = parser.settings(settings);
        assertEquals(settings, newParser.settings());
    }

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello, world!</h1></body></html>";
        Document doc = Parser.parse(html, "");
        assertNotNull(doc);
        assertEquals("<html>", doc.html());
    }

    @Test
    public void testParseFragment() {
        String fragmentHtml = "<h1>Hello, world!</h1>";
        Element context = new Element("div");
        String baseUri = "";
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertEquals("<h1>Hello, world!</h1>", nodes.get(0).outerHtml());
    }

    @Test
    public void testParseXmlFragment() {
        String fragmentXml = "<div><p>Hello, world!</p></div>";
        String baseUri = "";
        List<Node> nodes = Parser.parseXmlFragment(fragmentXml, baseUri);
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertEquals("<div><p>Hello, world!</p></div>", nodes.get(0).outerHtml());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello, world!</h1>";
        String baseUri = "";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        assertNotNull(doc);
        assertEquals("<html><head></head><body><h1>Hello, world!</h1></body></html>", doc.html());
    }

    @Test
    public void testUnescapeEntities() {
        String string = "Hello &lt;world&gt;!";
        boolean inAttribute = false;
        String unescapedString = Parser.unescapeEntities(string, inAttribute);
        assertNotNull(unescapedString);
        assertEquals("Hello <world>!", unescapedString);
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<h1>Hello, world!</h1>";
        String baseUri = "";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        assertNotNull(doc);
        assertEquals("<html><head><title>Test</title></head><body><h1>Hello, world!</h1></body></html>", doc.html());
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