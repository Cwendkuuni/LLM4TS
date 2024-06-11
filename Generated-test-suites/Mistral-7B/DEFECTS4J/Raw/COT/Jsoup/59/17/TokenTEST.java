package org.jsoup.parser.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;

public class TokenTest {

    @org.junit.Test
    public void testTokenType() {
        Token token = new Token().tokenType();
        assertEquals("Token", token);
    }

    @org.junit.Test
    public void testResetDoctype() {
        Token doctype = new Token.Doctype();
        doctype.getName().equals("html");
        doctype.reset();
        assertEquals(null, doctype.getName());
    }

    @org.junit.Test
    public void testDoctypeGetters() {
        Token doctype = new Token.Doctype();
        doctype.getName().equals("html");
        doctype.getPubSysKey() = "pubSysKey";
        doctype.getPublicIdentifier().equals("publicIdentifier");
        doctype.getSystemIdentifier().equals("systemIdentifier");
        doctype.isForceQuirks() = true;

        assertEquals("html", doctype.getName());
        assertEquals("pubSysKey", doctype.getPubSysKey());
        assertEquals("publicIdentifier", doctype.getPublicIdentifier());
        assertEquals("systemIdentifier", doctype.getSystemIdentifier());
        assertTrue(doctype.isForceQuirks());
    }

    @org.junit.Test
    public void testResetTag() {
        Token tag = new Token.StartTag();
        tag.tagName = "tagName";
        tag.reset();
        assertNull(tag.tagName);
    }

    @org.junit.Test
    public void testTagGetters() {
        Token tag = new Token.StartTag();
        tag.tagName = "tagName";
        tag.normalName = "normalName";
        tag.selfClosing = true;

        assertEquals("tagName", tag.tagName);
        assertEquals("normalName", tag.normalName());
        assertTrue(tag.isSelfClosing());

        tag.name("tagName2");
        assertEquals("tagName2", tag.tagName);
        assertEquals("tagName2", tag.normalName());
    }

    @org.junit.Test(expected = AssertionError.class)
    public void testTagGetAttributesThrowsExceptionWhenNull() {
        Token tag = new Token.StartTag();
        tag.getAttributes();
    }

    @org.junit.Test
    public void testTagNewAttribute() {
        Token tag = new Token.StartTag();
        tag.newAttribute();
        assertNull(tag.getAttributes());
    }

    @org.junit.Test
    public void testTagFinaliseTag() {
        Token tag = new Token.StartTag();
        tag.finaliseTag();
        assertSame(tag.getAttributes(), tag.getAttributes());
    }

    @org.junit.Test
    public void testTagNameAttribute() {
        Token tag = new Token.StartTag();
        tag.appendTagName("tagName");
        assertEquals("tagName", tag.tagName);
    }

    @org.junit.Test
    public void testTagAppendAttributeName() {
        Token tag = new Token.StartTag();
        tag.appendAttributeName("attributeName");
        assertEquals("attributeName", tag.pendingAttributeName);
    }

    @org.junit.Test
    public void testTagAppendAttributeValue() {
        Token tag = new Token.StartTag();
        tag.appendAttributeValue("attributeValue");
        assertEquals("attributeValue", tag.pendingAttributeValue.toString());
    }

    @org.junit.Test
    public void testTagSetEmptyAttributeValue() {
        Token tag = new Token.StartTag();
        tag.setEmptyAttributeValue();
        assertTrue(tag.hasEmptyAttributeValue);
    }

    @org.junit.Test
    public void testTokenTypeIsDoctype() {
        Token token = new Token.Doctype();
        assertTrue(token.isDoctype());
    }

    @org.junit.Test
    public void testTokenTypeIsStartTag() {
        Token token = new Token.StartTag();
        assertTrue(token.isStartTag());
    }

    @org.junit.Test
    public void testTokenTypeIsEndTag() {
        Token token = new Token.EndTag();
        assertTrue(token.isEndTag());
    }

    @org.junit.Test
    public void testTokenTypeIsComment() {
        Token token = new Token.Comment();
        assertTrue(token.isComment());
    }

    @org.junit.Test
    public void testTokenTypeIsCharacter() {
        Token token = new Token.Character();
        assertTrue(token.isCharacter());
    }

    @org.junit.Test
    public void testTokenTypeIsEOF() {
        Token token = new Token.EOF();
        assertTrue(token.isEOF());
    }
}