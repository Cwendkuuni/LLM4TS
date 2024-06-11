package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctype() {
        Token doctype = new Token.Doctype();
        Token doctypeWithName = new Token.Doctype().name("html");
        Token doctypeWithPublicIdentifier = new Token.Doctype().name("html").publicIdentifier("XHTML 1.0");
        Token doctypeWithSystemIdentifier = new Token.Doctype().name("html").systemIdentifier("/path/to/file.html");
        Token doctypeWithForceQuirks = new Token.Doctype().name("html").forceQuirks(true);
        Token doctypeWithNameAndIdentifier = new Token.Doctype().name("html").publicIdentifier("XHTML 1.0").systemIdentifier("/path/to/file.html");
        Token doctypeWithNameAndForceQuirks = new Token.Doctype().name("html").forceQuirks(true);

        assertEquals("Doctype", doctype.tokenType());
        assertEquals("Doctype", doctypeWithName.tokenType());
        assertEquals("html", doctypeWithName.getName());
        assertEquals("html", doctypeWithName.getName());
        assertEquals("", doctype.getPublicIdentifier());
        assertEquals("XHTML 1.0", doctypeWithPublicIdentifier.getPublicIdentifier());
        assertEquals("/path/to/file.html", doctypeWithSystemIdentifier.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());
        assertTrue(doctypeWithForceQuirks.isForceQuirks());
        assertEquals("Doctype", doctypeWithNameAndIdentifier.tokenType());
        assertEquals("html", doctypeWithNameAndIdentifier.getName());
        assertEquals("XHTML 1.0", doctypeWithNameAndIdentifier.getPublicIdentifier());
        assertEquals("/path/to/file.html", doctypeWithNameAndIdentifier.getSystemIdentifier());
        assertEquals("Doctype", doctypeWithNameAndForceQuirks.tokenType());
        assertEquals("html", doctypeWithNameAndForceQuirks.getName());
        assertTrue(doctypeWithNameAndForceQuirks.isForceQuirks());
    }

    @Test
    public void testTag() {
        Token tag = new Token.Tag();
        Token tagWithName = new Token.Tag().name("div");
        Token tagWithSelfClosing = new Token.Tag().name("img").selfClosing(true);
        Token tagWithAttributes = new Token.Tag().name("a").attributes(new Token.Attributes().put("class", "link").put("href", "/page.html"));
        Token tagWithNameAndAttributes = new Token.Tag().name("a").attributes(new Token.Attributes().put("class", "link").put("href", "/page.html"));

        assertEquals("Tag", tag.tokenType());
        assertEquals("Tag", tagWithName.tokenType());
        assertEquals("div", tagWithName.getName());
        assertEquals("div", tagWithName.getName());
        assertFalse(tag.isSelfClosing());
        assertTrue(tagWithSelfClosing.isSelfClosing());
        assertEquals(null, tag.getAttributes());
        assertNotNull(tagWithAttributes.getAttributes());
        assertEquals("class", tagWithNameAndAttributes.getAttributes().keySet().iterator().next().toString());
        assertEquals("link", tagWithAttributes.getAttributes().value("class"));
        assertEquals("/page.html", tagWithAttributes.getAttributes().value("href"));
    }

    @Test
    public void testComment() {
        Token comment = new Token.Comment();
        Token commentWithData = new Token.Comment().data("This is a comment");

        assertEquals("Comment", comment.tokenType());
        assertEquals("Comment", commentWithData.tokenType());
        assertEquals("", comment.getData());
        assertEquals("This is a comment", commentWithData.getData());
    }

    @Test
    public void testCharacter() {
        Token character = new Token.Character("a");
        Token characterWithData = new Token.Character("&copy;");

        assertEquals("Character", character.tokenType());
        assertEquals("Character", characterWithData.tokenType());
        assertEquals("a", character.getData());
        assertEquals("&copy;", characterWithData.getData());
    }

    @Test
    public void testEOF() {
        Token eof = new Token.EOF();

        assertEquals("EOF", eof.tokenType());
    }

    @Test
    public void testTokenType() {
        Token doctype = new Token.Doctype();
        Token startTag = new Token.StartTag();
        Token endTag = new Token.EndTag();
        Token comment = new Token.Comment();
        Token character = new Token.Character("");
        Token eof = new Token.EOF();

        assertTrue(doctype.isDoctype());
        assertFalse(startTag.isDoctype());
        assertFalse(endTag.isDoctype());
        assertTrue(comment.isComment());
        assertFalse(comment.isDoctype());
        assertFalse(comment.isComment());
        assertTrue(character.isCharacter());
        assertFalse(character.isComment());
        assertFalse(character.isDoctype());
        assertFalse(eof.isDoctype());
        assertFalse(eof.isComment());
        assertFalse(eof.isCharacter());

        assertEquals(Token.TokenType.Doctype, doctype.type);
        assertEquals(Token.TokenType.StartTag, startTag.type);
        assertEquals(Token.TokenType.EndTag, endTag.type);
        assertEquals(Token.TokenType.Comment, comment.type);
        assertEquals(Token.TokenType.Character, character.type);
        assertEquals(Token.TokenType.EOF, eof.type);
    }
}