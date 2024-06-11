package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.Arrays;

public class TokenTest {
    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.getName().equals("doctype");
        doctype.reset();
        assertEquals(null, doctype.getName());
    }

    @Test
    public void testDoctypeGetName() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name("html");
        doctype.reset();
        assertEquals("html", doctype.getName());
    }

    @Test(expected = AssertionError.class)
    public void testDoctypeForceQuirksIsFalseByDefault() {
        Token.Doctype doctype = new Token.Doctype();
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testDoctypeForceQuirksTrue() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.isForceQuirks(true);
        assertTrue(doctype.isForceQuirks());
    }

    @Test
    public void testTagReset() {
        Token.Tag tag = new Token.StartTag();
        tag.name("tagName");
        tag.reset();
        assertNull(tag.tagName);
    }

    @Test
    public void testTagName() {
        Token.StartTag tag = new Token.StartTag();
        tag.name("tagName");
        tag.reset();
        tag.name("newTagName");
        assertEquals("newTagName", tag.name());
    }

    @Test
    public void testTagSelfClosingFalseByDefault() {
        Token.StartTag tag = new Token.StartTag();
        assertFalse(tag.isSelfClosing());
    }

    @Test
    public void testTagSelfClosingTrue() {
        Token.StartTag tag = new Token.StartTag();
        tag.isSelfClosing(true);
        assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testTagAttributesEmpty() {
        Token.StartTag tag = new Token.StartTag();
        assertNull(tag.getAttributes());
    }

    @Test
    public void testTagAttributesNotNull() {
        Token.StartTag tag = new Token.StartTag();
        tag.newAttribute();
        tag.newAttribute().put("key", "value");
        tag.newAttribute().put("key2", "value2");
        Attributes attributes = tag.getAttributes();
        assertEquals(2, attributes.size());
        assertEquals("value", attributes.get("key"));
        assertEquals("value2", attributes.get("key2"));
    }

    @Test(expected = AssertionError.class)
    public void testTagAttributesNullValue() {
        Token.StartTag tag = new Token.StartTag();
        tag.newAttribute().put("key", null);
    }

    @Test(expected = AssertionError.class)
    public void testTagAttributesEmptyStringValue() {
        Token.StartTag tag = new Token.StartTag();
        tag.newAttribute().put("key", "");
    }

    @Test
    public void testTagToString() {
        Token.StartTag tag = new Token.StartTag().name("tagName");
        tag.toString().startsWith("<tagName>");
    }

    @Test
    public void testEndTagToString() {
        Token.EndTag tag = new Token.EndTag().name("tagName");
        tag.toString().startsWith("</tagName>");
    }

    @Test
    public void testCommentToString() {
        Token.Comment comment = new Token.Comment();
        comment.getData().equals("-- COMMENT --");
        String data = "-- Test Comment --";
        comment.getData(data);
        assertEquals(data, comment.getData());
    }

    @Test
    public void testCharacterToString() {
        Token.Character character = new Token.Character();
        character.data('A').toString().equals("A");
    }

    @Test
    public void testCDataToString() {
        Token.CData cData = new Token.CData("[CDATA[Test Data]]");
        cData.toString().startsWith("<![CDATA[Test Data]]>");
    }

    @Test
    public void testEOFToString() {
        Token.EOF eof = new Token.EOF();
        assertEquals("", eof.toString());
    }
}