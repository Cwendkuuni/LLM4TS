import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.HtmlTreeBuilder;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ParserTest {

    private Parser parser;

    @Before
    public void setUp() {
        parser = new Parser(new HtmlTreeBuilder());
    }

    @Test
    public void testParseInput() {
        String html = "<div>Test</div>";
        String baseUri = "http://example.com";
        Document doc = parser.parseInput(html, baseUri);
        assertNotNull(doc);
    }

    @Test
    public void testGetTreeBuilder() {
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        assertNotNull(treeBuilder);
    }

    @Test
    public void testSetTreeBuilder() {
        TreeBuilder newTreeBuilder = new XmlTreeBuilder();
        parser.setTreeBuilder(newTreeBuilder);
        assertEquals(newTreeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors() {
        assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        int maxErrors = 5;
        parser.setTrackErrors(maxErrors);
        assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testGetErrors() {
        List<ParseError> errors = parser.getErrors();
        assertNotNull(errors);
    }

    @Test
    public void testParse() {
        String html = "<div>Test</div>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        assertNotNull(doc);
    }

    @Test
    public void testParseFragment() {
        String html = "<div>Test</div>";
        Element context = new Element("div");
        String baseUri = "http://example.com";
        List<Node> nodes = Parser.parseFragment(html, context, baseUri);
        assertNotNull(nodes);
    }

    @Test
    public void testParseBodyFragment() {
        String html = "<div>Test</div>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragment(html, baseUri);
        assertNotNull(doc);
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String html = "<div>Test</div>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragmentRelaxed(html, baseUri);
        assertNotNull(doc);
    }

    @Test
    public void testHtmlParser() {
        Parser htmlParser = Parser.htmlParser();
        assertNotNull(htmlParser);
    }

    @Test
    public void testXmlParser() {
        Parser xmlParser = Parser.xmlParser();
        assertNotNull(xmlParser);
    }
}