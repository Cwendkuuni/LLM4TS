import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.junit.Test;

public class ParserTest {
 
    @Test
    public void testParseInput() {
        // Test case where html is null and baseUri is null
        Document result1 = Parser.parseInput(null, null);
        assertNull(result1);
        
        // Test case where html is empty and baseUri is null
        Document result2 = Parser.parseInput("", null);
        
        assertNotNull(result2);
        assertEquals(0, result2.childNodeSize());
        
        // Test case where html is valid and baseUri is valid
        String html = "<html><head><title>Test</title></head><body></body></html>";
        String baseUri = "http://example.com";
        Document result3 = Parser.parseInput(html, baseUri);
        
        assertNotNull(result3);
        assertEquals(html, result3.html());
        assertEquals(1, result3.head().childNodeSize());
        assertEquals(1, result3.body().childNodeSize());
    }
    
    @Test
    public void testGetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder result = parser.getTreeBuilder();
        assertNotNull(result);
        assertTrue(result instanceof HtmlTreeBuilder);
    }
    
    @Test
    public void testSetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder newTreeBuilder = new XmlTreeBuilder();
        
        Parser result = parser.setTreeBuilder(newTreeBuilder);
        assertEquals(newTreeBuilder, result.getTreeBuilder());
    }
    
    @Test
    public void testIsTrackErrors() {
        // Test case where maxErrors is 0
        Parser parser1 = new Parser(new HtmlTreeBuilder());
        boolean result1 = parser1.isTrackErrors();
        assertFalse(result1);
        
        // Test case where maxErrors is greater than 0
        Parser parser2 = new Parser(new HtmlTreeBuilder()).setTrackErrors(10);
        boolean result2 = parser2.isTrackErrors();
        assertTrue(result2);
    }
    
    @Test
    public void testSetTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        
        // Test case where maxErrors is 0
        Parser result1 = parser.setTrackErrors(0);
        assertFalse(result1.isTrackErrors());
        
        // Test case where maxErrors is greater than 0
        Parser result2 = parser.setTrackErrors(10);
        assertTrue(result2.isTrackErrors());
    }
    
    @Test
    public void testGetErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        
        // Test case where errors were not tracked
        List<ParseError> result1 = parser.getErrors();
        assertNull(result1);
        
        // Test case where errors were tracked
        Parser parser2 = new Parser(new HtmlTreeBuilder()).setTrackErrors(10);
        List<ParseError> result2 = parser2.getErrors();
        assertNotNull(result2);
        assertEquals(0, result2.size());
    }
    
    @Test
    public void testParse() {
        // Test case where html is null and baseUri is null
        Document result1 = Parser.parse(null, null);
        assertNull(result1);
        
        // Test case where html is empty and baseUri is null
        Document result2 = Parser.parse("", null);
        
        assertNotNull(result2);
        assertEquals(0, result2.childNodeSize());
        
        // Test case where html is valid and baseUri is valid
        String html = "<html><head><title>Test</title></head><body></body></html>";
        String baseUri = "http://example.com";
        Document result3 = Parser.parse(html, baseUri);
        
        assertNotNull(result3);
        assertEquals(html, result3.html());
        assertEquals(1, result3.head().childNodeSize());
        assertEquals(1, result3.body().childNodeSize());
    }
    
    @Test
    public void testParseFragment() {
        // Test case where fragmentHtml is null, context is null, and baseUri is null
        List<Node> result1 = Parser.parseFragment(null, null, null);
        assertNotNull(result1);
        assertEquals(0, result1.size());
        
        // Test case where fragmentHtml is empty, context is null, and baseUri is null
        List<Node> result2 = Parser.parseFragment("", null, null);
        assertNotNull(result2);
        assertEquals(0, result2.size());
        
        // Test case where fragmentHtml is valid, context is valid, and baseUri is valid
        String fragmentHtml = "<p>Test</p>";
        Element context = new Element("div");
        String baseUri = "http://example.com";
        List<Node> result3 = Parser.parseFragment(fragmentHtml, context, baseUri);
        
        assertNotNull(result3);
        assertEquals(1, result3.size());
        assertTrue(result3.get(0) instanceof Element);
        assertEquals("p", ((Element) result3.get(0)).tagName());
        assertEquals("Test", ((Element) result3.get(0)).text());
    }
    
    @Test
    public void testParseBodyFragment() {
        // Test case where bodyHtml is null and baseUri is null
        Document result1 = Parser.parseBodyFragment(null, null);
        assertNotNull(result1);
        assertEquals(0, result1.childNodeSize());
        
        // Test case where bodyHtml is empty and baseUri is null
        Document result2 = Parser.parseBodyFragment("", null);
        assertNotNull(result2);
        assertEquals(0, result2.childNodeSize());
        
        // Test case where bodyHtml is valid and baseUri is valid
        String bodyHtml = "<p>Test</p>";
        String baseUri = "http://example.com";
        Document result3 = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        assertNotNull(result3);
        assertEquals(bodyHtml, result3.body().html());
        assertEquals(1, result3.body().childNodeSize());
    }
    
    @Test
    public void testParseBodyFragmentRelaxed() {
        // Test case where bodyHtml is null and baseUri is null
        Document result1 = Parser.parseBodyFragmentRelaxed(null, null);
        assertNull(result1);
        
        // Test case where bodyHtml is empty and baseUri is null
        Document result2 = Parser.parseBodyFragmentRelaxed("", null);
        assertNotNull(result2);
        assertEquals(0, result2.childNodeSize());
        
        // Test case where bodyHtml is valid and baseUri is valid
        String bodyHtml = "<p>Test</p>";
        String baseUri = "http://example.com";
        Document result3 = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        
        assertNotNull(result3);
        assertEquals(bodyHtml, result3.html());
        assertEquals(1, result3.childNodeSize());
    }
    
    @Test
    public void testHtmlParser() {
        Parser result = Parser.htmlParser();
        assertNotNull(result);
        assertTrue(result.getTreeBuilder() instanceof HtmlTreeBuilder);
    }
    
    @Test
    public void testXmlParser() {
        Parser result = Parser.xmlParser();
        assertNotNull(result);
        assertTrue(result.getTreeBuilder() instanceof XmlTreeBuilder);
    }
}