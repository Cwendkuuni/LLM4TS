package org.jsoup.parser;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.HtmlTreeBuilder;
import org.jsoup.parser.XmlTreeBuilder;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head></head><body><div>Hello World</div></body></html>";
        Document doc = Parser.parse(html, null);
        Element body = doc.body();
        assertEquals(1, doc.childNodes().size());
        assertEquals(1, body.childNodes().size());
        assertEquals("Hello World", body.childNode(0).text());
    }

    @Test
    public void testParseWithBaseUri() {
        String html = "<img src=\"image.png\" alt=\"Image\"/>";
        Document doc = Parser.parse(html, "http://example.com");
        Element img = doc.select("img").first();
        assertEquals("http://example.com/image.png", img.absUrl("src"));
    }

    @Test
    public void testParseFragment() {
        String fragmentHtml = "<div>Hello World</div>";
        Element context = new Element("div");
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, null);
        assertEquals(1, nodes.size());
        assertEquals("Hello World", nodes.get(0).text());
        assertEquals(context, nodes.get(0).parent());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<div>Hello World</div>";
        Document doc = Parser.parseBodyFragment(bodyHtml, null);
        Element body = doc.body();
        assertEquals(1, doc.childNodes().size());
        assertEquals(1, body.childNodes().size());
        assertEquals("Hello World", body.childNode(0).text());
    }

    @Test
    public void testParseInputWithTreeBuilder() {
        String html = "<html><head></head><body><div>Hello World</div></body></html>";
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Document doc = new Parser(treeBuilder).parseInput(html, null);
        Element body = doc.body();
        assertEquals(1, doc.childNodes().size());
        assertEquals(1, body.childNodes().size());
        assertEquals("Hello World", body.childNode(0).text());
    }

    @Test
    public void testParseInputWithErrorTracking() {
        String html = "<html><head></head><body><script>throw new Error(\"Parser Error\");</script></body></html>";
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(1);
        Document doc = parser.parseInput(html, null);
        List<ParseError> errors = parser.getErrors();
        assertEquals(1, errors.size());
        assertEquals("Parser Error", errors.get(0).getMessage());
    }

    @Test
    public void testParseInputWithMaxErrors() {
        String html = "<html><head></head><body><script>throw new Error(\"Parser Error 1\");</script><script>throw new Error(\"Parser Error 2\");</script></body></html>";
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(2);
        List<ParseError> errors = parser.parseInput(html, null).getErrors();
        assertEquals(2, errors.size());
        assertEquals("Parser Error 1", errors.get(0).getMessage());
        assertEquals("Parser Error 2", errors.get(1).getMessage());
    }

    @Test
    public void testParseInputWithNoErrors() {
        String html = "<html><head></head><body><div>Hello World</div></body></html>";
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(0);
        Document doc = parser.parseInput(html, null);
        List<ParseError> errors = parser.getErrors();
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testParseInputWithTrackErrors() {
        String html = "<html><head></head><body><script>throw new Error(\"Parser Error\");</script></body></html>";
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(true);
        Document doc = parser.parseInput(html, null);
        List<ParseError> errors = parser.getErrors();
        assertEquals(1, errors.size());
        assertEquals("Parser Error", errors.get(0).getMessage());
    }

    @Test
    public void testParseInputWithDifferentTreeBuilders() {
        String html = "<html><head></head><body><div>Hello World</div></body></html>";
        TreeBuilder htmlTreeBuilder = new HtmlTreeBuilder();
        TreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

        Parser htmlParser = new Parser(htmlTreeBuilder);
        Document htmlDoc = htmlParser.parseInput(html, null);
        Parser xmlParser = new Parser(xmlTreeBuilder);
        Document xmlDoc = xmlParser.parseInput(html, null);

        assertEquals(htmlDoc, htmlParser.parseInput(html, null));
        assertNotSame(htmlDoc, xmlDoc);
        assertNull(xmlDoc.head());
        assertEquals(1, htmlDoc.childNodes().size());
        assertEquals(1, htmlDoc.body().childNodes().size());
        assertEquals("Hello World", htmlDoc.body().childNode(0).text());
    }

    @Test
    public void testParseInputWithSameTreeBuilder() {
        String html = "<html><head></head><body><div>Hello World</div></body></html>";
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser1 = new Parser(treeBuilder);
        Document doc1 = parser1.parseInput(html, null);
        Parser parser2 = new Parser(treeBuilder);
        Document doc2 = parser2.parseInput(html, null);
        assertEquals(doc1, doc2);
        assertEquals(1, doc1.childNodes().size());
        assertEquals(1, doc1.body().childNodes().size());
        assertEquals("Hello World", doc1.body().childNode(0).text());
    }

    @Test
    public void testParseInputWithEmptyHtml() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput("", null);
        assertEquals(0, doc.childNodes().size());
    }

    @Test(expected = NullPointerException.class)
    public void testParseInputWithNullHtml() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.parseInput(null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testParseInputWithNullBaseUri() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.parseInput(null, (String) null);
    }

    @Test
    public void testGetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        assertSame(new HtmlTreeBuilder(), parser.getTreeBuilder());
    }

    @Test
    public void testSetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder treeBuilder = new XmlTreeBuilder();
        parser.setTreeBuilder(treeBuilder);
        assertSame(treeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        assertFalse(parser.isTrackErrors());
        parser.setTrackErrors(1);
        assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(1);
        assertEquals(1, parser.maxErrors);
    }

    @Test
    public void testGetErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        List<ParseError> errors = parser.getErrors();
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<div>Hello World</div>";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, null);
        Element body = doc.body();
        assertEquals(1, doc.childNodes().size());
        assertEquals(1, body.childNodes().size());
        assertEquals("Hello World", body.childNode(0).text());
    }

    @Test
    public void testDeprecatedParseBodyFragmentRelaxed() {
        String bodyHtml = "<div>Hello World</div>";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, null);
        assertEquals(doc, Parser.parseBodyFragment(bodyHtml, null));
    }

    @Test
    public void testHtmlParser() {
        Parser parser = Parser.htmlParser();
        assertEquals(new HtmlTreeBuilder(), parser.getTreeBuilder());
    }

    @Test
    public void testXmlParser() {
        Parser parser = Parser.xmlParser();
        assertEquals(new XmlTreeBuilder(), parser.getTreeBuilder());
    }
}