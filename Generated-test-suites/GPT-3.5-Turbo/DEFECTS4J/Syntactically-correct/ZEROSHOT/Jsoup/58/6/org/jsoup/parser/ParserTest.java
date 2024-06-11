import org.jsoup.parser.Parser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.ParseSettings;
import org.junit.Before;
import org.junit.Test;
import java.util.List;

import static org.junit.Assert.*;

public class ParserTest {
    private Parser parser;

    @Before
    public void setUp() {
        parser = Parser.htmlParser();
    }

    @Test
    public void testParseInput() {
        String html = "<html><head></head><body><h1>Title</h1><p>Paragraph</p></body></html>";
        String baseUri = "http://example.com";
        Document document = parser.parseInput(html, baseUri);
        assertTrue(document != null);
        assertEquals("Title", document.selectFirst("h1").text());
        assertEquals("Paragraph", document.selectFirst("p").text());
    }

    @Test
    public void testGetTreeBuilder() {
        assertTrue(parser.getTreeBuilder() != null);
        assertTrue(parser.getTreeBuilder() instanceof HtmlTreeBuilder);
    }

    @Test
    public void testSetTreeBuilder() {
        Parser newParser = parser.setTreeBuilder(new XmlTreeBuilder());
        assertTrue(newParser.getTreeBuilder() != null);
        assertTrue(newParser.getTreeBuilder() instanceof XmlTreeBuilder);
    }

    @Test
    public void testIsTrackErrors() {
        assertFalse(parser.isTrackErrors());
        parser.setTrackErrors(10);
        assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        Parser newParser = parser.setTrackErrors(5);
        assertTrue(newParser.isTrackErrors());
        assertEquals(5, newParser.getErrors().size());
    }

    @Test
    public void testGetErrors() {
        assertEquals(0, parser.getErrors().size());
        parser.setTrackErrors(3);
        assertEquals(3, parser.getErrors().size());
    }

    @Test
    public void testSettings() {
        ParseSettings settings = new ParseSettings(false, true);
        parser.settings(settings);
        assertEquals(settings, parser.settings());
    }

    @Test
    public void testParse() {
        String html = "<html><head></head><body><h1>Title</h1><p>Paragraph</p></body></html>";
        String baseUri = "http://example.com";
        Document document = Parser.parse(html, baseUri);
        assertTrue(document != null);
        assertEquals("Title", document.selectFirst("h1").text());
        assertEquals("Paragraph", document.selectFirst("p").text());
    }

    @Test
    public void testParseFragment() {
        String fragmentHtml = "<div><p>Paragraph</p></div>";
        Element context = new Element("div");
        String baseUri = "http://example.com";
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        assertEquals("p", ((Element) nodes.get(0)).tagName());
        assertEquals("Paragraph", ((Element) nodes.get(0)).text());
    }

    @Test
    public void testParseXmlFragment() {
        String fragmentXml = "<xml><tag>Value</tag></xml>";
        String baseUri = "http://example.com";
        List<Node> nodes = Parser.parseXmlFragment(fragmentXml, baseUri);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        assertEquals("tag", ((Element) nodes.get(0)).tagName());
        assertEquals("Value", ((Element) nodes.get(0)).text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Title</h1><p>Paragraph</p>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        assertTrue(doc != null);
        assertEquals("Title", doc.selectFirst("h1").text());
        assertEquals("Paragraph", doc.selectFirst("p").text());
    }

    @Test
    public void testUnescapeEntities() {
        String string = "&lt;div&gt;Content&lt;/div&gt;";
        String unescapedString = Parser.unescapeEntities(string, false);
        assertEquals("<div>Content</div>", unescapedString);
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<h1>Title</h1><p>Paragraph</p>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        assertTrue(doc != null);
        assertEquals("Title", doc.selectFirst("h1").text());
        assertEquals("Paragraph", doc.selectFirst("p").text());
    }

    @Test
    public void testHtmlParser() {
        Parser htmlParser = Parser.htmlParser();
        assertTrue(htmlParser.getTreeBuilder() instanceof HtmlTreeBuilder);
    }

    @Test
    public void testXmlParser() {
        Parser xmlParser = Parser.xmlParser();
        assertTrue(xmlParser.getTreeBuilder() instanceof XmlTreeBuilder);
    }
}