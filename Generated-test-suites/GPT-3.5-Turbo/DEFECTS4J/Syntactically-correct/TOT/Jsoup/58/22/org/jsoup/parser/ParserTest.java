package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class ParserTest {
    private Parser parser;
    private TreeBuilder treeBuilder;

    @Before
    public void setUp() {
        treeBuilder = new HtmlTreeBuilder();
        parser = new Parser(treeBuilder);
    }
    
    @Test
    public void testParseInput_typicalCase() {
        String html = "<html><body><h1>Title</h1><p>Paragraph</p></body></html>";
        String baseUri = "http://www.example.com";
        
        Document document = parser.parseInput(html, baseUri);
        
        assertNotNull("Document should not be null", document);
        assertEquals("Document should have HTML root element", "html", document.nodeName());
        
        Element bodyElement = document.body();
        assertNotNull("Body element should not be null", bodyElement);
        
        List<Element> h1Elements = bodyElement.getElementsByTag("h1");
        assertEquals("There should be 1 h1 element", 1, h1Elements.size());
        Element h1Element = h1Elements.get(0);
        assertEquals("h1 element text should be 'Title'", "Title", h1Element.text());
        
        List<Element> pElements = bodyElement.getElementsByTag("p");
        assertEquals("There should be 1 p element", 1, pElements.size());
        Element pElement = pElements.get(0);
        assertEquals("p element text should be 'Paragraph'", "Paragraph", pElement.text());
    }
    
    @Test
    public void testGetTreeBuilder_typicalCase() {
        TreeBuilder result = parser.getTreeBuilder();
        
        assertEquals("GetTreeBuilder should return the same TreeBuilder used in the constructor", treeBuilder, result);
    }
    
    @Test
    public void testSetTreeBuilder_typicalCase() {
        TreeBuilder newTreeBuilder = new HtmlTreeBuilder();
        
        parser.setTreeBuilder(newTreeBuilder);
        
        TreeBuilder result = parser.getTreeBuilder();
        assertEquals("SetTreeBuilder should update the TreeBuilder used", newTreeBuilder, result);
    }
    
    @Test
    public void testIsTrackErrors_typicalCase() {
        assertFalse("IsTrackErrors should return false by default", parser.isTrackErrors());
    }
    
    @Test
    public void testSetTrackErrors_typicalCase() {
        int maxErrors = 5;
        
        parser.setTrackErrors(maxErrors);
        
        assertTrue("IsTrackErrors should return true after setting maxErrors", parser.isTrackErrors());
    }
    
    @Test
    public void testGetErrors_typicalCase() {
        List<ParseError> errors = parser.getErrors();
        
        assertNotNull("GetErrors should never return null", errors);
        assertEquals("GetErrors should return an empty list by default", 0, errors.size());
    }
    
    @Test
    public void testSettings_typicalCase() {
        ParseSettings settings = new ParseSettings(false, false);
        
        parser.settings(settings);
        
        assertEquals("Settings should be updated", settings, parser.settings());
    }
    
    @Test
    public void testParse_typicalCase() {
        String html = "<html><body><h1>Title</h1><p>Paragraph</p></body></html>";
        String baseUri = "http://www.example.com";
        
        Document document = Parser.parse(html, baseUri);
        
        assertNotNull("Document should not be null", document);
        assertEquals("Document should have HTML root element", "html", document.nodeName());
        
        Element bodyElement = document.body();
        assertNotNull("Body element should not be null", bodyElement);
        
        List<Element> h1Elements = bodyElement.getElementsByTag("h1");
        assertEquals("There should be 1 h1 element", 1, h1Elements.size());
        Element h1Element = h1Elements.get(0);
        assertEquals("h1 element text should be 'Title'", "Title", h1Element.text());
        
        List<Element> pElements = bodyElement.getElementsByTag("p");
        assertEquals("There should be 1 p element", 1, pElements.size());
        Element pElement = pElements.get(0);
        assertEquals("p element text should be 'Paragraph'", "Paragraph", pElement.text());
    }
    
    @Test
    public void testParseFragment_typicalCase() {
        String fragmentHtml = "<h1>Title</h1><p>Paragraph</p>";
        Element context = new Element("div");
        String baseUri = "http://www.example.com";
        
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
        
        assertNotNull("Nodes should not be null", nodes);
        assertEquals("There should be 2 nodes", 2, nodes.size());
        
        Node firstNode = nodes.get(0);
        assertTrue("First node should be an instance of Element", firstNode instanceof Element);
        assertEquals("First node should have tag name 'h1'", "h1", ((Element)firstNode).tagName());
        
        Node secondNode = nodes.get(1);
        assertTrue("Second node should be an instance of Element", secondNode instanceof Element);
        assertEquals("Second node should have tag name 'p'", "p", ((Element)secondNode).tagName());
    }
    
    @Test
    public void testParseXmlFragment_typicalCase() {
        String fragmentXml = "<root><element1>Value 1</element1><element2>Value 2</element2></root>";
        String baseUri = "http://www.example.com";
        
        List<Node> nodes = Parser.parseXmlFragment(fragmentXml, baseUri);
        
        assertNotNull("Nodes should not be null", nodes);
        assertEquals("There should be 1 node", 1, nodes.size());
        
        Node rootNode = nodes.get(0);
        assertTrue("Root node should be an instance of Element", rootNode instanceof Element);
        assertEquals("Root node should have tag name 'root'", "root", ((Element)rootNode).tagName());
        
        List<Element> childElements = ((Element)rootNode).children();
        assertEquals("There should be 2 child elements", 2, childElements.size());
        
        Element firstChild = childElements.get(0);
        assertEquals("First child element should have tag name 'element1'", "element1", firstChild.tagName());
        assertEquals("First child element text should be 'Value 1'", "Value 1", firstChild.text());
        
        Element secondChild = childElements.get(1);
        assertEquals("Second child element should have tag name 'element2'", "element2", secondChild.tagName());
        assertEquals("Second child element text should be 'Value 2'", "Value 2", secondChild.text());
    }
    
    @Test
    public void testParseBodyFragment_typicalCase() {
        String bodyHtml = "<h1>Title</h1><p>Paragraph</p>";
        String baseUri = "http://www.example.com";
        
        Document document = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        assertNotNull("Document should not be null", document);
        assertEquals("Document should have HTML root element", "html", document.nodeName());
        
        Element bodyElement = document.body();
        assertNotNull("Body element should not be null", bodyElement);
        
        List<Element> h1Elements = bodyElement.getElementsByTag("h1");
        assertEquals("There should be 1 h1 element", 1, h1Elements.size());
        Element h1Element = h1Elements.get(0);
        assertEquals("h1 element text should be 'Title'", "Title", h1Element.text());
        
        List<Element> pElements = bodyElement.getElementsByTag("p");
        assertEquals("There should be 1 p element", 1, pElements.size());
        Element pElement = pElements.get(0);
        assertEquals("p element text should be 'Paragraph'", "Paragraph", pElement.text());
    }
    
    @Test
    public void testUnescapeEntities_typicalCase() {
        String escapedString = "&lt;html&gt;&lt;body&gt;&lt;h1&gt;Title&lt;/h1&gt;&lt;/body&gt;&lt;/html&gt;";
        boolean inAttribute = false;
        
        String unescapedString = Parser.unescapeEntities(escapedString, inAttribute);
        
        assertEquals("Unescaped string should be '<html><body><h1>Title</h1></body></html>'", 
                     "<html><body><h1>Title</h1></body></html>", unescapedString);
    }
    
    @Test
    public void testParseBodyFragmentRelaxed_typicalCase() {
        String bodyHtml = "<h1>Title</h1><p>Paragraph</p>";
        String baseUri = "http://www.example.com";
        
        Document document = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        
        assertNotNull("Document should not be null", document);
        assertEquals("Document should have HTML root element", "html", document.nodeName());
        
        Element bodyElement = document.body();
        assertNotNull("Body element should not be null", bodyElement);
        
        List<Element> h1Elements = bodyElement.getElementsByTag("h1");
        assertEquals("There should be 1 h1 element", 1, h1Elements.size());
        Element h1Element = h1Elements.get(0);
        assertEquals("h1 element text should be 'Title'", "Title", h1Element.text());
        
        List<Element> pElements = bodyElement.getElementsByTag("p");
        assertEquals("There should be 1 p element", 1, pElements.size());
        Element pElement = pElements.get(0);
        assertEquals("p element text should be 'Paragraph'", "Paragraph", pElement.text());
    }
    
    @Test
    public void testHtmlParser_typicalCase() {
        Parser result = Parser.htmlParser();
        
        assertTrue("HtmlParser should return an instance of Parser", result instanceof Parser);
        assertTrue("HtmlParser should use HtmlTreeBuilder as the TreeBuilder", result.getTreeBuilder() instanceof HtmlTreeBuilder);
    }
    
    @Test
    public void testXmlParser_typicalCase() {
        Parser result = Parser.xmlParser();
        
        assertTrue("XmlParser should return an instance of Parser", result instanceof Parser);
        assertTrue("XmlParser should use XmlTreeBuilder as the TreeBuilder", result.getTreeBuilder() instanceof XmlTreeBuilder);
    }
}