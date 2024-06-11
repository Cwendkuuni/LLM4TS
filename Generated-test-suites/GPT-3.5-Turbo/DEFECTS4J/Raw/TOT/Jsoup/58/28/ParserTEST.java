import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.Parser;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import java.util.List;

public class ParserTest {
    private Parser parser;

    @Before
    public void setup() {
        parser = new Parser(new HtmlTreeBuilder());
    }

    @Test
    public void testParseInput() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        String baseUri = "http://example.com";

        Document document = parser.parseInput(html, baseUri);

        assertNotNull(document);
        assertEquals("<title>Test</title>", document.head().html());
        assertEquals("<h1>Hello World!</h1>", document.body().html());
    }

    @Test
    public void testGetAndSetTreeBuilder() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();

        parser.setTreeBuilder(treeBuilder);

        assertEquals(treeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors() {
        assertFalse(parser.isTrackErrors());

        parser.setTrackErrors(10);

        assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testGetErrors() {
        List<ParseError> errors = parser.getErrors();

        assertNotNull(errors);
        assertEquals(0, errors.size());
    }

    @Test
    public void testSettings() {
        ParseSettings settings = new ParseSettings(false, false);

        parser.settings(settings);

        assertEquals(settings, parser.settings());
    }

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        String baseUri = "http://example.com";

        Document document = Parser.parse(html, baseUri);

        assertNotNull(document);
        assertEquals("<title>Test</title>", document.head().html());
        assertEquals("<h1>Hello World!</h1>", document.body().html());
    }

    @Test
    public void testParseFragment() {
        String fragmentHtml = "<div><p>Test</p></div>";
        Element context = new Element("div");
        String baseUri = "http://example.com";

        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);

        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertEquals("<p>Test</p>", nodes.get(0).outerHtml());
    }

    @Test
    public void testParseXmlFragment() {
        String fragmentXml = "<root><child>Test</child></root>";
        String baseUri = "http://example.com";

        List<Node> nodes = Parser.parseXmlFragment(fragmentXml, baseUri);

        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertEquals("<child>Test</child>", nodes.get(0).outerHtml());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<div><p>Test</p></div>";
        String baseUri = "http://example.com";

        Document document = Parser.parseBodyFragment(bodyHtml, baseUri);

        assertNotNull(document);
        assertEquals("<p>Test</p>", document.body().html());
    }

    @Test
    public void testUnescapeEntities() {
        String escapedString = "&lt;div&gt;Test&lt;/div&gt;";
        boolean inAttribute = false;

        String unescapedString = Parser.unescapeEntities(escapedString, inAttribute);

        assertNotNull(unescapedString);
        assertEquals("<div>Test</div>", unescapedString);
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<div><p>Test</p></div>";
        String baseUri = "http://example.com";

        Document document = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);

        assertNotNull(document);
        assertEquals("<p>Test</p>", document.body().html());
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