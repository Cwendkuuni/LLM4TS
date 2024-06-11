package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.parser.Token.*;
import org.jsoup.parser.TokenType;

import java.util.Arrays;

public class TokenTest {

    @org.junit.Test
    public void testDoctypeReset() {
        Doctype doctype = new Doctype();
        doctype.getName().equals("doctype"); // initial test

        doctype.reset();

        assertEquals(TokenType.Doctype, doctype.type);
        assertTrue(doctype.getName().isEmpty());
        assertNull(doctype.getPubSysKey());
        assertTrue(doctype.getPublicIdentifier().isEmpty());
        assertTrue(doctype.getSystemIdentifier().isEmpty());
        assertFalse(doctype.isForceQuirks());
    }

    @org.junit.Test
    public void testDoctypeSetters() {
        Doctype doctype = new Doctype();

        doctype.getName().append("html");
        assertEquals("html", doctype.getName().toString());

        doctype.setForceQuirks(true);
        assertTrue(doctype.isForceQuirks());

        doctype.getPublicIdentifier().append("public id");
        assertEquals("public id", doctype.getPublicIdentifier().toString());

        doctype.getSystemIdentifier().append("system id");
        assertEquals("system id", doctype.getSystemIdentifier().toString());

        doctype.getPubSysKey().append("pub sys key");
        assertEquals("pub sys key", doctype.getPubSysKey());
    }

    @org.junit.Test(expected = AssertionError.class)
    public void testDoctypeSettersNullNames() {
        Doctype doctype = new Doctype();

        doctype.getName().append(null);
    }

    @org.junit.Test
    public void testTagReset() {
        Tag tag = new StartTag();
        tag.tagName = "tag";

        tag.reset();

        assertEquals(TokenType.StartTag, tag.type);
        assertNull(tag.tagName);
        assertNull(tag.normalName);
        assertNull(tag.pendingAttributeName);
        assertTrue(tag.pendingAttributeValue.isEmpty());
        assertTrue(tag.hasPendingAttributeValue == false);
        assertFalse(tag.selfClosing);
        assertNull(tag.attributes);
    }

    @org.junit.Test
    public void testTagNameSetter() {
        StartTag tag = new StartTag();
        tag.name("tagName");
        assertEquals("tagName", tag.tagName);
    }

    @org.junit.Test
    public void testTagNameSetterNull() {
        StartTag tag = new StartTag();
        tag.name(null);
        assertNull(tag.tagName);
    }

    @org.junit.Test
    public void testTagSelfClosingSetter() {
        StartTag tag = new StartTag();
        tag.selfClosing(true);
        assertTrue(tag.selfClosing);

        tag.selfClosing(false);
        assertFalse(tag.selfClosing);
    }

    @org.junit.Test
    public void testTagNewAttribute() {
        StartTag tag = new StartTag();
        tag.newAttribute();
        assertNotNull(tag.attributes);
        assertTrue(tag.attributes.isEmpty());
    }

    @org.junit.Test
    public void testTagNewAttributeWithNameAndValue() {
        StartTag tag = new StartTag();
        tag.appendAttributeName("attrName");
        tag.appendAttributeValue("attrValue");
        tag.newAttribute();
        assertEquals("attrName", tag.attributes.getKey(0));
        assertEquals("attrValue", tag.attributes.getValue(0));
        assertTrue(tag.attributes.size() == 1);
    }

    @org.junit.Test
    public void testTagNewAttributeWithNameAndEmptyValue() {
        StartTag tag = new StartTag();
        tag.appendAttributeName("attrName");
        tag.setEmptyAttributeValue();
        tag.newAttribute();
        assertEquals("attrName", tag.attributes.getKey(0));
        assertEquals("", tag.attributes.getValue(0));
        assertTrue(tag.attributes.size() == 1);
    }

    @org.junit.Test
    public void testTagNewAttributeWithNameAndValueAndAnotherAttribute() {
        StartTag tag = new StartTag();
        tag.appendAttributeName("attrName1");
        tag.appendAttributeValue("attrValue1");
        tag.appendAttributeName("attrName2");
        tag.appendAttributeValue("attrValue2");
        tag.newAttribute();
        assertEquals("attrName1", tag.attributes.getKey(0));
        assertEquals("attrValue1", tag.attributes.getValue(0));
        assertEquals("attrName2", tag.attributes.getKey(1));
        assertEquals("attrValue2", tag.attributes.getValue(1));
        assertTrue(tag.attributes.size() == 2);
    }

    @org.junit.Test(expected = AssertionError.class)
    public void testTagSettersWhenAttributeValueIsNull() {
        StartTag tag = new StartTag();
        tag.appendAttributeValue(null);
    }

    @org.junit.Test(expected = AssertionError.class)
    public void testTagSettersWhenAttributeNameIsNull() {
        StartTag tag = new StartTag();
        tag.appendAttributeName(null);
    }

    @org.junit.Test
    public void testCommentReset() {
        Comment comment = new Comment();
        comment.data.append("comment");

        comment.reset();

        assertEquals(TokenType.Comment, comment.type);
        assertTrue(comment.data.isEmpty());
        assertFalse(comment.bogus);
    }

    @org.junit.Test
    public void testCharacterReset() {
        Character character = new Character();
        character.data.append("char");

        character.reset();

        assertEquals(TokenType.Character, character.type);
        assertNull(character.data);
    }

    @org.junit.Test
    public void testCharacterSetters() {
        Character character = new Character();
        character.data.append("char");

        String expected = "char";
        assertEquals(expected, character.getData());

        character.data("newChar");
        assertEquals(expected, character.getData());
        assertEquals("newChar", character.getData());
    }

    @org.junit.Test(expected = AssertionError.class)
    public void testCharacterSettersNull() {
        Character character = new Character();
        character.data(null);
    }

    @org.junit.Test
    public void testTokenTypeIdentification() {
        Token token = new Doctype();
        assertFalse(token.isDoctype() == false);
        assertTrue(token.isDoctype());

        token = new StartTag();
        assertFalse(token.isDoctype() == false);
        assertFalse(token.isDoctype());
        assertFalse(token.isStartTag() == false);
        assertTrue(token.isStartTag());

        token = new EndTag();
        assertFalse(token.isDoctype() == false);
        assertFalse(token.isDoctype());
        assertFalse(token.isStartTag() == false);
        assertTrue(token.isEndTag());

        token = new Comment();
        assertFalse(token.isDoctype() == false);
        assertFalse(token.isDoctype());
        assertFalse(token.isStartTag() == false);
        assertFalse(token.isEndTag() == false);
        assertTrue(token.isComment());

        token = new Character();
        assertFalse(token.isDoctype() == false);
        assertFalse(token.isDoctype());
        assertFalse(token.isStartTag() == false);
        assertFalse(token.isEndTag() == false);
        assertFalse(token.isComment() == false);
        assertTrue(token.isCharacter());

        token = new CData();
        assertFalse(token.isDoctype() == false);
        assertFalse(token.isDoctype());
        assertFalse(token.isStartTag() == false);
        assertFalse(token.isEndTag() == false);
        assertFalse(token.isComment() == false);
        assertTrue(token.isCharacter());
        assertTrue(token.isCData());

        token = new EOF();
        assertFalse(token.isDoctype() == false);
        assertFalse(token.isDoctype());
        assertFalse(token.isStartTag() == false);
        assertFalse(token.isEndTag() == false);
        assertFalse(token.isComment() == false);
        assertFalse(token.isCharacter() == false);
        assertTrue(token.isEOF());
    }
}