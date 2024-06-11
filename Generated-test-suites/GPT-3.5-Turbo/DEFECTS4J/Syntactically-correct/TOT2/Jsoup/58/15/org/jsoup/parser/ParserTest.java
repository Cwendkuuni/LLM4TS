import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class ParserTest {

    private Parser parser = new Parser(new HtmlTreeBuilder());

    @Test
    public void testParseInput() {
        String html = "<html><body><p>Hello, World!</p></body></html>";
        String baseUri = "http://www.example.com";
        Document document = parser.parseInput(html, baseUri);
        assertNotNull(document);
        assertEquals("Hello, World!", document.select("p").text());
    }

    @Test
    public void testGetTreeBuilder() {
        assertEquals(parser.getTreeBuilder(), new HtmlTreeBuilder());
    }

    @Test
    public void testSetTreeBuilder() {
        Parser newParser = parser.setTreeBuilder(new XmlTreeBuilder());
        assertEquals(newParser.getTreeBuilder(), new XmlTreeBuilder());
    }

    @Test
    public void testIsTrackErrors() {
        assertFalse(parser.isTrackErrors());
        parser.setTrackErrors(5);
        assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        parser.setTrackErrors(3);
        assertTrue(parser.isTrackErrors());
        assertEquals(3, parser.getErrors().size());
    }

    @Test
    public void testStaticParse() {
        String html = "<html><body><p>Hello, World!</p></body></html>";
        String baseUri = "http://www.example.com";
        Document document = Parser.parse(html, baseUri);
        assertNotNull(document);
        assertEquals("Hello, World!", document.select("p").text());
    }

    @Test
    public void testParseFragment() {
        String fragmentHtml = "<p>Hello, Fragment!</p>";
        Element context = new Element("div");
        String baseUri = "http://www.example.com";
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertEquals("Hello, Fragment!", nodes.get(0).toString());
    }

    @Test
    public void testParseXmlFragment() {
        String fragmentXml = "<p>Hello, XML!</p>";
        String baseUri = "http://www.example.com";
        List<Node> nodes = Parser.parseXmlFragment(fragmentXml, baseUri);
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertEquals("Hello, XML!", nodes.get(0).toString());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello, Body!</p>";
        String baseUri = "http://www.example.com";
        Document document = Parser.parseBodyFragment(bodyHtml, baseUri);
        assertNotNull(document);
        assertEquals("Hello, Body!", document.select("p").text());
    }

    @Test
    public void testUnescapeEntities() {
        String escapedString = "&lt;p&gt;Hello, World!&lt;/p&gt;";
        String unescapedString = Parser.unescapeEntities(escapedString, true);
        assertEquals("<p>Hello, World!</p>", unescapedString);
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<p>Hello, Body!</p>";
        String baseUri = "http://www.example.com";
        Document document = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        assertNotNull(document);
        assertEquals("Hello, Body!", document.select("p").text());
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