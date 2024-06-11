package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.XmlTreeBuilder;

import java.util.ArrayList;
import java.util.List;

public class ParserTest {

    @Test
    public void testParserInstance() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        assertNotNull(parser);
    }

    @Test
    public void testParseInput() {
        String html = "<html><body><p>Hello World</p></body></html>";
        Parser parser = new Parser(new HtmlTreeBuilder());

        Document doc = parser.parseInput(html, null);
        assertNotNull(doc);
        assertEquals("Hello World", doc.body().text());
    }

    @Test
    public void testParseInputWithBaseUri() {
        String html = "<img src='image.jpg' alt='Image'>";
        String baseUri = "http://example.com/";

        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput(html, baseUri);
        Element imgElement = doc.selectFirst("img");
        assertNotNull(imgElement);
        assertEquals("http://example.com/image.jpg", imgElement.attr("src"));
    }

    @Test
    public void testGetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        assertSame(treeBuilder, parser.treeBuilder);
    }

    @Test
    public void testSetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder newTreeBuilder = new HtmlTreeBuilder();
        parser.setTreeBuilder(newTreeBuilder);
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        assertSame(newTreeBuilder, treeBuilder);
    }

    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        assertFalse(parser.isTrackErrors());

        parser = new Parser(new HtmlTreeBuilder(), 5);
        assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(5);
        assertTrue(parser.isTrackErrors());

        parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(0);
        assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testGetErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder(), 3);
        List<ParseError> errors = parser.getErrors();
        assertTrue(errors.size() == 3);
    }

    @Test
    public void testParseStaticMethods() {
        String html = "<html><body><p>Hello World</p></body></html>";

        Document doc = Parser.parse(html, null);
        assertNotNull(doc);
        assertEquals("Hello World", doc.body().text());

        List<Node> nodes = Parser.parseFragment("<p>Test</p>", null, null);
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        Node node = nodes.get(0);
        assertEquals("Test", node.text());

        Document xmlDoc = Parser.parseXmlFragment("<xml><data>Test</data></xml>", null);
        assertNotNull(xmlDoc);
        Element dataElement = xmlDoc.selectFirst("data");
        assertNotNull(dataElement);
        assertEquals("Test", dataElement.text());

        Document bodyDoc = Parser.parseBodyFragment("<p>Test</p>", null);
        assertNotNull(bodyDoc);
        assertEquals("Test", bodyDoc.body().text());

        String unescaped = Parser.unescapeEntities("&lt;p&gt;Test&amp; &quot;Quoted&quot; &apos;Apostrophe&apos;", false);
        assertEquals("<p>Test Quoted Apostrophe</p>", unescaped);

        String unescapedAttribute = Parser.unescapeEntities("&lt;p data-test='Test&amp; &quot;Quoted&quot; &apos;Apostrophe&apos; &amp;#x2014;'&gt;", true);
        assertEquals("<p data-test='Test Quoted Quoted Apostrophe ---'>", unescapedAttribute);
    }

    @Test
    public void testParseRelaxedDeprecated() {
        String html = "<html><body><p>Hello World</p></body></html>";
        Document doc = Parser.parseBodyFragmentRelaxed(html, null);
        assertNotNull(doc);
        assertEquals("Hello World", doc.body().text());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testParseRelaxedDeprecatedException() {
        String html = "<p>Test</p>";
        Parser.parseBodyFragmentRelaxed(html, null);
    }
}