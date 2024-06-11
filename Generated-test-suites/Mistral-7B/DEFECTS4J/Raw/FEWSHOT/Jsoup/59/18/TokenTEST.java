package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.BooleanAttribute;
import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Token doctype = new Token.Doctype();
        assertEquals(TokenType.Doctype, doctype.type);

        doctype.reset();

        assertEquals(TokenType.TOKEN_TYPE_UNKNOWN, doctype.type);
        assertNull(doctype.name);
        assertNull(doctype.pubSysKey);
        assertNull(doctype.publicIdentifier);
        assertNull(doctype.systemIdentifier);
        assertFalse(doctype.forceQuirks);
    }

    @Test
    public void testDoctypeGetters() {
        Token doctype = new Token.Doctype();

        doctype.name = "html";
        doctype.pubSysKey = "DTD HTML5";
        doctype.publicIdentifier = "W3C_HTML_Markup_Version_5";
        doctype.systemIdentifier = "/foo/bar.html";
        doctype.forceQuirks = true;

        assertEquals("html", doctype.getName());
        assertEquals("DTD HTML5", doctype.getPubSysKey());
        assertEquals("W3C_HTML_Markup_Version_5", doctype.getPublicIdentifier());
        assertEquals("/foo/bar.html", doctype.getSystemIdentifier());
        assertTrue(doctype.isForceQuirks());
    }

    @Test
    public void testTagReset() {
        Token tag = new Token.Tag();
        assertEquals(TokenType.TAG, tag.type);

        tag.reset();

        assertEquals(TokenType.TOKEN_TYPE_UNKNOWN, tag.type);
        assertNull(tag.tagName);
        assertNull(tag.normalName);
        assertNull(tag.pendingAttributeName);
        assertNull(tag.pendingAttributeValue);
        assertNull(tag.pendingAttributeValueS);
        assertFalse(tag.hasEmptyAttributeValue);
        assertFalse(tag.hasPendingAttributeValue);
        assertFalse(tag.selfClosing);
        assertNull(tag.attributes);
    }

    @Test
    public void testTagNewAttribute() {
        Token tag = new Token.Tag();

        tag.pendingAttributeName = "class";
        tag.pendingAttributeValue = new StringBuilder("red foo");

        tag.newAttribute();

        Attributes attributes = tag.getAttributes();
        assertTrue(attributes != null);
        assertEquals("class", attributes.keySet().iterator().next());
        assertEquals("red foo", attributes.get("class").getValue());

        assertNull(tag.pendingAttributeName);
        assertNull(tag.pendingAttributeValue);
        assertNull(tag.pendingAttributeValueS);
        assertFalse(tag.hasEmptyAttributeValue);
        assertFalse(tag.hasPendingAttributeValue);
        assertFalse(tag.selfClosing);
    }

    @Test
    public void testTagFinaliseTag() {
        Token tag = new Token.Tag();

        tag.pendingAttributeName = "class";
        tag.pendingAttributeValue = new StringBuilder("red foo");

        tag.newAttribute();
        tag.finaliseTag();

        Attributes attributes = tag.getAttributes();
        assertTrue(attributes != null);
        assertEquals("class", attributes.keySet().iterator().next());
        assertEquals("red foo", attributes.get("class").getValue());

        assertNull(tag.pendingAttributeName);
        assertNull(tag.pendingAttributeValue);
        assertNull(tag.pendingAttributeValueS);
        assertFalse(tag.hasEmptyAttributeValue);
        assertFalse(tag.hasPendingAttributeValue);
        assertFalse(tag.selfClosing);
    }

    @Test
    public void testTagSetEmptyAttributeValue() {
        Token tag = new Token.Tag();

        tag.pendingAttributeName = "class";

        tag.setEmptyAttributeValue();

        Attributes attributes = tag.getAttributes();
        assertTrue(attributes != null);
        assertTrue(attributes.size() == 1);
        assertTrue(attributes.get("class") instanceof BooleanAttribute);
        assertFalse(tag.hasPendingAttributeValue);
    }

    @Test
    public void testTagGetters() {
        Token tag = new Token.Tag();

        tag.tagName = "div";
        tag.normalName = "div";
        tag.selfClosing = true;

        assertEquals("div", tag.name());
        assertEquals("div", tag.normalName());
        assertTrue(tag.isSelfClosing());
        assertFalse(tag.isStartTag());
        assertFalse(tag.isEndTag());
        assertFalse(tag.isComment());
        assertFalse(tag.isCharacter());
        assertFalse(tag.isEOF());
        assertNull(tag.getAttributes());
    }

    @Test
    public void testStartTagReset() {
        Token tag = new Token.StartTag();
        assertEquals(TokenType.START_TAG, tag.type);

        tag.reset();

        assertEquals(TokenType.TOKEN_TYPE_UNKNOWN, tag.type);
        assertNull(tag.tagName);
        assertNull(tag.normalName);
        assertNull(tag.pendingAttributeName);
        assertNull(tag.pendingAttributeValue);
        assertNull(tag.pendingAttributeValueS);
        assertFalse(tag.hasEmptyAttributeValue);
        assertFalse(tag.hasPendingAttributeValue);
        assertFalse(tag.selfClosing);
        assertNull(tag.attributes);
    }

    @Test
    public void testStartTagNameAttr() {
        Token tag = new Token.StartTag();

        tag.tagName = "div";
        tag.attributes = new Attributes();

        assertEquals("div", tag.name());

        tag.nameAttr("p", tag.attributes);

        assertEquals("p", tag.name());
        assertEquals("p", tag.normalName());
        assertTrue(tag.isStartTag());
        assertTrue(tag.attributes != null);
        assertTrue(tag.attributes.size() > 0);
    }

    @Test
    public void testEndTagReset() {
        Token tag = new Token.EndTag();
        assertEquals(TokenType.END_TAG, tag.type);

        tag.reset();

        assertEquals(TokenType.TOKEN_TYPE_UNKNOWN, tag.type);
        assertNull(tag.tagName);
        assertNull(tag.normalName);
        assertNull(tag.pendingAttributeName);
        assertNull(tag.pendingAttributeValue);
        assertNull(tag.pendingAttributeValueS);
        assertFalse(tag.hasEmptyAttributeValue);
        assertFalse(tag.hasPendingAttributeValue);
        assertFalse(tag.selfClosing);
        assertNull(tag.attributes);
    }

    @Test
    public void testCommentReset() {
        Token tag = new Token.Comment();
        assertEquals(TokenType.COMMENT, tag.type);

        tag.reset();

        assertEquals(TokenType.TOKEN_TYPE_UNKNOWN, tag.type);
        assertNull(tag.data);
        assertFalse(tag.bogus);
    }

    @Test
    public void testCommentgetData() {
        Token tag = new Token.Comment();

        tag.data = new StringBuilder("This is a comment");

        assertEquals("This is a comment", tag.getData());
        assertEquals("This is a comment", tag.toString());
    }

    @Test
    public void testCharacterReset() {
        Token tag = new Token.Character();
        assertEquals(TokenType.CHARACTER, tag.type);

        tag.reset();

        assertEquals(TokenType.TOKEN_TYPE_UNKNOWN, tag.type);
        assertNull(tag.data);
    }

    @Test
    public void testCharactergetData() {
        Token tag = new Token.Character();

        tag.data = "!";

        assertEquals("!", tag.getData());
        assertEquals("!", tag.toString());
    }

    @Test
    public void testEOFReset() {
        Token tag = new Token.EOF();
        assertEquals(TokenType.EOF, tag.type);

        tag.reset();

        assertEquals(TokenType.TOKEN_TYPE_UNKNOWN, tag.type);
    }
}