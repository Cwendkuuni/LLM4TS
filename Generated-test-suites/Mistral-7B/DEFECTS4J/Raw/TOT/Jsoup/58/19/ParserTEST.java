package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.ParseSettings;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ParserTest {

    @Test
    public void testParseInput_NormalCase() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput(html, "http://example.com");

        Assert.assertEquals("http://example.com", doc.baseUri());
        Assert.assertEquals("Hello World", doc.title());
        Assert.assertNotNull(doc.body());
        Assert.assertEquals(1, doc.head().childNodeSize());
        Assert.assertEquals(1, doc.body().childNodeSize());
        Assert.assertEquals("Hello World", doc.body().firstChild().text());
    }

    @Test
    public void testParseInput_TrackErrors() {
        String html = "<html><body><h1>Hello <script>alert('XSS')</script> World</h1></body></html>";
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(1);

        Document doc = parser.parseInput(html, "http://example.com");
        List<ParseError> errors = parser.getErrors();

        Assert.assertEquals(1, errors.size());
        Assert.assertEquals("Error parsing start tag : script", errors.get(0).getMessage());
        Assert.assertEquals("script", errors.get(0).getLineNumber());
        Assert.assertEquals("script", errors.get(0).getColumnNumber());

        // Check document structure is correct despite error
        Assert.assertEquals("http://example.com", doc.baseUri());
        Assert.assertEquals("Hello <script>alert('XSS')</script> World", doc.title());
        Assert.assertNotNull(doc.body());
        Assert.assertEquals(1, doc.head().childNodeSize());
        Assert.assertEquals(1, doc.body().childNodeSize());
        Assert.assertEquals("Hello ", doc.body().firstChild().text());
    }

    @Test
    public void testParseInput_ZeroTrackErrors() {
        String html = "<html><body><h1>Hello <script>alert('XSS')</script> World</h1></body></html>";
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(0);

        Document doc = parser.parseInput(html, "http://example.com");
        List<ParseError> errors = parser.getErrors();

        Assert.assertTrue(errors.isEmpty());
    }

    @Test
    public void testParseInput_DifferentParser() {
        String html = "<xml><body><h1>Hello World</h1></body></xml>";
        Parser parser = new Parser(new XmlTreeBuilder());
        Document doc = parser.parseInput(html, "http://example.com");

        Assert.assertEquals("http://example.com", doc.baseUri());
        Assert.assertFalse(doc.title() instanceof Element);
        Assert.assertEquals(1, doc.childNodes().size());
        Assert.assertEquals("Hello World", doc.childNodes().get(0).text());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInput_NullHtml() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.parseInput(null, "http://example.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInput_NullBaseUri() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.parseInput(html, null);
    }

    @Test
    public void testParseInput_EmptyHtml() {
        String html = "";
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput(html, "http://example.com");
        Assert.assertEquals("", doc.title());
        Assert.assertEquals(0, doc.childNodes().size());
    }

    @Test
    public void testParseInput_WhiteSpace() {
        String html = " \t\n<html><body><h1>Hello World</h1></body></html>\n ";
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput(html, "http://example.com");
        Assert.assertEquals("http://example.com", doc.baseUri());
        Assert.assertEquals("Hello World", doc.title());
        Assert.assertNotNull(doc.body());
        Assert.assertEquals(1, doc.head().childNodeSize());
        Assert.assertEquals(1, doc.body().childNodeSize());
        Assert.assertEquals("Hello World", doc.body().firstChild().text());
    }
}