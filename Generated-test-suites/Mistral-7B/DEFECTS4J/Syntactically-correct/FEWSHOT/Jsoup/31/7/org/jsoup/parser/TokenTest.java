package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctype() {
        Token doctype = new Token.Doctype();
        assertEquals(TokenType.Doctype, doctype.type);

        Token doctypeWithName = new Token.Doctype().name("html");
        assertEquals("Doctype", doctypeWithName.tokenType());
        assertEquals("html", doctypeWithName.asDoctype().getName());

        Token doctypeWithPublicIdentifier = new Token.Doctype().name("html").publicIdentifier("XHTML");
        assertEquals("Doctype", doctypeWithPublicIdentifier.tokenType());
        assertEquals("html", doctypeWithPublicIdentifier.asDoctype().getName());
        assertEquals("XHTML", doctypeWithPublicIdentifier.asDoctype().getPublicIdentifier());

        Token doctypeWithSystemIdentifier = new Token.Doctype().name("html").systemIdentifier("index.html");
        assertEquals("Doctype", doctypeWithSystemIdentifier.tokenType());
        assertEquals("html", doctypeWithSystemIdentifier.asDoctype().getName());
        assertEquals("index.html", doctypeWithSystemIdentifier.asDoctype().getSystemIdentifier());

        Token doctypeWithForceQuirks = new Token.Doctype().name("html").isForceQuirks(true);
        assertEquals("Doctype", doctypeWithForceQuirks.tokenType());
        assertEquals("html", doctypeWithForceQuirks.asDoctype().getName());
        assertTrue(doctypeWithForceQuirks.asDoctype().isForceQuirks());
    }

    @Test
    public void testTag() {
        Token tag = new Token.StartTag();
        assertEquals(TokenType.StartTag, tag.type);
        assertEquals("StartTag", tag.tokenType());

        Token startTagWithName = new Token.StartTag().name("div");
        assertEquals("StartTag", startTagWithName.tokenType());
        assertEquals("div", startTagWithName.asStartTag().name());

        Token startTagWithAttributes = new Token.StartTag().name("input").attributes(new Token.Attributes().put("type", "text"));
        assertEquals("StartTag", startTagWithAttributes.tokenType());
        assertEquals("input", startTagWithAttributes.asStartTag().name());
        assertEquals(1, startTagWithAttributes.asStartTag().getAttributes().size());
        assertEquals("type", startTagWithAttributes.asStartTag().getAttributes().keySet().iterator().next());
        assertEquals("text", startTagWithAttributes.asStartTag().getAttributes().get("type"));

        Token tagWithSelfClosing = new Token.StartTag().name("img").selfClosing(true);
        assertEquals("StartTag", tagWithSelfClosing.tokenType());
        assertEquals("img", tagWithSelfClosing.asStartTag().name());
        assertTrue(tagWithSelfClosing.asStartTag().isSelfClosing());

        Token tagWithEmptyName = new Token.StartTag();
        assertEquals(0, tagWithEmptyName.asStartTag().getName().length());

        Token tagWithNullName = new Token.StartTag(null);
        assertNull(tagWithNullName.asStartTag().getName());

        Token tagWithNullAttributes = new Token.StartTag().attributes(null);
        assertTrue(tagWithNullAttributes.asStartTag().getAttributes() == null);
    }

    @Test
    public void testEndTag() {
        Token endTag = new Token.EndTag();
        assertEquals(TokenType.EndTag, endTag.type);
        assertEquals("EndTag", endTag.tokenType());

        Token endTagWithName = new Token.EndTag().name("div");
        assertEquals("EndTag", endTagWithName.tokenType());
        assertEquals("div", endTagWithName.asEndTag().name());

        Token endTagWithEmptyName = new Token.EndTag();
        assertEquals(0, endTagWithEmptyName.asEndTag().getName().length());

        Token endTagWithNullName = new Token.EndTag(null);
        assertNull(endTagWithNullName.asEndTag().getName());
    }

    @Test
    public void testComment() {
        Token comment = new Token.Comment();
        assertEquals(TokenType.Comment, comment.type);
        assertEquals("Comment", comment.tokenType());

        Token commentWithData = new Token.Comment().data("This is a comment.");
        assertEquals("Comment", commentWithData.tokenType());
        assertEquals("Comment", commentWithData.tokenType());
        assertEquals("This is a comment.", commentWithData.asComment().getData());
    }

    @Test
    public void testCharacter() {
        Token character = new Token.Character("&nbsp;");
        assertEquals(TokenType.Character, character.type);
        assertEquals("Character", character.tokenType());
        assertEquals("&nbsp;", character.asCharacter().getData());

        Token characterWithSingleQuote = new Token.Character("'");
        assertEquals("Character", characterWithSingleQuote.tokenType());
        assertEquals("'", characterWithSingleQuote.asCharacter().getData());

        Token characterWithDoubleQuote = new Token.Character("\"");
        assertEquals("Character", characterWithDoubleQuote.tokenType());
        assertEquals("\"", characterWithDoubleQuote.asCharacter().getData());

        Token characterWithNullData = new Token.Character(null);
        assertNull(characterWithNullData.asCharacter().getData());
    }

    @Test
    public void testEOF() {
        Token eof = new Token.EOF();
        assertEquals(TokenType.EOF, eof.type);
        assertEquals("EOF", eof.tokenType());
    }

    @Test
    public void testTokenType() {
        Token doctype = new Token.Doctype();
        assertEquals(TokenType.Doctype, doctype.type);
        assertFalse(doctype.isStartTag());
        assertFalse(doctype.isEndTag());
        assertFalse(doctype.isComment());
        assertFalse(doctype.isCharacter());
        assertFalse(doctype.isEOF());

        Token startTag = new Token.StartTag();
        assertFalse(startTag.isDoctype());
        assertTrue(startTag.isStartTag());
        assertFalse(startTag.isEndTag());
        assertFalse(startTag.isComment());
        assertFalse(startTag.isCharacter());
        assertFalse(startTag.isEOF());

        Token endTag = new Token.EndTag();
        assertFalse(endTag.isDoctype());
        assertFalse(endTag.isStartTag());
        assertTrue(endTag.isEndTag());
        assertFalse(endTag.isComment());
        assertFalse(endTag.isCharacter());
        assertFalse(endTag.isEOF());

        Token comment = new Token.Comment();
        assertFalse(comment.isDoctype());
        assertFalse(comment.isStartTag());
        assertFalse(comment.isEndTag());
        assertTrue(comment.isComment());
        assertFalse(comment.isCharacter());
        assertFalse(comment.isEOF());

        Token character = new Token.Character("a");
        assertFalse(character.isDoctype());
        assertFalse(character.isStartTag());
        assertFalse(character.isEndTag());
        assertFalse(character.isComment());
        assertFalse(character.isEOF());
        assertTrue(character.isCharacter());

        Token eof = new Token.EOF();
        assertFalse(eof.isDoctype());
        assertFalse(eof.isStartTag());
        assertFalse(eof.isEndTag());
        assertFalse(eof.isComment());
        assertFalse(eof.isCharacter());
        assertTrue(eof.isEOF());
    }

    @Test
    public void testTokenTypeEnum() {
        TokenType type = TokenType.Doctype;
        assertEquals(TokenType.Doctype, type);
        assertEquals("DOCTYPE", type.name());

        type = TokenType.StartTag;
        assertEquals(TokenType.StartTag, type);
        assertEquals("START_TAG", type.name());

        type = TokenType.EndTag;
        assertEquals(TokenType.EndTag, type);
        assertEquals("END_TAG", type.name());

        type = TokenType.Comment;
        assertEquals(TokenType.Comment, type);
        assertEquals("COMMENT", type.name());

        type = TokenType.Character;
        assertEquals(TokenType.Character, type);
        assertEquals("CHARACTER", type.name());

        type = TokenType.EOF;
        assertEquals(TokenType.EOF, type);
        assertEquals("EOF", type.name());
    }
}