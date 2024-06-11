package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.BooleanAttribute;
import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Doctype doctype = new Doctype();
        doctype.getName().equals("html");
        assertEquals("html", doctype.getName());

        doctype.reset();

        assertNull(doctype.getName());
    }

    @Test
    public void testDoctypeGetters() {
        Doctype doctype = new Doctype();
        doctype.getName().equals("html");
        doctype.setName("html");

        assertEquals("html", doctype.getName());
        assertEquals("html", doctype.normalName());
        assertFalse(doctype.isForceQuirks());

        doctype.isForceQuirks(true);
        assertTrue(doctype.isForceQuirks());
    }

    @Test
    public void testTagReset() {
        StartTag startTag = new StartTag();
        assertNull(startTag.tagName);

        startTag.name("html").reset();

        assertNull(startTag.tagName);
    }

    @Test
    public void testTagGetters() {
        StartTag startTag = new StartTag();
        startTag.name("html");

        assertEquals("html", startTag.name());
        assertEquals("html", startTag.normalName());
        assertFalse(startTag.isSelfClosing());

        startTag.isSelfClosing(true);
        assertTrue(startTag.isSelfClosing());
    }

    @Test
    public void testTagSetters() {
        StartTag startTag = new StartTag();

        startTag.name("html").attributes(new Attributes())
                .newAttribute().name("class").value("class-name").setEmptyAttributeValue()
                .newAttribute().name("id").value("id-name");

        assertEquals("html", startTag.name());
        assertEquals("html", startTag.normalName());
        assertFalse(startTag.isSelfClosing());
        assertSame(startTag.getAttributes(), startTag.attributes);
        assertEquals(2, startTag.getAttributes().size());
        assertEquals("class-name", startTag.getAttributes().getClassAttribute("class").getName());
        assertEquals("id-name", startTag.getAttributes().idAttribute("id").getValue());
    }

    @Test
    public void testTagResetAttributes() {
        StartTag startTag = new StartTag();
        startTag.name("html").attributes(new Attributes())
                .newAttribute().name("class").value("class-name").setEmptyAttributeValue()
                .newAttribute().name("id").value("id-name");

        assertEquals(2, startTag.getAttributes().size());

        startTag.reset();

        assertNull(startTag.tagName);
        assertNull(startTag.getAttributes());
    }

    @Test
    public void testTagFinaliseTag() {
        StartTag startTag = new StartTag();
        startTag.name("html").attributes(new Attributes())
                .newAttribute().name("class").value("class-name").setEmptyAttributeValue()
                .newAttribute().name("id").value("id-name");

        assertEquals(2, startTag.getAttributes().size());

        startTag.finaliseTag();

        assertNull(startTag.getAttributes());
    }

    @Test
    public void testCommentReset() {
        Comment comment = new Comment();
        comment.getData().equals("comment-data");

        assertEquals("comment-data", comment.getData());

        comment.reset();

        assertNull(comment.getData());
    }

    @Test
    public void testCommentGetters() {
        Comment comment = new Comment();
        comment.getData().equals("comment-data");
        comment.setData("comment-data");

        assertEquals("comment-data", comment.getData());

        comment.bogus = true;

        assertTrue(comment.isBogus());
    }

    @Test
    public void testCharacterReset() {
        Character character = new Character();
        character.getData().equals("character-data");

        assertEquals("character-data", character.getData());

        character.reset();

        assertNull(character.getData());
    }

    @Test
    public void testCharacterGetters() {
        Character character = new Character();
        character.getData().equals("character-data");
        character.setData("character-data");

        assertEquals("character-data", character.getData());
    }

    @Test
    public void testTokenTypes() {
        Token token = new Token();

        assertFalse(token.isDoctype());
        assertFalse(token.isStartTag());
        assertFalse(token.isEndTag());
        assertFalse(token.isComment());
        assertFalse(token.isCharacter());
        assertFalse(token.isEOF());

        Doctype doctype = new Doctype();

        assertTrue(doctype.isDoctype());
        assertFalse(doctype.isStartTag());
        assertFalse(doctype.isEndTag());
        assertFalse(doctype.isComment());
        assertFalse(doctype.isCharacter());
        assertFalse(doctype.isEOF());

        StartTag startTag = new StartTag();

        assertFalse(startTag.isDoctype());
        assertTrue(startTag.isStartTag());
        assertFalse(startTag.isEndTag());
        assertFalse(startTag.isComment());
        assertFalse(startTag.isCharacter());
        assertFalse(startTag.isEOF());

        EndTag endTag = new EndTag();

        assertFalse(endTag.isDoctype());
        assertFalse(endTag.isStartTag());
        assertTrue(endTag.isEndTag());
        assertFalse(endTag.isComment());
        assertFalse(endTag.isCharacter());
        assertFalse(endTag.isEOF());

        Comment comment = new Comment();

        assertFalse(comment.isDoctype());
        assertFalse(comment.isStartTag());
        assertFalse(comment.isEndTag());
        assertTrue(comment.isComment());
        assertFalse(comment.isCharacter());
        assertFalse(comment.isEOF());

        Character character = new Character();

        assertFalse(character.isDoctype());
        assertFalse(character.isStartTag());
        assertFalse(character.isEndTag());
        assertFalse(character.isComment());
        assertTrue(character.isCharacter());
        assertFalse(character.isEOF());

        EOF eof = new EOF();

        assertFalse(eof.isDoctype());
        assertFalse(eof.isStartTag());
        assertFalse(eof.isEndTag());
        assertFalse(eof.isComment());
        assertFalse(eof.isCharacter());
        assertTrue(eof.isEOF());
    }
}