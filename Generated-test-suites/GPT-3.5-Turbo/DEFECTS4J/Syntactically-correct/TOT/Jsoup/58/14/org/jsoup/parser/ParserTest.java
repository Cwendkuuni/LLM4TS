import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.ParseSettings;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ParserTest {

    @Test
    public void testParseInput() {
        String html = "<html><head></head><body><p>This is a test.</p></body></html>";
        String baseUri = "http://www.example.com";
        Parser parser = new Parser(new HtmlTreeBuilder());

        Document document = parser.parseInput(html, baseUri);

        assertNotNull(document);
        assertEquals("<html>\n <head></head>\n <body>\n  <p>This is a test.</p>\n </body>\n</html>", document.toString());
        assertEquals("http://www.example.com", document.baseUri());
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

        TreeBuilder newTreeBuilder = new XmlTreeBuilder();
        parser.setTreeBuilder(newTreeBuilder);

        assertEquals(newTreeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());

        assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());

        parser.setTrackErrors(5);

        assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testGetErrors() {
        String html = "<invalid tag>";
        String baseUri = "http://www.example.com";
        Parser parser = new Parser(new HtmlTreeBuilder());

        Document document = parser.parseInput(html, baseUri);
        List<ParseError> errors = parser.getErrors();

        assertNotNull(errors);
        assertEquals(1, errors.size());
        assertEquals("Unexpected char '<' at 0", errors.get(0).toString());
    }

    @Test
    public void testSettings() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        ParseSettings settings = new ParseSettings(false, false);

        parser.settings(settings);

        assertEquals(settings, parser.settings());
    }

    @Test
    public void testParse() {
        String html = "<html><head></head><body><p>This is a test.</p></body></html>";
        String baseUri = "http://www.example.com";

        Document document = Parser.parse(html, baseUri);

        assertNotNull(document);
        assertEquals("<html>\n <head></head>\n <body>\n  <p>This is a test.</p>\n </body>\n</html>", document.toString());
        assertEquals("http://www.example.com", document.baseUri());
    }

    @Test
    public void testParseFragment() {
        String fragmentHtml = "<p>This is a test.</p>";
        Element context = new Element("div");
        String baseUri = "http://www.example.com";

        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);

        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertEquals("<p>This is a test.</p>", nodes.get(0).outerHtml());
        assertEquals("http://www.example.com", context.baseUri());
    }

    @Test
    public void testParseXmlFragment() {
        String fragmentXml = "<tag>This is a test</tag>";
        String baseUri = "http://www.example.com";

        List<Node> nodes = Parser.parseXmlFragment(fragmentXml, baseUri);

        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertEquals("<tag>This is a test</tag>", nodes.get(0).outerHtml());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>This is a test.</p>";
        String baseUri = "http://www.example.com";

        Document document = Parser.parseBodyFragment(bodyHtml, baseUri);

        assertNotNull(document);
        assertEquals("<html>\n <head></head>\n <body>\n  <p>This is a test.</p>\n </body>\n</html>", document.toString());
        assertEquals("http://www.example.com", document.baseUri());
    }

    @Test
    public void testUnescapeEntities() {
        String string = "This is an &lt;example&gt; string.";
        boolean inAttribute = false;

        String unescapedString = Parser.unescapeEntities(string, inAttribute);

        assertEquals("This is an <example> string.", unescapedString);
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<html><head></head><body><p>This is a test.</p></body></html>";
        String baseUri = "http://www.example.com";

        Document document = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);

        assertNotNull(document);
        assertEquals("<html>\n <head></head>\n <body>\n  <p>This is a test.</p>\n </body>\n</html>", document.toString());
        assertEquals("http://www.example.com", document.baseUri());
    }

    @Test
    public void testHtmlParser() {
        Parser parser = Parser.htmlParser();
        assertTrue(parser.getTreeBuilder() instanceof HtmlTreeBuilder);
    }

    @Test
    public void testXmlParser() {
        Parser parser = Parser.xmlParser();
        assertTrue(parser.getTreeBuilder() instanceof XmlTreeBuilder);
    }

}