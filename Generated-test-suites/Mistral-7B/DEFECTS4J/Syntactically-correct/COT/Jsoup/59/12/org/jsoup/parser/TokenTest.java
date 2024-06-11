package org.jsoup.parser.test;

import static org.junit.Assert.*;

import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;

public class TokenTest {

    @org.junit.Test
    public void testDoctypeReset() {
        Token doctype = new Token.Doctype();
        doctype.reset();
        assertEquals(TokenType.Doctype, doctype.type);
        assertNull(doctype.getName());
        assertNull(doctype.getPubSysKey());
        assertNull(doctype.getPublicIdentifier());
        assertNull(doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());
    }

    @org.junit.Test
    public void testDoctypeMethods() {
        Token doctype = new Token.Doctype();
        doctype.getName().append("html");
        doctype.setForceQuirks(true);
        doctype.getPublicIdentifier().append("public");
        doctype.getSystemIdentifier().append("/path/to/file.html");

        assertEquals("Doctype", doctype.tokenType());
        assertEquals("html", doctype.getName());
        assertTrue(doctype.isForceQuirks());
        assertEquals("public", doctype.getPublicIdentifier());
        assertEquals("/path/to/file.html", doctype.getSystemIdentifier());
        assertFalse(doctype.isDoctype());
        assertFalse(doctype.isStartTag());
        assertFalse(doctype.isEndTag());
        assertFalse(doctype.isComment());
        assertFalse(doctype.isCharacter());
        assertFalse(doctype.isEOF());
    }

    @org.junit.Test(expected = AssertionError.class)
    public void testDoctypeGettersAfterReset() {
        Token doctype = new Token.Doctype();
        doctype.reset();
        doctype.getName().append("html");
        doctype.getPublicIdentifier().append("public");
        doctype.getSystemIdentifier().append("/path/to/file.html");
        doctype.reset();
        assertEquals(TokenType.Doctype, doctype.type);
        assertNull(doctype.getName());
        assertNull(doctype.getPubSysKey());
        assertNull(doctype.getPublicIdentifier());
        assertNull(doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());
    }

    @org.junit.Test
    public void testTagReset() {
        Token tag = new Token.StartTag();
        tag.reset();
        assertEquals(TokenType.StartTag, tag.type);
        assertNull(tag.tagName);
        assertNull(tag.normalName);
        assertNull(tag.pendingAttributeName);
        assertNull(tag.getAttributes());
        assertFalse(tag.isSelfClosing());
    }

    @org.junit.Test
    public void testTagMethods() {
        Token tag = new Token.StartTag();
        tag.name("html");
        tag.newAttribute().name("class").value("test");
        tag.finaliseTag();

        assertEquals("StartTag", doctest.tokenType());
        assertEquals("html", tag.name());
        assertEquals("html", tag.normalName());
        assertFalse(tag.isSelfClosing());
        assertNotNull(tag.getAttributes());
        assertEquals("class", tag.getAttributes().keySet().iterator().next());
        assertEquals("test", tag.getAttributes().getString("class"));
        assertFalse(tag.isDoctype());
        assertFalse(tag.isStartTag());
        assertFalse(tag.isEndTag());
        assertFalse(tag.isComment());
        assertFalse(tag.isCharacter());
        assertFalse(tag.isEOF());
    }

    // Add test cases for other methods and edge cases here.

    @org.junit.Test
    public void testTokenTypes() {
        Token token1 = new Token.Comment();
        Token token2 = new Token.Doctype();
        Token token3 = new Token.Character();
        Token token4 = new Token.StartTag();
        Token token5 = new Token.EndTag();
        Token token6 = new Token.EOF();

        assertTrue(token1.isComment());
        assertTrue(token2.isDoctype());
        assertTrue(token3.isCharacter());
        assertTrue(token4.isStartTag());
        assertTrue(token5.isEndTag());
        assertTrue(token6.isEOF());

        assertFalse(token1.isDoctype());
        assertFalse(token1.isStartTag());
        assertFalse(token1.isEndTag());
        assertFalse(token1.isComment());
        assertFalse(token1.isCharacter());
        assertFalse(token1.isEOF());

        // Repeat the assertions for other token types.
    }
}