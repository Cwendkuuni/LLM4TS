package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.parser.Token.*;
import org.jsoup.parser.TokenType;

public class TokenTest {

    @Test
    public void testDoctype() {
        Doctype doctype = new Doctype();
        doctype.getName().equals("html"); // assuming HTML is the default doctype name
        assertTrue(doctype.isDoctype());
        assertFalse(doctype.isStartTag());
        assertFalse(doctype.isEndTag());
        assertFalse(doctype.isComment());
        assertFalse(doctype.isCharacter());
        assertFalse(doctype.isEOF());

        Doctype htmlDoctype = new Doctype().name("html");
        htmlDoctype.getName().equals("html");
        htmlDoctype.isDoctype();
        htmlDoctype.isStartTag();
        htmlDoctype.isEndTag();
        htmlDoctype.isComment();
        htmlDoctype.isCharacter();
        htmlDoctype.isEOF();
        assertFalse(htmlDoctype.isForceQuirks());

        Doctype quirksDoctype = new Doctype().name("html").isForceQuirks(true);
        quirksDoctype.getName().equals("html");
        quirksDoctype.isDoctype();
        quirksDoctype.isStartTag();
        quirksDoctype.isEndTag();
        quirksDoctype.isComment();
        quirksDoctype.isCharacter();
        quirksDoctype.isEOF();
        quirksDoctype.isForceQuirks();
        assertTrue(quirksDoctype.isForceQuirks());
    }

    @Test
    public void testTag() {
        StartTag startTag = new StartTag("div").name("div");
        EndTag endTag = new EndTag("div").name("div");
        Tag tag = new Tag().name("div");

        startTag.isDoctype();
        assertFalse(startTag.isDoctype());
        startTag.isStartTag();
        assertTrue(startTag.isStartTag());
        startTag.isEndTag();
        assertFalse(startTag.isEndTag());
        startTag.isComment();
        assertFalse(startTag.isComment());
        startTag.isCharacter();
        assertFalse(startTag.isCharacter());
        startTag.isEOF();
        assertFalse(startTag.isEOF());

        endTag.isDoctype();
        assertFalse(endTag.isDoctype());
        endTag.isStartTag();
        assertFalse(endTag.isStartTag());
        endTag.isEndTag();
        assertTrue(endTag.isEndTag());
        endTag.isComment();
        assertFalse(endTag.isComment());
        endTag.isCharacter();
        assertFalse(endTag.isCharacter());
        endTag.isEOF();
        assertFalse(endTag.isEOF());

        tag.isDoctype();
        assertFalse(tag.isDoctype());
        tag.isStartTag();
        assertFalse(tag.isStartTag());
        tag.isEndTag();
        assertFalse(tag.isEndTag());
        tag.isComment();
        assertFalse(tag.isComment());
        tag.isCharacter();
        assertFalse(tag.isCharacter());
        tag.isEOF();
        assertFalse(tag.isEOF());
        tag.name().equals("div");
    }

    @Test
    public void testComment() {
        Comment comment = new Comment();
        comment.isDoctype();
        assertFalse(comment.isDoctype());
        comment.isStartTag();
        assertFalse(comment.isStartTag());
        comment.isEndTag();
        assertFalse(comment.isEndTag());
        comment.isComment();
        assertTrue(comment.isComment());
        comment.isCharacter();
        assertFalse(comment.isCharacter());
        comment.isEOF();
        assertFalse(comment.isEOF());

        Comment commentWithData = new Comment().getData("This is a comment");
        commentWithData.isDoctype();
        assertFalse(commentWithData.isDoctype());
        commentWithData.isStartTag();
        assertFalse(commentWithData.isStartTag());
        commentWithData.isEndTag();
        assertFalse(commentWithData.isEndTag());
        commentWithData.isComment();
        assertTrue(commentWithData.isComment());
        commentWithData.isCharacter();
        assertFalse(commentWithData.isCharacter());
        commentWithData.isEOF();
        assertFalse(commentWithData.isEOF());
        commentWithData.getData().equals("This is a comment");
    }

    @Test
    public void testCharacter() {
        Character character = new Character("A");
        character.isDoctype();
        assertFalse(character.isDoctype());
        character.isStartTag();
        assertFalse(character.isStartTag());
        character.isEndTag();
        assertFalse(character.isEndTag());
        character.isComment();
        assertFalse(character.isComment());
        character.isCharacter();
        assertTrue(character.isCharacter());
        character.isEOF();
        assertFalse(character.isEOF());

        Character characterWithData = new Character("&nbsp;");
        characterWithData.isDoctype();
        assertFalse(characterWithData.isDoctype());
        characterWithData.isStartTag();
        assertFalse(characterWithData.isStartTag());
        characterWithData.isEndTag();
        assertFalse(characterWithData.isEndTag());
        characterWithData.isComment();
        assertFalse(characterWithData.isComment());
        characterWithData.isCharacter();
        assertTrue(characterWithData.isCharacter());
        characterWithData.isEOF();
        assertFalse(characterWithData.isEOF());
        characterWithData.getData().equals("&nbsp;");
    }

    @Test
    public void testEOF() {
        EOF eof = new EOF();
        eof.isDoctype();
        assertFalse(eof.isDoctype());
        eof.isStartTag();
        assertFalse(eof.isStartTag());
        eof.isEndTag();
        assertFalse(eof.isEndTag());
        eof.isComment();
        assertFalse(eof.isComment());
        eof.isCharacter();
        assertFalse(eof.isCharacter());
        eof.isEOF();
        assertTrue(eof.isEOF());
    }

    @Test
    public void testTokenType() {
        assertEquals(TokenType.Doctype, TokenType.valueOf("Doctype"));
        assertEquals(TokenType.StartTag, TokenType.valueOf("StartTag"));
        assertEquals(TokenType.EndTag, TokenType.valueOf("EndTag"));
        assertEquals(TokenType.Comment, TokenType.valueOf("Comment"));
        assertEquals(TokenType.Character, TokenType.valueOf("Character"));
        assertEquals(TokenType.EOF, TokenType.valueOf("EOF"));
    }
}