
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ParserTest {

    @Test
    public void testParseInput() {
        // Test basic functionality
        Parser parser = new Parser(new TreeBuilder());
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        String baseUri = "http://example.com";
        Document doc = parser.parseInput(html, baseUri);
        assertEquals("Hello, World!", doc.select("h1").text());
    }

    @Test
    public void testGetTreeBuilder() {
        // Test getter for TreeBuilder
        TreeBuilder treeBuilder = new TreeBuilder();
        Parser parser = new Parser(treeBuilder);
        assertEquals(treeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testSetTreeBuilder() {
        // Test setter for TreeBuilder
        TreeBuilder treeBuilder = new TreeBuilder();
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTreeBuilder(treeBuilder);
        assertEquals(treeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors() {
        // Test isTrackErrors method
        Parser parser = new Parser(new TreeBuilder());
        assertEquals(false, parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        // Test setTrackErrors method
        Parser parser = new Parser(new TreeBuilder());
        parser.setTrackErrors(3);
        assertEquals(true, parser.isTrackErrors());
    }

    @Test
    public void testGetErrors() {
        // Test getErrors method
        Parser parser = new Parser(new TreeBuilder());
        assertEquals(null, parser.getErrors());
    }

    @Test
    public void testParse() {
        // Test static parse method
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        assertEquals("Hello, World!", doc.select("h1").text());
    }

    @Test
    public void testParseFragment() {
        // Test static parseFragment method
        String fragmentHtml = "<div>Hello!</div>";
        Element context = new Element("body");
        String baseUri = "http://example.com";
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
        assertEquals(1, nodes.size());
        assertEquals("Hello!", nodes.get(0).outerHtml());
    }

    @Test
    public void testParseBodyFragment() {
        // Test static parseBodyFragment method
        String bodyHtml = "<h1>Hello, World!</h1>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        assertEquals("Hello, World!", doc.select("h1").text());
    }

    // TODO: Add test cases for unescapeHtmlEntities method

    @Test
    public void testParseBodyFragmentRelaxed() {
        // Test deprecated static parseBodyFragmentRelaxed method
        String bodyHtml = "<h1>Hello, World!</h1>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        assertEquals("Hello, World!", doc.select("h1").text());
    }

    @Test
    public void testHtmlParser() {
        // Test htmlParser method
        Parser parser = Parser.htmlParser();
        assertEquals(HtmlTreeBuilder.class, parser.getTreeBuilder().getClass());
    }

    @Test
    public void testXmlParser() {
        // Test xmlParser method
        Parser parser = Parser.xmlParser();
        assertEquals(XmlTreeBuilder.class, parser.getTreeBuilder().getClass());
    }
}

