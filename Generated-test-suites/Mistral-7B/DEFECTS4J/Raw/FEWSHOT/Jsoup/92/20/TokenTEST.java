package org.jsoup.parser;

import static org.junit.Assert.*;
import static org.jsoup.internal.Normalizer.lowerCase;

import org.jsoup.nodes.Attributes;

import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Token doctype = new Token.Doctype();
        doctype.asDoctype().getName("html");
        doctype.asDoctype().getPublicIdentifier().append("public-identifier");
        doctype.asDoctype().getSystemIdentifier().append("system-identifier");
        doctype.asDoctype().isForceQuirks(true);

        Token resetDoctype = doctype.reset();
        assertEquals(TokenType.Doctype, resetDoctype.type);
        assertNull(resetDoctype.asDoctype().getName());
        assertNull(resetDoctype.asDoctype().getPublicIdentifier());
        assertNull(resetDoctype.asDoctype().getSystemIdentifier());
        assertFalse(resetDoctype.asDoctype().isForceQuirks());
    }

    @Test
    public void testTagReset() {
        Token tag = new Token.StartTag();
        tag.asStartTag().name("div");
        tag.asStartTag().attributes = new Attributes();
        tag.asStartTag().attributes.put("class", "red");

        Token resetTag = tag.reset();
        assertEquals(TokenType.StartTag, resetTag.type);
        assertNull(resetTag.asStartTag().name());
        assertNull(resetTag.asStartTag().getAttributes());
    }

    @Test
    public void testCommentReset() {
        Token comment = new Token.Comment();
        comment.asComment().getData().append("This is a comment");

        Token resetComment = comment.reset();
        assertEquals(TokenType.Comment, resetComment.type);
        assertNull(resetComment.asComment().getData());
    }

    @Test
    public void testCharacterReset() {
        Token character = new Token.Character();
        character.asCharacter().getData().append("A");

        Token resetCharacter = character.reset();
        assertEquals(TokenType.Character, resetCharacter.type);
        assertNull(resetCharacter.asCharacter().getData());
    }

    @Test
    public void testTokenTypeIsDoctype() {
        Token doctype = new Token.Doctype();
        assertTrue(doctype.isDoctype());
    }

    @Test
    public void testTokenTypeIsStartTag() {
        Token startTag = new Token.StartTag();
        assertTrue(startTag.isStartTag());
    }

    @Test
    public void testTokenTypeIsEndTag() {
        Token endTag = new Token.EndTag();
        assertTrue(endTag.isEndTag());
    }

    @Test
    public void testTokenTypeIsComment() {
        Token comment = new Token.Comment();
        assertTrue(comment.isComment());
    }

    @Test
    public void testTokenTypeIsCharacter() {
        Token character = new Token.Character();
        assertTrue(character.isCharacter());
    }

    @Test
    public void testTokenTypeIsCData() {
        Token cData = new Token.CData("CDATA");
        assertTrue(cData.isCharacter());
        assertTrue(cData.isCData());
    }

    @Test
    public void testTokenTypeIsEOF() {
        Token eof = new Token.EOF();
        assertTrue(eof.isEOF());
    }

    @Test
    public void testTokenName() {
        Token startTag = new Token.StartTag().name("div");
        assertEquals("div", startTag.name());
    }

    @Test
    public void testTokenNormalName() {
        Token startTag = new Token.StartTag().name("Div");
        assertEquals("div", startTag.normalName());
    }

    @Test
    public void testTagIsSelfClosing() {
        Token startTag = new Token.StartTag();
        Token endTag = new Token.EndTag();

        assertFalse(startTag.isSelfClosing());
        assertTrue(endTag.isSelfClosing());
    }

    @Test
    public void testTagGetAttributes() {
        Token startTag = new Token.StartTag();
        startTag.asStartTag().attributes = new Attributes();
        startTag.asStartTag().attributes.put("class", "red");

        Attributes attributes = startTag.asStartTag().getAttributes();
        assertEquals("class", attributes.keySet().iterator().next());
        assertEquals("red", attributes.get("class"));
    }
}