package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

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
import org.junit.Test;

public class ParserTest {

    @Test
    public void testNewParserWithTreeBuilder() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);
        assertSame(treeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testParseInput() {
        Parser parser = Parser.htmlParser();
        Document doc = parser.parseInput("<html><body><p>Hello World</p></body></html>", "http://example.com");
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("html", doc.nodeName());
        assertEquals(1, doc.childNodeSize());
        Element body = doc.body();
        assertEquals("body", body.nodeName());
        assertEquals("p", body.childNode(0).nodeName());
        assertEquals("Hello World", body.childNode(0).text());
    }

    @Test
    public void testParseInputWithTrackErrors() {
        Parser parser = Parser.htmlParser();
        parser.setTrackErrors(1);
        String invalidHtml = "<html><body><div><script>alert('Hello World')</script></div></body></html>";
        Document doc = null;
        try {
            doc = parser.parseInput(invalidHtml, "http://example.com");
        } catch (Exception e) {
            List<ParseError> errors = parser.getErrors();
            assertEquals(1, errors.size());
            ParseError error = errors.get(0);
            assertEquals(ParseError.VERSION_MISMATCH, error.getType());
            assertEquals(0, error.getLine());
            assertEquals(0, error.getColumn());
            assertEquals(0, error.getOffset());
            assertEquals(0, error.getLength());
            assertEquals(invalidHtml, error.getMessage());
            assertEquals("http://example.com", error.getSourceFile());
            assertNull(doc);
        }
    }

    @Test
    public void testParseInputWithMaxErrors() {
        Parser parser = Parser.htmlParser();
        parser.setTrackErrors(3);
        String invalidHtml = "<html><body><div><script>alert('Hello World')</script></div></body></html>";
        Document doc = null;
        try {
            doc = parser.parseInput(invalidHtml, "http://example.com");
            fail("Expected ParseException to be thrown");
        } catch (Exception e) {
            List<ParseError> errors = parser.getErrors();
            assertEquals(3, errors.size());
            ParseError error1 = errors.get(0);
            ParseError error2 = errors.get(1);
            ParseError error3 = errors.get(2);
            assertEquals(ParseError.VERSION_MISMATCH, error1.getType());
            assertEquals(ParseError.TAG_OMITTED_START, error2.getType());
            assertEquals(ParseError.TAG_MISMATCHED_END, error3.getType());
            assertNull(doc);
        }
    }

    @Test
    public void testParseInputWithMaxErrorsAndTrackErrorsDisabled() {
        Parser parser = Parser.htmlParser();
        parser.setTrackErrors(0);
        String invalidHtml = "<html><body><div><script>alert('Hello World')</script></div></body></html>";
        Document doc = null;
        try {
            doc = parser.parseInput(invalidHtml, "http://example.com");
            fail("Expected ParseException to be thrown");
        } catch (Exception e) {
            List<ParseError> errors = parser.getErrors();
            assertTrue(errors.isEmpty());
            assertNull(doc);
        }
    }

    @Test
    public void testSetTreeBuilder() {
        Parser parser = Parser.htmlParser();
        TreeBuilder treeBuilder = new XmlTreeBuilder();
        parser.setTreeBuilder(treeBuilder);
        assertSame(treeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors() {
        Parser parser = Parser.htmlParser();
        assertTrue(parser.isTrackErrors());
        parser.setTrackErrors(0);
        assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrorsWithMaxErrors() {
        Parser parser = Parser.htmlParser();
        parser.setTrackErrors(3);
        parser.setTrackErrors(0);
        assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testGetErrors() {
        Parser parser = Parser.htmlParser();
        List<ParseError> errors = parser.getErrors();
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testStaticParseFunction() {
        Document doc = Parser.parse("<html><body><p>Hello World</p></body></html>", "http://example.com");
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("html", doc.nodeName());
        assertEquals(1, doc.childNodeSize());
        Element body = doc.body();
        assertEquals("body", body.nodeName());
        assertEquals("p", body.childNode(0).nodeName());
        assertEquals("Hello World", body.childNode(0).text());
    }

    @Test
    public void testParseFragment() {
        Parser parser = Parser.htmlParser();
        Element context = Jsoup.parse("<html><body><div id='container'></div></body></html>").body();
        String fragment = "<p>Hello World</p>";
        List<Node> nodes = parser.parseFragment(fragment, context, "http://example.com");
        assertEquals(1, nodes.size());
        assertNotNull(nodes.get(0));
        assertEquals("p", nodes.get(0).nodeName());
        assertEquals("Hello World", nodes.get(0).text());
        Element p = (Element) nodes.get(0);
        assertEquals("Hello World", p.text());
        assertEquals("container", p.parent().nodeName());
    }

    @Test
    public void testParseBodyFragment() {
        Parser parser = Parser.htmlParser();
        String bodyHtml = "<p>Hello World</p>";
        Document doc = parser.parseBodyFragment(bodyHtml, "http://example.com");
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("html", doc.nodeName());
        assertEquals(1, doc.childNodeSize());
        Element body = doc.body();
        assertEquals("body", body.nodeName());
        assertEquals("p", body.childNode(0).nodeName());
        assertEquals("Hello World", body.childNode(0).text());
    }

    @Test(expected = NoSuchElementException.class)
    public void testParseBodyFragmentWithEmptyBody() {
        Parser parser = Parser.htmlParser();
        String bodyHtml = "";
        Document doc = parser.parseBodyFragment(bodyHtml, "http://example.com");
        fail("Expected NoSuchElementException to be thrown");
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        Parser parser = Parser.htmlParser();
        String bodyHtml = "<p>Hello World</p>";
        Document doc = parser.parseBodyFragmentRelaxed(bodyHtml);
        assertEquals("html", doc.nodeName());
        assertEquals(1, doc.childNodeSize());
        Element body = doc.body();
        assertEquals("body", body.nodeName());
        assertEquals("p", body.childNode(0).nodeName());
        assertEquals("Hello World", body.childNode(0).text());
    }

    @Test
    public void testHtmlParser() {
        Parser parser = Parser.htmlParser();
        Document doc = parser.parseInput("<html><body><p>Hello World</p></body></html>", "http://example.com");
        assertEquals("html", doc.nodeName());
        assertEquals(1, doc.childNodes().size());
        Element body = doc.body();
        assertEquals("body", body.nodeName());
        assertEquals(1, body.childNodes().size());
        Element p = (Element) body.childNodes().get(0);
        assertEquals("p", p.nodeName());
        assertEquals("Hello World", p.text());
    }

    @Test
    public void testXmlParser() {
        Parser parser = Parser.xmlParser();
        String xml = "<root><element>Hello World</element></root>";
        Document doc = parser.parseInput(xml, "http://example.com");
        assertEquals("root", doc.nodeName());
        assertEquals(1, doc.childNodes().size());
        Element root = (Element) doc.childNodes().get(0);
        assertEquals("element", root.nodeName());
        assertEquals("Hello World", root.text());
    }
}