
package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class ParserTest {

    private Parser parser;

    @Before
    public void setup() {
        parser = Parser.htmlParser();
    }

    @Test
    public void testParseInput() {
        Document doc = parser.parseInput("<html><body><p>Hello, world!</p></body></html>", "");
        assertNotNull(doc);
        Element body = doc.body();
        assertNotNull(body);
        assertEquals("Hello, world!", body.select("p").first().text());
    }

    @Test
    public void testGetTreeBuilder() {
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        assertNotNull(treeBuilder);
    }

    @Test
    public void testSetTreeBuilder() {
        TreeBuilder treeBuilder = new XmlTreeBuilder();
        parser.setTreeBuilder(treeBuilder);
        assertEquals(treeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors() {
        assertFalse(parser.isTrackErrors());
        parser.setTrackErrors(10);
        assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        parser.setTrackErrors(5);
        assertTrue(parser.isTrackErrors());
        parser.setTrackErrors(0);
        assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testGetErrors() {
        List<ParseError> errors = parser.getErrors();
        assertNotNull(errors);
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testSettings() {
        ParseSettings settings = new ParseSettings(false, false);
        parser.settings(settings);
        assertEquals(settings, parser.settings());
    }

    @Test
    public void testParse() {
        Document doc = Parser.parse("<html><body><p>Hello, world!</p></body></html>", "");
        assertNotNull(doc);
        Element body = doc.body();
        assertNotNull(body);
        assertEquals("Hello, world!", body.select("p").first().text());
    }

    @Test
    public void testParseFragment() {
        Element context = new Element("div");
        List<Node> nodes = Parser.parseFragment("<p>Hello, world!</p>", context, "");
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        Element p = (Element) nodes.get(0);
        assertEquals("p", p.tagName());
        assertEquals("Hello, world!", p.text());
        assertNull(p.parent());
    }

    @Test
    public void testParseXmlFragment() {
        List<Node> nodes = Parser.parseXmlFragment("<tag>Hello, world!</tag>", "");
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        Element tag = (Element) nodes.get(0);
        assertEquals("tag", tag.tagName());
        assertEquals("Hello, world!", tag.text());
        assertNull(tag.parent());
    }

    @Test
    public void testParseBodyFragment() {
        Document doc = Parser.parseBodyFragment("<p>Hello, world!</p>", "");
        assertNotNull(doc);
        Element body = doc.body();
        assertNotNull(body);
        assertEquals("Hello, world!", body.select("p").first().text());
    }

    @Test
    public void testUnescapeEntities() {
        String unescaped = Parser.unescapeEntities("&lt;p&gt;Hello, world!&lt;/p&gt;", false);
        assertEquals("<p>Hello, world!</p>", unescaped);
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        Document doc = Parser.parseBodyFragmentRelaxed("<p>Hello, world!</p>", "");
        assertNotNull(doc);
        Element body = doc.body();
        assertNotNull(body);
        assertEquals("Hello, world!", body.select("p").first().text());
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
