import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import java.util.List;

public class ParserTest {

    private ParseErrorList errors;
    private TreeBuilder treeBuilder;
    private Parser parser;

    @Before
    public void setUp() {
        errors = ParseErrorList.noTracking();
        treeBuilder = new HtmlTreeBuilder();
        parser = new Parser(treeBuilder);
    }

    @Test
    public void testParseInput() {
        String html = "<html><body><h1>Title</h1></body></html>";
        String baseUri = "https://www.example.com";
        Document doc = parser.parseInput(html, baseUri);

        assertNotNull(doc);
        assertEquals("https://www.example.com", doc.baseUri());
        assertEquals("<html>\n <head></head>\n <body>\n  <h1>Title</h1>\n </body>\n</html>",
                doc.toString().trim());
    }

    @Test
    public void testGetTreeBuilder() {
        TreeBuilder treeBuilder = parser.getTreeBuilder();

        assertNotNull(treeBuilder);
        assertTrue(treeBuilder instanceof HtmlTreeBuilder);
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
        parser.setTrackErrors(10);

        assertTrue(parser.isTrackErrors());

        parser.setTrackErrors(0);
        assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testGetErrors() {
        List<ParseError> errorList = parser.getErrors();

        assertNotNull(errorList);
        assertTrue(errorList.isEmpty());
    }

    @Test
    public void testSettings() {
        ParseSettings settings = new ParseSettings(false, true);
        parser.settings(settings);

        assertEquals(settings, parser.settings());
    }

    @Test
    public void testParse() {
        String html = "<html><body><h1>Title</h1></body></html>";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parse(html, baseUri);

        assertNotNull(doc);
        assertEquals("https://www.example.com", doc.baseUri());
        assertEquals("<html>\n <head></head>\n <body>\n  <h1>Title</h1>\n </body>\n</html>",
                doc.toString().trim());
    }

    @Test
    public void testParseFragment() {
        String fragmentHtml = "<div><p>Paragraph 1</p><p>Paragraph 2</p></div>";
        Element context = new Element("div");
        String baseUri = "https://www.example.com";

        List<Node> nodeList = Parser.parseFragment(fragmentHtml, context, baseUri);

        assertNotNull(nodeList);
        assertEquals(2, nodeList.size());
        assertEquals("div", nodeList.get(0).nodeName());
        assertEquals("p", nodeList.get(1).nodeName());
    }

    @Test
    public void testParseXmlFragment() {
        String fragmentXml = "<root><element attr=\"value\">Text</element></root>";
        String baseUri = "https://www.example.com";

        List<Node> nodeList = Parser.parseXmlFragment(fragmentXml, baseUri);

        assertNotNull(nodeList);
        assertEquals(3, nodeList.size());
        assertEquals("root", nodeList.get(0).nodeName());
        assertEquals("element", nodeList.get(1).nodeName());
        assertEquals("Text", nodeList.get(1).childNodes().get(0).outerHtml());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Paragraph 1</p><p>Paragraph 2</p>";
        String baseUri = "https://www.example.com";

        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

        assertNotNull(doc);
        assertEquals("https://www.example.com", doc.baseUri());
        assertEquals("<html>\n <head></head>\n <body>\n  <p>Paragraph 1</p>\n  <p>Paragraph 2</p>\n </body>\n</html>",
                doc.toString().trim());
    }

    @Test
    public void testUnescapeEntities() {
        String html = "&lt;div&gt;&amp;nbsp;&lt;/div&gt;";
        boolean inAttribute = false;
        String unescapedString = Parser.unescapeEntities(html, inAttribute);

        assertEquals("<div>&nbsp;</div>", unescapedString);
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<p>Paragraph 1</p><p>Paragraph 2</p>";
        String baseUri = "https://www.example.com";

        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);

        assertNotNull(doc);
        assertEquals("https://www.example.com", doc.baseUri());
        assertEquals("<html>\n <head></head>\n <body>\n  <p>Paragraph 1</p>\n  <p>Paragraph 2</p>\n </body>\n</html>",
                doc.toString().trim());
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