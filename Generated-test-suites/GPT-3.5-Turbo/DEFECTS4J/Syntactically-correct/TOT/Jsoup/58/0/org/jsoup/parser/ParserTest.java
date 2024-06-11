import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ParserTest {

    private Parser parser;

    @Before
    public void setup() {
        parser = new Parser(new HtmlTreeBuilder());
    }

    @Test
    public void testParseInput_typicalCase() {
        // typical case: valid html input
        String html = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        String baseUri = "https://www.example.com";
        Document result = parser.parseInput(html, baseUri);

        assertNotNull(result);
        assertEquals("https://www.example.com", result.baseUri());
        assertEquals("Test", result.title());
        assertEquals("Hello World!", result.body().select("h1").text());
    }

    @Test
    public void testParseInput_emptyHtml() {
        // edge case: empty html input
        String html = "";
        String baseUri = "https://www.example.com";
        Document result = parser.parseInput(html, baseUri);

        assertNotNull(result);
        assertEquals("https://www.example.com", result.baseUri());
        assertTrue(result.title().isEmpty());
        assertTrue(result.body().children().isEmpty());
    }

    @Test
    public void testParseInput_invalidHtml() {
        // error scenario: invalid html input
        String html = "<html><head><title>Test</head><body><h1>Hello World!</h1></body></html>"; // missing closing </title> tag
        String baseUri = "https://www.example.com";
        Document result = parser.parseInput(html, baseUri);

        assertNotNull(result);
        assertEquals("https://www.example.com", result.baseUri());
        assertEquals("Test", result.title());
        assertEquals("Hello World!", result.body().select("h1").text());
        assertEquals(1, parser.getErrors().size());
        assertEquals("Error parsing HTML: Unexpectedly reached EOF", parser.getErrors().get(0).toString());
    }

    @Test
    public void testGetTreeBuilder() {
        // typical case: get the TreeBuilder
        TreeBuilder treeBuilder = parser.getTreeBuilder();

        assertNotNull(treeBuilder);
        assertEquals(HtmlTreeBuilder.class, treeBuilder.getClass());
    }

    @Test
    public void testSetTreeBuilder() {
        // typical case: set the TreeBuilder
        TreeBuilder newTreeBuilder = new XmlTreeBuilder();
        parser.setTreeBuilder(newTreeBuilder);

        assertEquals(newTreeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors() {
        // typical case: check if error tracking is enabled
        boolean result = parser.isTrackErrors();

        assertFalse(result);
    }

    @Test
    public void testSetTrackErrors() {
        // typical case: enable error tracking
        parser.setTrackErrors(10);

        assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testGetErrors() {
        // error scenario: no errors tracked
        List<ParseError> errors = parser.getErrors();

        assertNotNull(errors);
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testSettings() {
        // typical case: set custom parse settings
        ParseSettings settings = new ParseSettings(false, true);
        parser.settings(settings);

        assertEquals(settings, parser.settings());
    }

    @Test
    public void testParse() {
        // typical case: parse HTML into Document
        String html = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        String baseUri = "https://www.example.com";
        Document result = Parser.parse(html, baseUri);

        assertNotNull(result);
        assertEquals("https://www.example.com", result.baseUri());
        assertEquals("Test", result.title());
        assertEquals("Hello World!", result.body().select("h1").text());
    }

    @Test
    public void testParseFragment_typicalCase() {
        // typical case: parse fragment of HTML into list of nodes
        String fragmentHtml = "<div><h2>Fragment</h2></div>";
        Element context = new Element(Tag.valueOf("div"), "");
        String baseUri = "https://www.example.com";
        List<Node> result = Parser.parseFragment(fragmentHtml, context, baseUri);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0) instanceof Element);
        assertEquals("h2", ((Element) result.get(0)).tagName());
        assertEquals("Fragment", ((Element) result.get(0)).text());
    }

    @Test
    public void testParseXmlFragment_typicalCase() {
        // typical case: parse fragment of XML into list of nodes
        String fragmentXml = "<root><node>Text</node></root>";
        String baseUri = "https://www.example.com";
        List<Node> result = Parser.parseXmlFragment(fragmentXml, baseUri);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0) instanceof Element);
        assertEquals("node", ((Element) result.get(0)).tagName());
        assertEquals("Text", ((Element) result.get(0)).text());
    }

    @Test
    public void testParseBodyFragment_typicalCase() {
        // typical case: parse fragment of HTML into body of Document
        String bodyHtml = "<h1>Body Fragment</h1>";
        String baseUri = "https://www.example.com";
        Document result = Parser.parseBodyFragment(bodyHtml, baseUri);

        assertNotNull(result);
        assertEquals("https://www.example.com", result.baseUri());
        assertEquals("", result.title());
        assertEquals("Body Fragment", result.body().select("h1").text());
    }

    @Test
    public void testUnescapeEntities_typicalCase() {
        // typical case: unescape HTML entities in a string
        String escapedString = "&lt;div&gt;Hello World! &amp; &pound;</div>";
        String result = Parser.unescapeEntities(escapedString, true);

        assertEquals("<div>Hello World! & £</div>", result);
    }

    @Test
    public void testParseBodyFragmentRelaxed_typicalCase() {
        // typical case: deprecated method, should call parse method
        String bodyHtml = "<h1>Body Fragment</h1>";
        String baseUri = "https://www.example.com";
        Document result = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);

        assertNotNull(result);
        assertEquals("https://www.example.com", result.baseUri());
        assertEquals("", result.title());
        assertEquals("Body Fragment", result.body().select("h1").text());
    }

    @Test
    public void testHtmlParser() {
        // typical case: create new HTML parser
        Parser htmlParser = Parser.htmlParser();

        assertNotNull(htmlParser);
        assertNotNull(htmlParser.getTreeBuilder());
        assertEquals(HtmlTreeBuilder.class, htmlParser.getTreeBuilder().getClass());
    }

    @Test
    public void testXmlParser() {
        // typical case: create new XML parser
        Parser xmlParser = Parser.xmlParser();

        assertNotNull(xmlParser);
        assertNotNull(xmlParser.getTreeBuilder());
        assertEquals(XmlTreeBuilder.class, xmlParser.getTreeBuilder().getClass());
    }
}