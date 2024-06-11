package org.jsoup.parser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Token doctype = new Token.Doctype();
        String nameBefore = doctype.asDoctype().getName();
        doctype.reset();
        assertNull(doctype.asDoctype().getName());
    }

    @Test
    public void testDoctypeSetName() {
        Token doctype = new Token.Doctype();
        doctype.asDoctype().name("HTML");
        assertEquals("HTML", doctype.asDoctype().getName());
    }

    @Test
    public void testDoctypeGetPublicIdentifier() {
        Token doctype = new Token.Doctype();
        doctype.asDoctype().publicIdentifier("publicId");
        assertEquals("publicId", doctype.asDoctype().getPublicIdentifier());
    }

    @Test
    public void testDoctypeGetSystemIdentifier() {
        Token doctype = new Token.Doctype();
        doctype.asDoctype().systemIdentifier("systemId");
        assertEquals("systemId", doctype.asDoctype().getSystemIdentifier());
    }

    @Test
    public void testDoctypeIsForceQuirks() {
        Token doctype = new Token.Doctype();
        doctype.asDoctype().isForceQuirks();
        assertTrue(doctype.asDoctype().isForceQuirks());
    }

    @Test
    public void testTagReset() {
        Token tag = new Token.Tag();
        String nameBefore = tag.asTag().name();
        tag.reset();
        assertNull(tag.asTag().name());
    }

    @Test
    public void testTagSetName() {
        Token tag = new Token.Tag();
        tag.asTag().name("div");
        assertEquals("div", tag.asTag().name());
    }

    @Test
    public void testTagIsSelfClosing() {
        Token tag = new Token.Tag();
        tag.asTag().isSelfClosing(true);
        assertTrue(tag.asTag().isSelfClosing());
    }

    @Test
    public void testTagName() {
        Token tag = new Token.Tag();
        tag.asTag().name("input");
        assertEquals("input", tag.asTag().name());
    }

    @Test
    public void testTagNormalName() {
        Token tag = new Token.Tag();
        tag.asTag().name("INPUT");
        assertEquals("input", tag.asTag().normalName());
    }

    @Test
    public void testTagGetAttributes() {
        Token tag = new Token.Tag();
        tag.asTag().getAttributes();
        assertNull(tag.asTag().getAttributes());
    }

    @Test
    public void testTagNewAttribute() {
        Token tag = new Token.Tag();
        tag.asTag().newAttribute();
        assertNull(tag.asTag().getAttributes());
    }

    @Test
    public void testTagFinaliseTag() {
        Token tag = new Token.Tag();
        tag.asTag().finaliseTag();
        assertNull(tag.asTag().getAttributes());
    }

    @Test
    public void testStartTagReset() {
        Token tag = new Token.StartTag();
        String nameBefore = tag.asStartTag().name();
        tag.reset();
        assertNull(tag.asStartTag().name());
    }

    @Test
    public void testStartTagSetAttributes() {
        Token tag = new Token.StartTag();
        tag.asStartTag().name("button").attributes(new Attributes().put("class", "button"));
        assertEquals("button", tag.asStartTag().name());
        assertEquals("button class=button", tag.asStartTag().toString());
    }

    @Test
    public void testEndTagReset() {
        Token tag = new Token.EndTag();
        String nameBefore = tag.asEndTag().name();
        tag.reset();
        assertNull(tag.asEndTag().name());
    }

    @Test
    public void testCommentReset() {
        Token comment = new Token.Comment();
        String dataBefore = comment.asComment().getData();
        comment.reset();
        assertNull(comment.asComment().getData());
    }

    @Test
    public void testCommentSetData() {
        Token comment = new Token.Comment();
        comment.asComment().data("This is a comment");
        assertEquals("This is a comment", comment.asComment().getData());
    }

    @Test
    public void testCommentToString() {
        Token comment = new Token.Comment();
        comment.asComment().data("This is a comment");
        assertEquals("<!--This is a comment-->", comment.toString());
    }

    @Test
    public void testCharacterReset() {
        Token character = new Token.Character();
        String dataBefore = character.asCharacter().getData();
        character.reset();
        assertNull(character.asCharacter().getData());
    }

    @Test
    public void testCharacterSetData() {
        Token character = new Token.Character();
        character.asCharacter().data("Character data");
        assertEquals("Character data", character.asCharacter().getData());
    }

    @Test
    public void testCharacterToString() {
        Token character = new Token.Character();
        character.asCharacter().data("Character data");
        assertEquals("Character data", character.toString());
    }

    @Test
    public void testTokenTypeDoctype() {
        Token token = new Token.Doctype();
        assertTrue(token.isDoctype());
    }

    @Test
    public void testTokenTypeStartTag() {
        Token token = new Token.StartTag();
        assertTrue(token.isStartTag());
    }

    @Test
    public void testTokenTypeEndTag() {
        Token token = new Token.EndTag();
        assertTrue(token.isEndTag());
    }

    @Test
    public void testTokenTypeComment() {
        Token token = new Token.Comment();
        assertTrue(token.isComment());
    }

    @Test
    public void testTokenTypeCharacter() {
        Token token = new Token.Character();
        assertTrue(token.isCharacter());
    }

    @Test
    public void testTokenTypeEOF() {
        Token token = new Token.EOF();
        assertTrue(token.isEOF());
    }
}