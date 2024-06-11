package org.jsoup.parser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.BooleanAttribute;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Element;

public class TokenTest {
    @Test
    public void testDoctypeReset() {
        Token doctype = new Token.Doctype();
        doctype.asDoctype().getName().equals("doctype");

        Token resetDoctype = doctype.reset();
        assertNotEquals(doctype, resetDoctype);
        resetDoctype.asDoctype().getName().equals("");
    }

    @Test
    public void testDoctypeAttributes() {
        Token doctype = new Token.Doctype();
        doctype.asDoctype().getName().equals("doctype");

        doctype.asDoctype().setName("html");
        doctype.asDoctype().getSystemIdentifier().equals("");
        doctype.asDoctype().getPublicIdentifier().equals("");

        doctype.asDoctype().setName("html")
            .setPublicIdentifier("public id")
            .setSystemIdentifier("system id")
            .isForceQuirks();

        Token resetDoctype = doctype.reset();
        resetDoctype.asDoctype().getName().equals("");
        resetDoctype.asDoctype().getPublicIdentifier().equals("");
        resetDoctype.asDoctype().getSystemIdentifier().equals("");
        assertFalse(resetDoctype.asDoctype().isForceQuirks());
    }

    @Test
    public void testTagReset() {
        Token tag = new Token.StartTag();
        tag.asStartTag().name().equals("tag");

        Token resetTag = tag.reset();
        assertNotEquals(tag, resetTag);
        resetTag.asStartTag().name().equals(null);
    }

    @Test
    public void testTagAttributes() {
        Token tag = new Token.StartTag();
        tag.asStartTag().name().equals("tag");

        tag.asStartTag().name("tagName");
        tag.asStartTag().getAttributes().size();

        tag.asStartTag().newAttribute()
            .setAttributeName("attributeName")
            .setAttributeValue("attributeValue");

        tag.asStartTag().newAttribute();
        tag.asStartTag().newAttribute().setAttributeName("booleanAttr").setAttributeValue("true");

        tag.asStartTag().finaliseTag();
        tag.asStartTag().getAttributes().size();

        Tag resetTag = tag.reset();
        resetTag.asStartTag().name().equals(null);
        resetTag.asStartTag().getAttributes().size();
    }

    @Test
    public void testCommentReset() {
        Token comment = new Token.Comment();
        comment.asComment().getData().equals("");

        Token resetComment = comment.reset();
        assertNotEquals(comment, resetComment);
        resetComment.asComment().getData().equals("");
    }

    @Test
    public void testCommentData() {
        Token comment = new Token.Comment();
        comment.asComment().getData().equals("");

        comment.asComment().getData("data");
        comment.asComment().getData().equals("data");

        Token resetComment = comment.reset();
        resetComment.asComment().getData().equals("");
    }

    @Test
    public void testCharacterReset() {
        Token character = new Token.Character();
        character.asCharacter().getData().equals("");

        Token resetCharacter = character.reset();
        assertNotEquals(character, resetCharacter);
        resetCharacter.asCharacter().getData().equals("");
    }

    @Test
    public void testCharacterData() {
        Token character = new Token.Character();
        character.asCharacter().getData().equals("");

        character.asCharacter().data("data");
        character.asCharacter().getData().equals("data");

        Token resetCharacter = character.reset();
        resetCharacter.asCharacter().getData().equals("");
    }

    @Test
    public void testTagToString() {
        Token tag = new Token.StartTag().name("tag");
        String tagString = tag.toString();

        assertEquals("<tag>", tagString);

        tag.asStartTag().newAttribute().setAttributeName("attributeName").setAttributeValue("attributeValue");
        tagString = tag.toString();

        assertEquals("<tag attributeName=\"attributeValue\">", tagString);

        tag.asStartTag().name("tagName").newAttribute().setAttributeName("booleanAttr").setAttributeValue("true");
        tagString = tag.toString();

        assertEquals("<tagName booleanAttr=\"true\">", tagString);

        tag = new Token.EndTag().name("tag");
        tagString = tag.toString();

        assertEquals("</tag>", tagString);
    }

    @Test
    public void testEOF() {
        Token eof = new Token.EOF();

        Token resetEof = eof.reset();
        assertNotEquals(eof, resetEof);
        assertEquals(eof.getClass(), resetEof.getClass());

        assertTrue(eof.isEOF());
        assertTrue(resetEof.isEOF());
    }

    @Test
    public void testTokenType() {
        Token doctype = new Token.Doctype();
        assertTrue(doctype.isDoctype());

        Token tag = new Token.StartTag();
        assertTrue(tag.isStartTag());

        Token comment = new Token.Comment();
        assertTrue(comment.isComment());

        Token character = new Token.Character();
        assertTrue(character.isCharacter());

        Token eof = new Token.EOF();
        assertTrue(eof.isEOF());
    }
}