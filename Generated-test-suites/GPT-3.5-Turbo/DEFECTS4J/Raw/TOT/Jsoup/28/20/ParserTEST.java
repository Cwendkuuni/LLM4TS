import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import static org.junit.Assert.assertEquals;


public class ParserTest {

    private Parser parser;

    @Before
    public void setUp() {
        parser = new Parser(new HtmlTreeBuilder());
    }

    @Test
    public void testParseInput() {
        String html = "<html><body><h1>Test</h1></body></html>";
        String baseUri = "http://example.com";
        
        Document doc = parser.parseInput(html, baseUri);

        assertNotNull("Document should not be null", doc);
        
        // Typical use case
        assertEquals("Document's title should be 'Test'", "Test", doc.title());
        
        // Error scenario
        assertEquals("Number of parse errors should be 0", 0, parser.getErrors().size());
    }

    @Test
    public void testGetTreeBuilder() {
        TreeBuilder treeBuilder = parser.getTreeBuilder();

        assertNotNull("TreeBuilder should not be null", treeBuilder);
        
        // Edge case
        assertEquals("TreeBuilder should be an instance of HtmlTreeBuilder", HtmlTreeBuilder.class, treeBuilder.getClass());
    }

    @Test
    public void testSetTreeBuilder() {
        TreeBuilder newTreeBuilder = new XmlTreeBuilder();
        
        parser.setTreeBuilder(newTreeBuilder);
        
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        
        assertNotNull("TreeBuilder should not be null", treeBuilder);
        
        // Typical use case
        assertEquals("TreeBuilder should be the new TreeBuilder", newTreeBuilder, treeBuilder);
    }

    @Test
    public void testIsTrackErrors() {
        boolean trackErrors = parser.isTrackErrors();
        
        // Typical use case
        assertFalse("Track errors should be disabled by default", trackErrors);
    }

    @Test
    public void testSetTrackErrors() {
        int maxErrors = 5;
        
        parser.setTrackErrors(maxErrors);
        
        boolean trackErrors = parser.isTrackErrors();
        
        // Typical use case
        assertTrue("Track errors should be enabled with a positive maxErrors value", trackErrors);
        
        // Error scenario
        assertEquals("Max errors should be set correctly", maxErrors, parser.getMaxErrors());
    }

    @Test
    public void testGetErrors() {
        List<ParseError> errors = parser.getErrors();
        
        assertNotNull("Errors should not be null", errors);
        
        // Typical use case
        assertEquals("Number of errors should be 0", 0, errors.size());
    }

    @Test
    public void testParse() {
        String html = "<html><body><h1>Test</h1></body></html>";
        String baseUri = "http://example.com";
        
        Document doc = Parser.parse(html, baseUri);
        
        assertNotNull("Document should not be null", doc);
        
        // Typical use case
        assertEquals("Document's title should be 'Test'", "Test", doc.title());
        
        // Error scenario
        assertEquals("Number of parse errors should be 0", 0, parser.getErrors().size());
    }

    @Test
    public void testParseFragment() {
        String fragmentHtml = "<div><p>Hello, World!</p></div>";
        Element context = new Element("div");
        String baseUri = "http://example.com";
        
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);

        assertNotNull("Nodes should not be null", nodes);
        
        // Typical use case
        assertEquals("Number of nodes should be 1", 1, nodes.size());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello, World!</p>";
        String baseUri = "http://example.com";
        
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        assertNotNull("Document should not be null", doc);
        
        // Typical use case
        assertEquals("Document's body should contain 1 child node", 1, doc.body().childNodeSize());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<p>Hello, World!</p>";
        String baseUri = "http://example.com";
        
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        
        assertNotNull("Document should not be null", doc);
        
        // Typical use case
        assertEquals("Document's title should be 'Test'", "Test", doc.title());
        
        // Error scenario
        assertEquals("Number of parse errors should be 0", 0, parser.getErrors().size());
    }

    @Test
    public void testHtmlParser() {
        Parser htmlParser = Parser.htmlParser();
        
        assertNotNull("HTML Parser should not be null", htmlParser);
        
        // Typical use case
        assertEquals("TreeBuilder should be an instance of HtmlTreeBuilder", HtmlTreeBuilder.class, htmlParser.getTreeBuilder().getClass());
    }

    @Test
    public void testXmlParser() {
        Parser xmlParser = Parser.xmlParser();
        
        assertNotNull("XML Parser should not be null", xmlParser);
        
        // Typical use case
        assertEquals("TreeBuilder should be an instance of XmlTreeBuilder", XmlTreeBuilder.class, xmlParser.getTreeBuilder().getClass());
    }
}