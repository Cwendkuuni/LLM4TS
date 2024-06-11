import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ParserTest {
    private Parser parser;

    @Before
    public void setUp() {
        parser = new Parser(new TreeBuilder());
    }

    @Test
    public void testParseInput_typicalCase() {
        String html = "<html><body><h1>Title</h1></body></html>";
        String baseUri = "http://example.com";
        Document document = parser.parseInput(html, baseUri);

        assertNotNull(document);
        assertEquals("http://example.com", document.baseUri());
        Element body = document.body();
        assertNotNull(body);
        Element h1 = body.getElementsByTag("h1").first();
        assertNotNull(h1);
        assertEquals("Title", h1.text());
    }

    @Test
    public void testParseInput_emptyHtml() {
        String html = "";
        String baseUri = "http://example.com";
        Document document = parser.parseInput(html, baseUri);

        assertNotNull(document);
        assertEquals("http://example.com", document.baseUri());
        assertNotNull(document.body());
        assertEquals(0, document.body().children().size());
    }

    @Test
    public void testParseInput_errorTrackingEnabled() {
        parser.setTrackErrors(5);
        String html = "<html><body><h1>Title</h1</body></html>";
        String baseUri = "http://example.com";
        Document document = parser.parseInput(html, baseUri);

        assertEquals(1, parser.getErrors().size());
    }

    @Test
    public void testGetTreeBuilder() {
        TreeBuilder treeBuilder = parser.getTreeBuilder();

        assertNotNull(treeBuilder);
    }

    @Test
    public void testSetTreeBuilder() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        parser.setTreeBuilder(treeBuilder);

        assertSame(treeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors() {
        assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        parser.setTrackErrors(10);

        assertTrue(parser.isTrackErrors());
        assertEquals(10, parser.getTrackErrors());
    }

    @Test
    public void testGetErrors() {
        assertNotNull(parser.getErrors());
        assertEquals(0, parser.getErrors().size());
    }

    @Test
    public void testSettings() {
        ParseSettings settings = new ParseSettings(false, true);
        parser.settings(settings);

        assertSame(settings, parser.settings());
    }

    @Test
    public void testParse_htmlFragment_typicalCase() {
        String html = "<div><p>Paragraph 1</p><p>Paragraph 2</p></div>";
        String baseUri = "http://example.com";
        List<Node> nodes = Parser.parseFragment(html, null, baseUri);

        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        Element div = (Element) nodes.get(0);
        assertEquals("div", div.tagName());
        assertEquals(2, div.getElementsByTag("p").size());
        assertEquals("Paragraph 1", div.getElementsByTag("p").get(0).text());
        assertEquals("Paragraph 2", div.getElementsByTag("p").get(1).text());
    }

    @Test
    public void testParse_htmlFragment_emptyHtml() {
        String html = "";
        String baseUri = "http://example.com";
        List<Node> nodes = Parser.parseFragment(html, null, baseUri);

        assertNotNull(nodes);
        assertEquals(0, nodes.size());
    }

    @Test
    public void testParse_htmlFragment_withContextElement() {
        String html = "<p>Paragraph</p>";
        String baseUri = "http://example.com";
        Element context = new Element("div");
        List<Node> nodes = Parser.parseFragment(html, context, baseUri);

        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        Element p = (Element) nodes.get(0);
        assertEquals("p", p.tagName());
        assertEquals("Paragraph", p.text());
    }

    @Test
    public void testParse_xmlFragment_typicalCase() {
        String xml = "<root><element1/><element2/></root>";
        String baseUri = "http://example.com";
        List<Node> nodes = Parser.parseXmlFragment(xml, baseUri);

        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        Element root = (Element) nodes.get(0);
        assertEquals("root", root.tagName());
        assertEquals(2, root.children().size());
        assertEquals("element1", root.children().get(0).tagName());
        assertEquals("element2", root.children().get(1).tagName());
    }

    @Test
    public void testParseBodyFragment_typicalCase() {
        String bodyHtml = "<h1>Title</h1><p>Paragraph</p>";
        String baseUri = "http://example.com";
        Document document = Parser.parseBodyFragment(bodyHtml, baseUri);

        assertNotNull(document);
        assertEquals("http://example.com", document.baseUri());
        Element body = document.body();
        assertNotNull(body);
        assertEquals(2, body.children().size());
        assertEquals("h1", body.children().get(0).tagName());
        assertEquals("Title", body.children().get(0).text());
        assertEquals("p", body.children().get(1).tagName());
        assertEquals("Paragraph", body.children().get(1).text());
    }

    @Test
    public void testUnescapeEntities() {
        String escaped = "&#x62;&#x61;&#x73;&#x69;&#x65;";
        String unescaped = Parser.unescapeEntities(escaped, false);

        assertEquals("baseIE", unescaped);
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<h1>Title</h1><p>Paragraph</p>";
        String baseUri = "http://example.com";
        Document document = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);

        assertNotNull(document);
        assertEquals("http://example.com", document.baseUri());
        Element body = document.body();
        assertNotNull(body);
        assertEquals(2, body.children().size());
        assertEquals("h1", body.children().get(0).tagName());
        assertEquals("Title", body.children().get(0).text());
        assertEquals("p", body.children().get(1).tagName());
        assertEquals("Paragraph", body.children().get(1).text());
    }
}