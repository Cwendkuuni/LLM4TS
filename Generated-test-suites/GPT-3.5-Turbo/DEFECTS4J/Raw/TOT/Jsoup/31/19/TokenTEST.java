package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokenTest {

    private Token.Doctype doctype;
    private Token.StartTag startTag;
    private Token.EndTag endTag;
    private Token.Comment comment;
    private Token.Character character;
    private Token.EOF eof;

    @Before
    public void setUp() {
        doctype = new Token.Doctype();
        startTag = new Token.StartTag();
        endTag = new Token.EndTag();
        comment = new Token.Comment();
        character = new Token.Character("data");
        eof = new Token.EOF();
    }

    @Test
    public void testTokenTypeDoctype() {
        assertEquals(Token.TokenType.Doctype, doctype.type);
    }

    @Test
    public void testTokenTypeStartTag() {
        assertEquals(Token.TokenType.StartTag, startTag.type);
    }

    @Test
    public void testTokenTypeEndTag() {
        assertEquals(Token.TokenType.EndTag, endTag.type);
    }

    @Test
    public void testTokenTypeComment() {
        assertEquals(Token.TokenType.Comment, comment.type);
    }

    @Test
    public void testTokenTypeCharacter() {
        assertEquals(Token.TokenType.Character, character.type);
    }

    @Test
    public void testTokenTypeEOF() {
        assertEquals(Token.TokenType.EOF, eof.type);
    }

    @Test
    public void testTokenTypeName() {
        assertEquals("Doctype", doctype.tokenType());
        assertEquals("StartTag", startTag.tokenType());
        assertEquals("EndTag", endTag.tokenType());
        assertEquals("Comment", comment.tokenType());
        assertEquals("Character", character.tokenType());
        assertEquals("EOF", eof.tokenType());
    }

    @Test
    public void testDoctypeGetName() {
        doctype.name.append("html");
        assertEquals("html", doctype.getName());
    }

    @Test
    public void testDoctypeGetPublicIdentifier() {
        doctype.publicIdentifier.append("-//W3C//DTD HTML 4.01//EN");
        assertEquals("-//W3C//DTD HTML 4.01//EN", doctype.getPublicIdentifier());
    }

    @Test
    public void testDoctypeGetSystemIdentifier() {
        doctype.systemIdentifier.append("http://www.w3.org/TR/html4/strict.dtd");
        assertEquals("http://www.w3.org/TR/html4/strict.dtd", doctype.getSystemIdentifier());
    }

    @Test
    public void testDoctypeIsForceQuirks() {
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testStartTagIsSelfClosing() {
        assertFalse(startTag.isSelfClosing());
    }

    @Test
    public void testStartTagName() {
        startTag.name("div");
        assertEquals("div", startTag.name());
    }

    @Test
    public void testStartTagGetAttributes() {
        startTag.attributes = new Attributes();
        assertEquals(new Attributes(), startTag.getAttributes());
    }

    @Test
    public void testEndTagName() {
        endTag.name("div");
        assertEquals("div", endTag.name());
    }

    @Test
    public void testCommentGetData() {
        comment.data.append("This is a comment");
        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testCharacterGetData() {
        assertEquals("data", character.getData());
    }

    @Test
    public void testEOF() {
        assertEquals(Token.TokenType.EOF, eof.type);
    }
}