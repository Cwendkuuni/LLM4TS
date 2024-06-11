import org.junit.Test;

import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParseInput_typicalUseCase() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        String html = "<html><body><h1>Title</h1></body></html>";
        String baseUri = "https://example.com";
        Document document = parser.parseInput(html, baseUri);
        assertNotNull(document);
        assertEquals("<html>\n" +
                " <head></head>\n" +
                " <body>\n" +
                "  <h1>\n" +
                "   Title\n" +
                "  </h1>\n" +
                " </body>\n" +
                "</html>", document.html());
    }

    @Test
    public void testParseInput_edgeCase() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        String html = "<html></html>";
        String baseUri = "https://example.com";
        Document document = parser.parseInput(html, baseUri);
        assertNotNull(document);
        assertEquals("<html>\n" +
                " <head></head>\n" +
                " <body></body>\n" +
                "</html>", document.html());
    }

    @Test
    public void testParseInput_errorScenario() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        String html = "<html>";
        String baseUri = "https://example.com";
        Document document = parser.parseInput(html, baseUri);
        assertNull(document); // Expecting parse error
        assertEquals(1, parser.getErrors().size());
        assertEquals("Unexpectedly reached end of file (EOF)", parser.getErrors().get(0).getMessage());
    }

    @Test
    public void testGetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        assertNotNull(treeBuilder);
        assertTrue(treeBuilder instanceof HtmlTreeBuilder);
    }

    @Test
    public void testSetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder treeBuilder = new XmlTreeBuilder();
        parser.setTreeBuilder(treeBuilder);
        assertEquals(treeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        assertFalse(parser.isTrackErrors());
        parser.setTrackErrors(10);
        assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(5);
        assertTrue(parser.isTrackErrors());
        assertEquals(5, parser.getErrors().size()); // Expecting ParseErrors to be tracked
    }

    @Test
    public void testGetErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        List<ParseError> errors = parser.getErrors();
        assertNotNull(errors);
        assertEquals(0, errors.size());
    }

    @Test
    public void testSettings() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        ParseSettings settings = new ParseSettings(false, false);
        parser.settings(settings);
        assertEquals(settings, parser.settings());
    }

    @Test
    public void testParse_htmlFragment_typicalUseCase() {
        String html = "<div><p>Paragraph 1</p><p>Paragraph 2</p></div>";
        String baseUri = "https://example.com";
        List<Node> nodes = Parser.parseFragment(html, null, baseUri);
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        Element divElement = (Element) nodes.get(0);
        assertEquals(2, divElement.children().size());
        assertEquals("Paragraph 1", divElement.child(0).text());
        assertEquals("Paragraph 2", divElement.child(1).text());
    }

    @Test
    public void testParse_htmlFragment_errorScenario() {
        String html = "<div><p>Paragraph</div>";
        String baseUri = "https://example.com";
        List<Node> nodes = Parser.parseFragment(html, null, baseUri);
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        Element divElement = (Element) nodes.get(0);
        assertEquals(0, divElement.children().size());
        assertNotNull(parser.getErrors());
        assertEquals(1, parser.getErrors().size());
        assertEquals("Unexpectedly reached end of file (EOF)", parser.getErrors().get(0).getMessage());
    }

    @Test
    public void testParse_xmlFragment_typicalUseCase() {
        String xml = "<root><element>Value</element></root>";
        String baseUri = "https://example.com";
        List<Node> nodes = Parser.parseXmlFragment(xml, baseUri);
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        Element rootElement = (Element) nodes.get(0);
        assertEquals("element", rootElement.child(0).tagName());
        assertEquals("Value", rootElement.child(0).text());
    }

    @Test
    public void testParseBodyFragment_typicalUseCase() {
        String bodyHtml = "<p>Paragraph 1</p><p>Paragraph 2</p>";
        String baseUri = "https://example.com";
        Document document = Parser.parseBodyFragment(bodyHtml, baseUri);
        assertNotNull(document);
        assertEquals("<html>\n" +
                " <head></head>\n" +
                " <body>\n" +
                "  <p>\n" +
                "   Paragraph 1\n" +
                "  </p>\n" +
                "  <p>\n" +
                "   Paragraph 2\n" +
                "  </p>\n" +
                " </body>\n" +
                "</html>", document.html());
    }

    @Test
    public void testUnescapeEntities_typicalUseCase() {
        String escapedString = "&lt;div&gt;Some text&lt;/div&gt;";
        String unescapedString = Parser.unescapeEntities(escapedString, true);
        assertEquals("<div>Some text</div>", unescapedString);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnescapeEntities_errorScenario() {
        String escapedString = "InvalidString";
        Parser.unescapeEntities(escapedString, true);
    }

    @Test
    public void testParseBodyFragmentRelaxed_typicalUseCase() {
        String bodyHtml = "<p>Paragraph 1</p><p>Paragraph 2</p>";
        String baseUri = "https://example.com";
        Document document = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        assertNotNull(document);
        assertEquals("<html>\n" +
                " <head></head>\n" +
                " <body>\n" +
                "  <p>\n" +
                "   Paragraph 1\n" +
                "  </p>\n" +
                "  <p>\n" +
                "   Paragraph 2\n" +
                "  </p>\n" +
                " </body>\n" +
                "</html>", document.html());
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