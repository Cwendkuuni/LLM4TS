package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert;

public class TokenTest {
    
    @Test
    public void testTokenType() {
        Token token = new Token.Doctype();
        Assert.assertEquals("Doctype", token.tokenType());
    }
    
    @Test
    public void testDoctypeGetName() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("HTML");
        Assert.assertEquals("HTML", doctype.getName());
    }
    
    @Test
    public void testDoctypeGetPublicIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.publicIdentifier.append("//public");
        Assert.assertEquals("//public", doctype.getPublicIdentifier());
    }
    
    @Test
    public void testDoctypeGetSystemIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.systemIdentifier.append("//system");
        Assert.assertEquals("//system", doctype.getSystemIdentifier());
    }
    
    @Test
    public void testDoctypeIsForceQuirks() {
        Token.Doctype doctype = new Token.Doctype();
        Assert.assertFalse(doctype.isForceQuirks());
    }
    
    @Test
    public void testStartTagName() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";
        Assert.assertEquals("div", startTag.name());
    }
    
    @Test
    public void testStartTagIsSelfClosing() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.selfClosing = true;
        Assert.assertTrue(startTag.isSelfClosing());
    }
    
    @Test
    public void testStartTagGetAttributes() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.attributes = new org.jsoup.nodes.Attributes();
        Assert.assertNotNull(startTag.getAttributes());
    }
    
    @Test
    public void testEndTagName() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "div";
        Assert.assertEquals("div", endTag.name());
    }
    
    @Test
    public void testCommentGetData() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("This is a comment");
        Assert.assertEquals("This is a comment", comment.getData());
    }
    
    @Test
    public void testCharacterGetData() {
        Token.Character character = new Token.Character("Hello");
        Assert.assertEquals("Hello", character.getData());
    }
    
    @Test
    public void testEOF() {
        Token.EOF eof = new Token.EOF();
        Assert.assertNotNull(eof);
    }
    
    @Test
    public void testIsDoctype() {
        Token token = new Token.Doctype();
        Assert.assertTrue(token.isDoctype());
    }
    
    @Test
    public void testAsDoctype() {
        Token token = new Token.Doctype();
        Token.Doctype doctype = token.asDoctype();
        Assert.assertNotNull(doctype);
    }
    
    @Test
    public void testIsStartTag() {
        Token token = new Token.StartTag();
        Assert.assertTrue(token.isStartTag());
    }
    
    @Test
    public void testAsStartTag() {
        Token token = new Token.StartTag();
        Token.StartTag startTag = token.asStartTag();
        Assert.assertNotNull(startTag);
    }
    
    @Test
    public void testIsEndTag() {
        Token token = new Token.EndTag();
        Assert.assertTrue(token.isEndTag());
    }
    
    @Test
    public void testAsEndTag() {
        Token token = new Token.EndTag();
        Token.EndTag endTag = token.asEndTag();
        Assert.assertNotNull(endTag);
    }
    
    @Test
    public void testIsComment() {
        Token token = new Token.Comment();
        Assert.assertTrue(token.isComment());
    }
    
    @Test
    public void testAsComment() {
        Token token = new Token.Comment();
        Token.Comment comment = token.asComment();
        Assert.assertNotNull(comment);
    }
    
    @Test
    public void testIsCharacter() {
        Token token = new Token.Character("Hello");
        Assert.assertTrue(token.isCharacter());
    }
    
    @Test
    public void testAsCharacter() {
        Token token = new Token.Character("Hello");
        Token.Character character = token.asCharacter();
        Assert.assertNotNull(character);
    }
    
    @Test
    public void testIsEOF() {
        Token token = new Token.EOF();
        Assert.assertTrue(token.isEOF());
    }
    
}