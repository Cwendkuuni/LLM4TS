package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

public class XmlTreeBuilderTest {
    
    private XmlTreeBuilder xmlTreeBuilder;
    
    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }
    
    @Test
    public void testInitialiseParse() {
        xmlTreeBuilder.initialiseParse("input", "baseUri", new ParseErrorList());
        //assertions
    }
    
    @Test
    public void testProcess_StartTag() {
        Token token = new Token(Token.TokenType.StartTag);
        xmlTreeBuilder.process(token);
        //assertions
    }
    
    @Test
    public void testProcess_EndTag() {
        Token token = new Token(Token.TokenType.EndTag);
        xmlTreeBuilder.process(token);
        //assertions
    }
    
    @Test
    public void testProcess_Comment() {
        Token token = new Token(Token.TokenType.Comment);
        xmlTreeBuilder.process(token);
        //assertions
    }
    
    @Test
    public void testProcess_Character() {
        Token token = new Token(Token.TokenType.Character);
        xmlTreeBuilder.process(token);
        //assertions
    }
    
    @Test
    public void testProcess_Doctype() {
        Token token = new Token(Token.TokenType.Doctype);
        xmlTreeBuilder.process(token);
        //assertions
    }
    
    @Test
    public void testInsertNode() {
        Node node = new Node();
        xmlTreeBuilder.insertNode(node);
        //assertions
    }
    
    @Test
    public void testInsert_StartTag() {
        Token.StartTag startTag = new Token.StartTag();
        Element el = xmlTreeBuilder.insert(startTag);
        //assertions
    }
    
    @Test
    public void testInsert_Comment() {
        Token.Comment commentToken = new Token.Comment();
        xmlTreeBuilder.insert(commentToken);
        //assertions
    }
    
    @Test
    public void testInsert_Character() {
        Token.Character characterToken = new Token.Character();
        xmlTreeBuilder.insert(characterToken);
        //assertions
    }
    
    @Test
    public void testInsert_Doctype() {
        Token.Doctype d = new Token.Doctype();
        xmlTreeBuilder.insert(d);
        //assertions
    }
    
    @Test
    public void testPopStackToClose() {
        Token.EndTag endTag = new Token.EndTag();
        xmlTreeBuilder.popStackToClose(endTag);
        //assertions
    }
}