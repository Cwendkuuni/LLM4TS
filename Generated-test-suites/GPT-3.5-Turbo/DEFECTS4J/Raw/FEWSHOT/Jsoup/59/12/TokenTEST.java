package org.jsoup.parser;

import org.junit.Test;

import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testResetDoctype() {
        Token.Doctype doctype = new Token.Doctype();

        doctype.name.append("html");
        doctype.pubSysKey = "pubKey";
        doctype.publicIdentifier.append("//pubid");
        doctype.systemIdentifier.append("sysid");
        doctype.forceQuirks = true;

        doctype.reset();

        assertEquals("", doctype.name.toString());
        assertNull(doctype.pubSysKey);
        assertEquals("", doctype.publicIdentifier.toString());
        assertEquals("", doctype.systemIdentifier.toString());
        assertFalse(doctype.forceQuirks);
    }

    @Test
    public void testResetStartTag() {
        Token.StartTag startTag = new Token.StartTag();

        startTag.tagName = "div";
        startTag.normalName = "div";
        startTag.pendingAttributeName = "id";
        startTag.pendingAttributeValue.append("123");
        startTag.pendingAttributeValueS = "123";
        startTag.hasEmptyAttributeValue = true;
        startTag.hasPendingAttributeValue = true;
        startTag.selfClosing = true;
        startTag.attributes = null;

        startTag.reset();

        assertNull(startTag.tagName);
        assertNull(startTag.normalName);
        assertNull(startTag.pendingAttributeName);
        assertEquals("", startTag.pendingAttributeValue.toString());
        assertNull(startTag.pendingAttributeValueS);
        assertFalse(startTag.hasEmptyAttributeValue);
        assertFalse(startTag.hasPendingAttributeValue);
        assertFalse(startTag.selfClosing);
        assertNull(startTag.attributes);
    }

    @Test
    public void testResetEndTag() {
        Token.EndTag endTag = new Token.EndTag();

        endTag.tagName = "div";
        endTag.normalName = "div";

        endTag.reset();

        assertNull(endTag.tagName);
        assertNull(endTag.normalName);
    }

    @Test
    public void testResetComment() {
        Token.Comment comment = new Token.Comment();

        comment.data.append("comment");
        comment.bogus = true;

        comment.reset();

        assertEquals("", comment.data.toString());
        assertFalse(comment.bogus);
    }

    @Test
    public void testResetCharacter() {
        Token.Character character = new Token.Character();

        character.data = "data";

        character.reset();

        assertNull(character.data);
    }

    @Test
    public void testResetEOF() {
        Token.EOF eof = new Token.EOF();

        eof.reset();

        assertNotNull(eof);
    }

    @Test
    public void testTokenTypeIsDoctype() {
        Token.Doctype doctype = new Token.Doctype();

        assertTrue(doctype.isDoctype());

        assertFalse(doctype.isStartTag());
        assertFalse(doctype.isEndTag());
        assertFalse(doctype.isComment());
        assertFalse(doctype.isCharacter());
        assertFalse(doctype.isEOF());
    }

    @Test
    public void testTokenTypeIsStartTag() {
        Token.StartTag startTag = new Token.StartTag();

        assertTrue(startTag.isStartTag());

        assertFalse(startTag.isDoctype());
        assertFalse(startTag.isEndTag());
        assertFalse(startTag.isComment());
        assertFalse(startTag.isCharacter());
        assertFalse(startTag.isEOF());
    }

    @Test
    public void testTokenTypeIsEndTag() {
        Token.EndTag endTag = new Token.EndTag();

        assertTrue(endTag.isEndTag());

        assertFalse(endTag.isDoctype());
        assertFalse(endTag.isStartTag());
        assertFalse(endTag.isComment());
        assertFalse(endTag.isCharacter());
        assertFalse(endTag.isEOF());
    }

    @Test
    public void testTokenTypeIsComment() {
        Token.Comment comment = new Token.Comment();

        assertTrue(comment.isComment());

        assertFalse(comment.isDoctype());
        assertFalse(comment.isStartTag());
        assertFalse(comment.isEndTag());
        assertFalse(comment.isCharacter());
        assertFalse(comment.isEOF());
    }

    @Test
    public void testTokenTypeIsCharacter() {
        Token.Character character = new Token.Character();

        assertTrue(character.isCharacter());

        assertFalse(character.isDoctype());
        assertFalse(character.isStartTag());
        assertFalse(character.isEndTag());
        assertFalse(character.isComment());
        assertFalse(character.isEOF());
    }

    @Test
    public void testTokenTypeIsEOF() {
        Token.EOF eof = new Token.EOF();

        assertTrue(eof.isEOF());

        assertFalse(eof.isDoctype());
        assertFalse(eof.isStartTag());
        assertFalse(eof.isEndTag());
        assertFalse(eof.isComment());
        assertFalse(eof.isCharacter());
    }
}