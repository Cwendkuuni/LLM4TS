package org.jsoup.parser;

import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.nodes.*;
import org.jsoup.parser.Token.*;

public class XmlTreeBuilderTest {
    
    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.initialiseParse("", "", new ParseErrorList());
        
        assertEquals(1, builder.stack.size());
        assertEquals(builder.doc, builder.stack.peek());
    }
    
    @Test
    public void testProcess() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        
        // StartTag token
        StartTag startTag = new StartTag("tag", "");
        builder.process(startTag);
        
        assertEquals("tag", builder.currentElement().nodeName());
        
        // EndTag token
        EndTag endTag = new EndTag("tag");
        builder.process(endTag);
        
        assertTrue(builder.stack.isEmpty());
        
        // Comment token
        Comment comment = new Comment("comment", "");
        builder.process(comment);
        
        assertEquals(1, builder.currentElement().childNodeSize());
        assertTrue(builder.currentElement().childNode(0) instanceof Comment);
        
        // Character token
        Character character = new Character("character");
        builder.process(character);
        
        assertEquals(2, builder.currentElement().childNodeSize());
        assertTrue(builder.currentElement().childNode(1) instanceof TextNode);
        
        // Doctype token
        Doctype doctype = new Doctype("doctype", "", "", "");
        builder.process(doctype);
        
        assertEquals(3, builder.currentElement().childNodeSize());
        assertTrue(builder.currentElement().childNode(2) instanceof DocumentType);
        
        // EOF token
        EOF eof = new EOF();
        builder.process(eof);
    }
    
    @Test
    public void testInsertNode() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        
        Element element = new Element("element");
        builder.insertNode(element);
        
        assertEquals(1, builder.currentElement().childNodeSize());
        assertEquals(element, builder.currentElement().childNode(0));
    }
    
    @Test
    public void testInsert() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        
        StartTag startTag = new StartTag("tag", "");
        Element element = builder.insert(startTag);
        
        assertEquals("tag", element.nodeName());
        assertEquals(1, builder.currentElement().childNodeSize());
        assertEquals(element, builder.currentElement().childNode(0));
        
        EndTag endTag = new EndTag("tag");
        builder.insert(endTag);
        
        assertTrue(builder.stack.isEmpty());
    }
    
    @Test
    public void testInsert_Comment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        
        Comment commentToken = new Comment("comment", "");
        builder.insert(commentToken);
        
        assertEquals(1, builder.currentElement().childNodeSize());
        assertTrue(builder.currentElement().childNode(0) instanceof Comment);
    }
    
    @Test
    public void testInsert_Character() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        
        Character characterToken = new Character("character");
        builder.insert(characterToken);
        
        assertEquals(1, builder.currentElement().childNodeSize());
        assertTrue(builder.currentElement().childNode(0) instanceof TextNode);
    }
    
    @Test
    public void testInsert_Doctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        
        Doctype doctype = new Doctype("doctype", "", "", "");
        builder.insert(doctype);
        
        assertEquals(1, builder.currentElement().childNodeSize());
        assertTrue(builder.currentElement().childNode(0) instanceof DocumentType);
    }
    
    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        
        StartTag startTag1 = new StartTag("tag", "");
        builder.insert(startTag1);
        
        StartTag startTag2 = new StartTag("tag", "");
        builder.insert(startTag2);
        
        EndTag endTag = new EndTag("tag");
        builder.popStackToClose(endTag);
        
        assertEquals(1, builder.stack.size());
        assertEquals(startTag1.name(), builder.stack.peek().nodeName());
        
        builder.popStackToClose(endTag);
        
        assertTrue(builder.stack.isEmpty());
    }
}