import org.jsoup.parser.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import java.util.List;

import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {
    private Parser parser;

    @Test
    public void testParseInput() {
        parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput("<html><body>Hello, World!</body></html>", "http://example.com");
        assertNotNull(doc);
        assertEquals("Hello, World!", doc.body().text());
    }

    @Test
    public void testGetTreeBuilder() {
        parser = new Parser(new HtmlTreeBuilder());
        assertEquals(HtmlTreeBuilder.class, parser.getTreeBuilder().getClass());
    }

    @Test
    public void testSetTrackErrors() {
        parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(5);
        assertTrue(parser.isTrackErrors());
        assertEquals(5, parser.getErrors().size());
    }

    @Test
    public void testParse() {
        Document doc = Parser.parse("<html><body>Hello, JUnit!</body></html>", "http://example.com");
        assertNotNull(doc);
        assertEquals("Hello, JUnit!", doc.body().text());
    }

    @Test
    public void testParseFragment() {
        List<Node> nodes = Parser.parseFragment("<div>Hello, JUnit!</div>", null, "http://example.com");
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
    }

    @Test
    public void testParseXmlFragment() {
        List<Node> nodes = Parser.parseXmlFragment("<root><child>Hello, JUnit!</child></root>", "http://example.com");
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
    }

    @Test
    public void testParseBodyFragment() {
        Document doc = Parser.parseBodyFragment("<div>Hello, JUnit!</div>", "http://example.com");
        assertNotNull(doc);
        assertEquals("Hello, JUnit!", doc.body().text());
    }

    @Test
    public void testUnescapeEntities() {
        String unescaped = Parser.unescapeEntities("&lt;div&gt;Hello, JUnit!&lt;/div&gt;", true);
        assertEquals("<div>Hello, JUnit!</div>", unescaped);
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        Document doc = Parser.parseBodyFragmentRelaxed("<html><body>Hello, JUnit!</body></html>", "http://example.com");
        assertNotNull(doc);
    }

    @Test
    public void testHtmlParser() {
        Parser htmlParser = Parser.htmlParser();
        assertNotNull(htmlParser);
        assertEquals(HtmlTreeBuilder.class, htmlParser.getTreeBuilder().getClass());
    }

    @Test
    public void testXmlParser() {
        Parser xmlParser = Parser.xmlParser();
        assertNotNull(xmlParser);
        assertEquals(XmlTreeBuilder.class, xmlParser.getTreeBuilder().getClass());
    }
}