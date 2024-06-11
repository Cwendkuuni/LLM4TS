package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class TokenTest {
    private Token.Doctype doctype;
    private Token.StartTag startTag;
    private Token.EndTag endTag;
    private Token.Comment comment;
    private Token.Character character;
    private Token.EOF eof;

    @Before
    public void setUp() {
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
        assertNull(doctype.getName());
        assertNull(doctype.getPublicIdentifier());
        assertNull(doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testStartTagReset() {
        startTag.reset();
        assertNull(startTag.name());
        assertNull(startTag.normalName());
        assertNull(startTag.pendingAttributeName);
        assertEquals("", startTag.pendingAttributeValue.toString());
        assertNull(startTag.pendingAttributeValueS);
        assertFalse(startTag.hasEmptyAttributeValue);
        assertFalse(startTag.hasPendingAttributeValue);
        assertFalse(startTag.isSelfClosing());
        assertNull(startTag.getAttributes());
    }

    @Test
    public void testEndTagReset() {
        endTag.reset();
        assertNull(endTag.name());
        assertNull(endTag.normalName());
        assertNull(endTag.pendingAttributeName);
        assertEquals("", endTag.pendingAttributeValue.toString());
        assertNull(endTag.pendingAttributeValueS);
        assertFalse(endTag.hasEmptyAttributeValue);
        assertFalse(endTag.hasPendingAttributeValue);
        assertFalse(endTag.isSelfClosing());
        assertNull(endTag.getAttributes());
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
    public void testEOFReset() {
        assertEquals(eof, eof.reset());
    }

    @Test
    public void testTokenTypeIsDoctype() {
        assertTrue(doctype.isDoctype());
        assertFalse(startTag.isDoctype());
        assertFalse(endTag.isDoctype());
        assertFalse(comment.isDoctype());
        assertFalse(character.isDoctype());
        assertFalse(eof.isDoctype());
    }

    @Test
    public void testAsDoctype() {
        assertNotNull(doctype.asDoctype());
        assertNull(startTag.asDoctype());
        assertNull(endTag.asDoctype());
        assertNull(comment.asDoctype());
        assertNull(character.asDoctype());
        assertNull(eof.asDoctype());
    }

    @Test
    public void testTokenTypeIsStartTag() {
        assertFalse(doctype.isStartTag());
        assertTrue(startTag.isStartTag());
        assertFalse(endTag.isStartTag());
        assertFalse(comment.isStartTag());
        assertFalse(character.isStartTag());
        assertFalse(eof.isStartTag());
    }

    @Test
    public void testAsStartTag() {
        assertNull(doctype.asStartTag());
        assertNotNull(startTag.asStartTag());
        assertNull(endTag.asStartTag());
        assertNull(comment.asStartTag());
        assertNull(character.asStartTag());
        assertNull(eof.asStartTag());
    }

    @Test
    public void testTokenTypeIsEndTag() {
        assertFalse(doctype.isEndTag());
        assertFalse(startTag.isEndTag());
        assertTrue(endTag.isEndTag());
        assertFalse(comment.isEndTag());
        assertFalse(character.isEndTag());
        assertFalse(eof.isEndTag());
    }

    @Test
    public void testAsEndTag() {
        assertNull(doctype.asEndTag());
        assertNull(startTag.asEndTag());
        assertNotNull(endTag.asEndTag());
        assertNull(comment.asEndTag());
        assertNull(character.asEndTag());
        assertNull(eof.asEndTag());
    }

    @Test
    public void testTokenTypeIsComment() {
        assertFalse(doctype.isComment());
        assertFalse(startTag.isComment());
        assertFalse(endTag.isComment());
        assertTrue(comment.isComment());
        assertFalse(character.isComment());
        assertFalse(eof.isComment());
    }

    @Test
    public void testAsComment() {
        assertNull(doctype.asComment());
        assertNull(startTag.asComment());
        assertNull(endTag.asComment());
        assertNotNull(comment.asComment());
        assertNull(character.asComment());
        assertNull(eof.asComment());
    }

    @Test
    public void testTokenTypeIsCharacter() {
        assertFalse(doctype.isCharacter());
        assertFalse(startTag.isCharacter());
        assertFalse(endTag.isCharacter());
        assertFalse(comment.isCharacter());
        assertTrue(character.isCharacter());
        assertFalse(eof.isCharacter());
    }

    @Test
    public void testAsCharacter() {
        assertNull(doctype.asCharacter());
        assertNull(startTag.asCharacter());
        assertNull(endTag.asCharacter());
        assertNull(comment.asCharacter());
        assertNotNull(character.asCharacter());
        assertNull(eof.asCharacter());
    }

    @Test
    public void testTokenTypeIsEOF() {
        assertFalse(doctype.isEOF());
        assertFalse(startTag.isEOF());
        assertFalse(endTag.isEOF());
        assertFalse(comment.isEOF());
        assertFalse(character.isEOF());
        assertTrue(eof.isEOF());
    }

    @Test
    public void testResetStringBuilder() {
        StringBuilder sb = new StringBuilder("test");
        Token.reset(sb);
        assertEquals("", sb.toString());
    }
}