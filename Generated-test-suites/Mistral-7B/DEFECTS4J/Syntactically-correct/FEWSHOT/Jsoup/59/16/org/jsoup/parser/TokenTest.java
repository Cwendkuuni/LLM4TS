package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Token token = new Token.Doctype();
        Token resetToken = token.reset();
        assertNotSame(token, resetToken);
        assertEquals(TokenType.Doctype, resetToken.type);
        assertNull(resetToken.getName());
        assertNull(resetToken.getPubSysKey());
        assertNull(resetToken.getPublicIdentifier());
        assertNull(resetToken.getSystemIdentifier());
        assertFalse(resetToken.isForceQuirks());
    }

    @Test
    public void testDoctypeMethods() {
        Token token = new Token.Doctype();
        token.getName("html");
        Token resetToken = token.reset();
        assertEquals("html", resetToken.getName());

        token.getPubSysKey("foo");
        resetToken = token.reset();
        assertEquals(null, resetToken.getPubSysKey());

        token.getPublicIdentifier("public id");
        resetToken = token.reset();
        assertEquals(null, resetToken.getPublicIdentifier());

        token.setSystemIdentifier("system id");
        resetToken = token.reset();
        assertEquals(null, resetToken.getSystemIdentifier());

        token.isForceQuirks(true);
        resetToken = token.reset();
        assertFalse(resetToken.isForceQuirks());
    }

    @Test
    public void testTagReset() {
        Token token = new Token.Tag();
        Token resetToken = token.reset();
        assertNotSame(token, resetToken);
        assertEquals(TokenType.Tag, resetToken.type);
        assertNull(resetToken.tagName);
        assertNull(resetToken.normalName);
        assertNull(resetToken.pendingAttributeName);
        assertNull(resetToken.pendingAttributeValue);
        assertNull(resetToken.pendingAttributeValueS);
        assertFalse(resetToken.hasEmptyAttributeValue);
        assertFalse(resetToken.hasPendingAttributeValue);
        assertFalse(resetToken.selfClosing);
        assertNull(resetToken.attributes);
    }

    @Test
    public void testTagMethods() {
        Token token = new Token.Tag();
        token.name("div");
        Token resetToken = token.reset();
        assertEquals("div", resetToken.name());
        assertEquals("div", resetToken.normalName());

        token.newAttribute();
        token.newAttribute();
        token.newAttribute().name("class").value("foo");
        resetToken = token.reset();
        assertNull(resetToken.attributes);

        token.getAttributes().put(new Attribute("id", "bar"));
        resetToken = token.reset();
        assertNull(resetToken.attributes);

        token.finaliseTag();
        resetToken = token.reset();
        assertNull(resetToken.pendingAttributeName);
        assertNull(resetToken.pendingAttributeValue);
        assertNull(resetToken.pendingAttributeValueS);
        assertFalse(resetToken.hasEmptyAttributeValue);
        assertFalse(resetToken.hasPendingAttributeValue);
        assertFalse(resetToken.selfClosing);
    }

    @Test
    public void testCommentReset() {
        Token token = new Token.Comment();
        Token resetToken = token.reset();
        assertNotSame(token, resetToken);
        assertEquals(TokenType.Comment, resetToken.type);
        assertNull(resetToken.getData());
        assertFalse(resetToken.bogus);
    }

    @Test
    public void testCommentMethods() {
        Token token = new Token.Comment();
        token.getData("This is a comment");
        Token resetToken = token.reset();
        assertEquals("This is a comment", resetToken.getData());

        token.setBogus(true);
        resetToken = token.reset();
        assertFalse(resetToken.bogus);
    }

    @Test
    public void testCharacterReset() {
        Token token = new Token.Character();
        Token resetToken = token.reset();
        assertNotSame(token, resetToken);
        assertEquals(TokenType.Character, resetToken.type);
        assertNull(resetToken.getData());
    }

    @Test
    public void testCharacterMethods() {
        Token token = new Token.Character();
        token.data("a");
        Token resetToken = token.reset();
        assertEquals("a", resetToken.getData());
    }

    @Test
    public void testEOFReset() {
        Token token = new Token.EOF();
        Token resetToken = token.reset();
        assertNotSame(token, resetToken);
        assertEquals(TokenType.EOF, resetToken.type);
    }
}