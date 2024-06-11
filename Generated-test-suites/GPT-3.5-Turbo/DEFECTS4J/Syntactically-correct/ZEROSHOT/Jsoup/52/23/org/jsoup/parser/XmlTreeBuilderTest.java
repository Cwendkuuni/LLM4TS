package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;

import java.util.List;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder xmlTreeBuilder;
    
    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }
    
    @Test
    public void testInitialiseParse() {
        String input = "";
        String baseUri = "";
        ParseErrorList errors = new ParseErrorList();
        
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);
        
        // Assert statements
        assertEquals(xmlTreeBuilder.getStack().size(), 1);
        assertEquals(xmlTreeBuilder.getStack().get(0), xmlTreeBuilder.getDoc());
        assertEquals(xmlTreeBuilder.getDoc().outputSettings().syntax(), Document.OutputSettings.Syntax.xml);
    }
    
    @Test
    public void testProcess_StartTag() {
        Token startTagToken = new Token(Token.TokenType.StartTag, "tag");
        
        boolean result = xmlTreeBuilder.process(startTagToken);
        
        // Assert statements
        assertTrue(result);
        assertEquals(xmlTreeBuilder.getStack().size(), 2);
        assertEquals(xmlTreeBuilder.getCurrentElement(), xmlTreeBuilder.getStack().get(1));
    }
    
    @Test
    public void testProcess_EndTag() {
        Tag tag = Tag.valueOf("tag");
        Element element = new Element(tag, "", null);
        xmlTreeBuilder.getStack().add(element);
        Token endTagToken = new Token(Token.TokenType.EndTag, "tag");
        
        boolean result = xmlTreeBuilder.process(endTagToken);
        
        // Assert statements
        assertTrue(result);
        assertEquals(xmlTreeBuilder.getStack().size(), 1);
    }
    
    @Test
    public void testProcess_Comment() {
        Token.Comment commentToken = new Token.Comment("comment", "");
        
        boolean result = xmlTreeBuilder.process(commentToken);
        
        // Assert statements
        assertTrue(result);
        assertEquals(xmlTreeBuilder.getCurrentElement().childNode(0).outerHtml(), "<!--comment-->");
    }
    
    @Test
    public void testProcess_Character() {
        Token.Character characterToken = new Token.Character("data");
        
        boolean result = xmlTreeBuilder.process(characterToken);
        
        // Assert statements
        assertTrue(result);
        assertEquals(xmlTreeBuilder.getCurrentElement().childNode(0).outerHtml(), "data");
    }
    
    @Test
    public void testProcess_Doctype() {
        Token.Doctype doctypeToken = new Token.Doctype("name", "", "", "");
        
        boolean result = xmlTreeBuilder.process(doctypeToken);
        
        // Assert statements
        assertTrue(result);
        assertEquals(xmlTreeBuilder.getCurrentElement().childNode(0).outerHtml(), "<!DOCTYPE name>");
    }
    
    @Test
    public void testInsertNode() {
        Element element = new Element(Tag.valueOf("tag"), "", null);
        xmlTreeBuilder.getStack().add(element);
        Node node = new TextNode("text", "");
        
        xmlTreeBuilder.insertNode(node);
        
        // Assert statements
        assertEquals(xmlTreeBuilder.getCurrentElement().childNode(0).outerHtml(), "text");
    }
    
    @Test
    public void testInsert_StartTag_SelfClosing() {
        Token.StartTag startTag = new Token.StartTag("tag");
        startTag.selfClosing = true;
        
        Element result = xmlTreeBuilder.insert(startTag);
        
        // Assert statements
        assertTrue(result.isBlock());
        assertTrue(result.tag().isKnownTag());
        assertTrue(result.tag().isSelfClosing());
    }
    
    @Test
    public void testInsert_StartTag_NotSelfClosing() {
        Tag tag = Tag.valueOf("tag");
        xmlTreeBuilder.getStack().add(new Element(tag, "", null));
        
        Token.StartTag startTag = new Token.StartTag("tag");
        startTag.selfClosing = false;
        
        Element result = xmlTreeBuilder.insert(startTag);
        
        // Assert statements
        assertEquals(result, xmlTreeBuilder.getCurrentElement());
    }
    
    @Test
    public void testInsert_Comment_Bogus() {
        Token.Comment commentToken = new Token.Comment("<!-- comment -->", "");
        
        xmlTreeBuilder.insert(commentToken);
        
        // Assert statements
        assertEquals(xmlTreeBuilder.getCurrentElement().childNode(0).outerHtml(), "<!DOCTYPE comment>");
    }
    
    @Test
    public void testInsert_Comment_NotBogus() {
        Token.Comment commentToken = new Token.Comment("comment", "");
        
        xmlTreeBuilder.insert(commentToken);
        
        // Assert statements
        assertEquals(xmlTreeBuilder.getCurrentElement().childNode(0).outerHtml(), "<!--comment-->");
    }
    
    @Test
    public void testInsert_Character() {
        Token.Character characterToken = new Token.Character("data");
        
        xmlTreeBuilder.insert(characterToken);
        
        // Assert statements
        assertEquals(xmlTreeBuilder.getCurrentElement().childNode(0).outerHtml(), "data");
    }
    
    @Test
    public void testInsert_Doctype() {
        Token.Doctype doctypeToken = new Token.Doctype("name", "", "", "");
        
        xmlTreeBuilder.insert(doctypeToken);
        
        // Assert statements
        assertEquals(xmlTreeBuilder.getCurrentElement().childNode(0).outerHtml(), "<!DOCTYPE name>");
    }
    
    @Test
    public void testPopStackToClose_ElementExists() {
        Tag tag = Tag.valueOf("tag");
        Element element = new Element(tag, "", null);
        xmlTreeBuilder.getStack().add(element);
        
        Token.EndTag endTagToken = new Token.EndTag("tag");
        
        xmlTreeBuilder.popStackToClose(endTagToken);
        
        // Assert statements
        assertEquals(xmlTreeBuilder.getStack().size(), 0);
    }
    
    @Test
    public void testPopStackToClose_ElementDoesNotExist() {
        Tag tag1 = Tag.valueOf("tag1");
        Element element1 = new Element(tag1, "", null);
        xmlTreeBuilder.getStack().add(element1);
        
        Tag tag2 = Tag.valueOf("tag2");
        Element element2 = new Element(tag2, "", null);
        xmlTreeBuilder.getStack().add(element2);
        
        Token.EndTag endTagToken = new Token.EndTag("tag3");
        
        xmlTreeBuilder.popStackToClose(endTagToken);
        
        // Assert statements
        assertEquals(xmlTreeBuilder.getStack().size(), 2);
    }
    
    @Test
    public void testParseFragment() {
        String inputFragment = "<tag></tag>";
        String baseUri = "";
        ParseErrorList errors = new ParseErrorList();
        
        List<Node> result = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors);
        
        // Assert statements
        assertEquals(result.size(), 1);
        assertEquals(result.get(0).outerHtml(), "<tag></tag>");
    }
    
}