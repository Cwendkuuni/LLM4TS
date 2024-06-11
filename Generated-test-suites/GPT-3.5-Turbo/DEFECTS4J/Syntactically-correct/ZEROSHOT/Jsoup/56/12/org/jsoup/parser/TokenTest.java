package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokenTest {

    private Token token;

    @Before
    public void setUp() {
        token = new Token.Doctype();
    }

    @Test
    public void testTokenType() {
        assertEquals("Doctype", token.tokenType());
    }

    @Test
    public void testReset() {
        Token resetToken = token.reset();
        assertNotNull(resetToken);
    }

    @Test
    public void testDoctypeGetName() {
        Token.Doctype doctype = (Token.Doctype) token;
        assertNotNull(doctype.getName());
    }

    @Test
    public void testDoctypeGetPublicIdentifier() {
        Token.Doctype doctype = (Token.Doctype) token;
        assertNotNull(doctype.getPublicIdentifier());
    }

    @Test
    public void testDoctypeGetSystemIdentifier() {
        Token.Doctype doctype = (Token.Doctype) token;
        assertNotNull(doctype.getSystemIdentifier());
    }

    @Test
    public void testDoctypeIsForceQuirks() {
        Token.Doctype doctype = (Token.Doctype) token;
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testTagReset() {
        Token.Tag tag = new Token.StartTag();
        Token.Tag resetTag = tag.reset();
        assertNotNull(resetTag);
    }

    @Test
    public void testTagName() {
        Token.Tag tag = new Token.StartTag();
        String name = "test";
        Token.Tag namedTag = tag.name(name);
        assertEquals(name, namedTag.name());
    }

    @Test
    public void testTagNormalName() {
        Token.Tag tag = new Token.StartTag();
        String name = "test";
        Token.Tag namedTag = tag.name(name);
        assertEquals(name.toLowerCase(), namedTag.normalName());
    }

    @Test
    public void testTagIsSelfClosing() {
        Token.Tag tag = new Token.StartTag();
        assertFalse(tag.isSelfClosing());
    }

    @Test
    public void testTagGetAttributes() {
        Token.Tag tag = new Token.StartTag();
        assertNull(tag.getAttributes());
    }

    @Test
    public void testTagAppendTagName() {
        Token.Tag tag = new Token.StartTag();
        char append = 'a';
        tag.appendTagName(append);
        assertEquals(String.valueOf(append), tag.tagName);
    }

    @Test
    public void testTagAppendAttributeName() {
        Token.Tag tag = new Token.StartTag();
        char append = 'a';
        tag.appendAttributeName(append);
        assertEquals(String.valueOf(append), tag.pendingAttributeName);
    }

    @Test
    public void testTagAppendAttributeValue() {
        Token.Tag tag = new Token.StartTag();
        String append = "test";
        tag.appendAttributeValue(append);
        assertTrue(tag.hasPendingAttributeValue);
        assertEquals(append, tag.pendingAttributeValue.toString());
    }

    @Test
    public void testTagSetEmptyAttributeValue() {
        Token.Tag tag = new Token.StartTag();
        tag.setEmptyAttributeValue();
        assertTrue(tag.hasEmptyAttributeValue);
    }

    @Test
    public void testTagFinaliseTag() {
        Token.Tag tag = new Token.StartTag();
        tag.finaliseTag();
        assertNull(tag.pendingAttributeName);
    }

    @Test
    public void testStartTagReset() {
        Token.StartTag startTag = new Token.StartTag();
        Token.StartTag resetStartTag = startTag.reset();
        assertNotNull(resetStartTag);
    }

    @Test
    public void testStartTagNameAttr() {
        Token.StartTag startTag = new Token.StartTag();
        String name = "test";
        Attributes attributes = new Attributes();
        Token.StartTag namedStartTag = startTag.nameAttr(name, attributes);
        assertEquals(name, namedStartTag.tagName);
        assertSame(attributes, namedStartTag.attributes);
        assertEquals(name.toLowerCase(), namedStartTag.normalName());
    }

    @Test
    public void testStartTagToStringNoAttributes() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("test");
        assertEquals("<test>", startTag.toString());
    }

    @Test
    public void testStartTagToStringWithAttributes() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("test");
        Attributes attributes = new Attributes();
        attributes.put("attr1", "value1");
        attributes.put("attr2", "value2");
        startTag.attributes = attributes;
        assertEquals("<test attr1=\"value1\" attr2=\"value2\">", startTag.toString());
    }

    @Test
    public void testEndTagReset() {
        Token.EndTag endTag = new Token.EndTag();
        Token.EndTag resetEndTag = endTag.reset();
        assertNotNull(resetEndTag);
    }

    @Test
    public void testEndTagToString() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.name("test");
        assertEquals("</test>", endTag.toString());
    }

    @Test
    public void testCommentReset() {
        Token.Comment comment = new Token.Comment();
        Token.Comment resetComment = comment.reset();
        assertNotNull(resetComment);
    }

    @Test
    public void testCommentGetData() {
        Token.Comment comment = new Token.Comment();
        assertNotNull(comment.getData());
    }

    @Test
    public void testCharacterReset() {
        Token.Character character = new Token.Character();
        Token.Character resetCharacter = character.reset();
        assertNotNull(resetCharacter);
    }

    @Test
    public void testCharacterData() {
        Token.Character character = new Token.Character();
        String data = "test";
        Token.Character characterWithData = character.data(data);
        assertEquals(data, characterWithData.getData());
    }

    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();
        Token.EOF resetEof = eof.reset();
        assertNotNull(resetEof);
    }

    @Test
    public void testIsDoctype() {
        Token token = new Token.Doctype();
        assertTrue(token.isDoctype());
        assertFalse(token.isStartTag());
        assertFalse(token.isEndTag());
        assertFalse(token.isComment());
        assertFalse(token.isCharacter());
        assertFalse(token.isEOF());
    }

    @Test
    public void testIsStartTag() {
        Token token = new Token.StartTag();
        assertTrue(token.isStartTag());
        assertFalse(token.isDoctype());
        assertFalse(token.isEndTag());
        assertFalse(token.isComment());
        assertFalse(token.isCharacter());
        assertFalse(token.isEOF());
    }

    @Test
    public void testIsEndTag() {
        Token token = new Token.EndTag();
        assertTrue(token.isEndTag());
        assertFalse(token.isDoctype());
        assertFalse(token.isStartTag());
        assertFalse(token.isComment());
        assertFalse(token.isCharacter());
        assertFalse(token.isEOF());
    }

    @Test
    public void testIsComment() {
        Token token = new Token.Comment();
        assertTrue(token.isComment());
        assertFalse(token.isDoctype());
        assertFalse(token.isStartTag());
        assertFalse(token.isEndTag());
        assertFalse(token.isCharacter());
        assertFalse(token.isEOF());
    }

    @Test
    public void testIsCharacter() {
        Token token = new Token.Character();
        assertTrue(token.isCharacter());
        assertFalse(token.isDoctype());
        assertFalse(token.isStartTag());
        assertFalse(token.isEndTag());
        assertFalse(token.isComment());
        assertFalse(token.isEOF());
    }

    @Test
    public void testIsEOF() {
        Token token = new Token.EOF();
        assertTrue(token.isEOF());
        assertFalse(token.isDoctype());
        assertFalse(token.isStartTag());
        assertFalse(token.isEndTag());
        assertFalse(token.isComment());
        assertFalse(token.isCharacter());
    }

    @Test
    public void testTokenTypeValues() {
        Token.TokenType[] tokenTypes = Token.TokenType.values();
        assertEquals(6, tokenTypes.length);
        assertEquals(Token.TokenType.Doctype, tokenTypes[0]);
        assertEquals(Token.TokenType.StartTag, tokenTypes[1]);
        assertEquals(Token.TokenType.EndTag, tokenTypes[2]);
        assertEquals(Token.TokenType.Comment, tokenTypes[3]);
        assertEquals(Token.TokenType.Character, tokenTypes[4]);
        assertEquals(Token.TokenType.EOF, tokenTypes[5]);
    }

}