package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.ParseError;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseErrorList;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ParserTest {

    @Test
    public void testParser() {
        Parser parser = Parser.htmlParser();
        assertNotNull(parser);
    }

    @Test
    public void testParseInput() {
        String html = "<html><body><div>Hello World!</div></body></html>";
        Parser parser = Parser.htmlParser();
        Document doc = parser.parseInput(html, null);
        assertNotNull(doc);
        Element body = doc.body();
        Element div = body.child(0);
        assertNotNull(div);
        assertEquals("Hello World!", div.text());
    }

    @Test
    public void testParseInputWithErrors() {
        String html = "<html><body><div>Hello<script>alert('XSS')</script>World!</div></body></html>";
        Parser parser = Parser.htmlParser();
        parser.setTrackErrors(1);
        Document doc = parser.parseInput(html, null);
        List<ParseError> errors = parser.getErrors();
        assertNotNull(errors);
        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals("Error parsing begin tag: script", error.getMessage());
        assertEquals(13, error.getLine());
        assertEquals(6, error.getColumn());
    }

    @Test
    public void testParseInputWithNoErrors() {
        String html = "<html><body><div>Hello World!</div></body></html>";
        Parser parser = Parser.htmlParser();
        parser.setTrackErrors(0);
        Document doc = parser.parseInput(html, null);
        List<ParseError> errors = parser.getErrors();
        assertNull(errors);
    }

    @Test
    public void testParseInputWithMaxErrors() {
        String html = "<html><body><div>Hello<script>alert('XSS')</script>World!</div></body></html>";
        Parser parser = Parser.htmlParser();
        parser.setTrackErrors(5);
        Document doc = parser.parseInput(html, null);
        List<ParseError> errors = parser.getErrors();
        assertEquals(5, errors.size());
    }

    @Test
    public void testSetTreeBuilder() {
        Parser parser = Parser.htmlParser();
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        parser.setTreeBuilder(treeBuilder);
        assertSame(treeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors() {
        Parser parser = Parser.htmlParser();
        assertFalse(parser.isTrackErrors());
        parser.setTrackErrors(1);
        assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrorsWithZero() {
        Parser parser = Parser.htmlParser();
        parser.setTrackErrors(0);
        assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrorsWithPositiveNumber() {
        Parser parser = Parser.htmlParser();
        parser.setTrackErrors(5);
        assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testParse() {
        String html = "<html><body><div>Hello World!</div></body></html>";
        Document doc = Parser.parse(html, null);
        assertNotNull(doc);
        Element body = doc.body();
        Element div = body.child(0);
        assertNotNull(div);
        assertEquals("Hello World!", div.text());
    }

    @Test
    public void testParseFragment() {
        String fragmentHtml = "<div>Hello World!</div>";
        Element context = new Element("html");
        Document doc = Document.createShell(null);
        doc.appendChild(context);
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, null);
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        Element divNode = (Element) nodes.get(0);
        assertEquals("Hello World!", divNode.text());
        assertSame(context, divNode.parent());
    }

    @Test
    public void testParseXmlFragment() {
        String fragmentXml = "<xml><tag>Hello World!</tag></xml>";
        List<Node> nodes = Parser.parseXmlFragment(fragmentXml, null);
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        Element tagNode = (Element) nodes.get(0);
        assertEquals("Hello World!", tagNode.text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<body><div>Hello World!</div></body>";
        Document doc = Parser.parseBodyFragment(bodyHtml, null);
        assertNotNull(doc);
        Element body = doc.body();
        Element div = body.child(0);
        assertNotNull(div);
        assertEquals("Hello World!", div.text());
    }

    @Test
    public void testUnescapeEntities() {
        String string = "&lt;Hello&gt; World! &amp; &apos; &quot; &eacute;";
        String unescapedString = Parser.unescapeEntities(string, false);
        assertEquals("<Hello> World! & & ' " + Character.quote(Character.toChars(0xE9)[0]) + "", unescapedString);

        string = "<script>alert('XSS')</script>";
        unescapedString = Parser.unescapeEntities(string, true);
        assertEquals("alert('XSS')", unescapedString);
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<body><div>Hello World!</div></body>";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, null);
        assertNotNull(doc);
        Element body = doc.body();
        Element div = body.child(0);
        assertNotNull(div);
        assertEquals("Hello World!", div.text());
    }
}