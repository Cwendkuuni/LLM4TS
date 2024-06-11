package org.jsoup.parser;

import org.junit.Test;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {
    
    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.initialiseParse("input", "baseUri", null);
    }
    
    @Test
    public void testProcess() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.process(Token.StartTag);
        builder.process(Token.EndTag);
        builder.process(Token.Comment);
        builder.process(Token.Character);
        builder.process(Token.Doctype);
        builder.process(Token.EOF);
    }
    
    @Test
    public void testInsertNode() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Node node = new Node();
        builder.insertNode(node);
    }
    
    @Test
    public void testInsert() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag();
        builder.insert(startTag);
    }
    
    @Test
    public void testInsertComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Comment commentToken = new Token.Comment();
        builder.insert(commentToken);
    }
    
    @Test
    public void testInsertCharacter() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Character characterToken = new Token.Character();
        builder.insert(characterToken);
    }
    
    @Test
    public void testInsertDoctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Doctype doctype = new Token.Doctype();
        builder.insert(doctype);
    }
    
    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.EndTag endTag = new Token.EndTag();
        builder.popStackToClose(endTag);
    }
    
    @Test
    public void testParseFragment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        List<Node> nodes = builder.parseFragment("inputFragment", "baseUri", null);
    }
}