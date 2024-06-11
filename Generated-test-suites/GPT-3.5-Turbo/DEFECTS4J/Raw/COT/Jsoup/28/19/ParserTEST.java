
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import java.util.List;

public class ParserTest {
    private Parser parser;
    private String html;
    private String baseUri;
    private Element context;

    @Before
    public void setUp() {
        parser = new Parser(new HtmlTreeBuilder());
        html = "<html><body><p>Hello, World!</p></body></html>";
        baseUri = "http://example.com";
        context = new Element("div");
    }

    @Test
    public void testParseInput() {
        Document doc = parser.parseInput(html, baseUri);
        // Assert that the document is correctly parsed
        assertNotNull(doc);
        assertEquals(html, doc.html());
        assertEquals(baseUri, doc.baseUri());
    }

    @Test
    public void testGetTreeBuilder() {
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        // Assert that the correct TreeBuilder instance is returned
        assertNotNull(treeBuilder);
        assertTrue(treeBuilder instanceof HtmlTreeBuilder);
    }

    @Test
    public void testSetTreeBuilder() {
        TreeBuilder treeBuilder = new XmlTreeBuilder();
        Parser updatedParser = parser.setTreeBuilder(treeBuilder);
        // Assert that the TreeBuilder is updated correctly
        assertNotNull(updatedParser);
        assertEquals(treeBuilder, updatedParser.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors() {
        // Assert that error tracking is disabled by default
        assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        int maxErrors = 10;
        Parser updatedParser = parser.setTrackErrors(maxErrors);
        // Assert that error tracking is enabled with the specified maximum errors
        assertNotNull(updatedParser);
        assertTrue(updatedParser.isTrackErrors());
        assertEquals(maxErrors, updatedParser.getMaxErrors());
    }

    @Test
    public void testGetErrors() {
        List<ParseError> errors = parser.getErrors();
        // Assert that an empty list of errors is returned by default
        assertNotNull(errors);
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testParse() {
        Document doc = Parser.parse(html, baseUri);
        // Assert that the document is correctly parsed
        assertNotNull(doc);
        assertEquals(html, doc.html());
        assertEquals(baseUri, doc.baseUri());
    }

    @Test
    public void testParseFragment() {
        List<Node> nodeList = Parser.parseFragment(html, context, baseUri);
        // Assert that a list of nodes is parsed correctly
        assertNotNull(nodeList);
        assertFalse(nodeList.isEmpty());
        assertEquals(1, nodeList.size());
        assertTrue(nodeList.get(0) instanceof Element);
        Element element = (Element) nodeList.get(0);
        assertEquals("html", element.tagName());
        assertEquals(baseUri, element.baseUri());
    }

    @Test
    public void testParseBodyFragment() {
        Document doc = Parser.parseBodyFragment(html, baseUri);
        // Assert that the document is correctly parsed with HTML fragment in the body
        assertNotNull(doc);
        Element body = doc.body();
        assertNotNull(body);
        assertEquals(html, body.html());
        assertEquals(baseUri, doc.baseUri());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        Document doc = Parser.parseBodyFragmentRelaxed(html, baseUri);
        // Assert that the document is correctly parsed with HTML fragment
        assertNotNull(doc);
        assertEquals(html, doc.html());
        assertEquals(baseUri, doc.baseUri());
    }

    @Test
    public void testHtmlParser() {
        Parser htmlParser = Parser.htmlParser();
        // Assert that an instance of Parser with HtmlTreeBuilder is returned
        assertNotNull(htmlParser);
        assertTrue(htmlParser.getTreeBuilder() instanceof HtmlTreeBuilder);
    }

    @Test
    public void testXmlParser() {
        Parser xmlParser = Parser.xmlParser();
        // Assert that an instance of Parser with XmlTreeBuilder is returned
        assertNotNull(xmlParser);
        assertTrue(xmlParser.getTreeBuilder() instanceof XmlTreeBuilder);
    }
}

