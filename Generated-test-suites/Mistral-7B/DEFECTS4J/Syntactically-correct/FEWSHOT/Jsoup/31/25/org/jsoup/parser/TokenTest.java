package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctype() {
        Token doctype = new Token.Doctype();

        assertEquals("Doctype", doctype.tokenType());

        Token doctypeWithName = new Token.Doctype();
        doctypeWithName.asDoctype().name("html");
        assertEquals("Doctype", doctypeWithName.tokenType());
        assertEquals("html", doctypeWithName.asDoctype().getName());

        Token doctypeWithPublicIdentifier = new Token.Doctype();
        doctypeWithPublicIdentifier.asDoctype().name("html");
        doctypeWithPublicIdentifier.asDoctype().getPublicIdentifier().append("UTF-8");
        assertEquals("Doctype", doctypeWithPublicIdentifier.tokenType());
        assertEquals("html", doctypeWithPublicIdentifier.asDoctype().getName());
        assertEquals("UTF-8", doctypeWithPublicIdentifier.asDoctype().getPublicIdentifier());

        Token doctypeWithSystemIdentifier = new Token.Doctype();
        doctypeWithSystemIdentifier.asDoctype().name("html");
        doctypeWithSystemIdentifier.asDoctype().getSystemIdentifier().append("/index.html");
        assertEquals("Doctype", doctypeWithSystemIdentifier.tokenType());
        assertEquals("html", doctypeWithSystemIdentifier.asDoctype().getName());
        assertEquals("/index.html", doctypeWithSystemIdentifier.asDoctype().getSystemIdentifier());

        Token doctypeWithForceQuirks = new Token.Doctype();
        doctypeWithForceQuirks.asDoctype().name("html");
        doctypeWithForceQuirks.asDoctype().isForceQuirks(true);
        assertEquals("Doctype", doctypeWithForceQuirks.tokenType());
        assertEquals("html", doctypeWithForceQuirks.asDoctype().getName());
        assertTrue(doctypeWithForceQuirks.asDoctype().isForceQuirks());
    }

    @Test
    public void testStartTag() {
        Token startTag = new Token.StartTag();

        assertEquals("StartTag", startTag.tokenType());

        Token startTagWithName = new Token.StartTag();
        startTagWithName.asStartTag().name("p");
        assertEquals("StartTag", startTagWithName.tokenType());
        assertEquals("p", startTagWithName.asStartTag().name());

        Token startTagWithAttributes = new Token.StartTag();
        startTagWithAttributes.asStartTag().name("a").attributes(new Token.Attributes().put(new Attribute("href", "http://example.com")));
        assertEquals("StartTag", startTagWithAttributes.tokenType());
        assertEquals("a", startTagWithAttributes.asStartTag().name());
        assertEquals("href", startTagWithAttributes.asStartTag().getAttributes().getClass().getSimpleName());
        assertEquals("http://example.com", startTagWithAttributes.asStartTag().getAttributes().get("href"));

        Token startTagWithSelfClosing = new Token.StartTag();
        startTagWithSelfClosing.asStartTag().name("img").selfClosing(true);
        assertEquals("StartTag", startTagWithSelfClosing.tokenType());
        assertEquals("img", startTagWithSelfClosing.asStartTag().name());
        assertTrue(startTagWithSelfClosing.asStartTag().isSelfClosing());

        Token startTagWithNameAndAttributes = new Token.StartTag();
        startTagWithNameAndAttributes.asStartTag().name("input").attributes(new Token.Attributes().put(new Attribute("type", "text")).put(new Attribute("name", "username")));
        assertEquals("StartTag", startTagWithNameAndAttributes.tokenType());
        assertEquals("input", startTagWithNameAndAttributes.asStartTag().name());
        assertEquals("type", startTagWithNameAndAttributes.asStartTag().getAttributes().get("type"));
        assertEquals("text", startTagWithNameAndAttributes.asStartTag().getAttributes().get("type").getValue());
        assertEquals("name", startTagWithNameAndAttributes.asStartTag().getAttributes().get("name"));
        assertEquals("username", startTagWithNameAndAttributes.asStartTag().getAttributes().get("name").getValue());
    }

    @Test
    public void testEndTag() {
        Token endTag = new Token.EndTag();

        assertEquals("EndTag", endTag.tokenType());

        Token endTagWithName = new Token.EndTag();
        endTagWithName.asEndTag().name("p");
        assertEquals("EndTag", endTagWithName.tokenType());
        assertEquals("p", endTagWithName.asEndTag().name());

        Token endTagWithNameAndAttributes = new Token.EndTag();
        endTagWithNameAndAttributes.asEndTag().name("input");
        assertEquals("EndTag", endTagWithNameAndAttributes.tokenType());
        assertEquals("input", endTagWithNameAndAttributes.asEndTag().name());
    }

    @Test
    public void testComment() {
        Token comment = new Token.Comment();

        assertEquals("Comment", comment.tokenType());

        Token commentWithData = new Token.Comment();
        commentWithData.asComment().getData().append("This is a comment.");
        assertEquals("Comment", commentWithData.tokenType());
        assertEquals("This is a comment.", commentWithData.asComment().getData());
    }

    @Test
    public void testCharacter() {
        Token character = new Token.Character("&hellip;");

        assertEquals("Character", character.tokenType());
        assertEquals("&hellip;", character.getData());

        Token characterWithData = new Token.Character("&amp;");
        assertEquals("Character", characterWithData.tokenType());
        assertEquals("&amp;", characterWithData.getData());

        Token characterWithDataAndSelfClosing = new Token.Character("\n");
        characterWithDataAndSelfClosing.asCharacter().selfClosing(true);
        assertEquals("Character", characterWithDataAndSelfClosing.tokenType());
        assertEquals("\n", characterWithDataAndSelfClosing.getData());
        assertTrue(characterWithDataAndSelfClosing.isCharacter());
        assertTrue(characterWithDataAndSelfClosing.isEOF());
    }

    @Test
    public void testEOF() {
        Token eof = new Token.EOF();

        assertEquals("EOF", eof.tokenType());
        assertTrue(eof.isEOF());
    }

    @Test
    public void testTokenTypes() {
        Token doctype = new Token.Doctype();
        Token startTag = new Token.StartTag();
        Token endTag = new Token.EndTag();
        Token comment = new Token.Comment();
        Token character = new Token.Character("");
        Token eof = new Token.EOF();

        assertEquals(TokenType.Doctype, doctype.isDoctype());
        assertEquals(TokenType.StartTag, startTag.isStartTag());
        assertEquals(TokenType.EndTag, endTag.isEndTag());
        assertEquals(TokenType.Comment, comment.isComment());
        assertEquals(TokenType.Character, character.isCharacter());
        assertEquals(TokenType.EOF, eof.isEOF());

        assertSame(Token.Doctype.class, doctype.getClass());
        assertSame(Token.StartTag.class, startTag.getClass());
        assertSame(Token.EndTag.class, endTag.getClass());
        assertSame(Token.Comment.class, comment.getClass());
        assertSame(Token.Character.class, character.getClass());
        assertSame(Token.EOF.class, eof.getClass());
    }

    @Test
    public void testGetAttributes() {
        Token startTag = new Token.StartTag();
        Token attributes = new Token.Attributes();
        attributes.put(new Attribute("id", "button"));
        startTag.asStartTag().attributes = attributes;

        assertEquals("Attributes", startTag.asStartTag().getAttributes().tokenType());
        assertEquals("id", startTag.asStartTag().getAttributes().getClass().getSimpleName());
        assertEquals("button", startTag.asStartTag().getAttributes().get("id").getValue());
    }
}