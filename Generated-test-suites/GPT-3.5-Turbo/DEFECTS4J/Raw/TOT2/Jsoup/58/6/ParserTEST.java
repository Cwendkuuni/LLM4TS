import org.jsoup.nodes.Document;
import org.jsoup.parser.HtmlTreeBuilder;
import org.jsoup.parser.Parser;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.util.List;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParseInput() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput("<html><head></head><body>Hello World</body></html>", "http://example.com");
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testGetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        assertNotNull(parser.getTreeBuilder());
    }
    
    @Test
    public void testSetTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(5);
        assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testGetErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(5);
        assertEquals(0, parser.getErrors().size());
    }

    @Test
    public void testStaticParse() {
        Document doc = Parser.parse("<html><head></head><body>Hello World</body></html>", "http://example.com");
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testParseFragment() {
        Element context = new Element("div");
        List<Node> nodes = Parser.parseFragment("<p>Hello</p><p>World</p>", context, "http://example.com");
        assertNotNull(nodes);
        assertEquals(2, nodes.size());
    }

    @Test
    public void testParseXmlFragment() {
        List<Node> nodes = Parser.parseXmlFragment("<root><child>Node 1</child><child>Node 2</child></root>", "http://example.com");
        assertNotNull(nodes);
        assertEquals(3, nodes.size()); // 1 root element, 2 child elements
    }

    @Test
    public void testParseBodyFragment() {
        Document doc = Parser.parseBodyFragment("<p>Hello World</p>", "http://example.com");
        assertNotNull(doc);
        Elements bodyElements = doc.body().children();
        assertEquals(1, bodyElements.size());
        assertEquals("p", bodyElements.get(0).tagName());
    }

    @Test
    public void testUnescapeEntities() {
        String unescaped = Parser.unescapeEntities("&lt;p&gt;Hello World&lt;/p&gt;", false);
        assertEquals("<p>Hello World</p>", unescaped);
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        Document doc = Parser.parseBodyFragmentRelaxed("<p>Hello World</p>", "http://example.com");
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testHtmlParser() {
        Parser parser = Parser.htmlParser();
        assertNotNull(parser);
        assertEquals(HtmlTreeBuilder.class, parser.getTreeBuilder().getClass());
    }

    @Test
    public void testXmlParser() {
        Parser parser = Parser.xmlParser();
        assertNotNull(parser);
        assertEquals(XmlTreeBuilder.class, parser.getTreeBuilder().getClass());
    }
}