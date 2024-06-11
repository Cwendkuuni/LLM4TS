package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import java.util.List;

import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParseInput() {
        // create Parser instance
        Parser parser = new Parser(new HtmlTreeBuilder());

        // test input
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";

        // expected output
        Document expected = new Document(baseUri);
        Element head = new Element("head");
        Element title = new Element("title");
        title.text("Test");
        head.appendChild(title);
        Element body = new Element("body");
        Element h1 = new Element("h1");
        h1.text("Hello World");
        body.appendChild(h1);
        expected.appendChild(head).appendChild(body);

        // actual output
        Document actual = parser.parseInput(html, baseUri);

        // compare
        assertEquals(expected, actual);
    }

    @Test
    public void testGetTreeBuilder() {
        // create Parser instance
        Parser parser = new Parser(new HtmlTreeBuilder());

        // expected output
        TreeBuilder expected = new HtmlTreeBuilder();

        // actual output
        TreeBuilder actual = parser.getTreeBuilder();

        // compare
        assertEquals(expected, actual);
    }

    @Test
    public void testSetTreeBuilder() {
        // create Parser instance
        Parser parser = new Parser(new HtmlTreeBuilder());

        // new TreeBuilder
        TreeBuilder treeBuilder = new XmlTreeBuilder();

        // set TreeBuilder
        parser.setTreeBuilder(treeBuilder);

        // expected output
        TreeBuilder expected = treeBuilder;

        // actual output
        TreeBuilder actual = parser.getTreeBuilder();

        // compare
        assertEquals(expected, actual);
    }

    @Test
    public void testIsTrackErrors() {
        // create Parser instance
        Parser parser = new Parser(new HtmlTreeBuilder());

        // expected output
        boolean expected = false;

        // actual output
        boolean actual = parser.isTrackErrors();

        // compare
        assertEquals(expected, actual);
    }

    @Test
    public void testSetTrackErrors() {
        // create Parser instance
        Parser parser = new Parser(new HtmlTreeBuilder());

        // set max errors
        int maxErrors = 10;
        parser.setTrackErrors(maxErrors);

        // expected output
        boolean expected = true;

        // actual output
        boolean actual = parser.isTrackErrors();

        // compare
        assertEquals(expected, actual);
    }

    @Test
    public void testGetErrors() {
        // create Parser instance
        Parser parser = new Parser(new HtmlTreeBuilder());

        // expected output
        List<ParseError> expected = null;

        // actual output
        List<ParseError> actual = parser.getErrors();

        // compare
        assertEquals(expected, actual);
    }

    @Test
    public void testParse() {
        // test input
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";

        // expected output
        Document expected = new Document(baseUri);
        Element head = new Element("head");
        Element title = new Element("title");
        title.text("Test");
        head.appendChild(title);
        Element body = new Element("body");
        Element h1 = new Element("h1");
        h1.text("Hello World");
        body.appendChild(h1);
        expected.appendChild(head).appendChild(body);

        // actual output
        Document actual = Parser.parse(html, baseUri);

        // compare
        assertEquals(expected, actual);
    }

    @Test
    public void testParseFragment() {
        // test input
        String fragmentHtml = "<div><p>Hello</p><p>World</p></div>";
        Element context = new Element("div");
        String baseUri = "http://example.com";

        // expected output
        Element div = new Element("div");
        Element p1 = new Element("p");
        p1.text("Hello");
        Element p2 = new Element("p");
        p2.text("World");
        div.appendChild(p1).appendChild(p2);
        List<Node> expected = div.childNodes();

        // actual output
        List<Node> actual = Parser.parseFragment(fragmentHtml, context, baseUri);

        // compare
        assertEquals(expected, actual);
    }

    @Test
    public void testParseBodyFragment() {
        // test input
        String bodyHtml = "<p>Hello</p><p>World</p>";
        String baseUri = "http://example.com";

        // expected output
        Document expected = new Document(baseUri);
        Element body = expected.body();
        Element p1 = new Element("p");
        p1.text("Hello");
        Element p2 = new Element("p");
        p2.text("World");
        body.appendChild(p1).appendChild(p2);

        // actual output
        Document actual = Parser.parseBodyFragment(bodyHtml, baseUri);

        // compare
        assertEquals(expected, actual);
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        // test input
        String bodyHtml = "<p>Hello</p><p>World</p>";
        String baseUri = "http://example.com";

        // expected output
        Document expected = new Document(baseUri);
        Element body = expected.body();
        Element p1 = new Element("p");
        p1.text("Hello");
        Element p2 = new Element("p");
        p2.text("World");
        body.appendChild(p1).appendChild(p2);

        // actual output
        Document actual = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);

        // compare
        assertEquals(expected, actual);
    }

    @Test
    public void testHtmlParser() {
        // create Parser instance
        Parser parser = Parser.htmlParser();

        // expected output
        Parser expected = new Parser(new HtmlTreeBuilder());

        // actual output
        Parser actual = Parser.htmlParser();

        // compare
        assertEquals(expected, actual);
    }

    @Test
    public void testXmlParser() {
        // create Parser instance
        Parser parser = Parser.xmlParser();

        // expected output
        Parser expected = new Parser(new XmlTreeBuilder());

        // actual output
        Parser actual = Parser.xmlParser();

        // compare
        assertEquals(expected, actual);
    }
}