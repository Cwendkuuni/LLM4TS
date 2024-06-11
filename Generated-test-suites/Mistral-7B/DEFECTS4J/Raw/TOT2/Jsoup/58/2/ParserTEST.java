import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.parser.ParserTest;
import org.junit.Test;

public class ParserTest1 {
    // Test constructor
    @Test
    public void testParserConstructor() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        assertNotNull(parser);
    }

    // Test parseInput method with valid HTML
    @Test
    public void testParseInputValidHtml() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Document doc = parser.parseInput(html, "http://localhost");
        assertNotNull(doc);
        assertEquals("Hello World", doc.select("h1").text());
    }

    // Test parseInput method with empty HTML
    @Test
    public void testParseInputEmptyHtml() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        String html = "";
        Document doc = parser.parseInput(html, "http://localhost");
        assertNull(doc);
    }

    // Test parseInput method with baseUri as null
    @Test(expected = IllegalArgumentException.class)
    public void testParseInputNullBaseUri() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        String html = "<html><body><h1>Hello World</h1></body></html>";
        parser.parseInput(html, null);
    }

    // Test parseInput method with invalid HTML
    @Test
    public void testParseInputInvalidHtml() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        String html = "<html<body><h1>Hello World</h1>";
        Document doc = parser.parseInput(html, "http://localhost");
        assertNull(doc);
    }

    // Test getTreeBuilder method
    @Test
    public void testGetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        assertSame(treeBuilder, parser.treeBuilder);
    }

    // Test setTreeBuilder method
    @Test
    public void testSetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder newTreeBuilder = new HtmlTreeBuilder();
        parser.setTreeBuilder(newTreeBuilder);
        assertSame(newTreeBuilder, parser.treeBuilder);
    }

    // Test isTrackErrors method
    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        assertFalse(parser.isTrackErrors());

        parser.setTrackErrors(1);
        assertTrue(parser.isTrackErrors());
    }

    // Test setTrackErrors method
    @Test
    public void testSetTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(1);
        assertEquals(1, parser.maxErrors);

        parser.setTrackErrors(0);
        assertEquals(0, parser.maxErrors);
    }

    // Test getErrors method
    @Test
    public void testGetErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(1);
        String html = "<html><body><h1>Hello World</h1></body></html>";
        parser.parseInput(html, "http://localhost");
        List<ParseError> errors = parser.getErrors();
        assertNotNull(errors);
        assertEquals(0, errors.size());

        html = "<html<body><h1>Hello World</h1>";
        parser.setMaxErrors(2);
        parser.parseInput(html, "http://localhost");
        errors = parser.getErrors();
        assertNotNull(errors);
        assertEquals(2, errors.size());
    }

    // Test settings method
    @Test
    public void testSettings() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        ParseSettings settings = parser.settings();
        assertNotNull(settings);
    }

    // Test settings method with custom settings
    @Test
    public void testSettingsCustom() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        ParseSettings settings = parser.settings();
        settings.setDefaultParseHandler(new DefaultParseHandler() {
            @Override
            public void handleStartTag(int depth, IntStringMap atts, NameTag nameTag, Bool isSelfClosing) {
                fail("Should not be called");
            }

            @Override
            public void handleStartTag(int depth, String tagName, IntStringMap atts) {
                fail("Should not be called");
            }

            @Override
            public void handleText(String text, int length) {
                fail("Should not be called");
            }

            @Override
            public void handleEndTag(int depth, String tagName) {
                fail("Should not be called");
            }
        });
        parser.settings(settings);
    }

    // Test parse static method with valid HTML
    @Test
    public void testParseValidHtml() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Document doc = Parser.parse(html, "http://localhost");
        assertNotNull(doc);
        assertEquals("Hello World", doc.select("h1").text());
    }

    // Test parse static method with empty HTML
    @Test
    public void testParseEmptyHtml() {
        String html = "";
        Document doc = Parser.parse(html, "http://localhost");
        assertNull(doc);
    }

    // Test parse static method with null baseUri
    @Test(expected = IllegalArgumentException.class)
    public void testParseNullBaseUri() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Parser.parse(html, null);
    }

    // Test parse static method with invalid HTML
    @Test
    public void testParseInvalidHtml() {
        String html = "<html<body><h1>Hello World</h1>";
        Document doc = Parser.parse(html, "http://localhost");
        assertNull(doc);
    }

    // Test parseFragment static method with valid HTML
    @Test
    public void testParseFragmentValidHtml() {
        String html = "<p>Hello World</p>";
        List<Node> nodes = Parser.parseFragment(html, null, "http://localhost");
        assertEquals(1, nodes.size());
        Element pElement = (Element) nodes.get(0);
        assertEquals("Hello World", pElement.text());
    }

    // Test parseFragment static method with empty HTML
    @Test
    public void testParseFragmentEmptyHtml() {
        String html = "";
        List<Node> nodes = Parser.parseFragment(html, null, "http://localhost");
        assertEmpty(nodes);
    }

    // Test parseFragment static method with null context
    @Test
    public void testParseFragmentNullContext() {
        String html = "<p>Hello World</p>";
        List<Node> nodes = Parser.parseFragment(html, null, "http://localhost");
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        Element pElement = (Element) nodes.get(0);
        assertNull(pElement.parent());
    }

    // Test parseFragment static method with invalid HTML
    @Test
    public void testParseFragmentInvalidHtml() {
        String html = "<html<body><p>Hello World</p>";
        List<Node> nodes = Parser.parseFragment(html, null, "http://localhost");
        assertNull(nodes);
    }

    // Test parseXmlFragment static method with valid XML
    @Test
    public void testParseXmlFragmentValidXml() {
        String xml = "<xml><data>Hello World</data></xml>";
        List<Node> nodes = Parser.parseXmlFragment(xml, "http://localhost");
        assertEquals(1, nodes.size());
        Element dataElement = (Element) nodes.get(0);
        assertEquals("Hello World", dataElement.text());
    }

    // Test parseXmlFragment static method with empty XML
    @Test
    public void testParseXmlFragmentEmptyXml() {
        String xml = "";
        List<Node> nodes = Parser.parseXmlFragment(xml, "http://localhost");
        assertEmpty(nodes);
    }

    // Test parseBodyFragment static method with valid HTML
    @Test
    public void testParseBodyFragmentValidHtml() {
        String html = "<html><body><p>Hello World</p></body></html>";
        Document doc = Parser.parseBodyFragment(html, "http://localhost");
        assertNotNull(doc);
        assertEquals("Hello World", doc.select("p").text());
    }

    // Test parseBodyFragment static method with empty HTML
    @Test
    public void testParseBodyFragmentEmptyHtml() {
        String html = "<html><body></body></html>";
        Document doc = Parser.parseBodyFragment(html, "http://localhost");
        assertNotNull(doc);
        assertTrue(doc.body().text().isEmpty());
    }

    // Test parseBodyFragment static method with null baseUri
    @Test(expected = IllegalArgumentException.class)
    public void testParseBodyFragmentNullBaseUri() {
        String html = "<html><body><p>Hello World</p></body></html>";
        Parser.parseBodyFragment(html, null);
    }

    // Test parseBodyFragment static method with invalid HTML
    @Test(expected = IllegalArgumentException.class)
    public void testParseBodyFragmentInvalidHtml() {
        String html = "<html<body><p>Hello World</p></body></html>";
        Parser.parseBodyFragment(html, "http://localhost");
    }

    // Test unescapeEntities static method with valid entities
    @Test
    public void testUnescapeEntitiesValidEntities() {
        String html = "&lt;p&gt;Hello &amp; World&lt;/p&gt;";
        String unescapedHtml = Parser.unescapeEntities(html, false);
        assertEquals("<p>Hello & World</p>", unescapedHtml);
    }

    // Test unescapeEntities static method with invalid entities
    @Test
    public void testUnescapeEntitiesInvalidEntities() {
        String html = "&lt;p&gt;Hello &amp;amp; World&lt;/p&gt;";
        String unescapedHtml = Parser.unescapeEntities(html, false);
        assertEquals("<p>Hello &amp; World</p>", unescapedHtml);
    }

    // Test unescapeEntities static method with attributes
    @Test
    public void testUnescapeEntitiesWithAttributes() {
        String html = "<a href=\"&lt;script&gt;alert(&apos;Hello&apos;)&lt;/script&gt;\" target=\"_blank\">Link</a>";
        String unescapedHtml = Parser.unescapeEntities(html, true);
        assertEquals("<a href=\"<script>alert('Hello');</script>\">Link</a>", unescapedHtml);
    }
}

