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
    public void setUp() {
        // Initialize the parser with default TreeBuilder
        parser = new Parser(new TreeBuilder());
    }

    @Test
    public void testParseInput() {
        String html = "<html><body><h1>Hello World!</h1></body></html>";
        String baseUri = "https://example.com";

        Document document = parser.parseInput(html, baseUri);

        assertNotNull(document);
        assertEquals("<html>\n <head></head>\n <body>\n  <h1>Hello World!</h1>\n </body>\n</html>", document.toString());
    }

    @Test
    public void testGetTreeBuilder() {
        TreeBuilder treeBuilder = parser.getTreeBuilder();

        assertNotNull(treeBuilder);
        assertTrue(treeBuilder instanceof TreeBuilder);
    }

    @Test
    public void testSetTreeBuilder() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();

        Parser newParser = parser.setTreeBuilder(treeBuilder);

        assertNotNull(newParser);
        assertEquals(treeBuilder, newParser.getTreeBuilder());
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

        assertNotNull(newParser);
        assertTrue(newParser.isTrackErrors());
        assertEquals(5, newParser.getErrors().size());
    }

    @Test
    public void testGetErrors() {
        List<ParseError> errors = parser.getErrors();
        assertTrue(errors.isEmpty());

        parser.setTrackErrors(3);

        errors = parser.getErrors();
        assertEquals(3, errors.size());
    }

    @Test
    public void testSettings() {
        ParseSettings settings = new ParseSettings(false);
        Parser newParser = parser.settings(settings);

        assertNotNull(newParser);
        assertEquals(settings, newParser.settings());
    }

    @Test
    public void testParse() {
        String html = "<html><body><h1>Hello World!</h1></body></html>";
        String baseUri = "https://example.com";

        Document document = Parser.parse(html, baseUri);

        assertNotNull(document);
        assertEquals("<html>\n <body>\n  <h1>Hello World!</h1>\n </body>\n</html>", document.toString());
    }

    @Test
    public void testParseFragment() {
        String fragmentHtml = "<div><p>Paragraph 1</p><p>Paragraph 2</p></div>";
        Element context = new Element("div");
        String baseUri = "https://example.com";

        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);

        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
    }

    @Test
    public void testParseXmlFragment() {
        String fragmentXml = "<root><child>Child 1</child><child>Child 2</child></root>";
        String baseUri = "https://example.com";

        List<Node> nodes = Parser.parseXmlFragment(fragmentXml, baseUri);

        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Paragraph 1</p><p>Paragraph 2</p>";
        String baseUri = "https://example.com";

        Document document = Parser.parseBodyFragment(bodyHtml, baseUri);

        assertNotNull(document);
        assertEquals("<body>\n <p>Paragraph 1</p>\n <p>Paragraph 2</p>\n</body>", document.body().toString());
    }

    @Test
    public void testUnescapeEntities() {
        String escapedString = "&lt;html&gt;&amp;";
        boolean inAttribute = true;

        String unescapedString = Parser.unescapeEntities(escapedString, inAttribute);

        assertNotNull(unescapedString);
        assertEquals("<html>&", unescapedString);
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<p>Paragraph 1</p><p>Paragraph 2</p>";
        String baseUri = "https://example.com";

        Document document = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);

        assertNotNull(document);
        assertEquals("<html>\n <head></head>\n <body>\n  <p>Paragraph 1</p>\n  <p>Paragraph 2</p>\n </body>\n</html>", document.toString());
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