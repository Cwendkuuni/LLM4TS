package org.jsoup.parser;

import static org.junit.Assert.*;
import org.junit.Test;

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

        token = new Token.Character("data");
        assertEquals("Character", token.tokenType());

        token = new Token.EOF();
        assertEquals("EOF", token.tokenType());
    }

    @Test
    public void testDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        assertFalse(doctype.isForceQuirks());
        assertEquals("", doctype.getName());
        assertEquals("", doctype.getPublicIdentifier());
        assertEquals("", doctype.getSystemIdentifier());

        doctype.name.append("name");
        doctype.publicIdentifier.append("public");
        doctype.systemIdentifier.append("system");
        doctype.forceQuirks = true;

        assertTrue(doctype.isForceQuirks());
        assertEquals("name", doctype.getName());
        assertEquals("public", doctype.getPublicIdentifier());
        assertEquals("system", doctype.getSystemIdentifier());
    }

    @Test
    public void testStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "tag";

        assertFalse(startTag.isSelfClosing());
        assertNull(startTag.getAttributes());
        assertEquals("tag", startTag.name());

        startTag.selfClosing = true;

        assertTrue(startTag.isSelfClosing());

        Attributes attributes = new Attributes();
        attributes.put("attr1", "value1");
        attributes.put("attr2", "value2");
        startTag.attributes = attributes;

        assertNotNull(startTag.getAttributes());
        assertEquals(attributes.toString(), startTag.getAttributes().toString());
    }

    @Test
    public void testEndTag() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "tag";

        assertEquals("</tag>", endTag.toString());
        assertEquals("tag", endTag.name());
    }

    @Test
    public void testComment() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("data");

        assertEquals("data", comment.getData());
        assertEquals("<!--data-->", comment.toString());
    }

    @Test
    public void testCharacter() {
        Token.Character character = new Token.Character("data");

        assertEquals("data", character.getData());
        assertEquals("data", character.toString());
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

        token = new Token.Character("data");
        assertFalse(token.isDoctype());

        token = new Token.EOF();
        assertFalse(token.isDoctype());
    }

    @Test
    public void testAsDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        Token token = doctype;
        assertEquals(doctype, token.asDoctype());

        token = new Token.StartTag();
        assertNull(token.asDoctype());

        token = new Token.EndTag();
        assertNull(token.asDoctype());

        token = new Token.Comment();
        assertNull(token.asDoctype());

        token = new Token.Character("data");
        assertNull(token.asDoctype());

        token = new Token.EOF();
        assertNull(token.asDoctype());
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

        token = new Token.Character("data");
        assertFalse(token.isStartTag());

        token = new Token.EOF();
        assertFalse(token.isStartTag());
    }

    @Test
    public void testAsStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        Token token = startTag;
        assertEquals(startTag, token.asStartTag());

        token = new Token.EndTag();
        assertNull(token.asStartTag());

        token = new Token.Comment();
        assertNull(token.asStartTag());

        token = new Token.Character("data");
        assertNull(token.asStartTag());

        token = new Token.EOF();
        assertNull(token.asStartTag());
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

        token = new Token.Character("data");
        assertFalse(token.isEndTag());

        token = new Token.EOF();
        assertFalse(token.isEndTag());
    }

    @Test
    public void testAsEndTag() {
        Token.EndTag endTag = new Token.EndTag();
        Token token = endTag;
        assertEquals(endTag, token.asEndTag());

        token = new Token.StartTag();
        assertNull(token.asEndTag());

        token = new Token.Comment();
        assertNull(token.asEndTag());

        token = new Token.Character("data");
        assertNull(token.asEndTag());

        token = new Token.EOF();
        assertNull(token.asEndTag());
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

        token = new Token.Character("data");
        assertFalse(token.isComment());

        token = new Token.EOF();
        assertFalse(token.isComment());
    }

    @Test
    public void testAsComment() {
        Token.Comment comment = new Token.Comment();
        Token token = comment;
        assertEquals(comment, token.asComment());

        token = new Token.Doctype();
        assertNull(token.asComment());

        token = new Token.StartTag();
        assertNull(token.asComment());

        token = new Token.EndTag();
        assertNull(token.asComment());

        token = new Token.Character("data");
        assertNull(token.asComment());

        token = new Token.EOF();
        assertNull(token.asComment());
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

        token = new Token.Character("data");
        assertTrue(token.isCharacter());

        token = new Token.EOF();
        assertFalse(token.isCharacter());
    }

    @Test
    public void testAsCharacter() {
        Token.Character character = new Token.Character("data");
        Token token = character;
        assertEquals(character, token.asCharacter());

        token = new Token.Doctype();
        assertNull(token.asCharacter());

        token = new Token.StartTag();
        assertNull(token.asCharacter());

        token = new Token.EndTag();
        assertNull(token.asCharacter());

        token = new Token.Comment();
        assertNull(token.asCharacter());

        token = new Token.EOF();
        assertNull(token.asCharacter());
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

        token = new Token.Character("data");
        assertFalse(token.isEOF());

        token = new Token.EOF();
        assertTrue(token.isEOF());
    }
}