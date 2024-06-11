import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.ParseSettings;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.util.List;

public class ParserTest {

    private Parser parser;

    @Before
    public void setUp() {
        parser = new Parser(new TreeBuilder());
    }

    @Test
    public void testParseInput() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        String baseUri = "http://example.com";
        Document document = parser.parseInput(html, baseUri);
        assertEquals("Test", document.title());
        Element body = document.body();
        assertTrue(body.hasText());
        assertEquals("Hello World!", body.text());
    }

    @Test
    public void testGetTreeBuilder() {
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        assertTrue(treeBuilder != null);
    }

    @Test
    public void testSetTreeBuilder() {
        TreeBuilder treeBuilder = new TreeBuilder();
        parser.setTreeBuilder(treeBuilder);
        assertEquals(treeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors() {
        assertTrue(!parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        int maxErrors = 10;
        parser.setTrackErrors(maxErrors);
        assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testGetErrors() {
        List<ParseError> errors = parser.getErrors();
        assertTrue(errors != null);
    }

    @Test
    public void testSettings() {
        ParseSettings settings = new ParseSettings(true, true);
        parser.settings(settings);
        assertEquals(settings, parser.settings());
    }

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        String baseUri = "http://example.com";
        Document document = Parser.parse(html, baseUri);
        assertEquals("Test", document.title());
        Element body = document.body();
        assertTrue(body.hasText());
        assertEquals("Hello World!", body.text());
    }

    @Test
    public void testParseFragment() {
        String fragmentHtml = "<div><h1>Hello World!</h1></div>";
        Element context = new Element("div");
        String baseUri = "http://example.com";
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
        assertTrue(nodes != null);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        assertEquals("h1", ((Element) nodes.get(0)).tagName());
    }

    @Test
    public void testParseXmlFragment() {
        String fragmentXml = "<div><h1>Hello World!</h1></div>";
        String baseUri = "http://example.com";
        List<Node> nodes = Parser.parseXmlFragment(fragmentXml, baseUri);
        assertTrue(nodes != null);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        assertEquals("h1", ((Element) nodes.get(0)).tagName());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World!</h1>";
        String baseUri = "http://example.com";
        Document document = Parser.parseBodyFragment(bodyHtml, baseUri);
        Element body = document.body();
        assertTrue(body != null);
        assertTrue(body.hasText());
        assertEquals("Hello World!", body.text());
    }

    @Test
    public void testUnescapeEntities() {
        String string = "&lt;div&gt;&nbsp;&amp;&lt;/div&gt;";
        boolean inAttribute = false;
        String unescaped = Parser.unescapeEntities(string, inAttribute);
        assertEquals("<div> &amp;</div>", unescaped);
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<h1>Hello World!</h1>";
        String baseUri = "http://example.com";
        Document document = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        Element body = document.body();
        assertTrue(body != null);
        assertTrue(body.hasText());
        assertEquals("Hello World!", body.text());
    }

    @Test
    public void testHtmlParser() {
        Parser htmlParser = Parser.htmlParser();
        assertTrue(htmlParser != null);
        assertTrue(htmlParser.getTreeBuilder() instanceof HtmlTreeBuilder);
    }

    @Test
    public void testXmlParser() {
        Parser xmlParser = Parser.xmlParser();
        assertTrue(xmlParser != null);
        assertTrue(xmlParser.getTreeBuilder() instanceof XmlTreeBuilder);
    }
}