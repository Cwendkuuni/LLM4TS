package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
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

public class ParserTest {

    @Test
    public void testParser_construct() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        assertEquals(new HtmlTreeBuilder(), parser.getTreeBuilder());
    }

    @Test
    public void testParseInput_default() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput("<html><head></head><body><p>Hello World!</p></body></html>", null);
        assertEquals("<html><head></head><body><p>Hello World!</p></body></html>", doc.html());
    }

    @Test
    public void testParseInput_trackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput("<html><head></head><body><p>Hello <script>World!</script></p></body></html>", null);
        assertTrue(parser.isTrackErrors());
        List<ParseError> errors = parser.getErrors();
        assertSame(1, errors.size());
        assertEquals("Error at line 2 column 11: Unexpected tag 'script'", errors.get(0).getMessage());
        assertEquals("<html><head></head><body><p>Hello </p></body></html>", doc.html());
    }

    @Test
    public void testParseInput_noTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(0);
        Document doc = parser.parseInput("<html><head></head><body><p>Hello <script>World!</script></p></body></html>", null);
        assertNull(parser.getErrors());
        assertEquals("<html><head></head><body><p>Hello <script>World!</script></p></body></html>", doc.html());
    }

    @Test
    public void testParseInput_treeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput("<html><head></head><body><p>Hello World!</p></body></html>", null);
        TreeBuilder treeBuilder = new XmlTreeBuilder();
        Document docWithXMlTreeBuilder = parser.parseInput("<html><head></head><body><p>Hello World!</p></body></html>", null, treeBuilder);
        assertEquals("<html><head></head><body><p>Hello World!</p></body></html>", doc.html());
        assertEquals("<xml><html><head></head><body><p>Hello World!</p></body></html></xml>", docWithXMlTreeBuilder.html());
    }

    @Test
    public void testParseInput_baseUri() {
        String baseUri = "http://example.com";
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput("<html><head></head><body><img src='image.png'></body></html>", baseUri);
        String imgSrc = doc.select("img[src]").first().attr("src");
        assertEquals("http://example.com/image.png", imgSrc);
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<ul><li><a href='link1.html'>Link 1</a></li><li><a href='link2.html'>Link 2</a></li></ul>";
        Document doc = Parser.parseBodyFragment(bodyHtml, null);
        List<Node> list = doc.select("ul > li");
        assertEquals(2, list.size());
        List<Node> links = new ArrayList<>();
        for (Node node : list) {
            List<Node> children = node.children();
            assertSame(2, children.size());
            Node link = children.get(0);
            links.add(link);
            assertEquals("Link 1", link.text());
            assertEquals("link1.html", link.attr("href"));
            Node textNode = children.get(1);
            assertEquals(TextNode.class, textNode.getClass());
            assertEquals("", textNode.text());
        }
        assertEquals(links.get(0), doc.select("a[href='link1.html']").first());
        assertEquals(links.get(1), doc.select("a[href='link2.html']").first());
    }

    @Test
    public void testParseFragment() {
        String fragmentHtml = "<ul><li><a href='link1.html'>Link 1</a></li><li><a href='link2.html'>Link 2</a></li></ul>";
        Element context = Jsoup.parse("<html></html>").body();
        Document doc = Parser.parseFragment(fragmentHtml, context, null);
        List<Node> list = doc.select("ul > li");
        assertEquals(2, list.size());
        List<Node> links = new ArrayList<>();
        for (Node node : list) {
            List<Node> children = node.children();
            assertSame(2, children.size());
            Node link = children.get(0);
            links.add(link);
            assertEquals("Link 1", link.text());
            assertEquals("link1.html", link.attr("href"));
            Node textNode = children.get(1);
            assertEquals(TextNode.class, textNode.getClass());
            assertEquals("", textNode.text());
        }
        assertEquals(links.get(0), doc.select("a[href='link1.html']").first());
        assertEquals(links.get(1), doc.select("a[href='link2.html']").first());
        assertEquals(context, doc.select("html > body").first());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<ul><li><a href='link1.html'>Link 1</a></li><li><a href='link2.html'>Link 2</a></li></ul>";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, null);
        List<Node> list = doc.select("ul > li");
        assertEquals(2, list.size());
        List<Node> links = new ArrayList<>();
        for (Node node : list) {
            List<Node> children = node.children();
            assertSame(2, children.size());
            Node link = children.get(0);
            links.add(link);
            assertEquals("Link 1", link.text());
            assertEquals("link1.html", link.attr("href"));
            Node textNode = children.get(1);
            assertEquals(TextNode.class, textNode.getClass());
            assertEquals("", textNode.text());
        }
        assertEquals(links.get(0), doc.select("a[href='link1.html']").first());
        assertEquals(links.get(1), doc.select("a[href='link2.html']").first());
    }

    @Test
    public void testHtmlParser() {
        Parser parser = Parser.htmlParser();
        Document doc = parser.parseInput("<html><head></head><body><p>Hello World!</p></body></html>", null);
        assertEquals("<html><head></head><body><p>Hello World!</p></body></html>", doc.html());
    }

    @Test
    public void testXmlParser() {
        Parser parser = Parser.xmlParser();
        Document doc = parser.parseInput("<xml><html><head></head><body><p>Hello World!</p></body></html></xml>", null);
        assertEquals("<xml><html><head></head><body><p>Hello World!</p></body></html></xml>", doc.html());
    }
}