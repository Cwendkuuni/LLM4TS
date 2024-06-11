import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
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
        parser = new Parser(new TreeBuilder());
    }

    @Test
    public void testParseInput() {
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        String baseUri = "http://example.com";

        Document document = parser.parseInput(html, baseUri);

        assertNotNull(document);
        assertEquals("Hello, World!", document.body().select("h1").text());
    }

    @Test
    public void testGetTreeBuilder() {
        TreeBuilder treeBuilder = parser.getTreeBuilder();

        assertNotNull(treeBuilder);
    }

    @Test
    public void testSetTreeBuilder() {
        TreeBuilder newTreeBuilder = new TreeBuilder();
        parser.setTreeBuilder(newTreeBuilder);

        assertEquals(newTreeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors() {
        assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        parser.setTrackErrors(10);

        assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testGetErrors() {
        List<ParseError> errors = parser.getErrors();

        assertNotNull(errors);
    }

    @Test
    public void testSettings() {
        parser.settings(new ParseSettings());

        assertNotNull(parser.settings());
    }

    @Test
    public void testParse() {
        String html = "<p>Paragraph</p>";
        String baseUri = "http://example.com";

        Document document = Parser.parse(html, baseUri);

        assertNotNull(document);
        assertEquals("Paragraph", document.select("p").text());
    }

    @Test
    public void testParseFragment() {
        String fragmentHtml = "<div>Hello, World!</div>";
        Element context = new Element("div");
        String baseUri = "http://example.com";

        List<Node> nodeList = Parser.parseFragment(fragmentHtml, context, baseUri);

        assertNotNull(nodeList);
        assertEquals("Hello, World!", nodeList.get(0).text());
    }

    @Test
    public void testParseXmlFragment() {
        String fragmentXml = "<root><node>XML Node</node></root>";
        String baseUri = "http://example.com";

        List<Node> nodeList = Parser.parseXmlFragment(fragmentXml, baseUri);

        assertNotNull(nodeList);
        assertEquals("XML Node", nodeList.get(0).text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h2>Heading</h2><p>Paragraph</p>";
        String baseUri = "http://example.com";

        Document document = Parser.parseBodyFragment(bodyHtml, baseUri);

        assertNotNull(document);
        assertEquals("Heading", document.select("h2").text());
    }

    @Test
    public void testUnescapeEntities() {
        String escapedString = "&lt;div&gt;Escaped&lt;/div&gt;";
        String unescapedString = Parser.unescapeEntities(escapedString, true);

        assertEquals("<div>Escaped</div>", unescapedString);
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String html = "<p>Paragraph</p>";
        String baseUri = "http://example.com";

        Document document = Parser.parseBodyFragmentRelaxed(html, baseUri);

        assertNotNull(document);
        assertEquals("Paragraph", document.body().select("p").text());
    }

    @Test
    public void testHtmlParser() {
        Parser htmlParser = Parser.htmlParser();

        assertTrue(htmlParser instanceof Parser);
    }

    @Test
    public void testXmlParser() {
        Parser xmlParser = Parser.xmlParser();

        assertTrue(xmlParser instanceof Parser);
    }
}