package org.jsoup.parser;

import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.EOF;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TokenTest {

    // Testing Token.Doctype

    @Test
    public void testDoctypeGetName() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("html");
        assertEquals("html", doctype.getName());
    }

    @Test
    public void testDoctypeGetPublicIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.publicIdentifier.append("-//W3C//DTD HTML 4.01//EN");
        assertEquals("-//W3C//DTD HTML 4.01//EN", doctype.getPublicIdentifier());
    }

    @Test
    public void testDoctypeGetSystemIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.systemIdentifier.append("http://www.w3.org/TR/html4/strict.dtd");
        assertEquals("http://www.w3.org/TR/html4/strict.dtd", doctype.getSystemIdentifier());
    }

    @Test
    public void testDoctypeIsForceQuirks() {
        Token.Doctype doctype = new Token.Doctype();
        assertFalse(doctype.isForceQuirks());

        doctype.forceQuirks = true;
        assertTrue(doctype.isForceQuirks());
    }

    // Testing Token.Tag

    @Test
    public void testTagSelfClosing() {
        Token.Tag tag = new Token.StartTag();
        assertFalse(tag.isSelfClosing());

        tag.selfClosing = true;
        assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testTagGetName() {
        Token.Tag tag = new Token.StartTag("div");
        assertEquals("div", tag.name());

        tag.name("span");
        assertEquals("span", tag.name());
    }

    @Test
    public void testTagFinaliseTag() {
        Token.Tag tag = new Token.StartTag();
        assertNull(tag.attributes);

        tag.pendingAttributeName = "id";
        tag.pendingAttributeValue = new StringBuilder("1");
        tag.finaliseTag();

        assertNotNull(tag.attributes);
        assertEquals(1, tag.attributes.size());

        Attribute attribute = tag.attributes.get("id");
        assertNotNull(attribute);
        assertEquals("1", attribute.getValue());
    }

    @Test
    public void testTagNewAttribute() {
        Token.Tag tag = new Token.StartTag();
        assertNull(tag.attributes);

        tag.pendingAttributeName = "class";
        tag.pendingAttributeValue = new StringBuilder("container");
        tag.newAttribute();

        assertNotNull(tag.attributes);
        assertEquals(1, tag.attributes.size());

        Attribute attribute = tag.attributes.get("class");
        assertNotNull(attribute);
        assertEquals("container", attribute.getValue());
    }

    // Testing Token.StartTag

    @Test
    public void testStartTagToString() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("div");
        assertEquals("<div>", startTag.toString());

        startTag.attributes = new Attributes();
        startTag.attributes.put("class", "container");
        startTag.attributes.put("id", "1");
        assertEquals("<div class=\"container\" id=\"1\">", startTag.toString());
    }

    // Testing Token.EndTag

    @Test
    public void testEndTagToString() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.name("div");
        assertEquals("</div>", endTag.toString());
    }

    // Testing Token.Comment

    @Test
    public void testCommentGetData() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("This is a comment");
        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testCommentToString() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("This is a comment");
        assertEquals("<!--This is a comment-->", comment.toString());
    }

    // Testing Token.Character

    @Test
    public void testCharacterGetData() {
        Token.Character character = new Token.Character("This is a character");
        assertEquals("This is a character", character.getData());
    }

    @Test
    public void testCharacterToString() {
        Token.Character character = new Token.Character("This is a character");
        assertEquals("This is a character", character.toString());
    }

    // Testing Token.EOF

    @Test
    public void testEOF() {
        Token.EOF eof = new Token.EOF();
        assertTrue(eof.isEOF());
    }

    // Testing Token class methods

    @Test
    public void testIsDoctype() {
        Token token = new Token.Doctype();
        assertTrue(token.isDoctype());

        assertFalse(token.isStartTag());
        assertFalse(token.isEndTag());
        assertFalse(token.isComment());
        assertFalse(token.isCharacter());
        assertFalse(token.isEOF());
    }

    @Test
    public void testIsStartTag() {
        Token token = new Token.StartTag();
        assertTrue(token.isStartTag());

        assertFalse(token.isDoctype());
        assertFalse(token.isEndTag());
        assertFalse(token.isComment());
        assertFalse(token.isCharacter());
        assertFalse(token.isEOF());
    }

    @Test
    public void testIsEndTag() {
        Token token = new Token.EndTag();
        assertTrue(token.isEndTag());

        assertFalse(token.isDoctype());
        assertFalse(token.isStartTag());
        assertFalse(token.isComment());
        assertFalse(token.isCharacter());
        assertFalse(token.isEOF());
    }

    @Test
    public void testIsComment() {
        Token token = new Token.Comment();
        assertTrue(token.isComment());

        assertFalse(token.isDoctype());
        assertFalse(token.isStartTag());
        assertFalse(token.isEndTag());
        assertFalse(token.isCharacter());
        assertFalse(token.isEOF());
    }

    @Test
    public void testIsCharacter() {
        Token token = new Token.Character("This is a character");
        assertTrue(token.isCharacter());

        assertFalse(token.isDoctype());
        assertFalse(token.isStartTag());
        assertFalse(token.isEndTag());
        assertFalse(token.isComment());
        assertFalse(token.isEOF());
    }

    @Test
    public void testIsEOF() {
        Token token = new Token.EOF();
        assertTrue(token.isEOF());

        assertFalse(token.isDoctype());
        assertFalse(token.isStartTag());
        assertFalse(token.isEndTag());
        assertFalse(token.isComment());
        assertFalse(token.isCharacter());
    }
}