package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.junit.Test;
import org.junit.Assert.*;

import static org.jsoup.internal.Normalizer.lowerCase;

/**
 * Test for the Token class.
 */
public class TokenTest {

    @Test
    public void testDoctypeToken() {
        Token token = new Token.Doctype();
        assertEquals("Doctype", token.tokenType());

        Token resetToken = token.reset();
        assertEquals("Doctype", resetToken.tokenType());

        Token doctypeToken = new Token.Doctype();
        doctypeToken.getName("html");
        assertEquals("html", doctypeToken.getName());
        assertEquals("HTML", doctypeToken.name());

        doctypeToken.setForceQuirks(true);
        assertTrue(doctypeToken.isForceQuirks());

        doctypeToken.reset();
        assertFalse(doctypeToken.isForceQuirks());
    }

    @Test
    public void testTagToken() {
        Token tagToken = new Token.StartTag();
        tagToken.name("div");
        tagToken.name("div").attributes().put("id", "test");

        assertEquals("StartTag", tagToken.tokenType());
        assertEquals("div", tagToken.name());
        assertEquals("div", tagToken.name());
        assertTrue(tagToken.isStartTag());

        tagToken.newAttribute();
        tagToken.name("class").set("test");
        tagToken.newAttribute();

        Tag startTag = tagToken.asStartTag();
        assertEquals("div", startTag.name());
        assertEquals("div", startTag.normalName());
        assertEquals("test", startTag.getAttributes().get("class"));

        tagToken.finaliseTag();

        Token endTagToken = new Token.EndTag();
        endTagToken.name("div");
        assertEquals("EndTag", endTagToken.tokenType());
        assertEquals("div", endTagToken.name());
        assertTrue(endTagToken.isEndTag());

        tagToken.reset();
        assertFalse(tagToken.isStartTag());
        assertFalse(tagToken.isEndTag());
    }

    @Test
    public void testCommentToken() {
        Token commentToken = new Token.Comment();
        commentToken.data("This is a comment");

        assertEquals("Comment", commentToken.tokenType());
        assertEquals("This is a comment", commentToken.getData());
        assertTrue(commentToken.isComment());

        commentToken.reset();
        assertFalse(commentToken.isComment());
    }

    @Test
    public void testCharacterToken() {
        Token characterToken = new Token.Character();
        characterToken.data("A");

        assertEquals("Character", characterToken.tokenType());
        assertEquals("A", characterToken.getData());
        assertTrue(characterToken.isCharacter());

        characterToken.reset();
        assertNull(characterToken.getData());
    }

    @Test
    public void testCDATAToken() {
        Token cdataToken = new Token.CData("This is a CDATA");

        assertEquals("Character", cdataToken.tokenType());
        assertEquals("<![CDATA[This is a CDATA]]>", cdataToken.toString());
        assertTrue(cdataToken.isCData());

        cdataToken.reset();
        assertNull(cdataToken.getData());
    }

    @Test
    public void testEOToken() {
        Token eotoken = new Token.EOF();

        assertEquals("EOF", eotoken.tokenType());
        assertTrue(eotoken.isEOF());

        eotoken.reset();
        assertFalse(eotoken.isEOF());
    }

}