package org.jsoup.parser;

import org.jsoup.nodes.*;
import org.jsoup.helper.Validate;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

/**
* Unit tests for the XmlTreeBuilder class.
*/
public class XmlTreeBuilderTest {
    
    private XmlTreeBuilder xmlTreeBuilder;
    
    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }
    
    @Test
    public void testInitialiseParse() {
        String input = "<xml><tag1>Text</tag1><tag2>Text2</tag2></xml>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);
        
        // Assert statements
        assertEquals("http://example.com", xmlTreeBuilder.baseUri);
        assertNotNull(xmlTreeBuilder.doc);
        assertNotNull(xmlTreeBuilder.stack);
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.doc.outputSettings().syntax());
    }
    
    @Test
    public void testProcess_startTagToken() {
        Token startTagToken = new Token.StartTag("tag1");
        
        boolean result = xmlTreeBuilder.process(startTagToken);
        
        // Assert statements
        assertTrue(result);
        assertNotNull(xmlTreeBuilder.currentElement());
        assertEquals("tag1", xmlTreeBuilder.currentElement().tagName());
    }
    
    @Test
    public void testProcess_endTagToken() {
        Token endTagToken = new Token.EndTag("tag1");
        
        boolean result = xmlTreeBuilder.process(endTagToken);
        
        // Assert statements
        assertTrue(result);
        assertEquals(0, xmlTreeBuilder.stack.size());
    }
    
    @Test
    public void testProcess_commentToken() {
        Token.Comment commentToken = new Token.Comment("Test comment");
        
        boolean result = xmlTreeBuilder.process(commentToken);
        
        // Assert statements
        assertTrue(result);
        assertEquals(1, xmlTreeBuilder.doc.childNodes().size());
        assertTrue(xmlTreeBuilder.doc.childNode(0) instanceof Comment);
    }
    
    @Test
    public void testProcess_characterToken() {
        Token.Character characterToken = new Token.Character("Text");
        
        boolean result = xmlTreeBuilder.process(characterToken);
        
        // Assert statements
        assertTrue(result);
        assertEquals(1, xmlTreeBuilder.doc.childNodes().size());
        assertTrue(xmlTreeBuilder.doc.childNode(0) instanceof TextNode);
    }
    
    @Test
    public void testProcess_doctypeToken() {
        Token.Doctype doctypeToken = new Token.Doctype("html", "", "", "");
        
        boolean result = xmlTreeBuilder.process(doctypeToken);
        
        // Assert statements
        assertTrue(result);
        assertEquals(1, xmlTreeBuilder.doc.childNodes().size());
        assertTrue(xmlTreeBuilder.doc.childNode(0) instanceof DocumentType);
    }
    
    @Test
    public void testInsert_startTag() {
        Token.StartTag startTag = new Token.StartTag("tag1");
        
        Element element = xmlTreeBuilder.insert(startTag);
        
        // Assert statements
        assertEquals("tag1", element.tagName());
        assertEquals(xmlTreeBuilder.baseUri, element.baseUri());
        assertNotNull(element.attributes());
        assertEquals(1, xmlTreeBuilder.doc.childNodes().size());
    }
    
    @Test
    public void testInsert_commentToken() {
        Token.Comment commentToken = new Token.Comment("Test comment");
        
        xmlTreeBuilder.insert(commentToken);
        
        // Assert statements
        assertEquals(1, xmlTreeBuilder.doc.childNodes().size());
        assertTrue(xmlTreeBuilder.doc.childNode(0) instanceof Comment);
    }
    
    @Test
    public void testInsert_characterToken() {
        Token.Character characterToken = new Token.Character("Text");
        
        xmlTreeBuilder.insert(characterToken);
        
        // Assert statements
        assertEquals(1, xmlTreeBuilder.doc.childNodes().size());
        assertTrue(xmlTreeBuilder.doc.childNode(0) instanceof TextNode);
    }
    
    @Test
    public void testInsert_doctypeToken() {
        Token.Doctype doctypeToken = new Token.Doctype("html", "", "", "");
        
        xmlTreeBuilder.insert(doctypeToken);
        
        // Assert statements
        assertEquals(1, xmlTreeBuilder.doc.childNodes().size());
        assertTrue(xmlTreeBuilder.doc.childNode(0) instanceof DocumentType);
    }
    
    @Test
    public void testPopStackToClose_elementFound() {
        Token.EndTag endTag = new Token.EndTag("tag1");
        
        xmlTreeBuilder.stack.add(new Element("tag1"));
        
        xmlTreeBuilder.popStackToClose(endTag);
        
        // Assert statements
        assertEquals(0, xmlTreeBuilder.stack.size());
    }
    
    @Test
    public void testPopStackToClose_elementNotFound() {
        Token.EndTag endTag = new Token.EndTag("tag2");
        
        xmlTreeBuilder.stack.add(new Element("tag1"));
        
        xmlTreeBuilder.popStackToClose(endTag);
        
        // Assert statements
        assertEquals(1, xmlTreeBuilder.stack.size());
    }
    
    @Test
    public void testParseFragment() {
        String inputFragment = "<tag1>Text</tag1>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors);
        
        // Assert statements
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
    }
}