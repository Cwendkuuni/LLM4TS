import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.HtmlTreeBuilder;
import org.jsoup.parser.XmlTreeBuilder;

import java.util.List;
import static org.junit.Assert.*;

public class ParserTest {

    private Parser parser;
    
    @Test
    public void testParseInput() {
        parser = new Parser(new HtmlTreeBuilder());
        Document document = parser.parseInput("<html><body><h1>Hello World</h1></body></html>", "http://example.com");
        assertNotNull(document);
    }

    @Test
    public void testGetTreeBuilder() {
        parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        assertNotNull(treeBuilder);
    }

    @Test
    public void testSetTreeBuilder() {
        parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        parser.setTreeBuilder(treeBuilder);
        assertEquals(treeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors() {
        parser = new Parser(new HtmlTreeBuilder());
        assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        parser = new Parser(new HtmlTreeBuilder());
        int maxErrors = 5;
        parser.setTrackErrors(maxErrors);
        assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testGetErrors() {
        parser = new Parser(new HtmlTreeBuilder());
        ParseErrorList errors = parser.getErrors();
        assertNotNull(errors);
    }

    @Test
    public void testSettings() {
        parser = new Parser(new HtmlTreeBuilder());
        ParseSettings settings = new ParseSettings(false, false);
        parser.settings(settings);
        assertEquals(settings, parser.settings());
    }

    @Test
    public void testParse() {
        Document document = Parser.parse("<html><body><h1>Hello World</h1></body></html>", "http://example.com");
        assertNotNull(document);
    }

    @Test
    public void testParseFragment() {
        List<Node> nodeList = Parser.parseFragment("<h1>Hello World</h1>", null, "http://example.com");
        assertNotNull(nodeList);
    }

    @Test
    public void testParseXmlFragment() {
        List<Node> nodeList = Parser.parseXmlFragment("<p>Hello World</p>", "http://example.com");
        assertNotNull(nodeList);
    }

    @Test
    public void testParseBodyFragment() {
        Document document = Parser.parseBodyFragment("<h1>Hello World</h1>", "http://example.com");
        assertNotNull(document);
    }

    @Test
    public void testUnescapeEntities() {
        String unescapedString = Parser.unescapeEntities("&lt;h1&gt;Hello World&lt;/h1&gt;", true);
        assertEquals("<h1>Hello World</h1>", unescapedString);
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        Document document = Parser.parseBodyFragmentRelaxed("<h1>Hello World</h1>", "http://example.com");
        assertNotNull(document);
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