package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.jsoup.nodes.Attributes;
import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctypeToken() {
        Token token = new Token.Doctype();
        assertEquals("Doctype", token.tokenType());

        Token resetToken = token.reset();
        assertEquals("Doctype", resetToken.tokenType());

        token.getName() = "html";
        assertEquals("html", token.getName());

        token.setPubSysKey("pubSysKey");
        assertEquals("pubSysKey", token.getPubSysKey());

        token.setPublicIdentifier("publicIdentifier");
        assertEquals("publicIdentifier", token.getPublicIdentifier());

        token.setSystemIdentifier("systemIdentifier");
        assertEquals("systemIdentifier", token.getSystemIdentifier());

        token.setForceQuirks(true);
        assertTrue(token.isForceQuirks());

        resetToken = token.reset();
        assertNull(token.getName());
        assertNull(token.getPubSysKey());
        assertNull(token.getPublicIdentifier());
        assertNull(token.getSystemIdentifier());
        assertFalse(token.isForceQuirks());
    }

    @Test
    public void testStartTagToken() {
        Token token = new Token.StartTag();
        assertEquals("StartTag", token.tokenType());

        token.name("div");
        assertEquals("div", token.name());
        assertEquals("div", token.normalName());

        token.selfClosing = true;
        assertTrue(token.isSelfClosing());

        token.reset();
        assertEquals("StartTag", token.tokenType());
        assertNull(token.name());
        assertNull(token.normalName());
        assertFalse(token.isSelfClosing());

        Attributes attributes = new Attributes();
        attributes.put("class", "classValue");
        token.attributes = attributes;
        assertEquals(1, token.getAttributes().size());
        assertEquals("class", token.getAttributes().keySet().iterator().next());
        assertEquals("classValue", token.getAttributes().get("class").getValue());

        token.newAttribute();
        attributes.put("id", "idValue");
        assertEquals(1, token.getAttributes().size());
        assertEquals("class", token.getAttributes().keySet().iterator().next());
        assertEquals("classValue", token.getAttributes().get("class").getValue());
        assertEquals("id", token.getPendingAttributeName());
        assertEquals("idValue", token.getPendingAttributeValue().toString());

        token.newAttribute();
        token.newAttribute();
        assertNull(token.getPendingAttributeName());
        assertNull(token.getPendingAttributeValue());
    }

    @Test
    public void testEndTagToken() {
        Token token = new Token.EndTag();
        assertEquals("EndTag", token.tokenType());

        token.name("div");
        assertEquals("div", token.name());
        assertEquals("div", token.normalName());

        token.reset();
        assertEquals("EndTag", token.tokenType());
        assertNull(token.name());
        assertNull(token.normalName());

        token.finaliseTag();
        assertEquals("div", token.name());
    }

    @Test
    public void testCommentToken() {
        Token token = new Token.Comment();
        assertEquals("Comment", token.tokenType());

        token.getData() = "This is a comment";
        assertEquals("This is a comment", token.getData());

        token.reset();
        assertEquals("Comment", token.tokenType());
        assertNull(token.getData());
    }

    @Test
    public void testCharacterToken() {
        Token token = new Token.Character();
        assertEquals("Character", token.tokenType());

        token.data("a");
        assertEquals("a", token.getData());

        token.reset();
        assertEquals("Character", token.tokenType());
        assertNull(token.getData());
    }

    @Test
    public void testEOFToken() {
        Token token = new Token.EOF();
        assertEquals("EOF", token.tokenType());

        token.reset();
        assertEquals("EOF", token.tokenType());
    }
}