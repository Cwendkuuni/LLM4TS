package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.parser.Token.*;
import org.jsoup.parser.TokenType;

import java.util.Arrays;

public class TokenTest {

    @org.junit.Test
    public void testDoctypeReset() {
        Doctype doctype = new Doctype();
        doctype.getName().equals("");
        doctype.reset();
        doctype.getName().equals("");

        doctype.getName("html");
        doctype.reset();
        doctype.getName().equals("");
    }

    @org.junit.Test
    public void testDoctypeGetters() {
        Doctype doctype = new Doctype();
        doctype.getName("html");
        doctype.getPublicIdentifier().equals("");
        doctype.getSystemIdentifier().equals("");
        doctype.isForceQuirks();
        assertFalse(doctype.isForceQuirks());

        doctype.setForceQuirks(true);
        assertTrue(doctype.isForceQuirks());
    }

    @org.junit.Test
    public void testTagReset() {
        Tag tag = new StartTag();
        tag.tagName = null;
        tag.normalName = null;
        tag.pendingAttributeName = null;
        tag.hasPendingAttributeValue = false;
        tag.hasEmptyAttributeValue = false;
        tag.selfClosing = false;
        tag.attributes = null;
        tag.reset();

        tag.tagName = "div";
        tag.normalName = "div";
        tag.pendingAttributeName = null;
        tag.hasPendingAttributeValue = false;
        tag.hasEmptyAttributeValue = false;
        tag.selfClosing = false;
        tag.attributes = null;
        tag.reset();

        tag.tagName = "div";
        tag.normalName = "div";
        tag.pendingAttributeName = null;
        tag.hasPendingAttributeValue = false;
        tag.hasEmptyAttributeValue = false;
        tag.selfClosing = false;
        tag.attributes = new Attributes();
        tag.reset();

        tag.tagName = "div";
        tag.normalName = "div";
        tag.pendingAttributeName = null;
        tag.hasPendingAttributeValue = false;
        tag.hasEmptyAttributeValue = false;
        tag.selfClosing = false;
        tag.attributes = new Attributes();
        tag.newAttribute();
        tag.attributes.put("id", "test");
        tag.newAttribute();
        tag.attributes.put("class", "test-class");
        tag.finaliseTag();
        assertEquals("div id=\"test\" class=\"test-class\"", tag.toString());
    }

    @org.junit.Test
    public void testTagGetters() {
        StartTag tag = new StartTag();
        tag.tagName = "div";
        tag.normalName = "div";
        tag.selfClosing = false;
        tag.getAttributes().put("id", "test");
        tag.getAttributes().put("class", "test-class");

        assertEquals("div", tag.name());
        assertEquals("div", tag.normalName());
        assertFalse(tag.isSelfClosing());
        assertEquals("id=\"test\" class=\"test-class\"", tag.toString());

        tag.name("span");
        tag.normalName("span");
        tag.selfClosing = true;
        tag.getAttributes().clear();
        assertEquals("span", tag.name());
        assertEquals("span", tag.normalName());
        assertTrue(tag.isSelfClosing());
        assertEquals("<span />", tag.toString());
    }

    @org.junit.Test
    public void testTagAppenders() {
        StartTag tag = new StartTag();
        tag.appendTagName("div");
        tag.appendTagName("id");
        tag.appendAttributeValue("test");
        tag.appendAttributeValue("class");
        tag.appendAttributeValue("test-class");
        tag.finaliseTag();
        assertEquals("div id=\"test\" class=\"test-class\"", tag.toString());

        tag.reset();

        tag.appendTagName("div").appendTagName("id").appendTagName("class");
        tag.appendAttributeValue("test");
        tag.appendAttributeValue("test-class");
        tag.finaliseTag();
        assertEquals("div id=\"test\" class=\"test-class\"", tag.toString());

        tag.reset();

        tag.appendTagName("div").appendAttributeName("id").appendAttributeValue("test");
        tag.appendAttributeName("class").appendAttributeValue("test-class");
        tag.finaliseTag();
        assertEquals("div id=\"test\" class=\"test-class\"", tag.toString());

        tag.reset();

        tag.appendTagName("div").appendAttributeValue("id", "test");
        tag.appendAttributeValue("class", "test-class");
        tag.finaliseTag();
        assertEquals("div id=\"test\" class=\"test-class\"", tag.toString());

        tag.reset();

        tag.appendTagName("div").appendAttributeValue("class", "test-class");
        tag.appendAttributeValue("id", "test");
        tag.finaliseTag();
        assertEquals("div id=\"test\" class=\"test-class\"", tag.toString());
    }

    @org.junit.Test
    public void testTagEmptyAttributeValue() {
        StartTag tag = new StartTag();
        tag.appendTagName("div");
        tag.appendAttributeName("class");
        tag.setEmptyAttributeValue();
        tag.finaliseTag();
        assertEquals("div class=\"\"", tag.toString());
    }

    @org.junit.Test
    public void testTokenTypeIdentification() {
        Token token = new Doctype();
        assertTrue(token.isDoctype());
        assertEquals(TokenType.Doctype, token.tokenType());

        token = new StartTag();
        assertFalse(token.isDoctype());
        assertFalse(token.isStartTag());
        assertFalse(token.isEndTag());
        assertFalse(token.isComment());
        assertFalse(token.isCharacter());
        assertFalse(token.isEOF());
        assertEquals(TokenType.StartTag, token.tokenType());

        token = new EndTag();
        assertFalse(token.isDoctype());
        assertTrue(token.isEndTag());
        assertFalse(token.isStartTag());
        assertFalse(token.isComment());
        assertFalse(token.isCharacter());
        assertFalse(token.isEOF());
        assertEquals(TokenType.EndTag, token.tokenType());

        token = new Comment();
        assertFalse(token.isDoctype());
        assertFalse(token.isStartTag());
        assertFalse(token.isEndTag());
        assertTrue(token.isComment());
        assertFalse(token.isCharacter());
        assertFalse(token.isEOF());
        assertEquals(TokenType.Comment, token.tokenType());

        token = new Character();
        assertFalse(token.isDoctype());
        assertFalse(token.isStartTag());
        assertFalse(token.isEndTag());
        assertFalse(token.isComment());
        assertFalse(token.isEOF());
        assertTrue(token.isCharacter());
        assertEquals(TokenType.Character, token.tokenType());

        token = new EOF();
        assertFalse(token.isDoctype());
        assertFalse(token.isStartTag());
        assertFalse(token.isEndTag());
        assertFalse(token.isComment());
        assertTrue(token.isCharacter());
        assertFalse(token.isEOF());
        assertEquals(TokenType.EOF, token.tokenType());
    }
}