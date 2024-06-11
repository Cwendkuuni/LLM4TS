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
        doctype.setName("test");
        doctype.setPubSysKey("key");
        doctype.setPublicIdentifier("pub");
        doctype.setSystemIdentifier("sys");
        doctype.setForceQuirks(true);

        doctype.reset();

        assertEquals("", doctype.getName());
        assertNull(doctype.getPubSysKey());
        assertEquals("", doctype.getPublicIdentifier());
        assertEquals("", doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testStartTagReset() {
        startTag.setName("test");
        startTag.setSelfClosing(true);

        startTag.reset();

        assertNull(startTag.getName());
        assertNull(startTag.getNormalName());
        assertNull(startTag.getAttributes());
        assertFalse(startTag.isSelfClosing());
    }

    @Test
    public void testEndTagReset() {
        endTag.setName("test");

        endTag.reset();

        assertNull(endTag.getName());
        assertNull(endTag.getNormalName());
    }

    @Test
    public void testCommentReset() {
        comment.setData("test");
        comment.setBogus(true);

        comment.reset();

        assertEquals("", comment.getData());
        assertFalse(comment.isBogus());
    }

    @Test
    public void testCharacterReset() {
        character.setData("test");

        character.reset();

        assertNull(character.getData());
    }

    @Test
    public void testEOFReset() {
        eof.reset();

        assertNotNull(eof);
    }

    @Test
    public void testDoctypeGetType() {
        assertEquals(Token.TokenType.Doctype, doctype.getType());
    }

    @Test
    public void testStartTagGetType() {
        assertEquals(Token.TokenType.StartTag, startTag.getType());
    }

    @Test
    public void testEndTagGetType() {
        assertEquals(Token.TokenType.EndTag, endTag.getType());
    }

    @Test
    public void testCommentGetType() {
        assertEquals(Token.TokenType.Comment, comment.getType());
    }

    @Test
    public void testCharacterGetType() {
        assertEquals(Token.TokenType.Character, character.getType());
    }

    @Test
    public void testEOFGetType() {
        assertEquals(Token.TokenType.EOF, eof.getType());
    }
}