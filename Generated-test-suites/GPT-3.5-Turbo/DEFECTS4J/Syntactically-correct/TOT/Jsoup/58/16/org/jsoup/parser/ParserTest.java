import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class ParserTest {

    private Parser parser;

    @Before
    public void setup() {
        parser = new Parser(new HtmlTreeBuilder());
    }

    @Test
    public void testParseInput_typicalCase() {
        String html = "<html><body><h1>Test</h1></body></html>";
        String baseUri = "http://www.example.com";

        Document document = parser.parseInput(html, baseUri);

        assertNotNull(document);
        assertEquals("http://www.example.com", document.baseUri());
        assertEquals(1, document.getElementsByTag("h1").size());
        assertEquals("Test", document.getElementsByTag("h1").get(0).text());
    }

    @Test
    public void testParseInput_emptyHtml() {
        String html = "";
        String baseUri = "http://www.example.com";

        Document document = parser.parseInput(html, baseUri);

        assertNotNull(document);
        assertEquals("http://www.example.com", document.baseUri());
        assertEquals(0, document.getAllElements().size());
    }

    @Test
    public void testParseInput_nullHtml() {
        String baseUri = "http://www.example.com";

        Document document = parser.parseInput(null, baseUri);

        assertNotNull(document);
        assertEquals("http://www.example.com", document.baseUri());
        assertEquals(0, document.getAllElements().size());
    }

    @Test
    public void testGetTreeBuilder() {
        HtmlTreeBuilder htmlTreeBuilder = new HtmlTreeBuilder();
        parser.setTreeBuilder(htmlTreeBuilder);

        TreeBuilder treeBuilder = parser.getTreeBuilder();

        assertNotNull(treeBuilder);
        assertEquals(htmlTreeBuilder, treeBuilder);
    }

    @Test
    public void testSetTreeBuilder() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();

        Parser result = parser.setTreeBuilder(treeBuilder);

        assertNotNull(result);
        assertEquals(treeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors() {
        assertFalse(parser.isTrackErrors());

        parser.setTrackErrors(5);

        assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        Parser result = parser.setTrackErrors(10);

        assertNotNull(result);
        assertTrue(parser.isTrackErrors());
        assertEquals(10, parser.getErrors().size());
    }

    @Test
    public void testGetErrors() {
        parser.setTrackErrors(3);

        List<ParseError> errors = parser.getErrors();

        assertNotNull(errors);
        assertEquals(3, errors.size());
    }

    @Test
    public void testSettings() {
        ParseSettings settings = new ParseSettings(false, false);

        Parser result = parser.settings(settings);

        assertNotNull(result);
        assertEquals(settings, parser.settings());
    }

    @Test
    public void testSettings_defaultSettings() {
        ParseSettings settings = parser.settings();

        assertNotNull(settings);
        assertFalse(settings.isEnableHtml5Mode());
        assertFalse(settings.isPreserveTagCase());
    }

    @Test
    public void testParse_typicalCase() {
        String html = "<html><body><h1>Test</h1></body></html>";
        String baseUri = "http://www.example.com";

        Document document = Parser.parse(html, baseUri);

        assertNotNull(document);
        assertEquals("http://www.example.com", document.baseUri());
        assertEquals(1, document.getElementsByTag("h1").size());
        assertEquals("Test", document.getElementsByTag("h1").get(0).text());
    }

    @Test
    public void testParse_emptyHtml() {
        String html = "";
        String baseUri = "http://www.example.com";

        Document document = Parser.parse(html, baseUri);

        assertNotNull(document);
        assertEquals("http://www.example.com", document.baseUri());
        assertEquals(0, document.getAllElements().size());
    }

    @Test
    public void testParse_nullHtml() {
        String baseUri = "http://www.example.com";

        Document document = Parser.parse(null, baseUri);

        assertNotNull(document);
        assertEquals("http://www.example.com", document.baseUri());
        assertEquals(0, document.getAllElements().size());
    }

    @Test
    public void testParseFragment_typicalCase() {
        String fragmentHtml = "<h1>Test</h1>";
        Element context = new Element("div");
        String baseUri = "http://www.example.com";

        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);

        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        assertEquals("h1", ((Element) nodes.get(0)).tagName());
        assertEquals("Test", ((Element) nodes.get(0)).text());
    }

    @Test
    public void testParseFragment_emptyHtml() {
        String fragmentHtml = "";
        Element context = new Element("div");
        String baseUri = "http://www.example.com";

        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);

        assertNotNull(nodes);
        assertEquals(0, nodes.size());
    }

    @Test
    public void testParseFragment_nullHtml() {
        Element context = new Element("div");
        String baseUri = "http://www.example.com";

        List<Node> nodes = Parser.parseFragment(null, context, baseUri);

        assertNotNull(nodes);
        assertEquals(0, nodes.size());
    }

    @Test
    public void testParseXmlFragment_typicalCase() {
        String fragmentXml = "<h1>Test</h1>";
        String baseUri = "http://www.example.com";

        List<Node> nodes = Parser.parseXmlFragment(fragmentXml, baseUri);

        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        assertEquals("h1", ((Element) nodes.get(0)).tagName());
        assertEquals("Test", ((Element) nodes.get(0)).text());
    }

    @Test
    public void testParseXmlFragment_emptyHtml() {
        String fragmentXml = "";
        String baseUri = "http://www.example.com";

        List<Node> nodes = Parser.parseXmlFragment(fragmentXml, baseUri);

        assertNotNull(nodes);
        assertEquals(0, nodes.size());
    }

    @Test
    public void testParseBodyFragment_typicalCase() {
        String bodyHtml = "<h1>Test</h1>";
        String baseUri = "http://www.example.com";

        Document document = Parser.parseBodyFragment(bodyHtml, baseUri);

        assertNotNull(document);
        assertEquals("http://www.example.com", document.baseUri());

        Element body = document.body();
        assertNotNull(body);
        assertEquals(1, body.getElementsByTag("h1").size());
        assertEquals("Test", body.getElementsByTag("h1").get(0).text());
    }

    @Test
    public void testUnescapeEntities_typicalCase() {
        String escapedString = "&lt;div&gt;Test&lt;/div&gt;";
        String unescapedString = "<div>Test</div>";

        String result = Parser.unescapeEntities(escapedString, false);

        assertEquals(unescapedString, result);
    }

    @Test
    public void testParseBodyFragmentRelaxed_typicalCase() {
        String bodyHtml = "<h1>Test</h1>";
        String baseUri = "http://www.example.com";

        Document document = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);

        assertNotNull(document);
        assertEquals("http://www.example.com", document.baseUri());
        assertEquals(1, document.getElementsByTag("h1").size());
        assertEquals("Test", document.getElementsByTag("h1").get(0).text());
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