package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testDoctypeToken() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("html");
        doctype.publicIdentifier.append("-//W3C//DTD HTML 4.01//EN");
        doctype.systemIdentifier.append("http://www.w3.org/TR/html4/strict.dtd");
        doctype.forceQuirks = false;

        assertEquals("html", doctype.getName());
        assertEquals("-//W3C//DTD HTML 4.01//EN", doctype.getPublicIdentifier());
        assertEquals("http://www.w3.org/TR/html4/strict.dtd", doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testStartTagToken() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";
        startTag.selfClosing = true;
        startTag.attributes = new Attributes();
        startTag.attributes.put("id", "myDiv");

        assertEquals("div", startTag.name());
        assertTrue(startTag.isSelfClosing());
        assertNotNull(startTag.getAttributes());
        assertEquals("id", startTag.getAttributes().get("id").getKey());
        assertEquals("myDiv", startTag.getAttributes().get("id").getValue());
    }

    @Test
    public void testEndTagToken() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "p";

        assertEquals("p", endTag.name());
    }

    @Test
    public void testCommentToken() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("This is a comment");

        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testCharacterToken() {
        Token.Character character = new Token.Character("A");

        assertEquals("A", character.getData());
    }

    @Test
    public void testEOFToken() {
        Token.EOF eof = new Token.EOF();

        assertTrue(eof.isEOF());
    }
}