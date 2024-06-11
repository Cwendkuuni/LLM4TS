package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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
        doctypeWithPublicIdentifier.asDoctype().name("html").publicIdentifier("HTML 5");
        assertEquals("Doctype", doctypeWithPublicIdentifier.tokenType());
        assertEquals("html", doctypeWithPublicIdentifier.asDoctype().getName());
        assertEquals("HTML 5", doctypeWithPublicIdentifier.asDoctype().getPublicIdentifier());

        Token doctypeWithSystemIdentifier = new Token.Doctype();
        doctypeWithSystemIdentifier.asDoctype().name("html").systemIdentifier("/path/to/page.html");
        assertEquals("Doctype", doctypeWithSystemIdentifier.tokenType());
        assertEquals("html", doctypeWithSystemIdentifier.asDoctype().getName());
        assertEquals("/path/to/page.html", doctypeWithSystemIdentifier.asDoctype().getSystemIdentifier());

        Token doctypeWithForceQuirks = new Token.Doctype();
        doctypeWithForceQuirks.asDoctype().name("html").forceQuirks(true);
        assertEquals("Doctype", doctypeWithForceQuirks.tokenType());
        assertEquals("html", doctypeWithForceQuirks.asDoctype().getName());
        assertTrue(doctypeWithForceQuirks.asDoctype().isForceQuirks());
    }

    @Test
    public void testTag() {
        Token tag = new Token.StartTag();
        assertEquals("StartTag", tag.tokenType());

        Token startTagWithName = new Token.StartTag();
        startTagWithName.asStartTag().name("div");
        assertEquals("StartTag", startTagWithName.tokenType());
        assertEquals("div", startTagWithName.asStartTag().name());

        Token startTagWithNameAndAttributes = new Token.StartTag();
        startTagWithNameAndAttributes.asStartTag().name("input").attributes(new Token.Attributes().put("type", "text").put("name", "username"));
        assertEquals("StartTag", startTagWithNameAndAttributes.tokenType());
        assertEquals("input", startTagWithNameAndAttributes.asStartTag().name());
        assertNotNull(startTagWithNameAndAttributes.asStartTag().getAttributes());
        assertEquals("text", startTagWithNameAndAttributes.asStartTag().getAttributes().get("type"));
        assertEquals("username", startTagWithNameAndAttributes.asStartTag().getAttributes().get("name"));

        Token tagWithSelfClosing = new Token.EndTag();
        tagWithSelfClosing.asEndTag().name("img");
        tagWithSelfClosing.asEndTag().selfClosing(true);
        assertEquals("EndTag", tagWithSelfClosing.tokenType());
        assertEquals("img", tagWithSelfClosing.asEndTag().name());
        assertTrue(tagWithSelfClosing.asEndTag().isSelfClosing());

        Token tagWithNullName = new Token.StartTag();
        assertNull(tagWithNullName.asStartTag().name());
    }

    @Test
    public void testComment() {
        Token comment = new Token.Comment();
        assertEquals("Comment", comment.tokenType());

        Token commentWithData = new Token.Comment();
        commentWithData.asComment().data("This is a comment");
        assertEquals("Comment", commentWithData.tokenType());
        assertEquals("This is a comment", commentWithData.asComment().getData());
    }

    @Test
    public void testCharacter() {
        Token character = new Token.Character("&amp;");
        assertEquals("Character", character.tokenType());
        assertEquals("&amp;", character.asCharacter().getData());

        Token characterWithData = new Token.Character('a');
        characterWithData.asCharacter().data("a");
        assertEquals("Character", characterWithData.tokenType());
        assertEquals("a", characterWithData.asCharacter().getData());
    }

    @Test
    public void testEOF() {
        Token eof = new Token.EOF();
        assertEquals("EOF", eof.tokenType());
    }
}