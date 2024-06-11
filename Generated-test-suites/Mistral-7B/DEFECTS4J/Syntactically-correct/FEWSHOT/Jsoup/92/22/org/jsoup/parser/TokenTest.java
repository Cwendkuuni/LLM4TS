package org.jsoup.parser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;

import org.junit.Test;

public class TokenTest {

    @Test
    public void testTokenReset() {
        Token token = new Token.Doctype();
        token.reset();
        assertNull(token.getType());
        assertNull(token.getName());
        assertNull(token.getPublicIdentifier());
        assertNull(token.getSystemIdentifier());
        assertFalse(token.isForceQuirks());
    }

    @Test
    public void testDoctypeTokenReset() {
        TokenType tokenType = TokenType.Doctype;
        Token doctypeToken = new Token.Doctype();
        Token resetDoctypeToken = doctypeToken.reset();

        assertSame(tokenType, resetDoctypeToken.getType());
        assertNull(resetDoctypeToken.getName());
        assertNull(resetDoctypeToken.getPublicIdentifier());
        assertNull(resetDoctypeToken.getSystemIdentifier());
        assertFalse(resetDoctypeToken.isForceQuirks());
    }

    @Test
    public void testDoctypeTokenName() {
        TokenType tokenType = TokenType.Doctype;
        String doctypeName = "html";
        Token doctypeToken = new Token.Doctype().name(doctypeName);

        assertEquals(doctypeName, doctypeToken.getName());
        assertEquals(doctypeName.toLowerCase(), doctypeToken.normalName());
    }

    @Test
    public void testDoctypeTokenGetters() {
        TokenType tokenType = TokenType.Doctype;
        String doctypeName = "html";
        StringBuilder name = new StringBuilder("name");
        String pubSysKey = "pubSysKey";
        StringBuilder publicIdentifier = new StringBuilder("publicIdentifier");
        StringBuilder systemIdentifier = new StringBuilder("systemIdentifier");
        boolean forceQuirks = true;

        Token doctypeToken = new Token.Doctype()
                .name(doctypeName)
                .getName(name.toString())
                .setPublicIdentifier(pubSysKey)
                .setPublicIdentifier(publicIdentifier.toString())
                .setSystemIdentifier(systemIdentifier.toString())
                .setForceQuirks(forceQuirks);

        assertEquals(tokenType, doctypeToken.getType());
        assertEquals(doctypeName, doctypeToken.getName());
        assertEquals(name.toString(), doctypeToken.getName());
        assertEquals(pubSysKey, doctypeToken.getPublicIdentifier());
        assertEquals(publicIdentifier.toString(), doctypeToken.getPublicIdentifier());
        assertEquals(systemIdentifier.toString(), doctypeToken.getSystemIdentifier());
        assertEquals(forceQuirks, doctypeToken.isForceQuirks());
    }

    @Test
    public void testTagTokenReset() {
        TokenType tokenType = TokenType.StartTag;
        String tagName = "div";
        Token tagToken = new Token.Tag().name(tagName);
        Token resetTagToken = tagToken.reset();

        assertSame(tokenType, resetTagToken.getType());
        assertNull(resetTagToken.getName());
        assertNull(resetTagToken.normalName);
        assertNull(resetTagToken.attributes);
        assertFalse(resetTagToken.selfClosing);
    }

    @Test
    public void testTagTokenName() {
        TokenType tokenType = TokenType.StartTag;
        String tagName = "div";
        Token tagToken = new Token.Tag().name(tagName);

        assertEquals(tagName, tagToken.getName());
        assertEquals(tagName.toLowerCase(), tagToken.normalName);
    }

    @Test
    public void testTagTokenNewAttribute() {
        TokenType tokenType = TokenType.StartTag;
        String tagName = "div";
        StringBuilder attributeName = new StringBuilder("class");
        String attributeValue = "class1 class2";

        Token tagToken = new Token.Tag().name(tagName)
                .name("name", tagToken)
                .name(attributeName.toString())
                .appendAttributeValue(attributeValue);

        Attributes attributes = tagToken.getAttributes();
        assertEquals(1, attributes.size());
        assertTrue(attributes.containsKey(attributeName.toString()));
        assertEquals(attributeValue, attributes.getValue(attributeName.toString()));
    }

    @Test
    public void testTagTokenFinaliseTag() {
        TokenType tokenType = TokenType.StartTag;
        String tagName = "div";
        StringBuilder attributeName = new StringBuilder("class");
        String attributeValue = "class1 class2";

        Token tagToken = new Token.Tag().name(tagName)
                .name("name", tagToken)
                .appendAttributeName(attributeName.toString())
                .appendAttributeValue(attributeValue);

        tagToken.finaliseTag();

        Attributes attributes = tagToken.getAttributes();
        assertEquals(1, attributes.size());
        assertTrue(attributes.containsKey(attributeName.toString()));
        assertEquals(attributeValue, attributes.getValue(attributeName.toString()));
    }

    @Test
    public void testTagTokenToString() {
        TokenType tokenType = TokenType.StartTag;
        String tagName = "div";
        StringBuilder attributeName = new StringBuilder("class");
        String attributeValue = "class1 class2";

        Token tagToken = new Token.StartTag()
                .name(tagName)
                .name("name", tagToken)
                .appendAttributeName(attributeName.toString())
                .appendAttributeValue(attributeValue);

        assertEquals("<div class=\"class1 class2\">", tagToken.toString());
    }

    @Test
    public void testTagTokenSelfClosing() {
        TokenType tokenType = TokenType.StartTag;
        String tagName = "img";
        boolean selfClosing = true;

        Token tagToken = new Token.StartTag().name(tagName).selfClosing(selfClosing);

        assertEquals(tokenType, tagToken.getType());
        assertEquals(tagName, tagToken.getName());
        assertEquals(tagName.toLowerCase(), tagToken.normalName);
        assertEquals(selfClosing, tagToken.isSelfClosing());
    }

    @Test
    public void testCommentTokenReset() {
        TokenType tokenType = TokenType.Comment;
        StringBuilder data = new StringBuilder("<!-- This is a comment -->");
        Token commentToken = new Token.Comment().data(data.toString());
        Token resetCommentToken = commentToken.reset();

        assertSame(tokenType, resetCommentToken.getType());
        assertNull(resetCommentToken.getName());
        assertNull(resetCommentToken.normalName);
        assertNull(resetCommentToken.getData());
        assertFalse(resetCommentToken.isBogus());
    }

    @Test
    public void testCommentTokenData() {
        TokenType tokenType = TokenType.Comment;
        StringBuilder data = new StringBuilder("<!-- This is a comment -->");
        Token commentToken = new Token.Comment().data(data.toString());

        assertEquals(tokenType, commentToken.getType());
        assertEquals(data.toString(), commentToken.getData());
    }

    @Test
    public void testCommentTokenBogus() {
        TokenType tokenType = TokenType.Comment;
        StringBuilder data = new StringBuilder("<!-- This is a comment -->");
        Token commentToken = new Token.Comment().data(data.toString()).bogus(true);

        assertEquals(tokenType, commentToken.getType());
        assertEquals(data.toString(), commentToken.getData());
        assertTrue(commentToken.isBogus());
    }

    @Test
    public void testCharacterTokenReset() {
        TokenType tokenType = TokenType.Character;
        String characterData = "abc";
        Token characterToken = new Token.Character().data(characterData);
        Token resetCharacterToken = characterToken.reset();

        assertSame(tokenType, resetCharacterToken.getType());
        assertNull(resetCharacterToken.getName());
        assertNull(resetCharacterToken.normalName);
        assertNull(resetCharacterToken.getData());
    }

    @Test
    public void testCharacterTokenData() {
        TokenType tokenType = TokenType.Character;
        String characterData = "abc";
        Token characterToken = new Token.Character().data(characterData);

        assertEquals(tokenType, characterToken.getType());
        assertEquals(characterData, characterToken.getData());
    }

    @Test
    public void testCharacterTokenCData() {
        TokenType tokenType = TokenType.Character;
        String cdataData = "<![CDATA[abc]]>";
        Token cdataToken = new Token.CData().data(cdataData);

        assertEquals(tokenType, cdataToken.getType());
        assertEquals(cdataData, cdataToken.getData());
    }

    @Test
    public void testCharacterTokenToString() {
        TokenType tokenType = TokenType.Character;
        String characterData = "abc";
        Token characterToken = new Token.Character().data(characterData);

        assertEquals(characterData, characterToken.toString());
    }

    @Test
    public void testCharacterTokenCDataToString() {
        TokenType tokenType = TokenType.Character;
        String cdataData = "<![CDATA[abc]]>";
        Token cdataToken = new Token.CData().data(cdataData);

        assertEquals(cdataData, cdataToken.toString());
    }

    @Test
    public void testEOFTokenReset() {
        TokenType tokenType = Token.TokenType.EOF;
        Token eofToken = new Token.EOF();
        Token resetEofToken = eofToken.reset();

        assertSame(tokenType, resetEofToken.getType());
        assertNull(resetEofToken.getName());
        assertNull(resetEofToken.normalName);
        assertNull(resetEofToken.getData());
    }

    @Test
    public void testEOFTokenToString() {
        TokenType tokenType = Token.TokenType.EOF;
        Token eofToken = new Token.EOF();

        assertEquals("", eofToken.toString());
    }
}