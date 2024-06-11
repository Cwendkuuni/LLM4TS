package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new Token.Doctype();
        Assert.assertEquals("Doctype", token.tokenType());
    }

    @Test
    public void testDoctypeGetName() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("html");
        Assert.assertEquals("html", doctype.getName());
    }

    @Test
    public void testDoctypeGetPublicIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.publicIdentifier.append("public");
        Assert.assertEquals("public", doctype.getPublicIdentifier());
    }

    @Test
    public void testDoctypeGetSystemIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.systemIdentifier.append("system");
        Assert.assertEquals("system", doctype.getSystemIdentifier());
    }

    @Test
    public void testDoctypeIsForceQuirks() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.forceQuirks = true;
        Assert.assertTrue(doctype.isForceQuirks());
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
        startTag.attributes = new Attributes();
        Assert.assertNotNull(startTag.getAttributes());
    }

    @Test
    public void testStartTagAppendTagName() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.appendTagName("div");
        Assert.assertEquals("div", startTag.tagName);
    }

    @Test
    public void testStartTagAppendAttributeName() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.appendAttributeName("class");
        Assert.assertEquals("class", startTag.pendingAttributeName);
    }

    @Test
    public void testStartTagAppendAttributeValue() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.appendAttributeValue("container");
        Assert.assertEquals("container", startTag.pendingAttributeValue.toString());
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
        Token.Character character = new Token.Character("This is a character");
        Assert.assertEquals("This is a character", character.getData());
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
        Assert.assertEquals("Doctype", doctype.getClass().getSimpleName());
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
        Assert.assertEquals("StartTag", startTag.getClass().getSimpleName());
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
        Assert.assertEquals("EndTag", endTag.getClass().getSimpleName());
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
        Assert.assertEquals("Comment", comment.getClass().getSimpleName());
    }

    @Test
    public void testIsCharacter() {
        Token token = new Token.Character("character");
        Assert.assertTrue(token.isCharacter());
    }

    @Test
    public void testAsCharacter() {
        Token token = new Token.Character("character");
        Token.Character character = token.asCharacter();
        Assert.assertEquals("Character", character.getClass().getSimpleName());
    }

    @Test
    public void testIsEOF() {
        Token token = new Token.EOF();
        Assert.assertTrue(token.isEOF());
    }

    @Test
    public void testTokenTypeEnums() {
        Assert.assertEquals(Token.TokenType.Doctype, Token.TokenType.valueOf("Doctype"));
        Assert.assertEquals(Token.TokenType.StartTag, Token.TokenType.valueOf("StartTag"));
        Assert.assertEquals(Token.TokenType.EndTag, Token.TokenType.valueOf("EndTag"));
        Assert.assertEquals(Token.TokenType.Comment, Token.TokenType.valueOf("Comment"));
        Assert.assertEquals(Token.TokenType.Character, Token.TokenType.valueOf("Character"));
        Assert.assertEquals(Token.TokenType.EOF, Token.TokenType.valueOf("EOF"));
    }
}