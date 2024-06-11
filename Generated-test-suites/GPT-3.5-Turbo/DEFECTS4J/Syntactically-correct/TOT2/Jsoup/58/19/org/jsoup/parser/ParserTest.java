import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

public class ParserTest {
    private Parser parser;

    @Before
    public void setup() {
        parser = new Parser(new HtmlTreeBuilder());
    }

    @Test
    public void testParseInput() {
        Document doc = parser.parseInput("<html><body><p>Hello, World!</p></body></html>", "http://example.com");
        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testGetTreeBuilder() {
        assertEquals(HtmlTreeBuilder.class, parser.getTreeBuilder().getClass());
    }

    @Test
    public void testSetTrackErrors() {
        parser.setTrackErrors(10);
        assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testGetErrors() {
        parser.setTrackErrors(5);
        List<ParseError> errors = parser.getErrors();
        assertNotNull(errors);
    }

    @Test
    public void testSettings() {
        ParseSettings settings = new ParseSettings(false);
        parser.settings(settings);
        assertEquals(settings, parser.settings());
    }

    @Test
    public void testParse() {
        Document doc = Parser.parse("<html><body><p>Hello, World!</p></body></html>", "http://example.com");
        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testParseFragment() {
        List<Node> nodes = Parser.parseFragment("<p>Hello, World!</p>", new Element("div"), "http://example.com");
        assertEquals(1, nodes.size());
    }

    @Test
    public void testParseBodyFragment() {
        Document doc = Parser.parseBodyFragment("<p>Hello</p><div>World</div>", "http://example.com");
        assertNotNull(doc.body());
    }

    @Test
    public void testUnescapeEntities() {
        String unescaped = Parser.unescapeEntities("&lt;p&gt;Hello&lt;/p&gt;", true);
        assertEquals("<p>Hello</p>", unescaped);
    }

    @Test
    public void testHtmlParser() {
        Parser htmlParser = Parser.htmlParser();
        assertEquals(HtmlTreeBuilder.class, htmlParser.getTreeBuilder().getClass());
    }

    @Test
    public void testXmlParser() {
        Parser xmlParser = Parser.xmlParser();
        assertEquals(XmlTreeBuilder.class, xmlParser.getTreeBuilder().getClass());
    }
}