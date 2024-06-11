package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;
import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Token doctype = new Token.Doctype();
        doctype.asDoctype().getName("html");
        doctype.asDoctype().setForceQuirks(true);
        doctype.reset();
        assertNull(doctype.asDoctype().getName());
        assertFalse(doctype.asDoctype().isForceQuirks());
    }

    @Test
    public void testTagReset() {
        Token tag = new Token.StartTag();
        tag.name("div");
        tag.name("span");
        tag.attributes = new Attributes();
        tag.attributes.put("class", "foo");
        tag.newAttribute();
        tag.attributes.put("id", "bar");
        tag.finaliseTag();
        tag.reset();
        assertEquals("", tag.tagName);
        assertEquals(null, tag.attributes);
    }

    @Test
    public void testCommentReset() {
        Token comment = new Token.Comment();
        comment.asComment().getData("This is a comment.");
        comment.reset();
        assertEquals("", comment.getData());
    }

    @Test
    public void testCharacterReset() {
        Token character = new Token.Character();
        character.asCharacter().getData("A");
        character.reset();
        assertNull(character.getData());
    }

    @Test
    public void testEOFReset() {
        Token eof = new Token.EOF();
        eof.reset();
        assertTrue(eof.isEOF());
    }

    @Test
    public void testTokenType() {
        Token doctype = new Token.Doctype();
        Token startTag = new Token.StartTag();
        Token endTag = new Token.EndTag();
        Token comment = new Token.Comment();
        Token character = new Token.Character();
        Token eof = new Token.EOF();

        assertTrue(doctype.isDoctype());
        assertTrue(startTag.isStartTag());
        assertTrue(endTag.isEndTag());
        assertTrue(comment.isComment());
        assertTrue(character.isCharacter());
        assertTrue(eof.isEOF());

        assertEquals(TokenType.Doctype, doctype.type);
        assertEquals(TokenType.StartTag, startTag.type);
        assertEquals(TokenType.EndTag, endTag.type);
        assertEquals(TokenType.Comment, comment.type);
        assertEquals(TokenType.Character, character.type);
        assertEquals(TokenType.EOF, eof.type);
    }
}