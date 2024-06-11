import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    private Parser parser;

    @Before
    public void setUp() {
        parser = new Parser(new HtmlTreeBuilder());
    }

    @Test
    public void testGetTreeBuilder() {
        assertEquals(parser.getTreeBuilder().getClass(), HtmlTreeBuilder.class);
    }

    @Test
    public void testSetTreeBuilder() {
        TreeBuilder treeBuilder = new XmlTreeBuilder();
        parser.setTreeBuilder(treeBuilder);
        assertEquals(parser.getTreeBuilder(), treeBuilder);
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
        assertNull(parser.getErrors());
    }

    @Test
    public void testSettings() {
        ParseSettings settings = new ParseSettings(true);
        parser.settings(settings);
        assertEquals(parser.settings(), settings);
    }

    @Test
    public void testParse() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        Document document = Parser.parse(html, baseUri);
        assertNotNull(document);
        assertEquals(document.title(), "");
    }

    @Test
    public void testParseFragment() {
        String fragmentHtml = "<h1>Test Fragment</h1>";
        Element context = new Element("div");
        String baseUri = "http://example.com";
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
        assertNotNull(nodes);
        assertEquals(nodes.size(), 1);
        assertEquals(nodes.get(0).nodeName(), "h1");
    }

    @Test
    public void testParseXmlFragment() {
        String fragmentXml = "<root><element>Test</element></root>";
        String baseUri = "http://example.com";
        List<Node> nodes = Parser.parseXmlFragment(fragmentXml, baseUri);
        assertNotNull(nodes);
        assertEquals(nodes.size(), 1);
        assertEquals(nodes.get(0).nodeName(), "root");
    }
    
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Test Fragment</h1>";
        String baseUri = "http://example.com";
        Document document = Parser.parseBodyFragment(bodyHtml, baseUri);
        assertNotNull(document);
        assertEquals(document.body().childNode(0).nodeName(), "h1");
    }
    
    @Test
    public void testUnescapeEntities() {
        String escapedString = "&lt;div&gt;Test&lt;/div&gt;";
        String unescapedString = Parser.unescapeEntities(escapedString, false);
        assertEquals(unescapedString, "<div>Test</div>");
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        // Deprecated method. Not tested.
    }
    
    @Test
    public void testHtmlParser() {
        Parser htmlParser = Parser.htmlParser();
        assertEquals(htmlParser.getTreeBuilder().getClass(), HtmlTreeBuilder.class);
    }

    @Test
    public void testXmlParser() {
        Parser xmlParser = Parser.xmlParser();
        assertEquals(xmlParser.getTreeBuilder().getClass(), XmlTreeBuilder.class);
    }
}