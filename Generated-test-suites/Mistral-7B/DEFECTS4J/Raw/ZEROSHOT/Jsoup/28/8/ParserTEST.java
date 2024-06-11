package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.ParseError;
import org.jsoup.nodes.TreeBuilder;
import org.jsoup.parser.ParseErrorList;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link Parser} class.
 */
public class ParserTest {

    private Parser parser;
    private TreeBuilder treeBuilder;
    private Document document;
    private ParseError error;

    @Before
    public void setUp() {
        treeBuilder = new HtmlTreeBuilder();
        parser = new Parser(treeBuilder);
    }

    @Test
    public void testNewParserWithNullTreeBuilder() {
        assertNull(new Parser(null));
    }

    @Test
    public void testParserWithNullTreeBuilder() {
        parser.setTreeBuilder(null);
        assertNull(parser.getTreeBuilder());
    }

    @Test
    public void testParseHtmlWithBaseUriAndNullErrorList() {
        String html = "<html><body><h1>Test</h1></body></html>";
        document = parser.parseInput(html, "http://example.com");
        assertEquals("<html><head></head><body><h1>Test</h1></body></html>", document.html());
        assertFalse(parser.isTrackErrors());
        assertNull(parser.getErrors());
    }

    @Test
    public void testParseHtmlWithBaseUriAndTrackErrors() {
        String html = "<html><body><h1>Test</h1><script>1+1;</script></body></html>";
        parser.setTrackErrors(2);
        document = parser.parseInput(html, "http://example.com");
        assertEquals("<html><head></head><body><h1>Test</h1></body></html>", document.html());
        List<ParseError> errors = parser.getErrors();
        assertEquals(1, errors.size());
        error = errors.get(0);
        assertEquals(ParseError.SCRIPT_IN_HEAD, error.getType());
        assertEquals("http://example.com/", error.getUrl());
        assertEquals(1, error.getLine());
        assertEquals(1, error.getColumn());
        assertEquals("Expected '<', but was 'S' at line 2, column 1. (Scripting is only allowed in the head or body)",
                error.getMessage());

        errors = parser.getErrors();
        assertEquals(2, errors.size());
        error = errors.get(1);
        assertEquals(ParseError.TAG_MISMATCH, error.getType());
        assertEquals("http://example.com/", error.getUrl());
        assertEquals(2, error.getLine());
        assertEquals(12, error.getColumn());
        assertEquals("Expected '</script>', but was '<' at line 3, column 1. No script or tag found, but '<' was found here.",
                error.getMessage());
    }

    @Test
    public void testParseHtmlWithBaseUriAndEmptyErrorList() {
        String html = "<html><body><h1>Test</h1></body></html>";
        parser.setTrackErrors(0);
        document = parser.parseInput(html, "http://example.com");
        assertEquals("<html><head></head><body><h1>Test</h1></body></html>", document.html());
        assertFalse(parser.isTrackErrors());
        assertNull(parser.getErrors());
    }

    @Test
    public void testParseHtmlWithBaseUriAndNullHtml() {
        document = parser.parseInput(null, "http://example.com");
        assertNull(document);
        assertFalse(parser.isTrackErrors());
        assertNull(parser.getErrors());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseHtmlWithBaseUriAndEmptyString() {
        parser.parseInput("", "http://example.com");
    }

    @Test
    public void testParseHtmlWithNullBaseUri() {
        String html = "<html><body><h1>Test</h1></body></html>";
        document = parser.parseInput(html, null);
        assertEquals("<html><head></head><body><h1>Test</h1></body></html>", document.html());
        assertFalse(parser.isTrackErrors());
        assertNull(parser.getErrors());
    }

    @Test(expected = NullPointerException.class)
    public void testParseHtmlWithNullHtml() {
        parser.parseInput(null, "http://example.com");
    }

    @Test
    public void testSetTreeBuilder() {
        TreeBuilder newTreeBuilder = new XmlTreeBuilder();
        parser.setTreeBuilder(newTreeBuilder);
        assertEquals(newTreeBuilder, parser.getTreeBuilder());
    }

    @Test(expected = NullPointerException.class)
    public void testSetTreeBuilderWithNullTreeBuilder() {
        parser.setTreeBuilder(null);
    }

    @Test
    public void testIsTrackErrors() {
        assertFalse(parser.isTrackErrors());
        parser.setTrackErrors(1);
        assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrorsWithNegativeNumber() {
        parser.setTrackErrors(-1);
        assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrorsWithZero() {
        parser.setTrackErrors(0);
        assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testGetErrors() {
        List<ParseError> errors = new ArrayList<>();
        ParseError error1 = new ParseError(ParseError.TAG_MISMATCH, "http://example.com/", 1, 1, "Test error 1");
        ParseError error2 = new ParseError(ParseError.TAG_MISMATCH, "http://example.com/", 2, 1, "Test error 2");
        errors.add(error1);
        errors.add(error2);
        parser.setTreeBuilder(new HtmlTreeBuilder());
        parser.setTrackErrors(2);
        document = parser.parseInput("<html></html>", "http://example.com");
        List<ParseError> actualErrors = parser.getErrors();
        assertEquals(errors.size(), actualErrors.size());
        for (int i = 0; i < errors.size(); i++) {
            ParseError expectedError = errors.get(i);
            ParseError actualError = actualErrors.get(i);
            assertEquals(expectedError.getType(), actualError.getType());
            assertEquals(expectedError.getUrl(), actualError.getUrl());
            assertEquals(expectedError.getLine(), actualError.getLine());
            assertEquals(expectedError.getColumn(), actualError.getColumn());
            assertEquals(expectedError.getMessage(), actualError.getMessage());
        }
    }

    @Test
    public void testGetErrorsWithEmptyList() {
        parser.setTreeBuilder(new HtmlTreeBuilder());
        parser.setTrackErrors(0);
        document = parser.parseInput("<html></html>", "http://example.com");
        List<ParseError> actualErrors = parser.getErrors();
        assertEquals(0, actualErrors.size());
    }

    @Test
    public void testStaticParseFunctionWithNullHtml() {
        List<Node> nodes = Parser.parseFragment(null, null, "http://example.com");
        assertEmpty(nodes);
    }

    @Test
    public void testStaticParseFunctionWithEmptyHtml() {
        List<Node> nodes = Parser.parseFragment("", null, "http://example.com");
        assertEmpty(nodes);
    }

    @Test
    public void testStaticParseBodyFragmentFunctionWithNullHtml() {
        Document document = Parser.parseBodyFragment(null, "http://example.com");
        assertNull(document);
    }

    @Test
    public void testStaticParseBodyFragmentFunctionWithEmptyHtml() {
        Document document = Parser.parseBodyFragment("", "http://example.com");
        assertNull(document);
    }

    @Test
    public void testStaticParseFunctionWithBaseUriAndEmptyErrorList() {
        String fragmentHtml = "<script>1+1;</script>";
        Element context = Element.parse("<div></div>");
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, "http://example.com");
        assertEmpty(nodes);
        assertFalse(Parser.isTrackErrors());
        assertNull(Parser.getErrors());
    }

    @Test
    public void testStaticParseFunctionWithBaseUriAndTrackErrors() {
        String fragmentHtml = "<html><script>1+1;</script></html>";
        Element context = Element.parse("<div></div>");
        Parser.setTrackErrors(2);
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, "http://example.com");
        assertEmpty(nodes);
        List<ParseError> errors = Parser.getErrors();
        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals(ParseError.SCRIPT_IN_HEAD, error.getType());
        assertEquals("http://example.com/", error.getUrl());
        assertEquals(1, error.getLine());
        assertEquals(1, error.getColumn());
        assertEquals("Expected '<', but was 'S' at line 1, column 1. (Scripting is only allowed in the head or body)",
                error.getMessage());

        errors = Parser.getErrors();
        assertEquals(2, errors.size());
        error = errors.get(1);
        assertEquals(ParseError.TAG_MISMATCH, error.getType());
        assertEquals("http://example.com/", error.getUrl());
        assertEquals(2, error.getLine());
        assertEquals(1, error.getColumn());
        assertEquals("Expected '</script>', but was '<' at line 3, column 1. No script or tag found, but '<' was found here.",
                error.getMessage());
    }

    @Test
    public void testStaticParseFunctionWithBaseUriAndNullContext() {
        String fragmentHtml = "<html><script>1+1;</script></html>";
        List<Node> nodes = Parser.parseFragment(fragmentHtml, null, "http://example.com");
        assertEmpty(nodes);
        assertFalse(Parser.isTrackErrors());
        assertNull(Parser.getErrors());
    }

    @Test
    public void testParseInputWithStaticFunction() {
        String html = "<html><body><h1>Test</h1></body></html>";
        document = Parser.parse(html, "http://example.com");
        assertEquals("<html><head></head><body><h1>Test</h1></body></html>", document.html());
        assertFalse(Parser.isTrackErrors());
        assertNull(Parser.getErrors());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInputWithStaticFunctionAndNegativeLineNumber() {
        Parser.parse("<html><body><h1>Test</h1></body></html>", "http://example.com", -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInputWithStaticFunctionAndNegativeColumnNumber() {
        Parser.parse("<html><body><h1>Test</h1></body></html>", "http://example.com", 1, -1);
    }

    private void assertEmpty(List<Node> list) {
        assertTrue(list.isEmpty());
    }
}