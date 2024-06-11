package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

public class XmlTreeBuilderTest {
    
    private XmlTreeBuilder xmlTreeBuilder;
    
    @Before
    public void setup() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }
    
    @Test
    public void testInitialiseParse() {
        String input = "<xml><tag1>text</tag1><tag2><tag3>text</tag3></tag2></xml>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);
        
        assertEquals("http://example.com", xmlTreeBuilder.baseUri);
        assertEquals(2, xmlTreeBuilder.doc.childNodes().size());
    }
    
    @Test
    public void testProcess_startTag() {
        Token token = new Token.StartTag("tag1");
        
        boolean result = xmlTreeBuilder.process(token);
        
        assertTrue(result);
        assertEquals(1, xmlTreeBuilder.doc.childNodes().size());
    }
    
    @Test
    public void testProcess_endTag() {
        Token token = new Token.EndTag("tag1");
        
        boolean result = xmlTreeBuilder.process(token);
        
        assertTrue(result);
        assertEquals(0, xmlTreeBuilder.doc.childNodes().size());
    }
    
    @Test
    public void testProcess_comment() {
        Token token = new Token.Comment("this is a comment");
        
        boolean result = xmlTreeBuilder.process(token);
        
        assertTrue(result);
        assertEquals(1, xmlTreeBuilder.doc.childNodes().size());
    }
    
    @Test
    public void testProcess_character() {
        Token token = new Token.Character("text");
        
        boolean result = xmlTreeBuilder.process(token);
        
        assertTrue(result);
        assertEquals(1, xmlTreeBuilder.doc.childNodes().size());
    }
    
    @Test
    public void testProcess_doctype() {
        Token token = new Token.Doctype("html");
        
        boolean result = xmlTreeBuilder.process(token);
        
        assertTrue(result);
        assertEquals(1, xmlTreeBuilder.doc.childNodes().size());
    }
    
    @Test
    public void testInsertNode() {
        Node node = new Element(Tag.valueOf("tag1"), "http://example.com", null);
        
        xmlTreeBuilder.insertNode(node);
        
        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
    }
    
    @Test
    public void testInsert_startTag() {
        Token.StartTag startTag = new Token.StartTag("tag1");
        
        Element element = xmlTreeBuilder.insert(startTag);
        
        assertEquals("tag1", element.nodeName());
        assertEquals(1, xmlTreeBuilder.doc.childNodes().size());
        assertEquals(1, xmlTreeBuilder.stack.size());
    }
    
    @Test
    public void testInsert_comment() {
        Token.Comment commentToken = new Token.Comment("this is a comment");
        
        xmlTreeBuilder.insert(commentToken);
        
        assertEquals(1, xmlTreeBuilder.doc.childNodes().size());
    }
    
    @Test
    public void testInsert_character() {
        Token.Character characterToken = new Token.Character("text");
        
        xmlTreeBuilder.insert(characterToken);
        
        assertEquals(1, xmlTreeBuilder.doc.childNodes().size());
    }
    
    @Test
    public void testInsert_doctype() {
        Token.Doctype doctype = new Token.Doctype("html");
        
        xmlTreeBuilder.insert(doctype);
        
        assertEquals(1, xmlTreeBuilder.doc.childNodes().size());
    }
    
    @Test
    public void testPopStackToClose() {
        Token.EndTag endTag = new Token.EndTag("tag1");
        Element element = new Element(Tag.valueOf("tag1"), "http://example.com", null);
        xmlTreeBuilder.stack.add(element);
        
        xmlTreeBuilder.popStackToClose(endTag);
        
        assertEquals(0, xmlTreeBuilder.stack.size());
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<tag1>text</tag1><tag2><tag3>text</tag3></tag2>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors);
        
        assertEquals(2, nodes.size());
    }
}