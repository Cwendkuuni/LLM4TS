package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.jsoup.internal.Normalizer.lowerCase;

import org.junit.Test;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new Token.Doctype();
        assertEquals("Doctype", token.tokenType());

        token = new Token.StartTag();
        assertEquals("StartTag", token.tokenType());

        token = new Token.EndTag();
        assertEquals("EndTag", token.tokenType());

        token = new Token.Comment();
        assertEquals("Comment", token.tokenType());

        token = new Token.Character();
        assertEquals("Character", token.tokenType());

        token = new Token.CData();
        assertEquals("Character", token.tokenType()); // CData is considered as an extension of Character

        token = new Token.EOF();
        assertEquals("EOF", token.tokenType());
    }

    // Test Doctype token

    @Test
    public void testDoctypeReset() {
        Token doctype = new Token.Doctype();
        Token resetDoctype = doctype.reset();

        assertSame(doctype, resetDoctype);
        assertEquals(TokenType.Doctype, resetDoctype.type);
        assertNull(resetDoctype.getName());
        assertNull(resetDoctype.getPubSysKey());
        assertNull(resetDoctype.getPublicIdentifier());
        assertNull(resetDoctype.getSystemIdentifier());
        assertFalse(resetDoctype.isForceQuirks());
    }

    @Test
    public void testDoctypeMethods() {
        Token doctype = new Token.Doctype();

        doctype.getName("html");
        assertEquals("html", doctype.getName());
        assertEquals("html", doctype.normalName());

        doctype.setForceQuirks(true);
        assertTrue(doctype.isForceQuirks());
    }

    // Test StartTag token

    @Test
    public void testStartTagReset() {
        Token startTag = new Token.StartTag();
        Token resetStartTag = startTag.reset();

        assertSame(startTag, resetStartTag);
        assertEquals(TokenType.StartTag, resetStartTag.type);
        assertNull(resetStartTag.tagName);
        assertNull(resetStartTag.normalName);
        assertNull(resetStartTag.getAttributes());
        assertFalse(resetStartTag.isSelfClosing());
    }

    @Test
    public void testStartTagMethods() {
        Token startTag = new Token.StartTag();

        startTag.name("html");
        assertEquals("html", startTag.tagName);
        assertEquals("html", startTag.normalName());

        startTag.name("html").attributes(new Attributes());
        assertSame(startTag.getAttributes(), startTag.attributes);

        startTag.name("html").attributes(new Attributes()).isSelfClosing(true);
        assertTrue(startTag.isSelfClosing());
    }

    // Test EndTag token

    @Test
    public void testEndTagReset() {
        Token endTag = new Token.EndTag();
        Token resetEndTag = endTag.reset();

        assertSame(endTag, resetEndTag);
        assertEquals(TokenType.EndTag, resetEndTag.type);
        assertNull(resetEndTag.tagName);
        assertNull(resetEndTag.normalName);
        assertNull(resetEndTag.getAttributes());
        assertFalse(resetEndTag.isSelfClosing());
    }

    @Test
    public void testEndTagMethods() {
        Token endTag = new Token.EndTag();

        endTag.name("html");
        assertEquals("html", endTag.tagName);
        assertEquals("html", endTag.normalName());
    }

    // Test Comment token

    @Test
    public void testCommentReset() {
        Token comment = new Token.Comment();
        Token resetComment = comment.reset();

        assertSame(comment, resetComment);
        assertEquals(TokenType.Comment, resetComment.type);
        assertNull(resetComment.data);
        assertFalse(resetComment.bogus);
    }

    @Test
    public void testCommentMethods() {
        Token comment = new Token.Comment();

        comment.data("<!-- This is a comment -->");
        assertEquals("This is a comment", comment.getData());
        assertEquals("This is a comment", comment.toString());
    }

    // Test Character token

    @Test
    public void testCharacterReset() {
        Token character = new Token.Character();
        Token resetCharacter = character.reset();

        assertSame(character, resetCharacter);
        assertEquals(TokenType.Character, resetCharacter.type);
        assertNull(resetCharacter.getData());
    }

    @Test
    public void testCharacterMethods() {
        Token character = new Token.Character();

        character.data("A");
        assertEquals("A", character.getData());
        assertEquals("A", character.toString());
    }

    // Test CData token

    @Test
    public void testCDataReset() {
        Token cdata = new Token.CData("<!-- CDATA -->");
        Token resetCdata = cdata.reset();

        assertSame(cdata, resetCdata);
        assertEquals(TokenType.Character, resetCdata.type);
        assertNull(resetCdata.getData());
    }

    @Test
    public void testCDataMethods() {
        Token cdata = new Token.CData("<!-- CDATA -->");

        assertEquals("<!-- CDATA -->", cdata.getData());
        assertEquals("<![CDATA[ CDATA ]]", cdata.toString());
    }
}