package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class XmlTreeBuilderTest {
    
    private XmlTreeBuilder xmlTreeBuilder;
    private Document doc;
    
    @Before
    public void setup() {
        xmlTreeBuilder = new XmlTreeBuilder();
        doc = Jsoup.parse("<root></root>", "", Parser.xmlParser());
    }
    
    @Test
    public void testInitialiseParse() {
        xmlTreeBuilder.initialiseParse("", "", new ParseErrorList());
        assertEquals(doc, xmlTreeBuilder.stack.get(0));
        assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.doc.outputSettings().syntax());
    }
    
    @Test
    public void testProcess_StartTag() {
        Token.StartTag startTag = new Token.StartTag("tag", new Attributes());
        Element expectedElement = new Element(Tag.valueOf("tag"), "", new Attributes());
        
        xmlTreeBuilder.process(startTag);
        
        assertEquals(expectedElement, xmlTreeBuilder.stack.get(1));
    }
    
    @Test
    public void testProcess_EndTag() {
        Token.EndTag endTag = new Token.EndTag("tag");
        Element element = new Element(Tag.valueOf("tag"), "", new Attributes());
        xmlTreeBuilder.stack.add(element);
        
        xmlTreeBuilder.process(endTag);
        
        assertTrue(xmlTreeBuilder.stack.isEmpty());
    }
    
    @Test
    public void testProcess_Comment() {
        Token.Comment comment = new Token.Comment("comment", "");
        Comment expectedComment = new Comment("comment", "");
        
        xmlTreeBuilder.process(comment);
        
        assertEquals(expectedComment, xmlTreeBuilder.stack.get(0).childNodes().get(0));
    }
    
    @Test
    public void testProcess_Character() {
        Token.Character character = new Token.Character("data");
        TextNode expectedNode = new TextNode("data", "");
        
        xmlTreeBuilder.process(character);
        
        assertEquals(expectedNode, xmlTreeBuilder.stack.get(0).childNodes().get(0));
    }
    
    @Test
    public void testProcess_Doctype() {
        Token.Doctype doctype = new Token.Doctype("html");
        DocumentType expectedDoctype = new DocumentType("html", "", "", "");
        
        xmlTreeBuilder.process(doctype);
        
        assertEquals(expectedDoctype, xmlTreeBuilder.stack.get(0).childNodes().get(0));
    }
    
    @Test
    public void testInsertNode() {
        Node node = new TextNode("data", "");
        Element element = new Element(Tag.valueOf("tag"), "", new Attributes());
        element.appendChild(node);
        
        xmlTreeBuilder.insertNode(node);
        
        assertEquals(element, xmlTreeBuilder.currentElement());
    }
    
    @Test
    public void testInsert_StartTag() {
        Token.StartTag startTag = new Token.StartTag("tag", new Attributes());
        Element expectedElement = new Element(Tag.valueOf("tag"), "", new Attributes());
        
        Element element = xmlTreeBuilder.insert(startTag);
        
        assertEquals(expectedElement, element);
        assertEquals(expectedElement, xmlTreeBuilder.stack.get(1));
    }
    
    @Test
    public void testInsert_SelfClosingTag() {
        Token.StartTag selfClosingTag = new Token.StartTag("tag", new Attributes());
        selfClosingTag.selfClosing(true);
        Element expectedElement = new Element(Tag.valueOf("tag"), "", new Attributes());
        
        Element element = xmlTreeBuilder.insert(selfClosingTag);
        
        assertEquals(expectedElement, element);
        assertTrue(element.tag().isKnownTag());
    }
    
    @Test
    public void testInsert_Comment() {
        Token.Comment comment = new Token.Comment("comment", "");
        Comment expectedComment = new Comment("comment", "");
        
        xmlTreeBuilder.insert(comment);
        
        assertEquals(expectedComment, xmlTreeBuilder.stack.get(0).childNodes().get(0));
    }
    
    @Test
    public void testInsert_Character() {
        Token.Character character = new Token.Character("data");
        TextNode expectedNode = new TextNode("data", "");
        
        xmlTreeBuilder.insert(character);
        
        assertEquals(expectedNode, xmlTreeBuilder.stack.get(0).childNodes().get(0));
    }
    
    @Test
    public void testInsert_Doctype() {
        Token.Doctype doctype = new Token.Doctype("html");
        DocumentType expectedDoctype = new DocumentType("html", "", "", "");
        
        xmlTreeBuilder.insert(doctype);
        
        assertEquals(expectedDoctype, xmlTreeBuilder.stack.get(0).childNodes().get(0));
    }
    
    @Test
    public void testPopStackToClose_ElementFound() {
        Token.EndTag endTag = new Token.EndTag("tag");
        Element element = new Element(Tag.valueOf("tag"), "", new Attributes());
        xmlTreeBuilder.stack.add(element);
        
        xmlTreeBuilder.popStackToClose(endTag);
        
        assertTrue(xmlTreeBuilder.stack.isEmpty());
    }
    
    @Test
    public void testPopStackToClose_ElementNotFound() {
        Token.EndTag endTag = new Token.EndTag("tag");
        Element element = new Element(Tag.valueOf("otherTag"), "", new Attributes());
        xmlTreeBuilder.stack.add(element);
        
        xmlTreeBuilder.popStackToClose(endTag);
        
        assertFalse(xmlTreeBuilder.stack.isEmpty());
    }
    
    @Test
    public void testParseFragment() {
        String inputFragment = "<tag><child></child></tag>";
        List<Node> expectedNodes = xmlTreeBuilder.doc.childNodes();
        
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, "", new ParseErrorList());
        
        assertEquals(expectedNodes, nodes);
    }
}