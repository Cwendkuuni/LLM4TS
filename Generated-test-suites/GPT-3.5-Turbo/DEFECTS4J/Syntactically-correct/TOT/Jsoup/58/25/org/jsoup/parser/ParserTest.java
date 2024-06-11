package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {
    
    // Test parseInput method
    @Test
    public void testParseInput() {
        Parser parser = new Parser(new TreeBuilder());
        String html = "<html><head><title>Test</title></head><body><h1>Heading 1</h1></body></html>";
        String baseUri = "http://example.com";
        
        Document doc = parser.parseInput(html, baseUri);
        
        // Check if the document has been parsed correctly
        assertNotNull(doc);
        assertEquals("Test", doc.title());
        assertEquals("Heading 1", doc.select("h1").text());
        assertEquals(html, doc.html());
        
        // Check if error tracking is disabled
        assertTrue(parser.getErrors().isEmpty());
    }
    
    // Test getTreeBuilder and setTreeBuilder methods
    @Test
    public void testGetSetTreeBuilder() {
        Parser parser = new Parser(new TreeBuilder());
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        
        // Check if the default TreeBuilder is returned
        assertEquals(parser.getTreeBuilder(), treeBuilder);
        
        // Check if the TreeBuilder is set correctly
        parser.setTreeBuilder(new XmlTreeBuilder());
        assertEquals(parser.getTreeBuilder(), new XmlTreeBuilder());
    }
    
    // Test isTrackErrors and setTrackErrors methods
    @Test
    public void testIsSetTrackErrors() {
        Parser parser = new Parser(new TreeBuilder());
        
        // Check if error tracking is disabled by default
        assertFalse(parser.isTrackErrors());
        
        // Check if error tracking is enabled
        parser.setTrackErrors(10);
        assertTrue(parser.isTrackErrors());
    }
    
    // Test getErrors method
    @Test
    public void testGetErrors() {
        Parser parser = new Parser(new TreeBuilder());
        
        // Check if error list is empty by default
        assertTrue(parser.getErrors().isEmpty());
        
        // Check if error list has errors after parsing with error tracking enabled
        parser.setTrackErrors(10);
        parser.parseInput("<html><head</html>", "http://example.com");
        assertFalse(parser.getErrors().isEmpty());
    }
    
    // Test settings method
    @Test
    public void testSettings() {
        Parser parser = new Parser(new TreeBuilder());
        ParseSettings settings = new ParseSettings(true, true);
        
        // Check if the settings are set correctly
        parser.settings(settings);
        assertEquals(parser.settings(), settings);
    }
    
    // Test parse method
    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Heading 1</h1></body></html>";
        String baseUri = "http://example.com";
        
        Document doc = Parser.parse(html, baseUri);
        
        // Check if the document has been parsed correctly
        assertNotNull(doc);
        assertEquals("Test", doc.title());
        assertEquals("Heading 1", doc.select("h1").text());
        assertEquals(html, doc.html());
    }
    
    // Test parseFragment method
    @Test
    public void testParseFragment() {
        Element context = new Element(Tag.valueOf("div"), "");
        String fragmentHtml = "<p>Paragraph 1</p><p>Paragraph 2</p>";
        String baseUri = "http://example.com";
        
        List<Node> nodeList = Parser.parseFragment(fragmentHtml, context, baseUri);
        
        // Check if the fragment has been parsed correctly
        assertNotNull(nodeList);
        assertEquals(2, nodeList.size());
        assertEquals("Paragraph 1", nodeList.get(0).outerHtml());
        assertEquals("Paragraph 2", nodeList.get(1).outerHtml());
    }
    
    // Test parseXmlFragment method
    @Test
    public void testParseXmlFragment() {
        String fragmentXml = "<root><element>Value</element></root>";
        String baseUri = "http://example.com";
        
        List<Node> nodeList = Parser.parseXmlFragment(fragmentXml, baseUri);
        
        // Check if the fragment has been parsed correctly
        assertNotNull(nodeList);
        assertEquals(1, nodeList.size());
        assertEquals("Value", nodeList.get(0).outerHtml());
    }
    
    // Test parseBodyFragment method
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Paragraph 1</p><p>Paragraph 2</p>";
        String baseUri = "http://example.com";
        
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        // Check if the body fragment has been parsed correctly
        assertNotNull(doc);
        assertEquals("Paragraph 1\nParagraph 2", doc.body().text());
    }
    
    // Test unescapeEntities method
    @Test
    public void testUnescapeEntities() {
        String escapedString = "&lt;p&gt;Paragraph&lt;/p&gt;";
        
        String unescapedString = Parser.unescapeEntities(escapedString, true);
        
        // Check if the string has been unescaped correctly
        assertNotNull(unescapedString);
        assertEquals("<p>Paragraph</p>", unescapedString);
    }
    
    // Test deprecated parseBodyFragmentRelaxed method
    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<p>Paragraph 1</p><p>Paragraph 2</p>";
        String baseUri = "http://example.com";
        
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        
        // Check if the body fragment has been parsed correctly
        assertNotNull(doc);
        assertEquals("Paragraph 1\nParagraph 2", doc.body().text());
    }
    
    // Test htmlParser method
    @Test
    public void testHtmlParser() {
        Parser parser = Parser.htmlParser();
        
        // Check if the parser is an instance of HtmlTreeBuilder
        assertTrue(parser.getTreeBuilder() instanceof HtmlTreeBuilder);
    }
    
    // Test xmlParser method
    @Test
    public void testXmlParser() {
        Parser parser = Parser.xmlParser();
        
        // Check if the parser is an instance of XmlTreeBuilder
        assertTrue(parser.getTreeBuilder() instanceof XmlTreeBuilder);
    }
}