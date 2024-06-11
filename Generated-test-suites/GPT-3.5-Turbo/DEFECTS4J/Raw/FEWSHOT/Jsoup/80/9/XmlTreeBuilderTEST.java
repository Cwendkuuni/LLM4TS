package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {
    
    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        ParseSettings settings = builder.defaultSettings();
        
        assertNotNull(settings);
        assertEquals(ParseSettings.preserveCase, settings);
    }
    
    @Test
    public void testParseWithReaderAndBaseUri() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String input = "<xml><tag>test</tag></xml>";
        String baseUri = "http://example.com";
        
        Document doc = builder.parse(input, baseUri);
        
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("xml", doc.nodeName());
        assertEquals(1, doc.childNodes().size());
    }
    
    @Test
    public void testParseWithStringAndBaseUri() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String input = "<xml><tag>test</tag></xml>";
        String baseUri = "http://example.com";
        
        Document doc = builder.parse(input, baseUri);
        
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("xml", doc.nodeName());
        assertEquals(1, doc.childNodes().size());
    }
    
    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        
        // TODO: Test the initialiseParse method
        
    }
    
    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token token = new Token.StartTag("tag", null);
        
        boolean result = builder.process(token);
        
        assertTrue(result);
        assertEquals(1, builder.currentElement().childNodes().size());
    }
    
    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token token = new Token.EndTag("tag");
        builder.stack.add(new Element("tag", ""));
        
        boolean result = builder.process(token);
        
        assertTrue(result);
        assertEquals(0, builder.stack.size());
    }
    
    @Test
    public void testProcessComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token token = new Token.Comment("test");
        
        boolean result = builder.process(token);
        
        assertTrue(result);
        assertEquals(1, builder.currentElement().childNodes().size());
    }
    
    @Test
    public void testProcessCharacter() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token token = new Token.Character("test");
        
        boolean result = builder.process(token);
        
        assertTrue(result);
        assertEquals(1, builder.currentElement().childNodes().size());
    }
    
    @Test
    public void testProcessDoctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token token = new Token.Doctype("html", "", "");
        
        boolean result = builder.process(token);
        
        assertTrue(result);
        assertEquals(1, builder.currentElement().childNodes().size());
    }
    
    @Test
    public void testInsertNode() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element element = new Element("tag", "");
        
        builder.insertNode(element);
        
        assertEquals(1, builder.currentElement().childNodes().size());
        assertEquals(element, builder.currentElement().childNodes().get(0));
    }
    
    @Test
    public void testInsertStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("tag", null);
        
        Element element = builder.insert(startTag);
        
        assertNotNull(element);
        assertEquals("tag", element.nodeName());
        assertEquals(1, builder.currentElement().childNodes().size());
        assertFalse(element.tag().isSelfClosing());
    }
    
    @Test
    public void testInsertStartTagWithUnknownTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("unknown", null);
        
        Element element = builder.insert(startTag);
        
        assertNotNull(element);
        assertEquals("unknown", element.nodeName());
        assertEquals(1, builder.currentElement().childNodes().size());
        assertTrue(element.tag().isSelfClosing());
    }
    
    // TODO: Implement more tests for the remaining methods
    
}