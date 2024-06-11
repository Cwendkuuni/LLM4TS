package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token.Doctype doctype = new Token.Doctype();
        Token.StartTag startTag = new Token.StartTag();
        Token.EndTag endTag = new Token.EndTag();
        Token.Comment comment = new Token.Comment();
        Token.Character character = new Token.Character("");
        Token.EOF eof = new Token.EOF();

        assertEquals(Token.TokenType.Doctype, doctype.type);
        assertEquals(Token.TokenType.StartTag, startTag.type);
        assertEquals(Token.TokenType.EndTag, endTag.type);
        assertEquals(Token.TokenType.Comment, comment.type);
        assertEquals(Token.TokenType.Character, character.type);
        assertEquals(Token.TokenType.EOF, eof.type);
    }

    @Test
    public void testDoctype() {
        Token.Doctype doctype = new Token.Doctype();

        doctype.name.append("html");
        assertEquals("html", doctype.getName());

        doctype.publicIdentifier.append("-//W3C//DTD XHTML 1.0 Transitional//EN");
        assertEquals("-//W3C//DTD XHTML 1.0 Transitional//EN", doctype.getPublicIdentifier());

        doctype.systemIdentifier.append("http://www.example.org/xhtml1/DTD/xhtml1-transitional.dtd");
        assertEquals("http://www.example.org/xhtml1/DTD/xhtml1-transitional.dtd", doctype.getSystemIdentifier());

        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testTag() {
        Token.Tag tag = new Token.StartTag();

        tag.tagName = "div";
        assertEquals("div", tag.name());

        assertFalse(tag.isSelfClosing());
    }

    @Test
    public void testStartTag() {
        Token.StartTag startTag = new Token.StartTag();

        startTag.tagName = "div";
        assertEquals("div", startTag.name());

        assertFalse(startTag.isSelfClosing());

        Attributes attributes = new Attributes();
        attributes.put("id", "myDiv");
        attributes.put("class", "main");

        startTag.attributes = attributes;
        assertNotNull(startTag.getAttributes());
    }

    @Test
    public void testEndTag() {
        Token.EndTag endTag = new Token.EndTag();

        endTag.tagName = "div";
        assertEquals("div", endTag.name());
    }

    @Test
    public void testComment() {
        Token.Comment comment = new Token.Comment();

        comment.data.append("This is a comment");

        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testCharacter() {
        Token.Character character = new Token.Character("");

        character.data.append("Some character data");

        assertEquals("Some character data", character.getData());
    }

    @Test
    public void testEOF() {
        Token.EOF eof = new Token.EOF();
    }
}