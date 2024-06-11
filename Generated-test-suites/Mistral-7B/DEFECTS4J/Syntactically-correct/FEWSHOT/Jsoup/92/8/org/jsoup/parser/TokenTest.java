package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;

import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Token doctype = new Token.Doctype();
        doctype.asDoctype().getName("html");
        doctype.asDoctype().setForceQuirks(true);
        doctype = doctype.reset();
        assertNull(doctype.tokenType());
        assertNull(doctype.asDoctype().getName());
        assertFalse(doctype.asDoctype().isForceQuirks());
    }

    @Test
    public void testTagReset() {
        Token tag = new Token.StartTag();
        tag.name("div");
        tag.newAttribute();
        tag.newAttribute().name("class").value("class1 class2");
        tag = tag.reset();
        assertNull(tag.tokenType());
        assertNull(tag.name());
        assertNull(tag.getAttributes());
    }

    @Test
    public void testCommentReset() {
        Token comment = new Token.Comment();
        comment.asComment().getData("This is a comment");
        comment = comment.reset();
        assertNull(comment.tokenType());
        assertNull(comment.getData());
    }

    @Test
    public void testCharacterReset() {
        Token character = new Token.Character();
        character.asCharacter().data("A");
        character = character.reset();
        assertNull(character.tokenType());
        assertNull(character.getData());
    }

    @Test
    public void testTokenTypeIdentification() {
        Token doctype = new Token.Doctype();
        Token tag = new Token.StartTag();
        Token comment = new Token.Comment();
        Token character = new Token.Character();
        Token eof = new Token.EOF();

        assertTrue(doctype.isDoctype());
        assertTrue(tag.isStartTag());
        assertTrue(comment.isComment());
        assertTrue(character.isCharacter());
        assertFalse(eof.isDoctype());
        assertFalse(eof.isStartTag());
        assertFalse(eof.isComment());
        assertFalse(eof.isCharacter());
    }
}