package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctype() {
        Token doctype = new Token.Doctype();

        assertEquals(TokenType.Doctype, doctype.type);

        Token doctypeWithName = new Token.Doctype();
        doctypeWithName.asDoctype().name("html");
        assertEquals("html", doctypeWithName.asDoctype().getName());

        Token doctypeWithForceQuirks = new Token.Doctype();
        doctypeWithForceQuirks.asDoctype().isForceQuirks(true);
        assertTrue(doctypeWithForceQuirks.asDoctype().isForceQuirks());
    }

    @Test
    public void testStartTag() {
        Token startTag = new Token.StartTag();

        assertEquals(TokenType.StartTag, startTag.type);

        Token startTagWithName = new Token.StartTag();
        startTagWithName.asStartTag().name("p");
        assertEquals("p", startTagWithName.asStartTag().name());

        Token startTagWithAttributes = new Token.StartTag();
        startTagWithAttributes.asStartTag().name("a").attributes(new Attributes().put("href", "http://example.com"));
        assertEquals("a", startTagWithAttributes.asStartTag().name());
        assertEquals("href", startTagWithAttributes.asStartTag().getAttributes().get("href").getValue());

        Token startTagWithSelfClosing = new Token.StartTag();
        startTagWithSelfClosing.asStartTag().name("img").selfClosing(true);
        assertTrue(startTagWithSelfClosing.asStartTag().isSelfClosing());
    }

    @Test
    public void testEndTag() {
        Token endTag = new Token.EndTag();

        assertEquals(TokenType.EndTag, endTag.type);

        Token endTagWithName = new Token.EndTag();
        endTagWithName.asEndTag().name("p");
        assertEquals("p", endTagWithName.asEndTag().name());
    }

    @Test
    public void testComment() {
        Token comment = new Token.Comment();

        assertEquals(TokenType.Comment, comment.type);

        Token commentWithData = new Token.Comment();
        commentWithData.asComment().getData().append("This is a comment");
        assertEquals("This is a comment", commentWithData.asComment().getData().toString());
    }

    @Test
    public void testCharacter() {
        Token character = new Token.Character("a");

        assertEquals(TokenType.Character, character.type);

        assertEquals("a", character.asCharacter().getData());
    }

    @Test
    public void testEOF() {
        Token eof = new Token.EOF();

        assertEquals(TokenType.EOF, eof.type);
    }

    @Test
    public void testTokenTypeEnum() {
        assertEquals(TokenType.Doctype, TokenType.Doctype);
        assertEquals(TokenType.StartTag, TokenType.StartTag);
        assertEquals(TokenType.EndTag, TokenType.EndTag);
        assertEquals(TokenType.Comment, TokenType.Comment);
        assertEquals(TokenType.Character, TokenType.Character);
        assertEquals(TokenType.EOF, TokenType.EOF);

        Token startTag = new Token.StartTag();
        assertSame(TokenType.StartTag, startTag.type);
        assertSame(TokenType.StartTag, TokenType.StartTag);

        Token endTag = new Token.EndTag();
        assertSame(TokenType.EndTag, endTag.type);
        assertSame(TokenType.EndTag, endTag.type);
    }
}