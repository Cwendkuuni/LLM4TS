package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.jsoup.parser.TokenType.Doctype;
import static org.jsoup.parser.TokenType.EndTag;
import static org.jsoup.parser.TokenType.StartTag;
import static org.jsoup.parser.TokenType.Comment;
import static org.jsoup.parser.TokenType.Character;
import static org.jsoup.parser.TokenType.EOF;

import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Doctype doctype = new Doctype();
        doctype.name("html");
        assertEquals("html", doctype.getName());

        doctype.reset();

        assertEquals(null, doctype.getName());
    }

    @Test
    public void testTagReset() {
        StartTag startTag = new StartTag();
        startTag.name("div");
        assertEquals("div", startTag.name());

        startTag.reset();

        assertNull(startTag.name());
    }

    @Test
    public void testCommentReset() {
        Comment comment = new Comment();
        comment.data("This is a comment.");
        assertEquals("This is a comment.", comment.getData());

        comment.reset();

        assertNull(comment.getData());
    }

    @Test
    public void testCharacterReset() {
        Character character = new Character();
        character.data("A");
        assertEquals("A", character.getData());

        character.reset();

        assertNull(character.getData());
    }

    @Test
    public void testDoctypeType() {
        Doctype doctype = new Doctype();
        assertEquals(Doctype.class, doctype.getClass());
        assertEquals(Doctype, doctype.type);
    }

    @Test
    public void testStartTagType() {
        StartTag startTag = new StartTag();
        assertEquals(StartTag.class, startTag.getClass());
        assertEquals(StartTag, startTag.type);
    }

    @Test
    public void testEndTagType() {
        EndTag endTag = new EndTag();
        assertEquals(EndTag.class, endTag.getClass());
        assertEquals(EndTag, endTag.type);
    }

    @Test
    public void testCommentType() {
        Comment comment = new Comment();
        assertEquals(Comment.class, comment.getClass());
        assertEquals(Comment, comment.type);
    }

    @Test
    public void testCharacterType() {
        Character character = new Character();
        assertEquals(Character.class, character.getClass());
        assertEquals(Character, character.type);
    }

    @Test
    public void testEOFType() {
        EOF eof = new EOF();
        assertEquals(EOF.class, eof.getClass());
        assertEquals(EOF, eof.type);
    }

    @Test
    public void testDoctypeIsDoctype() {
        Doctype doctype = new Doctype();
        assertTrue(doctype.isDoctype());
    }

    @Test
    public void testStartTagIsStartTag() {
        StartTag startTag = new StartTag();
        assertTrue(startTag.isStartTag());
    }

    @Test
    public void testEndTagIsEndTag() {
        EndTag endTag = new EndTag();
        assertTrue(endTag.isEndTag());
    }

    @Test
    public void testCommentIsComment() {
        Comment comment = new Comment();
        assertTrue(comment.isComment());
    }

    @Test
    public void testCharacterIsCharacter() {
        Character character = new Character();
        assertTrue(character.isCharacter());
    }

    @Test
    public void testEOFIsEOF() {
        EOF eof = new EOF();
        assertTrue(eof.isEOF());
    }

    @Test
    public void testDoctypeToString() {
        Doctype doctype = new Doctype();
        assertEquals("<!DOCTYPE html>", doctype.toString());
    }

    @Test
    public void testStartTagToString() {
        StartTag startTag = new StartTag().name("div");
        assertEquals("<div>", startTag.toString());
    }

    @Test
    public void testEndTagToString() {
        EndTag endTag = new EndTag().name("div");
        assertEquals("</div>", endTag.toString());
    }

    @Test
    public void testCommentToString() {
        Comment comment = new Comment().data("This is a comment.");
        assertEquals("<!--This is a comment.-->", comment.toString());
    }

    @Test
    public void testCharacterToString() {
        Character character = new Character().data("A");
        assertEquals("A", character.toString());
    }

    @Test
    public void testEOFToString() {
        EOF eof = new EOF();
        assertEquals("", eof.toString());
    }
}