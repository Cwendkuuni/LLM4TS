
package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.TreeBuilder.HtmlTreeBuilder;
import org.jsoup.parser.TreeBuilder.XmlTreeBuilder;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ParserTest {
    @Test
    public void testParseInput() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);
        
        String html = "<html><body><h1>Example</h1><p>This is a test</p></body></html>";
        String baseUri = "http://www.example.com";
        
        Document doc = parser.parseInput(html, baseUri);
        
        assertEquals("http://www.example.com", doc.baseUri());
        assertEquals("<html>\n" +
                " <head></head>\n" +
                " <body>\n" +
                "  <h1>\n" +
                "   Example\n" +
                "  </h1>\n" +
                "  <p>\n" +
                "   This is a test\n" +
                "  </p>\n" +
                " </body>\n" +
                "</html>", doc.html());
        assertEquals("Example", doc.select("h1").first().text());
        assertEquals("This is a test", doc.select("p").first().text());
        assertFalse(parser.isTrackErrors());
        assertNull(parser.getErrors());
    }

    @Test
    public void testGetTreeBuilder() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);
        
        assertEquals(treeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testSetTreeBuilder() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);
        
        TreeBuilder newTreeBuilder = new XmlTreeBuilder();
        parser.setTreeBuilder(newTreeBuilder);
        
        assertEquals(newTreeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);
        
        assertFalse(parser.isTrackErrors());
        
        parser.setTrackErrors(5);
        assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);
        
        parser.setTrackErrors(5);
        assertTrue(parser.isTrackErrors());
        assertEquals(5, parser.getErrors());
        
        parser.setTrackErrors(0);
        assertFalse(parser.isTrackErrors());
        assertNull(parser.getErrors());
    }

    @Test
    public void testGetErrors() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);
        
        assertNull(parser.getErrors());
        
        parser.setTrackErrors(5);
        assertEquals(5, parser.getErrors());
    }

    @Test
    public void testParse() {
        String html = "<html><body><h1>Example</h1><p>This is a test</p></body></html>";
        String baseUri = "http://www.example.com";
        
        Document doc = Parser.parse(html, baseUri);
        
        assertEquals("http://www.example.com", doc.baseUri());
        assertEquals("<html>\n" +
                " <head></head>\n" +
                " <body>\n" +
                "  <h1>\n" +
                "   Example\n" +
                "  </h1>\n" +
                "  <p>\n" +
                "   This is a test\n" +
                "  </p>\n" +
                " </body>\n" +
                "</html>", doc.html());
        assertEquals("Example", doc.select("h1").first().text());
        assertEquals("This is a test", doc.select("p").first().text());
    }

    @Test
    public void testParseFragment() {
        Element context = new Element("");
        String fragmentHtml = "<h1>Example</h1><p>This is a test</p>";
        String baseUri = "http://www.example.com";
        
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
        
        assertEquals(2, nodes.size());
        assertEquals("h1", nodes.get(0).nodeName());
        assertEquals("Example", nodes.get(0).text());
        assertEquals("p", nodes.get(1).nodeName());
        assertEquals("This is a test", nodes.get(1).text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Example</h1><p>This is a test</p>";
        String baseUri = "http://www.example.com";
        
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        assertEquals("http://www.example.com", doc.baseUri());
        assertEquals("<html>\n" +
                " <head></head>\n" +
                " <body>\n" +
                "  <h1>\n" +
                "   Example\n" +
                "  </h1>\n" +
                "  <p>\n" +
                "   This is a test\n" +
                "  </p>\n" +
                " </body>\n" +
                "</html>", doc.html());
        assertEquals("Example", doc.select("h1").first().text());
        assertEquals("This is a test", doc.select("p").first().text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<h1>Example</h1><p>This is a test</p>";
        String baseUri = "http://www.example.com";
        
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        
        assertEquals("http://www.example.com", doc.baseUri());
        assertEquals("<html>\n" +
                " <head></head>\n" +
                " <body>\n" +
                "  <h1>\n" +
                "   Example\n" +
                "  </h1>\n" +
                "  <p>\n" +
                "   This is a test\n" +
                "  </p>\n" +
                " </body>\n" +
                "</html>", doc.html());
        assertEquals("Example", doc.select("h1").first().text());
        assertEquals("This is a test", doc.select("p").first().text());
    }

    // Remaining test methods for utility methods
}
