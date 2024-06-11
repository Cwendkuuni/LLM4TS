import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.List;

public class ParserTest {
    private Parser parser;
    private String html;
    private String baseUri;

    @Before
    public void setUp() {
        parser = new Parser(new TreeBuilder());
        html = "<html><head><title>Test</title></head><body><h1>Heading</h1><p>Paragraph</p></body></html>";
        baseUri = "https://www.example.com";
    }

    @Test
    public void testParseInput() {
        Document doc = parser.parseInput(html, baseUri);
        assertNotNull(doc);
        assertEquals("Test", doc.title());
        assertEquals("Heading", doc.select("h1").first().text());
        assertEquals("Paragraph", doc.select("p").first().text());
    }

    @Test
    public void testGetTreeBuilder() {
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        assertNotNull(treeBuilder);
    }

    @Test
    public void testSetTreeBuilder() {
        TreeBuilder treeBuilder = new TreeBuilder();
        Parser newParser = parser.setTreeBuilder(treeBuilder);
        assertNotNull(newParser);
        assertEquals(treeBuilder, newParser.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors() {
        boolean trackErrors = parser.isTrackErrors();
        assertFalse(trackErrors);
    }

    @Test
    public void testSetTrackErrors() {
        int maxErrors = 5;
        Parser newParser = parser.setTrackErrors(maxErrors);
        assertNotNull(newParser);
        assertTrue(newParser.isTrackErrors());
    }

    @Test
    public void testGetErrors() {
        List<ParseError> errors = parser.getErrors();
        assertNotNull(errors);
        assertEquals(0, errors.size());
    }

    @Test
    public void testParse() {
        Document doc = Parser.parse(html, baseUri);
        assertNotNull(doc);
        assertEquals("Test", doc.title());
        assertEquals("Heading", doc.select("h1").first().text());
        assertEquals("Paragraph", doc.select("p").first().text());
    }

    @Test
    public void testParseFragment() {
        Element context = new Element("div");
        List<Node> nodes = Parser.parseFragment(html, context, baseUri);
        assertNotNull(nodes);
        assertEquals(3, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        assertTrue(nodes.get(1) instanceof TextNode);
        assertTrue(nodes.get(2) instanceof Element);
    }

    @Test
    public void testParseBodyFragment() {
        Document doc = Parser.parseBodyFragment(html, baseUri);
        assertNotNull(doc);
        assertEquals("Heading", doc.select("h1").first().text());
        assertEquals("Paragraph", doc.select("p").first().text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        Document doc = Parser.parseBodyFragmentRelaxed(html, baseUri);
        assertNotNull(doc);
        assertEquals("Test", doc.title());
        assertEquals("Heading", doc.select("h1").first().text());
        assertEquals("Paragraph", doc.select("p").first().text());
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