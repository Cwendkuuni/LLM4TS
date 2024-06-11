package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    private Token.Doctype doctype;
    private Token.StartTag startTag;
    private Token.EndTag endTag;
    private Token.Comment comment;
    private Token.Character character;
    private Token.EOF eof;

    @Before
    public void setup() {
        doctype = new Token.Doctype();
        startTag = new Token.StartTag();
        endTag = new Token.EndTag();
        comment = new Token.Comment();
        character = new Token.Character();
        eof = new Token.EOF();
    }

    @Test
    public void testDoctypeReset() {
        doctype.reset();
        assertEquals("", doctype.getName());
        assertEquals("", doctype.getPublicIdentifier());
        assertEquals("", doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testStartTagReset() {
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
    public void testEndTagReset() {
        endTag.reset();
        assertNull(endTag.tagName);
        assertNull(endTag.normalName);
        assertNull(endTag.pendingAttributeName);
        assertEquals("", endTag.pendingAttributeValue.toString());
        assertNull(endTag.pendingAttributeValueS);
        assertFalse(endTag.hasEmptyAttributeValue);
        assertFalse(endTag.hasPendingAttributeValue);
        assertFalse(endTag.selfClosing);
        assertNull(endTag.attributes);
    }

    @Test
    public void testCommentReset() {
        comment.reset();
        assertEquals("", comment.getData());
        assertFalse(comment.bogus);
    }

    @Test
    public void testCharacterReset() {
        character.reset();
        assertNull(character.getData());
    }

    @Test
    public void testDoctypeGetType() {
        assertEquals(Token.TokenType.Doctype, doctype.type);
    }

    @Test
    public void testStartTagGetType() {
        assertEquals(Token.TokenType.StartTag, startTag.type);
    }

    @Test
    public void testEndTagGetType() {
        assertEquals(Token.TokenType.EndTag, endTag.type);
    }

    @Test
    public void testCommentGetType() {
        assertEquals(Token.TokenType.Comment, comment.type);
    }

    @Test
    public void testCharacterGetType() {
        assertEquals(Token.TokenType.Character, character.type);
    }

    @Test
    public void testEOFGetType() {
        assertEquals(Token.TokenType.EOF, eof.type);
    }

    // Add comprehensive tests for all other methods in the Token class

}