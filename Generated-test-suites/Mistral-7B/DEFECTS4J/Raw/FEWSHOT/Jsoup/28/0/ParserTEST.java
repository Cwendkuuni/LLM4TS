package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
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
    public void testParseInput() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Parser parser = Parser.htmlParser();
        Document doc = parser.parseInput(html, null);
        assertNotNull(doc);
        assertTrue(doc.body().tagName().equals("body"));
        assertTrue(doc.head() == null);
        assertTrue(doc.body().child(0).tagName().equals("h1"));
        assertTrue(doc.body().child(0).text().equals("Hello World"));
    }

    @Test
    public void testParseInputWithBaseUri() {
        String html = "<img src=\"image.jpg\" alt=\"Image\">";
        Parser parser = Parser.htmlParser();
        Document doc = parser.parseInput(html, "http://test.com");
        assertNotNull(doc);
        Element img = doc.getElementsByTag("img").first();
        assertTrue(img.src().equals("http://test.com/image.jpg"));
    }

    @Test
    public void testSetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTreeBuilder(new XmlTreeBuilder());
        assertTrue(parser.getTreeBuilder() instanceof XmlTreeBuilder);
    }

    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        assertTrue(parser.isTrackErrors());
        parser.setTrackErrors(0);
        assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrorsWithMaxErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        List<ParseError> errors = parser.getErrors();
        parser.setTrackErrors(3);
        String invalidHtml = "<html><div><script>";
        Document doc = parser.parseInput(invalidHtml, null);
        errors = parser.getErrors();
        assertTrue(errors.size() >= 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInputWithNullHtml() {
        Parser parser = Parser.htmlParser();
        parser.parseInput(null, null);
    }

    @Test
    public void testParse() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Document doc = Parser.parse(html, null);
        assertNotNull(doc);
        assertTrue(doc.body().tagName().equals("body"));
        assertTrue(doc.head() == null);
        assertTrue(doc.body().child(0).tagName().equals("h1"));
        assertTrue(doc.body().child(0).text().equals("Hello World"));
    }

    @Test
    public void testParseFragment() {
        String fragmentHtml = "<p>Fragment</p>";
        Element context = new Element("div");
        Document doc = Parser.htmlParser().parseDocument();
        doc.body().appendChild(context);
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, null);
        assertTrue(nodes.size() == 1);
        assertTrue(nodes.get(0) instanceof Element);
        assertTrue(((Element) nodes.get(0)).tagName().equals("p"));
        assertTrue(((Element) nodes.get(0)).text().equals("Fragment"));
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Body Fragment</p>";
        Document doc = Parser.parseBodyFragment(bodyHtml, null);
        assertNotNull(doc);
        assertTrue(doc.head() == null);
        assertTrue(doc.body() != null);
        assertTrue(doc.body().child(0).tagName().equals("p"));
        assertTrue(doc.body().child(0).text().equals("Body Fragment"));
    }

    @Test
    public void testParseBodyFragmentWithBaseUri() {
        String bodyHtml = "<img src=\"image.jpg\" alt=\"Image\">";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://test.com");
        assertNotNull(doc);
        Element img = doc.getElementsByTag("img").first();
        assertTrue(img.src().equals("http://test.com/image.jpg"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseBodyFragmentWithNullBodyHtml() {
        Parser parser = Parser.htmlParser();
        parser.parseBodyFragment(null, null);
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<html><body><p>Body Fragment</p></body></html>";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, null);
        assertNotNull(doc);
        assertTrue(doc.head() == null);
        assertTrue(doc.body() != null);
        assertTrue(doc.body().child(0).tagName().equals("p"));
        assertTrue(doc.body().child(0).text().equals("Body Fragment"));
    }
}