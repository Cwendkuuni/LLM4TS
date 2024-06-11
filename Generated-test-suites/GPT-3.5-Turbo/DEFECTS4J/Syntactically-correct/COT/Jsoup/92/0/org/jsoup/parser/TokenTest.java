
package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokenTest {
    Token.Doctype doctype;
    Token.StartTag startTag;
    Token.EndTag endTag;
    Token.Comment comment;
    Token.Character character;
    Token.CData cdata;
    Token.EOF eof;

    @Before
    public void setUp() {
        doctype = new Token.Doctype();
        startTag = new Token.StartTag();
        endTag = new Token.EndTag();
        comment = new Token.Comment();
        character = new Token.Character();
        cdata = new Token.CData("");
        eof = new Token.EOF();
    }

    @Test
    public void testDoctypeGetType() {
        assertSame(Token.TokenType.Doctype, doctype.type);
    }

    @Test
    public void testDoctypeReset() {
        Token resetDoctype = doctype.reset();
        assertSame(doctype, resetDoctype);
    }

    @Test
    public void testDoctypeGetName() {
        assertEquals("", doctype.getName());
    }

    @Test
    public void testDoctypeGetPubSysKey() {
        assertNull(doctype.getPubSysKey());
    }

    @Test
    public void testDoctypeGetPublicIdentifier() {
        assertEquals("", doctype.getPublicIdentifier());
    }

    @Test
    public void testDoctypeGetSystemIdentifier() {
        assertEquals("", doctype.getSystemIdentifier());
    }

    @Test
    public void testDoctypeIsForceQuirks() {
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testStartTagGetType() {
        assertSame(Token.TokenType.StartTag, startTag.type);
    }

    @Test
    public void testStartTagReset() {
        Token resetStartTag = startTag.reset();
        assertSame(startTag, resetStartTag);
    }

    @Test
    public void testEndTagGetType() {
        assertSame(Token.TokenType.EndTag, endTag.type);
    }

    @Test
    public void testEndTagReset() {
        Token resetEndTag = endTag.reset();
        assertSame(endTag, resetEndTag);
    }

    @Test
    public void testCommentGetType() {
        assertSame(Token.TokenType.Comment, comment.type);
    }

    @Test
    public void testCommentReset() {
        Token resetComment = comment.reset();
        assertSame(comment, resetComment);
    }

    @Test
    public void testCharacterGetType() {
        assertSame(Token.TokenType.Character, character.type);
    }

    @Test
    public void testCharacterReset() {
        Token resetCharacter = character.reset();
        assertSame(character, resetCharacter);
    }

    @Test
    public void testCharacterGetData() {
        assertNull(character.getData());
    }

    @Test
    public void testCDataGetType() {
        assertSame(Token.TokenType.Character, cdata.type);
    }

    @Test
    public void testCDataReset() {
        Token resetCdata = cdata.reset();
        assertSame(cdata, resetCdata);
    }

    @Test
    public void testEOFGetType() {
        assertSame(Token.TokenType.EOF, eof.type);
    }

    @Test
    public void testEOFReset() {
        Token resetEof = eof.reset();
        assertSame(eof, resetEof);
    }
}
