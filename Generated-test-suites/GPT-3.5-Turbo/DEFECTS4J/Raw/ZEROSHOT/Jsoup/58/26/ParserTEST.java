import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParseInput() {
        Parser parser = new Parser(new Parser.HtmlTreeBuilder());
        Document document = parser.parseInput("<html><body><h1>Hello, World!</h1></body></html>", "");
        assertEquals("Hello, World!", document.select("h1").text());
    }

    @Test
    public void testGetTreeBuilder() {
        Parser parser = new Parser(new Parser.HtmlTreeBuilder());
        assertEquals(parser.getTreeBuilder().getClass(), Parser.HtmlTreeBuilder.class);
    }

    @Test
    public void testSetTreeBuilder() {
        Parser parser = new Parser(new Parser.XmlTreeBuilder());
        parser.setTreeBuilder(new Parser.HtmlTreeBuilder());
        assertEquals(parser.getTreeBuilder().getClass(), Parser.HtmlTreeBuilder.class);
    }

    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new Parser.HtmlTreeBuilder());
        assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        Parser parser = new Parser(new Parser.HtmlTreeBuilder());
        parser.setTrackErrors(10);
        assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testGetErrors() {
        Parser parser = new Parser(new Parser.HtmlTreeBuilder());
        List<Parser.ParseError> errors = parser.getErrors();
        assertNotNull(errors);
        assertEquals(errors.size(), 0);
    }

    @Test
    public void testSettings() {
        Parser parser = new Parser(new Parser.HtmlTreeBuilder());
        Parser.ParseSettings settings = new Parser.ParseSettings(false);
        parser.settings(settings);
        assertEquals(parser.settings(), settings);
    }

    @Test
    public void testParse() {
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        String baseUri = "";
        Document document = Parser.parse(html, baseUri);
        assertEquals("Hello, World!", document.select("h1").text());
    }

    @Test
    public void testParseFragment() {
        String fragmentHtml = "<div><p>Hello, World!</p></div>";
        Element context = new Element("div");
        String baseUri = "";
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
        assertEquals(nodes.size(), 1);
        assertEquals(nodes.get(0).nodeName(), "div");
        assertEquals(nodes.get(0).childNode(0).nodeName(), "p");
        assertEquals(nodes.get(0).childNode(0).childNode(0).toString(), "Hello, World!");
    }

    @Test
    public void testParseXmlFragment() {
        String fragmentXml = "<root><a>Hello</a><b>World</b></root>";
        String baseUri = "";
        List<Node> nodes = Parser.parseXmlFragment(fragmentXml, baseUri);
        assertEquals(nodes.size(), 1);
        assertEquals(nodes.get(0).nodeName(), "root");
        assertEquals(nodes.get(0).childNode(0).nodeName(), "a");
        assertEquals(nodes.get(0).childNode(1).nodeName(), "b");
        assertEquals(nodes.get(0).childNode(0).childNode(0).toString(), "Hello");
        assertEquals(nodes.get(0).childNode(1).childNode(0).toString(), "World");
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello, World!</p>";
        String baseUri = "";
        Document document = Parser.parseBodyFragment(bodyHtml, baseUri);
        assertEquals(document.body().childNodeSize(), 1);
        assertEquals(document.body().childNode(0).nodeName(), "p");
        assertEquals(document.body().childNode(0).childNode(0).toString(), "Hello, World!");
    }

    @Test
    public void testUnescapeEntities() {
        String escapedString = "&lt;div&gt;Hello, &amp;World!&lt;/div&gt;";
        String unescapedString = Parser.unescapeEntities(escapedString, true);
        assertEquals(unescapedString, "<div>Hello, &World!</div>");
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<p>Hello, World!</p>";
        String baseUri = "";
        Document document = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        assertEquals(document.body().childNodeSize(), 1);
        assertEquals(document.body().childNode(0).nodeName(), "p");
        assertEquals(document.body().childNode(0).childNode(0).toString(), "Hello, World!");
    }

    @Test
    public void testHtmlParser() {
        Parser parser = Parser.htmlParser();
        assertEquals(parser.getTreeBuilder().getClass(), Parser.HtmlTreeBuilder.class);
    }

    @Test
    public void testXmlParser() {
        Parser parser = Parser.xmlParser();
        assertEquals(parser.getTreeBuilder().getClass(), Parser.XmlTreeBuilder.class);
    }
}