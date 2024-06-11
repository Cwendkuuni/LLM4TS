package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;

import java.util.ArrayList;
import java.util.List;

public class ParserTest {

    @Test
    public void testParseInput() {
        String html = "<html><body><p>Hello world</p></body></html>";
        String baseUri = "http://example.com";

        Parser parser = Parser.htmlParser();
        Document doc = parser.parseInput(html, baseUri);

        Assert.assertEquals("http://example.com", doc.baseUri());
        Assert.assertEquals(1, doc.childNodes().size());
        Assert.assertEquals("html", doc.tagName());
        Assert.assertEquals("body", doc.getElementsByTag("body").first().tagName());
        Assert.assertEquals("p", doc.getElementsByTag("p").first().tagName());
        Assert.assertEquals("Hello world", doc.getElementsByTag("p").first().text());
    }

    @Test
    public void testParseInputWithErrors() {
        String html = "<html><body><p>Hello <strong><script>alert('XSS')</script></strong> world</p></body></html>";
        String baseUri = "http://example.com";

        Parser parser = Parser.htmlParser();
        parser.setTrackErrors(1);
        Document doc = parser.parseInput(html, baseUri);

        List<ParseError> errors = parser.getErrors();
        Assert.assertEquals(1, errors.size());
        Assert.assertEquals("Error parsing tag 'script' in tag 'p': Unexpected tag 'script'", errors.get(0).getMessage());
    }

    @Test
    public void testParseInputWithMaxErrorsReached() {
        String html = "<html><body><p>Hello <strong><script>alert('XSS')</script></strong> world</p></body></html>";
        String baseUri = "http://example.com";

        Parser parser = Parser.htmlParser();
        parser.setTrackErrors(2);

        // Test parsing an HTML document with 2 errors
        parser.parseInput(html, baseUri);
        List<ParseError> errors = parser.getErrors();
        Assert.assertEquals(2, errors.size());

        // Test parsing an HTML document with 3 errors
        parser.setTreeBuilder(new HtmlTreeBuilder()); // create a new TreeBuilder to reset errors
        Document doc = parser.parseInput(html, baseUri);
        errors = parser.getErrors();
        Assert.assertEquals(3, errors.size());
        Assert.assertEquals("Error parsing tag 'script' in tag 'p': Unexpected tag 'script'", errors.get(0).getMessage());
        Assert.assertEquals("Error parsing tag 'script' in tag 'p': Unexpected tag 'script'", errors.get(1).getMessage());
        Assert.assertNull(errors.get(2)); // the third error should be null since we've reached the max errors
    }

    @Test
    public void testStaticParseFunctions() {
        String html = "<html><body><p>Hello world</p></body></html>";
        String baseUri = "http://example.com";

        Document doc = Parser.parse(html, baseUri);
        Assert.assertEquals("http://example.com", doc.baseUri());
        Assert.assertEquals(1, doc.childNodes().size());
        Assert.assertEquals("html", doc.tagName());
        Assert.assertEquals("body", doc.getElementsByTag("body").first().tagName());
        Assert.assertEquals("p", doc.getElementsByTag("p").first().tagName());
        Assert.assertEquals("Hello world", doc.getElementsByTag("p").first().text());

        Element context = new Element("div");
        List<Node> nodes = Parser.parseFragment(html, context, baseUri);
        Assert.assertEquals(1, nodes.size());
        Assert.assertEquals("p", nodes.get(0).tagName());
        Assert.assertEquals("Hello world", nodes.get(0).text());
        Assert.assertSame(context, nodes.get(0).parent());

        Document docBody = Parser.parseBodyFragment(html, baseUri);
        Assert.assertEquals(1, docBody.childNodes().size());
        Assert.assertEquals("body", docBody.tagName());
        Assert.assertEquals("p", docBody.getElementsByTag("p").first().tagName());
        Assert.assertEquals("Hello world", docBody.getElementsByTag("p").first().text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<p>Hello world</p>";
        String baseUri = "http://example.com";

        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        Assert.assertEquals("http://example.com", doc.baseUri());
        Assert.assertEquals(1, doc.childNodes().size());
        Assert.assertEquals("body", doc.tagName());
        Assert.assertEquals("p", doc.getElementsByTag("p").first().tagName());
        Assert.assertEquals("Hello world", doc.getElementsByTag("p").first().text());
    }

    @Test
    public void testHtmlParser() {
        Parser parser = Parser.htmlParser();
        Assert.assertSame(new HtmlTreeBuilder(), parser.getTreeBuilder());
    }

    @Test
    public void testXmlParser() {
        Parser parser = Parser.xmlParser();
        Assert.assertSame(new XmlTreeBuilder(), parser.getTreeBuilder());
    }
}