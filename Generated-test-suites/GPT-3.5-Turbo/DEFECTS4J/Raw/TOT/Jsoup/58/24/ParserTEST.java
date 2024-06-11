import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

public class ParserTest {

    private static final String BASE_URI = "http://example.com";
    private static final String HTML = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
    private static final String FRAGMENT_HTML = "<div><p>Paragraph 1</p><p>Paragraph 2</p></div>";
    private static final String FRAGMENT_XML = "<root><element1>Value 1</element1><element2>Value 2</element2></root>";
    private static final String BODY_HTML = "<p>Paragraph 1</p><p>Paragraph 2</p>";

    private Parser parser;

    @Before
    public void setUp() {
        parser = Parser.htmlParser();
    }

    @Test
    public void testParseInput() {
        Document document = parser.parseInput(HTML, BASE_URI);
        assertNotNull(document);
        assertEquals(HTML, document.html());
    }

    @Test
    public void testGetTreeBuilder() {
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        assertNotNull(treeBuilder);
    }

    @Test
    public void testSetTreeBuilder() {
        TreeBuilder treeBuilder = new XmlTreeBuilder();
        Parser newParser = parser.setTreeBuilder(treeBuilder);
        assertNotNull(newParser);
        assertEquals(treeBuilder, newParser.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors() {
        assertFalse(parser.isTrackErrors());
        Parser newParser = parser.setTrackErrors(1);
        assertTrue(newParser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        Parser newParser = parser.setTrackErrors(1);
        assertTrue(newParser.isTrackErrors());
    }

    @Test
    public void testGetErrors() {
        List<ParseError> errors = parser.getErrors();
        assertNotNull(errors);
        assertEquals(0, errors.size());
    }

    @Test
    public void testSettings() {
        // Test default settings
        ParseSettings defaultSettings = parser.settings();
        assertNotNull(defaultSettings);

        // Test custom settings
        ParseSettings customSettings = new ParseSettings(true, true);
        parser.settings(customSettings);
        ParseSettings updatedSettings = parser.settings();
        assertEquals(customSettings.isXmlMode(), updatedSettings.isXmlMode());
        assertEquals(customSettings.isTrackErrors(), updatedSettings.isTrackErrors());
    }

    @Test
    public void testParse() {
        Document document = Parser.parse(HTML, BASE_URI);
        assertNotNull(document);
        assertEquals(HTML, document.html());
    }

    @Test
    public void testParseFragmentWithContext() {
        Element context = new Element(Tag.valueOf("div"), BASE_URI);
        List<Node> nodes = Parser.parseFragment(FRAGMENT_HTML, context, BASE_URI);
        assertNotNull(nodes);
        assertEquals(2, nodes.size());
        assertEquals("Paragraph 1", nodes.get(0).toString());
        assertEquals("Paragraph 2", nodes.get(1).toString());
    }

    @Test
    public void testParseFragment() {
        List<Node> nodes = Parser.parseFragment(FRAGMENT_HTML, null, BASE_URI);
        assertNotNull(nodes);
        assertEquals(2, nodes.size());
        assertEquals("Paragraph 1", nodes.get(0).toString());
        assertEquals("Paragraph 2", nodes.get(1).toString());
    }

    @Test
    public void testParseXmlFragment() {
        List<Node> nodes = Parser.parseXmlFragment(FRAGMENT_XML, BASE_URI);
        assertNotNull(nodes);
        assertEquals(2, nodes.size());
        assertEquals("Value 1", nodes.get(0).toString());
        assertEquals("Value 2", nodes.get(1).toString());
    }

    @Test
    public void testParseBodyFragment() {
        Document document = Parser.parseBodyFragment(BODY_HTML, BASE_URI);
        assertNotNull(document);
        assertEquals(BODY_HTML, document.body().html());
    }

    @Test
    public void testUnescapeEntities() {
        String escaped = "&lt;p&gt;Paragraph&lt;/p&gt;";
        String unescaped = Parser.unescapeEntities(escaped, false);
        assertEquals("<p>Paragraph</p>", unescaped);
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        Document document = Parser.parseBodyFragmentRelaxed(BODY_HTML, BASE_URI);
        assertNotNull(document);
        assertEquals(BODY_HTML, document.html());
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