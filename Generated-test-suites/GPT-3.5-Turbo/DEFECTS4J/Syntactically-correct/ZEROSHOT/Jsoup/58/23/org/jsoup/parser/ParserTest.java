import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import org.jsoup.parser.Parser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.HtmlTreeBuilder;
import org.jsoup.parser.XmlTreeBuilder;

import java.util.List;

public class ParserTest {
    private Parser parser;

    @Before
    public void setUp() {
        parser = new Parser(new HtmlTreeBuilder());
    }

    @Test
    public void testGetTreeBuilder() {
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        assertNotNull(treeBuilder);
    }

    @Test
    public void testSetTreeBuilder() {
        TreeBuilder treeBuilder = new XmlTreeBuilder();
        parser.setTreeBuilder(treeBuilder);
        assertEquals(treeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors() {
        boolean trackErrors = parser.isTrackErrors();
        assertFalse(trackErrors);
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
        ParseSettings settings = parser.settings();
        assertNotNull(settings);
    }

    @Test
    public void testParse() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);

        assertNotNull(doc);
        assertEquals("Hello World", doc.select("h1").text());
    }

    @Test
    public void testParseFragment() {
        String fragmentHtml = "<div><p>Hello World</p></div>";
        Element context = new Element("div");
        String baseUri = "http://example.com";
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);

        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertEquals("Hello World", nodes.get(0).text());
    }

    @Test
    public void testParseXmlFragment() {
        String fragmentXml = "<root><element>Hello World</element></root>";
        String baseUri = "http://example.com";
        List<Node> nodes = Parser.parseXmlFragment(fragmentXml, baseUri);

        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertEquals("Hello World", nodes.get(0).text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello World</p>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

        assertNotNull(doc);
        assertEquals("Hello World", doc.body().select("p").text());
    }

    @Test
    public void testUnescapeEntities() {
        String escapedString = "&lt;p&gt;Hello World&lt;/p&gt;";
        boolean inAttribute = false;
        String unescapedString = Parser.unescapeEntities(escapedString, inAttribute);

        assertNotNull(unescapedString);
        assertEquals("<p>Hello World</p>", unescapedString);
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<p>Hello World</p>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);

        assertNotNull(doc);
        assertEquals("Hello World", doc.select("p").text());
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