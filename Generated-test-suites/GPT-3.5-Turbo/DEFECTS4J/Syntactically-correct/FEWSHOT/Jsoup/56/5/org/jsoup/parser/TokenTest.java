package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.BooleanAttribute;
import org.junit.Assert;
import org.junit.Test;

public class TokenTest {

    @Test
    public void testReset_Doctype() {
        Token token = new Token.Doctype();
        token.type = Token.TokenType.Doctype;
        ((Token.Doctype) token).name.append("name");
        ((Token.Doctype) token).publicIdentifier.append("publicIdentifier");
        ((Token.Doctype) token).systemIdentifier.append("systemIdentifier");
        ((Token.Doctype) token).forceQuirks = true;

        Token result = token.reset();

        Assert.assertEquals(Token.TokenType.Doctype, result.type);
        Token.Doctype doctypeResult = (Token.Doctype) result;
        Assert.assertEquals("", doctypeResult.name.toString());
        Assert.assertEquals("", doctypeResult.publicIdentifier.toString());
        Assert.assertEquals("", doctypeResult.systemIdentifier.toString());
        Assert.assertFalse(doctypeResult.forceQuirks);
    }

    @Test
    public void testReset_StartTag() {
        Token token = new Token.StartTag();
        token.type = Token.TokenType.StartTag;
        token.tagName = "tagName";
        token.normalName = "normalName";
        ((Token.Tag) token).pendingAttributeName = "pendingAttributeName";
        ((Token.Tag) token).pendingAttributeValue.append("pendingAttributeValue");
        ((Token.Tag) token).pendingAttributeValueS = "pendingAttributeValueS";
        ((Token.Tag) token).hasEmptyAttributeValue = true;
        ((Token.Tag) token).hasPendingAttributeValue = true;
        ((Token.Tag) token).selfClosing = true;
        ((Token.Tag) token).attributes = new Attributes();

        Token result = token.reset();

        Assert.assertEquals(Token.TokenType.StartTag, result.type);
        Token.StartTag startTagResult = (Token.StartTag) result;
        Assert.assertNull(startTagResult.tagName);
        Assert.assertNull(startTagResult.normalName);
        Assert.assertNull(((Token.Tag) startTagResult).pendingAttributeName);
        Assert.assertEquals("", ((Token.Tag) startTagResult).pendingAttributeValue.toString());
        Assert.assertNull(((Token.Tag) startTagResult).pendingAttributeValueS);
        Assert.assertFalse(((Token.Tag) startTagResult).hasEmptyAttributeValue);
        Assert.assertFalse(((Token.Tag) startTagResult).hasPendingAttributeValue);
        Assert.assertFalse(startTagResult.selfClosing);
        Assert.assertNull(((Token.Tag) startTagResult).attributes);
    }

    @Test
    public void testReset_EndTag() {
        Token token = new Token.EndTag();
        token.type = Token.TokenType.EndTag;
        token.tagName = "tagName";
        token.normalName = "normalName";
        ((Token.Tag) token).pendingAttributeName = "pendingAttributeName";
        ((Token.Tag) token).pendingAttributeValue.append("pendingAttributeValue");
        ((Token.Tag) token).pendingAttributeValueS = "pendingAttributeValueS";
        ((Token.Tag) token).hasEmptyAttributeValue = true;
        ((Token.Tag) token).hasPendingAttributeValue = true;
        ((Token.Tag) token).selfClosing = true;
        ((Token.Tag) token).attributes = new Attributes();

        Token result = token.reset();

        Assert.assertEquals(Token.TokenType.EndTag, result.type);
        Token.EndTag endTagResult = (Token.EndTag) result;
        Assert.assertNull(endTagResult.tagName);
        Assert.assertNull(endTagResult.normalName);
        Assert.assertNull(((Token.Tag) endTagResult).pendingAttributeName);
        Assert.assertEquals("", ((Token.Tag) endTagResult).pendingAttributeValue.toString());
        Assert.assertNull(((Token.Tag) endTagResult).pendingAttributeValueS);
        Assert.assertFalse(((Token.Tag) endTagResult).hasEmptyAttributeValue);
        Assert.assertFalse(((Token.Tag) endTagResult).hasPendingAttributeValue);
        Assert.assertFalse(endTagResult.selfClosing);
        Assert.assertNull(((Token.Tag) endTagResult).attributes);
    }

    @Test
    public void testReset_Comment() {
        Token token = new Token.Comment();
        token.type = Token.TokenType.Comment;
        ((Token.Comment) token).data.append("data");
        ((Token.Comment) token).bogus = true;

        Token result = token.reset();

        Assert.assertEquals(Token.TokenType.Comment, result.type);
        Token.Comment commentResult = (Token.Comment) result;
        Assert.assertEquals("", commentResult.data.toString());
        Assert.assertFalse(commentResult.bogus);
    }

    @Test
    public void testReset_Character() {
        Token token = new Token.Character();
        token.type = Token.TokenType.Character;
        ((Token.Character) token).data = "data";

        Token result = token.reset();

        Assert.assertEquals(Token.TokenType.Character, result.type);
        Token.Character characterResult = (Token.Character) result;
        Assert.assertNull(characterResult.data);
    }

    @Test
    public void testReset_EOF() {
        Token token = new Token.EOF();
        token.type = Token.TokenType.EOF;

        Token result = token.reset();

        Assert.assertEquals(Token.TokenType.EOF, result.type);
    }

    @Test
    public void testIsDoctype() {
        Token token = new Token.Doctype();
        token.type = Token.TokenType.Doctype;

        boolean result = token.isDoctype();

        Assert.assertTrue(result);
    }

    @Test
    public void testIsStartTag() {
        Token token = new Token.StartTag();
        token.type = Token.TokenType.StartTag;

        boolean result = token.isStartTag();

        Assert.assertTrue(result);
    }

    @Test
    public void testIsEndTag() {
        Token token = new Token.EndTag();
        token.type = Token.TokenType.EndTag;

        boolean result = token.isEndTag();

        Assert.assertTrue(result);
    }

    @Test
    public void testIsComment() {
        Token token = new Token.Comment();
        token.type = Token.TokenType.Comment;

        boolean result = token.isComment();

        Assert.assertTrue(result);
    }

    @Test
    public void testIsCharacter() {
        Token token = new Token.Character();
        token.type = Token.TokenType.Character;

        boolean result = token.isCharacter();

        Assert.assertTrue(result);
    }

    @Test
    public void testIsEOF() {
        Token token = new Token.EOF();
        token.type = Token.TokenType.EOF;

        boolean result = token.isEOF();

        Assert.assertTrue(result);
    }
}