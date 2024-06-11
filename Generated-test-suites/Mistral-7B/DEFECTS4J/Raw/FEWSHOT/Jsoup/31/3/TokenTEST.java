package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;
import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctype() {
        Token doctype = new Token.Doctype();
        Token doctypeWithName = new Token.Doctype().name("html");
        Token doctypeWithNamePublicId = new Token.Doctype().name("html").publicIdentifier("XHTML");
        Token doctypeWithNameSystemId = new Token.Doctype().name("html").systemIdentifier("index.html");
        Token doctypeWithNamePublicIdSystemId = new Token.Doctype().name("html")
                .publicIdentifier("XHTML").systemIdentifier("index.html");
        Token doctypeWithForceQuirks = new Token.Doctype().name("html").forceQuirks(true);

        assertEquals("Doctype", doctype.tokenType());
        assertEquals("Doctype", doctypeWithName.tokenType());
        assertEquals("html", doctypeWithName.getName());
        assertEquals("XHTML", doctypeWithNamePublicId.getPublicIdentifier());
        assertEquals("index.html", doctypeWithNameSystemId.getSystemIdentifier());
        assertTrue(doctypeWithForceQuirks.isForceQuirks());
    }

    @Test
    public void testTag() {
        Token tag = new Token.Tag();
        Token startTag = new Token.StartTag();
        Token startTagWithName = new Token.StartTag().name("p");
        Token startTagWithNameAttributes = new Token.StartTag().name("p").attributes(new Attributes());
        Token tagWithName = new Token.Tag().name("p");

        assertEquals("Tag", tag.tokenType());
        assertEquals("StartTag", startTag.tokenType());
        assertEquals("p", startTagWithName.name());
        assertNotNull(startTagWithNameAttributes.getAttributes());
        assertEquals("p", tagWithName.name());
    }

    @Test
    public void testComment() {
        Token comment = new Token.Comment();
        Token commentWithData = new Token.Comment().getData("This is a comment");

        assertEquals("Comment", comment.tokenType());
        assertEquals("This is a comment", commentWithData.getData());
    }

    @Test
    public void testCharacter() {
        Token character = new Token.Character("A");

        assertEquals("Character", character.tokenType());
        assertEquals("A", character.getData());
    }

    @Test
    public void testEOF() {
        Token eof = new Token.EOF();

        assertEquals(Token.TokenType.EOF, eof.tokenType());
    }

    @Test
    public void testTokenType() {
        Token token = new Token();
        Doctype doctype = new Token.Doctype();
        StartTag startTag = new Token.StartTag();
        EndTag endTag = new Token.EndTag();
        Comment comment = new Token.Comment();
        Character character = new Token.Character("A");
        EOF eof = new Token.EOF();

        assertTrue(token.isDoctype());
        assertTrue(doctype.isDoctype());
        assertTrue(startTag.isStartTag());
        assertTrue(endTag.isEndTag());
        assertTrue(comment.isComment());
        assertTrue(character.isCharacter());
        assertFalse(doctype.isEndTag());
        assertFalse(startTag.isDoctype());
        assertFalse(endTag.isComment());
        assertFalse(character.isEOF());
    }
}