package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.BooleanAttribute;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class TokenTest {

    @Test
    public void testTokenReset() {
        Token token = new Token.Doctype();
        token.reset();
        assertEquals(TokenType.Doctype, token.type);
        token = new Token.StartTag();
        token.reset();
        assertEquals(TokenType.StartTag, token.type);
    }

    @Test
    public void testDoctypeToken() {
        Token token = new Token.Doctype();
        token.reset();

        token.name("html");
        token.getName();
        assertEquals("html", token.getName());

        token.publicIdentifier("XYZ Public Identifier");
        token.getPublicIdentifier();
        assertEquals("XYZ Public Identifier", token.getPublicIdentifier());

        token.systemIdentifier("/foo/bar.html");
        token.getSystemIdentifier();
        assertEquals("/foo/bar.html", token.getSystemIdentifier());

        token.forceQuirks(true);
        assertTrue(token.isForceQuirks());
    }

    @Test
    public void testTagToken() {
        Token token = new Token.StartTag();
        token.reset();

        token.name("div");
        token.name("Div");
        token.name("DIV");
        assertEquals("div", token.name());
        assertEquals("div", token.normalName());

        token.newAttribute();
        token.appendAttributeName("class");
        token.appendAttributeName("CLAS");
        token.appendAttributeName("CLASS");
        token.appendAttributeValue("test");
        token.appendAttributeValue("Test");
        token.appendAttributeValue("TEST");
        token.attributes();
        Attributes attributes = token.getAttributes();
        assertEquals(1, attributes.size());
        Attribute attribute = attributes.get("class");
        assertEquals("test", attribute.getValue());
        assertEquals("Test", attribute.getName());
        assertEquals("TEST", attribute.name());

        token.newAttribute();
        token.appendAttributeName("id");
        token.appendAttributeValue("");
        token.setEmptyAttributeValue();
        assertEquals(true, token.hasEmptyAttributeValue());
        assertEquals("", token.getAttributes().get("id").getValue());
        assertEquals(0, token.getAttributes().get("id").size());

        token.selfClosing(true);
        assertTrue(token.isSelfClosing());
    }

    @Test
    public void testCommentToken() {
        Token token = new Token.Comment();
        token.reset();

        token.appendAttributeName("comment");
        token.appendAttributeValue("This is a comment");
        token.getData();
        assertEquals("This is a comment", token.getData());

        token.bogus(true);
        assertTrue(token.isBogus());
    }

    @Test
    public void testCharacterToken() {
        Token token = new Token.Character();
        token.reset();

        token.data("a");
        token.getData();
        assertEquals("a", token.getData());

        List<Token> tokens = new ArrayList<>();
        tokens.add(token);
        tokens.add(token);
        tokens.add(new Token.Character().data("b"));
        assertEquals("ab", new String(new StringBuilder().append(tokens.get(0).getData()).append(tokens.get(1).getData()).toString()));
    }

    @Test
    public void testEOFToken() {
        Token token = new Token.EOF();
        token.reset();

        assertTrue(token.isEOF());
    }

    @Test(expected = IllegalStateException.class)
    public void testInvalidToken() {
        new Token();
    }

    @Test
    public void testTokenTypes() {
        Token token = new Token.Comment();
        assertEquals(TokenType.Comment, token.type);

        token = new Token.Character();
        assertEquals(TokenType.Character, token.type);

        token = new Token.Doctype();
        assertEquals(TokenType.Doctype, token.type);

        token = new Token.EndTag();
        assertEquals(TokenType.EndTag, token.type);

        token = new Token.StartTag();
        assertEquals(TokenType.StartTag, token.type);

        token = new Token.EOF();
        assertEquals(TokenType.EOF, token.type);
    }

    @Test
    public void testTokenToString() {
        Token token = new Token.StartTag();
        token.name("div");
        token.newAttribute();
        token.appendAttributeName("class");
        token.appendAttributeValue("test");
        token.toString();
        assertEquals("<div class=\"test\">", token.toString());
    }
}