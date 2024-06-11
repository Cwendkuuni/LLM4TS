package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token.Doctype doctype = new Token.Doctype();
        Assert.assertEquals(Token.TokenType.Doctype, doctype.type);

        Token.StartTag startTag = new Token.StartTag();
        Assert.assertEquals(Token.TokenType.StartTag, startTag.type);

        Token.EndTag endTag = new Token.EndTag();
        Assert.assertEquals(Token.TokenType.EndTag, endTag.type);

        Token.Comment comment = new Token.Comment();
        Assert.assertEquals(Token.TokenType.Comment, comment.type);

        Token.Character character = new Token.Character();
        Assert.assertEquals(Token.TokenType.Character, character.type);

        Token.EOF eof = new Token.EOF();
        Assert.assertEquals(Token.TokenType.EOF, eof.type);
    }

    @Test
    public void testTokenReset() {
        Token.Doctype doctype = new Token.Doctype();
        Token.Doctype resetDoctype = doctype.reset();
        Assert.assertEquals(doctype.getClass(), resetDoctype.getClass());
        Assert.assertEquals(null, resetDoctype.getName());
        Assert.assertEquals(null, resetDoctype.getPubSysKey());
        Assert.assertEquals(null, resetDoctype.getPublicIdentifier());
        Assert.assertEquals(null, resetDoctype.getSystemIdentifier());
        Assert.assertEquals(false, resetDoctype.isForceQuirks());

        Token.StartTag startTag = new Token.StartTag();
        Token.StartTag resetStartTag = startTag.reset();
        Assert.assertEquals(startTag.getClass(), resetStartTag.getClass());
        Assert.assertEquals(null, resetStartTag.tagName);
        Assert.assertEquals(null, resetStartTag.normalName);
        Assert.assertEquals(null, resetStartTag.pendingAttributeName);
        Assert.assertEquals("", resetStartTag.pendingAttributeValue.toString());
        Assert.assertEquals(null, resetStartTag.pendingAttributeValueS);
        Assert.assertEquals(false, resetStartTag.hasEmptyAttributeValue);
        Assert.assertEquals(false, resetStartTag.hasPendingAttributeValue);
        Assert.assertEquals(false, resetStartTag.selfClosing);
        Assert.assertEquals(null, resetStartTag.attributes);

        Token.EndTag endTag = new Token.EndTag();
        Token.EndTag resetEndTag = endTag.reset();
        Assert.assertEquals(endTag.getClass(), resetEndTag.getClass());
        Assert.assertEquals(null, resetEndTag.tagName);
        Assert.assertEquals(null, resetEndTag.normalName);
        Assert.assertEquals(null, resetEndTag.pendingAttributeName);
        Assert.assertEquals("", resetEndTag.pendingAttributeValue.toString());
        Assert.assertEquals(null, resetEndTag.pendingAttributeValueS);
        Assert.assertEquals(false, resetEndTag.hasEmptyAttributeValue);
        Assert.assertEquals(false, resetEndTag.hasPendingAttributeValue);
        Assert.assertEquals(false, resetEndTag.selfClosing);
        Assert.assertEquals(null, resetEndTag.attributes);

        Token.Comment comment = new Token.Comment();
        Token.Comment resetComment = comment.reset();
        Assert.assertEquals(comment.getClass(), resetComment.getClass());
        Assert.assertEquals("", resetComment.data.toString());
        Assert.assertEquals(false, resetComment.bogus);

        Token.Character character = new Token.Character();
        Token.Character resetCharacter = character.reset();
        Assert.assertEquals(character.getClass(), resetCharacter.getClass());
        Assert.assertEquals(null, resetCharacter.getData());

        Token.EOF eof = new Token.EOF();
        Token.EOF resetEof = eof.reset();
        Assert.assertEquals(eof.getClass(), resetEof.getClass());
    }

    @Test
    public void testStartTagNameAttr() {
        Token.StartTag startTag = new Token.StartTag();
        Assert.assertNull(startTag.tagName);

        Attributes mockAttributes = mock(Attributes.class);
        when(mockAttributes.size()).thenReturn(1);
        
        Token.StartTag result = startTag.nameAttr("name", mockAttributes);

        Assert.assertEquals("name", startTag.tagName);
        Assert.assertEquals("name", startTag.normalName);
        Assert.assertEquals(mockAttributes, startTag.attributes);
        Assert.assertEquals(TokenType.StartTag, result.type);
    }

    @Test
    public void testStartTagToStringWithoutAttributes() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name = "name";

        String result = startTag.toString();

        Assert.assertEquals("<name>", result);
    }

    @Test
    public void testStartTagToStringWithAttributes() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name = "name";
        Attributes mockAttributes = mock(Attributes.class);
        when(mockAttributes.size()).thenReturn(1);
        when(mockAttributes.toString()).thenReturn("attr1=value1");
        startTag.attributes = mockAttributes;

        String result = startTag.toString();

        Assert.assertEquals("<name attr1=value1>", result);
    }

    @Test
    public void testEndTagToString() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "name";

        String result = endTag.toString();

        Assert.assertEquals("</name>", result);
    }

    @Test
    public void testCommentReset() {
        Token.Comment comment = new Token.Comment();
        comment.bogus = true;
        comment.data.append("test");

        Token.Comment resetComment = comment.reset();

        Assert.assertEquals(false, resetComment.bogus);
        Assert.assertEquals("", resetComment.data.toString());
    }

    @Test
    public void testCharacterReset() {
        Token.Character character = new Token.Character();
        character.data("test");

        Token.Character resetCharacter = character.reset();

        Assert.assertEquals(null, resetCharacter.getData());
    }

    @Test
    public void testCharacterData() {
        Token.Character character = new Token.Character();

        Token.Character result = character.data("test");

        Assert.assertEquals("test", character.getData());
        Assert.assertEquals(character, result);
    }

    @Test
    public void testCharacterToString() {
        Token.Character character = new Token.Character();
        character.data("test");

        String result = character.toString();

        Assert.assertEquals("test", result);
    }

    @Test
    public void testCDataToString() {
        Token.CData cData = new Token.CData("test");

        String result = cData.toString();

        Assert.assertEquals("<![CDATA[test]]>", result);
    }

    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();

        Token.EOF resetEof = eof.reset();

        Assert.assertEquals(eof.getClass(), resetEof.getClass());
    }
}