package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.TreeBuilder;
import org.junit.Before;
import org.junit.Test;

public class ParserTest {

    private TreeBuilder treeBuilder;
    private Parser parser;

    @Before
    public void setUp() {
        treeBuilder = new HtmlTreeBuilder();
        parser = new Parser(treeBuilder);
    }

    @Test
    public void testParseInput() {
        String html = "<html><body><div>Hello World</div></body></html>";
        Document doc = parser.parseInput(html, "http://example.com");

        Element body = doc.body();
        Element div = body.child(0);
        assertEquals("Hello World", div.text());
        assertEquals("http://example.com", doc.baseUri());
        assertNull(parser.getErrors());
    }

    @Test
    public void testParseInputWithErrors() {
        String html = "<html><body><script>alert('Hello')</script></body></html>";
        List<ParseError> errors = new ArrayList<>();
        ParseErrorList errorList = ParseErrorList.tracking(errors.size());
        Document doc = treeBuilder.parse(html, "", errorList);
        parser.setTreeBuilder(treeBuilder);
        parser.setTrackErrors(errors.size());
        Document parsedDoc = parser.parseInput(html, "http://example.com");
        assertEquals(errors.size(), parser.getErrors().size());
        for (ParseError error : errors) {
            assertEquals(error.toString(), parser.getErrors().get(errors.indexOf(error)).toString());
        }
    }

    @Test
    public void testGetTreeBuilder() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);
        assertSame(treeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testSetTreeBuilder() {
        TreeBuilder oldTreeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(oldTreeBuilder);
        TreeBuilder newTreeBuilder = new HtmlTreeBuilder();
        parser.setTreeBuilder(newTreeBuilder);
        assertSame(newTreeBuilder, parser.getTreeBuilder());
        assertNotSame(oldTreeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        assertFalse(parser.isTrackErrors());

        parser = new Parser(new HtmlTreeBuilder()) {
            @Override
            int maxErrors = 3;
        };
        assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(5);
        assertTrue(parser.isTrackErrors());
        assertEquals(5, parser.maxErrors);

        parser.setTrackErrors(0);
        assertFalse(parser.isTrackErrors());
        assertEquals(0, parser.maxErrors);
    }

    @Test
    public void testGetErrors() {
        List<ParseError> errors = new ArrayList<>();
        ParseErrorList errorList = ParseErrorList.tracking(errors.size());
        Document doc = treeBuilder.parse("<script>alert('Hello')</script>", "", errorList);
        Parser parser = new Parser(treeBuilder) {
            @Override
            ParseErrorList errors = errorList;
        };
        parser.parseInput("<html><body></body></html>", "http://example.com");
        List<ParseError> parsedErrors = parser.getErrors();
        assertEquals(errors.size(), parsedErrors.size());
        for (ParseError error : errors) {
            assertEquals(error.toString(), parsedErrors.get(errors.indexOf(error)).toString());
        }
    }

    @Test
    public void testParseDocumentFromString() {
        String html = "<html><body><div>Hello World</div></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Element body = doc.body();
        Element div = body.child(0);
        assertEquals("Hello World", div.text());
        assertEquals("http://example.com", doc.baseUri());
        assertNull(Parser.getErrors());
    }

    @Test
    public void testParseDocumentFromStringWithErrors() {
        String html = "<html><body><script>alert('Hello')</script></body></html>";
        List<ParseError> errors = new ArrayList<>();
        ParseErrorList errorList = ParseErrorList.tracking(errors.size());
        Document doc = treeBuilder.parse(html, "", errorList);
        List<ParseError> parsedErrors = Parser.getErrors();
        assertEquals(errors.size(), parsedErrors.size());
        for (ParseError error : errors) {
            assertEquals(error.toString(), parsedErrors.get(errors.indexOf(error)).toString());
        }
    }

    @Test
    public void testParseFragmentFromString() {
        String html = "<div>Hello World</div>";
        Element context = new Element("html");
        List<Node> nodes = Parser.parseFragment(html, context, "http://example.com");
        assertEquals(1, nodes.size());
        Element parsedDiv = (Element) nodes.get(0);
        assertEquals("Hello World", parsedDiv.text());
        assertEquals("http
--nobreak---><html><body></body></html><div>Hello World</div><--/nobreak-->", nodes.toString());
        assertEquals("http://example.com", context.baseUri());
    }

    @Test
    public void testParseFragmentFromStringWithErrors() {
        String html = "<script>alert('Hello')</script>";
        List<ParseError> errors = new ArrayList<>();
        ParseErrorList errorList = ParseErrorList.tracking(errors.size());
        Element context = new Element("html");
        List<Node> nodes = treeBuilder.parseFragment(html, context, "", errorList);
        List<ParseError> parsedErrors = Parser.getErrors();
        assertEquals(errors.size(), parsedErrors.size());
        for (ParseError error : errors) {
            assertEquals(error.toString(), parsedErrors.get(errors.indexOf(error)).toString());
        }
    }

    @Test
    public void testParseBodyFragmentFromStringWithErrors() {
        String html = "<body><script>alert('Hello')</script>";
        List<ParseError> errors = new ArrayList<>();
        ParseErrorList errorList = ParseErrorList.tracking(errors.size());
        Document doc = Document.createShell("http://example.com");
        List<Node> nodes = Parser.parseBodyFragment(html, "http://example.com");
        List<ParseError> parsedErrors = Parser.getErrors();
        assertEquals(errors.size(), parsedErrors.size());
        for (ParseError error : errors) {
            assertEquals(error.toString(), parsedErrors.get(errors.indexOf(error)).toString());
        }
    }

    @Test
    public void testParseBodyFragmentFromString() {
        String html = "<body><div>Hello World</div>";
        Document doc = Parser.parseBodyFragment(html, "http://example.com");
        Element body = doc.body();
        Element div = body.child(0);
        assertEquals("Hello World", div.text());
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("", doc.head().html(), "");
    }

    @Test
    public void testParseBodyFragmentRelaxedFromString() {
        String html = "<body><div>Hello World</div>";
        Document doc = Parser.parseBodyFragmentRelaxed(html, "http://example.com");
        Element body = doc.body();
        Element div = body.child(0);
        assertEquals("Hello World", div.text());
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("", doc.head().html(), "");
    }

    @Test
    public void testHtmlParser() {
        Parser parser = Parser.htmlParser();
        assertEquals(HtmlTreeBuilder.class, parser.getTreeBuilder().getClass());
    }

    @Test
    public void testXmlParser() {
        Parser parser = Parser.xmlParser();
        assertEquals(XmlTreeBuilder.class, parser.getTreeBuilder().getClass());
    }
}