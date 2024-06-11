package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit tests for the Parser class.
 */
public class ParserTest {
    private Parser parser;

    @Before
    public void setUp() {
        parser = new Parser(new HtmlTreeBuilder());
    }

    @Test
    public void testParseInput_withValidHtml_shouldReturnDocument() {
        String html = "<html><head></head><body></body></html>";
        String baseUri = "https://www.example.com";
        
        Document doc = parser.parseInput(html, baseUri);
        
        assertNotNull(doc);
        assertTrue(doc instanceof Document);
    }
    
    @Test
    public void testParseInput_withEmptyHtml_shouldReturnEmptyDocument() {
        String html = "";
        String baseUri = "https://www.example.com";
        
        Document doc = parser.parseInput(html, baseUri);
        
        assertNotNull(doc);
        assertTrue(doc instanceof Document);
        assertEquals("", doc.html());
    }
    
    @Test
    public void testGetTreeBuilder_shouldReturnCurrentTreeBuilder() {
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        
        assertNotNull(treeBuilder);
        assertTrue(treeBuilder instanceof HtmlTreeBuilder);
    }
    
    @Test
    public void testSetTreeBuilder_shouldSetNewTreeBuilder() {
        TreeBuilder newTreeBuilder = new XmlTreeBuilder();
        
        parser.setTreeBuilder(newTreeBuilder);
        TreeBuilder updatedTreeBuilder = parser.getTreeBuilder();
        
        assertNotNull(updatedTreeBuilder);
        assertTrue(updatedTreeBuilder instanceof XmlTreeBuilder);
    }
    
    @Test
    public void testIsTrackErrors_withEnabledTracking_shouldReturnTrue() {
        parser.setTrackErrors(5);
        
        boolean trackErrors = parser.isTrackErrors();
        
        assertTrue(trackErrors);
    }
    
    @Test
    public void testIsTrackErrors_withDisabledTracking_shouldReturnFalse() {
        parser.setTrackErrors(0);
        
        boolean trackErrors = parser.isTrackErrors();
        
        assertFalse(trackErrors);
    }
    
    @Test
    public void testSetTrackErrors_shouldUpdateMaxErrorsAndReturnCurrentParser() {
        int maxErrors = 10;
        
        Parser updatedParser = parser.setTrackErrors(maxErrors);
        
        assertEquals(maxErrors, updatedParser.maxErrors);
        assertEquals(parser, updatedParser);
    }
    
    @Test
    public void testGetErrors_withNoErrors_shouldReturnEmptyList() {
        List<ParseError> errors = parser.getErrors();
        
        assertNotNull(errors);
        assertTrue(errors.isEmpty());
    }
    
    @Test
    public void testParse_withValidHtml_shouldReturnDocument() {
        String html = "<html><head></head><body></body></html>";
        String baseUri = "https://www.example.com";
        
        Document doc = Parser.parse(html, baseUri);
        
        assertNotNull(doc);
        assertTrue(doc instanceof Document);
    }
    
    @Test
    public void testParseFragment_withValidFragmentHtmlAndContext_shouldReturnListOfNodes() {
        String fragmentHtml = "<div></div><span></span>";
        Element context = new Element("div");
        String baseUri = "https://www.example.com";
        
        List<Node> nodeList = Parser.parseFragment(fragmentHtml, context, baseUri);
        
        assertNotNull(nodeList);
        assertFalse(nodeList.isEmpty());
        assertEquals(2, nodeList.size());
    }
    
    @Test
    public void testParseBodyFragment_withValidBodyHtml_shouldReturnDocument() {
        String bodyHtml = "<div></div><span></span>";
        String baseUri = "https://www.example.com";
        
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        assertNotNull(doc);
        assertTrue(doc instanceof Document);
        assertEquals(bodyHtml, doc.body().html());
    }
    
    @Test
    public void testParseBodyFragmentRelaxed_withValidBodyHtml_shouldReturnDocument() {
        String bodyHtml = "<div></div><span></span>";
        String baseUri = "https://www.example.com";
        
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        
        assertNotNull(doc);
        assertTrue(doc instanceof Document);
        assertEquals(bodyHtml, doc.body().html());
    }
    
    @Test
    public void testHtmlParser_shouldCreateNewHtmlParser() {
        Parser htmlParser = Parser.htmlParser();
        
        assertNotNull(htmlParser);
        assertTrue(htmlParser.getTreeBuilder() instanceof HtmlTreeBuilder);
    }
    
    @Test
    public void testXmlParser_shouldCreateNewXmlParser() {
        Parser xmlParser = Parser.xmlParser();
        
        assertNotNull(xmlParser);
        assertTrue(xmlParser.getTreeBuilder() instanceof XmlTreeBuilder);
    }
}