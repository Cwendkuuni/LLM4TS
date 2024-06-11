package org.jsoup.parser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Token doctype = new Token.Doctype();
        doctype.asDoctype().getName = "html";
        doctype.asDoctype().getPublicIdentifier().append("public");
        doctype.asDoctype().getSystemIdentifier().append("system");
        doctype.asDoctype().isForceQuirks = true;

        Token resetDoctype = doctype.reset();
        assertTrue(resetDoctype instanceof Token.Doctype);
        assertFalse(resetDoctype.asDoctype().getName.toString().equals("html"));
        assertNull(resetDoctype.asDoctype().getPublicIdentifier());
        assertNull(resetDoctype.asDoctype().getSystemIdentifier());
        assertFalse(resetDoctype.asDoctype().isForceQuirks());
    }

    @Test
    public void testTagReset() {
        Token tag = new Token.StartTag();
        tag.asStartTag().name = "div";
        tag.asStartTag().attributes = new Attributes();
        tag.asStartTag().attributes.put("class", "container");

        Token resetTag = tag.reset();
        assertTrue(resetTag instanceof Token.StartTag);
        assertNull(resetTag.asStartTag().name);
        assertNull(resetTag.asStartTag().attributes);
    }

    @Test
    public void testTagNewAttribute() {
        Token tag = new Token.StartTag();
        tag.asStartTag().name = "input";
        tag.asStartTag().newAttribute();
        tag.asStartTag().appendAttributeName("type");
        tag.asStartTag().appendAttributeValue("text");

        Token tag2 = tag.reset();
        tag2.asStartTag().newAttribute();
        tag2.asStartTag().appendAttributeName("name");
        tag2.asStartTag().appendAttributeValue("username");

        assertTrue(tag.asStartTag().attributes.size() == 1);
        assertTrue(tag2.asStartTag().attributes.size() == 2);
        assertEquals("text", tag.asStartTag().attributes.get("type").getValue());
        assertEquals("username", tag2.asStartTag().attributes.get("name").getValue());
    }

    @Test
    public void testTagFinaliseTag() {
        Token tag = new Token.StartTag();
        tag.asStartTag().name = "div";
        tag.asStartTag().newAttribute();
        tag.asStartTag().appendAttributeName("class");
        tag.asStartTag().appendAttributeValue("container");
        tag.asStartTag().finaliseTag();

        assertTrue(tag.asStartTag().attributes.size() == 1);
    }

    @Test
    public void testCommentReset() {
        Token comment = new Token.Comment();
        comment.asComment().getData().append("This is a comment");

        Token resetComment = comment.reset();
        assertTrue(resetComment instanceof Token.Comment);
        assertNull(resetComment.asComment().getData());
    }

    @Test
    public void testCharacterReset() {
        Token character = new Token.Character();
        character.asCharacter().data = "Hello World!";

        Token resetCharacter = character.reset();
        assertTrue(resetCharacter instanceof Token.Character);
        assertNull(resetCharacter.asCharacter().data);
    }

    @Test
    public void testCharacterData() {
        Token character = new Token.Character();
        character.asCharacter().data("Hello World!");

        assertEquals("Hello World!", character.asCharacter().getData());
    }

    @Test
    public void testEOFReset() {
        Token eof = new Token.EOF();

        Token resetEof = eof.reset();
        assertTrue(resetEof instanceof Token.EOF);
    }
}