package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;

import java.util.List;

public class ParserTest {

    @Test
    public void testParseInput() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";

        Parser parser = new Parser(new HtmlTreeBuilder());
        Document document = parser.parseInput(html, baseUri);

        Assert.assertNotNull(document);
        Assert.assertEquals("Hello World", document.getElementsByTag("h1").text());
    }

    @Test
    public void testParseInputWithErrors() {
        String html = "<html><body><script>alert('Hello')</script></body></html>";
        String baseUri = "http://example.com";

        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(1);
        Document document = parser.parseInput(html, baseUri);

        List<ParseError> errors = parser.getErrors();
        Assert.assertEquals(1, errors.size());
        Assert.assertEquals("Expecting ';' but was '<' in tag <script>", errors.get(0).getMessage());
        Assert.assertNull(document);
    }

    @Test
    public void testSetTreeBuilder() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";

        Parser parser = new Parser(new HtmlTreeBuilder());
        Document document = parser.parseInput(html, baseUri);

        TreeBuilder newTreeBuilder = new XmlTreeBuilder();
        parser.setTreeBuilder(newTreeBuilder);
        Document document2 = parser.parseInput(html, baseUri);

        Assert.assertNotSame(parser.getTreeBuilder(), newTreeBuilder);
        Assert.assertSame(parser.getTreeBuilder(), document2.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        Assert.assertFalse(parser.isTrackErrors());

        parser.setTrackErrors(1);
        Assert.assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testParseStatic() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";

        Document document = Parser.parse(html, baseUri);
        Assert.assertNotNull(document);
        Assert.assertEquals("Hello World", document.getElementsByTag("h1").text());
    }
}