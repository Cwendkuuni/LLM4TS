package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.BooleanAttribute;
import org.junit.Test;

/**
 * Test class for the Token class.
 */
public class TokenTest {

    @Test
    public void testDoctypeToken() {
        Token token = new Token.Doctype();
        assertEquals("Doctype", token.tokenType());

        token.asDoctype().getName("html");
        assertEquals("html", token.asDoctype().getName());

        token.asDoctype().setForceQuirks(true);
        assertTrue(token.asDoctype().isForceQuirks());

        token.reset();
        assertFalse(token.asDoctype().isForceQuirks());
    }

    @Test
    public void testStartTagToken() {
        Token token = new Token.StartTag();
        assertEquals("StartTag", token.tokenType());

        token.asStartTag().name("div");
        assertEquals("div", token.asStartTag().name());

        token.asStartTag().name("div").attributes();
        assertNotNull(token.asStartTag().getAttributes());

        token.asStartTag().name("div").attributes().put("class", "test");
        assertEquals("class", token.asStartTag().getAttributes().keySet().iterator().next());

        token.reset();
        assertNull(token.asStartTag().getAttributes());
    }

    @Test
    public void testEndTagToken() {
        Token token = new Token.EndTag();
        assertEquals("EndTag", token.tokenType());

        token.asEndTag().name("div");
        assertEquals("div", token.asEndTag().name());

        token.reset();
        assertNull(token.asEndTag().name());
    }

    @Test
    public void testCommentToken() {
        Token token = new Token.Comment();
        assertEquals("Comment", token.tokenType());

        token.asComment().getData("This is a comment.");
        assertEquals("This is a comment.", token.asComment().getData());

        token.reset();
        assertNull(token.asComment().getData());
    }

    @Test
    public void testCharacterToken() {
        Token token = new Token.Character();
        assertEquals("Character", token.tokenType());

        token.asCharacter().data('A');
        assertEquals("A", token.asCharacter().getData());

        token.reset();
        assertNull(token.asCharacter().getData());
    }

    @Test(expected = AssertionError.class)
    public void testInvalidToken() {
        Token token = new Token(); // this class is abstract and should not be instantiated
    }

    @Test
    public void testResetAllTokens() {
        Token doctype = new Token.Doctype();
        Token startTag = new Token.StartTag();
        Token endTag = new Token.EndTag();
        Token comment = new Token.Comment();
        Token character = new Token.Character();
        Token eof = new Token.EOF();

        doctype.reset();
        endTag.reset();
        comment.reset();
        character.reset();
        eof.reset();

        startTag.reset();
        assertNull(startTag.getAttributes());
        assertEquals("", startTag.tagName);
    }
}