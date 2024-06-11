package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.ParseError;
import org.jsoup.nodes.Tree;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.jsoup.Jsoup.connect;
import static org.jsoup.parser.Parser.htmlParser;

public class ParserTest {

    private Parser parser;

    @Before
    public void setup() {
        parser = htmlParser();
    }

    @Test
    public void testParseValidHtml() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Document doc = parser.parseInput(html, null);
        Element head = doc.head(); // checking if head exists
        Element body = doc.body();
        Element h1 = body.selectFirst("h1");

        assertThat(head, is(notNullValue()));
        assertThat(body, is(notNullValue()));
        assertThat(h1, is(notNullValue()));
        assertThat(h1.text(), is("Hello World"));
    }

    @Test
    public void testParseInvalidHtml() {
        String html = "<html<script><h1>Hello World</h1>";
        List<ParseError> parseErrors = parser.parseInput(html, null).errors();

        assertThat(parseErrors.size(), is(1));
        ParseError firstError = parseErrors.get(0);
        assertThat(firstError.row(), is(1));
        assertThat(firstError.column(), is(7));
        assertThat(firstError.line(), is("html<script<h1>Hello World</h1>"));
    }

    @Test
    public void testParseHtmlWithTrackErrors() {
        Parser parserWithTrackErrors = htmlParser().setTrackErrors(3);
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Document doc = parserWithTrackErrors.parseInput(html, null);
        List<ParseError> parseErrors = parserWithTrackErrors.getErrors();

        assertThat(parseErrors.size(), is(0));
    }

    @Test
    public void testParseHtmlWithMaxErrors() {
        Parser parserWithMaxErrors = htmlParser().setTrackErrors(5).setMaxErrors(1);
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Document doc = parserWithMaxErrors.parseInput(html, null);
        List<ParseError> parseErrors = parserWithMaxErrors.getErrors();

        assertThat(parseErrors.size(), is(1));
        ParseError firstError = parseErrors.get(0);
        assertThat(firstError.row(), is(1));
        assertThat(firstError.column(), is(0));
        assertThat(firstError.line(), is("<html>"));
    }

    @Test
    public void testParseHtmlWithBaseUri() {
        String baseUri = "http://example.com/";
        Parser parserWithBaseUri = htmlParser().setBaseUri(baseUri);
        String html = "<img src=\"images/logo.png\" alt=\"logo\">";
        Document doc = parserWithBaseUri.parseInput(html, null);
        Element img = doc.selectFirst("img");
        String src = img.attr("src");

        assertThat(src, is("http://example.com/images/logo.png"));
    }

    @Test
    public void testParseValidXml() {
        String xml = "<xml><tag>Hello World</tag></xml>";
        Document doc = Parser.xmlParser().parseInput(xml, null);
        Element tag = doc.selectFirst("tag");

        assertThat(tag, is(notNullValue()));
        assertThat(tag.text(), is("Hello World"));
    }
}