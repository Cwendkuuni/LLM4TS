package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;
import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctype() {
        Token doctype = new Token.Doctype();
        Token doctypeWithName = new Token.Doctype().name("html");
        Token doctypeWithNamePublicIdentifier = new Token.Doctype().name("html").publicIdentifier("XHTML 1.0");
        Token doctypeWithNameSystemIdentifier = new Token.Doctype().name("html").systemIdentifier("/path/to/file.html");
        Token doctypeWithNameForceQuirks = new Token.Doctype().name("html").forceQuirks(true);
        Token doctypeWithNamePublicIdentifierSystemIdentifier = new Token.Doctype().name("html")
                .publicIdentifier("XHTML 1.0")
                .systemIdentifier("/path/to/file.html")
                .forceQuirks(true);

        assertEquals("Doctype", doctype.tokenType());
        assertEquals("Doctype", doctypeWithName.tokenType());
        assertEquals("html", doctypeWithName.getName());
        assertEquals("XHTML 1.0", doctypeWithNamePublicIdentifier.getPublicIdentifier());
        assertEquals("/path/to/file.html", doctypeWithNameSystemIdentifier.getSystemIdentifier());
        assertTrue(doctypeWithNameForceQuirks.isForceQuirks());
        assertTrue(doctypeWithNamePublicIdentifierSystemIdentifier.isForceQuirks());
    }

    @Test
    public void testTag() {
        Token tag = new Token.StartTag().name("div");
        Token tagWithAttributes = new Token.StartTag().name("a").attributes(new Attributes().put("href", "http://example.com"));
        Token tagWithSelfClosing = new Token.StartTag().name("img").selfClosing(true);
        Token tagWithNameAndAttributes = new Token.StartTag().name("button").attributes(new Attributes().put("type", "submit").put("class", "btn btn-primary"));

        assertEquals("StartTag", tag.tokenType());
        assertEquals("StartTag", tag.tokenType());
        assertEquals("div", tag.getName());
        assertEquals("a", tagWithAttributes.getName());
        assertEquals("href", tagWithAttributes.getAttributes().get("href").getName());
        assertEquals("http://example.com", tagWithAttributes.getAttributes().get("href").getValue());
        assertTrue(tagWithSelfClosing.isSelfClosing());
        assertEquals("button", tagWithNameAndAttributes.getName());
        assertEquals("submit", tagWithNameAndAttributes.getAttributes().get("type").getValue());
        assertEquals("btn btn-primary", tagWithNameAndAttributes.getAttributes().getClassAttribute());
    }

    @Test
    public void testComment() {
        Token comment = new Token.Comment().getData("This is a comment.");
        Token commentWithData = new Token.Comment().getData("/* This is a multi-line comment */");

        assertEquals("Comment", comment.tokenType());
        assertEquals("Comment", comment.tokenType());
        assertEquals("This is a comment.", comment.getData());
        assertEquals("/* This is a multi-line comment */", commentWithData.getData());
    }

    @Test
    public void testCharacter() {
        Token character = new Token.Character("&nbsp;");
        Token characterWithData = new Token.Character("Hello, &world!");

        assertEquals("Character", character.tokenType());
        assertEquals("Character", character.tokenType());
        assertEquals("&nbsp;", character.getData());
        assertEquals("Hello, &world!", characterWithData.getData());
    }

    @Test
    public void testEOF() {
        Token eof = new Token.EOF();

        assertEquals("EOF", eof.tokenType());
    }
}