package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctype() {
        Token docType = new Token.Doctype();
        Token docTypeWithName = new Token.Doctype();
        docTypeWithName.name("html");
        Token docTypeWithForceQuirks = new Token.Doctype();
        docTypeWithForceQuirks.forceQuirks(true);

        assertEquals("Doctype", docType.tokenType());
        assertEquals("Doctype", docTypeWithName.tokenType());
        assertEquals("Doctype", docTypeWithForceQuirks.tokenType());

        assertEquals(null, docType.getName());
        assertEquals("html", docTypeWithName.getName());
        assertFalse(docTypeWithForceQuirks.isForceQuirks());
        assertTrue(docTypeWithForceQuirks.forceQuirks);
    }

    @Test
    public void testTag() {
        Token tag = new Token.StartTag();
        Token tagWithName = new Token.StartTag();
        tagWithName.name("div");

        assertEquals("StartTag", tag.tokenType());
        assertEquals("StartTag", tagWithName.tokenType());
        assertEquals("div", tagWithName.name());

        assertNull(tag.tagName);
        assertEquals("div", tagWithName.tagName);
        assertFalse(tag.isSelfClosing());
        assertFalse(tagWithName.isSelfClosing());

        Token tagWithAttributes = new Token.StartTag();
        tagWithAttributes.name("img");
        tagWithAttributes.attributes = new org.jsoup.nodes.Attributes();
        tagWithAttributes.newAttribute();
        tagWithAttributes.appendAttributeName("src");
        tagWithAttributes.appendAttributeValue("image.jpg");
        tagWithAttributes.newAttribute();
        tagWithAttributes.appendAttributeName("alt");
        tagWithAttributes.appendAttributeValue("Image description");

        assertTrue(tagWithAttributes.isStartTag());
        assertTrue(tagWithAttributes.is("img"));
        assertEquals(2, tagWithAttributes.getAttributes().size());
        assertEquals("src", tagWithAttributes.getAttributes().getValue("src"));
        assertEquals("Image description", tagWithAttributes.getAttributes().getValue("alt"));
    }

    @Test
    public void testEndTag() {
        Token endTag = new Token.EndTag();
        Token endTagWithName = new Token.EndTag();
        endTagWithName.name("div");

        assertEquals("EndTag", endTag.tokenType());
        assertEquals("EndTag", endTagWithName.tokenType());
        assertEquals("div", endTagWithName.name());

        assertNull(endTag.tagName);
        assertEquals("div", endTagWithName.tagName);
        assertFalse(endTag.isSelfClosing());
        assertFalse(endTagWithName.isSelfClosing());
    }

    @Test
    public void testComment() {
        Token comment = new Token.Comment();
        Token commentWithData = new Token.Comment();
        commentWithData.data("This is a comment");

        assertEquals("Comment", comment.tokenType());
        assertEquals("Comment", commentWithData.tokenType());
        assertEquals("", comment.getData());
        assertEquals("This is a comment", commentWithData.getData());

        assertTrue(comment.isComment());
        assertTrue(commentWithData.isComment());
    }

    @Test
    public void testCharacter() {
        Token character = new Token.Character("A");

        assertEquals("Character", character.tokenType());
        assertEquals("A", character.getData());
        assertTrue(character.isCharacter());
    }

    @Test
    public void testEOF() {
        Token eof = new Token.EOF();

        assertEquals("EOF", eof.tokenType());
        assertFalse(eof.isDoctype());
        assertFalse(eof.isStartTag());
        assertFalse(eof.isEndTag());
        assertFalse(eof.isComment());
        assertFalse(eof.isCharacter());
    }

    @Test
    public void testTypeChecking() {
        Token tag = new Token.StartTag();
        tag.name("div");

        assertTrue(tag.isStartTag());
        assertTrue(tag.is("div"));
        assertFalse(tag.isDoctype());
        assertFalse(tag.isEndTag());
        assertFalse(tag.isComment());
        assertFalse(tag.isCharacter());

        Token comment = new Token.Comment();

        assertFalse(comment.isStartTag());
        assertFalse(comment.is("div"));
        assertFalse(comment.isDoctype());
        assertTrue(comment.isComment());
        assertFalse(comment.isCharacter());

        Token character = new Token.Character('A');

        assertFalse(character.isStartTag());
        assertFalse(character.is("div"));
        assertFalse(character.isDoctype());
        assertFalse(character.isComment());
        assertTrue(character.isCharacter());

        Token eof = new Token.EOF();

        assertFalse(eof.isStartTag());
        assertFalse(eof.is("div"));
        assertFalse(eof.isDoctype());
        assertFalse(eof.isComment());
        assertFalse(eof.isCharacter());
        assertTrue(eof.isEOF());
    }
}