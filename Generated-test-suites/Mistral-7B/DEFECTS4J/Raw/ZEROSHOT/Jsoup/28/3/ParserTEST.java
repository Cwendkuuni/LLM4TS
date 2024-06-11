package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.ParseError;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Test class for Parser.java
 */
public class ParserTest {

    @Test
    public void testParseInput() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";

        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput(html, baseUri);

        Element root = doc.rootElement();
        Assert.assertEquals("html", root.tagName());
        Assert.assertEquals("http://example.com", doc.baseUri());
        Assert.assertEquals("Hello World", root.child(0).child(0).text());

        List<ParseError> errors = parser.getErrors();
        Assert.assertEquals(0, errors.size());
    }

    @Test
    public void testParseInputWithTrackingErrors() {
        String html = "<html><body><h1>Hello World</h1><script>alert('Hello')</script></body></html>";
        String baseUri = "http://example.com";

        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(1);
        Document doc = parser.parseInput(html, baseUri);

        Element root = doc.rootElement();
        Assert.assertEquals("html", root.tagName());
        Assert.assertEquals("http://example.com", doc.baseUri());
        Assert.assertEquals("Hello World", root.child(0).child(0).text());

        List<ParseError> errors = parser.getErrors();
        Assert.assertEquals(1, errors.size());
        Assert.assertEquals("Error parsing start tag : script", errors.get(0).getMessage());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInputWithNullHtml() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.parseInput(null, "");
    }

    @Test
    public void testParseInputWithEmptyHtml() {
        String html = "";
        String baseUri = "http://example.com";

        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput(html, baseUri);

        Element root = doc.rootElement();
        Assert.assertEquals("html", root.tagName());
        Assert.assertEquals("http://example.com", doc.baseUri());
        Assert.assertTrue(doc.body() == null);

        List<ParseError> errors = parser.getErrors();
        Assert.assertEquals(0, errors.size());
    }

    @Test
    public void testParseInputWithSimpleXml() {
        String xml = "<root><element>Hello World</element></root>";

        Parser parser = new Parser(new XmlTreeBuilder());
        Document doc = parser.parseInput(xml, null);

        Element root = doc.rootElement();
        Assert.assertEquals("root", root.tagName());
        Assert.assertNull(doc.baseUri());
        Assert.assertEquals("Hello World", root.child(0).child(0).text());

        List<ParseError> errors = parser.getErrors();
        Assert.assertEquals(0, errors.size());
    }

    @Test
    public void testParseFragment() {
        String fragmentHtml = "<p>Hello World</p>";
        Element context = new Element("div");
        String baseUri = "http://example.com";

        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
        Assert.assertEquals(1, nodes.size());
        Element p = (Element) nodes.get(0);
        Assert.assertEquals("p", p.tagName());
        Assert.assertEquals("Hello World", p.child(0).text());

        List<ParseError> errors = parser.getErrors();
        Assert.assertEquals(0, errors.size());
    }

    @Test
    public void testParseFragmentWithTrackingErrors() {
        String fragmentHtml = "<p>Hello World</p><script>alert('Hello')</script>";
        Element context = new Element("div");
        String baseUri = "http://example.com";

        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(1);
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
        Assert.assertEquals(1, nodes.size());
        Element p = (Element) nodes.get(0);
        Assert.assertEquals("p", p.tagName());
        Assert.assertEquals("Hello World", p.child(0).text());

        List<ParseError> errors = parser.getErrors();
        Assert.assertEquals(1, errors.size());
        Assert.assertEquals("Error parsing start tag : script", errors.get(0).getMessage());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello World</p>";

        Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");

        Element root = doc.rootElement();
        Assert.assertEquals("html", root.tagName());
        Assert.assertEquals("http://example.com", doc.baseUri());
        Element body = doc.body();
        Assert.assertEquals("p", body.child(0).tagName());
        Assert.assertEquals("Hello World", body.child(0).child(0).text());

        List<ParseError> errors = parser.getErrors();
        Assert.assertEquals(0, errors.size());
    }

    @Test
    public void testParseBodyFragmentWithTrackingErrors() {
        String bodyHtml = "<p>Hello World</p><script>alert('Hello')</script>";

        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(1);
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");

        Element root = doc.rootElement();
        Assert.assertEquals("html", root.tagName());
        Assert.assertEquals("http://example.com", doc.baseUri());
        Element body = doc.body();
        Assert.assertEquals("p", body.child(0).tagName());
        Assert.assertEquals("Hello World", body.child(0).child(0).text());

        List<ParseError> errors = parser.getErrors();
        Assert.assertEquals(1, errors.size());
        Assert.assertEquals("Error parsing start tag : script", errors.get(0).getMessage());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<p>Hello World</p>";

        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "http://example.com");

        Element root = doc.rootElement();
        Assert.assertEquals("html", root.tagName());
        Assert.assertEquals("http://example.com", doc.baseUri());
        Element body = doc.body();
        Assert.assertEquals("p", body.child(0).tagName());
        Assert.assertEquals("Hello World", body.child(0).child(0).text());
    }

    @Test
    public void testHtmlParser() {
        Parser parser = Parser.htmlParser();
        String html = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";

        Document doc = parser.parseInput(html, baseUri);

        Element root = doc.rootElement();
        Assert.assertEquals("html", root.tagName());
        Assert.assertEquals("http://example.com", doc.baseUri());
        Assert.assertEquals("Hello World", root.child(0).child(0).text());

        List<ParseError> errors = parser.getErrors();
        Assert.assertEquals(0, errors.size());
    }

    @Test
    public void testXmlParser() {
        Parser parser = Parser.xmlParser();
        String xml = "<root><element>Hello World</element></root>";

        Document doc = parser.parseInput(xml, null);

        Element root = doc.rootElement();
        Assert.assertEquals("root", root.tagName());
        Assert.assertNull(doc.baseUri());
        Assert.assertEquals("Hello World", root.child(0).child(0).text());

        List<ParseError> errors = parser.getErrors();
        Assert.assertEquals(0, errors.size());
    }
}