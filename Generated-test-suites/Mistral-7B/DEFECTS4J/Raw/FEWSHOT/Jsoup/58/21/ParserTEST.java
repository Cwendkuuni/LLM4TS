package org.jsoup.parser;

import static org.junit.Assert.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.ParseError;
import org.junit.Test;

import java.util.List;

public class ParserTest {

    @Test
    public void testParseInput() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Parser parser = Parser.htmlParser();
        Document doc = parser.parseInput(html, null);
        assertEquals("Hello World", doc.getElementsByTag("h1").text());
    }

    @Test
    public void testParseInputWithBaseUri() {
        String html = "<img src=\"image.png\" alt=\"Image\">";
        Parser parser = Parser.htmlParser();
        Document doc = parser.parseInput(html, "http://example.com");
        Element img = doc.getElementsByTag("img").first();
        assertEquals("http://example.com/image.png", img.absUrl("src"));
    }

    @Test
    public void testGetTreeBuilder() {
        Parser parser = Parser.htmlParser();
        assertEquals(HtmlTreeBuilder.class, parser.getTreeBuilder().getClass());
    }

    @Test
    public void testSetTreeBuilder() {
        Parser parser = Parser.htmlParser();
        TreeBuilder newTreeBuilder = new XmlTreeBuilder();
        parser.setTreeBuilder(newTreeBuilder);
        assertEquals(XmlTreeBuilder.class, parser.getTreeBuilder().getClass());
    }

    @Test
    public void testIsTrackErrors() {
        Parser parser = Parser.htmlParser();
        assertFalse(parser.isTrackErrors());

        Parser parserWithErrors = parser.setTrackErrors(5);
        assertTrue(parserWithErrors.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        Parser parser = Parser.htmlParser();
        List<ParseError> errors = parser.setTrackErrors(5).getErrors();
        assertEquals(0, errors.size());

        String invalidHtml = "<html><body><script>alert('XSS')</script></body></html>";
        errors = parser.parseInput(invalidHtml, null).getErrors();
        assertEquals(1, errors.size());
        assertEquals("Unexpected 'script' tag", errors.get(0).getMessage());
    }

    @Test
    public void testParseFragment() {
        String htmlFragment = "<div><p>Fragment</p></div>";
        Parser parser = Parser.htmlParser();
        List<Node> nodes = parser.parseFragment(htmlFragment, null, null);
        assertEquals(2, nodes.size());
        assertEquals("Fragment", nodes.get(1).getElementsByTag("p").text());
    }

    @Test
    public void testParseXmlFragment() {
        String xmlFragment = "<xml><tag>Xml data</tag></xml>";
        Parser parser = Parser.xmlParser();
        List<Node> nodes = parser.parseXmlFragment(xmlFragment, null);
        assertEquals(1, nodes.size());
        assertEquals("Xml data", nodes.get(0).text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<body><h1>Test</h1></body>";
        Parser parser = Parser.htmlParser();
        Document doc = parser.parseBodyFragment(bodyHtml, null);
        assertEquals("Test", doc.getElementsByTag("h1").text());
    }

    @Test
    public void testUnescapeEntities() {
        String htmlString = "&lt;p&gt;Test&amp;amp;&#38;gt;";
        String decodedString = Parser.unescapeEntities(htmlString, false);
        assertEquals("<p>Test&amp;</gt>", decodedString);
    }
}