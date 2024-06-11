package org.jsoup.parser;

import static org.junit.Assert.*;
import org.junit.Test;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new Token.Doctype();
        assertEquals("Doctype", token.tokenType());
    }

    @Test
    public void testGetName() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("HTML");
        assertEquals("HTML", doctype.getName());
    }

    @Test
    public void testGetPublicIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.publicIdentifier.append("-//W3C//DTD HTML 4.01//EN");
        assertEquals("-//W3C//DTD HTML 4.01//EN", doctype.getPublicIdentifier());
    }

    @Test
    public void testGetSystemIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.systemIdentifier.append("http://www.w3.org/TR/html4/strict.dtd");
        assertEquals("http://www.w3.org/TR/html4/strict.dtd", doctype.getSystemIdentifier());
    }

    @Test
    public void testIsForceQuirks() {
        Token.Doctype doctype = new Token.Doctype();
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testFinaliseTag() {
        Token.Tag tag = new Token.EndTag();
        tag.tagName = "body";
        tag.finaliseTag();
        assertEquals("body", tag.tagName);
    }

    @Test
    public void testIsSelfClosing() {
        Token.Tag tag = new Token.StartTag();
        assertFalse(tag.isSelfClosing());
    }

    @Test
    public void testGetNameTag() {
        Token.Tag tag = new Token.StartTag();
        tag.tagName = "div";
        assertEquals("div", tag.name());
    }

    @Test
    public void testTagName() {
        Token.Tag tag = new Token.StartTag();
        tag.tagName = "p";
        assertEquals("p", tag.name("p").tagName);
    }

    @Test
    public void testIsDoctype() {
        Token token = new Token.Doctype();
        assertFalse(token.isDoctype());
    }

    @Test
    public void testAsDoctype() {
        Token token = new Token.Doctype();
        assertNull(token.asDoctype());
    }

    @Test
    public void testIsStartTag() {
        Token token = new Token.StartTag();
        assertFalse(token.isStartTag());
    }

    @Test
    public void testAsStartTag() {
        Token token = new Token.StartTag();
        assertNull(token.asStartTag());
    }

    @Test
    public void testIsEndTag() {
        Token token = new Token.EndTag();
        assertFalse(token.isEndTag());
    }

    @Test
    public void testAsEndTag() {
        Token token = new Token.EndTag();
        assertNull(token.asEndTag());
    }

    @Test
    public void testIsComment() {
        Token token = new Token.Comment();
        assertFalse(token.isComment());
    }

    @Test
    public void testAsComment() {
        Token token = new Token.Comment();
        assertNull(token.asComment());
    }

    @Test
    public void testIsCharacter() {
        Token token = new Token.Character("Hello");
        assertFalse(token.isCharacter());
    }

    @Test
    public void testAsCharacter() {
        Token token = new Token.Character("Hello");
        assertNull(token.asCharacter());
    }

    @Test
    public void testIsEOF() {
        Token token = new Token.EOF();
        assertFalse(token.isEOF());
    }

}