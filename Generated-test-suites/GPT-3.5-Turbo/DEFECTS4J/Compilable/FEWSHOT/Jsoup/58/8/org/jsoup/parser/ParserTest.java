
package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.junit.Before;
import org.junit.Test;

public class ParserTest {
    private Parser parser;

    @Before
    public void setup() {
        parser = new Parser(new HtmlTreeBuilder());
    }

    @Test
    public void testConstructorAndGetTreeBuilder() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);
        assertEquals(treeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testSetTreeBuilder() {
        TreeBuilder treeBuilder1 = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder1);
        
        TreeBuilder treeBuilder2 = new XmlTreeBuilder();
        parser.setTreeBuilder(treeBuilder2);
        
        assertEquals(treeBuilder2, parser.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors() {
        assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        parser.setTrackErrors(3);
        assertEquals(3, parser.isTrackErrors());
    }

    @Test
    public void testParseInput() {
        String html = "<html><body><h1>Hello World!</h1></body></html>";
        String baseUri = "https://www.example.com";
        
        Document document = parser.parseInput(html, baseUri);
        assertNotNull(document);
        assertEquals("https://www.example.com", document.baseUri());

        Element body = document.body();
        assertNotNull(body);

        List<Element> h1Elements = body.getElementsByTag("h1");
        assertEquals(1, h1Elements.size());
        Element h1 = h1Elements.get(0);
        assertEquals("Hello World!", h1.text());
    }

    @Test
    public void testGetSettingsAndSettings() {
        ParseSettings settings = new ParseSettings(true, true);
        parser.settings(settings);
        assertEquals(settings, parser.settings());
    }

    @Test
    public void testParse() {
        String html = "<html><body><h1>Hello World!</h1></body></html>";
        String baseUri = "https://www.example.com";
        
        Document document = Parser.parse(html, baseUri);
        assertNotNull(document);
        assertEquals("https://www.example.com", document.baseUri());

        Element body = document.body();
        assertNotNull(body);

        List<Element> h1Elements = body.getElementsByTag("h1");
        assertEquals(1, h1Elements.size());
        Element h1 = h1Elements.get(0);
        assertEquals("Hello World!", h1.text());
    }

    @Test
    public void testParseFragment() {
        String fragmentHtml = "<p>This is a paragraph.</p>";
        Element context = new Element("div");
        String baseUri = "https://www.example.com";
        
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
        assertEquals(1, nodes.size());
        Node node = nodes.get(0);
        assertEquals("<p>This is a paragraph.</p>", node.outerHtml());
    }

    @Test
    public void testParseXmlFragment() {
        String fragmentXml = "<p>This is a paragraph.</p>";
        String baseUri = "https://www.example.com";
        
        List<Node> nodes = Parser.parseXmlFragment(fragmentXml, baseUri);
        assertEquals(1, nodes.size());
        Node node = nodes.get(0);
        assertEquals("<p>This is a paragraph.</p>", node.outerHtml());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>This is a paragraph.</p>";
        String baseUri = "https://www.example.com";
        
        Document document = Parser.parseBodyFragment(bodyHtml, baseUri);
        assertNotNull(document);
        assertEquals("https://www.example.com", document.baseUri());

        Element body = document.body();
        assertNotNull(body);

        List<Element> pElements = body.getElementsByTag("p");
        assertEquals(1, pElements.size());
        Element p = pElements.get(0);
        assertEquals("This is a paragraph.", p.text());
    }

    @Test
    public void testUnescapeEntities() {
        String escapedString = "&lt;p&gt;This is a paragraph.&lt;/p&gt;";
        String unescapedString = Parser.unescapeEntities(escapedString, false);
        assertEquals("<p>This is a paragraph.</p>", unescapedString);
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<p>This is a paragraph.</p>";
        String baseUri = "https://www.example.com";
        
        Document document = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        assertNotNull(document);
        assertEquals("https://www.example.com", document.baseUri());

        Element body = document.body();
        assertNotNull(body);

        List<Element> pElements = body.getElementsByTag("p");
        assertEquals(1, pElements.size());
        Element p = pElements.get(0);
        assertEquals("This is a paragraph.", p.text());
    }

    @Test
    public void testHtmlParser() {
        Parser parser = Parser.htmlParser();
        assertNotNull(parser);
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        assertNotNull(treeBuilder);
        assertEquals(HtmlTreeBuilder.class, treeBuilder.getClass());
    }

    @Test
    public void testXmlParser() {
        Parser parser = Parser.xmlParser();
        assertNotNull(parser);
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        assertNotNull(treeBuilder);
        assertEquals(XmlTreeBuilder.class, treeBuilder.getClass());
    }
}
