package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {
    
    private XmlTreeBuilder xmlTreeBuilder;
    
    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }
    
    @Test
    public void testDefaultSettings() {
        ParseSettings parseSettings = xmlTreeBuilder.defaultSettings();
        assertNotNull(parseSettings);
        assertEquals(ParseSettings.preserveCase, parseSettings);
    }

    @Test
    public void testParseReaderString() {
        String input = "<xml><tag>data</tag></xml>";
        Reader reader = new StringReader(input);
        String baseUri = "http://example.com";
        Document document = xmlTreeBuilder.parse(reader, baseUri);
        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
        assertEquals(input, document.outerHtml());
    }
    
    @Test
    public void testParseStringString() {
        String input = "<xml><tag>data</tag></xml>";
        String baseUri = "http://example.com";
        Document document = xmlTreeBuilder.parse(input, baseUri);
        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
        assertEquals(input, document.outerHtml());
    }
    
    @Test
    public void testInitialiseParse() {
        String input = "<xml><tag>data</tag></xml>";
        Reader reader = new StringReader(input);
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        
        xmlTreeBuilder.initialiseParse(reader, baseUri, errors, settings);
        
        assertNotNull(xmlTreeBuilder.stack);
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals(xmlTreeBuilder.doc, xmlTreeBuilder.stack.get(0));
        assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.doc.outputSettings().syntax());
    }
    
    @Test
    public void testProcessStartTag() {
        Token.StartTag startTag = new Token.StartTag("tag");
        AttributeList attributes = new AttributeList();
        startTag.setAttributes(attributes);
        
        Element element = xmlTreeBuilder.insert(startTag);
        
        assertNotNull(element);
        assertEquals("tag", element.tagName());
        assertEquals(0, element.children().size());
        assertEquals(0, xmlTreeBuilder.stack.size());
    }
    
    @Test
    public void testProcessEndTag() {
        Token.EndTag endTag = new Token.EndTag("tag");
        Element element = new Element(Tag.valueOf("tag"), "http://example.com", new ParseSettings());
        
        xmlTreeBuilder.stack.add(element);
        xmlTreeBuilder.popStackToClose(endTag);
        
        assertEquals(0, xmlTreeBuilder.stack.size());
    }
    
    @Test
    public void testProcessComment() {
        Token.Comment commentToken = new Token.Comment("test comment");
        
        xmlTreeBuilder.insert(commentToken);
        
        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        assertTrue(xmlTreeBuilder.currentElement().childNodes().get(0) instanceof Comment);
    }
    
    @Test
    public void testProcessCharacter() {
        Token.Character character = new Token.Character("data");
        
        xmlTreeBuilder.insert(character);
        
        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        assertTrue(xmlTreeBuilder.currentElement().childNodes().get(0) instanceof TextNode);
    }
    
    @Test
    public void testProcessDoctype() {
        Token.Doctype doctype = new Token.Doctype("html", "", "");
        
        xmlTreeBuilder.insert(doctype);
        
        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        assertTrue(xmlTreeBuilder.currentElement().childNodes().get(0) instanceof DocumentType);
    }
    
    @Test
    public void testInsertNode() {
        Node node = new Element(Tag.valueOf("tag"), "http://example.com", new ParseSettings());
        
        xmlTreeBuilder.insertNode(node);
        
        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        assertEquals(node, xmlTreeBuilder.currentElement().childNodes().get(0));
    }
    
    @Test
    public void testParseFragment() {
        String inputFragment = "<tag>fragment</tag>";
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings);
        
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        assertEquals(baseUri, nodes.get(0).baseUri());
        assertEquals(inputFragment, nodes.get(0).outerHtml());
    }
    
}