package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.ParseError;
import org.jsoup.nodes.Document.OutputSettings;
import org.jsoup.nodes.Xml;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.html.HtmlTreeBuilder;
import org.jsoup.parser.xml.XmlTreeBuilder;

public class ParserTest {

    @Test
    public void testParseInput() {
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput(html, null);
        assertNotNull(doc);
        Element body = doc.body();
        Element h1 = body.child(0);
        assertEquals("Hello, World!", h1.text());
    }

    @Test
    public void testParseInputWithTrackErrors() {
        String html = "<html><body><h1>Hello, <script>World!</script></h1></body></html>";
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(1);
        Document doc = parser.parseInput(html, null);
        assertNotNull(doc);
        List<ParseError> errors = parser.getErrors();
        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals("Error parsing body: Unexpected tag 'script' at line 2 column 11", error.getMessage());
    }

    @Test
    public void testParseInputWithMaxErrors() {
        String html = "<html><body><h1>Hello, ";
        for (int i = 0; i < 10; i++) {
            html += "<script>World!</script>";
        }
        html += "</body></html>";
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(5);
        Document doc = parser.parseInput(html, null);
        assertNull(doc);
        List<ParseError> errors = parser.getErrors();
        assertEquals(5, errors.size());
    }

    @Test
    public void testParseInputWithBaseUri() {
        String html = "<img src=\"image.jpg\" alt=\"Image\">";
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput(html, "http://example.com");
        Element img = doc.selectFirst("img");
        assertEquals("http://example.com/image.jpg", img.attr("abs:src"));
    }

    @Test
    public void testParseInputWithNullBaseUri() {
        String html = "<img src=\"image.jpg\" alt=\"Image\">";
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput(html, null);
        Element img = doc.selectFirst("img");
        assertNull(img.attr("abs:src"));
    }

    @Test
    public void testParseInputWithErrorListTracking() {
        String html = "<html><body><h1>Hello, <script>World!</script></h1></body></html>";
        Parser parser = new Parser(new HtmlTreeBuilder());
        ParseErrorList errorList = ParseErrorList.tracking(1);
        Document doc = parser.parseInput(html, null, errorList, parser.defaultSettings());
        assertNotNull(doc);
        List<ParseError> errors = parser.getErrors();
        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals("Error parsing body: Unexpected tag 'script' at line 2 column 11", error.getMessage());
        assertEquals(error, errorList.getLastError());
    }

    @Test
    public void testParseInputWithErrorListNoTracking() {
        String html = "<html><body><h1>Hello, <script>World!</script></h1></body></html>";
        Parser parser = new Parser(new HtmlTreeBuilder());
        ParseErrorList errorList = ParseErrorList.noTracking();
        try {
            parser.parseInput(html, null, errorList, parser.defaultSettings());
            fail("Expected exception");
        } catch (Exception e) {
            // expected
        }
        assertNull(parser.getErrors());
    }

    @Test
    public void testGetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        assertSame(new HtmlTreeBuilder(), treeBuilder);
    }

    @Test
    public void testSetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder newTreeBuilder = new XmlTreeBuilder();
        parser.setTreeBuilder(newTreeBuilder);
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        assertSame(new XmlTreeBuilder(), treeBuilder);
    }

    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        assertTrue(parser.isTrackErrors());
        parser.setTrackErrors(0);
        assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(1);
        assertTrue(parser.isTrackErrors());
        parser.setTrackErrors(0);
        assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testGetErrors() {
        String html = "<html><body><h1>Hello, <script>World!</script></h1></body></html>";
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(1);
        Document doc = parser.parseInput(html, null);
        List<ParseError> errors = parser.getErrors();
        assertEquals(1, errors.size());
    }

    @Test
    public void testGetErrorsWhenTrackingDisabled() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(0);
        Document doc = parser.parseInput("<html><body><h1>Hello, <script>World!</script></h1></body></html>", null);
        List<ParseError> errors = parser.getErrors();
        assertNull(errors);
    }

    @Test
    public void testParseFragment() {
        String html = "<div><p>Hello, <strong>World!</strong></p></div>";
        Parser parser = new Parser(new HtmlTreeBuilder());
        Element context = Element.parse("<div></div>");
        List<Node> nodes = parser.parseFragment(html, context, null);
        assertEquals(3, nodes.size());
        Node div = nodes.get(0);
        assertEquals(Element.Tag.DIV, div.tagName());
        Node p = nodes.get(1);
        assertEquals(Element.Tag.P, p.tagName());
        Node strong = nodes.get(2);
        assertEquals(Element.Tag.STRONG, strong.tagName());
        assertEquals("Hello, World!", strong.text());
    }

    @Test
    public void testParseFragmentWithErrorListTracking() {
        String html = "<script>Hello, World!</script>";
        Parser parser = new Parser(new HtmlTreeBuilder());
        ParseErrorList errorList = ParseErrorList.tracking(1);
        List<Node> nodes = parser.parseFragment(html, null, null, errorList, parser.defaultSettings());
        assertEquals(0, nodes.size());
        ParseError error = errorList.getLastError();
        assertEquals("Error parsing fragment: Unexpected tag 'script' at line 1 column 1", error.getMessage());
    }

    @Test
    public void testParseFragmentWithErrorListNoTracking() {
        String html = "<script>Hello, World!</script>";
        Parser parser = new Parser(new HtmlTreeBuilder());
        ParseErrorList errorList = ParseErrorList.noTracking();
        try {
            parser.parseFragment(html, null, null, errorList, parser.defaultSettings());
            fail("Expected exception");
        } catch (Exception e) {
            // expected
        }
        assertNull(parser.getErrors());
        assertNull(errorList.getLastError());
    }

    @Test
    public void testParseXmlFragment() {
        String xml = "<root><child><subchild>Hello, World!</subchild></child></root>";
        Parser parser = new Parser(new XmlTreeBuilder());
        List<Node> nodes = parser.parseXmlFragment(xml, null);
        assertEquals(3, nodes.size());
        Node root = nodes.get(0);
        assertEquals(Xml.Tag.ROOT, root.tagName());
        Node child = nodes.get(1);
        assertEquals(Xml.Tag.ELEMENT, child.tagName());
        assertEquals("child", child.nodeName());
        Node subchild = nodes.get(2);
        assertEquals(Xml.Tag.ELEMENT, subchild.tagName());
        assertEquals("subchild", subchild.nodeName());
        assertEquals("Hello, World!", subchild.text());
    }

    @Test
    public void testParseBodyFragment() {
        String html = "<body><p>Hello, World!</p></body>";
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseBodyFragment(html, null);
        assertEquals("<html><body><p>Hello, World!</p></body></html>", doc.html());
        assertEquals("Hello, World!", doc.body().text());
    }

    @Test
    public void testUnescapeEntities() {
        String html = "&lt;Hello, &amp;World!&gt;";
        String text = Parser.unescapeEntities(html, false);
        assertEquals("<Hello, &World!>", text);
        String text2 = Parser.unescapeEntities(html, true);
        assertEquals("<Hello, World!>", text2);
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String html = "<body><p>Hello, World!</p></body>";
        Document doc = Parser.parseBodyFragment(html, null);
        assertEquals("<html><body><p>Hello, World!</p></body></html>", doc.html());
        assertEquals("Hello, World!", doc.body().text());
    }

    @Test
    public void testHtmlParser() {
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        Parser parser = Parser.htmlParser();
        Document doc = parser.parseInput(html, null);
        assertEquals("<html><body><h1>Hello, World!</h1></body></html>", doc.html());
        assertEquals("Hello, World!", doc.body().text());
    }

    @Test
    public void testXmlParser() {
        String xml = "<root><child><subchild>Hello, World!</subchild></child></root>";
        Parser parser = Parser.xmlParser();
        List<Node> nodes = parser.parseInput(xml, null);
        assertEquals(3, nodes.size());
        Node root = nodes.get(0);
        assertEquals(Xml.Tag.ROOT, root.tagName());
        Node child = nodes.get(1);
        assertEquals(Xml.Tag.ELEMENT, child.tagName());
        assertEquals("child", child.nodeName());
        Node subchild = nodes.get(2);
        assertEquals(Xml.Tag.ELEMENT, subchild.tagName());
        assertEquals("subchild", subchild.nodeName());
        assertEquals("Hello, World!", subchild.text());
    }
}