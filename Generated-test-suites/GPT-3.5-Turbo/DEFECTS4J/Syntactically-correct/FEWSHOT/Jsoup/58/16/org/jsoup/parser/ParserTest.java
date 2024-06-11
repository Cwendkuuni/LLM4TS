
package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.ParseError;
import org.junit.Test;

import java.util.List;

public class ParserTest {

    @Test
    public void testParseInput() {
        Parser parser = new Parser(new TreeBuilder());

        // Test input with no errors
        Document document = parser.parseInput("<html><body><p>Hello World</p></body></html>", "");
        Element body = document.body();
        assertEquals("Hello World", body.select("p").first().text());

        // Test input with errors
        parser.setTrackErrors(5);
        document = parser.parseInput("<html><body><p>Hello World</body></html>", "");
        List<ParseError> errors = parser.getErrors();
        assertEquals(1, errors.size());
        assertEquals(1, errors.get(0).getLineNumber());
        assertEquals(26, errors.get(0).getColumnNumber());
        assertEquals("Opening and ending tag mismatch: p line 1 and body", errors.get(0).getErrorMessage());

        // Test input with base URI
        document = parser.parseInput("<html><body><img src=\"image.png\"></body></html>", "http://www.example.com");
        Element img = document.select("img").first();
        assertEquals("http://www.example.com/image.png", img.attr("src"));
    }

    @Test
    public void testGetTreeBuilder() {
        TreeBuilder treeBuilder = new TreeBuilder();
        Parser parser = new Parser(treeBuilder);
        assertEquals(treeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testSetTreeBuilder() {
        TreeBuilder treeBuilder1 = new TreeBuilder();
        TreeBuilder treeBuilder2 = new TreeBuilder();
        Parser parser = new Parser(treeBuilder1);
        parser.setTreeBuilder(treeBuilder2);
        assertEquals(treeBuilder2, parser.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new TreeBuilder());
        assertFalse(parser.isTrackErrors());

        parser.setTrackErrors(5);
        assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        Parser parser = new Parser(new TreeBuilder());

        parser.setTrackErrors(5);
        assertTrue(parser.isTrackErrors());
        assertEquals(5, parser.getErrors().size());

        parser.setTrackErrors(0);
        assertFalse(parser.isTrackErrors());
        assertNull(parser.getErrors());
    }

    @Test
    public void testGetErrors() {
        Parser parser = new Parser(new TreeBuilder());
        assertNull(parser.getErrors());

        parser.setTrackErrors(5);
        List<ParseError> errors = parser.getErrors();
        assertEquals(0, errors.size());

        parser.parseInput("<html><body><p>Hello World</body></html>", "");
        errors = parser.getErrors();
        assertEquals(1, errors.size());
    }

    @Test
    public void testSettings() {
        Parser parser = new Parser(new TreeBuilder());
        ParseSettings settings = parser.settings();
        assertNull(settings);

        settings = new ParseSettings(false);
        parser.settings(settings);
        assertEquals(settings, parser.settings());
    }

    @Test
    public void testParse() {
        Document document = Parser.parse("<html><body><p>Hello World</p></body></html>", "");
        Element body = document.body();
        assertEquals("Hello World", body.select("p").first().text());
    }

    @Test
    public void testParseFragment() {
        Element context = new Element("div");
        List<Node> nodes = Parser.parseFragment("<p>Hello World</p>", context, "");
        assertEquals(1, nodes.size());
        assertEquals("Hello World", nodes.get(0).toString());
    }

    @Test
    public void testParseXmlFragment() {
        List<Node> nodes = Parser.parseXmlFragment("<p>Hello World</p>", "");
        assertEquals(1, nodes.size());
        assertEquals("Hello World", nodes.get(0).toString());
    }

    @Test
    public void testParseBodyFragment() {
        Document document = Parser.parseBodyFragment("<p>Hello World</p>", "");
        Element body = document.body();
        assertEquals("Hello World", body.select("p").first().text());
    }

    @Test
    public void testUnescapeEntities() {
        String unescaped = Parser.unescapeEntities("&lt;p&gt;Hello&nbsp;World&lt;/p&gt;", false);
        assertEquals("<p>Hello World</p>", unescaped);
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        Document document = Parser.parseBodyFragmentRelaxed("<p>Hello World</p>", "");
        Element body = document.body();
        assertEquals("Hello World", body.select("p").first().text());
    }

    @Test
    public void testHtmlParser() {
        Parser parser = Parser.htmlParser();
        assertTrue(parser.getTreeBuilder() instanceof HtmlTreeBuilder);
    }

    @Test
    public void testXmlParser() {
        Parser parser = Parser.xmlParser();
        assertTrue(parser.getTreeBuilder() instanceof XmlTreeBuilder);
    }
}
