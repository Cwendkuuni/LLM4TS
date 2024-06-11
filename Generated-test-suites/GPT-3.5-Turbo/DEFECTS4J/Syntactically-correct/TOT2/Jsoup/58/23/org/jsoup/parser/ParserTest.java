package org.jsoup.parser;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import java.util.List;

public class ParserTest {
    private Parser parser;

    @Before
    public void setup() {
        parser = new Parser(new HtmlTreeBuilder());
    }

    @Test
    public void testParseInput() {
        String html = "<html><body><p>Hello World</p></body></html>";
        String baseUri = "http://example.com";
        
        Document document = parser.parseInput(html, baseUri);
        
        assertNotNull(document);
        assertEquals("Hello World", document.body().text());
    }
    
    @Test
    public void testSetTreeBuilder() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        parser.setTreeBuilder(treeBuilder);
        
        assertEquals(treeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testSetTrackErrors() {
        parser.setTrackErrors(10);
        
        assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testGetErrors() {
        String html = "<html><body><p>Hello World</p></body></html>";
        String baseUri = "http://example.com";
        
        parser.parseInput(html, baseUri);
        List<ParseError> errors = parser.getErrors();
        
        assertNotNull(errors);
        assertEquals(0, errors.size());
    }

    @Test
    public void testStaticParse() {
        String html = "<html><body><p>Hello World</p></body></html>";
        String baseUri = "http://example.com";
        
        Document document = Parser.parse(html, baseUri);
        
        assertNotNull(document);
        assertEquals("Hello World", document.body().text());
    }

    @Test
    public void testParseFragment() {
        String fragmentHtml = "<p>Hello World</p>";
        Element context = new Element("div");
        String baseUri = "http://example.com";
        
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
        
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
    }

    // Additional tests for other methods and edge cases can be added here

}