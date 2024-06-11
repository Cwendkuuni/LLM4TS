package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

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

        token = new Token.Character("");
        assertEquals("Character", token.tokenType());

        token = new Token.EOF();
        assertEquals("EOF", token.tokenType());
    }

    @Test
    public void testDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("html");
        doctype.publicIdentifier.append("-//W3C//DTD HTML 4.01//EN");
        doctype.systemIdentifier.append("http://www.w3.org/TR/html4/strict.dtd");
        doctype.forceQuirks = true;

        assertEquals("html", doctype.getName());
        assertEquals("-//W3C//DTD HTML 4.01//EN", doctype.getPublicIdentifier());
        assertEquals("http://www.w3.org/TR/html4/strict.dtd", doctype.getSystemIdentifier());
        assertTrue(doctype.isForceQuirks());
    }

    @Test
    public void testTag() {
        Token.Tag tag = new Token.StartTag();
        tag.tagName = "div";
        tag.selfClosing = true;

        assertEquals("div", tag.name());
        assertTrue(tag.isSelfClosing());

        tag = new Token.EndTag();
        tag.tagName = "p";

        assertEquals("p", tag.name());
        assertFalse(tag.isSelfClosing());
    }

    @Test
    public void testStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "a";
        startTag.attributes = new Attributes();
        startTag.attributes.put("href", "https://www.example.com");
        startTag.attributes.put("target", "_blank");

        assertEquals("<a href=\"https://www.example.com\" target=\"_blank\">", startTag.toString());
    }

    @Test
    public void testEndTag() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "div";

        assertEquals("</div>", endTag.toString());
    }

    @Test
    public void testComment() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("This is a comment.");

        assertEquals("This is a comment.", comment.getData());
        assertEquals("<!--This is a comment.-->", comment.toString());
    }

    @Test
    public void testCharacter() {
        Token.Character character = new Token.Character("Hello, world!");

        assertEquals("Hello, world!", character.getData());
        assertEquals("Hello, world!", character.toString());
    }

    @Test
    public void testEOF() {
        Token.EOF eof = new Token.EOF();

        assertTrue(eof.isEOF());
    }

    @Test
    public void testIsDoctype() {
        Token token = new Token.Doctype();
        assertTrue(token.isDoctype());

        token = new Token.StartTag();
        assertFalse(token.isDoctype());

        token = new Token.EndTag();
        assertFalse(token.isDoctype());

        token = new Token.Comment();
        assertFalse(token.isDoctype());

        token = new Token.Character("");
        assertFalse(token.isDoctype());

        token = new Token.EOF();
        assertFalse(token.isDoctype());
    }

    @Test
    public void testAsDoctype() {
        Token token = new Token.Doctype();

        assertTrue(token.asDoctype() instanceof Token.Doctype);
    }

    @Test
    public void testIsStartTag() {
        Token token = new Token.Doctype();
        assertFalse(token.isStartTag());

        token = new Token.StartTag();
        assertTrue(token.isStartTag());

        token = new Token.EndTag();
        assertFalse(token.isStartTag());

        token = new Token.Comment();
        assertFalse(token.isStartTag());

        token = new Token.Character("");
        assertFalse(token.isStartTag());

        token = new Token.EOF();
        assertFalse(token.isStartTag());
    }

    @Test
    public void testAsStartTag() {
        Token token = new Token.StartTag();

        assertTrue(token.asStartTag() instanceof Token.StartTag);
    }

    @Test
    public void testIsEndTag() {
        Token token = new Token.Doctype();
        assertFalse(token.isEndTag());

        token = new Token.StartTag();
        assertFalse(token.isEndTag());

        token = new Token.EndTag();
        assertTrue(token.isEndTag());

        token = new Token.Comment();
        assertFalse(token.isEndTag());

        token = new Token.Character("");
        assertFalse(token.isEndTag());

        token = new Token.EOF();
        assertFalse(token.isEndTag());
    }

    @Test
    public void testAsEndTag() {
        Token token = new Token.EndTag();

        assertTrue(token.asEndTag() instanceof Token.EndTag);
    }

    @Test
    public void testIsComment() {
        Token token = new Token.Doctype();
        assertFalse(token.isComment());

        token = new Token.StartTag();
        assertFalse(token.isComment());

        token = new Token.EndTag();
        assertFalse(token.isComment());

        token = new Token.Comment();
        assertTrue(token.isComment());

        token = new Token.Character("");
        assertFalse(token.isComment());

        token = new Token.EOF();
        assertFalse(token.isComment());
    }

    @Test
    public void testAsComment() {
        Token token = new Token.Comment();

        assertTrue(token.asComment() instanceof Token.Comment);
    }

    @Test
    public void testIsCharacter() {
        Token token = new Token.Doctype();
        assertFalse(token.isCharacter());

        token = new Token.StartTag();
        assertFalse(token.isCharacter());

        token = new Token.EndTag();
        assertFalse(token.isCharacter());

        token = new Token.Comment();
        assertFalse(token.isCharacter());

        token = new Token.Character("");
        assertTrue(token.isCharacter());

        token = new Token.EOF();
        assertFalse(token.isCharacter());
    }

    @Test
    public void testAsCharacter() {
        Token token = new Token.Character("");

        assertTrue(token.asCharacter() instanceof Token.Character);
    }

    @Test
    public void testIsEOF() {
        Token token = new Token.Doctype();
        assertFalse(token.isEOF());

        token = new Token.StartTag();
        assertFalse(token.isEOF());

        token = new Token.EndTag();
        assertFalse(token.isEOF());

        token = new Token.Comment();
        assertFalse(token.isEOF());

        token = new Token.Character("");
        assertFalse(token.isEOF());

        token = new Token.EOF();
        assertTrue(token.isEOF());
    }

}