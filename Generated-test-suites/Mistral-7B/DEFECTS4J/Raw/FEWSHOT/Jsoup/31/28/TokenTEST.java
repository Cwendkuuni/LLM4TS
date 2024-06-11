package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctype() {
        Token doctype = new Token.Doctype();
        assertEquals(TokenType.Doctype, doctype.type);
        assertEquals("Doctype", doctype.tokenType());

        Token doctypeWithName = new Token.Doctype();
        doctypeWithName.asDoctype().name = "html";
        assertEquals("Doctype[html]", doctypeWithName.tokenType());

        Token doctypeWithForceQuirks = new Token.Doctype();
        doctypeWithForceQuirks.asDoctype().forceQuirks = true;
        assertEquals("Doctype[forceQuirks=true]", doctypeWithForceQuirks.tokenType());
    }

    @Test
    public void testStartTag() {
        Token startTag = new Token.StartTag();
        assertEquals(TokenType.StartTag, startTag.type);
        assertEquals("StartTag", startTag.tokenType());

        Token startTagWithName = new Token.StartTag();
        startTagWithName.asStartTag().name = "div";
        assertEquals("StartTag[div]", startTagWithName.tokenType());

        Token startTagWithSelfClosing = new Token.StartTag();
        startTagWithSelfClosing.asStartTag().selfClosing = true;
        assertEquals("StartTag[selfClosing=true]", startTagWithSelfClosing.tokenType());

        Token startTagWithAttributes = new Token.StartTag();
        startTagWithAttributes.asStartTag().newAttribute();
        startTagWithAttributes.asStartTag().appendAttributeName("class");
        startTagWithAttributes.asStartTag().appendAttributeValue("button");
        assertEquals("StartTag[class=button]", startTagWithAttributes.tokenType());
    }

    @Test
    public void testComment() {
        Token comment = new Token.Comment();
        assertEquals(TokenType.Comment, comment.type);
        assertEquals("Comment", comment.tokenType());

        Token commentWithData = new Token.Comment();
        commentWithData.asComment().data.append("This is a comment");
        assertEquals("Comment[getData=This is a comment]", commentWithData.tokenType());
    }

    @Test
    public void testCharacter() {
        Token character = new Token.Character("a");
        assertEquals(TokenType.Character, character.type);
        assertEquals("Character[a]", character.tokenType());

        Token characterWithData = new Token.Character("&nbsp;");
        assertEquals(TokenType.Character, character.type);
        assertEquals("Character[&nbsp;]", characterWithData.tokenType());
    }

    @Test
    public void testEndTag() {
        Token endTag = new Token.EndTag();
        assertEquals(TokenType.EndTag, endTag.type);
        assertEquals("EndTag", endTag.tokenType());

        Token endTagWithName = new Token.EndTag();
        endTagWithName.asEndTag().name = "div";
        assertEquals("EndTag[div]", endTagWithName.tokenType());
    }

    @Test
    public void testEFOToken() {
        Token eof = new Token.EOF();
        assertEquals(TokenType.EOF, eof.type);
        assertEquals("EOF", eof.tokenType());

        assertFalse(eof.isDoctype());
        assertFalse(eof.isStartTag());
        assertFalse(eof.isEndTag());
        assertFalse(eof.isComment());
        assertFalse(eof.isCharacter());
        assertTrue(eof.isEOF());
    }

    @Test
    public void testNullToken() {
        assertNull(new Token());
    }
}