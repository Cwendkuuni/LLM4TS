package org.jsoup.parser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.instanceOf;

import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Doctype doctype = new Doctype();
        doctype.getName().equals("html");
        doctype.reset();
        assertNull(doctype.getName());
    }

    @Test
    public void testDoctypeGetters() {
        Doctype doctype = new Doctype();
        doctype.getName().equals("html");

        assertEquals("html", doctype.getName());
        assertEquals("html", doctype.toString());
        assertFalse(doctype.isForceQuirks());

        doctype.forceQuirks = true;
        assertTrue(doctype.isForceQuirks());
    }

    @Test
    public void testStartTagReset() {
        StartTag startTag = new StartTag();
        startTag.tagName = "div";

        startTag.reset();
        assertNull(startTag.tagName);
    }

    @Test
    public void testStartTagGetters() {
        StartTag startTag = new StartTag();
        startTag.tagName = "div";

        assertEquals("div", startTag.tagName());
        assertEquals("div", startTag.normalName());
        assertFalse(startTag.isSelfClosing());

        startTag.selfClosing = true;
        assertTrue(startTag.isSelfClosing());

        assertEquals("<div>", startTag.toString());
    }

    @Test
    public void testEndTagReset() {
        EndTag endTag = new EndTag();
        endTag.tagName = "div";

        endTag.reset();
        assertNull(endTag.tagName);
    }

    @Test
    public void testEndTagGetters() {
        EndTag endTag = new EndTag();
        endTag.tagName = "div";

        assertEquals("div", endTag.tagName());
        assertEquals("div", endTag.normalName());
        assertFalse(endTag.isSelfClosing());

        assertEquals("</div>", endTag.toString());
    }

    @Test
    public void testCommentReset() {
        Comment comment = new Comment();
        comment.data.equals("This is a comment.");

        comment.reset();
        assertNull(comment.getData());
        assertFalse(comment.isBogus());
    }

    @Test
    public void testCommentGetters() {
        Comment comment = new Comment();
        comment.data.equals("This is a comment.");

        assertEquals("This is a comment.", comment.getData());
        assertEquals("<!--This is a comment-->", comment.toString());
        assertFalse(comment.isBogus());

        comment.bogus = true;
        assertTrue(comment.isBogus());
    }

    @Test
    public void testCharacterReset() {
        Character character = new Character();
        character.data = "A";

        character.reset();
        assertNull(character.getData());
    }

    @Test
    public void testCharacterGetters() {
        Character character = new Character();
        character.data = "A";

        assertEquals("A", character.getData());
        assertEquals("A", character.toString());
    }

    @Test
    public void testTokenTypes() {
        Token doctype = new Doctype();
        Token startTag = new StartTag();
        Token endTag = new EndTag();
        Token comment = new Comment();
        Token character = new Character();
        Token eof = new EOF();

        assertTrue(doctype.isDoctype());
        assertTrue(startTag.isStartTag());
        assertTrue(endTag.isEndTag());
        assertTrue(comment.isComment());
        assertTrue(character.isCharacter());
        assertTrue(eof.isEOF());

        assertFalse(doctype.isComment());
        assertFalse(doctype.isCharacter());
        assertFalse(doctype.isStartTag());
        assertFalse(doctype.isEndTag());
        assertFalse(doctype.isEOF());

        assertSame(Doctype.class, doctype.getClass());
        assertSame(StartTag.class, startTag.getClass());
        assertSame(EndTag.class, endTag.getClass());
        assertSame(Comment.class, comment.getClass());
        assertSame(Character.class, character.getClass());
        assertSame(EOF.class, eof.getClass());
    }
}