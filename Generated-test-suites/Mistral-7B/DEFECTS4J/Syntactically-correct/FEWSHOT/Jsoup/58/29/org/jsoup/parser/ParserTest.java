package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parse.ParseError;
import org.jsoup.parse.ParseErrorList;
import org.jsoup.parse.ParseSettings;
import org.jsoup.parser.Parser;

public class ParserTest {

    @Test
    public void testCreateParser() {
        Parser parser = Parser.htmlParser();
        assertNotNull(parser);
    }

    @Test
    public void testParseDocument() {
        Parser parser = Parser.htmlParser();
        String html = "<html><head><title>Test</title></head><body><p>Hello World</p></body></html>";
        Document doc = parser.parseInput(html, null);
        assertNotNull(doc);
        assertEquals("Test", doc.title().text());
        assertEquals("Hello World", doc.body().text());
    }

    @Test
    public void testParseDocumentWithTrackErrors() {
        Parser parser = Parser.htmlParser();
        String invalidHtml = "<html><head><title>Test<script>alert('Hello')</script></title></head><body><p>Hello World</p></body></html>";
        parser.setTrackErrors(1);
        Document doc = parser.parseInput(invalidHtml, null);
        assertNotNull(doc);
        List<ParseError> errors = parser.getErrors();
        assertEquals(1, errors.size());
        assertEquals("Error parsing start tag : script\n    at line 2 column 10", errors.get(0).getMessage());
    }

    @Test
    public void testParseDocumentWithMaxErrors() {
        Parser parser = Parser.htmlParser();
        String invalidHtml = "<html><head><title>Test<script>alert('Hello')</script></title></head><body><p>Hello World</p><script>alert('Hello')</script></body></html>";
        parser.setTrackErrors(2);
        Document doc = parser.parseInput(invalidHtml, null);
        assertNotNull(doc);
        List<ParseError> errors = parser.getErrors();
        assertEquals(2, errors.size());
        assertEquals("Error parsing start tag : script\n    at line 3 column 10", errors.get(1).getMessage());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseDocumentWithNegativeMaxErrors() {
        Parser parser = Parser.htmlParser();
        parser.setTrackErrors(-1);
        String html = "<html></html>";
        parser.parseInput(html, null);
    }

    @Test
    public void testParseFragment() {
        Parser parser = Parser.htmlParser();
        String fragmentHtml = "<p>Hello World</p>";
        Element context = new Element("div");
        Document doc = parser.parseInput("", null);
        List<Node> nodes = parser.parseFragment(fragmentHtml, context, null);
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertEquals(Element.class, nodes.get(0).getClass());
        Node node = nodes.get(0);
        assertEquals("p", node.tagName());
        assertEquals("Hello World", node.text());
        doc.body().appendChild(node);
        assertEquals("Hello World", doc.body().text());
    }

    @Test
    public void testParseFragmentWithContext() {
        Parser parser = Parser.htmlParser();
        String fragmentHtml = "<ul><li>List Item 1</li><li>List Item 2</li></ul>";
        Element context = new Element("div");
        Document doc = parser.parseInput("", null);
        Element ul = doc.createElement("ul");
        context.appendChild(ul);
        List<Node> nodes = parser.parseFragment(fragmentHtml, context, null);
        assertNotNull(nodes);
        assertEquals(2, nodes.size());
        for (Node node : nodes) {
            ul.appendChild(node);
        }
        assertEquals("<ul><li>List Item 1</li><li>List Item 2</li></ul>", ul.outerHtml());
    }

    @Test
    public void testParseFragmentWithErrors() {
        Parser parser = Parser.htmlParser();
        String fragmentHtml = "<ul><li>List Item 1</li><script>alert('Hello')</script><li>List Item 2</li></ul>";
        Element context = new Element("div");
        Document doc = parser.parseInput("", null);
        List<Node> nodes = parser.parseFragment(fragmentHtml, context, null);
        assertNotNull(nodes);
        assertEquals(2, nodes.size());
        Element ul = doc.createElement("ul");
        context.appendChild(ul);
        for (Node node : nodes) {
            ul.appendChild(node);
        }
        List<ParseError> errors = parser.getErrors();
        assertEquals(1, errors.size());
        assertEquals("Error parsing start tag : script\n    at line 2 column 9", errors.get(0).getMessage());
    }

    @Test
    public void testParseXmlFragment() {
        Parser parser = Parser.xmlParser();
        String fragmentXml = "<person><name>John Doe</name><age>30</age></person>";
        Document doc = parser.parseInput("", null);
        Element person = doc.createElement("person");
        List<Node> nodes = parser.parseXmlFragment(fragmentXml, null);
        assertNotNull(nodes);
        assertEquals(3, nodes.size());
        for (Node node : nodes) {
            person.appendChild(node);
        }
        assertEquals("<person><name>John Doe</name><age>30</age></person>", person.outerHtml());
    }

    @Test
    public void testParseBodyFragment() {
        Parser parser = Parser.htmlParser();
        String bodyHtml = "<p>Hello World</p>";
        Document doc = parser.parseInput("", null);
        Element body = doc.body();
        List<Node> nodes = parser.parseFragment(bodyHtml, body, null);
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertEquals(Element.class, nodes.get(0).getClass());
        Node node = nodes.get(0);
        assertEquals("p", node.tagName());
        assertEquals("Hello World", node.text());
        body.appendChild(node);
        assertEquals("Hello World", doc.body().text());
    }

    @Test
    public void testUnescapeEntities() {
        Parser parser = Parser.htmlParser();
        String string = "&lt;p&gt;Hello &amp; World&lt;/p&gt;";
        String unescapedString = parser.unescapeEntities(string, false);
        assertEquals("<p>Hello & World</p>", unescapedString);
    }

    @Test
    public void testUnescapeEntitiesInAttribute() {
        Parser parser = Parser.htmlParser();
        String string = "<a href=\"http://example.com/&amp;test=1&amp;another=2&amp;more=3\">Test</a>";
        String unescapedString = parser.unescapeEntities(string, true);
        assertEquals("<a href=\"http://example.com/&test=1&amp;another=2&more=3\">Test</a>", unescapedString);
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        Parser parser = Parser.htmlParser();
        String bodyHtml = "<p>Hello World</p>";
        Document doc = parser.parseBodyFragmentRelaxed(bodyHtml, null);
        assertEquals("<html><head></head><body><p>Hello World</p></body></html>", doc.outerHtml());
    }

    @Test
    public void testHtmlParser() {
        Parser parser = Parser.htmlParser();
        String html = "<html><head><title>Test</title></head><body><p>Hello World</p></body></html>";
        Document doc = parser.parseInput(html, null);
        assertNotNull(doc);
        assertEquals("Test", doc.title().text());
        assertEquals("Hello World", doc.body().text());
    }

    @Test
    public void testXmlParser() {
        Parser parser = Parser.xmlParser();
        String xml = "<person><name>John Doe</name><age>30</age></person>";
        Document doc = parser.parseInput(xml, null);
        assertNotNull(doc);
        Element person = doc.element("person");
        assertEquals("John Doe", person.element("name").text());
        assertEquals("30", person.element("age").text());
    }

    @Test
    public void testTreeBuilder() {
        Parser parser = Parser.htmlParser();
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        assertNotNull(treeBuilder);
    }

    @Test
    public void testSetTreeBuilder() {
        Parser parser = Parser.htmlParser();
        TreeBuilder newTreeBuilder = new HtmlTreeBuilder();
        parser.setTreeBuilder(newTreeBuilder);
        TreeBuilder oldTreeBuilder = parser.getTreeBuilder();
        assertNotNull(newTreeBuilder);
        assertNotNull(oldTreeBuilder);
        assertNotSame(newTreeBuilder, oldTreeBuilder);
    }

    @Test
    public void testIsTrackErrors() {
        Parser parser = Parser.htmlParser();
        assertFalse(parser.isTrackErrors());
        parser.setTrackErrors(1);
        assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        Parser parser = Parser.htmlParser();
        parser.setTrackErrors(1);
        assertEquals(1, parser.maxErrors());
        parser.setTrackErrors(0);
        assertEquals(0, parser.maxErrors());
    }

    @Test
    public void testMaxErrors() {
        Parser parser = Parser.htmlParser();
        assertEquals(0, parser.maxErrors());
        parser.setTrackErrors(1);
        assertEquals(1, parser.maxErrors());
        parser.setTrackErrors(2);
        assertEquals(2, parser.maxErrors());
    }

    @Test
    public void testGetErrors() {
        Parser parser = Parser.htmlParser();
        List<ParseError> errors = parser.getErrors();
        assertNull(errors);
        parser.setTrackErrors(1);
        String invalidHtml = "<html><head><title>Test<script>alert('Hello')</script></title></head><body><p>Hello World</p></body></html>";
        parser.parseInput(invalidHtml, null);
        errors = parser.getErrors();
        assertNotNull(errors);
        assertEquals(1, errors.size());
    }

    @Test
    public void testSettings() {
        Parser parser = Parser.htmlParser();
        ParseSettings settings = parser.settings();
        assertNotNull(settings);
        assertEquals(ParseSettings.defaultSettings(), settings);
        settings.setTrimEmptyText(true);
        parser.settings(settings);
        Document doc = parser.parseInput("<p></p>", null);
        TextNode textNode = (TextNode) doc.body().child(0);
        assertFalse(textNode.text().trim().isEmpty());
    }

}