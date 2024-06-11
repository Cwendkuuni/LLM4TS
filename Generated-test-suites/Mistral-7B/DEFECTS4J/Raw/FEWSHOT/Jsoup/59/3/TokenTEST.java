package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Token doctype = new Token.Doctype();
        doctype.asDoctype().getName = "html";
        doctype.asDoctype().pubSysKey = "foo";
        doctype.asDoctype().publicIdentifier.append("bar");
        doctype.asDoctype().systemIdentifier.append("baz");
        doctype.asDoctype().forceQuirks = true;

        Token resetToken = doctype.reset();

        assertNotNull(resetToken);
        assertEquals(TokenType.Doctype, resetToken.type);
        assertNull(resetToken.asDoctype().getName);
        assertNull(resetToken.asDoctype().pubSysKey);
        assertNull(resetToken.asDoctype().publicIdentifier);
        assertNull(resetToken.asDoctype().systemIdentifier);
        assertFalse(resetToken.asDoctype().isForceQuirks());
    }

    @Test
    public void testTagReset() {
        Token tag = new Token.StartTag();
        tag.tagName = "div";
        tag.attributes = new Attributes();
        tag.attributes.put("class", "red");

        Token resetToken = tag.reset();

        assertNotNull(resetToken);
        assertEquals(TokenType.StartTag, resetToken.type);
        assertNull(resetToken.tagName);
        assertNull(resetToken.attributes);
    }

    @Test
    public void testCommentReset() {
        Token comment = new Token.Comment();
        comment.asComment().data.append("This is a comment");

        Token resetToken = comment.reset();

        assertNotNull(resetToken);
        assertEquals(TokenType.Comment, resetToken.type);
        assertNull(resetToken.asComment().getData());
    }

    @Test
    public void testCharacterReset() {
        Token character = new Token.Character();
        character.asCharacter().data = "!";

        Token resetToken = character.reset();

        assertNotNull(resetToken);
        assertEquals(TokenType.Character, resetToken.type);
        assertNull(resetToken.asCharacter().getData());
    }

    @Test
    public void testEOFReset() {
        Token eof = new Token.EOF();

        Token resetToken = eof.reset();

        assertNotNull(resetToken);
        assertEquals(TokenType.EOF, resetToken.type);
    }

    @Test
    public void testTokenType() {
        Token doctype = new Token.Doctype();
        Token startTag = new Token.StartTag();
        Token endTag = new Token.EndTag();
        Token comment = new Token.Comment();
        Token character = new Token.Character();
        Token eof = new Token.EOF();

        assertEquals("Doctype", doctype.tokenType());
        assertEquals("StartTag", startTag.tokenType());
        assertEquals("EndTag", endTag.tokenType());
        assertEquals("Comment", comment.tokenType());
        assertEquals("Character", character.tokenType());
        assertEquals("EOF", eof.tokenType());
    }

    @Test
    public void testResetStringBuilder() {
        StringBuilder sb = new StringBuilder("Hello World");
        Token.reset(sb);
        assertNotNull(sb);
        assertEquals(0, sb.length());
    }
}