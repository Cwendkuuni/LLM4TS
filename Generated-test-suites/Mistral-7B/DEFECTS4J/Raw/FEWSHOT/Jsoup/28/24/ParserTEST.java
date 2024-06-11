package org.jsoup.parser;

import static org.junit.Assert.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ParserTest {

    @Test
    public void testParseInput() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput("<html><head></head><body><div>Hello World</div></body></html>", "http://example.com");
        assertEquals("<html><head></head><body><div>Hello World</div></body></html>", doc.html());
    }

    @Test
    public void testParseInputWithTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(1);
        Document doc = parser.parseInput("<html><head></head><body><div>Hello World</div></body></html>", "http://example.com");
        List<ParseError> errors = parser.getErrors();
        assertEquals(0, errors.size());
        assertEquals("<html><head></head><body><div>Hello World</div></body></html>", doc.html());

        parser.setTrackErrors(2);
        String invalidHtml = "<html><head></head><body><div>Hello <script>alert('XSS')</script> World</div></body></html>";
        doc = parser.parseInput(invalidHtml, "http://example.com");
        errors = parser.getErrors();
        assertEquals(1, errors.size());
        assertEquals("Tag 'script' not allowed in document element at line 3 column 15", errors.get(0).getMessage());
        assertEquals("<html><head></head><body><div>Hello World</div></body></html>", doc.html());
    }

    @Test
    public void testParseInputWithMaxErrorsReached() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(1);
        String invalidHtml = "<html><head></head><body>";
        List<ParseError> errors = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Document doc = parser.parseInput(invalidHtml, "http://example.com");
            errors.addAll(parser.getErrors());
            parser.clearErrors();
        }
        assertEquals(10, errors.size());
        assertEquals("Tag 'body' not closed at line 2 column 1", errors.get(0).getMessage());
    }

    @Test
    public void testParseBodyFragment() {
        Parser parser = Parser.htmlParser();
        Document doc = parser.parseBodyFragment("<div>Hello World</div>", "http://example.com");
        assertEquals("<html><head></head><body><div>Hello World</div></body></html>", doc.html());
    }

    @Test
    public void testParseFragment() {
        Parser parser = Parser.htmlParser();
        Element context = new Element("html");
        Document doc = parser.parseInput("<html><head></head><body><div>Hello World</div></body></html>", "http://example.com");
        List<Node> nodes = parser.parseFragment("<div>Hello <b>World</b></div>", context, "http://example.com");
        Node[] expectedNodes = {new Element("div"), new Text("Hello "), new Element("b"), new Text("World"), new Element("b")};
        assertArrayEquals(expectedNodes, nodes.toArray(expectedNodes));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseFragmentWithNullContext() {
        Parser parser = Parser.htmlParser();
        parser.parseFragment(null, null, "http://example.com");
    }

    @Test
    public void testParseInputWithTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput("<html><head></head><body><div>Hello World</div></body></html>", "http://example.com");
        Element div = doc.body().getElementsByTag("div").first();
        assertEquals("Hello World", div.text());
    }

    @Test
    public void testParseInputWithXmlTreeBuilder() {
        Parser parser = new Parser(new XmlTreeBuilder());
        Document doc = parser.parseInput("<root><div>Hello World</div></root>", "http://example.com");
        Element div = doc.getElementsByTag("div").first();
        assertEquals("Hello World", div.text());
    }

    @Test
    public void testParseInputWithEmptyInput() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput("", "http://example.com");
        assertEquals("<html><head></head><body></body></html>", doc.html());
    }

    @Test
    public void testParseInputWithNullInput() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput(null, "http://example.com");
        assertNull(doc);
    }
}