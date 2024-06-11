package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctype() {
        Token doctype = new Token.Doctype();
        Token doctypeExpected = new Token.Doctype();

        assertEquals(TokenType.Doctype, doctype.type);
        assertEquals(doctype.getClass(), doctypeExpected.getClass());

        Token doctypeWithName = new Token.Doctype();
        doctypeWithName.name("html");
        assertEquals("html", doctypeWithName.asDoctype().getName());

        Token doctypeWithPublicIdentifier = new Token.Doctype();
        doctypeWithPublicIdentifier.name("html");
        doctypeWithPublicIdentifier.publicIdentifier("HTML 5");
        assertEquals("HTML 5", doctypeWithPublicIdentifier.asDoctype().getPublicIdentifier());

        Token doctypeWithSystemIdentifier = new Token.Doctype();
        doctypeWithSystemIdentifier.name("html");
        doctypeWithSystemIdentifier.systemIdentifier("/path/to/file.html");
        assertEquals("/path/to/file.html", doctypeWithSystemIdentifier.asDoctype().getSystemIdentifier());

        Token doctypeWithForceQuirks = new Token.Doctype();
        doctypeWithForceQuirks.name("html");
        doctypeWithForceQuirks.forceQuirks(true);
        assertTrue(doctypeWithForceQuirks.asDoctype().isForceQuirks());
    }

    @Test
    public void testTag() {
        Token tag = new Token.StartTag();
        Token tagExpected = new Token.StartTag();

        assertEquals(TokenType.StartTag, tag.type);
        assertEquals(tag.getClass(), tagExpected.getClass());

        Token tagWithName = new Token.StartTag();
        tagWithName.name("div");
        assertEquals("div", tagWithName.asStartTag().name());

        Token tagWithSelfClosing = new Token.StartTag();
        tagWithSelfClosing.tagName("img");
        tagWithSelfClosing.selfClosing(true);
        assertTrue(tagWithSelfClosing.asStartTag().isSelfClosing());

        Token tagWithAttributes = new Token.StartTag();
        tagWithAttributes.tagName("a");
        tagWithAttributes.newAttribute();
        tagWithAttributes.appendAttributeName("href");
        tagWithAttributes.appendAttributeValue("https://example.com");
        tagWithAttributes.newAttribute();
        tagWithAttributes.appendAttributeName("target");
        tagWithAttributes.appendAttributeValue("_blank");
        assertEquals("href=\"https://example.com\" target=\"_blank\"", tagWithAttributes.asStartTag().getAttributes().toString());

        Token tagWithNestedAttributes = new Token.StartTag();
        tagWithNestedAttributes.tagName("ul");
        tagWithNestedAttributes.newAttribute();
        tagWithNestedAttributes.appendAttributeName("class");
        tagWithNestedAttributes.appendAttributeValue("menu");
        tagWithNestedAttributes.newAttribute();
        tagWithNestedAttributes.appendAttributeName("id");
        tagWithNestedAttributes.appendAttributeValue("main-menu");
        assertEquals("class=\"menu\" id=\"main-menu\"", tagWithNestedAttributes.asStartTag().getAttributes().toString());

        Token tagWithSelfClosingAndAttributes = new Token.StartTag();
        tagWithSelfClosingAndAttributes.tagName("img");
        tagWithSelfClosingAndAttributes.selfClosing(true);
        tagWithSelfClosingAndAttributes.newAttribute();
        tagWithSelfClosingAndAttributes.appendAttributeName("src");
        tagWithSelfClosingAndAttributes.appendAttributeValue("image.png");
        tagWithSelfClosingAndAttributes.newAttribute();
        tagWithSelfClosingAndAttributes.appendAttributeName("alt");
        tagWithSelfClosingAndAttributes.appendAttributeValue("Image");
        assertEquals("<img src=\"image.png\" alt=\"Image\" />", tagWithSelfClosingAndAttributes.toString());
    }

    @Test
    public void testEndTag() {
        Token endTag = new Token.EndTag();
        Token endTagExpected = new Token.EndTag();

        assertEquals(TokenType.EndTag, endTag.type);
        assertEquals(endTag.getClass(), endTagExpected.getClass());

        Token endTagWithName = new Token.EndTag();
        endTagWithName.tagName("li");
        assertEquals("li", endTagWithName.asEndTag().name());

        Token tag = new Token.StartTag();
        tag.tagName("ul");
        Token endTagWithNestedTag = new Token.EndTag();
        endTagWithNestedTag.tagName("li");
        assertEquals("<li>", endTagWithNestedTag.toString());
        assertTrue(tag.asStartTag().getAttributes().putIfAbsent("class", new Attribute("class", "")) == null);
        tag.asStartTag().getAttributes().put("class", new Attribute("class", "menu"));
        assertEquals("<ul class=\"menu\">\n<li>", tag.toString());
        assertEquals("<ul class=\"menu\">", endTagWithNestedTag.asEndTag().getParentTag().toString());
        assertEquals("li", endTagWithNestedTag.asEndTag().name());
    }

    @Test
    public void testComment() {
        Token comment = new Token.Comment();
        Token commentExpected = new Token.Comment();

        assertEquals(TokenType.Comment, comment.type);
        assertEquals(comment.getClass(), commentExpected.getClass());

        Token commentWithData = new Token.Comment();
        commentWithData.data("<!-- This is a comment -->");
        assertEquals("This is a comment", commentWithData.asComment().getData());
    }

    @Test
    public void testCharacter() {
        Token character = new Token.Character("\\n");
        Token characterExpected = new Token.Character("\\n");

        assertEquals(TokenType.Character, character.type);
        assertEquals(character.getClass(), characterExpected.getClass());

        assertEquals("\\n", character.getData());
    }

    @Test
    public void testEOF() {
        Token eof = new Token.EOF();
        Token eofExpected = new Token.EOF();

        assertEquals(TokenType.EOF, eof.type);
        assertEquals(eof.getClass(), eofExpected.getClass());

        assertNull(eof.asCharacter());
        assertNull(eof.asComment());
        assertNull(eof.asDoctype());
        assertNull(eof.asEndTag());
        assertNull(eof.asStartTag());
    }

    @Test
    public void testTokenTypeEnum() {
        TokenType tokenTypeDoctype = TokenType.Doctype;
        TokenType tokenTypeStartTag = TokenType.StartTag;
        TokenType tokenTypeEndTag = TokenType.EndTag;
        TokenType tokenTypeComment = TokenType.Comment;
        TokenType tokenTypeCharacter = TokenType.Character;
        TokenType tokenTypeEOF = TokenType.EOF;

        assertEquals(TokenType.Doctype, tokenTypeDoctype);
        assertEquals(TokenType.StartTag, tokenTypeStartTag);
        assertEquals(TokenType.EndTag, tokenTypeEndTag);
        assertEquals(TokenType.Comment, tokenTypeComment);
        assertEquals(TokenType.Character, tokenTypeCharacter);
        assertEquals(TokenType.EOF, tokenTypeEOF);
    }
}