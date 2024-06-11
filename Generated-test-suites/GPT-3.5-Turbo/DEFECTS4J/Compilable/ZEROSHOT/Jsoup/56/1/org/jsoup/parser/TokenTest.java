package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.forceQuirks = true;
        doctype.name.append("html");
        doctype.publicIdentifier.append("-//W3C//DTD HTML 4.01 Transitional//EN");
        doctype.systemIdentifier.append("http://www.w3.org/TR/html4/loose.dtd");

        assertEquals("Doctype", doctype.tokenType());
        assertEquals("html", doctype.getName());
        assertEquals("-//W3C//DTD HTML 4.01 Transitional//EN", doctype.getPublicIdentifier());
        assertEquals("http://www.w3.org/TR/html4/loose.dtd", doctype.getSystemIdentifier());
        assertTrue(doctype.isForceQuirks());

        doctype.reset();

        assertEquals("", doctype.getName());
        assertEquals("", doctype.getPublicIdentifier());
        assertEquals("", doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("div");
        startTag.selfClosing = false;
        startTag.appendAttributeName("class");
        startTag.appendAttributeValue("content");

        assertEquals("StartTag", startTag.tokenType());
        assertEquals("div", startTag.name());
        assertFalse(startTag.isSelfClosing());
        assertNotNull(startTag.getAttributes());
        assertEquals("content", startTag.getAttributes().get("class"));

        startTag.reset();

        assertNull(startTag.name());
        assertFalse(startTag.isSelfClosing());
        assertNull(startTag.getAttributes());
    }

    @Test
    public void testEndTag() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.name("div");

        assertEquals("EndTag", endTag.tokenType());
        assertEquals("div", endTag.name());

        endTag.reset();

        assertNull(endTag.name());
    }

    @Test
    public void testComment() {
        Token.Comment comment = new Token.Comment();
        comment.bogus = true;
        comment.data.append("This is a comment");

        assertEquals("Comment", comment.tokenType());
        assertEquals("This is a comment", comment.getData());
        assertTrue(comment.bogus);

        comment.reset();

        assertEquals("", comment.getData());
        assertFalse(comment.bogus);
    }

    @Test
    public void testCharacter() {
        Token.Character character = new Token.Character();
        character.data("This is some text");

        assertEquals("Character", character.tokenType());
        assertEquals("This is some text", character.getData());

        character.reset();

        assertNull(character.getData());
    }

    @Test
    public void testEOF() {
        Token.EOF eof = new Token.EOF();

        assertEquals("EOF", eof.tokenType());
    }
}