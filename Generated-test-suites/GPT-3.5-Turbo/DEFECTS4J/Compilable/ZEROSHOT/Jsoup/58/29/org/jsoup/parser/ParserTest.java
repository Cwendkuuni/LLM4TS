package org.jsoup.parser;
import static org.junit.Assert.*;
import org.junit.Test;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import java.util.List;

public class ParserTest {
    
    @Test
    public void testParseInput() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document document = parser.parseInput("<html><body><h1>Test</h1></body></html>", "https://example.com");
        
        assertEquals("Test", document.getElementsByTag("h1").text());
    }

    @Test
    public void testGetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        
        assertEquals(parser.getTreeBuilder().getClass(), HtmlTreeBuilder.class);
    }

    @Test
    public void testSetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTreeBuilder(new XmlTreeBuilder());
        
        assertEquals(parser.getTreeBuilder().getClass(), XmlTreeBuilder.class);
    }

    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(10);
        
        assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(10);
        
        assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testGetErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(10);
        List<ParseError> errors = parser.getErrors();
        
        assertEquals(errors.size(), 0);
    }

    @Test
    public void testSettings() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        ParseSettings settings = new ParseSettings(false, false);
        parser.settings(settings);
        
        assertEquals(parser.settings(), settings);
    }

    @Test
    public void testParse() {
        Document document = Parser.parse("<html><body><h1>Test</h1></body></html>", "https://example.com");
        
        assertEquals("Test", document.getElementsByTag("h1").text());
    }

    @Test
    public void testParseFragment() {
        Element context = new Element("div");
        List<Node> nodes = Parser.parseFragment("<p>Test</p>", context, "https://example.com");
        
        assertEquals(nodes.size(), 1);
        assertEquals(nodes.get(0).outerHtml(), "<p>Test</p>");
    }

    @Test
    public void testParseXmlFragment() {
        List<Node> nodes = Parser.parseXmlFragment("<p>Test</p>", "https://example.com");
        
        assertEquals(nodes.size(), 1);
        assertEquals(nodes.get(0).outerHtml(), "<p>Test</p>");
    }

    @Test
    public void testParseBodyFragment() {
        Document document = Parser.parseBodyFragment("<p>Test</p>", "https://example.com");
        
        assertEquals(document.getElementsByTag("p").text(), "Test");
    }

    @Test
    public void testUnescapeEntities() {
        String unescaped = Parser.unescapeEntities("&lt;p&gt;Test&lt;/p&gt;", false);
        
        assertEquals(unescaped, "<p>Test</p>");
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        Document document = Parser.parseBodyFragmentRelaxed("<p>Test</p>", "https://example.com");
        
        assertEquals(document.getElementsByTag("p").text(), "Test");
    }

    @Test
    public void testHtmlParser() {
        Parser parser = Parser.htmlParser();
        
        assertEquals(parser.getTreeBuilder().getClass(), HtmlTreeBuilder.class);
    }

    @Test
    public void testXmlParser() {
        Parser parser = Parser.xmlParser();
        
        assertEquals(parser.getTreeBuilder().getClass(), XmlTreeBuilder.class);
    }
}