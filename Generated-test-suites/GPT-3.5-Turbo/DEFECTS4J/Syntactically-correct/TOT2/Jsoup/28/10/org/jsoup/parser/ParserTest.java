import org.junit.Test;

import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParseInput() {
        // Typical use case
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput("<html></html>", "http://example.com");
        assertNotNull(doc);

        // Test edge case - empty input
        doc = parser.parseInput("", "http://example.com");
        assertNotNull(doc);

        // Test error scenario - invalid HTML
        doc = parser.parseInput("<html><div></html>", "http://example.com");
        assertNull(doc);
    }

    @Test
    public void testGetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        assertNotNull(treeBuilder);
    }

    @Test
    public void testSetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder newTreeBuilder = new XmlTreeBuilder();
        parser.setTreeBuilder(newTreeBuilder);
        assertEquals(newTreeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        assertFalse(parser.isTrackErrors());

        parser.setTrackErrors(5);
        assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(3);
        assertTrue(parser.isTrackErrors());

        parser.setTrackErrors(0);
        assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testGetErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        List<ParseError> errors = parser.getErrors();
        assertNotNull(errors);
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testParse() {
        // Typical use case
        Document doc = Parser.parse("<html></html>", "http://example.com");
        assertNotNull(doc);

        // Test edge case - empty input
        doc = Parser.parse("", "http://example.com");
        assertNotNull(doc);
    }

    @Test
    public void testParseFragment() {
        Element context = new Element("div");
        List<Node> nodeList = Parser.parseFragment("<p></p>", context, "http://example.com");
        assertNotNull(nodeList);
        assertFalse(nodeList.isEmpty());
    }

    @Test
    public void testParseBodyFragment() {
        Document doc = Parser.parseBodyFragment("<div></div>", "http://example.com");
        assertNotNull(doc);
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        Document doc = Parser.parseBodyFragmentRelaxed("<html></html>", "http://example.com");
        assertNotNull(doc);
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