package org.jsoup.parser;

import org.junit.Test;

import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("html");
        doctype.publicIdentifier.append("-//W3C//DTD HTML 4.01 Transitional//EN");
        doctype.systemIdentifier.append("http://www.w3.org/TR/html4/loose.dtd");
        doctype.forceQuirks = true;

        assertEquals("html", doctype.getName());
        assertEquals("-//W3C//DTD HTML 4.01 Transitional//EN", doctype.getPublicIdentifier());
        assertEquals("http://www.w3.org/TR/html4/loose.dtd", doctype.getSystemIdentifier());
        assertTrue(doctype.isForceQuirks());
    }

    @Test
    public void testStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("div");
        startTag.selfClosing = true;

        assertEquals("div", startTag.name());
        assertTrue(startTag.isSelfClosing());
    }

    @Test
    public void testEndTag() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.name("div");

        assertEquals("div", endTag.name());
    }

    @Test
    public void testComment() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("This is a comment.");

        assertEquals("This is a comment.", comment.getData());
    }

    @Test
    public void testCharacter() {
        Token.Character character = new Token.Character("A");

        assertEquals("A", character.getData());
    }

    @Test
    public void testEOF() {
        Token.EOF eof = new Token.EOF();

        assertTrue(eof.isEOF());
    }
}