package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;

import static org.jsoup.parser.TokenType.*;

public class TokenTest {

    @Test
    public void testDoctypeToken() {
        Token token = new Token.Doctype();
        assertEquals("Doctype", token.tokenType());
        Token resetToken = token.reset();
        assertSame(token, resetToken);
        Assert.assertNull(token.asDoctype().getName());
        token.asDoctype().getName("html");
        Assert.assertEquals("html", token.asDoctype().getName());
        token.asDoctype().getPublicIdentifier().append("public");
        token.asDoctype().setForceQuirks(true);
        Assert.assertEquals("public", token.asDoctype().getPublicIdentifier().toString());
        Assert.assertTrue(token.asDoctype().isForceQuirks());
    }

    @Test
    public void testStartTagToken() {
        Token token = new Token.StartTag();
        assertEquals("StartTag", token.tokenType());
        token.asStartTag().name("div");
        Assert.assertEquals("div", token.asStartTag().name());
        token.asStartTag().name("div").attributes().put("class", "test");
        Assert.assertEquals("div", token.asStartTag().name());
        Assert.assertEquals("class=\"test\"", token.asStartTag().toString());
        Token resetToken = token.reset();
        assertSame(token, resetToken);
        Assert.assertNull(token.asStartTag().name());
        Assert.assertNull(token.asStartTag().getAttributes());
    }

    @Test
    public void testEndTagToken() {
        Token token = new Token.EndTag();
        assertEquals("EndTag", token.tokenType());
        token.asEndTag().name("div");
        Assert.assertEquals("div", token.asEndTag().name());
        Token resetToken = token.reset();
        assertSame(token, resetToken);
        Assert.assertNull(token.asEndTag().name());
    }

    @Test
    public void testCommentToken() {
        Token token = new Token.Comment();
        assertEquals("Comment", token.tokenType());
        token.asComment().getData().append("This is a comment");
        Assert.assertEquals("This is a comment", token.asComment().getData().toString());
        Token resetToken = token.reset();
        assertSame(token, resetToken);
        Assert.assertNull(token.asComment().getData());
    }

    @Test
    public void testCharacterToken() {
        Token token = new Token.Character();
        assertEquals("Character", token.tokenType());
        token.asCharacter().data('A');
        Assert.assertEquals('A', token.asCharacter().getData().charAt(0));
        Token resetToken = token.reset();
        assertSame(token, resetToken);
        Assert.assertNull(token.asCharacter().getData());
    }

    @Test
    public void testEOFToken() {
        Token token = new Token.EOF();
        assertEquals("EOF", token.tokenType());
        Token resetToken = token.reset();
        assertSame(token, resetToken);
    }
}