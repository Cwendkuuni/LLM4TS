package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;

public class TokenTest {

    @Test
    public void testDoctypeToken() {
        Token doctype = new Token.Doctype();
        assertEquals("Doctype", doctype.tokenType());

        Token doctypeWithName = new Token.Doctype();
        doctypeWithName.asDoctype().name("html");
        assertEquals("Doctype", doctypeWithName.tokenType());
        assertEquals("html", doctypeWithName.asDoctype().getName());

        Token doctypeWithNamePublicIdentifier = new Token.Doctype();
        doctypeWithNamePublicIdentifier.asDoctype().name("html").publicIdentifier("_xml");
        assertEquals("Doctype", doctypeWithNamePublicIdentifier.tokenType());
        assertEquals("html", doctypeWithNamePublicIdentifier.asDoctype().getName());
        assertEquals("_xml", doctypeWithNamePublicIdentifier.asDoctype().getPublicIdentifier());

        Token doctypeWithNameSystemIdentifier = new Token.Doctype();
        doctypeWithNameSystemIdentifier.asDoctype().name("html").systemIdentifier("about:blank");
        assertEquals("Doctype", doctypeWithNameSystemIdentifier.tokenType());
        assertEquals("html", doctypeWithNameSystemIdentifier.asDoctype().getName());
        assertEquals("about:blank", doctypeWithNameSystemIdentifier.asDoctype().getSystemIdentifier());

        Token doctypeWithForceQuirks = new Token.Doctype();
        doctypeWithForceQuirks.asDoctype().isForceQuirks(true);
        assertEquals("Doctype", doctypeWithForceQuirks.tokenType());
        assertTrue(doctypeWithForceQuirks.asDoctype().isForceQuirks());
    }

    @Test
    public void testTagToken() {
        Token tag = new Token.StartTag();
        assertEquals("StartTag", tag.tokenType());

        Token tagWithName = new Token.StartTag();
        tagWithName.asStartTag().name("div");
        assertEquals("StartTag", tagWithName.tokenType());
        assertEquals("div", tagWithName.asStartTag().name());

        Token tagWithNameAndAttributes = new Token.StartTag();
        tagWithNameAndAttributes.asStartTag().name("input").attributes(new Attributes().put("type", "text").put("name", "username"));
        assertEquals("StartTag", tagWithNameAndAttributes.tokenType());
        assertEquals("div", tagWithName.asStartTag().name());
        assertEquals("input", tagWithNameAndAttributes.asStartTag().name());
        assertEquals(2, tagWithNameAndAttributes.asStartTag().getAttributes().size());
        assertEquals("text", tagWithNameAndAttributes.asStartTag().getAttributes().get("type"));
        assertEquals("username", tagWithNameAndAttributes.asStartTag().getAttributes().get("name"));

        Token tagWithSelfClosing = new Token.StartTag();
        tagWithSelfClosing.asStartTag().name("img").selfClosing(true);
        assertEquals("StartTag", tagWithSelfClosing.tokenType());
        assertEquals("img", tagWithSelfClosing.asStartTag().name());
        assertTrue(tagWithSelfClosing.asStartTag().isSelfClosing());

        Token tagWithNameAndAttributesAndSelfClosing = new Token.StartTag();
        tagWithNameAndAttributesAndSelfClosing.asStartTag().name("link").attributes(new Attributes().put("rel", "stylesheet").put("href", "/styles.css")).selfClosing(true);
        assertEquals("StartTag", tagWithNameAndAttributesAndSelfClosing.tokenType());
        assertEquals("link", tagWithNameAndAttributesAndSelfClosing.asStartTag().name());
        assertTrue(tagWithNameAndAttributesAndSelfClosing.asStartTag().isSelfClosing());
        assertEquals(2, tagWithNameAndAttributesAndSelfClosing.asStartTag().getAttributes().size());
        assertEquals("stylesheet", tagWithNameAndAttributesAndSelfClosing.asStartTag().getAttributes().get("rel"));
        assertEquals("/styles.css", tagWithNameAndAttributesAndSelfClosing.asStartTag().getAttributes().get("href"));

        Token tagWithNameAndChildren = new Token.StartTag();
        Element child1 = new Element("p");
        child1.text("Paragraph 1");
        Element child2 = new Element("p");
        child2.text("Paragraph 2");
        tagWithNameAndChildren.asStartTag().name("div").children(child1, child2);
        assertEquals("StartTag", tagWithNameAndChildren.tokenType());
        assertEquals("div", tagWithNameAndChildren.asStartTag().name());
        assertEquals(2, tagWithNameAndChildren.asStartTag().childrenSize());
        assertEquals("Paragraph 1", tagWithNameAndChildren.asStartTag().child(0).text());
        assertEquals("Paragraph 2", tagWithNameAndChildren.asStartTag().child(1).text());
    }

    @Test
    public void testCommentToken() {
        Token comment = new Token.Comment();
        assertEquals("Comment", comment.tokenType());

        Token commentWithData = new Token.Comment();
        commentWithData.asComment().data("This is a comment.");
        assertEquals("Comment", commentWithData.tokenType());
        assertEquals("This is a comment.", commentWithData.asComment().getData());
    }

    @Test
    public void testCharacterToken() {
        Token character = new Token.Character(' ');
        assertEquals("Character", character.tokenType());
        assertEquals(" ", character.asCharacter().getData());

        Token characterWithData = new Token.Character('&');
        characterWithData.asCharacter().appendAttributeValue("amp;");
        assertEquals("Character", characterWithData.tokenType());
        assertEquals("&amp;", characterWithData.asCharacter().getData());
    }

    @Test
    public void testEOFToken() {
        Token eof = new Token.EOF();
        assertEquals("EOF", eof.tokenType());
        assertFalse(eof.isDoctype());
        assertFalse(eof.isStartTag());
        assertFalse(eof.isEndTag());
        assertFalse(eof.isComment());
        assertFalse(eof.isCharacter());
        assertNull(eof.asDoctype());
        assertNull(eof.asStartTag());
        assertNull(eof.asEndTag());
        assertNull(eof.asComment());
        assertNull(eof.asCharacter());
    }

    @Test
    public void testTokenTypeEnum() {
        assertEquals(Token.TokenType.Doctype, Token.TokenType.valueOf("Doctype"));
        assertEquals(Token.TokenType.StartTag, Token.TokenType.valueOf("StartTag"));
        assertEquals(Token.TokenType.EndTag, Token.TokenType.valueOf("EndTag"));
        assertEquals(Token.TokenType.Comment, Token.TokenType.valueOf("Comment"));
        assertEquals(Token.TokenType.Character, Token.TokenType.valueOf("Character"));
        assertEquals(Token.TokenType.EOF, Token.TokenType.valueOf("EOF"));
    }
}