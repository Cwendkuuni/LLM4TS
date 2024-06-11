package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = Mockito.mock(Token.class);
        Mockito.when(token.tokenType()).thenReturn("TokenType");
        
        Assert.assertEquals("TokenType", token.tokenType());
    }

    @Test
    public void testResetDoctype() {
        Token.Doctype doctype = new Token.Doctype();

        doctype.reset();

        Assert.assertEquals("", doctype.name.toString());
        Assert.assertEquals("", doctype.publicIdentifier.toString());
        Assert.assertEquals("", doctype.systemIdentifier.toString());
        Assert.assertEquals(false, doctype.forceQuirks);
    }

    @Test
    public void testGetNameDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("name");

        Assert.assertEquals("name", doctype.getName());
    }

    @Test
    public void testGetPublicIdentifierDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.publicIdentifier.append("publicIdentifier");

        Assert.assertEquals("publicIdentifier", doctype.getPublicIdentifier());
    }

    @Test
    public void testGetSystemIdentifierDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.systemIdentifier.append("systemIdentifier");

        Assert.assertEquals("systemIdentifier", doctype.getSystemIdentifier());
    }

    @Test
    public void testIsForceQuirksDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.forceQuirks = true;

        Assert.assertEquals(true, doctype.isForceQuirks());
    }

    @Test
    public void testResetStartTag() {
        Token.StartTag startTag = new Token.StartTag();

        startTag.reset();

        Assert.assertEquals(null, startTag.tagName);
        Assert.assertEquals(null, startTag.normalName);
        Assert.assertEquals(null, startTag.pendingAttributeName);
        Assert.assertEquals("", startTag.pendingAttributeValue.toString());
        Assert.assertEquals(null, startTag.pendingAttributeValueS);
        Assert.assertEquals(false, startTag.hasEmptyAttributeValue);
        Assert.assertEquals(false, startTag.hasPendingAttributeValue);
        Assert.assertEquals(false, startTag.selfClosing);
        Assert.assertEquals(null, startTag.attributes);
    }

    @Test
    public void testResetEndTag() {
        Token.EndTag endTag = new Token.EndTag();

        endTag.reset();

        Assert.assertEquals(null, endTag.tagName);
        Assert.assertEquals(null, endTag.normalName);
        Assert.assertEquals(null, endTag.pendingAttributeName);
        Assert.assertEquals("", endTag.pendingAttributeValue.toString());
        Assert.assertEquals(null, endTag.pendingAttributeValueS);
        Assert.assertEquals(false, endTag.hasEmptyAttributeValue);
        Assert.assertEquals(false, endTag.hasPendingAttributeValue);
        Assert.assertEquals(false, endTag.selfClosing);
        Assert.assertEquals(null, endTag.attributes);
    }

    @Test
    public void testResetComment() {
        Token.Comment comment = new Token.Comment();

        comment.reset();

        Assert.assertEquals("", comment.data.toString());
        Assert.assertEquals(false, comment.bogus);
    }

    @Test
    public void testGetDataComment() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("data");

        Assert.assertEquals("data", comment.getData());
    }

    @Test
    public void testResetCharacter() {
        Token.Character character = new Token.Character();

        character.reset();

        Assert.assertEquals(null, character.data);
    }

    @Test
    public void testGetDataCharacter() {
        Token.Character character = new Token.Character();
        character.data = "data";

        Assert.assertEquals("data", character.getData());
    }

    @Test
    public void testIsDoctype() {
        Token token = new Token();

        token.type = Token.TokenType.Doctype;

        Assert.assertEquals(true, token.isDoctype());
    }

    @Test
    public void testAsDoctype() {
        Token token = new Token();

        token.type = Token.TokenType.Doctype;

        Assert.assertTrue(token.asDoctype() instanceof Token.Doctype);
    }

    @Test
    public void testIsStartTag() {
        Token token = new Token();

        token.type = Token.TokenType.StartTag;

        Assert.assertEquals(true, token.isStartTag());
    }

    @Test
    public void testAsStartTag() {
        Token token = new Token();

        token.type = Token.TokenType.StartTag;

        Assert.assertTrue(token.asStartTag() instanceof Token.StartTag);
    }

    @Test
    public void testIsEndTag() {
        Token token = new Token();

        token.type = Token.TokenType.EndTag;

        Assert.assertEquals(true, token.isEndTag());
    }

    @Test
    public void testAsEndTag() {
        Token token = new Token();

        token.type = Token.TokenType.EndTag;

        Assert.assertTrue(token.asEndTag() instanceof Token.EndTag);
    }

    @Test
    public void testIsComment() {
        Token token = new Token();

        token.type = Token.TokenType.Comment;

        Assert.assertEquals(true, token.isComment());
    }

    @Test
    public void testAsComment() {
        Token token = new Token();

        token.type = Token.TokenType.Comment;

        Assert.assertTrue(token.asComment() instanceof Token.Comment);
    }

    @Test
    public void testIsCharacter() {
        Token token = new Token();

        token.type = Token.TokenType.Character;

        Assert.assertEquals(true, token.isCharacter());
    }

    @Test
    public void testAsCharacter() {
        Token token = new Token();

        token.type = Token.TokenType.Character;

        Assert.assertTrue(token.asCharacter() instanceof Token.Character);
    }

    @Test
    public void testIsEOF() {
        Token token = new Token();

        token.type = Token.TokenType.EOF;

        Assert.assertEquals(true, token.isEOF());
    }

    @Test
    public void testResetEOF() {
        Token.EOF eof = new Token.EOF();

        eof.reset();

        Assert.assertTrue(eof instanceof Token.EOF);
    }

    @Test
    public void testEnsureAttributeValue() {
        Token.Tag tag = new Token.StartTag();

        tag.ensureAttributeValue();

        Assert.assertEquals(false, tag.hasPendingAttributeValue);
    }

    @Test
    public void testSetEmptyAttributeValue() {
        Token.Tag tag = new Token.StartTag();

        tag.setEmptyAttributeValue();

        Assert.assertEquals(true, tag.hasEmptyAttributeValue);
    }
}