package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.parser.Token.*;
import org.jsoup.parser.TokenType;

import java.util.Arrays;

public class TokenTest {

    @org.junit.Test
    public void testDoctypeReset() {
        Doctype doctype = new Doctype();
        doctype.getName().equals("html"); // assert initial state

        doctype.reset();
        assertEquals(doctype.getClass().getSimpleName(), "");
        assertNull(doctype.getPubSysKey());
        assertNull(doctype.getPublicIdentifier());
        assertNull(doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());
    }

    @org.junit.Test
    public void testDoctypeMethods() {
        Doctype doctype = new Doctype();
        doctype.getName().equals("html");

        doctype.setName("html5");
        assertEquals("html5", doctype.getName());

        doctype.setPubSysKey("foo");
        assertEquals("foo", doctype.getPubSysKey());

        doctype.setPublicIdentifier("public id");
        assertEquals("public id", doctype.getPublicIdentifier());

        doctype.setSystemIdentifier("system id");
        assertEquals("system id", doctype.getSystemIdentifier());

        doctype.setForceQuirks(true);
        assertTrue(doctype.isForceQuirks());
    }

    @org.junit.Test
    public void testTagReset() {
        Tag tag = new StartTag();

        tag.name().equals("div"); // assert initial state

        tag.reset();
        assertEquals(null, tag.tagName);
        assertEquals(null, tag.normalName);
        assertNull(tag.pendingAttributeName);
        assertEquals("", tag.pendingAttributeValue);
        assertFalse(tag.hasEmptyAttributeValue);
        assertFalse(tag.hasPendingAttributeValue);
        assertFalse(tag.isSelfClosing());
        assertNull(tag.attributes);
    }

    @org.junit.Test
    public void testTagMethods() {
        StartTag tag = new StartTag();
        tag.name("div");

        tag.name("span");
        assertEquals("span", tag.name());

        tag.appendTagName("class");
        assertEquals("spanclass", tag.name());

        tag.appendAttributeName("id");
        tag.appendAttributeValue("foo");
        tag.newAttribute();
        assertEquals("id=\"foo\"", tag.toString());

        tag.setEmptyAttributeValue();
        tag.appendAttributeName("class");
        tag.newAttribute();
        assertEquals("<div class></div>", tag.toString());

        tag.appendAttributeValue("bar");
        tag.newAttribute();
        assertEquals("<div class=\"bar\"></div>", tag.toString());

        tag.finaliseTag();
        assertEquals("div\n", tag.toString());

        tag.reset();
        assertEquals(null, tag.attributes);
    }

    @org.junit.Test(expected = AssertionError.class)
    public void testTagInvalidReset() {
        StartTag tag = new StartTag();
        tag.name("div");
        tag.newAttribute();
        tag.finaliseTag();
        tag.reset();
    }

    @org.junit.Test
    public void testCommentReset() {
        Comment comment = new Comment();
        comment.getData().equals(""); // assert initial state

        comment.reset();
        assertEquals(Comment.class.getSimpleName(), "");
        assertFalse(comment.bogus);
    }

    @org.junit.Test
    public void testCommentMethods() {
        Comment comment = new Comment();
        comment.getData().equals("");

        comment.setData("<!-- hello -->");
        assertEquals("<!-- hello -->", comment.getData());
    }

    @org.junit.Test
    public void testCharacterReset() {
        Character character = new Character();
        character.getData().equals(""); // assert initial state

        character.reset();
        assertEquals(Character.class.getSimpleName(), "");
    }

    @org.junit.Test
    public void testCharacterMethods() {
        Character character = new Character();
        character.getData().equals("");

        character.setData("ABC");
        assertEquals("ABC", character.getData());
    }

    @org.junit.Test
    public void testTokenTypes() {
        assertArrayEquals(new TokenType[]{TokenType.Doctype, TokenType.StartTag, TokenType.EndTag,
                                           TokenType.Comment, TokenType.Character, TokenType.EOF},
                TokenType.values());

        assertFalse(TokenType.Doctype.isCharacter());
        assertFalse(TokenType.StartTag.isComment());
        assertFalse(TokenType.EndTag.isComment());
        assertFalse(TokenType.Character.isDoctype());
        assertFalse(TokenType.EOF.isComment());

        assertTrue(new Doctype().isDoctype());
        assertTrue(new StartTag().isStartTag());
        assertTrue(new EndTag().isEndTag());
        assertTrue(new Comment().isComment());
        assertTrue(new Character().isCharacter());
        assertTrue(new EOF().isEOF());
    }
}