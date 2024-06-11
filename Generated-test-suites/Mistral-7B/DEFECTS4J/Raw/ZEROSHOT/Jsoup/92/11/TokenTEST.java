package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctypeToken() {
        Token doctype = new Token.Doctype();

        assertEquals("Doctype", doctype.tokenType());
        doctype.reset();
        assertNull(doctype.getName());
        assertNull(doctype.getPubSysKey());
        assertNull(doctype.getPublicIdentifier());
        assertNull(doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());

        doctype.getName("html");
        assertEquals("html", doctype.getName());
        assertEquals("HTML", doctype.name());

        doctype.setPubSysKey("pubSysKey");
        assertEquals("pubSysKey", doctype.getPubSysKey());

        doctype.getPublicIdentifier().append("publicIdentifier");
        assertEquals("publicIdentifier", doctype.getPublicIdentifier().toString());

        doctype.getSystemIdentifier().append("systemIdentifier");
        assertEquals("systemIdentifier", doctype.getSystemIdentifier().toString());

        doctype.isForceQuirks(true);
        assertTrue(doctype.isForceQuirks());

        doctype.reset();
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testTagToken() {
        Token tag = new Token.Tag();

        assertEquals("Tag", tag.tokenType());
        tag.reset();
        assertNull(tag.getTagName());
        assertNull(tag.getNormalName());
        assertNull(tag.getPendingAttributeName());
        assertNull(tag.getPendingAttributeValue());
        assertNull(tag.getPendingAttributeValueS());
        assertFalse(tag.isSelfClosing());
        assertNull(tag.getAttributes());

        tag.name("div");
        assertEquals("div", tag.getTagName());
        assertEquals("div", tag.name());
        assertEquals("div", tag.getNormalName());

        tag.isSelfClosing(true);
        assertTrue(tag.isSelfClosing());

        tag.newAttribute();
        tag.appendAttributeName("id");
        tag.appendAttributeValue("idValue");
        tag.finaliseTag();
        Attributes attributes = tag.getAttributes();
        assertEquals("idValue", attributes.getValue("id"));

        tag.reset();
        assertNull(tag.getAttributes());

        tag.appendAttributeValue("class");
        tag.newAttribute();
        tag.appendAttributeName("class");
        tag.appendAttributeValue("classValue");
        tag.finaliseTag();
        attributes = tag.getAttributes();
        assertEquals("classValue", attributes.getValue("class"));

        tag.reset();
        assertNull(tag.getAttributes());

        tag.appendAttributeValue("style");
        tag.newAttribute();
        tag.appendAttributeName("style");
        tag.appendAttributeValue("styleValue");
        tag.finaliseTag();
        attributes = tag.getAttributes();
        assertEquals("styleValue", attributes.getValue("style"));

        tag.reset();
        assertNull(tag.getAttributes());

        tag.appendAttributeValue("style");
        tag.appendAttributeValue(" styleValue ");
        tag.newAttribute();
        tag.appendAttributeName("style");
        tag.finaliseTag();
        attributes = tag.getAttributes();
        assertEquals("styleValue", attributes.getValue("style"));

        tag.reset();
        assertNull(tag.getAttributes());

        tag.appendAttributeValue("class");
        tag.newAttribute();
        tag.appendAttributeName("class");
        tag.finaliseTag();
        attributes = tag.getAttributes();
        assertNull(attributes.getValue("class"));

        tag.reset();
        assertNull(tag.getAttributes());
    }

    @Test
    public void testCommentToken() {
        Token comment = new Token.Comment();

        assertEquals("Comment", comment.tokenType());
        comment.reset();
        assertNull(comment.getData());
        assertFalse(comment.isBogus());

        comment.getData().append("--comment--");
        assertEquals("--comment--", comment.getData().toString());
        assertEquals("--comment--", comment.toString());

        comment.isBogus(true);
        assertTrue(comment.isBogus());

        comment.reset();
        assertFalse(comment.isBogus());

        comment.getData().append("--");
        comment.isBogus(true);
        assertTrue(comment.isBogus());

        comment.reset();
        assertFalse(comment.isBogus());
    }

    @Test
    public void testCharacterToken() {
        Token character = new Token.Character();

        assertEquals("Character", character.tokenType());
        character.reset();
        assertNull(character.getData());

        character.getData("text");
        assertEquals("text", character.getData().toString());
        assertEquals("text", character.toString());

        List<Character> characters = new ArrayList<>();
        characters.add(character.data("text1"));
        characters.add(character.data("text2"));
        characters.add(character.data("text3"));
        for (Character c : characters) {
            assertEquals("text" + (c.isCData() ? "Data" : ""), c.tokenType());
        }

        character.reset();
        assertNull(character.getData());
    }

    @Test
    public void testEOFToken() {
        Token eof = new Token.EOF();

        assertEquals("EOF", eof.tokenType());
        eof.reset();

        assertFalse(eof.isDoctype());
        assertFalse(eof.isStartTag());
        assertFalse(eof.isEndTag());
        assertFalse(eof.isComment());
        assertFalse(eof.isCharacter());
        assertFalse(eof.isCData());

        assertTrue(eof.isEOF());
    }

    @Test(expected = IllegalStateException.class)
    public void testIllegalStateException() {
        Token tag = new Token.Tag();
        tag.appendAttributeName("id");
        tag.appendAttributeValue("value");
        tag.newAttribute();
        tag.appendAttributeName("id");
        tag.appendAttributeValue("value2");
        tag.finaliseTag();
        tag.newAttribute();
    }
}