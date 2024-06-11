package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokenTest {

    Token.Doctype doctype;
    Token.StartTag startTag;
    Token.EndTag endTag;
    Token.Comment comment;
    Token.Character character;
    Token.EOF eof;

    @Before
    public void setUp() {
        doctype = new Token.Doctype();
        startTag = new Token.StartTag();
        endTag = new Token.EndTag();
        comment = new Token.Comment();
        character = new Token.Character("");
        eof = new Token.EOF();
    }

    @Test
    public void testDoctype_getName() {
        doctype.name.append("html");
        assertEquals("html", doctype.getName());
    }

    @Test
    public void testDoctype_getPublicIdentifier() {
        doctype.publicIdentifier.append("-//W3C//DTD HTML 4.0//EN");
        assertEquals("-//W3C//DTD HTML 4.0//EN", doctype.getPublicIdentifier());
    }

    @Test
    public void testDoctype_getSystemIdentifier() {
        doctype.systemIdentifier.append("http://www.w3.org/TR/REC-html40/strict.dtd");
        assertEquals("http://www.w3.org/TR/REC-html40/strict.dtd", doctype.getSystemIdentifier());
    }

    @Test
    public void testDoctype_isForceQuirks() {
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testStartTag_name() {
        startTag.tagName = "div";
        assertEquals("div", startTag.name());
    }

    @Test
    public void testStartTag_isSelfClosing() {
        assertFalse(startTag.isSelfClosing());
    }

    @Test
    public void testStartTag_getAttributes() {
        startTag.attributes = new Attributes();
        assertNotNull(startTag.getAttributes());
    }

    @Test
    public void testEndTag_name() {
        endTag.tagName = "div";
        assertEquals("div", endTag.name());
    }

    @Test
    public void testComment_getData() {
        comment.data.append("This is a comment");
        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testCharacter_getData() {
        character = new Token.Character("Some text");
        assertEquals("Some text", character.getData());
    }

    @Test
    public void testEOF() {
        assertTrue(eof.isEOF());
    }
}