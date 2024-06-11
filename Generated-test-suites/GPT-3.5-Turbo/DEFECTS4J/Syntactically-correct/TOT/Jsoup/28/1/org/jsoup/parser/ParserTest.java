
package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ParserTest {
    
    private Parser parser;

    @Before
    public void setUp() {
        parser = new Parser(new HtmlTreeBuilder());
    }

    @Test
    public void testParseInput() {
      String html = "<html><head><title>Test</title></head><body><h1>Test Heading</h1><p>Test paragraph</p></body></html>";
      String baseUri = "http://example.com";
      
      Document document = parser.parseInput(html, baseUri);
      
      assertEquals("Test", document.title());
      assertEquals("Test Heading", document.body().select("h1").first().text());
      assertEquals("Test paragraph", document.body().select("p").first().text());
    }
    
    @Test
    public void testGetTreeBuilder() {
      TreeBuilder treeBuilder = parser.getTreeBuilder();
      
      assertTrue(treeBuilder instanceof HtmlTreeBuilder);
    }
    
    @Test
    public void testSetTreeBuilder() {
      TreeBuilder treeBuilder = new XmlTreeBuilder();
      
      parser.setTreeBuilder(treeBuilder);
      
      assertEquals(treeBuilder, parser.getTreeBuilder());
    }
    
    @Test
    public void testIsTrackErrors() {
      assertFalse(parser.isTrackErrors());
      
      parser.setTrackErrors(5);
      
      assertTrue(parser.isTrackErrors());
    }
    
    @Test
    public void testSetTrackErrors() {
      parser.setTrackErrors(5);
      
      assertTrue(parser.isTrackErrors());
      assertEquals(5, parser.getErrors().size());
    }
    
    @Test
    public void testGetErrors() {
      parser.setTrackErrors(5);
      
      assertEquals(5, parser.getErrors().size());
    }
    
    @Test
    public void testParse() {
      String html = "<div><p>Test paragraph</p></div>";
      String baseUri = "http://example.com";
      
      Document document = Parser.parse(html, baseUri);
      
      assertEquals("Test paragraph", document.select("p").first().text());
    }
    
    @Test
    public void testParseFragment() {
      Element context = new Element("div");
      String fragmentHtml = "<p>Test paragraph</p>";
      String baseUri = "http://example.com";
      
      List<Node> nodeList = Parser.parseFragment(fragmentHtml, context, baseUri);
      
      assertEquals(1, nodeList.size());
      assertEquals("Test paragraph", ((Element) nodeList.get(0)).text());
    }
    
    @Test
    public void testParseBodyFragment() {
      String bodyHtml = "<p>Test paragraph</p>";
      String baseUri = "http://example.com";
      
      Document document = Parser.parseBodyFragment(bodyHtml, baseUri);
      
      assertEquals("Test paragraph", document.select("p").first().text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
      String bodyHtml = "<p>Test paragraph</p>";
      String baseUri = "http://example.com";
      
      Document document = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
      
      assertEquals("Test paragraph", document.select("p").first().text());
    }
    
    @Test
    public void testHtmlParser() {
      Parser htmlParser = Parser.htmlParser();
      
      assertTrue(htmlParser.getTreeBuilder() instanceof HtmlTreeBuilder);
    }
    
    @Test
    public void testXmlParser() {
      Parser xmlParser = Parser.xmlParser();
      
      assertTrue(xmlParser.getTreeBuilder() instanceof XmlTreeBuilder);
    }
}

