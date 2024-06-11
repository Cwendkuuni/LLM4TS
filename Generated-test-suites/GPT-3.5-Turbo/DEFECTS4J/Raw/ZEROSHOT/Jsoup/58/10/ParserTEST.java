import org.junit.Test;

import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParseInput() {
        Parser parser = new Parser(new TreeBuilder());

        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        String baseUri = "http://example.com";

        Document document = parser.parseInput(html, baseUri);

        assertNotNull(document);
        assertEquals(html, document.html());
        assertEquals(baseUri, document.baseUri());
        assertEquals("Hello, World!", document.select("h1").text());
    }

    @Test
    public void testGetTreeBuilder() {
        Parser parser = new Parser(new TreeBuilder());

        TreeBuilder treeBuilder = parser.getTreeBuilder();

        assertNotNull(treeBuilder);
    }

    @Test
    public void testSetTreeBuilder() {
        Parser parser = new Parser(new TreeBuilder());

        TreeBuilder treeBuilder = new TreeBuilder();
        parser.setTreeBuilder(treeBuilder);

        assertEquals(treeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new TreeBuilder());

        assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        Parser parser = new Parser(new TreeBuilder());

        parser.setTrackErrors(10);

        assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testGetErrors() {
        Parser parser = new Parser(new TreeBuilder());

        List<ParseError> errors = parser.getErrors();

        assertNotNull(errors);
        assertEquals(0, errors.size());
    }

    @Test
    public void testSettings() {
        Parser parser = new Parser(new TreeBuilder());

        ParseSettings settings = new ParseSettings();
        parser.settings(settings);

        assertEquals(settings, parser.settings());
    }

    @Test
    public void testParse() {
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        String baseUri = "http://example.com";

        Document document = Parser.parse(html, baseUri);

        assertNotNull(document);
        assertEquals(html, document.html());
        assertEquals(baseUri, document.baseUri());
        assertEquals("Hello, World!", document.select("h1").text());
    }

    @Test
    public void testParseFragment() {
        Element context = new Element("div");
        String fragmentHtml = "<h1>Hello, World!</h1>";
        String baseUri = "http://example.com";

        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);

        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertEquals("h1", nodes.get(0).nodeName());
        assertEquals("Hello, World!", nodes.get(0).text());
    }

    @Test
    public void testParseXmlFragment() {
        String fragmentXml = "<root><element>Hello, World!</element></root>";
        String baseUri = "http://example.com";

        List<Node> nodes = Parser.parseXmlFragment(fragmentXml, baseUri);

        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertEquals("root", nodes.get(0).nodeName());
        assertEquals("Hello, World!", nodes.get(0).select("element").text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello, World!</h1>";
        String baseUri = "http://example.com";

        Document document = Parser.parseBodyFragment(bodyHtml, baseUri);

        assertNotNull(document);
        assertEquals("<html><head></head><body><h1>Hello, World!</h1></body></html>", document.html());
        assertEquals(baseUri, document.baseUri());
        assertEquals("Hello, World!", document.select("h1").text());
    }

    @Test
    public void testUnescapeEntities() {
        String string = "&lt;div&gt;Hello, World!&lt;/div&gt;";
        boolean inAttribute = false;

        String unescaped = Parser.unescapeEntities(string, inAttribute);

        assertEquals("<div>Hello, World!</div>", unescaped);
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<h1>Hello, World!</h1>";
        String baseUri = "http://example.com";

        Document document = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);

        assertNotNull(document);
        assertEquals("<html><head></head><body><h1>Hello, World!</h1></body></html>", document.html());
        assertEquals(baseUri, document.baseUri());
        assertEquals("Hello, World!", document.select("h1").text());
    }

    @Test
    public void testHtmlParser() {
        Parser parser = Parser.htmlParser();

        assertNotNull(parser);
        assertTrue(parser.getTreeBuilder() instanceof HtmlTreeBuilder);
    }

    @Test
    public void testXmlParser() {
        Parser parser = Parser.xmlParser();

        assertNotNull(parser);
        assertTrue(parser.getTreeBuilder() instanceof XmlTreeBuilder);
    }
}